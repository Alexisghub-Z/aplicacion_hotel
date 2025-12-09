package com.hotel.reservation.dto;

import com.hotel.reservation.models.LoyaltyLevel;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para transferencia de datos de Customer
 * Usado en API REST para request/response
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerDTO {

    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    private String firstName;

    @NotBlank(message = "El apellido es obligatorio")
    private String lastName;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe ser válido")
    private String email;

    @NotBlank(message = "El teléfono es obligatorio")
    private String phone;

    @NotNull(message = "El nivel de lealtad es obligatorio")
    private LoyaltyLevel loyaltyLevel;

    // Campos calculados (no en la entidad)
    private String fullName;
    private Integer discountPercentage;
}
