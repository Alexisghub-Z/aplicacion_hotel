package com.hotel.reservation.patterns.behavioral.chain;

import com.hotel.reservation.dto.ReservationDTO;
import com.hotel.reservation.exception.InvalidReservationException;
import com.hotel.reservation.models.Room;
import com.hotel.reservation.repositories.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Validador de disponibilidad de habitación
 */
@Slf4j
@RequiredArgsConstructor
public class AvailabilityValidationHandler extends ValidationHandler {

    private final RoomRepository roomRepository;

    @Override
    public void validate(ReservationDTO reservation) {
        log.info("🔍 Validando disponibilidad de habitación...");

        Room room = roomRepository.findById(reservation.getRoomId())
            .orElseThrow(() -> new InvalidReservationException("Habitación no encontrada"));

        if (!room.isAvailable()) {
            throw new InvalidReservationException(
                String.format("La habitación %s no está disponible", room.getRoomNumber())
            );
        }

        if (reservation.getNumberOfGuests() > room.getCapacity()) {
            throw new InvalidReservationException(
                String.format("La habitación tiene capacidad máxima de %d huéspedes", room.getCapacity())
            );
        }

        log.info("✅ Habitación disponible y capacidad suficiente");
        validateNext(reservation);
    }
}
