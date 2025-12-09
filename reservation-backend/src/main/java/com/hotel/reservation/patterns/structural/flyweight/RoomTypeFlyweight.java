package com.hotel.reservation.patterns.structural.flyweight;

import com.hotel.reservation.models.RoomType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * PATRÓN FLYWEIGHT - RoomTypeFlyweight
 *
 * Propósito: Compartir de manera eficiente datos comunes (amenidades, descripciones)
 * entre múltiples habitaciones del mismo tipo, reduciendo el uso de memoria.
 *
 * Beneficios:
 * - Reduce memoria al compartir datos inmutables
 * - Separa estado intrínseco (compartido) del extrínseco (único)
 * - Optimiza rendimiento en sistemas con muchos objetos similares
 *
 * En nuestro hotel:
 * - Estado intrínseco: amenidades, descripciones (compartidas por tipo)
 * - Estado extrínseco: número de habitación, disponibilidad (únicos)
 */
public class RoomTypeFlyweight {

    // Pool de flyweights - una instancia por tipo de habitación
    private static final Map<RoomType, RoomTypeFlyweight> flyweights = new HashMap<>();

    private final RoomType type;
    private final List<String> standardAmenities;
    private final String standardDescription;
    private final String imageUrl;

    private RoomTypeFlyweight(RoomType type, List<String> amenities, String description, String imageUrl) {
        this.type = type;
        this.standardAmenities = amenities;
        this.standardDescription = description;
        this.imageUrl = imageUrl;
    }

    /**
     * Obtiene o crea un flyweight para el tipo de habitación
     * @param type tipo de habitación
     * @return flyweight con datos compartidos
     */
    public static RoomTypeFlyweight getFlyweight(RoomType type) {
        return flyweights.computeIfAbsent(type, RoomTypeFlyweight::createFlyweight);
    }

    /**
     * Crea un nuevo flyweight con datos del tipo de habitación
     */
    private static RoomTypeFlyweight createFlyweight(RoomType type) {
        return switch (type) {
            case SINGLE -> new RoomTypeFlyweight(
                RoomType.SINGLE,
                List.of("WiFi gratuito", "TV por cable", "Aire acondicionado",
                       "Escritorio", "Baño privado"),
                "Habitación individual cómoda y acogedora, perfecta para viajeros solitarios",
                "/images/rooms/single.jpg"
            );

            case DOUBLE -> new RoomTypeFlyweight(
                RoomType.DOUBLE,
                List.of("WiFi gratuito", "TV por cable", "Aire acondicionado",
                       "Mini bar", "Caja fuerte", "Baño privado", "Balcón"),
                "Habitación doble espaciosa con vista panorámica de Oaxaca",
                "/images/rooms/double.jpg"
            );

            case SUITE -> new RoomTypeFlyweight(
                RoomType.SUITE,
                List.of("WiFi gratuito", "TV por cable", "Aire acondicionado",
                       "Mini bar", "Caja fuerte", "Jacuzzi", "Sala de estar",
                       "Kitchenette", "Balcón amplio", "Servicio de habitación 24h"),
                "Suite de lujo con sala separada, ideal para familias o estancias prolongadas",
                "/images/rooms/suite.jpg"
            );

            case PRESIDENTIAL -> new RoomTypeFlyweight(
                RoomType.PRESIDENTIAL,
                List.of("WiFi gratuito", "TV por cable 60\"", "Aire acondicionado",
                       "Mini bar premium", "Caja fuerte", "Jacuzzi de lujo",
                       "Sala de estar amplia", "Comedor privado", "Cocina completa",
                       "Terraza privada", "Servicio de habitación 24h",
                       "Mayordomo personal", "Vistas panorámicas a la ciudad"),
                "Suite presidencial con las mejores vistas de Oaxaca y servicios VIP exclusivos",
                "/images/rooms/presidential.jpg"
            );
        };
    }

    /**
     * Obtiene las amenidades estándar del tipo
     * @return lista de amenidades (inmutable)
     */
    public List<String> getStandardAmenities() {
        return List.copyOf(standardAmenities);
    }

    /**
     * Obtiene la descripción estándar del tipo
     * @return descripción
     */
    public String getStandardDescription() {
        return standardDescription;
    }

    /**
     * Obtiene la URL de imagen estándar del tipo
     * @return URL de imagen
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * Obtiene el tipo de habitación
     * @return tipo
     */
    public RoomType getType() {
        return type;
    }

    /**
     * Muestra información del flyweight (para debugging)
     */
    public void display() {
        System.out.printf("RoomTypeFlyweight [%s]%n", type);
        System.out.printf("  Descripción: %s%n", standardDescription);
        System.out.printf("  Amenidades (%d): %s%n",
            standardAmenities.size(), String.join(", ", standardAmenities));
        System.out.printf("  Imagen: %s%n", imageUrl);
    }

    /**
     * Obtiene el número de flyweights en el pool
     * @return cantidad de tipos únicos cargados
     */
    public static int getFlyweightCount() {
        return flyweights.size();
    }

    /**
     * Limpia el pool (útil para testing)
     */
    public static void clearPool() {
        flyweights.clear();
    }
}
