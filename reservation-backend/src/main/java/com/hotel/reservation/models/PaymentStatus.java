package com.hotel.reservation.models;

/**
 * Estados del pago
 */
public enum PaymentStatus {
    PENDING,      // Pago pendiente
    COMPLETED,    // Pago completado exitosamente
    FAILED,       // Pago fallido
    REFUNDED      // Pago reembolsado
}
