package com.hotel.reservation.patterns.behavioral.iterator;

import com.hotel.reservation.models.Room;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Iterador para habitaciones de un tipo específico
 */
public class RoomTypeIterator implements RoomIterator {

    private final List<Room> rooms;
    private int currentPosition = 0;

    public RoomTypeIterator(List<Room> rooms, String roomType) {
        this.rooms = rooms.stream()
            .filter(room -> room.getRoomType().name().equals(roomType))
            .toList();
    }

    @Override
    public boolean hasNext() {
        return currentPosition < rooms.size();
    }

    @Override
    public Room next() {
        if (!hasNext()) {
            throw new NoSuchElementException("No hay más habitaciones de este tipo");
        }
        return rooms.get(currentPosition++);
    }

    @Override
    public void reset() {
        currentPosition = 0;
    }

    @Override
    public Room current() {
        if (currentPosition > 0 && currentPosition <= rooms.size()) {
            return rooms.get(currentPosition - 1);
        }
        throw new NoSuchElementException("No hay habitación actual");
    }
}
