package com.hotel.reservation.patterns.behavioral.interpreter;

import com.hotel.reservation.models.Room;

/**
 * Expresión terminal - Habitaciones disponibles
 */
public class AvailableExpression implements Expression {

    @Override
    public boolean interpret(Room room) {
        return room.isAvailable();
    }

    @Override
    public String getDescription() {
        return "available";
    }
}
