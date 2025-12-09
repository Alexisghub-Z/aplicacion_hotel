package com.hotel.reservation.patterns.behavioral.iterator;

import com.hotel.reservation.models.Room;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Colección concreta de habitaciones con soporte para iteradores
 */
@Getter
public class HotelRoomCollection implements RoomCollection {

    private final List<Room> rooms = new ArrayList<>();

    @Override
    public RoomIterator createAvailableIterator() {
        return new AvailableRoomIterator(rooms);
    }

    @Override
    public RoomIterator createByTypeIterator(String roomType) {
        return new RoomTypeIterator(rooms, roomType);
    }

    @Override
    public RoomIterator createByFloorIterator(int floor) {
        return new FloorRoomIterator(rooms, floor);
    }

    @Override
    public void addRoom(Room room) {
        rooms.add(room);
    }
}
