package com.hotel.reservation.controller;

import com.hotel.reservation.dto.CreatePackageDTO;
import com.hotel.reservation.dto.PackageDTO;
import com.hotel.reservation.service.PackageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestión de paquetes de servicios
 */
@Slf4j
@RestController
@RequestMapping("/api/packages")
@RequiredArgsConstructor
public class PackageController {

    private final PackageService packageService;

    /**
     * Obtener todos los paquetes
     */
    @GetMapping
    public ResponseEntity<List<PackageDTO>> getAllPackages(@RequestParam(required = false) Boolean activeOnly) {
        log.debug("GET /api/packages - activeOnly: {}", activeOnly);

        List<PackageDTO> packages = (activeOnly != null && activeOnly)
                ? packageService.getActivePackages()
                : packageService.getAllPackages();

        return ResponseEntity.ok(packages);
    }

    /**
     * Obtener paquete por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<PackageDTO> getPackageById(@PathVariable Long id) {
        log.debug("GET /api/packages/{}", id);
        PackageDTO packageDTO = packageService.getPackageById(id);
        return ResponseEntity.ok(packageDTO);
    }

    /**
     * Crear nuevo paquete
     */
    @PostMapping
    public ResponseEntity<PackageDTO> createPackage(@RequestBody CreatePackageDTO createPackageDTO) {
        log.info("POST /api/packages - {}", createPackageDTO.getName());
        PackageDTO createdPackage = packageService.createPackage(createPackageDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPackage);
    }

    /**
     * Actualizar paquete existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<PackageDTO> updatePackage(
            @PathVariable Long id,
            @RequestBody CreatePackageDTO createPackageDTO) {
        log.info("PUT /api/packages/{} - {}", id, createPackageDTO.getName());
        PackageDTO updatedPackage = packageService.updatePackage(id, createPackageDTO);
        return ResponseEntity.ok(updatedPackage);
    }

    /**
     * Eliminar paquete
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePackage(@PathVariable Long id) {
        log.info("DELETE /api/packages/{}", id);
        packageService.deletePackage(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Activar/desactivar paquete
     */
    @PatchMapping("/{id}/toggle-active")
    public ResponseEntity<Void> togglePackageActive(@PathVariable Long id) {
        log.info("PATCH /api/packages/{}/toggle-active", id);
        packageService.togglePackageActive(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Buscar paquetes por nombre
     */
    @GetMapping("/search")
    public ResponseEntity<List<PackageDTO>> searchPackages(@RequestParam String name) {
        log.debug("GET /api/packages/search?name={}", name);
        List<PackageDTO> packages = packageService.searchPackagesByName(name);
        return ResponseEntity.ok(packages);
    }
}
