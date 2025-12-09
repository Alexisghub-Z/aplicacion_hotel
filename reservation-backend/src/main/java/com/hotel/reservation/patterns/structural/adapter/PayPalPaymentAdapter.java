package com.hotel.reservation.patterns.structural.adapter;

import com.hotel.reservation.models.Payment;
import org.springframework.stereotype.Component;

/**
 * PATRÓN ADAPTER - PayPal Payment Adapter
 *
 * Adapta la API de PayPal a nuestra interfaz común de pago
 * En producción, esto usaría el SDK oficial de PayPal
 */
@Component
public class PayPalPaymentAdapter implements PaymentGatewayAdapter {

    @Override
    public PaymentResult processPayment(Payment payment) {
        try {
            // Simulación de llamada a la API de PayPal
            // En producción: PayPalClient.execute(order);

            String transactionId = generatePayPalTransactionId();

            // Simular procesamiento exitoso (90% de éxito)
            boolean success = Math.random() > 0.10;

            if (success) {
                return PaymentResult.success(
                    transactionId,
                    "Pago procesado exitosamente via PayPal"
                );
            } else {
                return PaymentResult.failure(
                    "Cuenta PayPal no verificada",
                    "PAYPAL_ACCOUNT_UNVERIFIED"
                );
            }
        } catch (Exception e) {
            return PaymentResult.failure(
                "Error al procesar el pago: " + e.getMessage(),
                "PAYPAL_API_ERROR"
            );
        }
    }

    @Override
    public PaymentResult refund(Payment payment) {
        try {
            // Simulación de reembolso en PayPal

            String refundId = "pp_refund_" + System.currentTimeMillis();

            return PaymentResult.success(
                refundId,
                "Reembolso procesado exitosamente via PayPal"
            );
        } catch (Exception e) {
            return PaymentResult.failure(
                "Error al procesar el reembolso: " + e.getMessage(),
                "PAYPAL_REFUND_ERROR"
            );
        }
    }

    @Override
    public PaymentResult checkStatus(String transactionId) {
        // Simular verificación de estado
        return PaymentResult.success(
            transactionId,
            "Transacción completada"
        );
    }

    private String generatePayPalTransactionId() {
        return "pp_" + System.currentTimeMillis();
    }
}
