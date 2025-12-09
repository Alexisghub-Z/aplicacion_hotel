package com.hotel.reservation.patterns.structural.decorator;

import java.math.BigDecimal;

/**
 * PATRÓN DECORATOR - Abstract Decorator
 *
 * Clase base para todos los decoradores de servicios
 * Envuelve un componente de reserva y añade funcionalidad adicional
 */
public abstract class ReservationDecorator implements ReservationComponent {

    protected ReservationComponent wrappedReservation;

    public ReservationDecorator(ReservationComponent reservation) {
        this.wrappedReservation = reservation;
    }

    @Override
    public BigDecimal getPrice() {
        return wrappedReservation.getPrice();
    }

    @Override
    public String getDescription() {
        return wrappedReservation.getDescription();
    }
}
