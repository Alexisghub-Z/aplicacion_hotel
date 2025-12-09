package com.hotel.reservation.service;

import com.hotel.reservation.dto.AdditionalServiceDTO;
import com.hotel.reservation.dto.CreatePackageDTO;
import com.hotel.reservation.dto.PackageDTO;
import com.hotel.reservation.exception.ResourceNotFoundException;
import com.hotel.reservation.models.AdditionalService;
import com.hotel.reservation.models.Package;
import com.hotel.reservation.patterns.creational.singleton.ConfigurationManager;
import com.hotel.reservation.repositories.AdditionalServiceRepository;
import com.hotel.reservation.repositories.PackageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Servicio de negocio para gestión de paquetes de servicios
 * Implementa el patrón Composite para agrupar servicios
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PackageService {

    private final PackageRepository packageRepository;
    private final AdditionalServiceRepository serviceRepository;
    private final AdditionalServiceService additionalServiceService;
    private final ConfigurationManager config = ConfigurationManager.INSTANCE;

    /**
     * Obtener todos los paquetes
     */
    @Transactional(readOnly = true)
    public List<PackageDTO> getAllPackages() {
        return packageRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtener solo paquetes activos
     */
    @Transactional(readOnly = true)
    public List<PackageDTO> getActivePackages() {
        return packageRepository.findByActiveTrue().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtener paquete por ID
     */
    @Transactional(readOnly = true)
    public PackageDTO getPackageById(Long id) {
        Package pkg = packageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Package", "id", id));
        return convertToDTO(pkg);
    }

    /**
     * Crear nuevo paquete
     */
    public PackageDTO createPackage(CreatePackageDTO dto) {
        // Validar que haya al menos un servicio
        if (dto.getServiceIds() == null || dto.getServiceIds().isEmpty()) {
            throw new IllegalArgumentException("El paquete debe contener al menos un servicio");
        }

        // Buscar todos los servicios
        List<AdditionalService> services = dto.getServiceIds().stream()
                .map(id -> serviceRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("AdditionalService", "id", id)))
                .collect(Collectors.toList());

        // Crear paquete
        Package pkg = Package.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .discount(dto.getDiscount() != null ? dto.getDiscount() : BigDecimal.ZERO)
                .imageUrl(dto.getImageUrl())
                .active(dto.getActive() != null ? dto.getActive() : true)
                .services(services)
                .build();

        Package savedPackage = packageRepository.save(pkg);
        log.info("📦 Paquete creado: {} - {} servicios incluidos - Descuento: {}%",
                savedPackage.getName(),
                savedPackage.getServices().size(),
                savedPackage.getDiscount().multiply(BigDecimal.valueOf(100)));

        return convertToDTO(savedPackage);
    }

    /**
     * Actualizar paquete existente
     */
    public PackageDTO updatePackage(Long id, CreatePackageDTO dto) {
        Package pkg = packageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Package", "id", id));

        // Actualizar campos básicos
        pkg.setName(dto.getName());
        pkg.setDescription(dto.getDescription());
        pkg.setDiscount(dto.getDiscount() != null ? dto.getDiscount() : BigDecimal.ZERO);
        pkg.setImageUrl(dto.getImageUrl());
        pkg.setActive(dto.getActive() != null ? dto.getActive() : true);

        // Actualizar servicios si se especificaron
        if (dto.getServiceIds() != null && !dto.getServiceIds().isEmpty()) {
            List<AdditionalService> services = dto.getServiceIds().stream()
                    .map(serviceId -> serviceRepository.findById(serviceId)
                            .orElseThrow(() -> new ResourceNotFoundException("AdditionalService", "id", serviceId)))
                    .collect(Collectors.toList());
            pkg.setServices(services);
        }

        Package updatedPackage = packageRepository.save(pkg);
        log.info("📦 Paquete actualizado: {}", updatedPackage.getName());

        return convertToDTO(updatedPackage);
    }

    /**
     * Eliminar paquete
     */
    public void deletePackage(Long id) {
        Package pkg = packageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Package", "id", id));

        packageRepository.delete(pkg);
        log.info("📦 Paquete eliminado: {}", pkg.getName());
    }

    /**
     * Activar/desactivar paquete
     */
    public void togglePackageActive(Long id) {
        Package pkg = packageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Package", "id", id));

        pkg.setActive(!pkg.getActive());
        packageRepository.save(pkg);

        log.info("📦 Paquete {} {}: {}",
                pkg.getActive() ? "activado" : "desactivado",
                pkg.getName(),
                pkg.getId());
    }

    /**
     * Buscar paquetes por nombre
     */
    @Transactional(readOnly = true)
    public List<PackageDTO> searchPackagesByName(String name) {
        return packageRepository.findByNameContainingIgnoreCase(name).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Convertir Package entity a PackageDTO
     */
    private PackageDTO convertToDTO(Package pkg) {
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("es", "MX"));

        List<AdditionalServiceDTO> serviceDTOs = pkg.getServices().stream()
                .map(additionalServiceService::convertServiceToDTO)
                .collect(Collectors.toList());

        BigDecimal originalPrice = pkg.getOriginalPrice();
        BigDecimal finalPrice = pkg.getFinalPrice();
        BigDecimal savings = pkg.getSavings();

        return PackageDTO.builder()
                .id(pkg.getId())
                .name(pkg.getName())
                .description(pkg.getDescription())
                .discount(pkg.getDiscount())
                .imageUrl(pkg.getImageUrl())
                .active(pkg.getActive())
                .services(serviceDTOs)
                .originalPrice(originalPrice)
                .finalPrice(finalPrice)
                .formattedFinalPrice(currencyFormatter.format(finalPrice))
                .savings(savings)
                .discountPercentage(pkg.getDiscount().multiply(BigDecimal.valueOf(100)).intValue())
                .serviceCount(pkg.getServices().size())
                .build();
    }
}
