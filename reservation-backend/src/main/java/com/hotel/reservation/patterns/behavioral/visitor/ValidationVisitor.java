package com.hotel.reservation.patterns.behavioral.visitor;

import com.hotel.reservation.models.Customer;
import com.hotel.reservation.models.Payment;
import com.hotel.reservation.models.Reservation;
import com.hotel.reservation.models.Room;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * Visitor para validar entidades y detectar inconsistencias
 */
@Slf4j
@Getter
public class ValidationVisitor implements EntityVisitor {

    private final List<String> validationErrors = new ArrayList<>();

    @Override
    public void visit(Room room) {
        if (room.getPrice().doubleValue() <= 0) {
            validationErrors.add(String.format("Habitación %s tiene precio inválido: %.2f",
                room.getRoomNumber(), room.getPrice()));
        }

        if (room.getCapacity() <= 0) {
            validationErrors.add(String.format("Habitación %s tiene capacidad inválida: %d",
                room.getRoomNumber(), room.getCapacity()));
        }

        if (room.getFloor() < 0) {
            validationErrors.add(String.format("Habitación %s tiene piso inválido: %d",
                room.getRoomNumber(), room.getFloor()));
        }

        log.debug("✓ Validando habitación: {}", room.getRoomNumber());
    }

    @Override
    public void visit(Customer customer) {
        if (customer.getEmail() == null || !customer.getEmail().contains("@")) {
            validationErrors.add(String.format("Cliente %d tiene email inválido: %s",
                customer.getId(), customer.getEmail()));
        }

        if (customer.getFirstName() == null || customer.getFirstName().trim().isEmpty()) {
            validationErrors.add(String.format("Cliente %d no tiene nombre", customer.getId()));
        }

        if (customer.getLastName() == null || customer.getLastName().trim().isEmpty()) {
            validationErrors.add(String.format("Cliente %d no tiene apellido", customer.getId()));
        }

        log.debug("✓ Validando cliente: {} {}", customer.getFirstName(), customer.getLastName());
    }

    @Override
    public void visit(Reservation reservation) {
        if (reservation.getCheckOutDate().isBefore(reservation.getCheckInDate()) ||
            reservation.getCheckOutDate().isEqual(reservation.getCheckInDate())) {
            validationErrors.add(String.format("Reserva %d tiene fechas inválidas: %s - %s",
                reservation.getId(),
                reservation.getCheckInDate(),
                reservation.getCheckOutDate()));
        }

        if (reservation.getNumberOfGuests() <= 0) {
            validationErrors.add(String.format("Reserva %d tiene número de huéspedes inválido: %d",
                reservation.getId(), reservation.getNumberOfGuests()));
        }

        if (reservation.getNumberOfGuests() > reservation.getRoom().getCapacity()) {
            validationErrors.add(String.format("Reserva %d excede capacidad de habitación: %d > %d",
                reservation.getId(),
                reservation.getNumberOfGuests(),
                reservation.getRoom().getCapacity()));
        }

        if (reservation.getTotalPrice().doubleValue() <= 0) {
            validationErrors.add(String.format("Reserva %d tiene precio total inválido: %.2f",
                reservation.getId(), reservation.getTotalPrice()));
        }

        log.debug("✓ Validando reserva: #{}", reservation.getId());
    }

    @Override
    public void visit(Payment payment) {
        if (payment.getAmount().doubleValue() <= 0) {
            validationErrors.add(String.format("Pago %d tiene monto inválido: %.2f",
                payment.getId(), payment.getAmount()));
        }

        if (payment.getTransactionId() == null || payment.getTransactionId().trim().isEmpty()) {
            validationErrors.add(String.format("Pago %d no tiene ID de transacción",
                payment.getId()));
        }

        log.debug("✓ Validando pago: #{}", payment.getId());
    }

    public boolean hasErrors() {
        return !validationErrors.isEmpty();
    }

    public int getErrorCount() {
        return validationErrors.size();
    }

    public void reset() {
        validationErrors.clear();
    }
}
