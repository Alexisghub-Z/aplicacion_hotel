# 🧪 Comandos para Probar la API - Fase 3

## ✅ Nuevos Endpoints Disponibles

### 🏨 **ROOMS (Habitaciones)**

```bash
# 1. Listar todas las habitaciones
curl http://localhost:8080/api/rooms

# 2. Obtener habitación por ID
curl http://localhost:8080/api/rooms/1

# 3. Buscar habitaciones disponibles
curl "http://localhost:8080/api/rooms/available?checkIn=2025-12-20&checkOut=2025-12-25"

# 4. Buscar habitaciones disponibles por tipo
curl "http://localhost:8080/api/rooms/available?checkIn=2025-12-20&checkOut=2025-12-25&roomType=SUITE"

# 5. Crear habitación con Factory Pattern
curl -X POST "http://localhost:8080/api/rooms?roomType=SUITE&roomNumber=405&floor=4"

# 6. Clonar habitación (Prototype Pattern)
curl -X POST "http://localhost:8080/api/rooms/1/clone?newRoomNumber=105"

# 7. Actualizar habitación
curl -X PUT http://localhost:8080/api/rooms/1 \
  -H "Content-Type: application/json" \
  -d '{
    "roomNumber": "101",
    "roomType": "SINGLE",
    "price": 850.00,
    "capacity": 1,
    "available": true,
    "floor": 1,
    "amenities": ["WiFi", "TV", "AC"],
    "imageUrl": "/images/rooms/single-101.jpg",
    "description": "Habitación renovada"
  }'

# 8. Cambiar disponibilidad
curl -X PATCH http://localhost:8080/api/rooms/1/toggle-availability

# 9. Eliminar habitación
curl -X DELETE http://localhost:8080/api/rooms/1
```

---

### 👥 **CUSTOMERS (Clientes)**

```bash
# 1. Listar todos los clientes
curl http://localhost:8080/api/customers

# 2. Obtener cliente por ID
curl http://localhost:8080/api/customers/1

# 3. Buscar cliente por email
curl http://localhost:8080/api/customers/email/juan.garcia@email.com

# 4. Crear nuevo cliente
curl -X POST http://localhost:8080/api/customers \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Roberto",
    "lastName": "Pérez Silva",
    "email": "roberto.perez@email.com",
    "phone": "+52 951 666 6666",
    "loyaltyLevel": "SILVER"
  }'

# 5. Actualizar cliente
curl -X PUT http://localhost:8080/api/customers/1 \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Juan",
    "lastName": "García López",
    "email": "juan.garcia@email.com",
    "phone": "+52 951 111 1111",
    "loyaltyLevel": "GOLD"
  }'

# 6. Eliminar cliente
curl -X DELETE http://localhost:8080/api/customers/1
```

---

### 🎯 **SERVICES (Servicios Adicionales)**

```bash
# 1. Listar todos los servicios
curl http://localhost:8080/api/services

# 2. Obtener servicio por ID
curl http://localhost:8080/api/services/1

# 3. Buscar servicios por tipo
curl http://localhost:8080/api/services/type/BREAKFAST
curl http://localhost:8080/api/services/type/SPA
curl http://localhost:8080/api/services/type/EXCURSION

# 4. Crear nuevo servicio
curl -X POST http://localhost:8080/api/services \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Yoga Matutino",
    "description": "Clase de yoga al amanecer (60 minutos)",
    "price": 350.00,
    "serviceType": "SPA"
  }'

# 5. Actualizar servicio
curl -X PUT http://localhost:8080/api/services/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Desayuno Continental Premium",
    "description": "Desayuno buffet con frutas, pan dulce, café y jugos naturales",
    "price": 250.00,
    "serviceType": "BREAKFAST"
  }'

# 6. Eliminar servicio
curl -X DELETE http://localhost:8080/api/services/1
```

---

## 🎨 Comandos con formato JSON (usando jq)

Si tienes `jq` instalado, puedes ver las respuestas formateadas:

```bash
# Listar habitaciones formateado
curl -s http://localhost:8080/api/rooms | jq .

# Ver cliente específico
curl -s http://localhost:8080/api/customers/1 | jq .

# Ver servicios de tipo SPA
curl -s http://localhost:8080/api/services/type/SPA | jq .

# Habitaciones disponibles
curl -s "http://localhost:8080/api/rooms/available?checkIn=2025-12-20&checkOut=2025-12-25" | jq .
```

---

## 📊 Tipos de Datos

### RoomType (Tipos de Habitación)
- `SINGLE` - $800 MXN
- `DOUBLE` - $1,200 MXN
- `SUITE` - $2,500 MXN
- `PRESIDENTIAL` - $5,000 MXN

### LoyaltyLevel (Niveles de Lealtad)
- `REGULAR` - 0% descuento
- `SILVER` - 5% descuento
- `GOLD` - 10% descuento
- `PLATINUM` - 20% descuento

### ServiceType (Tipos de Servicio)
- `BREAKFAST` - Desayunos
- `SPA` - Servicios de spa
- `TRANSPORT` - Transporte
- `EXCURSION` - Excursiones
- `ROOM_SERVICE` - Servicio a habitación

---

## 🧪 Flujo de Prueba Completo

### Escenario 1: Crear cliente y buscar habitación

```bash
# Paso 1: Crear cliente
curl -X POST http://localhost:8080/api/customers \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Laura",
    "lastName": "Mendoza",
    "email": "laura.mendoza@email.com",
    "phone": "+52 951 777 7777",
    "loyaltyLevel": "REGULAR"
  }'

# Paso 2: Buscar habitaciones disponibles
curl -s "http://localhost:8080/api/rooms/available?checkIn=2026-01-15&checkOut=2026-01-20" | jq .

# Paso 3: Ver servicios de desayuno disponibles
curl -s http://localhost:8080/api/services/type/BREAKFAST | jq .
```

### Escenario 2: Usar Factory Pattern

```bash
# Crear una nueva suite usando Factory
curl -X POST "http://localhost:8080/api/rooms?roomType=SUITE&roomNumber=405&floor=4"

# Verificar que se creó con las amenidades predefinidas
curl -s http://localhost:8080/api/rooms | jq '.[] | select(.roomNumber == "405")'
```

### Escenario 3: Usar Prototype Pattern

```bash
# Clonar la habitación 203 (Suite) para crear 403
curl -X POST "http://localhost:8080/api/rooms/7/clone?newRoomNumber=403"

# Ver que se clonó con las mismas características
curl -s http://localhost:8080/api/rooms | jq '.[] | select(.roomNumber == "403")'
```

---

## 🔍 Verificar Estado del Sistema

```bash
# Ver endpoint de test
curl http://localhost:8080/api/test

# Contar habitaciones disponibles
curl -s http://localhost:8080/api/rooms | jq '[.[] | select(.available == true)] | length'

# Contar clientes por nivel de lealtad
curl -s http://localhost:8080/api/customers | jq 'group_by(.loyaltyLevel) | map({level: .[0].loyaltyLevel, count: length})'

# Ver servicios más caros
curl -s http://localhost:8080/api/services | jq 'sort_by(.price) | reverse | .[0:3]'
```

---

## 📝 Datos Iniciales

### Clientes (IDs 1-5)
1. Juan García López - REGULAR
2. María Hernández Ruiz - SILVER
3. Carlos Martínez Sánchez - GOLD
4. Ana López Flores - PLATINUM
5. Pedro Ramírez Cruz - REGULAR

### Habitaciones (IDs 1-12)
- 101, 102 - SINGLE
- 103, 104, 201, 202 - DOUBLE
- 203, 204, 301, 302 - SUITE
- 303, 304 - PRESIDENTIAL

### Servicios (IDs 1-8)
1. Desayuno Continental - $200
2. Desayuno Oaxaqueño - $280
3. Masaje Relajante - $800
4. Tratamiento Facial - $650
5. Transporte Aeropuerto - $500
6. Tour Monte Albán - $1,200
7. Tour Hierve el Agua - $1,500
8. Servicio de Habitación Premium - $150

---

## ✅ Todo listo para probar!

Espera a que el servidor termine de iniciar (~20 segundos) y luego prueba los comandos.
