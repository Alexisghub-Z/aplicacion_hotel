package com.hotel.reservation.patterns.behavioral.interpreter;

import com.hotel.reservation.models.Room;

/**
 * Interpreter Pattern - Expresión abstracta
 * Define una interfaz para interpretar consultas de búsqueda de habitaciones
 */
public interface Expression {
    boolean interpret(Room room);
    String getDescription();
}
