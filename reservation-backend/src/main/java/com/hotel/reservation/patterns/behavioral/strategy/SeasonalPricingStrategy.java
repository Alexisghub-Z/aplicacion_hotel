package com.hotel.reservation.patterns.behavioral.strategy;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;

/**
 * Estrategia de precio por temporada alta
 * Temporada alta en Oaxaca: Julio-Agosto (Guelaguetza) y Diciembre (Navidad/Año Nuevo)
 */
public class SeasonalPricingStrategy implements PricingStrategy {

    private static final BigDecimal SEASONAL_INCREASE = new BigDecimal("0.30"); // 30%

    @Override
    public BigDecimal calculatePrice(BigDecimal basePrice, LocalDate checkInDate, LocalDate checkOutDate) {
        if (isHighSeason(checkInDate) || isHighSeason(checkOutDate)) {
            BigDecimal increase = basePrice.multiply(SEASONAL_INCREASE);
            return basePrice.add(increase);
        }
        return basePrice;
    }

    /**
     * Verifica si la fecha está en temporada alta
     */
    private boolean isHighSeason(LocalDate date) {
        Month month = date.getMonth();
        // Temporada alta: Julio, Agosto (Guelaguetza) y Diciembre (fiestas)
        return month == Month.JULY || month == Month.AUGUST || month == Month.DECEMBER;
    }

    @Override
    public String getDescription() {
        return "Precio temporada alta (+30%) - Julio, Agosto, Diciembre";
    }
}
