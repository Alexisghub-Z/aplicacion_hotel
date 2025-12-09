package com.hotel.reservation.service;

import com.hotel.reservation.dto.AdditionalServiceDTO;
import com.hotel.reservation.exception.ResourceNotFoundException;
import com.hotel.reservation.models.AdditionalService;
import com.hotel.reservation.models.ServiceType;
import com.hotel.reservation.patterns.creational.singleton.ConfigurationManager;
import com.hotel.reservation.repositories.AdditionalServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Servicio de negocio para gestión de servicios adicionales
 */
@Service
@RequiredArgsConstructor
@Transactional
public class AdditionalServiceService {

    private final AdditionalServiceRepository serviceRepository;

    /**
     * Obtener todos los servicios
     */
    @Transactional(readOnly = true)
    public List<AdditionalServiceDTO> getAllServices() {
        return serviceRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtener servicio por ID
     */
    @Transactional(readOnly = true)
    public AdditionalServiceDTO getServiceById(Long id) {
        AdditionalService service = serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AdditionalService", "id", id));
        return convertToDTO(service);
    }

    /**
     * Obtener servicios por tipo
     */
    @Transactional(readOnly = true)
    public List<AdditionalServiceDTO> getServicesByType(ServiceType type) {
        return serviceRepository.findByServiceType(type)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Crear nuevo servicio
     */
    public AdditionalServiceDTO createService(AdditionalServiceDTO serviceDTO) {
        AdditionalService service = AdditionalService.builder()
                .name(serviceDTO.getName())
                .description(serviceDTO.getDescription())
                .price(serviceDTO.getPrice())
                .serviceType(serviceDTO.getServiceType())
                .build();

        AdditionalService savedService = serviceRepository.save(service);
        return convertToDTO(savedService);
    }

    /**
     * Actualizar servicio
     */
    public AdditionalServiceDTO updateService(Long id, AdditionalServiceDTO serviceDTO) {
        AdditionalService service = serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AdditionalService", "id", id));

        service.setName(serviceDTO.getName());
        service.setDescription(serviceDTO.getDescription());
        service.setPrice(serviceDTO.getPrice());
        service.setServiceType(serviceDTO.getServiceType());

        AdditionalService updatedService = serviceRepository.save(service);
        return convertToDTO(updatedService);
    }

    /**
     * Eliminar servicio
     */
    public void deleteService(Long id) {
        AdditionalService service = serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AdditionalService", "id", id));
        serviceRepository.delete(service);
    }

    /**
     * Convertir AdditionalService entity a AdditionalServiceDTO
     * Utiliza Singleton Pattern para obtener configuración de moneda e idioma
     */
    public AdditionalServiceDTO convertServiceToDTO(AdditionalService service) {
        return convertToDTO(service);
    }

    private AdditionalServiceDTO convertToDTO(AdditionalService service) {
        ConfigurationManager config = ConfigurationManager.INSTANCE;
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("es", "MX"));
        String formattedPrice = currencyFormatter.format(service.getPrice());

        return AdditionalServiceDTO.builder()
                .id(service.getId())
                .name(service.getName())
                .description(service.getDescription())
                .price(service.getPrice())
                .serviceType(service.getServiceType())
                .formattedPrice(formattedPrice)
                .build();
    }
}
