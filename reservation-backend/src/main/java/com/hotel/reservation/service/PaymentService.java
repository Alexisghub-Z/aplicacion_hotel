package com.hotel.reservation.service;

import com.hotel.reservation.dto.PaymentDTO;
import com.hotel.reservation.exception.PaymentProcessingException;
import com.hotel.reservation.exception.ResourceNotFoundException;
import com.hotel.reservation.models.Payment;
import com.hotel.reservation.models.PaymentMethod;
import com.hotel.reservation.models.PaymentStatus;
import com.hotel.reservation.models.Reservation;
import com.hotel.reservation.patterns.behavioral.observer.EmailNotificationObserver;
import com.hotel.reservation.patterns.creational.singleton.ConfigurationManager;
import com.hotel.reservation.patterns.structural.adapter.CashPaymentAdapter;
import com.hotel.reservation.patterns.structural.adapter.PayPalPaymentAdapter;
import com.hotel.reservation.patterns.structural.adapter.PaymentGatewayAdapter;
import com.hotel.reservation.patterns.structural.adapter.StripePaymentAdapter;
import com.hotel.reservation.repositories.PaymentRepository;
import com.hotel.reservation.repositories.ReservationRepository;
import com.hotel.reservation.repositories.NotificationPreferenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Servicio de negocio para gestión de pagos.
 *
 * Integra:
 * - Adapter Pattern: Adapta diferentes gateways de pago a una interfaz común
 * - Singleton Pattern: Usa configuración global para tasas y moneda
 *
 * Responsabilidades:
 * - Procesar pagos usando diferentes métodos
 * - Gestionar estados de pago
 * - Reembolsos
 * - Conversión entre Entity y DTO
 */
@Service
@RequiredArgsConstructor
@Transactional
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final ReservationRepository reservationRepository;
    private final NotificationPreferenceRepository notificationPreferenceRepository;
    private final EmailNotificationObserver emailObserver;

    // Adapters para diferentes gateways de pago
    private final PaymentGatewayAdapter stripeAdapter = new StripePaymentAdapter();
    private final PaymentGatewayAdapter paypalAdapter = new PayPalPaymentAdapter();
    private final PaymentGatewayAdapter cashAdapter = new CashPaymentAdapter();

    /**
     * Procesar un pago para una reserva.
     * Usa Adapter Pattern para procesar con el gateway apropiado.
     */
    public PaymentDTO processPayment(Long reservationId, PaymentMethod paymentMethod) {
        // Buscar reserva
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", "id", reservationId));

        // Verificar que no exista ya un pago completado para esta reserva
        Optional<Payment> existingPayment = paymentRepository.findByReservationId(reservationId);
        if (existingPayment.isPresent() && existingPayment.get().getPaymentStatus() == PaymentStatus.COMPLETED) {
            throw new PaymentProcessingException("La reserva ya tiene un pago completado");
        }

        // Calcular monto con impuestos usando Singleton
        ConfigurationManager config = ConfigurationManager.INSTANCE;
        BigDecimal amount = reservation.getTotalPrice();
        // Aplicar tasa de impuesto manualmente ya que no hay método applyTax
        BigDecimal taxRate = BigDecimal.valueOf(config.getTaxRate());
        BigDecimal amountWithTax = amount.add(amount.multiply(taxRate));

        // Crear registro de pago
        Payment payment = Payment.builder()
                .reservation(reservation)
                .amount(amountWithTax)
                .paymentMethod(paymentMethod)
                .paymentStatus(PaymentStatus.PENDING)
                .build();

        // Seleccionar adapter y procesar pago
        PaymentGatewayAdapter adapter = selectPaymentAdapter(paymentMethod);
        PaymentGatewayAdapter.PaymentResult result = adapter.processPayment(payment);

        if (result.isSuccess()) {
            payment.markAsCompleted(result.getTransactionId());

            // Actualizar estado de la reservación a CONFIRMED
            reservation.setStatus(com.hotel.reservation.models.ReservationStatus.CONFIRMED);
            reservationRepository.save(reservation);

            // Notificar al cliente sobre el pago exitoso
            notifyPaymentCompleted(reservation);
        } else {
            payment.markAsFailed();
            throw new PaymentProcessingException(
                    result.getMessage(),
                    paymentMethod.name(),
                    result.getErrorCode()
            );
        }

        Payment savedPayment = paymentRepository.save(payment);
        return convertToDTO(savedPayment);
    }

    /**
     * Reembolsar un pago.
     */
    public PaymentDTO refundPayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", "id", paymentId));

        if (payment.getPaymentStatus() != PaymentStatus.COMPLETED) {
            throw new PaymentProcessingException("Solo se pueden reembolsar pagos completados");
        }

        // Seleccionar adapter y procesar reembolso
        PaymentGatewayAdapter adapter = selectPaymentAdapter(payment.getPaymentMethod());
        PaymentGatewayAdapter.PaymentResult result = adapter.refund(payment);

        if (result.isSuccess()) {
            payment.markAsRefunded();
            Payment refundedPayment = paymentRepository.save(payment);
            return convertToDTO(refundedPayment);
        } else {
            throw new PaymentProcessingException(result.getMessage());
        }
    }

    /**
     * Obtener pago por ID.
     */
    @Transactional(readOnly = true)
    public PaymentDTO getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", "id", id));
        return convertToDTO(payment);
    }

    /**
     * Obtener pagos por reserva.
     */
    @Transactional(readOnly = true)
    public List<PaymentDTO> getPaymentsByReservation(Long reservationId) {
        return paymentRepository.findByReservationId(reservationId)
                .map(payment -> List.of(convertToDTO(payment)))
                .orElse(List.of());
    }

    /**
     * Obtener pago por ID de transacción.
     */
    @Transactional(readOnly = true)
    public PaymentDTO getPaymentByTransactionId(String transactionId) {
        Payment payment = paymentRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", "transactionId", transactionId));
        return convertToDTO(payment);
    }

    /**
     * Verificar estado de un pago.
     */
    @Transactional(readOnly = true)
    public PaymentStatus checkPaymentStatus(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", "id", paymentId));
        return payment.getPaymentStatus();
    }

    /**
     * Notificar al cliente cuando el pago es completado exitosamente
     */
    private void notifyPaymentCompleted(Reservation reservation) {
        com.hotel.reservation.models.NotificationPreference preferences =
            notificationPreferenceRepository.findByCustomerId(reservation.getCustomer().getId())
                .orElse(null);

        // Si el cliente tiene email habilitado o no tiene preferencias (por defecto email activo)
        if (preferences == null || preferences.getEmailEnabled()) {
            emailObserver.onReservationConfirmed(reservation);
        }
    }

    /**
     * Seleccionar el adapter apropiado según el método de pago.
     * Implementación del Adapter Pattern.
     */
    private PaymentGatewayAdapter selectPaymentAdapter(PaymentMethod paymentMethod) {
        return switch (paymentMethod) {
            case CREDIT_CARD -> stripeAdapter;
            case PAYPAL -> paypalAdapter;
            case CASH -> cashAdapter;
        };
    }

    /**
     * Generar un ID de transacción único.
     */
    private String generateTransactionId() {
        return "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    /**
     * Convertir Payment entity a PaymentDTO.
     */
    private PaymentDTO convertToDTO(Payment payment) {
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("es", "MX"));
        String formattedAmount = currencyFormatter.format(payment.getAmount());

        return PaymentDTO.builder()
                .id(payment.getId())
                .reservationId(payment.getReservation().getId())
                .amount(payment.getAmount())
                .paymentMethod(payment.getPaymentMethod())
                .paymentStatus(payment.getPaymentStatus())
                .transactionId(payment.getTransactionId())
                .paymentDate(payment.getPaymentDate())
                .formattedAmount(formattedAmount)
                .build();
    }
}
