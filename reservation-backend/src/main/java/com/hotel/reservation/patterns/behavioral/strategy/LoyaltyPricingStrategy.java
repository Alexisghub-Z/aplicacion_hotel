package com.hotel.reservation.patterns.behavioral.strategy;

import com.hotel.reservation.models.LoyaltyLevel;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Estrategia de precio con descuento por nivel de lealtad
 */
public class LoyaltyPricingStrategy implements PricingStrategy {

    private final LoyaltyLevel loyaltyLevel;

    public LoyaltyPricingStrategy(LoyaltyLevel loyaltyLevel) {
        this.loyaltyLevel = loyaltyLevel;
    }

    @Override
    public BigDecimal calculatePrice(BigDecimal basePrice, LocalDate checkInDate, LocalDate checkOutDate) {
        int discountPercentage = getDiscountPercentage();
        if (discountPercentage == 0) {
            return basePrice;
        }

        BigDecimal discount = basePrice.multiply(new BigDecimal(discountPercentage).divide(new BigDecimal("100")));
        return basePrice.subtract(discount);
    }

    private int getDiscountPercentage() {
        return switch (loyaltyLevel) {
            case REGULAR -> 0;
            case SILVER -> 5;
            case GOLD -> 10;
            case PLATINUM -> 20;
        };
    }

    @Override
    public String getDescription() {
        int discount = getDiscountPercentage();
        return String.format("Descuento %s (-%d%%)", loyaltyLevel.name(), discount);
    }
}
