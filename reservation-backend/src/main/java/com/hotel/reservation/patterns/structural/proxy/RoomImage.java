package com.hotel.reservation.patterns.structural.proxy;

/**
 * PATRÓN PROXY - Subject Interface
 *
 * Interfaz común para imágenes reales y proxies
 */
public interface RoomImage {

    /**
     * Muestra la imagen
     */
    void display();

    /**
     * Obtiene la URL de la imagen
     * @return URL
     */
    String getUrl();

    /**
     * Obtiene el tamaño de la imagen en bytes
     * @return tamaño
     */
    long getSize();
}
