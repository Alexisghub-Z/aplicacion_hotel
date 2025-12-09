package com.hotel.reservation.controller;

import com.hotel.reservation.dto.AdditionalServiceDTO;
import com.hotel.reservation.models.ServiceType;
import com.hotel.reservation.service.AdditionalServiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestión de servicios adicionales
 *
 * Endpoints:
 * - GET /api/services - Listar todos los servicios
 * - GET /api/services/{id} - Obtener servicio por ID
 * - GET /api/services/type/{type} - Buscar servicios por tipo
 * - POST /api/services - Crear nuevo servicio
 * - PUT /api/services/{id} - Actualizar servicio
 * - DELETE /api/services/{id} - Eliminar servicio
 */
@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class AdditionalServiceController {

    private final AdditionalServiceService serviceService;

    /**
     * Listar todos los servicios
     * GET /api/services
     */
    @GetMapping
    public ResponseEntity<List<AdditionalServiceDTO>> getAllServices() {
        List<AdditionalServiceDTO> services = serviceService.getAllServices();
        return ResponseEntity.ok(services);
    }

    /**
     * Obtener servicio por ID
     * GET /api/services/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<AdditionalServiceDTO> getServiceById(@PathVariable Long id) {
        AdditionalServiceDTO service = serviceService.getServiceById(id);
        return ResponseEntity.ok(service);
    }

    /**
     * Buscar servicios por tipo
     * GET /api/services/type/{type}
     */
    @GetMapping("/type/{type}")
    public ResponseEntity<List<AdditionalServiceDTO>> getServicesByType(@PathVariable ServiceType type) {
        List<AdditionalServiceDTO> services = serviceService.getServicesByType(type);
        return ResponseEntity.ok(services);
    }

    /**
     * Crear nuevo servicio
     * POST /api/services
     */
    @PostMapping
    public ResponseEntity<AdditionalServiceDTO> createService(@Valid @RequestBody AdditionalServiceDTO serviceDTO) {
        AdditionalServiceDTO createdService = serviceService.createService(serviceDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdService);
    }

    /**
     * Actualizar servicio
     * PUT /api/services/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<AdditionalServiceDTO> updateService(
            @PathVariable Long id,
            @Valid @RequestBody AdditionalServiceDTO serviceDTO) {

        AdditionalServiceDTO updatedService = serviceService.updateService(id, serviceDTO);
        return ResponseEntity.ok(updatedService);
    }

    /**
     * Eliminar servicio
     * DELETE /api/services/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable Long id) {
        serviceService.deleteService(id);
        return ResponseEntity.noContent().build();
    }
}
