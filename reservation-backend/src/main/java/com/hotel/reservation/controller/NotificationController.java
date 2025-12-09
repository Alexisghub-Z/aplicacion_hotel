package com.hotel.reservation.controller;

import com.hotel.reservation.dto.NotificationPreferenceDTO;
import com.hotel.reservation.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador para gestionar las preferencias de notificación de los clientes
 */
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * Obtiene las preferencias de notificación de un cliente
     */
    @GetMapping("/preferences/{customerId}")
    public ResponseEntity<NotificationPreferenceDTO> getPreferences(@PathVariable Long customerId) {
        return ResponseEntity.ok(notificationService.getPreferences(customerId));
    }

    /**
     * Actualiza las preferencias de notificación de un cliente
     */
    @PutMapping("/preferences/{customerId}")
    public ResponseEntity<NotificationPreferenceDTO> updatePreferences(
            @PathVariable Long customerId,
            @RequestBody NotificationPreferenceDTO dto) {
        return ResponseEntity.ok(notificationService.updatePreferences(customerId, dto));
    }
}
