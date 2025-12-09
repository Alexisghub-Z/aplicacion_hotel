package com.hotel.reservation.patterns.behavioral.state;

import com.hotel.reservation.models.Reservation;
import com.hotel.reservation.models.ReservationStatus;

public class ConfirmedState implements ReservationState {
    @Override
    public void confirm(Reservation reservation) {
        // Ya está confirmada
    }

    @Override
    public void cancel(Reservation reservation) {
        reservation.setStatus(ReservationStatus.CANCELLED);
    }

    @Override
    public void complete(Reservation reservation) {
        reservation.setStatus(ReservationStatus.COMPLETED);
    }

    @Override
    public String getStateName() {
        return "CONFIRMED";
    }
}
