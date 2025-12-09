package com.hotel.reservation.patterns.structural.composite;

import com.hotel.reservation.models.AdditionalService;

import java.math.BigDecimal;

/**
 * PATRÓN COMPOSITE - Leaf
 *
 * Representa un servicio individual (hoja del árbol)
 * No puede contener otros servicios
 */
public class ServiceLeaf implements ServiceComponent {

    private final AdditionalService service;

    public ServiceLeaf(AdditionalService service) {
        this.service = service;
    }

    @Override
    public String getName() {
        return service.getName();
    }

    @Override
    public String getDescription() {
        return service.getDescription();
    }

    @Override
    public BigDecimal getPrice() {
        return service.getPrice();
    }

    @Override
    public void display(int indent) {
        String indentation = " ".repeat(indent);
        System.out.printf("%s- %s: $%,.2f MXN%n",
            indentation, getName(), getPrice());
    }

    public AdditionalService getService() {
        return service;
    }
}
