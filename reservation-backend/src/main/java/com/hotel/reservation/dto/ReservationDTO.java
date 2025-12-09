package com.hotel.reservation.dto;

import com.hotel.reservation.models.ReservationStatus;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para transferencia de datos de Reservation
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationDTO {

    private Long id;

    @NotNull(message = "El ID del cliente es obligatorio")
    private Long customerId;

    @NotNull(message = "El ID de la habitación es obligatorio")
    private Long roomId;

    @NotNull(message = "La fecha de check-in es obligatoria")
    private LocalDate checkInDate;

    @NotNull(message = "La fecha de check-out es obligatoria")
    private LocalDate checkOutDate;

    @NotNull(message = "El número de huéspedes es obligatorio")
    @Positive(message = "El número de huéspedes debe ser positivo")
    private Integer numberOfGuests;

    private BigDecimal totalPrice;
    private ReservationStatus status;
    private List<Long> additionalServiceIds;
    private Long packageId; // ID del paquete seleccionado (opcional)

    // Datos anidados para respuesta
    private CustomerDTO customer;
    private RoomDTO room;
    private List<AdditionalServiceDTO> services;
    private PackageDTO selectedPackage;

    // Campos calculados
    private Long numberOfNights;
    private String formattedTotalPrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
