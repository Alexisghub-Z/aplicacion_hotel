package com.hotel.reservation.exception;

/**
 * Excepción lanzada cuando se intenta realizar una transición de estado inválida.
 * Ejemplo: Intentar cancelar una reserva que ya está completada,
 * o confirmar un pago que ya está completado.
 */
public class InvalidStateTransitionException extends RuntimeException {

    private final String currentState;
    private final String targetState;

    public InvalidStateTransitionException(String currentState, String targetState, String message) {
        super(String.format("Transición inválida de %s a %s: %s", currentState, targetState, message));
        this.currentState = currentState;
        this.targetState = targetState;
    }

    public InvalidStateTransitionException(String message) {
        super(message);
        this.currentState = null;
        this.targetState = null;
    }

    public String getCurrentState() {
        return currentState;
    }

    public String getTargetState() {
        return targetState;
    }
}
