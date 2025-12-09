package com.hotel.reservation.repositories;

import com.hotel.reservation.models.Customer;
import com.hotel.reservation.models.LoyaltyLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad Customer
 * Proporciona operaciones CRUD y consultas personalizadas
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    /**
     * Busca un cliente por su email
     * @param email email del cliente
     * @return Optional con el cliente si existe
     */
    Optional<Customer> findByEmail(String email);

    /**
     * Busca clientes por nivel de lealtad
     * @param level nivel de lealtad
     * @return lista de clientes con ese nivel
     */
    List<Customer> findByLoyaltyLevel(LoyaltyLevel level);

    /**
     * Verifica si existe un cliente con el email dado
     * @param email email a verificar
     * @return true si existe
     */
    boolean existsByEmail(String email);

    /**
     * Busca clientes por nombre (ignora mayúsculas/minúsculas)
     * @param firstName nombre
     * @return lista de clientes
     */
    List<Customer> findByFirstNameContainingIgnoreCase(String firstName);

    /**
     * Busca clientes por apellido (ignora mayúsculas/minúsculas)
     * @param lastName apellido
     * @return lista de clientes
     */
    List<Customer> findByLastNameContainingIgnoreCase(String lastName);
}
