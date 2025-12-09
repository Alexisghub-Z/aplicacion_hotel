package com.hotel.reservation.patterns.behavioral.iterator;

import com.hotel.reservation.models.Room;

import java.util.Iterator;

/**
 * Iterator Pattern - Interfaz para iterar sobre colecciones de habitaciones
 */
public interface RoomIterator extends Iterator<Room> {
    void reset();
    Room current();
}
