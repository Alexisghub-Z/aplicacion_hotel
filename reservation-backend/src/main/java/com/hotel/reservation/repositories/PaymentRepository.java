package com.hotel.reservation.repositories;

import com.hotel.reservation.models.Payment;
import com.hotel.reservation.models.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad Payment
 */
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    /**
     * Busca un pago por ID de reserva
     * @param reservationId ID de la reserva
     * @return Optional con el pago
     */
    Optional<Payment> findByReservationId(Long reservationId);

    /**
     * Busca pagos por estado
     * @param status estado del pago
     * @return lista de pagos
     */
    List<Payment> findByPaymentStatus(PaymentStatus status);

    /**
     * Busca un pago por ID de transacción
     * @param transactionId ID de transacción
     * @return Optional con el pago
     */
    Optional<Payment> findByTransactionId(String transactionId);

    /**
     * Verifica si existe un pago para una reserva
     * @param reservationId ID de la reserva
     * @return true si existe
     */
    boolean existsByReservationId(Long reservationId);
}
