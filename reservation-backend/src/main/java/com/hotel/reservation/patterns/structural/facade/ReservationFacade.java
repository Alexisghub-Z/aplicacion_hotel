package com.hotel.reservation.patterns.structural.facade;

import com.hotel.reservation.models.*;
import com.hotel.reservation.patterns.creational.builder.ReservationBuilder;
import com.hotel.reservation.repositories.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * PATRÓN FACADE - ReservationFacade
 *
 * Propósito: Simplifica el proceso completo de crear una reserva
 * coordinando múltiples subsistemas:
 * - Validación de datos
 * - Verificación de disponibilidad
 * - Creación de reserva
 * - Procesamiento de pago
 * - Notificaciones
 *
 * Beneficios:
 * - Interfaz simple para operación compleja
 * - Oculta la complejidad de los subsistemas
 * - Reduce acoplamiento entre cliente y subsistemas
 * - Centraliza la lógica de negocio
 *
 * Ejemplo de uso:
 * ReservationResult result = facade.createCompleteReservation(
 *     customerEmail, roomId, checkIn, checkOut, guests, serviceIds, paymentMethod
 * );
 */
@Component
public class ReservationFacade {

    private final CustomerRepository customerRepository;
    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;
    private final AdditionalServiceRepository serviceRepository;
    private final PaymentRepository paymentRepository;

    public ReservationFacade(CustomerRepository customerRepository,
                            RoomRepository roomRepository,
                            ReservationRepository reservationRepository,
                            AdditionalServiceRepository serviceRepository,
                            PaymentRepository paymentRepository) {
        this.customerRepository = customerRepository;
        this.roomRepository = roomRepository;
        this.reservationRepository = reservationRepository;
        this.serviceRepository = serviceRepository;
        this.paymentRepository = paymentRepository;
    }

    /**
     * Crea una reserva completa coordinando todos los subsistemas
     *
     * @param customerEmail email del cliente
     * @param roomId ID de la habitación
     * @param checkInDate fecha de entrada
     * @param checkOutDate fecha de salida
     * @param numberOfGuests número de huéspedes
     * @param serviceIds lista de IDs de servicios adicionales
     * @param paymentMethod método de pago
     * @return resultado con la reserva y el pago
     * @throws IllegalArgumentException si hay errores de validación
     */
    public ReservationResult createCompleteReservation(
            String customerEmail,
            Long roomId,
            LocalDate checkInDate,
            LocalDate checkOutDate,
            Integer numberOfGuests,
            List<Long> serviceIds,
            PaymentMethod paymentMethod) {

        // PASO 1: Validar y obtener cliente
        Customer customer = customerRepository.findByEmail(customerEmail)
            .orElseThrow(() -> new IllegalArgumentException(
                "Cliente no encontrado con email: " + customerEmail));

        // PASO 2: Validar y obtener habitación
        Room room = roomRepository.findById(roomId)
            .orElseThrow(() -> new IllegalArgumentException(
                "Habitación no encontrada con ID: " + roomId));

        // PASO 3: Verificar disponibilidad
        if (!room.isAvailable()) {
            throw new IllegalArgumentException(
                "La habitación " + room.getRoomNumber() + " no está disponible");
        }

        // Verificar que no haya reservas en esas fechas
        List<Room> availableRooms = roomRepository.findAvailableRooms(checkInDate, checkOutDate);
        boolean isRoomAvailable = availableRooms.stream()
            .anyMatch(r -> r.getId().equals(roomId));

        if (!isRoomAvailable) {
            throw new IllegalArgumentException(
                "La habitación no está disponible para las fechas seleccionadas");
        }

        // PASO 4: Obtener servicios adicionales
        List<AdditionalService> services = serviceIds != null && !serviceIds.isEmpty()
            ? serviceRepository.findAllById(serviceIds)
            : List.of();

        // PASO 5: Construir reserva usando Builder Pattern
        Reservation reservation = new ReservationBuilder()
            .withCustomer(customer)
            .withRoom(room)
            .withDates(checkInDate, checkOutDate)
            .withGuests(numberOfGuests)
            .withServices(services)
            .withStatus(ReservationStatus.PENDING)
            .build();

        // PASO 6: Guardar reserva
        Reservation savedReservation = reservationRepository.save(reservation);

        // PASO 7: Crear y procesar pago
        Payment payment = Payment.builder()
            .reservation(savedReservation)
            .amount(savedReservation.getTotalPrice())
            .paymentMethod(paymentMethod)
            .paymentStatus(PaymentStatus.PENDING)
            .build();

        // Simular procesamiento de pago
        boolean paymentSuccessful = processPayment(payment);

        if (paymentSuccessful) {
            payment.markAsCompleted("TXN-" + System.currentTimeMillis());
            savedReservation.setStatus(ReservationStatus.CONFIRMED);
            reservationRepository.save(savedReservation);
        } else {
            payment.markAsFailed();
            savedReservation.setStatus(ReservationStatus.CANCELLED);
            reservationRepository.save(savedReservation);
        }

        Payment savedPayment = paymentRepository.save(payment);

        // PASO 8: Marcar habitación como ocupada (si el pago fue exitoso)
        if (paymentSuccessful) {
            room.markAsOccupied();
            roomRepository.save(room);
        }

        // PASO 9: Enviar notificaciones (implementado en Observer Pattern en Fase 3)
        // notificationService.notifyReservationCreated(savedReservation);

        // PASO 10: Retornar resultado
        return new ReservationResult(
            savedReservation,
            savedPayment,
            paymentSuccessful,
            paymentSuccessful ? "Reserva creada exitosamente" : "Error en el pago"
        );
    }

    /**
     * Simula el procesamiento de pago
     * En producción, esto llamaría a una pasarela de pago real (Adapter Pattern)
     *
     * @param payment pago a procesar
     * @return true si el pago fue exitoso
     */
    private boolean processPayment(Payment payment) {
        // Simulación: en producción usaríamos Adapter Pattern para integrar pasarelas reales
        // Por ahora, asumimos que todos los pagos son exitosos
        return true;
    }

    /**
     * Cancela una reserva y procesa el reembolso
     *
     * @param reservationId ID de la reserva
     * @return true si se canceló exitosamente
     */
    public boolean cancelReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
            .orElseThrow(() -> new IllegalArgumentException(
                "Reserva no encontrada con ID: " + reservationId));

        if (!reservation.canBeCancelled()) {
            throw new IllegalStateException(
                "La reserva no puede ser cancelada en su estado actual: " + reservation.getStatus());
        }

        // Cambiar estado de la reserva
        reservation.setStatus(ReservationStatus.CANCELLED);
        reservationRepository.save(reservation);

        // Liberar habitación
        Room room = reservation.getRoom();
        room.markAsAvailable();
        roomRepository.save(room);

        // Procesar reembolso
        paymentRepository.findByReservationId(reservationId).ifPresent(payment -> {
            if (payment.canBeRefunded()) {
                payment.markAsRefunded();
                paymentRepository.save(payment);
            }
        });

        // Notificar cancelación
        // notificationService.notifyReservationCancelled(reservation);

        return true;
    }

    /**
     * Clase interna para encapsular el resultado de la operación
     */
    public static class ReservationResult {
        private final Reservation reservation;
        private final Payment payment;
        private final boolean success;
        private final String message;

        public ReservationResult(Reservation reservation, Payment payment, boolean success, String message) {
            this.reservation = reservation;
            this.payment = payment;
            this.success = success;
            this.message = message;
        }

        public Reservation getReservation() {
            return reservation;
        }

        public Payment getPayment() {
            return payment;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }
    }
}
