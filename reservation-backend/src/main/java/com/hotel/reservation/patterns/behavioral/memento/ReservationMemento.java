package com.hotel.reservation.patterns.behavioral.memento;

import com.hotel.reservation.models.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Memento Pattern - Memento que guarda el estado de una reserva
 * Permite restaurar estados anteriores de una reserva
 */
@Getter
@AllArgsConstructor
public class ReservationMemento {

    private final Long reservationId;
    private final ReservationStatus status;
    private final LocalDate checkInDate;
    private final LocalDate checkOutDate;
    private final Integer numberOfGuests;
    private final BigDecimal totalPrice;
    private final LocalDateTime savedAt;

    public String getStateDescription() {
        return String.format("Reserva #%d - Estado: %s - Total: $%.2f - Guardado: %s",
            reservationId, status, totalPrice, savedAt);
    }
}
