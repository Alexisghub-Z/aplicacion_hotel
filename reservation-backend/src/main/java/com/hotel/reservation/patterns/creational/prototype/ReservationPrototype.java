package com.hotel.reservation.patterns.creational.prototype;

import com.hotel.reservation.models.Reservation;
import com.hotel.reservation.models.ReservationStatus;
import lombok.extern.slf4j.Slf4j;

/**
 * Prototype Pattern - Clonación profunda de reservas
 * Permite crear copias de reservas existentes modificando solo algunos campos
 */
@Slf4j
public class ReservationPrototype {

    /**
     * Clona una reserva para crear una nueva con las mismas características
     * Útil para reservas recurrentes o plantillas
     */
    public static Reservation cloneReservation(Reservation original) {
        log.info("🔄 Clonando reserva #{} como plantilla", original.getId());

        Reservation clone = Reservation.builder()
                .customer(original.getCustomer())
                .room(original.getRoom())
                .checkInDate(original.getCheckInDate())
                .checkOutDate(original.getCheckOutDate())
                .numberOfGuests(original.getNumberOfGuests())
                .totalPrice(original.getTotalPrice())
                .status(ReservationStatus.PENDING) // Nueva reserva siempre es PENDING
                .build();

        // Copiar servicios adicionales si existen
        if (original.getAdditionalServices() != null && !original.getAdditionalServices().isEmpty()) {
            clone.setAdditionalServices(new java.util.ArrayList<>(original.getAdditionalServices()));
        }

        log.info("✅ Reserva clonada exitosamente");
        return clone;
    }

    /**
     * Clona una reserva y ajusta las fechas
     */
    public static Reservation cloneWithNewDates(Reservation original,
                                                  java.time.LocalDate newCheckIn,
                                                  java.time.LocalDate newCheckOut) {
        Reservation clone = cloneReservation(original);
        clone.setCheckInDate(newCheckIn);
        clone.setCheckOutDate(newCheckOut);

        log.info("📅 Reserva clonada con nuevas fechas: {} a {}", newCheckIn, newCheckOut);
        return clone;
    }

    /**
     * Clona una reserva para otro cliente
     */
    public static Reservation cloneForDifferentCustomer(Reservation original,
                                                         com.hotel.reservation.models.Customer newCustomer) {
        Reservation clone = cloneReservation(original);
        clone.setCustomer(newCustomer);

        log.info("👤 Reserva clonada para nuevo cliente: {} {}",
                newCustomer.getFirstName(), newCustomer.getLastName());
        return clone;
    }
}
