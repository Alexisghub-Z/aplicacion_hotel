package com.hotel.reservation.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Entidad Customer - Representa a un cliente del hotel
 * Implementa niveles de lealtad para aplicar descuentos (Strategy Pattern)
 */
@Entity
@Table(name = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false)
    private String firstName;

    @NotBlank(message = "El apellido es obligatorio")
    @Column(nullable = false)
    private String lastName;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe ser válido")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "El teléfono es obligatorio")
    @Column(nullable = false)
    private String phone;

    @NotNull(message = "El nivel de lealtad es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private LoyaltyLevel loyaltyLevel = LoyaltyLevel.REGULAR;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Obtiene el nombre completo del cliente
     * @return nombre completo
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }

    /**
     * Obtiene el porcentaje de descuento según el nivel de lealtad
     * Usado por LoyaltyPricingStrategy (Strategy Pattern)
     * @return porcentaje de descuento (0-20)
     */
    public int getDiscountPercentage() {
        return switch (loyaltyLevel) {
            case REGULAR -> 0;
            case SILVER -> 5;
            case GOLD -> 10;
            case PLATINUM -> 20;
        };
    }
}
