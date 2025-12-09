package com.hotel.reservation.exception;

/**
 * Excepción lanzada cuando se intenta crear o modificar una reserva con datos inválidos.
 * Ejemplos:
 * - Fecha de check-out anterior a check-in
 * - Número de huéspedes excede la capacidad de la habitación
 * - Fechas en el pasado
 */
public class InvalidReservationException extends RuntimeException {

    private final String validationError;

    public InvalidReservationException(String message) {
        super(message);
        this.validationError = message;
    }

    public InvalidReservationException(String message, Throwable cause) {
        super(message, cause);
        this.validationError = message;
    }

    public String getValidationError() {
        return validationError;
    }
}
