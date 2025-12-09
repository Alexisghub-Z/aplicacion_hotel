package com.hotel.reservation.repositories;

import com.hotel.reservation.models.Reservation;
import com.hotel.reservation.models.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repositorio JPA para la entidad Reservation
 */
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    /**
     * Busca reservas por cliente
     * @param customerId ID del cliente
     * @return lista de reservas
     */
    List<Reservation> findByCustomerId(Long customerId);

    /**
     * Busca reservas por estado
     * @param status estado de la reserva
     * @return lista de reservas
     */
    List<Reservation> findByStatus(ReservationStatus status);

    /**
     * Busca reservas en un rango de fechas de check-in
     * @param start fecha inicial
     * @param end fecha final
     * @return lista de reservas
     */
    List<Reservation> findByCheckInDateBetween(LocalDate start, LocalDate end);

    /**
     * Busca reservas por habitación
     * @param roomId ID de la habitación
     * @return lista de reservas
     */
    List<Reservation> findByRoomId(Long roomId);

    /**
     * Busca reservas activas (PENDING o CONFIRMED)
     * @return lista de reservas activas
     */
    @Query("SELECT r FROM Reservation r WHERE r.status IN ('PENDING', 'CONFIRMED')")
    List<Reservation> findActiveReservations();

    /**
     * Busca reservas de un cliente en un rango de fechas
     * @param customerId ID del cliente
     * @param start fecha inicial
     * @param end fecha final
     * @return lista de reservas
     */
    @Query("SELECT r FROM Reservation r WHERE r.customer.id = :customerId AND " +
           "r.checkInDate >= :start AND r.checkOutDate <= :end")
    List<Reservation> findByCustomerAndDateRange(@Param("customerId") Long customerId,
                                                   @Param("start") LocalDate start,
                                                   @Param("end") LocalDate end);

    /**
     * Cuenta reservas por estado en un rango de fechas
     * @param status estado
     * @param start fecha inicial
     * @param end fecha final
     * @return cantidad de reservas
     */
    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.status = :status AND " +
           "r.checkInDate >= :start AND r.checkOutDate <= :end")
    long countByStatusAndDateRange(@Param("status") ReservationStatus status,
                                     @Param("start") LocalDate start,
                                     @Param("end") LocalDate end);

    /**
     * Busca reservas por email del cliente
     * @param email email del cliente
     * @return lista de reservas
     */
    @Query("SELECT r FROM Reservation r WHERE r.customer.email = :email ORDER BY r.checkInDate DESC")
    List<Reservation> findByCustomerEmail(@Param("email") String email);
}
