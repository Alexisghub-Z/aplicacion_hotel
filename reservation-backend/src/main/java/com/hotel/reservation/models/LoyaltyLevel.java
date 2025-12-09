package com.hotel.reservation.models;

/**
 * Niveles de lealtad para clientes del hotel
 * Cada nivel ofrece diferentes beneficios y descuentos
 */
public enum LoyaltyLevel {
    REGULAR,    // Cliente regular sin beneficios especiales
    SILVER,     // 5% de descuento
    GOLD,       // 10% de descuento
    PLATINUM    // 20% de descuento + beneficios VIP
}
