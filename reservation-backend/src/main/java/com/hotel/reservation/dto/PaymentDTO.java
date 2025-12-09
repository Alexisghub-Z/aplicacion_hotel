package com.hotel.reservation.dto;

import com.hotel.reservation.models.PaymentMethod;
import com.hotel.reservation.models.PaymentStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO para transferencia de datos de Payment
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDTO {

    private Long id;

    @NotNull(message = "El ID de la reserva es obligatorio")
    private Long reservationId;

    @NotNull(message = "El monto es obligatorio")
    @Positive(message = "El monto debe ser positivo")
    private BigDecimal amount;

    @NotNull(message = "El método de pago es obligatorio")
    private PaymentMethod paymentMethod;

    private PaymentStatus paymentStatus;
    private String transactionId;
    private LocalDateTime paymentDate;

    // Campo calculado
    private String formattedAmount;
}
