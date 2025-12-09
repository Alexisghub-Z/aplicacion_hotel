package com.hotel.reservation.patterns.behavioral.mediator;

import com.hotel.reservation.dto.ReservationDTO;
import com.hotel.reservation.models.Customer;
import com.hotel.reservation.models.Room;
import com.hotel.reservation.repositories.CustomerRepository;
import com.hotel.reservation.repositories.RoomRepository;
import com.hotel.reservation.service.PaymentService;
import com.hotel.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Mediador concreto que coordina el proceso de reserva
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class HotelBookingMediator implements BookingMediator {

    private final ReservationService reservationService;
    private final PaymentService paymentService;
    private final RoomRepository roomRepository;
    private final CustomerRepository customerRepository;

    @Override
    public void notifyRoomBooked(Room room, Customer customer) {
        log.info("🏨 Mediador: Habitación {} reservada para {} {}",
            room.getRoomNumber(),
            customer.getFirstName(),
            customer.getLastName());

        // Actualizar disponibilidad de la habitación
        room.setAvailable(false);
        roomRepository.save(room);

        log.info("✅ Mediador: Habitación marcada como no disponible");
    }

    @Override
    public void notifyRoomReleased(Room room) {
        log.info("🏨 Mediador: Liberando habitación {}", room.getRoomNumber());

        room.setAvailable(true);
        roomRepository.save(room);

        log.info("✅ Mediador: Habitación marcada como disponible");
    }

    @Override
    public void notifyPaymentProcessed(Long reservationId, String paymentMethod) {
        log.info("💳 Mediador: Pago procesado para reserva #{} vía {}",
            reservationId, paymentMethod);

        // Confirmar la reserva automáticamente cuando se procesa el pago
        reservationService.confirmReservation(reservationId);

        log.info("✅ Mediador: Reserva confirmada automáticamente");
    }

    @Override
    public ReservationDTO processBooking(ReservationDTO reservationDTO) {
        log.info("🎯 Mediador: Procesando reserva completa...");

        // 1. Crear la reserva
        ReservationDTO created = reservationService.createReservation(reservationDTO);

        log.info("📝 Mediador: Reserva #{} creada", created.getId());

        // 2. Notificar que la habitación fue reservada
        Room room = roomRepository.findById(reservationDTO.getRoomId())
            .orElseThrow(() -> new RuntimeException("Habitación no encontrada"));

        Customer customer = customerRepository.findById(reservationDTO.getCustomerId())
            .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        notifyRoomBooked(room, customer);

        log.info("✅ Mediador: Proceso de reserva completado");

        return created;
    }
}
