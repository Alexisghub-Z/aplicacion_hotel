package com.hotel.reservation.patterns.structural.decorator;

import java.math.BigDecimal;

/**
 * PATRÓN DECORATOR - Component Interface
 *
 * Interfaz base para el componente de reserva que será decorado
 * con servicios adicionales
 */
public interface ReservationComponent {

    /**
     * Obtiene el precio total de la reserva
     * @return precio total incluyendo servicios
     */
    BigDecimal getPrice();

    /**
     * Obtiene la descripción completa de la reserva
     * @return descripción con todos los servicios incluidos
     */
    String getDescription();
}
