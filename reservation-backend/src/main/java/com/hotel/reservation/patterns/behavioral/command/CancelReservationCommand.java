package com.hotel.reservation.patterns.behavioral.command;

import com.hotel.reservation.models.Reservation;
import com.hotel.reservation.models.ReservationStatus;
import com.hotel.reservation.repositories.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Comando para cancelar una reserva (reversible)
 */
@Slf4j
@RequiredArgsConstructor
public class CancelReservationCommand implements Command {

    private final ReservationRepository repository;
    private final Long reservationId;
    private ReservationStatus previousStatus;

    @Override
    public void execute() {
        Reservation reservation = repository.findById(reservationId)
            .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        this.previousStatus = reservation.getStatus();
        reservation.setStatus(ReservationStatus.CANCELLED);
        repository.save(reservation);

        log.info("✅ Comando ejecutado: Reserva #{} cancelada", reservationId);
    }

    @Override
    public void undo() {
        if (previousStatus != null) {
            Reservation reservation = repository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

            reservation.setStatus(previousStatus);
            repository.save(reservation);

            log.info("↩️ Comando deshecho: Reserva #{} restaurada a {}", reservationId, previousStatus);
        }
    }

    @Override
    public String getDescription() {
        return "Cancelar reserva ID: " + reservationId;
    }
}
