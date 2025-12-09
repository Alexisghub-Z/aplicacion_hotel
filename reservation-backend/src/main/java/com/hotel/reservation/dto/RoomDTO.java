package com.hotel.reservation.dto;

import com.hotel.reservation.models.RoomType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO para transferencia de datos de Room
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomDTO {

    private Long id;

    @NotBlank(message = "El número de habitación es obligatorio")
    private String roomNumber;

    @NotNull(message = "El tipo de habitación es obligatorio")
    private RoomType roomType;

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser positivo")
    private BigDecimal price;

    @NotNull(message = "La capacidad es obligatoria")
    @Positive(message = "La capacidad debe ser positiva")
    private Integer capacity;

    private Boolean available;

    @NotNull(message = "El piso es obligatorio")
    private Integer floor;

    private List<String> amenities;
    private String imageUrl;
    private String description;

    // Precio formateado (para frontend)
    private String formattedPrice;
}
