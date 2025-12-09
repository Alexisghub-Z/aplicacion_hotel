package com.hotel.reservation.patterns.structural.composite;

import java.math.BigDecimal;

/**
 * PATRÓN COMPOSITE - Component Interface
 *
 * Interfaz común para servicios individuales y paquetes de servicios
 * Permite tratar de forma uniforme elementos simples y compuestos
 */
public interface ServiceComponent {

    /**
     * Obtiene el nombre del servicio o paquete
     * @return nombre
     */
    String getName();

    /**
     * Obtiene la descripción del servicio o paquete
     * @return descripción
     */
    String getDescription();

    /**
     * Obtiene el precio total del servicio o paquete
     * @return precio
     */
    BigDecimal getPrice();

    /**
     * Muestra la información del servicio (para debugging)
     * @param indent nivel de indentación
     */
    void display(int indent);
}
