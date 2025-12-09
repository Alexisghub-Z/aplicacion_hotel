package com.hotel.reservation.service;

import com.hotel.reservation.dto.ReservationDTO;
import com.hotel.reservation.exception.ResourceNotFoundException;
import com.hotel.reservation.models.*;
import com.hotel.reservation.models.NotificationPreference;
import com.hotel.reservation.patterns.behavioral.observer.EmailNotificationObserver;
import com.hotel.reservation.patterns.behavioral.observer.ReservationObserver;
import com.hotel.reservation.patterns.behavioral.observer.SmsNotificationObserver;
import com.hotel.reservation.patterns.behavioral.observer.WhatsAppNotificationObserver;
import com.hotel.reservation.patterns.behavioral.strategy.*;
import com.hotel.reservation.patterns.behavioral.memento.ReservationHistory;
import com.hotel.reservation.patterns.behavioral.memento.ReservationMemento;
import com.hotel.reservation.patterns.behavioral.memento.ReservationOriginator;
import com.hotel.reservation.repositories.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final CustomerRepository customerRepository;
    private final RoomRepository roomRepository;
    private final AdditionalServiceRepository serviceRepository;
    private final NotificationPreferenceRepository notificationPreferenceRepository;
    private final PackageRepository packageRepository;

    private final EmailNotificationObserver emailObserver;
    private final SmsNotificationObserver smsObserver;
    private final WhatsAppNotificationObserver whatsappObserver;

    private final ReservationHistory reservationHistory;

    public ReservationService(ReservationRepository reservationRepository,
                            CustomerRepository customerRepository,
                            RoomRepository roomRepository,
                            AdditionalServiceRepository serviceRepository,
                            NotificationPreferenceRepository notificationPreferenceRepository,
                            PackageRepository packageRepository,
                            EmailNotificationObserver emailObserver,
                            SmsNotificationObserver smsObserver,
                            WhatsAppNotificationObserver whatsappObserver,
                            ReservationHistory reservationHistory) {
        this.reservationRepository = reservationRepository;
        this.customerRepository = customerRepository;
        this.roomRepository = roomRepository;
        this.serviceRepository = serviceRepository;
        this.notificationPreferenceRepository = notificationPreferenceRepository;
        this.packageRepository = packageRepository;
        this.emailObserver = emailObserver;
        this.smsObserver = smsObserver;
        this.whatsappObserver = whatsappObserver;
        this.reservationHistory = reservationHistory;
    }

    @Transactional(readOnly = true)
    public List<ReservationDTO> getAllReservations() {
        return reservationRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ReservationDTO getReservationById(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", "id", id));
        return convertToDTO(reservation);
    }

    @Transactional(readOnly = true)
    public List<ReservationDTO> getReservationsByEmail(String email) {
        return reservationRepository.findByCustomerEmail(email).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<String> getOccupiedDatesByRoom(Long roomId) {
        List<Reservation> activeReservations = reservationRepository.findByRoomId(roomId).stream()
                .filter(r -> r.getStatus() == ReservationStatus.PENDING || r.getStatus() == ReservationStatus.CONFIRMED)
                .collect(Collectors.toList());

        List<String> occupiedDates = new ArrayList<>();
        for (Reservation reservation : activeReservations) {
            LocalDate current = reservation.getCheckInDate();
            while (!current.isAfter(reservation.getCheckOutDate().minusDays(1))) {
                occupiedDates.add(current.toString());
                current = current.plusDays(1);
            }
        }
        return occupiedDates;
    }

    public ReservationDTO createReservation(ReservationDTO dto) {
        Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", dto.getCustomerId()));

        Room room = roomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Room", "id", dto.getRoomId()));

        // Calcular precio con Strategy Pattern
        long nights = ChronoUnit.DAYS.between(dto.getCheckInDate(), dto.getCheckOutDate());
        BigDecimal basePrice = room.getPrice().multiply(BigDecimal.valueOf(nights));

        PricingContext pricingContext = new PricingContext();
        pricingContext.addStrategy(new SeasonalPricingStrategy());
        pricingContext.addStrategy(new WeekendPricingStrategy());
        pricingContext.addStrategy(new LoyaltyPricingStrategy(customer.getLoyaltyLevel()));

        BigDecimal finalPrice = pricingContext.calculateFinalPrice(basePrice, dto.getCheckInDate(), dto.getCheckOutDate());

        Reservation reservation = Reservation.builder()
                .customer(customer)
                .room(room)
                .checkInDate(dto.getCheckInDate())
                .checkOutDate(dto.getCheckOutDate())
                .numberOfGuests(dto.getNumberOfGuests())
                .totalPrice(finalPrice)
                .status(ReservationStatus.PENDING)
                .build();

        // Si seleccionó un paquete, agregar sus servicios automáticamente
        if (dto.getPackageId() != null) {
            com.hotel.reservation.models.Package pkg = packageRepository.findById(dto.getPackageId())
                    .orElseThrow(() -> new ResourceNotFoundException("Package", "id", dto.getPackageId()));

            // Agregar todos los servicios del paquete a la reserva
            for (AdditionalService service : pkg.getServices()) {
                reservation.addService(service);
            }

            // Agregar el precio del paquete con descuento al total
            finalPrice = finalPrice.add(pkg.getFinalPrice());
            reservation.setTotalPrice(finalPrice);
        }
        // Si seleccionó servicios adicionales individuales
        else if (dto.getAdditionalServiceIds() != null && !dto.getAdditionalServiceIds().isEmpty()) {
            BigDecimal servicesPrice = BigDecimal.ZERO;
            for (Long serviceId : dto.getAdditionalServiceIds()) {
                AdditionalService service = serviceRepository.findById(serviceId)
                        .orElseThrow(() -> new ResourceNotFoundException("AdditionalService", "id", serviceId));
                reservation.addService(service);
                servicesPrice = servicesPrice.add(service.getPrice());
            }
            finalPrice = finalPrice.add(servicesPrice);
            reservation.setTotalPrice(finalPrice);
        }

        Reservation saved = reservationRepository.save(reservation);

        // Notificar según preferencias del cliente
        notifyReservationCreated(saved);

        return convertToDTO(saved);
    }

    public void confirmReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", "id", id));

        // Guardar estado actual antes de modificar
        saveReservationState(reservation);

        reservation.setStatus(ReservationStatus.CONFIRMED);
        reservationRepository.save(reservation);
        notifyReservationConfirmed(reservation);
    }

    public void cancelReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", "id", id));

        // Guardar estado actual antes de modificar
        saveReservationState(reservation);

        reservation.setStatus(ReservationStatus.CANCELLED);
        reservationRepository.save(reservation);
        notifyReservationCancelled(reservation);
    }

    /**
     * Actualiza una reserva existente guardando el estado anterior
     */
    public ReservationDTO updateReservation(Long id, ReservationDTO dto) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", "id", id));

        // Guardar estado actual antes de modificar (para undo)
        saveReservationState(reservation);

        // Actualizar campos
        if (dto.getCheckInDate() != null) {
            reservation.setCheckInDate(dto.getCheckInDate());
        }
        if (dto.getCheckOutDate() != null) {
            reservation.setCheckOutDate(dto.getCheckOutDate());
        }
        if (dto.getNumberOfGuests() != null) {
            reservation.setNumberOfGuests(dto.getNumberOfGuests());
        }
        if (dto.getTotalPrice() != null) {
            reservation.setTotalPrice(dto.getTotalPrice());
        }
        if (dto.getStatus() != null) {
            reservation.setStatus(dto.getStatus());
        }

        Reservation updated = reservationRepository.save(reservation);
        log.info("✏️ Reserva #{} actualizada. Historial disponible para undo", id);

        return convertToDTO(updated);
    }

    /**
     * Deshace el último cambio de una reserva (Undo)
     */
    public ReservationDTO undoReservationChange(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", "id", id));

        // Restaurar estado anterior desde el historial
        ReservationMemento memento = reservationHistory.restore(id);

        if (memento == null) {
            throw new IllegalStateException("No hay cambios previos para deshacer en la reserva #" + id);
        }

        // Crear originator y restaurar desde memento
        ReservationOriginator originator = new ReservationOriginator(reservation);
        originator.restoreFromMemento(memento);
        originator.applyToReservation(reservation);

        Reservation restored = reservationRepository.save(reservation);
        log.info("↩️ Deshecho último cambio en reserva #{}. Restaurado estado desde {}",
                id, memento.getSavedAt());

        return convertToDTO(restored);
    }

    /**
     * Obtiene el historial de cambios de una reserva
     */
    public List<ReservationMemento> getReservationHistory(Long id) {
        return reservationHistory.getHistory(id);
    }

    /**
     * Guarda el estado actual de una reserva en el historial
     */
    private void saveReservationState(Reservation reservation) {
        ReservationOriginator originator = new ReservationOriginator(reservation);
        ReservationMemento memento = originator.saveToMemento();
        reservationHistory.save(memento);
    }

    /**
     * Notifica la creación de una reserva según las preferencias del cliente
     */
    private void notifyReservationCreated(Reservation reservation) {
        NotificationPreference preferences = getOrCreatePreferences(reservation.getCustomer().getId());

        if (preferences.getEmailEnabled()) {
            emailObserver.onReservationCreated(reservation);
        }
        if (preferences.getSmsEnabled()) {
            smsObserver.onReservationCreated(reservation);
        }
        if (preferences.getWhatsappEnabled()) {
            whatsappObserver.onReservationCreated(reservation);
        }
    }

    /**
     * Notifica la confirmación de una reserva según las preferencias del cliente
     */
    private void notifyReservationConfirmed(Reservation reservation) {
        NotificationPreference preferences = getOrCreatePreferences(reservation.getCustomer().getId());

        if (preferences.getEmailEnabled()) {
            emailObserver.onReservationConfirmed(reservation);
        }
        if (preferences.getSmsEnabled()) {
            smsObserver.onReservationConfirmed(reservation);
        }
        if (preferences.getWhatsappEnabled()) {
            whatsappObserver.onReservationConfirmed(reservation);
        }
    }

    /**
     * Notifica la cancelación de una reserva según las preferencias del cliente
     */
    private void notifyReservationCancelled(Reservation reservation) {
        NotificationPreference preferences = getOrCreatePreferences(reservation.getCustomer().getId());

        if (preferences.getEmailEnabled()) {
            emailObserver.onReservationCancelled(reservation);
        }
        if (preferences.getSmsEnabled()) {
            smsObserver.onReservationCancelled(reservation);
        }
        if (preferences.getWhatsappEnabled()) {
            whatsappObserver.onReservationCancelled(reservation);
        }
    }

    /**
     * Obtiene o crea preferencias por defecto para un cliente
     */
    private NotificationPreference getOrCreatePreferences(Long customerId) {
        return notificationPreferenceRepository.findByCustomerId(customerId)
                .orElseGet(() -> createDefaultPreferences(customerId));
    }

    /**
     * Crea preferencias por defecto (solo email activado)
     */
    private NotificationPreference createDefaultPreferences(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", customerId));

        NotificationPreference preferences = NotificationPreference.builder()
                .customer(customer)
                .emailEnabled(true)
                .smsEnabled(false)
                .whatsappEnabled(false)
                .build();

        return notificationPreferenceRepository.save(preferences);
    }

    private ReservationDTO convertToDTO(Reservation reservation) {
        long nights = ChronoUnit.DAYS.between(reservation.getCheckInDate(), reservation.getCheckOutDate());

        return ReservationDTO.builder()
                .id(reservation.getId())
                .customerId(reservation.getCustomer().getId())
                .roomId(reservation.getRoom().getId())
                .checkInDate(reservation.getCheckInDate())
                .checkOutDate(reservation.getCheckOutDate())
                .numberOfGuests(reservation.getNumberOfGuests())
                .totalPrice(reservation.getTotalPrice())
                .status(reservation.getStatus())
                .numberOfNights(nights)
                .build();
    }
}
