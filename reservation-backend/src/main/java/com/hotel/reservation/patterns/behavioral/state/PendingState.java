package com.hotel.reservation.patterns.behavioral.state;

import com.hotel.reservation.exception.InvalidStateTransitionException;
import com.hotel.reservation.models.Reservation;
import com.hotel.reservation.models.ReservationStatus;

public class PendingState implements ReservationState {
    @Override
    public void confirm(Reservation reservation) {
        reservation.setStatus(ReservationStatus.CONFIRMED);
    }

    @Override
    public void cancel(Reservation reservation) {
        reservation.setStatus(ReservationStatus.CANCELLED);
    }

    @Override
    public void complete(Reservation reservation) {
        throw new InvalidStateTransitionException("PENDING", "COMPLETED",
            "No se puede completar una reserva pendiente. Debe confirmarse primero.");
    }

    @Override
    public String getStateName() {
        return "PENDING";
    }
}
