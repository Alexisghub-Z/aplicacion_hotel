package com.hotel.reservation.repositories;

import com.hotel.reservation.models.Customer;
import com.hotel.reservation.models.NotificationPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotificationPreferenceRepository extends JpaRepository<NotificationPreference, Long> {

    /**
     * Busca las preferencias de notificación por cliente
     */
    Optional<NotificationPreference> findByCustomer(Customer customer);

    /**
     * Busca las preferencias de notificación por ID de cliente
     */
    Optional<NotificationPreference> findByCustomerId(Long customerId);

    /**
     * Verifica si un cliente ya tiene preferencias configuradas
     */
    boolean existsByCustomerId(Long customerId);
}
