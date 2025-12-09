package com.hotel.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO para crear o actualizar paquetes de servicios
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePackageDTO {

    private String name;
    private String description;

    /**
     * Descuento del paquete (0.10 = 10%)
     */
    private BigDecimal discount;

    private String imageUrl;

    /**
     * IDs de los servicios que forman parte del paquete
     */
    private List<Long> serviceIds;

    private Boolean active;
}
