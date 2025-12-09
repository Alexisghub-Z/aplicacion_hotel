package com.hotel.reservation.patterns.behavioral.strategy;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;

/**
 * Estrategia de precio para fines de semana
 * Aplica incremento si el check-in es viernes o sábado
 */
public class WeekendPricingStrategy implements PricingStrategy {

    private static final BigDecimal WEEKEND_INCREASE = new BigDecimal("0.20"); // 20%

    @Override
    public BigDecimal calculatePrice(BigDecimal basePrice, LocalDate checkInDate, LocalDate checkOutDate) {
        if (isWeekend(checkInDate)) {
            BigDecimal increase = basePrice.multiply(WEEKEND_INCREASE);
            return basePrice.add(increase);
        }
        return basePrice;
    }

    /**
     * Verifica si la fecha es fin de semana (viernes o sábado)
     */
    private boolean isWeekend(LocalDate date) {
        DayOfWeek day = date.getDayOfWeek();
        return day == DayOfWeek.FRIDAY || day == DayOfWeek.SATURDAY;
    }

    @Override
    public String getDescription() {
        return "Precio fin de semana (+20%) - Viernes y Sábado";
    }
}
