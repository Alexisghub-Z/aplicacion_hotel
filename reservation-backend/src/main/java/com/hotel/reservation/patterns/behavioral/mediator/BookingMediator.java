package com.hotel.reservation.patterns.behavioral.mediator;

import com.hotel.reservation.dto.ReservationDTO;
import com.hotel.reservation.models.Customer;
import com.hotel.reservation.models.Room;

/**
 * Mediator Pattern - Interfaz mediadora para coordinar reservas
 * Coordina la comunicación entre componentes del sistema de reservas
 */
public interface BookingMediator {
    void notifyRoomBooked(Room room, Customer customer);
    void notifyRoomReleased(Room room);
    void notifyPaymentProcessed(Long reservationId, String paymentMethod);
    ReservationDTO processBooking(ReservationDTO reservationDTO);
}
