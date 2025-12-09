package com.hotel.reservation.patterns.behavioral.state;

import com.hotel.reservation.models.Reservation;

/**
 * PATRÓN STATE - ReservationState Interface
 *
 * Estados: PENDING → CONFIRMED → COMPLETED
 *                  ↓
 *              CANCELLED
 */
public interface ReservationState {
    void confirm(Reservation reservation);
    void cancel(Reservation reservation);
    void complete(Reservation reservation);
    String getStateName();
}
