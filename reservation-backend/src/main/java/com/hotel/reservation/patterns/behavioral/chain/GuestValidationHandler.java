package com.hotel.reservation.patterns.behavioral.chain;

import com.hotel.reservation.dto.ReservationDTO;
import com.hotel.reservation.exception.InvalidReservationException;
import lombok.extern.slf4j.Slf4j;

/**
 * Validador de número de huéspedes
 */
@Slf4j
public class GuestValidationHandler extends ValidationHandler {

    @Override
    public void validate(ReservationDTO reservation) {
        log.info("🔍 Validando número de huéspedes...");

        if (reservation.getNumberOfGuests() == null || reservation.getNumberOfGuests() <= 0) {
            throw new InvalidReservationException("El número de huéspedes debe ser mayor a 0");
        }

        if (reservation.getNumberOfGuests() > 10) {
            throw new InvalidReservationException("El número máximo de huéspedes es 10");
        }

        log.info("✅ Número de huéspedes válido: {}", reservation.getNumberOfGuests());
        validateNext(reservation);
    }
}
