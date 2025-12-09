package com.hotel.reservation.patterns.behavioral.interpreter;

import com.hotel.reservation.models.Room;
import lombok.RequiredArgsConstructor;

/**
 * Expresión no terminal - NOT lógico
 * Niega el resultado de una expresión
 */
@RequiredArgsConstructor
public class NotExpression implements Expression {

    private final Expression expr;

    @Override
    public boolean interpret(Room room) {
        return !expr.interpret(room);
    }

    @Override
    public String getDescription() {
        return "NOT(" + expr.getDescription() + ")";
    }
}
