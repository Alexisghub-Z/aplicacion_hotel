package com.hotel.reservation.service;

import com.hotel.reservation.dto.NotificationPreferenceDTO;
import com.hotel.reservation.exception.ResourceNotFoundException;
import com.hotel.reservation.models.Customer;
import com.hotel.reservation.models.NotificationPreference;
import com.hotel.reservation.repositories.CustomerRepository;
import com.hotel.reservation.repositories.NotificationPreferenceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificationPreferenceRepository preferenceRepository;
    private final CustomerRepository customerRepository;

    /**
     * Obtiene las preferencias de notificación de un cliente
     * Si no existen, crea unas por defecto
     */
    @Transactional(readOnly = true)
    public NotificationPreferenceDTO getPreferences(Long customerId) {
        NotificationPreference preference = preferenceRepository.findByCustomerId(customerId)
                .orElseGet(() -> createDefaultPreferences(customerId));

        return convertToDTO(preference);
    }

    /**
     * Actualiza las preferencias de notificación de un cliente
     */
    public NotificationPreferenceDTO updatePreferences(Long customerId, NotificationPreferenceDTO dto) {
        NotificationPreference preference = preferenceRepository.findByCustomerId(customerId)
                .orElseGet(() -> createDefaultPreferences(customerId));

        preference.setEmailEnabled(dto.getEmailEnabled());
        preference.setSmsEnabled(dto.getSmsEnabled());
        preference.setWhatsappEnabled(dto.getWhatsappEnabled());

        preference = preferenceRepository.save(preference);

        log.info("📋 Preferencias de notificación actualizadas para cliente #{}: Email={}, SMS={}, WhatsApp={}",
                customerId, dto.getEmailEnabled(), dto.getSmsEnabled(), dto.getWhatsappEnabled());

        return convertToDTO(preference);
    }

    /**
     * Crea preferencias por defecto para un cliente
     * Por defecto: Email activado, SMS y WhatsApp desactivados
     */
    private NotificationPreference createDefaultPreferences(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", customerId));

        NotificationPreference preference = NotificationPreference.builder()
                .customer(customer)
                .emailEnabled(true)
                .smsEnabled(false)
                .whatsappEnabled(false)
                .build();

        return preferenceRepository.save(preference);
    }

    /**
     * Convierte entidad a DTO
     */
    private NotificationPreferenceDTO convertToDTO(NotificationPreference preference) {
        return NotificationPreferenceDTO.builder()
                .id(preference.getId())
                .customerId(preference.getCustomer().getId())
                .emailEnabled(preference.getEmailEnabled())
                .smsEnabled(preference.getSmsEnabled())
                .whatsappEnabled(preference.getWhatsappEnabled())
                .activeChannelsCount(preference.getActiveChannelsCount())
                .build();
    }
}
