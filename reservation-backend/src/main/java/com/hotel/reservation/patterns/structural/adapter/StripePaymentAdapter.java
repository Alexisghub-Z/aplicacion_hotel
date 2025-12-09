package com.hotel.reservation.patterns.structural.adapter;

import com.hotel.reservation.models.Payment;
import org.springframework.stereotype.Component;

/**
 * PATRÓN ADAPTER - Stripe Payment Adapter
 *
 * Adapta la API de Stripe a nuestra interfaz común de pago
 * En producción, esto usaría la librería oficial de Stripe
 */
@Component
public class StripePaymentAdapter implements PaymentGatewayAdapter {

    @Override
    public PaymentResult processPayment(Payment payment) {
        try {
            // Simulación de llamada a la API de Stripe
            // En producción: Stripe.apiKey = "sk_test_...";
            // Charge charge = Charge.create(params);

            String transactionId = generateStripeTransactionId();

            // Simular procesamiento exitoso (95% de éxito)
            boolean success = Math.random() > 0.05;

            if (success) {
                return PaymentResult.success(
                    transactionId,
                    "Pago procesado exitosamente via Stripe"
                );
            } else {
                return PaymentResult.failure(
                    "Fondos insuficientes",
                    "STRIPE_INSUFFICIENT_FUNDS"
                );
            }
        } catch (Exception e) {
            return PaymentResult.failure(
                "Error al procesar el pago: " + e.getMessage(),
                "STRIPE_API_ERROR"
            );
        }
    }

    @Override
    public PaymentResult refund(Payment payment) {
        try {
            // Simulación de reembolso en Stripe
            // Refund refund = Refund.create(params);

            String refundId = "re_stripe_" + System.currentTimeMillis();

            return PaymentResult.success(
                refundId,
                "Reembolso procesado exitosamente via Stripe"
            );
        } catch (Exception e) {
            return PaymentResult.failure(
                "Error al procesar el reembolso: " + e.getMessage(),
                "STRIPE_REFUND_ERROR"
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

    private String generateStripeTransactionId() {
        return "ch_stripe_" + System.currentTimeMillis();
    }
}
