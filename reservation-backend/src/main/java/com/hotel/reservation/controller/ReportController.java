package com.hotel.reservation.controller;

import com.hotel.reservation.models.Customer;
import com.hotel.reservation.models.Payment;
import com.hotel.reservation.models.Reservation;
import com.hotel.reservation.models.Room;
import com.hotel.reservation.patterns.behavioral.template.OccupancyReport;
import com.hotel.reservation.patterns.behavioral.template.ReservationReport;
import com.hotel.reservation.patterns.behavioral.template.RevenueReport;
import com.hotel.reservation.patterns.behavioral.visitor.ExportVisitor;
import com.hotel.reservation.patterns.behavioral.visitor.StatisticsVisitor;
import com.hotel.reservation.patterns.behavioral.visitor.ValidationVisitor;
import com.hotel.reservation.repositories.CustomerRepository;
import com.hotel.reservation.repositories.PaymentRepository;
import com.hotel.reservation.repositories.ReservationRepository;
import com.hotel.reservation.repositories.RoomRepository;
import com.hotel.reservation.service.ExcelExportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador de reportes y estadísticas
 * Usa Template Method Pattern y Visitor Pattern
 */
@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class ReportController {

    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;
    private final PaymentRepository paymentRepository;
    private final CustomerRepository customerRepository;
    private final ExcelExportService excelExportService;

    // ========== TEMPLATE METHOD PATTERN - Reportes ==========

    @GetMapping(value = "/reservations", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getReservationReport() {
        ReservationReport report = new ReservationReport(reservationRepository);
        return ResponseEntity.ok(report.generateReport());
    }

    @GetMapping(value = "/occupancy", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getOccupancyReport() {
        OccupancyReport report = new OccupancyReport(roomRepository);
        return ResponseEntity.ok(report.generateReport());
    }

    @GetMapping(value = "/revenue", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getRevenueReport() {
        RevenueReport report = new RevenueReport(paymentRepository);
        return ResponseEntity.ok(report.generateReport());
    }

    // ========== VISITOR PATTERN - Estadísticas ==========

    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getStatistics() {
        StatisticsVisitor visitor = new StatisticsVisitor();

        // Visitar todas las entidades
        roomRepository.findAll().forEach(visitor::visit);
        customerRepository.findAll().forEach(visitor::visit);
        reservationRepository.findAll().forEach(visitor::visit);
        paymentRepository.findAll().forEach(visitor::visit);

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalRooms", visitor.getTotalRooms());
        stats.put("availableRooms", visitor.getAvailableRooms());
        stats.put("totalCustomers", visitor.getTotalCustomers());
        stats.put("customersByLoyalty", visitor.getCustomersByLoyalty());
        stats.put("totalReservations", visitor.getTotalReservations());
        stats.put("reservationsByStatus", visitor.getReservationsByStatus());
        stats.put("totalReservationValue", visitor.getTotalReservationValue());
        stats.put("totalPayments", visitor.getTotalPayments());
        stats.put("totalPaymentAmount", visitor.getTotalPaymentAmount());
        stats.put("paymentsByMethod", visitor.getPaymentsByMethod());
        stats.put("summary", visitor.generateReport());

        return ResponseEntity.ok(stats);
    }

    // ========== VISITOR PATTERN - Validación ==========

    @GetMapping("/validate")
    public ResponseEntity<Map<String, Object>> validateData() {
        ValidationVisitor visitor = new ValidationVisitor();

        // Visitar todas las entidades
        roomRepository.findAll().forEach(visitor::visit);
        customerRepository.findAll().forEach(visitor::visit);
        reservationRepository.findAll().forEach(visitor::visit);
        paymentRepository.findAll().forEach(visitor::visit);

        Map<String, Object> result = new HashMap<>();
        result.put("hasErrors", visitor.hasErrors());
        result.put("errorCount", visitor.getErrorCount());
        result.put("errors", visitor.getValidationErrors());

        return ResponseEntity.ok(result);
    }

    // ========== VISITOR PATTERN - Exportación ==========

    @GetMapping(value = "/export/rooms", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> exportRooms() {
        ExportVisitor visitor = new ExportVisitor();
        roomRepository.findAll().forEach(visitor::visit);

        String header = "ID,Numero,Tipo,Precio,Capacidad,Disponible,Piso\n";
        return ResponseEntity.ok(header + visitor.getExportedData());
    }

    @GetMapping(value = "/export/customers", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> exportCustomers() {
        ExportVisitor visitor = new ExportVisitor();
        customerRepository.findAll().forEach(visitor::visit);

        String header = "ID,Nombre,Apellido,Email,Telefono,Nivel\n";
        return ResponseEntity.ok(header + visitor.getExportedData());
    }

    @GetMapping(value = "/export/reservations", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> exportReservations() {
        ExportVisitor visitor = new ExportVisitor();
        reservationRepository.findAll().forEach(visitor::visit);

        String header = "ID,Cliente_ID,Habitacion_ID,Check_In,Check_Out,Huespedes,Total,Estado\n";
        return ResponseEntity.ok(header + visitor.getExportedData());
    }

    @GetMapping(value = "/export/payments", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> exportPayments() {
        ExportVisitor visitor = new ExportVisitor();
        paymentRepository.findAll().forEach(visitor::visit);

        String header = "ID,Reserva_ID,Monto,Metodo,Estado,Transaction_ID\n";
        return ResponseEntity.ok(header + visitor.getExportedData());
    }

    // ========== Dashboard Summary ==========

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboard() {
        Map<String, Object> dashboard = new HashMap<>();

        // Contar totales
        long totalRooms = roomRepository.count();
        long availableRooms = roomRepository.findByAvailableTrue().size();
        long totalCustomers = customerRepository.count();
        long totalReservations = reservationRepository.count();
        long totalPayments = paymentRepository.count();

        dashboard.put("totalRooms", totalRooms);
        dashboard.put("availableRooms", availableRooms);
        dashboard.put("occupiedRooms", totalRooms - availableRooms);
        dashboard.put("totalCustomers", totalCustomers);
        dashboard.put("totalReservations", totalReservations);
        dashboard.put("totalPayments", totalPayments);

        // Calcular tasa de ocupación
        double occupancyRate = totalRooms > 0 ? ((totalRooms - availableRooms) * 100.0 / totalRooms) : 0;
        dashboard.put("occupancyRate", Math.round(occupancyRate * 100.0) / 100.0);

        return ResponseEntity.ok(dashboard);
    }

    // ========== EXCEL EXPORT ENDPOINTS ==========

    @GetMapping("/reservations/excel")
    public ResponseEntity<byte[]> downloadReservationsExcel() {
        try {
            List<Reservation> reservations = reservationRepository.findAll();
            byte[] excelFile = excelExportService.exportReservationsReport(reservations);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDispositionFormData("attachment", "reporte-reservas.xlsx");
            headers.setContentLength(excelFile.length);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(excelFile);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/revenue/excel")
    public ResponseEntity<byte[]> downloadRevenueExcel() {
        try {
            List<Payment> payments = paymentRepository.findAll();
            byte[] excelFile = excelExportService.exportRevenueReport(payments);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDispositionFormData("attachment", "reporte-ingresos.xlsx");
            headers.setContentLength(excelFile.length);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(excelFile);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/occupancy/excel")
    public ResponseEntity<byte[]> downloadOccupancyExcel() {
        try {
            List<Room> rooms = roomRepository.findAll();
            byte[] excelFile = excelExportService.exportOccupancyReport(rooms);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDispositionFormData("attachment", "reporte-ocupacion.xlsx");
            headers.setContentLength(excelFile.length);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(excelFile);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
