package com.hotel.reservation.patterns.behavioral.interpreter;

import com.hotel.reservation.models.Room;
import com.hotel.reservation.models.RoomType;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Interpreter Pattern - Intérprete de búsqueda de habitaciones
 * Permite crear consultas complejas combinando expresiones
 */
@Slf4j
public class RoomSearchInterpreter {

    /**
     * Buscar habitaciones disponibles de un tipo específico
     */
    public static List<Room> findAvailableOfType(List<Room> rooms, RoomType type) {
        Expression query = new AndExpression(
                new AvailableExpression(),
                new TypeExpression(type)
        );

        log.info("🔍 Búsqueda: {}", query.getDescription());
        return filterRooms(rooms, query);
    }

    /**
     * Buscar habitaciones en rango de precio y disponibles
     */
    public static List<Room> findByPriceRange(List<Room> rooms, BigDecimal min, BigDecimal max) {
        Expression query = new AndExpression(
                new AvailableExpression(),
                new PriceRangeExpression(min, max)
        );

        log.info("🔍 Búsqueda: {}", query.getDescription());
        return filterRooms(rooms, query);
    }

    /**
     * Buscar habitaciones disponibles con capacidad mínima
     */
    public static List<Room> findByCapacity(List<Room> rooms, int minCapacity) {
        Expression query = new AndExpression(
                new AvailableExpression(),
                new CapacityExpression(minCapacity)
        );

        log.info("🔍 Búsqueda: {}", query.getDescription());
        return filterRooms(rooms, query);
    }

    /**
     * Búsqueda compleja: Suite O Presidential, disponible, y con capacidad >= 4
     */
    public static List<Room> findLuxuryRoomsForFamilies(List<Room> rooms) {
        Expression luxuryTypes = new OrExpression(
                new TypeExpression(RoomType.SUITE),
                new TypeExpression(RoomType.PRESIDENTIAL)
        );

        Expression query = new AndExpression(
                new AndExpression(new AvailableExpression(), luxuryTypes),
                new CapacityExpression(4)
        );

        log.info("🔍 Búsqueda compleja: {}", query.getDescription());
        return filterRooms(rooms, query);
    }

    /**
     * Búsqueda personalizada con expresión custom
     */
    public static List<Room> search(List<Room> rooms, Expression query) {
        log.info("🔍 Búsqueda personalizada: {}", query.getDescription());
        return filterRooms(rooms, query);
    }

    private static List<Room> filterRooms(List<Room> rooms, Expression query) {
        List<Room> results = rooms.stream()
                .filter(query::interpret)
                .collect(Collectors.toList());

        log.info("✅ Encontradas {} habitaciones que coinciden", results.size());
        return results;
    }
}
