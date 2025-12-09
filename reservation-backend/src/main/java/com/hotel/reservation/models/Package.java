package com.hotel.reservation.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad Package para almacenar paquetes de servicios
 * Implementa el patrón Composite para agrupar servicios
 *
 * Ejemplos:
 * - Paquete Romántico: Spa + Cena especial + Decoración
 * - Paquete Familiar: Desayuno + Excursión + Transporte
 * - Paquete Todo Incluido: Desayuno + Spa + Transporte + Excursión
 */
@Entity
@Table(name = "packages")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Package {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    /**
     * Descuento del paquete (0.10 = 10%)
     */
    @Column(nullable = false, precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal discount = BigDecimal.ZERO;

    /**
     * URL de la imagen del paquete
     */
    @Column(length = 500)
    private String imageUrl;

    /**
     * Servicios incluidos en el paquete
     */
    @ManyToMany
    @JoinTable(
        name = "package_services",
        joinColumns = @JoinColumn(name = "package_id"),
        inverseJoinColumns = @JoinColumn(name = "service_id")
    )
    @Builder.Default
    private List<AdditionalService> services = new ArrayList<>();

    /**
     * Indica si el paquete está activo (disponible para venta)
     */
    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Calcula el precio original del paquete (sin descuento)
     * @return precio original
     */
    public BigDecimal getOriginalPrice() {
        return services.stream()
            .map(AdditionalService::getPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Calcula el precio final del paquete (con descuento aplicado)
     * @return precio final
     */
    public BigDecimal getFinalPrice() {
        BigDecimal originalPrice = getOriginalPrice();

        if (discount.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal discountAmount = originalPrice.multiply(discount);
            return originalPrice.subtract(discountAmount);
        }

        return originalPrice;
    }

    /**
     * Calcula el ahorro total por el descuento
     * @return ahorro
     */
    public BigDecimal getSavings() {
        return getOriginalPrice().subtract(getFinalPrice());
    }

    /**
     * Añade un servicio al paquete
     * @param service servicio a añadir
     */
    public void addService(AdditionalService service) {
        if (services == null) {
            services = new ArrayList<>();
        }
        if (!services.contains(service)) {
            services.add(service);
        }
    }

    /**
     * Elimina un servicio del paquete
     * @param service servicio a eliminar
     */
    public void removeService(AdditionalService service) {
        if (services != null) {
            services.remove(service);
        }
    }
}
