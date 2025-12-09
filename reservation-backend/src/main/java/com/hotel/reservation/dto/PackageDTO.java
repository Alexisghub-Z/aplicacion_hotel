package com.hotel.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO para transferir información de paquetes de servicios
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PackageDTO {

    private Long id;
    private String name;
    private String description;
    private BigDecimal discount;
    private String imageUrl;
    private Boolean active;

    /**
     * Lista de servicios incluidos en el paquete
     */
    private List<AdditionalServiceDTO> services;

    /**
     * Precio original del paquete (sin descuento)
     */
    private BigDecimal originalPrice;

    /**
     * Precio final del paquete (con descuento)
     */
    private BigDecimal finalPrice;

    /**
     * Precio formateado para mostrar
     */
    private String formattedFinalPrice;

    /**
     * Ahorro total por el descuento
     */
    private BigDecimal savings;

    /**
     * Porcentaje de descuento (para mostrar)
     */
    private Integer discountPercentage;

    /**
     * Número de servicios incluidos
     */
    private Integer serviceCount;
}
