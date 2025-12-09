package com.hotel.reservation.patterns.behavioral.iterator;

import com.hotel.reservation.models.Room;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Iterador para habitaciones disponibles
 */
public class AvailableRoomIterator implements RoomIterator {

    private final List<Room> rooms;
    private int currentPosition = 0;

    public AvailableRoomIterator(List<Room> rooms) {
        this.rooms = rooms.stream()
            .filter(Room::isAvailable)
            .toList();
    }

    @Override
    public boolean hasNext() {
        return currentPosition < rooms.size();
    }

    @Override
    public Room next() {
        if (!hasNext()) {
            throw new NoSuchElementException("No hay más habitaciones disponibles");
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
