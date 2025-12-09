package com.hotel.reservation.patterns.behavioral.interpreter;

import com.hotel.reservation.models.Room;
import com.hotel.reservation.models.RoomType;
import lombok.RequiredArgsConstructor;

/**
 * Expresión terminal - Habitaciones de un tipo específico
 */
@RequiredArgsConstructor
public class TypeExpression implements Expression {

    private final RoomType roomType;

    @Override
    public boolean interpret(Room room) {
        return room.getRoomType() == roomType;
    }

    @Override
    public String getDescription() {
        return "type=" + roomType;
    }
}
