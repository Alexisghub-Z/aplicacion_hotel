package com.hotel.reservation.models;

/**
 * Métodos de pago aceptados
 * Se integran con diferentes proveedores mediante el patrón Adapter
 */
public enum PaymentMethod {
    CREDIT_CARD,    // Tarjeta de crédito/débito
    PAYPAL,         // PayPal
    CASH            // Efectivo (pago en recepción)
}
