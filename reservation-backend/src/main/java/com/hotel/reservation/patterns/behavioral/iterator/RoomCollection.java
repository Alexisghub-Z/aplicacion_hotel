package com.hotel.reservation.patterns.behavioral.iterator;

import com.hotel.reservation.models.Room;

import java.util.List;

/**
 * Interfaz de colección iterable de habitaciones
 */
public interface RoomCollection {
    RoomIterator createAvailableIterator();
    RoomIterator createByTypeIterator(String roomType);
    RoomIterator createByFloorIterator(int floor);
    void addRoom(Room room);
    List<Room> getRooms();
}
