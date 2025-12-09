package com.hotel.reservation.patterns.behavioral.nullobject;

import lombok.extern.slf4j.Slf4j;

/**
 * Factory para crear clientes reales o nulos
 * Centraliza la creación y evita checks de null en todo el código
 */
@Slf4j
public class CustomerFactory {

    private static final Customer NULL_CUSTOMER = new NullCustomer();

    public static Customer getCustomer(com.hotel.reservation.models.Customer customer) {
        if (customer == null) {
            log.warn("⚠️ Cliente nulo detectado, retornando NullCustomer");
            return NULL_CUSTOMER;
        }
        return new RealCustomer(customer);
    }

    public static Customer getNullCustomer() {
        return NULL_CUSTOMER;
    }
}
