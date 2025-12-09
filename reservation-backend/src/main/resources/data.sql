-- Datos iniciales simples para probar el sistema
-- Por ahora solo insertaremos clientes y habitaciones sin servicios ni reservas

-- ============================================
-- CLIENTES (Customers)
-- ============================================
INSERT INTO customers (first_name, last_name, email, phone, loyalty_level, created_at, updated_at) VALUES
('Juan', 'García López', 'juan.garcia@email.com', '+52 951 111 1111', 'REGULAR', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO customers (first_name, last_name, email, phone, loyalty_level, created_at, updated_at) VALUES
('María', 'Hernández Ruiz', 'maria.hernandez@email.com', '+52 951 222 2222', 'SILVER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO customers (first_name, last_name, email, phone, loyalty_level, created_at, updated_at) VALUES
('Carlos', 'Martínez Sánchez', 'carlos.martinez@email.com', '+52 951 333 3333', 'GOLD', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO customers (first_name, last_name, email, phone, loyalty_level, created_at, updated_at) VALUES
('Ana', 'López Flores', 'ana.lopez@email.com', '+52 951 444 4444', 'PLATINUM', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO customers (first_name, last_name, email, phone, loyalty_level, created_at, updated_at) VALUES
('Pedro', 'Ramírez Cruz', 'pedro.ramirez@email.com', '+52 951 555 5555', 'REGULAR', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- ============================================
-- HABITACIONES (Rooms)
-- ============================================
-- Habitaciones Sencillas
INSERT INTO rooms (room_number, room_type, price, capacity, available, floor, image_url, description) VALUES
('101', 'SINGLE', 800.00, 1, true, 1, '/images/rooms/single-101.jpg', 'Habitación sencilla en planta baja con vista al jardín');

INSERT INTO rooms (room_number, room_type, price, capacity, available, floor, image_url, description) VALUES
('102', 'SINGLE', 800.00, 1, true, 1, '/images/rooms/single-102.jpg', 'Habitación sencilla cómoda y acogedora');

-- Habitaciones Dobles
INSERT INTO rooms (room_number, room_type, price, capacity, available, floor, image_url, description) VALUES
('103', 'DOUBLE', 1200.00, 2, true, 1, '/images/rooms/double-103.jpg', 'Habitación doble con balcón');

INSERT INTO rooms (room_number, room_type, price, capacity, available, floor, image_url, description) VALUES
('104', 'DOUBLE', 1200.00, 2, true, 1, '/images/rooms/double-104.jpg', 'Habitación doble espaciosa');

INSERT INTO rooms (room_number, room_type, price, capacity, available, floor, image_url, description) VALUES
('201', 'DOUBLE', 1200.00, 2, true, 2, '/images/rooms/double-201.jpg', 'Habitación doble con vista panorámica');

INSERT INTO rooms (room_number, room_type, price, capacity, available, floor, image_url, description) VALUES
('202', 'DOUBLE', 1200.00, 2, false, 2, '/images/rooms/double-202.jpg', 'Habitación doble con mini bar');

-- Suites
INSERT INTO rooms (room_number, room_type, price, capacity, available, floor, image_url, description) VALUES
('203', 'SUITE', 2500.00, 4, true, 2, '/images/rooms/suite-203.jpg', 'Suite familiar con sala de estar');

INSERT INTO rooms (room_number, room_type, price, capacity, available, floor, image_url, description) VALUES
('204', 'SUITE', 2500.00, 4, true, 2, '/images/rooms/suite-204.jpg', 'Suite de lujo con jacuzzi');

INSERT INTO rooms (room_number, room_type, price, capacity, available, floor, image_url, description) VALUES
('301', 'SUITE', 2500.00, 4, true, 3, '/images/rooms/suite-301.jpg', 'Suite con kitchenette y balcón amplio');

INSERT INTO rooms (room_number, room_type, price, capacity, available, floor, image_url, description) VALUES
('302', 'SUITE', 2500.00, 4, false, 3, '/images/rooms/suite-302.jpg', 'Suite premium con comedor privado');

-- Suites Presidenciales
INSERT INTO rooms (room_number, room_type, price, capacity, available, floor, image_url, description) VALUES
('303', 'PRESIDENTIAL', 5000.00, 6, true, 3, '/images/rooms/presidential-303.jpg', 'Suite presidencial con terraza privada');

INSERT INTO rooms (room_number, room_type, price, capacity, available, floor, image_url, description) VALUES
('304', 'PRESIDENTIAL', 5000.00, 6, true, 3, '/images/rooms/presidential-304.jpg', 'Suite presidencial con vistas panorámicas');

-- ============================================
-- SERVICIOS ADICIONALES (Additional Services)
-- ============================================
INSERT INTO additional_services (name, description, price, service_type) VALUES
('Desayuno Continental', 'Desayuno buffet con frutas, pan dulce, café y jugos', 200.00, 'BREAKFAST');

INSERT INTO additional_services (name, description, price, service_type) VALUES
('Desayuno Oaxaqueño', 'Desayuno tradicional con tlayudas, chapulines y chocolate', 280.00, 'BREAKFAST');

INSERT INTO additional_services (name, description, price, service_type) VALUES
('Masaje Relajante', 'Masaje de cuerpo completo (60 minutos)', 800.00, 'SPA');

INSERT INTO additional_services (name, description, price, service_type) VALUES
('Tratamiento Facial', 'Limpieza facial profunda (45 minutos)', 650.00, 'SPA');

INSERT INTO additional_services (name, description, price, service_type) VALUES
('Transporte Aeropuerto', 'Servicio de transporte desde/hacia aeropuerto Xoxocotlán', 500.00, 'TRANSPORT');

INSERT INTO additional_services (name, description, price, service_type) VALUES
('Tour Monte Albán', 'Excursión guiada a zona arqueológica Monte Albán', 1200.00, 'EXCURSION');

INSERT INTO additional_services (name, description, price, service_type) VALUES
('Tour Hierve el Agua', 'Excursión a cascadas petrificadas con comida incluida', 1500.00, 'EXCURSION');

INSERT INTO additional_services (name, description, price, service_type) VALUES
('Servicio de Habitación Premium', 'Servicio de comida y bebidas en habitación 24h', 150.00, 'ROOM_SERVICE');

-- ============================================
-- RESERVAS CON SERVICIOS (Reservations)
-- ============================================
-- Reserva 1: Cliente GOLD con Suite
INSERT INTO reservations (customer_id, room_id, check_in_date, check_out_date, number_of_guests, total_price, status, created_at, updated_at) VALUES
(3, 7, '2025-12-15', '2025-12-18', 3, 7500.00, 'CONFIRMED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Reserva 2: Cliente PLATINUM con Suite Presidencial
INSERT INTO reservations (customer_id, room_id, check_in_date, check_out_date, number_of_guests, total_price, status, created_at, updated_at) VALUES
(4, 11, '2025-12-20', '2025-12-25', 5, 25000.00, 'CONFIRMED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Reserva 3: Cliente REGULAR con Doble (ocupada - habitación 202)
INSERT INTO reservations (customer_id, room_id, check_in_date, check_out_date, number_of_guests, total_price, status, created_at, updated_at) VALUES
(1, 6, '2025-12-10', '2025-12-12', 2, 2400.00, 'COMPLETED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Reserva 4: Cliente SILVER con Suite (ocupada - habitación 302)
INSERT INTO reservations (customer_id, room_id, check_in_date, check_out_date, number_of_guests, total_price, status, created_at, updated_at) VALUES
(2, 10, '2025-12-08', '2025-12-14', 4, 15000.00, 'CONFIRMED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- ============================================
-- SERVICIOS DE RESERVAS (Reservation Services)
-- ============================================
-- Reserva 1 con servicios (Tour + Desayuno + Masaje)
INSERT INTO reservation_services (reservation_id, service_id) VALUES (1, 6); -- Tour Monte Albán
INSERT INTO reservation_services (reservation_id, service_id) VALUES (1, 1); -- Desayuno Continental
INSERT INTO reservation_services (reservation_id, service_id) VALUES (1, 3); -- Masaje Relajante

-- Reserva 2 con servicios premium (Tour + Desayuno Oaxaqueño + Spa + Transporte)
INSERT INTO reservation_services (reservation_id, service_id) VALUES (2, 7); -- Tour Hierve el Agua
INSERT INTO reservation_services (reservation_id, service_id) VALUES (2, 2); -- Desayuno Oaxaqueño
INSERT INTO reservation_services (reservation_id, service_id) VALUES (2, 3); -- Masaje Relajante
INSERT INTO reservation_services (reservation_id, service_id) VALUES (2, 4); -- Tratamiento Facial
INSERT INTO reservation_services (reservation_id, service_id) VALUES (2, 5); -- Transporte Aeropuerto

-- Reserva 3 con servicio simple (Desayuno)
INSERT INTO reservation_services (reservation_id, service_id) VALUES (3, 1); -- Desayuno Continental

-- Reserva 4 con varios servicios (Tours + Desayuno + Transporte)
INSERT INTO reservation_services (reservation_id, service_id) VALUES (4, 6); -- Tour Monte Albán
INSERT INTO reservation_services (reservation_id, service_id) VALUES (4, 7); -- Tour Hierve el Agua
INSERT INTO reservation_services (reservation_id, service_id) VALUES (4, 2); -- Desayuno Oaxaqueño
INSERT INTO reservation_services (reservation_id, service_id) VALUES (4, 5); -- Transporte Aeropuerto

-- ============================================
-- PAGOS (Payments)
-- ============================================
-- Pago para Reserva 1 (COMPLETADO - Tarjeta)
INSERT INTO payments (reservation_id, amount, payment_method, payment_status, transaction_id, payment_date) VALUES
(1, 7500.00, 'CREDIT_CARD', 'COMPLETED', 'ch_stripe_1234567890', '2025-12-10 14:30:00');

-- Pago para Reserva 2 (COMPLETADO - PayPal)
INSERT INTO payments (reservation_id, amount, payment_method, payment_status, transaction_id, payment_date) VALUES
(2, 25000.00, 'PAYPAL', 'COMPLETED', 'pp_paypal_9876543210', '2025-12-15 10:15:00');

-- Pago para Reserva 3 (COMPLETADO - Efectivo)
INSERT INTO payments (reservation_id, amount, payment_method, payment_status, transaction_id, payment_date) VALUES
(3, 2400.00, 'CASH', 'COMPLETED', 'cash_rec_001', '2025-12-08 16:45:00');

-- Pago para Reserva 4 (COMPLETADO - Tarjeta)
INSERT INTO payments (reservation_id, amount, payment_method, payment_status, transaction_id, payment_date) VALUES
(4, 15000.00, 'CREDIT_CARD', 'COMPLETED', 'ch_stripe_5555555555', '2025-12-05 11:20:00');
-- ============================================
-- PAQUETES DE SERVICIOS (Packages)
-- ============================================

-- Paquete Romántico (Spa + Cena + Decoración) - 15% descuento
INSERT INTO packages (name, description, discount, image_url, active, created_at, updated_at) VALUES
('Paquete Romántico', 'El paquete perfecto para una escapada romántica en Oaxaca. Incluye masaje de pareja, cena especial y decoración con pétalos de rosa.', 0.15, '/images/packages/romantic.jpg', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Paquete Familiar (Desayuno + Excursión) - 10% descuento
INSERT INTO packages (name, description, discount, image_url, active, created_at, updated_at) VALUES
('Paquete Familiar', 'Disfruta en familia con desayuno buffet todos los días y una emocionante excursión a Monte Albán.', 0.10, '/images/packages/family.jpg', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Paquete Todo Incluido (Desayuno + Spa + Transporte + Excursión) - 20% descuento
INSERT INTO packages (name, description, discount, image_url, active, created_at, updated_at) VALUES
('Paquete Todo Incluido', 'La experiencia completa en Oaxaca Dreams. Desayuno, spa, transporte al aeropuerto y tours a sitios arqueológicos.', 0.20, '/images/packages/all-inclusive.jpg', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Paquete Wellness (Spa + Desayuno) - 12% descuento
INSERT INTO packages (name, description, discount, image_url, active, created_at, updated_at) VALUES
('Paquete Wellness', 'Relájate y rejuvenece con masajes terapéuticos y un delicioso desayuno saludable.', 0.12, '/images/packages/wellness.jpg', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- ============================================
-- RELACIONES PAQUETE-SERVICIO (Package-Services)
-- ============================================

-- Paquete Romántico (ID=1): Spa (2) + Cena (1)
INSERT INTO package_services (package_id, service_id) VALUES (1, 2);
INSERT INTO package_services (package_id, service_id) VALUES (1, 1);

-- Paquete Familiar (ID=2): Desayuno (1) + Excursión (4)
INSERT INTO package_services (package_id, service_id) VALUES (2, 1);
INSERT INTO package_services (package_id, service_id) VALUES (2, 4);

-- Paquete Todo Incluido (ID=3): Desayuno (1) + Spa (2) + Transporte (3) + Excursión (4)
INSERT INTO package_services (package_id, service_id) VALUES (3, 1);
INSERT INTO package_services (package_id, service_id) VALUES (3, 2);
INSERT INTO package_services (package_id, service_id) VALUES (3, 3);
INSERT INTO package_services (package_id, service_id) VALUES (3, 4);

-- Paquete Wellness (ID=4): Spa (2) + Desayuno (1)
INSERT INTO package_services (package_id, service_id) VALUES (4, 2);
INSERT INTO package_services (package_id, service_id) VALUES (4, 1);
