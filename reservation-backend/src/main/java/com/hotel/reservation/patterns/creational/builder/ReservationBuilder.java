package com.hotel.reservation.patterns.creational.builder;

import com.hotel.reservation.models.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * PATRÓN BUILDER - ReservationBuilder
 *
 * Propósito: Facilita la construcción paso a paso de reservas complejas
 * con múltiples atributos opcionales.
 *
 * Beneficios:
 * - Construcción fluida y legible
 * - Validaciones en el método build()
 * - Permite crear reservas parciales y luego completarlas
 * - Separa la construcción de la representación
 *
 * Ejemplo de uso:
 * Reservation reservation = new ReservationBuilder()
 *     .withCustomer(customer)
 *     .withRoom(room)
 *     .withDates(checkIn, checkOut)
 *     .withGuests(2)
 *     .addService(breakfastService)
 *     .build();
 */
public class ReservationBuilder {

    private Customer customer;
    private Room room;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Integer numberOfGuests;
    private BigDecimal basePrice;
    private List<AdditionalService> services;
    private ReservationStatus status;

    public ReservationBuilder() {
        this.services = new ArrayList<>();
        this.status = ReservationStatus.PENDING;
    }

    /**
     * Establece el cliente de la reserva
     * @param customer cliente
     * @return este builder para encadenamiento
     */
    public ReservationBuilder withCustomer(Customer customer) {
        this.customer = customer;
        return this;
    }

    /**
     * Establece la habitación de la reserva
     * @param room habitación
     * @return este builder para encadenamiento
     */
    public ReservationBuilder withRoom(Room room) {
        this.room = room;
        // Calcular precio base si tenemos las fechas
        if (checkInDate != null && checkOutDate != null) {
            calculateBasePrice();
        }
        return this;
    }

    /**
     * Establece las fechas de check-in y check-out
     * @param checkIn fecha de entrada
     * @param checkOut fecha de salida
     * @return este builder para encadenamiento
     */
    public ReservationBuilder withDates(LocalDate checkIn, LocalDate checkOut) {
        this.checkInDate = checkIn;
        this.checkOutDate = checkOut;
        // Calcular precio base si tenemos la habitación
        if (room != null) {
            calculateBasePrice();
        }
        return this;
    }

    /**
     * Establece el número de huéspedes
     * @param numberOfGuests cantidad de huéspedes
     * @return este builder para encadenamiento
     */
    public ReservationBuilder withGuests(Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
        return this;
    }

    /**
     * Añade un servicio adicional a la reserva
     * @param service servicio a añadir
     * @return este builder para encadenamiento
     */
    public ReservationBuilder addService(AdditionalService service) {
        this.services.add(service);
        return this;
    }

    /**
     * Añade múltiples servicios a la reserva
     * @param services lista de servicios
     * @return este builder para encadenamiento
     */
    public ReservationBuilder withServices(List<AdditionalService> services) {
        this.services.addAll(services);
        return this;
    }

    /**
     * Establece el estado inicial de la reserva
     * @param status estado
     * @return este builder para encadenamiento
     */
    public ReservationBuilder withStatus(ReservationStatus status) {
        this.status = status;
        return this;
    }

    /**
     * Calcula el precio base (habitación × noches)
     */
    private void calculateBasePrice() {
        if (room != null && checkInDate != null && checkOutDate != null) {
            long nights = checkOutDate.toEpochDay() - checkInDate.toEpochDay();
            this.basePrice = room.getPrice().multiply(BigDecimal.valueOf(nights));
        }
    }

    /**
     * Calcula el precio total incluyendo servicios adicionales
     * @return precio total
     */
    private BigDecimal calculateTotalPrice() {
        BigDecimal total = basePrice != null ? basePrice : BigDecimal.ZERO;

        // Sumar servicios adicionales
        for (AdditionalService service : services) {
            total = total.add(service.getPrice());
        }

        return total;
    }

    /**
     * Construye y valida la reserva
     * @return reserva construida
     * @throws IllegalStateException si faltan datos obligatorios
     */
    public Reservation build() {
        // Validaciones
        if (customer == null) {
            throw new IllegalStateException("La reserva debe tener un cliente");
        }
        if (room == null) {
            throw new IllegalStateException("La reserva debe tener una habitación");
        }
        if (checkInDate == null || checkOutDate == null) {
            throw new IllegalStateException("La reserva debe tener fechas de check-in y check-out");
        }
        if (checkInDate.isAfter(checkOutDate) || checkInDate.isEqual(checkOutDate)) {
            throw new IllegalStateException("La fecha de check-out debe ser posterior al check-in");
        }
        if (numberOfGuests == null || numberOfGuests <= 0) {
            throw new IllegalStateException("La reserva debe tener al menos un huésped");
        }
        if (numberOfGuests > room.getCapacity()) {
            throw new IllegalStateException(
                String.format("La habitación solo admite %d huéspedes pero se solicitaron %d",
                    room.getCapacity(), numberOfGuests)
            );
        }
        if (checkInDate.isBefore(LocalDate.now())) {
            throw new IllegalStateException("La fecha de check-in no puede ser en el pasado");
        }

        // Calcular precio total
        BigDecimal totalPrice = calculateTotalPrice();

        // Construir la reserva
        return Reservation.builder()
            .customer(customer)
            .room(room)
            .checkInDate(checkInDate)
            .checkOutDate(checkOutDate)
            .numberOfGuests(numberOfGuests)
            .totalPrice(totalPrice)
            .status(status)
            .additionalServices(new ArrayList<>(services))
            .build();
    }

    /**
     * Reinicia el builder para reutilizarlo
     * @return este builder limpio
     */
    public ReservationBuilder reset() {
        this.customer = null;
        this.room = null;
        this.checkInDate = null;
        this.checkOutDate = null;
        this.numberOfGuests = null;
        this.basePrice = null;
        this.services = new ArrayList<>();
        this.status = ReservationStatus.PENDING;
        return this;
    }
}
