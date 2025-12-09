package com.hotel.reservation.patterns.behavioral.nullobject;

import lombok.RequiredArgsConstructor;

/**
 * Cliente real con datos válidos
 */
@RequiredArgsConstructor
public class RealCustomer implements Customer {

    private final com.hotel.reservation.models.Customer customer;

    @Override
    public String getFullName() {
        return customer.getFirstName() + " " + customer.getLastName();
    }

    @Override
    public String getEmail() {
        return customer.getEmail();
    }

    @Override
    public String getPhoneNumber() {
        return customer.getPhone();
    }

    @Override
    public boolean isNullCustomer() {
        return false;
    }

    @Override
    public int getDiscountPercentage() {
        return customer.getDiscountPercentage();
    }
}
