package com.hotel.reservation.patterns.behavioral.visitor;

import com.hotel.reservation.models.Customer;
import com.hotel.reservation.models.Payment;
import com.hotel.reservation.models.Reservation;
import com.hotel.reservation.models.Room;

/**
 * Visitor Pattern - Interfaz para visitantes de entidades
 * Permite agregar nuevas operaciones sin modificar las entidades
 */
public interface EntityVisitor {
    void visit(Room room);
    void visit(Customer customer);
    void visit(Reservation reservation);
    void visit(Payment payment);
}
