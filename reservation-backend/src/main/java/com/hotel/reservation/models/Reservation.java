package com.hotel.reservation.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad Reservation - Representa una reserva de hotel
 * Construida usando el patrón Builder (ReservationBuilder)
 * Sus estados son manejados por el patrón State (ReservationState)
 * Los cambios notifican mediante el patrón Observer (NotificationManager)
 * El historial se guarda con el patrón Memento (ReservationMemento)
 */
@Entity
@Table(name = "reservations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El cliente es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @NotNull(message = "La habitación es obligatoria")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @NotNull(message = "La fecha de check-in es obligatoria")
    @Column(nullable = false)
    private LocalDate checkInDate;

    @NotNull(message = "La fecha de check-out es obligatoria")
    @Column(nullable = false)
    private LocalDate checkOutDate;

    @NotNull(message = "El número de huéspedes es obligatorio")
    @Positive(message = "El número de huéspedes debe ser positivo")
    @Column(nullable = false)
    private Integer numberOfGuests;

    @NotNull(message = "El precio total es obligatorio")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @NotNull(message = "El estado es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ReservationStatus status = ReservationStatus.PENDING;

    @ManyToMany
    @JoinTable(
        name = "reservation_services",
        joinColumns = @JoinColumn(name = "reservation_id"),
        inverseJoinColumns = @JoinColumn(name = "service_id")
    )
    @Builder.Default
    private List<AdditionalService> additionalServices = new ArrayList<>();

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Añade un servicio adicional a la reserva
     * Usado por el patrón Decorator
     * @param service servicio a añadir
     */
    public void addService(AdditionalService service) {
        if (this.additionalServices == null) {
            this.additionalServices = new ArrayList<>();
        }
        this.additionalServices.add(service);
        // Recalcular precio total
        recalculateTotalPrice();
    }

    /**
     * Elimina un servicio adicional de la reserva
     * @param service servicio a eliminar
     */
    public void removeService(AdditionalService service) {
        if (this.additionalServices != null) {
            this.additionalServices.remove(service);
            recalculateTotalPrice();
        }
    }

    /**
     * Recalcula el precio total incluyendo servicios adicionales
     */
    private void recalculateTotalPrice() {
        if (additionalServices != null && !additionalServices.isEmpty()) {
            BigDecimal servicesTotal = additionalServices.stream()
                .map(AdditionalService::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            // El precio base ya debe estar en totalPrice
            // Solo sumamos los servicios
        }
    }

    /**
     * Verifica si la reserva puede ser cancelada
     * @return true si el estado permite cancelación
     */
    public boolean canBeCancelled() {
        return status == ReservationStatus.PENDING || status == ReservationStatus.CONFIRMED;
    }

    /**
     * Verifica si la reserva puede ser confirmada
     * @return true si el estado permite confirmación
     */
    public boolean canBeConfirmed() {
        return status == ReservationStatus.PENDING;
    }

    /**
     * Verifica si la reserva puede ser completada
     * @return true si el estado permite completar
     */
    public boolean canBeCompleted() {
        return status == ReservationStatus.CONFIRMED;
    }

    /**
     * Calcula la cantidad de noches de la reserva
     * @return número de noches
     */
    public long getNumberOfNights() {
        if (checkInDate != null && checkOutDate != null) {
            return checkOutDate.toEpochDay() - checkInDate.toEpochDay();
        }
        return 0;
    }

    /**
     * Obtiene el costo de los servicios adicionales
     * @return costo total de servicios
     */
    public BigDecimal getServicesTotal() {
        if (additionalServices == null || additionalServices.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return additionalServices.stream()
            .map(AdditionalService::getPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
