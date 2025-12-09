package com.hotel.reservation.patterns.behavioral.chain;

import com.hotel.reservation.dto.ReservationDTO;

/**
 * Chain of Responsibility Pattern - Handler base para validaciones
 * Permite encadenar validaciones de forma flexible
 */
public abstract class ValidationHandler {

    protected ValidationHandler next;

    public ValidationHandler setNext(ValidationHandler next) {
        this.next = next;
        return next;
    }

    public abstract void validate(ReservationDTO reservation);

    protected void validateNext(ReservationDTO reservation) {
        if (next != null) {
            next.validate(reservation);
        }
    }
}
