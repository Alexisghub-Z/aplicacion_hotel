package com.hotel.reservation.controller;

import com.hotel.reservation.dto.ReservationDTO;
import com.hotel.reservation.patterns.behavioral.memento.ReservationMemento;
import com.hotel.reservation.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping
    public ResponseEntity<List<ReservationDTO>> getAllReservations() {
        return ResponseEntity.ok(reservationService.getAllReservations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationDTO> getReservationById(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.getReservationById(id));
    }

    @PostMapping
    public ResponseEntity<ReservationDTO> createReservation(@Valid @RequestBody ReservationDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reservationService.createReservation(dto));
    }

    @PatchMapping("/{id}/confirm")
    public ResponseEntity<Void> confirmReservation(@PathVariable Long id) {
        reservationService.confirmReservation(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelReservation(@PathVariable Long id) {
        reservationService.cancelReservation(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservationDTO> updateReservation(
            @PathVariable Long id,
            @Valid @RequestBody ReservationDTO dto) {
        return ResponseEntity.ok(reservationService.updateReservation(id, dto));
    }

    @PostMapping("/{id}/undo")
    public ResponseEntity<ReservationDTO> undoReservationChange(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.undoReservationChange(id));
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<List<ReservationMemento>> getReservationHistory(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.getReservationHistory(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ReservationDTO>> searchReservationsByEmail(@RequestParam String email) {
        return ResponseEntity.ok(reservationService.getReservationsByEmail(email));
    }

    @GetMapping("/room/{roomId}/occupied-dates")
    public ResponseEntity<List<String>> getOccupiedDates(@PathVariable Long roomId) {
        return ResponseEntity.ok(reservationService.getOccupiedDatesByRoom(roomId));
    }
}
