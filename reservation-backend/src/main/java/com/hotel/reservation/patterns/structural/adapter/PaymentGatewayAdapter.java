package com.hotel.reservation.patterns.structural.adapter;

import com.hotel.reservation.models.Payment;

/**
 * PATRÓN ADAPTER - Payment Gateway Adapter Interface
 *
 * Propósito: Define una interfaz común para diferentes pasarelas de pago
 * permitiendo que el sistema trabaje con múltiples proveedores de pago
 * sin cambiar el código cliente.
 *
 * Pasarelas soportadas:
 * - Stripe
 * - PayPal
 * - Cash (pago en efectivo en recepción)
 *
 * Beneficios:
 * - Interfaz uniforme para diferentes sistemas de pago
 * - Fácil agregar nuevas pasarelas
 * - Desacopla el código de los detalles de implementación de cada pasarela
 */
public interface PaymentGatewayAdapter {

    /**
     * Procesa un pago
     * @param payment datos del pago
     * @return resultado del procesamiento
     */
    PaymentResult processPayment(Payment payment);

    /**
     * Procesa un reembolso
     * @param payment pago a reembolsar
     * @return resultado del reembolso
     */
    PaymentResult refund(Payment payment);

    /**
     * Verifica el estado de una transacción
     * @param transactionId ID de la transacción
     * @return resultado de la verificación
     */
    PaymentResult checkStatus(String transactionId);

    /**
     * Clase para encapsular el resultado de una operación de pago
     */
    class PaymentResult {
        private final boolean success;
        private final String transactionId;
        private final String message;
        private final String errorCode;

        public PaymentResult(boolean success, String transactionId, String message, String errorCode) {
            this.success = success;
            this.transactionId = transactionId;
            this.message = message;
            this.errorCode = errorCode;
        }

        public static PaymentResult success(String transactionId, String message) {
            return new PaymentResult(true, transactionId, message, null);
        }

        public static PaymentResult failure(String message, String errorCode) {
            return new PaymentResult(false, null, message, errorCode);
        }

        public boolean isSuccess() {
            return success;
        }

        public String getTransactionId() {
            return transactionId;
        }

        public String getMessage() {
            return message;
        }

        public String getErrorCode() {
            return errorCode;
        }
    }
}
