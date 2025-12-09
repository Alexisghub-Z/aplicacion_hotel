package com.hotel.reservation.models;

/**
 * Estados posibles de una reserva
 * Sigue el patrón State para gestionar transiciones válidas
 */
public enum ReservationStatus {
    PENDING,      // Reserva creada pero pendiente de confirmación
    CONFIRMED,    // Reserva confirmada y pago procesado
    CANCELLED,    // Reserva cancelada
    COMPLETED     // Reserva completada (huésped ya hizo check-out)
}
