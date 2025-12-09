package com.hotel.reservation.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad Payment - Representa un pago asociado a una reserva
 * Procesado mediante el patrón Adapter (PaymentGatewayAdapter)
 * Diferentes métodos de pago se adaptan a una interfaz común
 */
@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La reserva es obligatoria")
    @OneToOne
    @JoinColumn(name = "reservation_id", nullable = false, unique = true)
    private Reservation reservation;

    @NotNull(message = "El monto es obligatorio")
    @Positive(message = "El monto debe ser positivo")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @NotNull(message = "El método de pago es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    @NotNull(message = "El estado del pago es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    @Column(unique = true)
    private String transactionId;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime paymentDate;

    /**
     * Marca el pago como completado
     * @param transactionId ID de la transacción del procesador de pago
     */
    public void markAsCompleted(String transactionId) {
        this.paymentStatus = PaymentStatus.COMPLETED;
        this.transactionId = transactionId;
    }

    /**
     * Marca el pago como fallido
     */
    public void markAsFailed() {
        this.paymentStatus = PaymentStatus.FAILED;
    }

    /**
     * Marca el pago como reembolsado
     */
    public void markAsRefunded() {
        this.paymentStatus = PaymentStatus.REFUNDED;
    }

    /**
     * Verifica si el pago fue exitoso
     * @return true si el pago está completado
     */
    public boolean isSuccessful() {
        return this.paymentStatus == PaymentStatus.COMPLETED;
    }

    /**
     * Verifica si el pago puede ser reembolsado
     * @return true si el estado permite reembolso
     */
    public boolean canBeRefunded() {
        return this.paymentStatus == PaymentStatus.COMPLETED;
    }
}
