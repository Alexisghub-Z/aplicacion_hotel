package com.hotel.reservation.patterns.structural.decorator;

import com.hotel.reservation.models.Reservation;

import java.math.BigDecimal;

/**
 * PATRÓN DECORATOR - Concrete Component
 *
 * Implementación base de una reserva sin servicios adicionales
 */
public class BaseReservation implements ReservationComponent {

    private final Reservation reservation;

    public BaseReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    @Override
    public BigDecimal getPrice() {
        return reservation.getTotalPrice();
    }

    @Override
    public String getDescription() {
        return String.format("Reserva #%d - Habitación %s - %d noches",
            reservation.getId() != null ? reservation.getId() : 0,
            reservation.getRoom().getRoomNumber(),
            reservation.getNumberOfNights());
    }

    public Reservation getReservation() {
        return reservation;
    }
}
