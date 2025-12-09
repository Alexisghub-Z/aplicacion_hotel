package com.hotel.reservation.patterns.structural.adapter;

import com.hotel.reservation.models.Payment;
import org.springframework.stereotype.Component;

/**
 * PATRÓN ADAPTER - Cash Payment Adapter
 *
 * Maneja pagos en efectivo realizados directamente en la recepción del hotel
 */
@Component
public class CashPaymentAdapter implements PaymentGatewayAdapter {

    @Override
    public PaymentResult processPayment(Payment payment) {
        // Para pagos en efectivo, se procesa cuando el huésped llega
        String transactionId = generateCashTransactionId();

        return PaymentResult.success(
            transactionId,
            "Pago en efectivo registrado. Será procesado en recepción."
        );
    }

    @Override
    public PaymentResult refund(Payment payment) {
        // Reembolso en efectivo
        String refundId = "cash_refund_" + System.currentTimeMillis();

        return PaymentResult.success(
            refundId,
            "Reembolso en efectivo aprobado. Disponible en recepción."
        );
    }

    @Override
    public PaymentResult checkStatus(String transactionId) {
        return PaymentResult.success(
            transactionId,
            "Pago en efectivo pendiente de confirmación en recepción"
        );
    }

    private String generateCashTransactionId() {
        return "cash_" + System.currentTimeMillis();
    }
}
