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

/**
 * Entidad AdditionalService - Servicios adicionales que se pueden agregar a una reserva
 * Usado por el patrón Decorator para añadir servicios dinámicamente
 * También usado por el patrón Composite para crear paquetes de servicios
 */
@Entity
@Table(name = "additional_services")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdditionalService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del servicio es obligatorio")
    @Column(nullable = false)
    private String name;

    @Column(length = 500)
    private String description;

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser positivo")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @NotNull(message = "El tipo de servicio es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ServiceType serviceType;

    /**
     * Verifica si este servicio es del tipo especificado
     * @param type tipo de servicio a verificar
     * @return true si coincide el tipo
     */
    public boolean isOfType(ServiceType type) {
        return this.serviceType == type;
    }
}
