package com.hotel.reservation.patterns.behavioral.chain;

import com.hotel.reservation.dto.ReservationDTO;
import com.hotel.reservation.repositories.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Cadena de validación completa para reservas
 * Ejecuta todas las validaciones en orden
 */
@Component
@RequiredArgsConstructor
public class ReservationValidationChain {

    private final RoomRepository roomRepository;

    public void validate(ReservationDTO reservation) {
        ValidationHandler dateValidator = new DateValidationHandler();
        ValidationHandler guestValidator = new GuestValidationHandler();
        ValidationHandler availabilityValidator = new AvailabilityValidationHandler(roomRepository);

        // Construir cadena de validación
        dateValidator.setNext(guestValidator).setNext(availabilityValidator);

        // Ejecutar validación en cadena
        dateValidator.validate(reservation);
    }
}
