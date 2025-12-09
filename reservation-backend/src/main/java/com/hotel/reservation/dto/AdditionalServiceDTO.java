package com.hotel.reservation.dto;

import com.hotel.reservation.models.ServiceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO para transferencia de datos de AdditionalService
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdditionalServiceDTO {

    private Long id;

    @NotBlank(message = "El nombre del servicio es obligatorio")
    private String name;

    private String description;

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser positivo")
    private BigDecimal price;

    @NotNull(message = "El tipo de servicio es obligatorio")
    private ServiceType serviceType;

    private String formattedPrice;
}
