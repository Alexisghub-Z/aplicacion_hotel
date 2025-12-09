package com.hotel.reservation.patterns.creational.factory;

import com.hotel.reservation.models.Room;
import com.hotel.reservation.models.RoomType;

import java.math.BigDecimal;
import java.util.Arrays;

/**
 * PATRÓN FACTORY - RoomFactory
 *
 * Propósito: Encapsula la creación de habitaciones según su tipo,
 * aplicando configuraciones específicas (precio, amenidades, capacidad)
 * para cada tipo de habitación.
 *
 * Beneficios:
 * - Centraliza la lógica de creación de habitaciones
 * - Facilita agregar nuevos tipos de habitación
 * - Garantiza consistencia en la configuración por tipo
 * - Separa la creación de la lógica de negocio
 *
 * Ejemplo de uso:
 * Room room = RoomFactory.createRoom(RoomType.SUITE, "301", 3);
 */
public class RoomFactory {

    /**
     * Crea una habitación según el tipo especificado
     * @param roomType tipo de habitación a crear
     * @param roomNumber número de la habitación
     * @param floor piso donde se ubica la habitación
     * @return habitación configurada según su tipo
     */
    public static Room createRoom(RoomType roomType, String roomNumber, Integer floor) {
        Room.RoomBuilder builder = Room.builder()
            .roomNumber(roomNumber)
            .roomType(roomType)
            .floor(floor)
            .available(true);

        // Configurar según el tipo de habitación
        switch (roomType) {
            case SINGLE:
                return buildSingleRoom(builder);
            case DOUBLE:
                return buildDoubleRoom(builder);
            case SUITE:
                return buildSuiteRoom(builder);
            case PRESIDENTIAL:
                return buildPresidentialRoom(builder);
            default:
                throw new IllegalArgumentException("Tipo de habitación desconocido: " + roomType);
        }
    }

    /**
     * Construye una habitación individual
     * Precio: $800 MXN, Capacidad: 1
     */
    private static Room buildSingleRoom(Room.RoomBuilder builder) {
        return builder
            .price(new BigDecimal("800.00"))
            .capacity(1)
            .amenities(Arrays.asList(
                "WiFi gratuito",
                "TV por cable",
                "Aire acondicionado",
                "Escritorio",
                "Baño privado"
            ))
            .description("Habitación individual cómoda y acogedora, perfecta para viajeros solitarios")
            .imageUrl("/images/rooms/single.jpg")
            .build();
    }

    /**
     * Construye una habitación doble
     * Precio: $1,200 MXN, Capacidad: 2
     */
    private static Room buildDoubleRoom(Room.RoomBuilder builder) {
        return builder
            .price(new BigDecimal("1200.00"))
            .capacity(2)
            .amenities(Arrays.asList(
                "WiFi gratuito",
                "TV por cable",
                "Aire acondicionado",
                "Mini bar",
                "Caja fuerte",
                "Baño privado",
                "Balcón"
            ))
            .description("Habitación doble espaciosa con vista panorámica de Oaxaca")
            .imageUrl("/images/rooms/double.jpg")
            .build();
    }

    /**
     * Construye una suite
     * Precio: $2,500 MXN, Capacidad: 3
     */
    private static Room buildSuiteRoom(Room.RoomBuilder builder) {
        return builder
            .price(new BigDecimal("2500.00"))
            .capacity(3)
            .amenities(Arrays.asList(
                "WiFi gratuito",
                "TV por cable",
                "Aire acondicionado",
                "Mini bar",
                "Caja fuerte",
                "Jacuzzi",
                "Sala de estar",
                "Kitchenette",
                "Balcón amplio",
                "Servicio de habitación 24h"
            ))
            .description("Suite de lujo con sala separada, ideal para familias o estancias prolongadas")
            .imageUrl("/images/rooms/suite.jpg")
            .build();
    }

    /**
     * Construye una suite presidencial
     * Precio: $5,000 MXN, Capacidad: 4
     */
    private static Room buildPresidentialRoom(Room.RoomBuilder builder) {
        return builder
            .price(new BigDecimal("5000.00"))
            .capacity(4)
            .amenities(Arrays.asList(
                "WiFi gratuito",
                "TV por cable 60\"",
                "Aire acondicionado",
                "Mini bar premium",
                "Caja fuerte",
                "Jacuzzi de lujo",
                "Sala de estar amplia",
                "Comedor privado",
                "Cocina completa",
                "Terraza privada",
                "Servicio de habitación 24h",
                "Mayordomo personal",
                "Vistas panorámicas a la ciudad"
            ))
            .description("Suite presidencial con las mejores vistas de Oaxaca y servicios VIP exclusivos")
            .imageUrl("/images/rooms/presidential.jpg")
            .build();
    }

    /**
     * Crea una habitación con configuración personalizada
     * Útil para casos especiales o habitaciones customizadas
     */
    public static Room createCustomRoom(RoomType roomType, String roomNumber, Integer floor,
                                       BigDecimal customPrice) {
        Room room = createRoom(roomType, roomNumber, floor);
        room.setPrice(customPrice);
        return room;
    }
}
