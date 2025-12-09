package com.hotel.reservation.patterns.behavioral.strategy;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * PATRÓN STRATEGY - PricingStrategy Interface
 *
 * Propósito: Define una familia de algoritmos de cálculo de precios,
 * encapsula cada uno y los hace intercambiables.
 *
 * Beneficios:
 * - Diferentes estrategias de pricing sin modificar el código cliente
 * - Fácil agregar nuevas estrategias
 * - Elimina condicionales complejos (if/switch)
 * - Permite cambiar estrategia en runtime
 *
 * Estrategias implementadas:
 * - RegularPricingStrategy: Precio base
 * - SeasonalPricingStrategy: +30% en temporada alta
 * - WeekendPricingStrategy: +20% en fines de semana
 * - LoyaltyPricingStrategy: Descuentos por nivel de lealtad
 */
public interface PricingStrategy {

    /**
     * Calcula el precio según la estrategia específica
     * @param basePrice precio base
     * @param checkInDate fecha de check-in
     * @param checkOutDate fecha de check-out
     * @return precio calculado
     */
    BigDecimal calculatePrice(BigDecimal basePrice, LocalDate checkInDate, LocalDate checkOutDate);

    /**
     * Descripción de la estrategia
     * @return descripción
     */
    String getDescription();
}
