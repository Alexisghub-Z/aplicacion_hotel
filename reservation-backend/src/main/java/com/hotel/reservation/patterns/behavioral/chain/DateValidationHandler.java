package com.hotel.reservation.patterns.behavioral.chain;

import com.hotel.reservation.dto.ReservationDTO;
import com.hotel.reservation.exception.InvalidReservationException;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

/**
 * Validador de fechas de reserva
 */
@Slf4j
public class DateValidationHandler extends ValidationHandler {

    @Override
    public void validate(ReservationDTO reservation) {
        log.info("🔍 Validando fechas de reserva...");

        if (reservation.getCheckInDate() == null || reservation.getCheckOutDate() == null) {
            throw new InvalidReservationException("Las fechas de entrada y salida son obligatorias");
        }

        if (reservation.getCheckInDate().isBefore(LocalDate.now())) {
            throw new InvalidReservationException("La fecha de entrada no puede ser en el pasado");
        }

        if (reservation.getCheckOutDate().isBefore(reservation.getCheckInDate()) ||
            reservation.getCheckOutDate().isEqual(reservation.getCheckInDate())) {
            throw new InvalidReservationException("La fecha de salida debe ser posterior a la fecha de entrada");
        }

        log.info("✅ Fechas válidas");
        validateNext(reservation);
    }
}
