package com.hotel.reservation.patterns.behavioral.interpreter;

import com.hotel.reservation.models.Room;
import lombok.RequiredArgsConstructor;

/**
 * Expresión no terminal - AND lógico
 * Combina dos expresiones con operador AND
 */
@RequiredArgsConstructor
public class AndExpression implements Expression {

    private final Expression expr1;
    private final Expression expr2;

    @Override
    public boolean interpret(Room room) {
        return expr1.interpret(room) && expr2.interpret(room);
    }

    @Override
    public String getDescription() {
        return "(" + expr1.getDescription() + " AND " + expr2.getDescription() + ")";
    }
}
