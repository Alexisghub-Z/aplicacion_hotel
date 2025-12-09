package com.hotel.reservation.service;

import com.hotel.reservation.dto.RoomDTO;
import com.hotel.reservation.exception.ResourceNotFoundException;
import com.hotel.reservation.models.Room;
import com.hotel.reservation.models.RoomType;
import com.hotel.reservation.patterns.creational.singleton.ConfigurationManager;
import com.hotel.reservation.patterns.creational.factory.RoomFactory;
import com.hotel.reservation.patterns.structural.flyweight.RoomTypeFlyweight;
import com.hotel.reservation.repositories.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Servicio de negocio para gestión de habitaciones.
 *
 * Integra:
 * - Factory Pattern: Creación de habitaciones con configuración predefinida
 * - Flyweight Pattern: Compartir información común de tipos de habitación
 * - Singleton Pattern: Acceso a configuración global
 *
 * Responsabilidades:
 * - CRUD de habitaciones
 * - Búsqueda de habitaciones disponibles
 * - Clonación de habitaciones (Prototype)
 * - Conversión entre Entity y DTO
 */
@Service
@RequiredArgsConstructor
@Transactional
public class RoomService {

    private final RoomRepository roomRepository;

    /**
     * Crear una nueva habitación usando Factory Pattern.
     */
    public RoomDTO createRoom(RoomType roomType, String roomNumber, Integer floor) {
        // Usar Factory para crear habitación con configuración predefinida
        Room room = RoomFactory.createRoom(roomType, roomNumber, floor);
        Room savedRoom = roomRepository.save(room);
        return convertToDTO(savedRoom);
    }

    /**
     * Clonar una habitación existente (Prototype Pattern).
     */
    public RoomDTO cloneRoom(Long roomId, String newRoomNumber) {
        Room originalRoom = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room", "id", roomId));

        // Usar método clone() del Prototype Pattern
        Room clonedRoom = originalRoom.clone();
        clonedRoom.setRoomNumber(newRoomNumber);

        Room savedRoom = roomRepository.save(clonedRoom);
        return convertToDTO(savedRoom);
    }

    /**
     * Obtener habitación por ID.
     */
    @Transactional(readOnly = true)
    public RoomDTO getRoomById(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room", "id", id));
        return convertToDTO(room);
    }

    /**
     * Obtener todas las habitaciones.
     */
    @Transactional(readOnly = true)
    public List<RoomDTO> getAllRooms() {
        return roomRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Buscar habitaciones disponibles en un rango de fechas.
     */
    @Transactional(readOnly = true)
    public List<RoomDTO> findAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate) {
        return roomRepository.findAvailableRooms(checkInDate, checkOutDate)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Buscar habitaciones disponibles por tipo.
     */
    @Transactional(readOnly = true)
    public List<RoomDTO> findAvailableRoomsByType(LocalDate checkInDate, LocalDate checkOutDate, RoomType roomType) {
        return roomRepository.findAvailableRooms(checkInDate, checkOutDate)
                .stream()
                .filter(room -> room.getRoomType() == roomType)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Actualizar habitación existente.
     */
    public RoomDTO updateRoom(Long id, RoomDTO roomDTO) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room", "id", id));

        // Actualizar campos
        room.setRoomNumber(roomDTO.getRoomNumber());
        room.setRoomType(roomDTO.getRoomType());
        room.setPrice(roomDTO.getPrice());
        room.setCapacity(roomDTO.getCapacity());
        room.setFloor(roomDTO.getFloor());
        room.setAvailable(roomDTO.getAvailable());
        room.setAmenities(roomDTO.getAmenities());
        room.setImageUrl(roomDTO.getImageUrl());
        room.setDescription(roomDTO.getDescription());

        Room updatedRoom = roomRepository.save(room);
        return convertToDTO(updatedRoom);
    }

    /**
     * Marcar habitación como disponible/no disponible.
     */
    public void toggleRoomAvailability(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room", "id", id));
        room.setAvailable(!room.getAvailable());
        roomRepository.save(room);
    }

    /**
     * Eliminar habitación.
     */
    public void deleteRoom(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room", "id", id));
        roomRepository.delete(room);
    }

    /**
     * Búsqueda avanzada con Interpreter Pattern
     */
    public List<RoomDTO> findLuxuryRoomsForFamilies() {
        List<Room> allRooms = roomRepository.findAll();
        List<Room> results = com.hotel.reservation.patterns.behavioral.interpreter.RoomSearchInterpreter
                .findLuxuryRoomsForFamilies(allRooms);
        return results.stream().map(this::convertToDTO).collect(java.util.stream.Collectors.toList());
    }

    public List<RoomDTO> findByPriceRange(java.math.BigDecimal min, java.math.BigDecimal max) {
        List<Room> allRooms = roomRepository.findAll();
        List<Room> results = com.hotel.reservation.patterns.behavioral.interpreter.RoomSearchInterpreter
                .findByPriceRange(allRooms, min, max);
        return results.stream().map(this::convertToDTO).collect(java.util.stream.Collectors.toList());
    }

    public List<RoomDTO> findByCapacity(int minCapacity) {
        List<Room> allRooms = roomRepository.findAll();
        List<Room> results = com.hotel.reservation.patterns.behavioral.interpreter.RoomSearchInterpreter
                .findByCapacity(allRooms, minCapacity);
        return results.stream().map(this::convertToDTO).collect(java.util.stream.Collectors.toList());
    }

    /**
     * Convertir Room entity a RoomDTO.
     * Utiliza Flyweight Pattern para obtener descripción del tipo de habitación.
     * Utiliza Singleton Pattern para obtener formato de moneda.
     */
    private RoomDTO convertToDTO(Room room) {
        // Usar Flyweight para obtener información compartida del tipo de habitación
        RoomTypeFlyweight flyweight = RoomTypeFlyweight.getFlyweight(room.getRoomType());

        // Usar Singleton para obtener configuración de moneda
        ConfigurationManager config = ConfigurationManager.INSTANCE;
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("es", "MX"));
        String formattedPrice = currencyFormatter.format(room.getPrice());

        return RoomDTO.builder()
                .id(room.getId())
                .roomNumber(room.getRoomNumber())
                .roomType(room.getRoomType())
                .price(room.getPrice())
                .capacity(room.getCapacity())
                .available(room.getAvailable())
                .floor(room.getFloor())
                .amenities(room.getAmenities())
                .imageUrl(room.getImageUrl())
                .description(room.getDescription() != null ? room.getDescription() : flyweight.getStandardDescription())
                .formattedPrice(formattedPrice)
                .build();
    }
}
