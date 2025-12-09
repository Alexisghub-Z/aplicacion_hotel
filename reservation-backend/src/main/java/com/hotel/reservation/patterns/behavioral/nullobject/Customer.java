package com.hotel.reservation.patterns.behavioral.nullobject;

/**
 * Null Object Pattern - Interfaz para clientes
 * Define operaciones comunes que tanto clientes reales como nulos deben implementar
 */
public interface Customer {
    String getFullName();
    String getEmail();
    String getPhoneNumber();
    boolean isNullCustomer();
    int getDiscountPercentage();
}
