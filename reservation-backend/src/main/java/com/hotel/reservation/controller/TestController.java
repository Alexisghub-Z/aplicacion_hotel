package com.hotel.reservation.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Controlador de prueba para verificar que el servidor está funcionando
 */
@RestController
public class TestController {

    @GetMapping("/api/test")
    public Map<String, Object> test() {
        return Map.of(
            "message", "Hotel Oaxaca Dreams API está funcionando correctamente",
            "status", "OK",
            "version", "Fase 3 - Completada"
        );
    }

    @GetMapping("/")
    public Map<String, Object> home() {
        return Map.of(
            "name", "Hotel Oaxaca Dreams - Reservation API",
            "version", "v1.0.0",
            "status", "running",
            "endpoints", Map.of(
                "test", "/api/test",
                "rooms", "/api/rooms",
                "customers", "/api/customers",
                "services", "/api/services",
                "reservations", "/api/reservations",
                "payments", "/api/payments"
            )
        );
    }
}
