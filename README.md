# Sistema de Reservas de Hotel - Oaxaca Dreams

Sistema completo de gestión de reservas hoteleras con panel de administración.

## Requisitos Previos

Antes de ejecutar el proyecto, asegúrate de tener instalado:

### Backend (Spring Boot)
- **Java JDK 17** o superior
  - Verifica con: `java -version`
  - Descarga: https://www.oracle.com/java/technologies/downloads/

- **Maven** (opcional, el proyecto incluye Maven Wrapper)
  - Verifica con: `mvn -version`

### Frontend (React + Vite)
- **Node.js 18+** y **npm**
  - Verifica con: `node -v` y `npm -v`
  - Descarga: https://nodejs.org/

### IDE Recomendado
- **IntelliJ IDEA** (Community o Ultimate)
  - Descarga: https://www.jetbrains.com/idea/download/

---

## 🪟 INSTRUCCIONES PARA WINDOWS

### Pasos para configurar el proyecto en Windows:

1. **Descargar e instalar Java JDK 17**
   - Ir a: https://www.oracle.com/java/technologies/downloads/#java17
   - Descargar "Windows x64 Installer"
   - Ejecutar el instalador y seguir los pasos
   - Verificar instalación abriendo **CMD** o **PowerShell** y ejecutar: `java -version`

2. **Descargar e instalar Node.js**
   - Ir a: https://nodejs.org/
   - Descargar la versión LTS (recomendada)
   - Ejecutar el instalador (marcar todas las opciones por defecto)
   - Verificar instalación abriendo **CMD** o **PowerShell** y ejecutar: `node -v` y `npm -v`

3. **Descargar IntelliJ IDEA**
   - Ir a: https://www.jetbrains.com/idea/download/#section=windows
   - Descargar la versión **Community** (gratis) o **Ultimate** (30 días gratis)
   - Instalar normalmente

4. **Descomprimir el proyecto**
   - Extraer el archivo ZIP en una carpeta (ejemplo: `C:\Users\TuUsuario\Desktop\hotel-dev`)
   - **IMPORTANTE:** Evita rutas con espacios o caracteres especiales

5. **Abrir el proyecto en IntelliJ**
   - Abrir IntelliJ IDEA
   - Click en **File → Open**
   - Navegar a la carpeta `hotel-dev` y seleccionarla
   - Click en **OK**
   - Esperar a que IntelliJ descargue las dependencias de Maven (puede tardar 5-10 minutos)

6. **Instalar dependencias del Frontend**
   - Dentro de IntelliJ, abrir la terminal: **View → Tool Windows → Terminal**
   - Ejecutar los siguientes comandos:
   ```bash
   cd frontend
   npm install
   ```
   - Este proceso puede tardar 3-5 minutos

7. **Ejecutar el Backend**
   - En IntelliJ, navegar a: `reservation-backend → src → main → java → com.hotel.reservation → HotelReservationBackendApplication`
   - Click derecho en `HotelReservationBackendApplication.java`
   - Seleccionar **Run 'HotelReservationBackendApplication'**
   - Esperar a que aparezca el mensaje: `Started HotelReservationBackendApplication`

8. **Ejecutar el Frontend**
   - En la terminal de IntelliJ (asegurarte de estar en la carpeta `frontend`):
   ```bash
   npm run dev
   ```
   - Debería aparecer: `Local: http://localhost:5173/`

9. **Abrir la aplicación**
   - Abrir el navegador (Chrome, Edge, Firefox)
   - Ir a: `http://localhost:5173`

### Comandos en Windows

**Para el Backend (en la carpeta `reservation-backend`):**
```bash
# Usar Maven Wrapper en Windows
mvnw.cmd spring-boot:run
```

**Para el Frontend (en la carpeta `frontend`):**
```bash
# Instalar dependencias
npm install

# Ejecutar en modo desarrollo
npm run dev
```

### Problemas Comunes en Windows

**Error: "javac no se reconoce como un comando"**
- Solución: Agregar Java al PATH de Windows
  1. Buscar "Variables de entorno" en el menú inicio
  2. Click en "Variables de entorno"
  3. En "Variables del sistema", buscar "Path" y click en "Editar"
  4. Agregar la ruta donde se instaló Java (ejemplo: `C:\Program Files\Java\jdk-17\bin`)
  5. Reiniciar IntelliJ

**Error: "Puerto 8080 ya está en uso"**
- Abrir PowerShell como Administrador y ejecutar:
```powershell
Get-Process -Id (Get-NetTCPConnection -LocalPort 8080).OwningProcess | Stop-Process
```

**Error: "Cannot find module" en Frontend**
- En la terminal de IntelliJ:
```bash
cd frontend
rmdir /s node_modules
del package-lock.json
npm install
```

**IntelliJ no descarga las dependencias de Maven**
1. Click derecho en `pom.xml`
2. Seleccionar **Maven → Reload Project**
3. Si sigue sin funcionar: **File → Invalidate Caches → Invalidate and Restart**

---

## Configuración del Proyecto en IntelliJ IDEA (Linux/Mac)

### 1. Importar el Proyecto

1. Abre IntelliJ IDEA
2. Selecciona **File → Open**
3. Navega a la carpeta `hotel-dev` y selecciónala
4. IntelliJ detectará automáticamente el proyecto Maven y lo configurará

### 2. Configurar el Backend (Spring Boot)

#### Opción A: Usando IntelliJ

1. Abre el proyecto en IntelliJ
2. Espera a que Maven descargue todas las dependencias (puede tardar unos minutos)
3. Navega a: `reservation-backend/src/main/java/com/hotel/reservation/HotelReservationBackendApplication.java`
4. Haz clic derecho en el archivo → **Run 'HotelReservationBackendApplication'**

#### Opción B: Usando Terminal

```bash
cd reservation-backend
./mvnw spring-boot:run
```

En Windows:
```bash
mvnw.cmd spring-boot:run
```

**El backend estará disponible en:** `http://localhost:8080`

### 3. Configurar el Frontend (React)

1. Abre una nueva terminal en IntelliJ: **View → Tool Windows → Terminal**

2. Navega al directorio del frontend:
```bash
cd frontend
```

3. Instala las dependencias de Node:
```bash
npm install
```

4. Inicia el servidor de desarrollo:
```bash
npm run dev
```

**El frontend estará disponible en:** `http://localhost:5173`

---

## Estructura del Proyecto

```
hotel-dev/
├── reservation-backend/          # Backend Spring Boot
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/             # Código fuente Java
│   │   │   └── resources/        # Configuración
│   │   └── test/                 # Tests
│   ├── data/                     # Base de datos H2
│   ├── pom.xml                   # Dependencias Maven
│   └── mvnw                      # Maven Wrapper
│
├── frontend/                     # Frontend React
│   ├── src/
│   │   ├── components/           # Componentes React
│   │   ├── pages/                # Páginas
│   │   ├── context/              # Context API
│   │   └── App.jsx               # Componente principal
│   ├── package.json              # Dependencias npm
│   └── vite.config.js            # Configuración Vite
│
└── README.md                     # Este archivo
```

---

## Credenciales de Acceso

### Panel de Administración
- **URL:** `http://localhost:5173/admin/login`
- **Usuario:** `admin@hotel.com`
- **Contraseña:** `admin123`

### Base de Datos H2 Console (desarrollo)
- **URL:** `http://localhost:8080/h2-console`
- **JDBC URL:** `jdbc:h2:file:./data/hoteldb`
- **Usuario:** `sa`
- **Contraseña:** *(dejar en blanco)*

---

## Características del Sistema

### Panel Público (Clientes)
- Ver habitaciones disponibles
- Hacer reservaciones
- Consultar reservaciones existentes
- Seleccionar paquetes especiales
- Sistema de pagos

### Panel de Administración
- **Dashboard:** Vista general del hotel
- **Habitaciones:** Gestión completa de habitaciones (crear, editar, eliminar)
- **Reservas:** Administración de todas las reservaciones
- **Paquetes:** Gestión de paquetes promocionales
- **Reportes:** Estadísticas y análisis
- **Configuración:** Ajustes del sistema

---

## Solución de Problemas Comunes

### Error: "Puerto 8080 ya está en uso"
```bash
# En Linux/Mac
lsof -ti:8080 | xargs kill -9

# En Windows (PowerShell)
Get-Process -Id (Get-NetTCPConnection -LocalPort 8080).OwningProcess | Stop-Process
```

### Error: "Database may be already in use"
```bash
# En Linux/Mac
cd reservation-backend/data
rm *.lock

# En Windows (CMD)
cd reservation-backend\data
del *.lock
```

### Error: "Cannot find module" en Frontend
```bash
cd frontend
rm -rf node_modules package-lock.json
npm install
```

### IntelliJ no reconoce el proyecto Maven
1. Click derecho en `pom.xml`
2. Selecciona **Maven → Reload Project**

### El backend no inicia en IntelliJ
1. Verifica que el JDK 17 esté configurado:
   - **File → Project Structure → Project → SDK**
2. Asegúrate de que Maven haya descargado las dependencias:
   - Click en el ícono de Maven en el panel derecho
   - Click en "Reload All Maven Projects"

---

## Comandos Útiles

### Backend
```bash
# Compilar el proyecto
./mvnw clean install

# Ejecutar tests
./mvnw test

# Empaquetar (crear JAR)
./mvnw package
```

### Frontend
```bash
# Instalar dependencias
npm install

# Modo desarrollo
npm run dev

# Compilar para producción
npm run build

# Vista previa de producción
npm run preview

# Verificar código (linting)
npm run lint
```

---

## Tecnologías Utilizadas

### Backend
- Spring Boot 3.2.0
- Spring Data JPA
- H2 Database (desarrollo)
- Maven
- Java 17

### Frontend
- React 19
- Vite 7
- React Router DOM
- React Bootstrap
- Axios
- React Icons

---

## Notas Importantes

1. **Base de Datos:** El proyecto usa H2 en modo archivo. Los datos se guardan en `reservation-backend/data/hoteldb.mv.db`

2. **Primer Inicio:** La primera vez que ejecutes el backend, creará automáticamente:
   - La base de datos
   - Tablas necesarias
   - Un usuario administrador de prueba

3. **CORS:** El backend está configurado para aceptar peticiones desde `http://localhost:5173`

4. **Archivos a NO incluir en el ZIP:**
   - `node_modules/` (se regenera con `npm install`)
   - `target/` (se regenera al compilar)
   - `*.log`
   - Archivos `.DS_Store` (Mac)
   - Carpeta `.idea/` (configuración de IntelliJ, opcional)

---

## Exportar el Proyecto como ZIP

### Antes de exportar, asegúrate de:

1. **Detener los servidores** (backend y frontend)

2. **Excluir carpetas pesadas** al crear el ZIP:
   - `frontend/node_modules/` (se regenera con npm install)
   - `reservation-backend/target/` (se regenera al compilar)
   - `.idea/` (opcional)

3. **Comprimir la carpeta** `hotel-dev` completa

### En la nueva PC Windows, después de descomprimir:

1. Instalar Java JDK 17 y Node.js (si no los tiene)
2. Instalar IntelliJ IDEA
3. Abrir el proyecto en IntelliJ (File → Open)
4. Esperar a que Maven descargue dependencias (5-10 min)
5. Abrir terminal en IntelliJ y ejecutar:
   ```bash
   cd frontend
   npm install
   ```
6. Ejecutar el backend: Click derecho en `HotelReservationBackendApplication.java` → Run
7. Ejecutar el frontend: En terminal ejecutar `npm run dev`
8. Abrir navegador en `http://localhost:5173`

---

## 📋 Guía Rápida de Inicio (Resumen para Windows)

**Si ya tienes Java 17, Node.js e IntelliJ instalados:**

1. Descomprimir el proyecto ZIP
2. Abrir IntelliJ → File → Open → Seleccionar carpeta `hotel-dev`
3. Esperar a que descargue dependencias de Maven
4. Abrir terminal en IntelliJ y ejecutar:
   ```bash
   cd frontend
   npm install
   ```
5. Click derecho en `HotelReservationBackendApplication.java` → Run
6. En terminal ejecutar: `npm run dev`
7. Abrir navegador en: `http://localhost:5173`
8. Login admin: `admin@hotel.com` / `admin123`

**Tiempo estimado de configuración:** 10-15 minutos (primera vez)

---

## Contacto y Soporte

Para preguntas o problemas, consulta la documentación de:
- Spring Boot: https://spring.io/projects/spring-boot
- React: https://react.dev/
- Vite: https://vitejs.dev/
