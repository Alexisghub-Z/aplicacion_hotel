package com.hotel.reservation.repositories;

import com.hotel.reservation.models.AdditionalService;
import com.hotel.reservation.models.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio JPA para la entidad AdditionalService
 */
@Repository
public interface AdditionalServiceRepository extends JpaRepository<AdditionalService, Long> {

    /**
     * Busca servicios por tipo
     * @param type tipo de servicio
     * @return lista de servicios
     */
    List<AdditionalService> findByServiceType(ServiceType type);

    /**
     * Busca servicios por nombre (ignora mayúsculas/minúsculas)
     * @param name nombre del servicio
     * @return lista de servicios
     */
    List<AdditionalService> findByNameContainingIgnoreCase(String name);
}
