package com.hotel.reservation.patterns.structural.decorator;

import java.math.BigDecimal;

/**
 * PATRÓN DECORATOR - Concrete Decorator: Excursión
 *
 * Añade servicio de excursión turística a Oaxaca
 * Precio: $1,200 MXN por persona por excursión
 *
 * Excursiones disponibles:
 * - Monte Albán
 * - Hierve el Agua
 * - Pueblos Mancomunados
 * - Taller de Alebrijes
 */
public class ExcursionDecorator extends ReservationDecorator {

    private static final BigDecimal EXCURSION_PRICE_PER_PERSON = new BigDecimal("1200.00");
    private final int numberOfPeople;
    private final String excursionName;

    public ExcursionDecorator(ReservationComponent reservation, int numberOfPeople, String excursionName) {
        super(reservation);
        this.numberOfPeople = numberOfPeople;
        this.excursionName = excursionName;
    }

    @Override
    public BigDecimal getPrice() {
        BigDecimal excursionCost = EXCURSION_PRICE_PER_PERSON.multiply(BigDecimal.valueOf(numberOfPeople));
        return super.getPrice().add(excursionCost);
    }

    @Override
    public String getDescription() {
        return super.getDescription() +
            String.format(" + Excursión a %s (%d persona%s)",
                excursionName, numberOfPeople, numberOfPeople > 1 ? "s" : "");
    }

    public BigDecimal getExcursionCost() {
        return EXCURSION_PRICE_PER_PERSON.multiply(BigDecimal.valueOf(numberOfPeople));
    }

    public String getExcursionName() {
        return excursionName;
    }
}
