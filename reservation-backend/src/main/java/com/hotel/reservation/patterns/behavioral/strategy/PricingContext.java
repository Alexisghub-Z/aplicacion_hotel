package com.hotel.reservation.patterns.behavioral.strategy;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Contexto que usa las estrategias de pricing
 * Puede combinar múltiples estrategias
 */
public class PricingContext {

    private final List<PricingStrategy> strategies = new ArrayList<>();

    /**
     * Agrega una estrategia de pricing
     */
    public void addStrategy(PricingStrategy strategy) {
        strategies.add(strategy);
    }

    /**
     * Calcula el precio aplicando todas las estrategias
     */
    public BigDecimal calculateFinalPrice(BigDecimal basePrice, LocalDate checkInDate, LocalDate checkOutDate) {
        BigDecimal finalPrice = basePrice;

        for (PricingStrategy strategy : strategies) {
            finalPrice = strategy.calculatePrice(finalPrice, checkInDate, checkOutDate);
        }

        return finalPrice;
    }

    /**
     * Obtiene descripción de todas las estrategias aplicadas
     */
    public List<String> getAppliedStrategies() {
        List<String> descriptions = new ArrayList<>();
        for (PricingStrategy strategy : strategies) {
            descriptions.add(strategy.getDescription());
        }
        return descriptions;
    }

    /**
     * Limpia todas las estrategias
     */
    public void clearStrategies() {
        strategies.clear();
    }
}
