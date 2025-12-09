package com.hotel.reservation.patterns.structural.decorator;

import java.math.BigDecimal;

/**
 * PATRÓN DECORATOR - Concrete Decorator: Desayuno
 *
 * Añade servicio de desayuno a una reserva
 * Precio: $200 MXN por persona por día
 *
 * Ejemplo de uso:
 * ReservationComponent reservation = new BaseReservation(res);
 * reservation = new BreakfastDecorator(reservation, 2, 3); // 2 personas, 3 días
 */
public class BreakfastDecorator extends ReservationDecorator {

    private static final BigDecimal BREAKFAST_PRICE_PER_PERSON_PER_DAY = new BigDecimal("200.00");
    private final int numberOfPeople;
    private final int numberOfDays;

    public BreakfastDecorator(ReservationComponent reservation, int numberOfPeople, int numberOfDays) {
        super(reservation);
        this.numberOfPeople = numberOfPeople;
        this.numberOfDays = numberOfDays;
    }

    @Override
    public BigDecimal getPrice() {
        BigDecimal breakfastCost = BREAKFAST_PRICE_PER_PERSON_PER_DAY
            .multiply(BigDecimal.valueOf(numberOfPeople))
            .multiply(BigDecimal.valueOf(numberOfDays));
        return super.getPrice().add(breakfastCost);
    }

    @Override
    public String getDescription() {
        return super.getDescription() +
            String.format(" + Desayuno (%d personas × %d días)", numberOfPeople, numberOfDays);
    }

    public BigDecimal getBreakfastCost() {
        return BREAKFAST_PRICE_PER_PERSON_PER_DAY
            .multiply(BigDecimal.valueOf(numberOfPeople))
            .multiply(BigDecimal.valueOf(numberOfDays));
    }
}
