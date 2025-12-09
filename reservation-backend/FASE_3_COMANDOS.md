# 🎉 API COMPLETA - Comandos de Prueba

## ✅ NUEVOS ENDPOINTS - Reservas y Pagos

### 📅 **RESERVATIONS** (`/api/reservations`)

```bash
# 1. Listar todas las reservas
curl http://localhost:8080/api/reservations

# 2. Obtener reserva por ID
curl http://localhost:8080/api/reservations/1

# 3. Crear nueva reserva (usa Strategy Pattern para calcular precio)
curl -X POST http://localhost:8080/api/reservations \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": 1,
    "roomId": 7,
    "checkInDate": "2025-12-20",
    "checkOutDate": "2025-12-25",
    "numberOfGuests": 2
  }'

# 4. Confirmar reserva (State Pattern + Observer Pattern)
curl -X PATCH http://localhost:8080/api/reservations/1/confirm

# 5. Cancelar reserva
curl -X PATCH http://localhost:8080/api/reservations/1/cancel
```

### 💳 **PAYMENTS** (`/api/payments`)

```bash
# 1. Obtener pago por ID
curl http://localhost:8080/api/payments/1

# 2. Ver pagos de una reserva
curl http://localhost:8080/api/payments/reservation/1

# 3. Procesar pago (Adapter Pattern)
curl -X POST "http://localhost:8080/api/payments?reservationId=1&paymentMethod=CREDIT_CARD"
curl -X POST "http://localhost:8080/api/payments?reservationId=2&paymentMethod=PAYPAL"
curl -X POST "http://localhost:8080/api/payments?reservationId=3&paymentMethod=CASH"

# 4. Reembolsar pago
curl -X POST http://localhost:8080/api/payments/1/refund
```

---

## 🎯 Patrones Implementados

### 1. **Strategy Pattern** - Cálculo de Precios
Al crear una reserva, se aplican automáticamente:
- Temporada alta (+30%): Julio, Agosto, Diciembre
- Fin de semana (+20%): Viernes y Sábado
- Descuento por lealtad: SILVER (-5%), GOLD (-10%), PLATINUM (-20%)

### 2. **State Pattern** - Estados de Reserva
- PENDING → CONFIRMED → COMPLETED
- PENDING/CONFIRMED → CANCELLED

### 3. **Observer Pattern** - Notificaciones
Al crear/confirmar/cancelar una reserva, se envían notificaciones automáticas (logs)

---

## 🧪 Flujo Completo de Prueba

```bash
# Paso 1: Crear cliente
curl -X POST http://localhost:8080/api/customers \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Laura",
    "lastName": "Mendoza",
    "email": "laura@test.com",
    "phone": "+52 951 888 8888",
    "loyaltyLevel": "GOLD"
  }'

# Paso 2: Buscar habitación disponible
curl "http://localhost:8080/api/rooms/available?checkIn=2025-12-20&checkOut=2025-12-25&roomType=SUITE"

# Paso 3: Crear reserva (precio se calcula con Strategy)
curl -X POST http://localhost:8080/api/reservations \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": 6,
    "roomId": 7,
    "checkInDate": "2025-12-20",
    "checkOutDate": "2025-12-25",
    "numberOfGuests": 2
  }'

# Paso 4: Confirmar reserva (Observer notifica)
curl -X PATCH http://localhost:8080/api/reservations/1/confirm

# Paso 5: Procesar pago (Adapter usa gateway)
curl -X POST "http://localhost:8080/api/payments?reservationId=1&paymentMethod=CREDIT_CARD"
```

---

## 📊 **TODOS LOS ENDPOINTS DISPONIBLES**

### ✅ Rooms
- GET `/api/rooms`
- GET `/api/rooms/{id}`
- GET `/api/rooms/available`
- POST `/api/rooms`
- POST `/api/rooms/{id}/clone`

### ✅ Customers
- GET `/api/customers`
- GET `/api/customers/{id}`
- GET `/api/customers/email/{email}`
- POST `/api/customers`

### ✅ Services
- GET `/api/services`
- GET `/api/services/{id}`
- GET `/api/services/type/{type}`
- POST `/api/services`

### ✅ Reservations (NUEVO)
- GET `/api/reservations`
- GET `/api/reservations/{id}`
- POST `/api/reservations`
- PATCH `/api/reservations/{id}/confirm`
- PATCH `/api/reservations/{id}/cancel`

### ✅ Payments (NUEVO)
- GET `/api/payments/{id}`
- GET `/api/payments/reservation/{reservationId}`
- POST `/api/payments`
- POST `/api/payments/{id}/refund`

---

## 🎨 **Archivos Compilados: 75**

**Patrones totales: 12**
- 3 Creacionales
- 6 Estructurales
- 3 Comportamentales (Strategy, State, Observer)

¡API lista para usar! 🚀
