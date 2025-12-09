package com.hotel.reservation.patterns.behavioral.interpreter;

import com.hotel.reservation.models.Room;
import lombok.RequiredArgsConstructor;

/**
 * Expresión terminal - Habitaciones con capacidad mínima
 */
@RequiredArgsConstructor
public class CapacityExpression implements Expression {

    private final int minCapacity;

    @Override
    public boolean interpret(Room room) {
        return room.getCapacity() >= minCapacity;
    }

    @Override
    public String getDescription() {
        return "capacity >= " + minCapacity;
    }
}
