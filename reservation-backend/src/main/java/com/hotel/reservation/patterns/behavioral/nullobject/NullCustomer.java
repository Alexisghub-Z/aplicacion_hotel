package com.hotel.reservation.patterns.behavioral.nullobject;

/**
 * Null Object Pattern - Cliente nulo
 * Proporciona un comportamiento por defecto seguro cuando no hay cliente
 * Evita NullPointerExceptions
 */
public class NullCustomer implements Customer {

    @Override
    public String getFullName() {
        return "Cliente no disponible";
    }

    @Override
    public String getEmail() {
        return "no-email@hotel.com";
    }

    @Override
    public String getPhoneNumber() {
        return "N/A";
    }

    @Override
    public boolean isNullCustomer() {
        return true;
    }

    @Override
    public int getDiscountPercentage() {
        return 0;
    }
}
