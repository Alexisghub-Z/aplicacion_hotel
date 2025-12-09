package com.hotel.reservation.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad Room - Representa una habitación del hotel
 * Creada mediante el patrón Factory (RoomFactory)
 * Puede ser clonada mediante el patrón Prototype
 * Usa Flyweight para compartir amenidades comunes entre habitaciones del mismo tipo
 */
@Entity
@Table(name = "rooms")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Room implements Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El número de habitación es obligatorio")
    @Column(nullable = false, unique = true)
    private String roomNumber;

    @NotNull(message = "El tipo de habitación es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomType roomType;

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser positivo")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @NotNull(message = "La capacidad es obligatoria")
    @Positive(message = "La capacidad debe ser positiva")
    @Column(nullable = false)
    private Integer capacity;

    @Column(nullable = false)
    @Builder.Default
    private Boolean available = true;

    @NotNull(message = "El piso es obligatorio")
    @Column(nullable = false)
    private Integer floor;

    @ElementCollection
    @CollectionTable(name = "room_amenities", joinColumns = @JoinColumn(name = "room_id"))
    @Column(name = "amenity")
    @Builder.Default
    private List<String> amenities = new ArrayList<>();

    @Column(length = 500)
    private String imageUrl;

    @Column(length = 1000)
    private String description;

    /**
     * Implementa el patrón Prototype para clonar habitaciones
     * Útil para crear habitaciones similares con configuraciones base
     * @return una copia de esta habitación
     */
    @Override
    public Room clone() {
        try {
            Room cloned = (Room) super.clone();
            // Clonar la lista de amenidades para evitar compartir referencias
            cloned.amenities = new ArrayList<>(this.amenities);
            // El clon no debe tener ID (será asignado por la base de datos)
            cloned.id = null;
            // El clon debe tener un número de habitación diferente
            cloned.roomNumber = null;
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Error al clonar la habitación", e);
        }
    }

    /**
     * Verifica si la habitación está disponible
     * @return true si está disponible
     */
    public boolean isAvailable() {
        return Boolean.TRUE.equals(this.available);
    }

    /**
     * Marca la habitación como ocupada
     */
    public void markAsOccupied() {
        this.available = false;
    }

    /**
     * Marca la habitación como disponible
     */
    public void markAsAvailable() {
        this.available = true;
    }
}
