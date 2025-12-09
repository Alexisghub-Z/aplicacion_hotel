package com.hotel.reservation.service;

import com.hotel.reservation.patterns.creational.singleton.ConfigurationManager;
import com.hotel.reservation.models.Payment;
import com.hotel.reservation.models.Reservation;
import com.hotel.reservation.models.Room;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Servicio para exportar reportes a formato Excel (.xlsx)
 * Utiliza Apache POI para generar archivos Excel con formato profesional
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ExcelExportService {

    private final ConfigurationManager config = ConfigurationManager.INSTANCE;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Exporta reporte de reservas a Excel
     */
    public byte[] exportReservationsReport(List<Reservation> reservations) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Reporte de Reservas");

            // Estilos
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dateStyle = createDateStyle(workbook);
            CellStyle currencyStyle = createCurrencyStyle(workbook);

            // Header Row
            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "Cliente", "Habitación", "Check-in", "Check-out",
                              "Huéspedes", "Noches", "Estado", "Precio Total"};

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Data Rows
            int rowNum = 1;
            for (Reservation reservation : reservations) {
                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(reservation.getId());
                row.createCell(1).setCellValue(reservation.getCustomer().getFullName());
                row.createCell(2).setCellValue(reservation.getRoom().getRoomNumber());

                Cell checkInCell = row.createCell(3);
                checkInCell.setCellValue(reservation.getCheckInDate().format(DATE_FORMATTER));
                checkInCell.setCellStyle(dateStyle);

                Cell checkOutCell = row.createCell(4);
                checkOutCell.setCellValue(reservation.getCheckOutDate().format(DATE_FORMATTER));
                checkOutCell.setCellStyle(dateStyle);

                row.createCell(5).setCellValue(reservation.getNumberOfGuests());
                row.createCell(6).setCellValue(reservation.getNumberOfNights());
                row.createCell(7).setCellValue(reservation.getStatus().toString());

                Cell priceCell = row.createCell(8);
                priceCell.setCellValue(reservation.getTotalPrice().doubleValue());
                priceCell.setCellStyle(currencyStyle);
            }

            // Auto-size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            return writeWorkbookToBytes(workbook);
        }
    }

    /**
     * Exporta reporte de ingresos (pagos) a Excel
     */
    public byte[] exportRevenueReport(List<Payment> payments) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Reporte de Ingresos");

            // Estilos
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dateStyle = createDateStyle(workbook);
            CellStyle currencyStyle = createCurrencyStyle(workbook);
            CellStyle totalStyle = createTotalStyle(workbook);

            // Header Row
            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID Pago", "Reserva", "Cliente", "Monto", "Método de Pago",
                              "Estado", "Fecha", "Transaction ID"};

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Data Rows
            int rowNum = 1;
            BigDecimal totalRevenue = BigDecimal.ZERO;

            for (Payment payment : payments) {
                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(payment.getId());
                row.createCell(1).setCellValue("RES-" + payment.getReservation().getId());
                row.createCell(2).setCellValue(payment.getReservation().getCustomer().getFullName());

                Cell amountCell = row.createCell(3);
                amountCell.setCellValue(payment.getAmount().doubleValue());
                amountCell.setCellStyle(currencyStyle);

                row.createCell(4).setCellValue(payment.getPaymentMethod().toString());
                row.createCell(5).setCellValue(payment.getPaymentStatus().toString());

                Cell dateCell = row.createCell(6);
                dateCell.setCellValue(payment.getPaymentDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
                dateCell.setCellStyle(dateStyle);

                row.createCell(7).setCellValue(payment.getTransactionId() != null ? payment.getTransactionId() : "N/A");

                // Acumular ingresos solo de pagos completados
                if (payment.getPaymentStatus().toString().equals("COMPLETED")) {
                    totalRevenue = totalRevenue.add(payment.getAmount());
                }
            }

            // Total Row
            Row totalRow = sheet.createRow(rowNum + 1);
            Cell totalLabelCell = totalRow.createCell(2);
            totalLabelCell.setCellValue("TOTAL INGRESOS:");
            totalLabelCell.setCellStyle(totalStyle);

            Cell totalValueCell = totalRow.createCell(3);
            totalValueCell.setCellValue(totalRevenue.doubleValue());
            totalValueCell.setCellStyle(totalStyle);

            // Auto-size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            return writeWorkbookToBytes(workbook);
        }
    }

    /**
     * Exporta reporte de ocupación (habitaciones) a Excel
     */
    public byte[] exportOccupancyReport(List<Room> rooms) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Reporte de Ocupación");

            // Estilos
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle currencyStyle = createCurrencyStyle(workbook);
            CellStyle statsStyle = createTotalStyle(workbook);

            // Header Row
            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "Número", "Tipo", "Capacidad", "Piso",
                              "Estado", "Precio por Noche"};

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Data Rows
            int rowNum = 1;
            long totalRooms = rooms.size();
            long occupiedRooms = rooms.stream().filter(r -> !r.getAvailable()).count();
            long availableRooms = rooms.stream().filter(Room::getAvailable).count();

            for (Room room : rooms) {
                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(room.getId());
                row.createCell(1).setCellValue(room.getRoomNumber());
                row.createCell(2).setCellValue(room.getRoomType().toString());
                row.createCell(3).setCellValue(room.getCapacity());
                row.createCell(4).setCellValue(room.getFloor());
                row.createCell(5).setCellValue(room.getAvailable() ? "Disponible" : "Ocupada");

                Cell priceCell = row.createCell(6);
                priceCell.setCellValue(room.getPrice().doubleValue());
                priceCell.setCellStyle(currencyStyle);
            }

            // Estadísticas
            int statsStartRow = rowNum + 2;

            Row statsHeader = sheet.createRow(statsStartRow);
            Cell statsHeaderCell = statsHeader.createCell(0);
            statsHeaderCell.setCellValue("ESTADÍSTICAS DE OCUPACIÓN");
            statsHeaderCell.setCellStyle(statsStyle);

            Row totalRoomsRow = sheet.createRow(statsStartRow + 2);
            totalRoomsRow.createCell(0).setCellValue("Total de Habitaciones:");
            totalRoomsRow.createCell(1).setCellValue(totalRooms);

            Row occupiedRow = sheet.createRow(statsStartRow + 3);
            occupiedRow.createCell(0).setCellValue("Habitaciones Ocupadas:");
            occupiedRow.createCell(1).setCellValue(occupiedRooms);

            Row availableRow = sheet.createRow(statsStartRow + 4);
            availableRow.createCell(0).setCellValue("Habitaciones Disponibles:");
            availableRow.createCell(1).setCellValue(availableRooms);

            Row occupancyRateRow = sheet.createRow(statsStartRow + 5);
            occupancyRateRow.createCell(0).setCellValue("% Ocupación:");
            double occupancyRate = totalRooms > 0 ? (occupiedRooms * 100.0 / totalRooms) : 0;
            occupancyRateRow.createCell(1).setCellValue(String.format("%.2f%%", occupancyRate));

            // Auto-size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            return writeWorkbookToBytes(workbook);
        }
    }

    // ========== Estilos ==========

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 12);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }

    private CellStyle createDateStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    private CellStyle createCurrencyStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        DataFormat format = workbook.createDataFormat();
        style.setDataFormat(format.getFormat("$#,##0.00"));
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    private CellStyle createTotalStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 11);
        style.setFont(font);
        DataFormat format = workbook.createDataFormat();
        style.setDataFormat(format.getFormat("$#,##0.00"));
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.MEDIUM);
        style.setBorderTop(BorderStyle.MEDIUM);
        style.setBorderLeft(BorderStyle.MEDIUM);
        style.setBorderRight(BorderStyle.MEDIUM);
        return style;
    }

    private byte[] writeWorkbookToBytes(Workbook workbook) throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }
}
