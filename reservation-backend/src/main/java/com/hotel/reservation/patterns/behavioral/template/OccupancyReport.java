package com.hotel.reservation.patterns.behavioral.template;

import com.hotel.reservation.models.Room;
import com.hotel.reservation.repositories.RoomRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * Reporte de ocupación de habitaciones
 */
@RequiredArgsConstructor
public class OccupancyReport extends ReportTemplate {

    private final RoomRepository roomRepository;

    @Override
    protected String getReportName() {
        return "REPORTE DE OCUPACIÓN";
    }

    @Override
    protected String generateBody() {
        List<Room> rooms = roomRepository.findAll();

        if (rooms.isEmpty()) {
            return "No hay habitaciones registradas.\n";
        }

        StringBuilder body = new StringBuilder();

        long totalRooms = rooms.size();
        long availableRooms = rooms.stream().filter(Room::isAvailable).count();
        long occupiedRooms = totalRooms - availableRooms;

        double occupancyRate = (occupiedRooms * 100.0) / totalRooms;

        body.append(String.format("Total de habitaciones: %d\n", totalRooms));
        body.append(String.format("Habitaciones disponibles: %d\n", availableRooms));
        body.append(String.format("Habitaciones ocupadas: %d\n", occupiedRooms));
        body.append(String.format("Tasa de ocupación: %.2f%%\n\n", occupancyRate));

        body.append("Detalle por tipo de habitación:\n");
        body.append("-".repeat(60)).append("\n");

        rooms.stream()
            .collect(java.util.stream.Collectors.groupingBy(Room::getRoomType))
            .forEach((type, roomList) -> {
                long available = roomList.stream().filter(Room::isAvailable).count();
                long occupied = roomList.size() - available;

                body.append(String.format("%s: %d total | %d disponibles | %d ocupadas\n",
                    type, roomList.size(), available, occupied));
            });

        body.append("-".repeat(60)).append("\n\n");

        body.append("Estado de habitaciones:\n");
        body.append("-".repeat(60)).append("\n");

        for (Room room : rooms) {
            String status = room.isAvailable() ? "✓ Disponible" : "✗ Ocupada";
            body.append(String.format("Habitación %s (%s): %s - $%.2f MXN\n",
                room.getRoomNumber(),
                room.getRoomType(),
                status,
                room.getPrice()));
        }

        return body.toString();
    }
}
