package com.hotel.reservation.patterns.behavioral.command;

import com.hotel.reservation.models.Reservation;
import com.hotel.reservation.repositories.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Comando para crear una reserva (reversible)
 */
@Slf4j
@RequiredArgsConstructor
public class CreateReservationCommand implements Command {

    private final ReservationRepository repository;
    private final Reservation reservation;
    private Long savedId;

    @Override
    public void execute() {
        Reservation saved = repository.save(reservation);
        this.savedId = saved.getId();
        log.info("✅ Comando ejecutado: Reserva #{} creada", savedId);
    }

    @Override
    public void undo() {
        if (savedId != null) {
            repository.deleteById(savedId);
            log.info("↩️ Comando deshecho: Reserva #{} eliminada", savedId);
        }
    }

    @Override
    public String getDescription() {
        return "Crear reserva para cliente ID: " + reservation.getCustomer().getId();
    }
}
