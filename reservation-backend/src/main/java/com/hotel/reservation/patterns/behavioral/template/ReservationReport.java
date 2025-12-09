package com.hotel.reservation.patterns.behavioral.template;

import com.hotel.reservation.models.Reservation;
import com.hotel.reservation.repositories.ReservationRepository;
import lombok.RequiredArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Reporte de reservas
 */
@RequiredArgsConstructor
public class ReservationReport extends ReportTemplate {

    private final ReservationRepository reservationRepository;

    @Override
    protected String getReportName() {
        return "REPORTE DE RESERVAS";
    }

    @Override
    protected String generateBody() {
        List<Reservation> reservations = reservationRepository.findAll();

        if (reservations.isEmpty()) {
            return "No hay reservas registradas.\n";
        }

        StringBuilder body = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        body.append(String.format("Total de reservas: %d\n\n", reservations.size()));

        long pending = reservations.stream().filter(r -> r.getStatus().name().equals("PENDING")).count();
        long confirmed = reservations.stream().filter(r -> r.getStatus().name().equals("CONFIRMED")).count();
        long cancelled = reservations.stream().filter(r -> r.getStatus().name().equals("CANCELLED")).count();

        body.append("Resumen por estado:\n");
        body.append(String.format("  - Pendientes: %d\n", pending));
        body.append(String.format("  - Confirmadas: %d\n", confirmed));
        body.append(String.format("  - Canceladas: %d\n\n", cancelled));

        body.append("Detalle de reservas:\n");
        body.append("-".repeat(80)).append("\n");

        for (Reservation r : reservations) {
            body.append(String.format("ID: %d | Cliente: %s %s | Habitación: %s\n",
                r.getId(),
                r.getCustomer().getFirstName(),
                r.getCustomer().getLastName(),
                r.getRoom().getRoomNumber()));

            body.append(String.format("Check-in: %s | Check-out: %s | Estado: %s\n",
                r.getCheckInDate().format(formatter),
                r.getCheckOutDate().format(formatter),
                r.getStatus()));

            body.append(String.format("Total: $%.2f MXN\n", r.getTotalPrice()));
            body.append("-".repeat(80)).append("\n");
        }

        return body.toString();
    }
}
