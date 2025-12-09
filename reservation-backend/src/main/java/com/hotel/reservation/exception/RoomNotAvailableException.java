package com.hotel.reservation.exception;

import java.time.LocalDate;

/**
 * Excepción lanzada cuando se intenta reservar una habitación que no está disponible
 * en las fechas solicitadas.
 */
public class RoomNotAvailableException extends RuntimeException {

    private final Long roomId;
    private final LocalDate checkInDate;
    private final LocalDate checkOutDate;

    public RoomNotAvailableException(Long roomId, LocalDate checkInDate, LocalDate checkOutDate) {
        super(String.format("La habitación ID %d no está disponible del %s al %s",
            roomId, checkInDate, checkOutDate));
        this.roomId = roomId;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    public RoomNotAvailableException(String message) {
        super(message);
        this.roomId = null;
        this.checkInDate = null;
        this.checkOutDate = null;
    }

    public Long getRoomId() {
        return roomId;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }
}
