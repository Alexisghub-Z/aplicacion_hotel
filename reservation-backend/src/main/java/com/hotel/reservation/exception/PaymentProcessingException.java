package com.hotel.reservation.exception;

/**
 * Excepción lanzada cuando ocurre un error al procesar un pago.
 * Puede ser por problemas con el gateway de pago, fondos insuficientes,
 * tarjeta rechazada, etc.
 */
public class PaymentProcessingException extends RuntimeException {

    private final String paymentMethod;
    private final String errorCode;

    public PaymentProcessingException(String message, String paymentMethod, String errorCode) {
        super(message);
        this.paymentMethod = paymentMethod;
        this.errorCode = errorCode;
    }

    public PaymentProcessingException(String message) {
        super(message);
        this.paymentMethod = null;
        this.errorCode = null;
    }

    public PaymentProcessingException(String message, Throwable cause) {
        super(message, cause);
        this.paymentMethod = null;
        this.errorCode = null;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
