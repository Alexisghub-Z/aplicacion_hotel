package com.hotel.reservation.repositories;

import com.hotel.reservation.models.Package;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository para gestión de paquetes de servicios
 */
@Repository
public interface PackageRepository extends JpaRepository<Package, Long> {

    /**
     * Encuentra todos los paquetes activos
     * @return lista de paquetes activos
     */
    List<Package> findByActiveTrue();

    /**
     * Encuentra paquetes por nombre (búsqueda parcial)
     * @param name nombre a buscar
     * @return lista de paquetes
     */
    List<Package> findByNameContainingIgnoreCase(String name);

    /**
     * Encuentra paquetes que contienen un servicio específico
     * @param serviceId ID del servicio
     * @return lista de paquetes
     */
    @Query("SELECT p FROM Package p JOIN p.services s WHERE s.id = :serviceId")
    List<Package> findByServiceId(Long serviceId);
}
