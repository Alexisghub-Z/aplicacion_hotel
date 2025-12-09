# Documentación de Patrones de Diseño - Sistema de Reservas de Hotel

Este documento describe todos los patrones de diseño implementados en el sistema de reservas del hotel, organizados por categoría y explicando su propósito y función dentro del sistema.

---

## Tabla de Contenidos

1. [Patrones Creacionales](#patrones-creacionales)
2. [Patrones Estructurales](#patrones-estructurales)
3. [Patrones de Comportamiento](#patrones-de-comportamiento)

---

## Patrones Creacionales

Los patrones creacionales se encargan de la creación de objetos de manera controlada y flexible.

### 1. Builder Pattern

**Ubicación:** `patterns/creational/builder/`

**Propósito:** Facilita la construcción paso a paso de reservas complejas con múltiples atributos opcionales.

**Implementación:**
- **ReservationBuilder.java**: Clase principal que permite construir objetos `Reservation` de forma fluida y legible.

**Código importante:**
```java
public ReservationBuilder withCustomer(Customer customer)
public ReservationBuilder withRoom(Room room)
public ReservationBuilder withDates(LocalDate checkIn, LocalDate checkOut)
public ReservationBuilder withGuests(Integer numberOfGuests)
public ReservationBuilder addService(AdditionalService service)
public Reservation build()
```

**Función en el sistema:**
- Construye reservas complejas con validaciones integradas
- Calcula automáticamente el precio base según habitación y noches
- Permite agregar servicios adicionales de forma incremental
- Valida que todos los datos obligatorios estén presentes antes de crear la reserva
- Verifica que las fechas sean lógicas y que la capacidad de la habitación no se exceda

**Ejemplo de uso:**
```java
Reservation reservation = new ReservationBuilder()
    .withCustomer(customer)
    .withRoom(room)
    .withDates(checkIn, checkOut)
    .withGuests(2)
    .addService(breakfastService)
    .build();
```

---

### 2. Factory Pattern

**Ubicación:** `patterns/creational/factory/`

**Propósito:** Encapsula la creación de habitaciones según su tipo, aplicando configuraciones específicas para cada categoría.

**Implementación:**
- **RoomFactory.java**: Fábrica estática que crea habitaciones con configuraciones predefinidas.

**Código importante:**
```java
public static Room createRoom(RoomType roomType, String roomNumber, Integer floor)
private static Room buildSingleRoom(Room.RoomBuilder builder)
private static Room buildDoubleRoom(Room.RoomBuilder builder)
private static Room buildSuiteRoom(Room.RoomBuilder builder)
private static Room buildPresidentialRoom(Room.RoomBuilder builder)
```

**Función en el sistema:**
- Centraliza la lógica de creación de habitaciones
- Garantiza que cada tipo de habitación tenga las configuraciones correctas:
  - **SINGLE**: $800 MXN, capacidad 1, amenidades básicas
  - **DOUBLE**: $1,200 MXN, capacidad 2, incluye mini bar y balcón
  - **SUITE**: $2,500 MXN, capacidad 3, incluye jacuzzi y sala de estar
  - **PRESIDENTIAL**: $5,000 MXN, capacidad 4, amenidades premium y mayordomo
- Facilita agregar nuevos tipos de habitación sin modificar código existente
- Mantiene consistencia en precios y amenidades por tipo

---

### 3. Prototype Pattern

**Ubicación:** `patterns/creational/prototype/`

**Propósito:** Permite clonar reservas existentes para crear nuevas reservas basadas en reservas anteriores.

**Implementación:**
- **ReservationPrototype.java**: Interface o clase que implementa el patrón de clonación.

**Función en el sistema:**
- Facilita la creación de reservas recurrentes (clientes que reservan regularmente)
- Permite duplicar reservas con pequeñas modificaciones (cambiar fechas pero mantener servicios)
- Optimiza la creación de reservas similares

---

### 4. Singleton Pattern

**Ubicación:** `patterns/creational/singleton/`

**Propósito:** Garantiza una única instancia del gestor de configuración del sistema.

**Implementación:**
- **ConfigurationManager.java**: Clase singleton que mantiene la configuración global del sistema.

**Función en el sistema:**
- Mantiene configuraciones globales del hotel (nombre, dirección, políticas)
- Asegura que todas las partes del sistema usen la misma configuración
- Evita múltiples instancias que podrían causar inconsistencias
- Gestiona parámetros como políticas de cancelación, horarios de check-in/out, etc.

---

## Patrones Estructurales

Los patrones estructurales se encargan de cómo se componen las clases y objetos para formar estructuras más grandes.

### 1. Adapter Pattern

**Ubicación:** `patterns/structural/adapter/`

**Propósito:** Proporciona una interfaz común para diferentes pasarelas de pago, permitiendo integrar múltiples proveedores sin cambiar el código cliente.

**Implementación:**
- **PaymentGatewayAdapter.java**: Interface común para todas las pasarelas
- **StripePaymentAdapter.java**: Adaptador para Stripe
- **PayPalPaymentAdapter.java**: Adaptador para PayPal
- **CashPaymentAdapter.java**: Adaptador para pagos en efectivo

**Código importante:**
```java
public interface PaymentGatewayAdapter {
    PaymentResult processPayment(Payment payment);
    PaymentResult refund(Payment payment);
    PaymentResult checkStatus(String transactionId);
}
```

**Función en el sistema:**
- Unifica la interfaz para procesar pagos de diferentes proveedores
- Facilita agregar nuevas pasarelas de pago sin modificar el código existente
- Cada adaptador traduce las llamadas genéricas a las APIs específicas de cada proveedor
- Maneja diferentes formatos de respuesta y los convierte a `PaymentResult` estándar
- Permite cambiar de proveedor de pago sin afectar el resto del sistema

---

### 2. Composite Pattern

**Ubicación:** `patterns/structural/composite/`

**Propósito:** Permite tratar servicios individuales y paquetes de servicios de forma uniforme mediante una estructura de árbol.

**Implementación:**
- **ServiceComponent.java**: Interface común para servicios y paquetes
- **ServiceLeaf.java**: Representa un servicio individual (spa, transporte, etc.)
- **ServicePackage.java**: Representa un paquete que contiene múltiples servicios

**Código importante:**
```java
public interface ServiceComponent {
    String getName();
    String getDescription();
    BigDecimal getPrice();
    void display(int indent);
}
```

**Función en el sistema:**
- Permite crear paquetes de servicios compuestos por otros servicios
- Calcula automáticamente el precio total de un paquete sumando sus componentes
- Facilita la creación de ofertas especiales (paquete romántico, paquete familiar, etc.)
- Los clientes pueden agregar servicios individuales o paquetes completos sin cambiar el código
- Permite anidar paquetes dentro de otros paquetes (paquetes de paquetes)

**Ejemplo:**
```
Paquete Romántico ($1,500)
  ├── Cena especial ($800)
  ├── Spa para 2 ($500)
  └── Decoración de habitación ($200)
```

---

### 3. Decorator Pattern

**Ubicación:** `patterns/structural/decorator/`

**Propósito:** Añade funcionalidades (servicios adicionales) a una reserva de forma dinámica sin modificar la clase base.

**Implementación:**
- **ReservationComponent.java**: Interface común
- **BaseReservation.java**: Reserva básica sin extras
- **ReservationDecorator.java**: Decorador abstracto base
- **BreakfastDecorator.java**: Añade servicio de desayuno
- **SpaDecorator.java**: Añade servicio de spa
- **TransportDecorator.java**: Añade servicio de transporte
- **ExcursionDecorator.java**: Añade servicio de excursiones

**Código importante:**
```java
public abstract class ReservationDecorator implements ReservationComponent {
    protected ReservationComponent wrappedReservation;

    public BigDecimal getPrice() {
        return wrappedReservation.getPrice();
    }
}
```

**Función en el sistema:**
- Permite agregar servicios adicionales a una reserva de forma incremental
- Cada decorador añade su costo al precio total de la reserva
- Los servicios se pueden combinar libremente (desayuno + spa + transporte)
- Mantiene la descripción actualizada incluyendo todos los servicios agregados
- Permite deshacer decoraciones fácilmente (quitar un servicio)

**Ejemplo de uso:**
```java
ReservationComponent reservation = new BaseReservation(room, customer);
reservation = new BreakfastDecorator(reservation);
reservation = new SpaDecorator(reservation);
// Precio total = habitación + desayuno + spa
```

---

### 4. Facade Pattern

**Ubicación:** `patterns/structural/facade/`

**Propósito:** Simplifica el proceso completo de crear una reserva coordinando múltiples subsistemas (validación, disponibilidad, pago, notificaciones).

**Implementación:**
- **ReservationFacade.java**: Fachada que orquesta todo el proceso de reserva

**Código importante:**
```java
public ReservationResult createCompleteReservation(
    String customerEmail,
    Long roomId,
    LocalDate checkInDate,
    LocalDate checkOutDate,
    Integer numberOfGuests,
    List<Long> serviceIds,
    PaymentMethod paymentMethod)
```

**Función en el sistema:**
El facade coordina 10 pasos complejos:

1. **Validar y obtener cliente**: Busca el cliente por email
2. **Validar y obtener habitación**: Verifica que la habitación exista
3. **Verificar disponibilidad**: Comprueba que la habitación esté libre
4. **Verificar fechas**: Confirma que no haya conflictos de reservas
5. **Obtener servicios adicionales**: Carga los servicios solicitados
6. **Construir reserva**: Usa el Builder Pattern para crear la reserva
7. **Guardar reserva**: Persiste en la base de datos
8. **Crear y procesar pago**: Utiliza los Adapters de pago
9. **Marcar habitación como ocupada**: Actualiza el estado si el pago fue exitoso
10. **Enviar notificaciones**: Notifica al cliente (implementado con Observer Pattern)

**Beneficios:**
- Oculta la complejidad de coordinar múltiples repositorios y servicios
- Proporciona una interfaz simple para una operación compleja
- Centraliza la lógica de negocio de las reservas
- Maneja transacciones y rollback en caso de errores
- Reduce el acoplamiento entre el cliente y los subsistemas

---

### 5. Flyweight Pattern

**Ubicación:** `patterns/structural/flyweight/`

**Propósito:** Comparte de manera eficiente datos comunes (amenidades, descripciones) entre múltiples habitaciones del mismo tipo, reduciendo el uso de memoria.

**Implementación:**
- **RoomTypeFlyweight.java**: Pool de flyweights que comparten datos inmutables

**Código importante:**
```java
private static final Map<RoomType, RoomTypeFlyweight> flyweights = new HashMap<>();

public static RoomTypeFlyweight getFlyweight(RoomType type) {
    return flyweights.computeIfAbsent(type, RoomTypeFlyweight::createFlyweight);
}
```

**Función en el sistema:**
- **Estado intrínseco (compartido)**: Amenidades, descripción estándar, URL de imagen
- **Estado extrínseco (único)**: Número de habitación, disponibilidad, precio específico

**Optimización:**
- Si hay 50 habitaciones tipo DOUBLE, en lugar de almacenar 50 veces la lista de amenidades, se almacena solo una vez
- Reduce significativamente el uso de memoria en hoteles con muchas habitaciones
- Mejora el rendimiento al evitar duplicación de datos
- Solo crea un flyweight por tipo de habitación (máximo 4 instancias para SINGLE, DOUBLE, SUITE, PRESIDENTIAL)

**Ejemplo:**
```
Sin Flyweight: 50 habitaciones × 7 amenidades × 50 bytes = 17,500 bytes
Con Flyweight: 1 lista compartida × 7 amenidades × 50 bytes = 350 bytes
```

---

### 6. Proxy Pattern

**Ubicación:** `patterns/structural/proxy/`

**Propósito:** Controla el acceso a imágenes de habitaciones mediante carga diferida (lazy loading), cargando las imágenes solo cuando son realmente necesarias.

**Implementación:**
- **RoomImage.java**: Interface común
- **RealRoomImage.java**: Imagen real que consume recursos
- **ImageProxy.java**: Proxy que controla el acceso

**Código importante:**
```java
public class ImageProxy implements RoomImage {
    private RealRoomImage realImage;
    private boolean isLoaded;

    private void loadImage() {
        if (!isLoaded) {
            realImage = new RealRoomImage(url);
            isLoaded = true;
        }
    }
}
```

**Función en el sistema:**
- **Virtual Proxy**: La imagen NO se carga cuando se crea el objeto proxy
- **Lazy Loading**: Solo se carga cuando se llama a `display()` o `getSize()`
- **Ahorro de memoria**: No carga imágenes que nunca se muestran
- **Mejora rendimiento inicial**: El listado de habitaciones es rápido (solo URLs)
- **Gestión de recursos**: Permite liberar imágenes de memoria con `unload()`
- **Pre-caching opcional**: Método `preload()` para cargar antes de mostrar

**Escenarios de uso:**
1. Listado de habitaciones: Solo muestra URLs, no carga imágenes
2. Detalle de habitación: Al hacer clic, se carga la imagen real
3. Galería: Se pueden precargar imágenes para navegación fluida

---

## Patrones de Comportamiento

Los patrones de comportamiento se encargan de la comunicación entre objetos y la asignación de responsabilidades.

### 1. Chain of Responsibility Pattern

**Ubicación:** `patterns/behavioral/chain/`

**Propósito:** Encadena validaciones de reservas de forma flexible, donde cada handler puede procesar o pasar la validación al siguiente.

**Implementación:**
- **ValidationHandler.java**: Handler base abstracto
- **DateValidationHandler.java**: Valida que las fechas sean lógicas
- **GuestValidationHandler.java**: Valida número de huéspedes vs capacidad
- **AvailabilityValidationHandler.java**: Verifica disponibilidad de la habitación
- **ReservationValidationChain.java**: Construye la cadena completa

**Código importante:**
```java
public abstract class ValidationHandler {
    protected ValidationHandler next;

    public ValidationHandler setNext(ValidationHandler next) {
        this.next = next;
        return next;
    }

    public abstract void validate(ReservationDTO reservation);
}
```

**Función en el sistema:**
- **Cadena de validación**: Fechas → Huéspedes → Disponibilidad
- Cada handler valida un aspecto específico de la reserva
- Si una validación falla, lanza excepción y detiene la cadena
- Si pasa, delega al siguiente handler
- Permite agregar/quitar validaciones sin modificar código existente
- Facilita reordenar la secuencia de validaciones

**Flujo de validación:**
```
Reserva DTO
    ↓
[DateValidationHandler] → ¿Fechas válidas? → No → Exception
    ↓ Sí
[GuestValidationHandler] → ¿Capacidad OK? → No → Exception
    ↓ Sí
[AvailabilityValidationHandler] → ¿Disponible? → No → Exception
    ↓ Sí
Validación exitosa
```

---

### 2. Command Pattern

**Ubicación:** `patterns/behavioral/command/`

**Propósito:** Encapsula operaciones de reserva como objetos, permitiendo deshacer/rehacer operaciones y mantener un historial de comandos.

**Implementación:**
- **Command.java**: Interface para comandos
- **CreateReservationCommand.java**: Comando para crear reserva
- **CancelReservationCommand.java**: Comando para cancelar reserva
- **CommandInvoker.java**: Ejecutor de comandos con historial

**Código importante:**
```java
public interface Command {
    void execute();
    void undo();
    String getDescription();
}
```

**Función en el sistema:**
- **Encapsulación de operaciones**: Cada comando es un objeto independiente
- **Deshacer operaciones**: Método `undo()` revierte la acción
- **Historial de comandos**: El invoker mantiene lista de comandos ejecutados
- **Auditoría**: Registro de todas las operaciones realizadas
- **Macro-comandos**: Permite agrupar múltiples comandos en uno

**Ejemplo de uso:**
```java
Command createCmd = new CreateReservationCommand(reservationData);
invoker.executeCommand(createCmd); // Crea la reserva

// Si hay un error...
invoker.undoLastCommand(); // Deshace la creación
```

**Casos de uso:**
- Crear reserva → Undo = Cancelar la reserva recién creada
- Cancelar reserva → Undo = Restaurar la reserva
- Modificar reserva → Undo = Volver al estado anterior

---

### 3. Interpreter Pattern

**Ubicación:** `patterns/behavioral/interpreter/`

**Propósito:** Permite crear consultas complejas de búsqueda de habitaciones combinando expresiones lógicas (AND, OR, NOT).

**Implementación:**
- **Expression.java**: Interface para expresiones
- **AvailableExpression.java**: Filtra habitaciones disponibles
- **TypeExpression.java**: Filtra por tipo de habitación
- **PriceRangeExpression.java**: Filtra por rango de precio
- **CapacityExpression.java**: Filtra por capacidad mínima
- **AndExpression.java**: Combina expresiones con AND lógico
- **OrExpression.java**: Combina expresiones con OR lógico
- **NotExpression.java**: Niega una expresión
- **RoomSearchInterpreter.java**: Intérprete que ejecuta las búsquedas

**Código importante:**
```java
public interface Expression {
    boolean interpret(Room room);
    String getDescription();
}
```

**Función en el sistema:**
- Construye consultas complejas de forma programática
- Permite búsquedas flexibles sin SQL hardcodeado
- Combina múltiples criterios con lógica booleana

**Ejemplos de búsquedas:**

1. **Habitaciones disponibles tipo SUITE:**
```java
Expression query = new AndExpression(
    new AvailableExpression(),
    new TypeExpression(RoomType.SUITE)
);
```

2. **Suite O Presidential, disponible, con capacidad ≥ 4:**
```java
Expression luxuryTypes = new OrExpression(
    new TypeExpression(RoomType.SUITE),
    new TypeExpression(RoomType.PRESIDENTIAL)
);

Expression query = new AndExpression(
    new AndExpression(new AvailableExpression(), luxuryTypes),
    new CapacityExpression(4)
);
```

3. **Habitaciones económicas ($800-$1500) disponibles:**
```java
Expression query = new AndExpression(
    new AvailableExpression(),
    new PriceRangeExpression(new BigDecimal("800"), new BigDecimal("1500"))
);
```

---

### 4. Iterator Pattern

**Ubicación:** `patterns/behavioral/iterator/`

**Propósito:** Proporciona diferentes formas de iterar sobre colecciones de habitaciones sin exponer la estructura interna.

**Implementación:**
- **RoomIterator.java**: Interface para iteradores
- **RoomCollection.java**: Interface para colecciones iterables
- **HotelRoomCollection.java**: Colección concreta de habitaciones
- **AvailableRoomIterator.java**: Itera solo sobre habitaciones disponibles
- **RoomTypeIterator.java**: Itera sobre habitaciones de un tipo específico
- **FloorRoomIterator.java**: Itera sobre habitaciones de un piso específico

**Código importante:**
```java
public interface RoomIterator extends Iterator<Room> {
    void reset();
    Room current();
}
```

**Función en el sistema:**
- Permite recorrer habitaciones con diferentes criterios de filtrado
- No expone la estructura interna de la colección (puede ser List, Set, Map, etc.)
- Facilita cambiar la implementación de la colección sin afectar el código cliente
- Permite múltiples iteraciones simultáneas con diferentes filtros

**Casos de uso:**

1. **Iterar solo habitaciones disponibles:**
```java
RoomIterator iterator = new AvailableRoomIterator(allRooms);
while (iterator.hasNext()) {
    Room room = iterator.next();
    // Procesar solo disponibles
}
```

2. **Iterar habitaciones del piso 3:**
```java
RoomIterator iterator = new FloorRoomIterator(allRooms, 3);
```

3. **Iterar solo suites:**
```java
RoomIterator iterator = new RoomTypeIterator(allRooms, RoomType.SUITE);
```

---

### 5. Mediator Pattern

**Ubicación:** `patterns/behavioral/mediator/`

**Propósito:** Coordina la comunicación entre componentes del sistema de reservas (habitaciones, clientes, pagos, notificaciones) reduciendo el acoplamiento.

**Implementación:**
- **BookingMediator.java**: Interface del mediador
- **HotelBookingMediator.java**: Implementación concreta que coordina todos los componentes

**Código importante:**
```java
public interface BookingMediator {
    void notifyRoomBooked(Room room, Customer customer);
    void notifyRoomReleased(Room room);
    void notifyPaymentProcessed(Long reservationId, String paymentMethod);
    ReservationDTO processBooking(ReservationDTO reservationDTO);
}
```

**Función en el sistema:**
- **Centraliza la comunicación**: Los componentes no se comunican directamente entre sí
- **Reduce acoplamiento**: Cada componente solo conoce al mediador, no a otros componentes
- **Coordina flujos complejos**: Maneja la secuencia completa de una reserva

**Flujo coordinado por el mediador:**
```
Cliente hace reserva
    ↓
Mediator verifica disponibilidad
    ↓
Mediator marca habitación como ocupada
    ↓
Mediator procesa el pago
    ↓
Mediator actualiza inventario
    ↓
Mediator envía notificaciones
    ↓
Reserva completada
```

**Beneficios:**
- Los componentes no necesitan conocerse entre sí
- Fácil agregar nuevos participantes sin modificar los existentes
- Centraliza la lógica de coordinación
- Facilita pruebas unitarias al aislar componentes

---

### 6. Memento Pattern

**Ubicación:** `patterns/behavioral/memento/`

**Propósito:** Permite guardar y restaurar estados anteriores de una reserva sin violar el encapsulamiento.

**Implementación:**
- **ReservationMemento.java**: Memento que almacena el estado
- **ReservationOriginator.java**: Crea y restaura mementos
- **ReservationHistory.java**: Caretaker que gestiona el historial

**Código importante:**
```java
@AllArgsConstructor
public class ReservationMemento {
    private final Long reservationId;
    private final ReservationStatus status;
    private final LocalDate checkInDate;
    private final LocalDate checkOutDate;
    private final Integer numberOfGuests;
    private final BigDecimal totalPrice;
    private final LocalDateTime savedAt;
}
```

**Función en el sistema:**
- **Snapshots de estado**: Guarda el estado completo de una reserva en un momento dado
- **Restauración**: Permite volver a un estado anterior
- **Historial**: Mantiene múltiples puntos de restauración
- **Auditoría**: Registro de cambios en las reservas

**Casos de uso:**

1. **Modificar reserva con rollback:**
```java
// Guardar estado antes de modificar
ReservationMemento snapshot = reservation.createMemento();

try {
    reservation.modifyDates(newCheckIn, newCheckOut);
    reservation.modifyGuests(3);
} catch (Exception e) {
    // Si falla, restaurar estado anterior
    reservation.restoreFromMemento(snapshot);
}
```

2. **Historial de cambios:**
```java
// Ver todas las versiones anteriores de la reserva
List<ReservationMemento> history = reservationHistory.getHistory(reservationId);
history.forEach(memento ->
    System.out.println(memento.getStateDescription())
);
```

**Estados guardados:**
- ID de reserva
- Estado (PENDING, CONFIRMED, CANCELLED, COMPLETED)
- Fechas de check-in/out
- Número de huéspedes
- Precio total
- Timestamp del guardado

---

### 7. Null Object Pattern

**Ubicación:** `patterns/behavioral/nullobject/`

**Propósito:** Evita verificaciones null proporcionando un objeto "nulo" que implementa el comportamiento por defecto de un cliente.

**Implementación:**
- **Customer.java**: Interface común
- **RealCustomer.java**: Cliente real con datos
- **NullCustomer.java**: Cliente nulo con valores por defecto
- **CustomerFactory.java**: Factory que retorna el tipo apropiado

**Código importante:**
```java
public interface Customer {
    String getFullName();
    String getEmail();
    String getPhoneNumber();
    boolean isNullCustomer();
    int getDiscountPercentage();
}
```

**Función en el sistema:**
- **Elimina verificaciones null**: No más `if (customer != null)`
- **Comportamiento por defecto**: El NullCustomer retorna valores seguros
- **Simplifica código**: Permite tratar todos los clientes uniformemente

**Comparación:**

**Sin Null Object:**
```java
String name = (customer != null) ? customer.getFullName() : "Guest";
String email = (customer != null) ? customer.getEmail() : "no-email";
int discount = (customer != null) ? customer.getDiscountPercentage() : 0;
```

**Con Null Object:**
```java
Customer customer = CustomerFactory.getCustomer(email); // Retorna NullCustomer si no existe
String name = customer.getFullName(); // "Guest"
String email = customer.getEmail(); // "no-email@hotel.com"
int discount = customer.getDiscountPercentage(); // 0
```

**Implementación de NullCustomer:**
- `getFullName()` → "Guest"
- `getEmail()` → "no-email@hotel.com"
- `getPhoneNumber()` → "000-000-0000"
- `isNullCustomer()` → true
- `getDiscountPercentage()` → 0

**Casos de uso:**
- Reservas sin cliente registrado (walk-ins)
- Búsquedas de clientes que no existen
- Operaciones que requieren un cliente pero es opcional

---

### 8. Observer Pattern

**Ubicación:** `patterns/behavioral/observer/`

**Propósito:** Implementa un sistema de notificaciones donde múltiples observadores son notificados automáticamente cuando cambia el estado de una reserva.

**Implementación:**
- **ReservationObserver.java**: Interface para observadores
- **EmailNotificationObserver.java**: Envía notificaciones por email
- **SmsNotificationObserver.java**: Envía notificaciones por SMS
- **WhatsAppNotificationObserver.java**: Envía notificaciones por WhatsApp

**Código importante:**
```java
public interface ReservationObserver {
    void onReservationCreated(Reservation reservation);
    void onReservationConfirmed(Reservation reservation);
    void onReservationCancelled(Reservation reservation);
}
```

**Función en el sistema:**
- **Desacoplamiento**: La reserva no necesita conocer los detalles de notificación
- **Múltiples canales**: Email, SMS, WhatsApp se notifican automáticamente
- **Extensibilidad**: Fácil agregar nuevos canales (Push notifications, Telegram, etc.)
- **Suscripción dinámica**: Los observadores se pueden agregar/quitar en runtime

**Flujo de notificación:**
```
Reserva cambia estado (ej: PENDING → CONFIRMED)
    ↓
Notifica a todos los observadores registrados
    ↓
├─→ EmailNotificationObserver → Envía email de confirmación
├─→ SmsNotificationObserver → Envía SMS con código de reserva
└─→ WhatsAppNotificationObserver → Envía mensaje de WhatsApp
```

**Eventos observados:**
1. **onReservationCreated**: Cuando se crea una nueva reserva
   - Email: Detalles completos de la reserva
   - SMS: Código de confirmación
   - WhatsApp: Resumen con enlace

2. **onReservationConfirmed**: Cuando se confirma el pago
   - Email: Confirmación con voucher PDF
   - SMS: "Reserva confirmada #123"
   - WhatsApp: Instrucciones de check-in

3. **onReservationCancelled**: Cuando se cancela
   - Email: Confirmación de cancelación y reembolso
   - SMS: "Reserva cancelada"
   - WhatsApp: Detalles del reembolso

**Integración con el sistema:**
```java
// Configurar observadores
reservationService.addObserver(new EmailNotificationObserver(emailService));
reservationService.addObserver(new SmsNotificationObserver(smsService));
reservationService.addObserver(new WhatsAppNotificationObserver(whatsappService));

// Cuando cambia el estado, automáticamente se notifica a todos
reservation.setStatus(ReservationStatus.CONFIRMED);
// → Se envían email, SMS y WhatsApp automáticamente
```

---

### 9. State Pattern

**Ubicación:** `patterns/behavioral/state/`

**Propósito:** Permite que una reserva cambie su comportamiento según su estado actual (PENDING, CONFIRMED, CANCELLED, COMPLETED).

**Implementación:**
- **ReservationState.java**: Interface para estados
- **PendingState.java**: Estado pendiente de confirmación
- **ConfirmedState.java**: Estado confirmado
- Otros estados: Cancelled, Completed

**Código importante:**
```java
public interface ReservationState {
    void confirm(Reservation reservation);
    void cancel(Reservation reservation);
    void complete(Reservation reservation);
    String getStateName();
}
```

**Función en el sistema:**
- **Comportamiento por estado**: Cada estado define qué acciones son permitidas
- **Transiciones controladas**: Evita transiciones inválidas
- **Lógica encapsulada**: Cada estado maneja su propia lógica

**Máquina de estados:**
```
        [PENDING]
           ├──confirm()──→ [CONFIRMED] ──complete()──→ [COMPLETED]
           │                    │
           └──cancel()──────────┴────────→ [CANCELLED]
```

**Comportamiento por estado:**

1. **PENDING State:**
   - `confirm()` ✓ → Cambia a CONFIRMED
   - `cancel()` ✓ → Cambia a CANCELLED
   - `complete()` ✗ → Lanza excepción

2. **CONFIRMED State:**
   - `confirm()` ✗ → Ya está confirmado
   - `cancel()` ✓ → Cambia a CANCELLED (puede haber penalización)
   - `complete()` ✓ → Cambia a COMPLETED

3. **CANCELLED State:**
   - `confirm()` ✗ → No se puede reactivar
   - `cancel()` ✗ → Ya está cancelado
   - `complete()` ✗ → No aplicable

4. **COMPLETED State:**
   - `confirm()` ✗ → Ya terminó
   - `cancel()` ✗ → No se puede cancelar lo completado
   - `complete()` ✗ → Ya está completado

**Ejemplo de uso:**
```java
Reservation reservation = new Reservation();
reservation.setState(new PendingState());

// Cliente paga
reservation.confirm(); // PendingState → ConfirmedState

// Cliente hace check-out
reservation.complete(); // ConfirmedState → CompletedState
```

**Validaciones automáticas:**
- Intento de cancelar una reserva completada → Exception
- Intento de confirmar una reserva ya confirmada → Exception
- Intento de completar una reserva pendiente → Exception

---

### 10. Strategy Pattern

**Ubicación:** `patterns/behavioral/strategy/`

**Propósito:** Define una familia de algoritmos de cálculo de precios, encapsula cada uno y los hace intercambiables en runtime.

**Implementación:**
- **PricingStrategy.java**: Interface para estrategias
- **RegularPricingStrategy.java**: Precio base sin modificaciones
- **SeasonalPricingStrategy.java**: +30% en temporada alta
- **WeekendPricingStrategy.java**: +20% en fines de semana
- **LoyaltyPricingStrategy.java**: Descuentos por nivel de lealtad
- **PricingContext.java**: Contexto que usa la estrategia

**Código importante:**
```java
public interface PricingStrategy {
    BigDecimal calculatePrice(BigDecimal basePrice,
                             LocalDate checkInDate,
                             LocalDate checkOutDate);
    String getDescription();
}
```

**Función en el sistema:**
- **Elimina condicionales**: No más `if (esTemporadaAlta)` o `switch (tipoPrecio)`
- **Cambio dinámico**: La estrategia se puede cambiar en runtime
- **Fácil extensión**: Agregar nuevas estrategias sin modificar código existente
- **Combinación**: Se pueden combinar estrategias

**Estrategias implementadas:**

1. **RegularPricingStrategy:**
   - Precio base sin modificaciones
   - `calculatePrice(1000, ...)` → $1,000

2. **SeasonalPricingStrategy:**
   - +30% en temporada alta (diciembre, semana santa, julio-agosto)
   - `calculatePrice(1000, ...)` → $1,300 (en temporada alta)

3. **WeekendPricingStrategy:**
   - +20% en fines de semana (viernes-domingo)
   - `calculatePrice(1000, ...)` → $1,200 (si incluye fin de semana)

4. **LoyaltyPricingStrategy:**
   - Descuentos por lealtad:
     - Bronze: 5% descuento
     - Silver: 10% descuento
     - Gold: 15% descuento
     - Platinum: 20% descuento
   - `calculatePrice(1000, ...)` → $800 (cliente Platinum)

**Ejemplo de uso:**
```java
PricingContext context = new PricingContext();

// Cliente normal en temporada baja
context.setStrategy(new RegularPricingStrategy());
BigDecimal price1 = context.calculatePrice(basePrice, checkIn, checkOut);

// Cliente normal en temporada alta
context.setStrategy(new SeasonalPricingStrategy());
BigDecimal price2 = context.calculatePrice(basePrice, checkIn, checkOut);

// Cliente VIP
context.setStrategy(new LoyaltyPricingStrategy(LoyaltyLevel.PLATINUM));
BigDecimal price3 = context.calculatePrice(basePrice, checkIn, checkOut);
```

**Combinación de estrategias:**
```java
// Precio base
BigDecimal price = basePrice;

// Aplicar temporada
price = new SeasonalPricingStrategy().calculatePrice(price, ...);

// Aplicar descuento de lealtad
price = new LoyaltyPricingStrategy(level).calculatePrice(price, ...);
```

---

### 11. Template Method Pattern

**Ubicación:** `patterns/behavioral/template/`

**Propósito:** Define el esqueleto del algoritmo de generación de reportes en la clase base, permitiendo que las subclases implementen pasos específicos.

**Implementación:**
- **ReportTemplate.java**: Plantilla base abstracta
- **ReservationReport.java**: Reporte de reservas
- **OccupancyReport.java**: Reporte de ocupación
- **RevenueReport.java**: Reporte de ingresos

**Código importante:**
```java
public abstract class ReportTemplate {
    // Template Method - Define el flujo (FINAL, no se puede sobrescribir)
    public final String generateReport() {
        StringBuilder report = new StringBuilder();
        report.append(generateHeader());
        report.append(generateBody());
        report.append(generateFooter());
        return report.toString();
    }

    // Métodos abstractos - Subclases DEBEN implementar
    protected abstract String getReportName();
    protected abstract String generateBody();

    // Métodos con implementación por defecto - Subclases PUEDEN sobrescribir
    protected String generateHeader() { ... }
    protected String generateFooter() { ... }
}
```

**Función en el sistema:**
- **Estructura común**: Todos los reportes tienen Header → Body → Footer
- **Variación en el contenido**: Cada reporte implementa su propio `generateBody()`
- **Reutilización**: Header y Footer son iguales para todos
- **Extensibilidad**: Fácil agregar nuevos tipos de reportes

**Flujo del Template Method:**
```
generateReport() [FINAL - no se puede modificar]
    ↓
1. generateHeader() [implementación por defecto, puede sobrescribirse]
    ↓
2. generateBody() [abstracto - DEBE implementarse]
    ↓
3. generateFooter() [implementación por defecto, puede sobrescribirse]
```

**Implementaciones concretas:**

1. **ReservationReport:**
```java
protected String generateBody() {
    // Lista todas las reservas con:
    // - ID, cliente, habitación, fechas, estado
    return reservationList;
}
```

2. **OccupancyReport:**
```java
protected String generateBody() {
    // Estadísticas de ocupación:
    // - % de ocupación por tipo de habitación
    // - Habitaciones disponibles vs ocupadas
    return occupancyStats;
}
```

3. **RevenueReport:**
```java
protected String generateBody() {
    // Análisis financiero:
    // - Ingresos totales
    // - Ingresos por tipo de habitación
    // - Servicios adicionales vendidos
    return revenueAnalysis;
}
```

**Ejemplo de salida:**
```
=====================================
REPORTE DE RESERVAS
=====================================
Fecha de generación: 08/12/2025 14:30:00

[Contenido específico del reporte]

=====================================
Fin del reporte
=====================================
```

**Ventajas:**
- Evita duplicación de código (header/footer)
- Garantiza estructura consistente en todos los reportes
- Facilita agregar nuevos reportes (solo implementar `generateBody()`)
- Permite personalizar partes específicas sin romper la estructura

---

### 12. Visitor Pattern

**Ubicación:** `patterns/behavioral/visitor/`

**Propósito:** Permite agregar nuevas operaciones sobre entidades (Room, Customer, Reservation, Payment) sin modificar las clases de las entidades.

**Implementación:**
- **EntityVisitor.java**: Interface para visitantes
- **ExportVisitor.java**: Exporta entidades a diferentes formatos (JSON, XML, CSV)
- **StatisticsVisitor.java**: Calcula estadísticas sobre entidades
- **ValidationVisitor.java**: Valida integridad de entidades

**Código importante:**
```java
public interface EntityVisitor {
    void visit(Room room);
    void visit(Customer customer);
    void visit(Reservation reservation);
    void visit(Payment payment);
}
```

**Función en el sistema:**
- **Separación de responsabilidades**: La lógica de operaciones está en visitors, no en entidades
- **Open/Closed Principle**: Las entidades están cerradas a modificación, abiertas a extensión
- **Nuevas operaciones sin modificar entidades**: Agregar nuevo visitor sin tocar Room, Customer, etc.

**Double Dispatch:**
```java
// 1. Cliente llama al método accept de la entidad
room.accept(exportVisitor);

// 2. La entidad llama de vuelta al visitor con su tipo específico
public void accept(EntityVisitor visitor) {
    visitor.visit(this); // this es Room
}

// 3. El visitor ejecuta la operación específica para Room
visitor.visit(Room room) {
    // Exportar room a JSON
}
```

**Visitantes implementados:**

1. **ExportVisitor:**
   - Exporta entidades a diferentes formatos
   - `visit(Room)` → JSON con todos los datos de la habitación
   - `visit(Customer)` → JSON con datos del cliente
   - `visit(Reservation)` → JSON con reserva completa
   - `visit(Payment)` → JSON con información de pago

2. **StatisticsVisitor:**
   - Recopila estadísticas
   - `visit(Room)` → Cuenta habitaciones por tipo, calcula precio promedio
   - `visit(Customer)` → Cuenta clientes por nivel de lealtad
   - `visit(Reservation)` → Calcula tasa de ocupación, duración promedio
   - `visit(Payment)` → Suma ingresos, cuenta métodos de pago

3. **ValidationVisitor:**
   - Valida integridad de datos
   - `visit(Room)` → Verifica precio > 0, capacidad > 0, amenidades no vacías
   - `visit(Customer)` → Verifica email válido, teléfono con formato correcto
   - `visit(Reservation)` → Verifica fechas lógicas, precio calculado correcto
   - `visit(Payment)` → Verifica monto coincide con reserva, estado consistente

**Ejemplo de uso:**
```java
// Exportar todas las habitaciones a JSON
ExportVisitor exporter = new ExportVisitor("JSON");
List<Room> rooms = roomRepository.findAll();
rooms.forEach(room -> room.accept(exporter));
String json = exporter.getResult();

// Calcular estadísticas de reservas
StatisticsVisitor stats = new StatisticsVisitor();
List<Reservation> reservations = reservationRepository.findAll();
reservations.forEach(reservation -> reservation.accept(stats));
System.out.println(stats.getReport());

// Validar integridad de pagos
ValidationVisitor validator = new ValidationVisitor();
List<Payment> payments = paymentRepository.findAll();
payments.forEach(payment -> payment.accept(validator));
if (validator.hasErrors()) {
    System.out.println(validator.getErrors());
}
```

**Ventajas:**
- Agregar nueva operación = Crear nuevo visitor (sin tocar entidades)
- Operaciones complejas centralizadas en un solo lugar
- Facilita mantenimiento al separar operaciones de las entidades
- Permite acumular estado durante la visita (ej: estadísticas)

**Cuándo usar:**
- Necesitas realizar múltiples operaciones diferentes sobre una jerarquía de objetos
- Las operaciones no son parte de la responsabilidad principal de las entidades
- Las operaciones cambian frecuentemente, pero la estructura de entidades es estable

---

## Resumen de Patrones por Función

### Creación de Objetos
- **Builder**: Construcción compleja de reservas
- **Factory**: Creación estandarizada de habitaciones
- **Prototype**: Clonación de reservas
- **Singleton**: Configuración global única

### Estructura y Composición
- **Adapter**: Interfaz unificada para pasarelas de pago
- **Composite**: Servicios individuales y paquetes
- **Decorator**: Agregar servicios a reservas dinámicamente
- **Facade**: Simplificar proceso completo de reserva
- **Flyweight**: Compartir datos de tipos de habitación
- **Proxy**: Carga diferida de imágenes

### Comportamiento y Coordinación
- **Chain of Responsibility**: Cadena de validaciones
- **Command**: Deshacer/rehacer operaciones
- **Interpreter**: Búsquedas complejas con expresiones
- **Iterator**: Diferentes formas de recorrer habitaciones
- **Mediator**: Coordinar componentes del sistema
- **Memento**: Guardar/restaurar estados de reserva
- **Null Object**: Evitar verificaciones null
- **Observer**: Sistema de notificaciones multi-canal
- **State**: Comportamiento según estado de reserva
- **Strategy**: Algoritmos intercambiables de pricing
- **Template Method**: Estructura común para reportes
- **Visitor**: Operaciones sobre entidades sin modificarlas

---

## Integración de Patrones en el Flujo Principal

### Flujo Completo de Creación de Reserva

```
1. Cliente selecciona habitación
   → Factory Pattern: Crear habitación con configuración por tipo
   → Flyweight Pattern: Usar datos compartidos del tipo

2. Cliente agrega servicios adicionales
   → Composite Pattern: Servicios individuales o paquetes
   → Decorator Pattern: Decorar reserva con servicios

3. Sistema valida la reserva
   → Chain of Responsibility: Validaciones en cadena
   → Strategy Pattern: Calcular precio según estrategia

4. Sistema crea la reserva
   → Builder Pattern: Construir reserva paso a paso
   → Memento Pattern: Guardar snapshot del estado

5. Cliente procesa el pago
   → Adapter Pattern: Integrar pasarela de pago
   → Facade Pattern: Coordinar todo el proceso

6. Sistema confirma la reserva
   → State Pattern: Cambiar estado PENDING → CONFIRMED
   → Observer Pattern: Notificar por email, SMS, WhatsApp
   → Mediator Pattern: Coordinar actualización de componentes

7. Sistema genera confirmación
   → Template Method: Generar reporte de confirmación
   → Visitor Pattern: Exportar a PDF

8. Cliente puede cancelar
   → Command Pattern: Ejecutar comando de cancelación con undo
   → State Pattern: Cambiar estado a CANCELLED
   → Memento Pattern: Guardar historial de cambios
```

---

## Conclusión

Este sistema implementa **19 patrones de diseño** diferentes, demostrando una arquitectura robusta y extensible. Cada patrón tiene un propósito específico y se integra con otros patrones para proporcionar una solución completa al problema del sistema de reservas hoteleras.

La combinación de estos patrones permite:
- **Flexibilidad**: Fácil agregar nuevas funcionalidades
- **Mantenibilidad**: Código organizado y fácil de entender
- **Escalabilidad**: Arquitectura que crece con las necesidades
- **Reutilización**: Componentes reutilizables en diferentes contextos
- **Testabilidad**: Componentes desacoplados fáciles de probar
