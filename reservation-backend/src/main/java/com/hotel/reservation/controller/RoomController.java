package com.hotel.reservation.controller;

import com.hotel.reservation.dto.RoomDTO;
import com.hotel.reservation.models.RoomType;
import com.hotel.reservation.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Controlador REST para gestión de habitaciones
 *
 * Endpoints:
 * - GET /api/rooms - Listar todas las habitaciones
 * - GET /api/rooms/{id} - Obtener habitación por ID
 * - GET /api/rooms/available - Buscar habitaciones disponibles
 * - POST /api/rooms - Crear nueva habitación (Factory Pattern)
 * - POST /api/rooms/{id}/clone - Clonar habitación (Prototype Pattern)
 * - PUT /api/rooms/{id} - Actualizar habitación
 * - DELETE /api/rooms/{id} - Eliminar habitación
 * - PATCH /api/rooms/{id}/toggle-availability - Cambiar disponibilidad
 */
@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class RoomController {

    private final RoomService roomService;

    /**
     * Listar todas las habitaciones
     * GET /api/rooms
     */
    @GetMapping
    public ResponseEntity<List<RoomDTO>> getAllRooms() {
        List<RoomDTO> rooms = roomService.getAllRooms();
        return ResponseEntity.ok(rooms);
    }

    /**
     * Obtener habitación por ID
     * GET /api/rooms/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<RoomDTO> getRoomById(@PathVariable Long id) {
        RoomDTO room = roomService.getRoomById(id);
        return ResponseEntity.ok(room);
    }

    /**
     * Buscar habitaciones disponibles en un rango de fechas
     * GET /api/rooms/available?checkIn=2025-12-20&checkOut=2025-12-25
     */
    @GetMapping("/available")
    public ResponseEntity<List<RoomDTO>> getAvailableRooms(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut,
            @RequestParam(required = false) RoomType roomType) {

        List<RoomDTO> rooms;
        if (roomType != null) {
            rooms = roomService.findAvailableRoomsByType(checkIn, checkOut, roomType);
        } else {
            rooms = roomService.findAvailableRooms(checkIn, checkOut);
        }
        return ResponseEntity.ok(rooms);
    }

    /**
     * Crear nueva habitación usando Factory Pattern
     * POST /api/rooms
     * Body: { "roomType": "SUITE", "roomNumber": "405", "floor": 4 }
     */
    @PostMapping
    public ResponseEntity<RoomDTO> createRoom(
            @RequestParam RoomType roomType,
            @RequestParam String roomNumber,
            @RequestParam Integer floor) {

        RoomDTO room = roomService.createRoom(roomType, roomNumber, floor);
        return ResponseEntity.status(HttpStatus.CREATED).body(room);
    }

    /**
     * Clonar habitación existente (Prototype Pattern)
     * POST /api/rooms/{id}/clone
     * Body: { "newRoomNumber": "405" }
     */
    @PostMapping("/{id}/clone")
    public ResponseEntity<RoomDTO> cloneRoom(
            @PathVariable Long id,
            @RequestParam String newRoomNumber) {

        RoomDTO clonedRoom = roomService.cloneRoom(id, newRoomNumber);
        return ResponseEntity.status(HttpStatus.CREATED).body(clonedRoom);
    }

    /**
     * Actualizar habitación
     * PUT /api/rooms/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<RoomDTO> updateRoom(
            @PathVariable Long id,
            @Valid @RequestBody RoomDTO roomDTO) {

        RoomDTO updatedRoom = roomService.updateRoom(id, roomDTO);
        return ResponseEntity.ok(updatedRoom);
    }

    /**
     * Cambiar disponibilidad de habitación
     * PATCH /api/rooms/{id}/toggle-availability
     */
    @PatchMapping("/{id}/toggle-availability")
    public ResponseEntity<Void> toggleRoomAvailability(@PathVariable Long id) {
        roomService.toggleRoomAvailability(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Eliminar habitación
     * DELETE /api/rooms/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Búsqueda avanzada de habitaciones (Interpreter Pattern)
     * GET /api/rooms/search/luxury-families
     * Busca Suites o Presidenciales disponibles con capacidad >= 4
     */
    @GetMapping("/search/luxury-families")
    public ResponseEntity<List<RoomDTO>> findLuxuryForFamilies() {
        return ResponseEntity.ok(roomService.findLuxuryRoomsForFamilies());
    }

    /**
     * Búsqueda por rango de precio (Interpreter Pattern)
     * GET /api/rooms/search/price?min=1000&max=3000
     */
    @GetMapping("/search/price")
    public ResponseEntity<List<RoomDTO>> findByPriceRange(
            @RequestParam BigDecimal min,
            @RequestParam BigDecimal max) {
        return ResponseEntity.ok(roomService.findByPriceRange(min, max));
    }

    /**
     * Búsqueda por capacidad (Interpreter Pattern)
     * GET /api/rooms/search/capacity?guests=4
     */
    @GetMapping("/search/capacity")
    public ResponseEntity<List<RoomDTO>> findByCapacity(@RequestParam int guests) {
        return ResponseEntity.ok(roomService.findByCapacity(guests));
    }
}
