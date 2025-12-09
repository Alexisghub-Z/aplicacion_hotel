package com.hotel.reservation.patterns.structural.decorator;

import java.math.BigDecimal;

/**
 * PATRÓN DECORATOR - Concrete Decorator: Spa
 *
 * Añade servicio de spa a una reserva
 * Precio: $800 MXN por sesión
 */
public class SpaDecorator extends ReservationDecorator {

    private static final BigDecimal SPA_PRICE_PER_SESSION = new BigDecimal("800.00");
    private final int numberOfSessions;

    public SpaDecorator(ReservationComponent reservation, int numberOfSessions) {
        super(reservation);
        this.numberOfSessions = numberOfSessions;
    }

    @Override
    public BigDecimal getPrice() {
        BigDecimal spaCost = SPA_PRICE_PER_SESSION.multiply(BigDecimal.valueOf(numberOfSessions));
        return super.getPrice().add(spaCost);
    }

    @Override
    public String getDescription() {
        return super.getDescription() +
            String.format(" + Spa (%d sesión%s)", numberOfSessions, numberOfSessions > 1 ? "es" : "");
    }

    public BigDecimal getSpaCost() {
        return SPA_PRICE_PER_SESSION.multiply(BigDecimal.valueOf(numberOfSessions));
    }
}
