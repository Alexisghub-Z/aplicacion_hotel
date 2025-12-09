package com.hotel.reservation.patterns.behavioral.visitor;

import com.hotel.reservation.models.Customer;
import com.hotel.reservation.models.Payment;
import com.hotel.reservation.models.Reservation;
import com.hotel.reservation.models.Room;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.format.DateTimeFormatter;

/**
 * Visitor para exportar entidades a formato CSV
 */
@Slf4j
@Getter
public class ExportVisitor implements EntityVisitor {

    private final StringBuilder csvData = new StringBuilder();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public void visit(Room room) {
        csvData.append(String.format("%d,%s,%s,%.2f,%d,%s,%d\n",
            room.getId(),
            room.getRoomNumber(),
            room.getRoomType(),
            room.getPrice(),
            room.getCapacity(),
            room.isAvailable() ? "SI" : "NO",
            room.getFloor()));

        log.debug("📤 Exportando habitación: {}", room.getRoomNumber());
    }

    @Override
    public void visit(Customer customer) {
        csvData.append(String.format("%d,%s,%s,%s,%s,%s\n",
            customer.getId(),
            customer.getFirstName(),
            customer.getLastName(),
            customer.getEmail(),
            customer.getPhone(),
            customer.getLoyaltyLevel()));

        log.debug("📤 Exportando cliente: {} {}", customer.getFirstName(), customer.getLastName());
    }

    @Override
    public void visit(Reservation reservation) {
        csvData.append(String.format("%d,%d,%d,%s,%s,%d,%.2f,%s\n",
            reservation.getId(),
            reservation.getCustomer().getId(),
            reservation.getRoom().getId(),
            reservation.getCheckInDate().format(formatter),
            reservation.getCheckOutDate().format(formatter),
            reservation.getNumberOfGuests(),
            reservation.getTotalPrice(),
            reservation.getStatus()));

        log.debug("📤 Exportando reserva: #{}", reservation.getId());
    }

    @Override
    public void visit(Payment payment) {
        csvData.append(String.format("%d,%d,%.2f,%s,%s,%s\n",
            payment.getId(),
            payment.getReservation().getId(),
            payment.getAmount(),
            payment.getPaymentMethod(),
            payment.getPaymentStatus(),
            payment.getTransactionId()));

        log.debug("📤 Exportando pago: #{}", payment.getId());
    }

    public String getExportedData() {
        return csvData.toString();
    }

    public void reset() {
        csvData.setLength(0);
    }
}
