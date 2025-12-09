package com.hotel.reservation.patterns.behavioral.command;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Stack;

/**
 * Invocador de comandos - Gestiona el historial de comandos ejecutados
 * Permite deshacer/rehacer operaciones
 */
@Slf4j
@Component
public class CommandInvoker {

    private final Stack<Command> executedCommands = new Stack<>();
    private final Stack<Command> undoneCommands = new Stack<>();

    public void executeCommand(Command command) {
        command.execute();
        executedCommands.push(command);
        undoneCommands.clear(); // Limpiar comandos deshechos al ejecutar uno nuevo
        log.info("📝 Historial: {} comandos ejecutados", executedCommands.size());
    }

    public void undo() {
        if (!executedCommands.isEmpty()) {
            Command command = executedCommands.pop();
            command.undo();
            undoneCommands.push(command);
            log.info("⬅️ Deshacer: {}", command.getDescription());
        } else {
            log.warn("⚠️ No hay comandos para deshacer");
        }
    }

    public void redo() {
        if (!undoneCommands.isEmpty()) {
            Command command = undoneCommands.pop();
            command.execute();
            executedCommands.push(command);
            log.info("➡️ Rehacer: {}", command.getDescription());
        } else {
            log.warn("⚠️ No hay comandos para rehacer");
        }
    }

    public int getHistorySize() {
        return executedCommands.size();
    }

    public int getUndoneSize() {
        return undoneCommands.size();
    }

    public void clearHistory() {
        executedCommands.clear();
        undoneCommands.clear();
        log.info("🗑️ Historial de comandos limpiado");
    }
}
