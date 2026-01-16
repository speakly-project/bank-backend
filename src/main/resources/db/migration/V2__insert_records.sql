-- Insertar clientes de ejemplo
INSERT INTO client (username, password, name, first_surname, second_surname, dni, api_key) VALUES
('juan', 'juan123', 'Juan', 'Pérez', 'García', '12345678A', 'apikey1'),
('maria', 'maria123', 'María', 'López', 'Martínez', '87654321B', 'apikey2'),
('carlos', 'carlos123', 'Carlos', 'Ruiz', 'Sánchez', '11223344C', 'apikey3'),
('pepe', 'pepe123', 'Pepe', 'Pérez', 'López', '12345677A', 'apikey4');

-- Insertar cuentas bancarias
INSERT INTO bank_account (iban, balance, client_id) VALUES
('ES9121000418450200051332', 50000.0000000000, 1),
('ES7921000813610123456789', 10500.000000000, 1),
('ES1420805801101234567891', 25000.00000000, 2),
('ES8923100001180123456789', 15000.0000000000, 3),
('ES7620770024003102575766', 15000.00000000, 4),
('ES9121000418450200051333', 32000.0000000000, 4);

-- Insertar tarjetas de crédito
INSERT INTO credit_card (card_number, expiration_date, cvv, full_name, bank_account_id) VALUES
('4532015112830366', '2027-12', '123', 'Juan Perez Garcia', 1),
('5425233430109903', '2028-06', '456', 'Juan Pérez Garcia', 2),
('4716042456789012', '2027-09', '789', 'Maria Lopez Martinez', 3),
('5500005555555559', '2029-03', '321', 'Carlos Ruiz Sanchez', 4),
('4532756279624064', '2027-09', '300', 'Pepe Perez Lopez', 5),
('5500005555554444', '2028-11', '123', 'Pepe Perez Lopez', 5),
('6767676767676767', '2067-06', '676', 'Pepe Perez Lopez', 6);

-- Insertar transacciones bancarias
INSERT INTO bank_transaction (origin, type, credit_card_number, transaction_date, amount, description, bank_account_id) VALUES
('TRANSFER', 'ADD', NULL, '2026-01-01 10:30:00', 1000.00, 'Transferencia recibida de nómina', 1),
('CREDIT_CARD', 'SUBTRACT', '4532015112830366', '2026-01-02 14:25:00', 50.00, 'Compra en supermercado', 1),
('DOM', 'SUBTRACT', '4532015112830366', '2026-01-03 09:00:00', 75.50, 'Domiciliación luz', 1),
('TRANSFER', 'ADD', NULL, '2026-01-01 11:00:00', 2000.00, 'Transferencia recibida', 2),
('CREDIT_CARD', 'SUBTRACT', '5425233430109903', '2026-01-04 18:45:00', 120.00, 'Compra online', 2),
('TRANSFER', 'ADD', NULL, '2026-01-01 12:00:00', 500.00, 'Transferencia de familiar', 3),
('CREDIT_CARD', 'SUBTRACT', '4716042456789012', '2026-01-05 16:20:00', 35.25, 'Cafetería', 3),
('DOM', 'SUBTRACT', '4716042456789012', '2026-01-05 09:00:00', 45.00, 'Domiciliación internet', 3),
('TRANSFER', 'ADD', NULL, '2026-01-01 13:00:00', 5000.00, 'Ingreso inicial', 4),
('CREDIT_CARD', 'SUBTRACT', '5500005555555559', '2026-01-06 20:15:00', 200.00, 'Compra en tienda de electrónica', 4),
('CREDIT_CARD', 'SUBTRACT', '4532756279624064', '2024-01-10 09:15:00', 1500.00, 'Compra grande', 5),
('CREDIT_CARD', 'SUBTRACT', '4532756279624064', '2024-01-11 13:05:00', 42.90, 'Comida en restaurante', 5),
('CREDIT_CARD', 'SUBTRACT', '4532756279624064', '2024-01-12 08:00:00', 39.99, 'Suscripción streaming', 5),
('CREDIT_CARD', 'SUBTRACT', '4532756279624064', '2024-01-13 18:20:00', 120.50, 'Compra en ferretería', 5),
('CREDIT_CARD', 'ADD', NULL, '2024-01-14 12:30:00', 300.00, 'Bizum', 5),
('CREDIT_CARD', 'SUBTRACT', '4532756279624064', '2024-01-15 10:30:00', 500.00, 'Pago de la baliza v16.', 5),
('CREDIT_CARD', 'SUBTRACT', '4532756279624064', '2024-01-16 19:45:00', 15.75, 'Cafetería', 5),
('CREDIT_CARD', 'SUBTRACT', '4532756279624064', '2024-01-20 07:30:00', 55.10, 'Factura teléfono', 5),
('CREDIT_CARD', 'ADD', NULL, '2024-01-25 16:10:00', 800.00, 'Ingreso efectivo', 5),
('CREDIT_CARD', 'SUBTRACT', '4532756279624064', '2024-02-01 09:10:00', 9.50, 'Desayuno', 5),
('CREDIT_CARD', 'SUBTRACT', '4532756279624064', '2024-02-03 18:05:00', 63.20, 'Compra en supermercado', 5),
('CREDIT_CARD', 'SUBTRACT', '4532756279624064', '2024-02-05 21:30:00', 27.99, 'Pedido comida a domicilio', 5),
('CREDIT_CARD', 'SUBTRACT', '4532756279624064', '2024-02-07 12:15:00', 18.40, 'Farmacia', 5),
('CREDIT_CARD', 'SUBTRACT', '4532756279624064', '2024-02-09 17:50:00', 89.95, 'Ropa', 5),
('CREDIT_CARD', 'SUBTRACT', '4532756279624064', '2024-02-12 08:25:00', 12.00, 'Transporte', 5),
('CREDIT_CARD', 'ADD', NULL, '2024-02-14 10:00:00', 25.00, 'Devolución de compra', 5),
('CREDIT_CARD', 'SUBTRACT', '4532756279624064', '2024-02-16 19:20:00', 134.70, 'Compra electrónica', 5),
('CREDIT_CARD', 'SUBTRACT', '4532756279624064', '2024-02-18 11:45:00', 7.80, 'Parking', 5),
('CREDIT_CARD', 'SUBTRACT', '4532756279624064', '2024-02-20 16:35:00', 49.90, 'Regalo', 5),
('CREDIT_CARD', 'SUBTRACT', '4532756279624064', '2024-02-22 09:05:00', 21.30, 'Café y tostada', 5),
('CREDIT_CARD', 'SUBTRACT', '4532756279624064', '2024-02-23 14:10:00', 58.65, 'Supermercado', 5),
('CREDIT_CARD', 'SUBTRACT', '4532756279624064', '2024-02-24 18:40:00', 12.90, 'Cine', 5),
('CREDIT_CARD', 'SUBTRACT', '4532756279624064', '2024-02-25 11:25:00', 26.75, 'Gasolina', 5),
('CREDIT_CARD', 'SUBTRACT', '4532756279624064', '2024-02-26 20:15:00', 33.50, 'Cena', 5),
('CREDIT_CARD', 'SUBTRACT', '4532756279624064', '2024-02-27 08:55:00', 2.40, 'Metro', 5),
('CREDIT_CARD', 'ADD', NULL, '2024-02-28 10:05:00', 19.99, 'Reembolso', 5),
('CREDIT_CARD', 'SUBTRACT', '4532756279624064', '2024-02-29 16:45:00', 74.90, 'Zapatos', 5),
('CREDIT_CARD', 'SUBTRACT', '4532756279624064', '2024-03-01 13:20:00', 6.80, 'Parking', 5),
('CREDIT_CARD', 'SUBTRACT', '4532756279624064', '2024-03-02 19:10:00', 44.00, 'Compra hogar', 5);
