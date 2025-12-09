package com.hotel.reservation.patterns.behavioral.visitor;

import com.hotel.reservation.models.Customer;
import com.hotel.reservation.models.Payment;
import com.hotel.reservation.models.Reservation;
import com.hotel.reservation.models.Room;
import com.hotel.reservation.patterns.creational.singleton.ConfigurationManager;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Visitor para calcular estadísticas de entidades
 */
@Slf4j
@Getter
public class StatisticsVisitor implements EntityVisitor {

    private int totalRooms = 0;
    private int availableRooms = 0;
    private BigDecimal totalRoomRevenue = BigDecimal.ZERO;

    private int totalCustomers = 0;
    private final Map<String, Integer> customersByLoyalty = new HashMap<>();

    private int totalReservations = 0;
    private final Map<String, Integer> reservationsByStatus = new HashMap<>();
    private BigDecimal totalReservationValue = BigDecimal.ZERO;

    private int totalPayments = 0;
    private BigDecimal totalPaymentAmount = BigDecimal.ZERO;
    private final Map<String, BigDecimal> paymentsByMethod = new HashMap<>();

    @Override
    public void visit(Room room) {
        totalRooms++;
        if (room.isAvailable()) {
            availableRooms++;
        }
        totalRoomRevenue = totalRoomRevenue.add(room.getPrice());

        log.debug("📊 Procesando estadísticas de habitación: {}", room.getRoomNumber());
    }

    @Override
    public void visit(Customer customer) {
        totalCustomers++;
        String loyaltyLevel = customer.getLoyaltyLevel().name();
        customersByLoyalty.merge(loyaltyLevel, 1, Integer::sum);

        log.debug("📊 Procesando estadísticas de cliente: {}", customer.getEmail());
    }

    @Override
    public void visit(Reservation reservation) {
        totalReservations++;
        String status = reservation.getStatus().name();
        reservationsByStatus.merge(status, 1, Integer::sum);
        totalReservationValue = totalReservationValue.add(reservation.getTotalPrice());

        log.debug("📊 Procesando estadísticas de reserva: #{}", reservation.getId());
    }

    @Override
    public void visit(Payment payment) {
        totalPayments++;
        totalPaymentAmount = totalPaymentAmount.add(payment.getAmount());
        String method = payment.getPaymentMethod().name();
        paymentsByMethod.merge(method, payment.getAmount(), BigDecimal::add);

        log.debug("📊 Procesando estadísticas de pago: #{}", payment.getId());
    }

    public String generateReport() {
        ConfigurationManager config = ConfigurationManager.INSTANCE;
        String currency = config.getCurrency();

        StringBuilder report = new StringBuilder();

        report.append("=== ESTADÍSTICAS GENERALES ===\n\n");

        report.append(String.format("Habitaciones: %d total | %d disponibles | %d ocupadas\n",
            totalRooms, availableRooms, totalRooms - availableRooms));

        report.append(String.format("Clientes: %d total\n", totalCustomers));
        customersByLoyalty.forEach((level, count) ->
            report.append(String.format("  - %s: %d\n", level, count)));

        report.append(String.format("\nReservas: %d total | Valor: $%.2f %s\n",
            totalReservations, totalReservationValue, currency));
        reservationsByStatus.forEach((status, count) ->
            report.append(String.format("  - %s: %d\n", status, count)));

        report.append(String.format("\nPagos: %d total | Monto: $%.2f %s\n",
            totalPayments, totalPaymentAmount, currency));
        paymentsByMethod.forEach((method, amount) ->
            report.append(String.format("  - %s: $%.2f %s\n", method, amount, currency)));

        return report.toString();
    }

    public void reset() {
        totalRooms = 0;
        availableRooms = 0;
        totalRoomRevenue = BigDecimal.ZERO;
        totalCustomers = 0;
        customersByLoyalty.clear();
        totalReservations = 0;
        reservationsByStatus.clear();
        totalReservationValue = BigDecimal.ZERO;
        totalPayments = 0;
        totalPaymentAmount = BigDecimal.ZERO;
        paymentsByMethod.clear();
    }
}
