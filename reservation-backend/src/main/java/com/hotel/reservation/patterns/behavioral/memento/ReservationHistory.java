package com.hotel.reservation.patterns.behavioral.memento;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Caretaker - Gestiona el historial de mementos
 */
@Slf4j
@Component
public class ReservationHistory {

    private final Map<Long, Stack<ReservationMemento>> history = new HashMap<>();

    public void save(ReservationMemento memento) {
        Long reservationId = memento.getReservationId();

        history.computeIfAbsent(reservationId, k -> new Stack<>());
        history.get(reservationId).push(memento);

        log.info("📚 Historial: Guardado estado de reserva #{} - {} estados totales",
            reservationId, history.get(reservationId).size());
    }

    public ReservationMemento restore(Long reservationId) {
        Stack<ReservationMemento> reservationHistory = history.get(reservationId);

        if (reservationHistory == null || reservationHistory.isEmpty()) {
            log.warn("⚠️ No hay historial para reserva #{}", reservationId);
            return null;
        }

        ReservationMemento memento = reservationHistory.pop();
        log.info("📖 Historial: Restaurado estado de reserva #{} desde {}",
            reservationId, memento.getSavedAt());

        return memento;
    }

    public List<ReservationMemento> getHistory(Long reservationId) {
        Stack<ReservationMemento> reservationHistory = history.get(reservationId);

        if (reservationHistory == null) {
            return new ArrayList<>();
        }

        return new ArrayList<>(reservationHistory);
    }

    public int getHistorySize(Long reservationId) {
        Stack<ReservationMemento> reservationHistory = history.get(reservationId);
        return reservationHistory == null ? 0 : reservationHistory.size();
    }

    public void clearHistory(Long reservationId) {
        history.remove(reservationId);
        log.info("🗑️ Historial de reserva #{} eliminado", reservationId);
    }

    public void clearAllHistory() {
        history.clear();
        log.info("🗑️ Todo el historial ha sido eliminado");
    }
}
