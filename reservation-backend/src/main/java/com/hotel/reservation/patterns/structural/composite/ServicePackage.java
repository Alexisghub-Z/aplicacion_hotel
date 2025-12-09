package com.hotel.reservation.patterns.structural.composite;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * PATRÓN COMPOSITE - Composite
 *
 * Representa un paquete de servicios (nodo del árbol)
 * Puede contener servicios individuales u otros paquetes
 *
 * Ejemplos de paquetes:
 * - Paquete Romántico: Suite + Spa + Cena especial
 * - Paquete Familiar: Habitación doble + Desayuno + Excursión
 * - Paquete Todo Incluido: Habitación + Desayuno + Spa + Transporte + Excursión
 */
public class ServicePackage implements ServiceComponent {

    private final String name;
    private final String description;
    private final List<ServiceComponent> services;
    private BigDecimal discount; // Descuento del paquete (0.10 = 10%)

    public ServicePackage(String name, String description) {
        this.name = name;
        this.description = description;
        this.services = new ArrayList<>();
        this.discount = BigDecimal.ZERO;
    }

    /**
     * Añade un servicio al paquete
     * @param service servicio a añadir
     */
    public void addService(ServiceComponent service) {
        services.add(service);
    }

    /**
     * Elimina un servicio del paquete
     * @param service servicio a eliminar
     */
    public void removeService(ServiceComponent service) {
        services.remove(service);
    }

    /**
     * Obtiene todos los servicios del paquete
     * @return lista de servicios
     */
    public List<ServiceComponent> getServices() {
        return new ArrayList<>(services);
    }

    /**
     * Establece un descuento para el paquete
     * @param discount porcentaje de descuento (0.10 = 10%)
     */
    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public BigDecimal getPrice() {
        // Suma de precios de todos los servicios
        BigDecimal totalPrice = services.stream()
            .map(ServiceComponent::getPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Aplicar descuento si existe
        if (discount.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal discountAmount = totalPrice.multiply(discount);
            totalPrice = totalPrice.subtract(discountAmount);
        }

        return totalPrice;
    }

    /**
     * Obtiene el precio sin descuento
     * @return precio original
     */
    public BigDecimal getOriginalPrice() {
        return services.stream()
            .map(ServiceComponent::getPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Obtiene el monto ahorrado por el descuento
     * @return ahorro
     */
    public BigDecimal getSavings() {
        return getOriginalPrice().subtract(getPrice());
    }

    @Override
    public void display(int indent) {
        String indentation = " ".repeat(indent);
        System.out.printf("%s📦 PAQUETE: %s%n", indentation, name);
        System.out.printf("%s   %s%n", indentation, description);

        if (discount.compareTo(BigDecimal.ZERO) > 0) {
            System.out.printf("%s   Descuento: %.0f%%%n",
                indentation, discount.multiply(BigDecimal.valueOf(100)));
        }

        System.out.printf("%s   Servicios incluidos:%n", indentation);
        for (ServiceComponent service : services) {
            service.display(indent + 4);
        }

        System.out.printf("%s   TOTAL: $%,.2f MXN", indentation, getPrice());
        if (discount.compareTo(BigDecimal.ZERO) > 0) {
            System.out.printf(" (Ahorro: $%,.2f MXN)", getSavings());
        }
        System.out.println();
    }

    /**
     * Cuenta el número total de servicios en el paquete (recursivo)
     * @return número de servicios
     */
    public int countServices() {
        int count = 0;
        for (ServiceComponent service : services) {
            if (service instanceof ServicePackage) {
                count += ((ServicePackage) service).countServices();
            } else {
                count++;
            }
        }
        return count;
    }
}
