package com.hotel.reservation.patterns.behavioral.observer;

import com.hotel.reservation.models.Reservation;

/**
 * PATRÓN OBSERVER - Observer Interface
 */
public interface ReservationObserver {
    void onReservationCreated(Reservation reservation);
    void onReservationConfirmed(Reservation reservation);
    void onReservationCancelled(Reservation reservation);
}
