package com.hotel.reservation.patterns.behavioral.strategy;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Estrategia de precio regular (sin modificaciones)
 */
public class RegularPricingStrategy implements PricingStrategy {

    @Override
    public BigDecimal calculatePrice(BigDecimal basePrice, LocalDate checkInDate, LocalDate checkOutDate) {
        // Precio base sin modificaciones
        return basePrice;
    }

    @Override
    public String getDescription() {
        return "Precio regular - Sin cargos adicionales";
    }
}
