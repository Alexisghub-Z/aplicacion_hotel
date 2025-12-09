# Guía de Pruebas - API Hotel Oaxaca Dreams

## 🚀 Estado del Sistema

✅ Backend corriendo en: **http://localhost:8080**
✅ Frontend corriendo en: **http://localhost:5173**
✅ Consola H2: **http://localhost:8080/h2-console**

---

## 📋 Comandos de Prueba

### 1. Verificar que el servidor está corriendo

```bash
curl http://localhost:8080/api/test
```

**Respuesta esperada**:
```json
{
  "message": "Hotel Oaxaca Dreams API está funcionando correctamente",
  "status": "OK",
  "version": "Fase 2 - Completada"
}
```

---

## 🏨 Datos Cargados en la Base de Datos

### Clientes (5)
1. Juan García López - REGULAR
2. María Hernández Ruiz - SILVER
3. Carlos Martínez Sánchez - GOLD
4. Ana López Flores - PLATINUM
5. Pedro Ramírez Cruz - REGULAR

### Habitaciones (12)
- **Sencillas (2)**: 101, 102 - $800 MXN
- **Dobles (4)**: 103, 104, 201, 202 - $1,200 MXN
- **Suites (4)**: 203, 204, 301, 302 - $2,500 MXN
- **Presidenciales (2)**: 303, 304 - $5,000 MXN

### Servicios Adicionales (8)
- Desayuno Continental - $200
- Desayuno Oaxaqueño - $280
- Masaje Relajante - $800
- Tratamiento Facial - $650
- Transporte Aeropuerto - $500
- Tour Monte Albán - $1,200
- Tour Hierve el Agua - $1,500
- Servicio de Habitación Premium - $150

---

## 🎯 Pruebas de Patrones Implementados

### 1. **Singleton Pattern** - ConfigurationManager

```bash
# El ConfigurationManager proporciona configuración global
# Se usa internamente en los servicios
```

**Configuración**:
- Moneda: MXN
- Tasa de impuesto: 16%
- Nombre del hotel: Hotel Oaxaca Dreams

---

### 2. **Factory Pattern** - RoomFactory

Puedes usar RoomService para crear habitaciones con configuración predefinida:

```java
// Ejemplo de uso (en código Java)
RoomDTO room = roomService.createRoom(RoomType.SUITE, "405", 4);
```

---

### 3. **Prototype Pattern** - Clonar Habitaciones

```java
// Clonar habitación existente
RoomDTO clonedRoom = roomService.cloneRoom(1L, "105");
```

---

### 4. **Adapter Pattern** - Payment Gateways

El PaymentService usa adapters para procesar pagos con diferentes métodos:

```java
// Procesar pago con tarjeta (usa StripePaymentAdapter)
PaymentDTO payment = paymentService.processPayment(1L, PaymentMethod.CREDIT_CARD);

// Procesar pago con PayPal (usa PayPalPaymentAdapter)
PaymentDTO payment = paymentService.processPayment(2L, PaymentMethod.PAYPAL);

// Procesar pago en efectivo (usa CashPaymentAdapter)
PaymentDTO payment = paymentService.processPayment(3L, PaymentMethod.CASH);
```

---

### 5. **Flyweight Pattern** - RoomTypeFlyweight

Compartir amenidades entre habitaciones del mismo tipo:

```java
RoomTypeFlyweight flyweight = RoomTypeFlyweight.getFlyweight(RoomType.SUITE);
List<String> amenities = flyweight.getStandardAmenities();
String description = flyweight.getStandardDescription();
```

---

### 6. **Proxy Pattern** - ImageProxy

Carga diferida de imágenes de habitaciones:

```java
RoomImage imageProxy = new ImageProxy("/images/rooms/suite-203.jpg");
imageProxy.display(); // La imagen se carga solo cuando se necesita
```

---

### 7. **Decorator Pattern** - Servicios Adicionales

Añadir servicios a una reserva:

```java
ReservationComponent reservation = new BaseReservation(room, 3); // 3 noches
reservation = new BreakfastDecorator(reservation, 2, 3); // 2 personas, 3 días
reservation = new SpaDecorator(reservation, 1); // 1 sesión
reservation = new TransportDecorator(reservation, 2); // 2 viajes

BigDecimal totalPrice = reservation.getPrice();
String description = reservation.getDescription();
```

---

### 8. **Composite Pattern** - Paquetes de Servicios

Crear paquetes de servicios con descuentos:

```java
ServicePackage package = new ServicePackage("Paquete Romance", 0.10); // 10% descuento
package.addService(new ServiceLeaf("Spa", 800));
package.addService(new ServiceLeaf("Cena", 600));
package.addService(new ServiceLeaf("Champagne", 400));

BigDecimal totalPrice = package.getPrice(); // Precio con descuento aplicado
```

---

### 9. **Facade Pattern** - ReservationFacade

Orquestar proceso completo de reserva en un solo método:

```java
ReservationResult result = reservationFacade.createCompleteReservation(
    "juan.garcia@email.com",  // Email del cliente
    1L,                        // ID de habitación
    LocalDate.of(2025, 12, 20),  // Check-in
    LocalDate.of(2025, 12, 25),  // Check-out
    2,                         // Número de huéspedes
    List.of(1L, 3L, 5L),      // IDs de servicios adicionales
    PaymentMethod.CREDIT_CARD  // Método de pago
);

// El Facade maneja:
// 1. Validación de cliente
// 2. Validación de habitación
// 3. Verificación de disponibilidad
// 4. Obtención de servicios
// 5. Construcción de reserva (Builder)
// 6. Guardado de reserva
// 7. Procesamiento de pago (Adapter)
// 8. Marcado de habitación como ocupada
// 9. Notificaciones (pendiente)
// 10. Retorno de resultado
```

---

## 🔍 Explorar la Base de Datos (Consola H2)

1. **Abrir en el navegador**: http://localhost:8080/h2-console

2. **Configuración de conexión**:
   - JDBC URL: `jdbc:h2:mem:hoteldb`
   - Username: `sa`
   - Password: *(dejar en blanco)*

3. **Queries de ejemplo**:

```sql
-- Ver todos los clientes
SELECT * FROM customers;

-- Ver todas las habitaciones
SELECT * FROM rooms;

-- Ver servicios adicionales
SELECT * FROM additional_services;

-- Ver habitaciones disponibles
SELECT * FROM rooms WHERE available = true;

-- Ver clientes por nivel de lealtad
SELECT * FROM customers WHERE loyalty_level = 'PLATINUM';
```

---

## 📊 Pruebas con Servicios

Actualmente tenemos los servicios **RoomService** y **PaymentService** implementados.

**Nota**: Los controladores REST aún no están implementados (son parte de la Fase 3), pero los servicios funcionan correctamente y pueden probarse directamente desde código Java o creando controladores simples.

---

## 🎨 Próximos Pasos - Fase 3

Para poder probar completamente la API mediante HTTP necesitamos:

1. **Controladores REST**:
   - RoomController
   - CustomerController
   - ReservationController
   - PaymentController
   - ServiceController

2. **Patrones Comportamentales** (10 patrones restantes)

3. **Mappers** (para convertir entre Entity y DTO)

---

## 💡 Ejemplos de Uso Actual

Aunque no hay controladores REST completos, ya puedes:

✅ Ver la consola H2 y explorar datos
✅ Probar el endpoint de test (/api/test)
✅ Ver los logs de Hibernate (SQL queries en consola)
✅ Ver que todas las tablas se crearon correctamente
✅ Verificar que los datos se cargaron

---

## 🐛 Troubleshooting

### Problema: El servidor no responde
**Solución**: Verificar que el proceso esté corriendo
```bash
ps aux | grep java
```

### Problema: Puerto 8080 ocupado
**Solución**: Cambiar puerto en application.properties o matar proceso
```bash
lsof -i :8080
kill -9 <PID>
```

### Problema: Datos no se cargan
**Solución**: Verificar logs de Hibernate en consola del servidor
```bash
# Los logs mostrarán si data.sql se ejecutó correctamente
```

---

**¡Sistema listo para probar!** 🎉
