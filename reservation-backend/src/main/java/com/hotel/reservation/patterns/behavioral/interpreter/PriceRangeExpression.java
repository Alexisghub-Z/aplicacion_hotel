package com.hotel.reservation.patterns.behavioral.interpreter;

import com.hotel.reservation.models.Room;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

/**
 * Expresión terminal - Habitaciones en un rango de precios
 */
@RequiredArgsConstructor
public class PriceRangeExpression implements Expression {

    private final BigDecimal minPrice;
    private final BigDecimal maxPrice;

    @Override
    public boolean interpret(Room room) {
        return room.getPrice().compareTo(minPrice) >= 0 &&
               room.getPrice().compareTo(maxPrice) <= 0;
    }

    @Override
    public String getDescription() {
        return String.format("price between %.2f and %.2f", minPrice, maxPrice);
    }
}
