package com.hotel.reservation.repositories;

import com.hotel.reservation.models.Room;
import com.hotel.reservation.models.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad Room
 */
@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    /**
     * Busca habitaciones por tipo
     * @param type tipo de habitación
     * @return lista de habitaciones
     */
    List<Room> findByRoomType(RoomType type);

    /**
     * Busca habitaciones disponibles
     * @return lista de habitaciones disponibles
     */
    List<Room> findByAvailableTrue();

    /**
     * Busca una habitación por su número
     * @param roomNumber número de habitación
     * @return Optional con la habitación
     */
    Optional<Room> findByRoomNumber(String roomNumber);

    /**
     * Busca habitaciones en un rango de precios
     * @param min precio mínimo
     * @param max precio máximo
     * @return lista de habitaciones
     */
    List<Room> findByPriceBetween(BigDecimal min, BigDecimal max);

    /**
     * Busca habitaciones disponibles de un tipo específico
     * @param type tipo de habitación
     * @return lista de habitaciones
     */
    List<Room> findByRoomTypeAndAvailableTrue(RoomType type);

    /**
     * Busca habitaciones disponibles en un rango de fechas
     * Excluye habitaciones con reservas en esas fechas
     */
    @Query("SELECT r FROM Room r WHERE r.available = true AND r.id NOT IN " +
           "(SELECT res.room.id FROM Reservation res WHERE " +
           "(res.checkInDate <= :checkOut AND res.checkOutDate >= :checkIn) AND " +
           "(res.status = 'CONFIRMED' OR res.status = 'PENDING'))")
    List<Room> findAvailableRooms(@Param("checkIn") LocalDate checkIn,
                                   @Param("checkOut") LocalDate checkOut);

    /**
     * Cuenta habitaciones por tipo
     * @param type tipo de habitación
     * @return cantidad de habitaciones
     */
    long countByRoomType(RoomType type);
}
