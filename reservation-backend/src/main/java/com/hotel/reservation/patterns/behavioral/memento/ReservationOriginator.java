package com.hotel.reservation.patterns.behavioral.memento;

import com.hotel.reservation.models.Reservation;
import com.hotel.reservation.models.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Originator - Objeto que crea y restaura mementos
 */
@Slf4j
@Data
@AllArgsConstructor
public class ReservationOriginator {

    private Long id;
    private ReservationStatus status;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Integer numberOfGuests;
    private BigDecimal totalPrice;

    public ReservationOriginator(Reservation reservation) {
        this.id = reservation.getId();
        this.status = reservation.getStatus();
        this.checkInDate = reservation.getCheckInDate();
        this.checkOutDate = reservation.getCheckOutDate();
        this.numberOfGuests = reservation.getNumberOfGuests();
        this.totalPrice = reservation.getTotalPrice();
    }

    public ReservationMemento saveToMemento() {
        log.info("💾 Guardando estado de reserva #{}", id);
        return new ReservationMemento(
            id,
            status,
            checkInDate,
            checkOutDate,
            numberOfGuests,
            totalPrice,
            LocalDateTime.now()
        );
    }

    public void restoreFromMemento(ReservationMemento memento) {
        log.info("↩️ Restaurando estado de reserva #{} desde {}", id, memento.getSavedAt());
        this.status = memento.getStatus();
        this.checkInDate = memento.getCheckInDate();
        this.checkOutDate = memento.getCheckOutDate();
        this.numberOfGuests = memento.getNumberOfGuests();
        this.totalPrice = memento.getTotalPrice();
    }

    public void applyToReservation(Reservation reservation) {
        reservation.setStatus(this.status);
        reservation.setCheckInDate(this.checkInDate);
        reservation.setCheckOutDate(this.checkOutDate);
        reservation.setNumberOfGuests(this.numberOfGuests);
        reservation.setTotalPrice(this.totalPrice);
    }
}
