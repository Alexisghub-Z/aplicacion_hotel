package com.hotel.reservation.patterns.behavioral.command;

/**
 * Command Pattern - Interfaz base para comandos
 * Permite encapsular operaciones como objetos para deshacer/rehacer
 */
public interface Command {
    void execute();
    void undo();
    String getDescription();
}
