package com.hotel.reservation.patterns.structural.decorator;

import java.math.BigDecimal;

/**
 * PATRÓN DECORATOR - Concrete Decorator: Transporte
 *
 * Añade servicio de transporte aeropuerto-hotel-aeropuerto
 * Precio: $500 MXN por trayecto
 */
public class TransportDecorator extends ReservationDecorator {

    private static final BigDecimal TRANSPORT_PRICE_PER_TRIP = new BigDecimal("500.00");
    private final boolean roundTrip; // ida y vuelta

    public TransportDecorator(ReservationComponent reservation, boolean roundTrip) {
        super(reservation);
        this.roundTrip = roundTrip;
    }

    @Override
    public BigDecimal getPrice() {
        int trips = roundTrip ? 2 : 1;
        BigDecimal transportCost = TRANSPORT_PRICE_PER_TRIP.multiply(BigDecimal.valueOf(trips));
        return super.getPrice().add(transportCost);
    }

    @Override
    public String getDescription() {
        String tripType = roundTrip ? "ida y vuelta" : "solo ida";
        return super.getDescription() + String.format(" + Transporte aeropuerto (%s)", tripType);
    }

    public BigDecimal getTransportCost() {
        int trips = roundTrip ? 2 : 1;
        return TRANSPORT_PRICE_PER_TRIP.multiply(BigDecimal.valueOf(trips));
    }
}
