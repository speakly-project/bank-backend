-- Insertar clientes de ejemplo
INSERT INTO client (username, password, name, first_surname, second_surname, dni, api_key) VALUES
('juan.perez', 'pass1', 'Juan', 'Pérez', 'García', '12345678A', 'apikey1'),
('maria.lopez', 'pass2', 'María', 'López', 'Martínez', '87654321B', 'apikey2'),
('carlos.ruiz', 'pass3', 'Carlos', 'Ruiz', 'Sánchez', '11223344C', 'apikey3');

-- Insertar cuentas bancarias
INSERT INTO bank_account (iban, balance, client_id) VALUES
('ES9121000418450200051332', 5000.0000000000, 1),
('ES7921000813610123456789', 10500.5000000000, 1),
('ES1420805801101234567891', 2500.7500000000, 2),
('ES8923100001180123456789', 15000.0000000000, 3);

-- Insertar tarjetas de crédito
INSERT INTO credit_card (card_number, expiration_date, cvc, full_name, bank_account_id) VALUES
('4532015112830366', '2027-12', '123', 'Juan Pérez García', 1),
('5425233430109903', '2028-06', '456', 'Juan Pérez García', 2),
('4716042456789012', '2027-09', '789', 'María López Martínez', 3),
('5500005555555559', '2029-03', '321', 'Carlos Ruiz Sánchez', 4);

-- Insertar transacciones bancarias
INSERT INTO bank_transaction (origin, type, credit_card_number, transaction_date, amount, description, bank_account_id) VALUES
('TRANSFER', 'ADD', NULL, '2026-01-01 10:30:00', 1000.00, 'Transferencia recibida de nómina', 1),
('CREDIT_CARD', 'SUBTRACT', '4532015112830366', '2026-01-02 14:25:00', 50.00, 'Compra en supermercado', 1),
('DOM', 'SUBTRACT', NULL, '2026-01-03 09:00:00', 75.50, 'Domiciliación luz', 1),
('TRANSFER', 'ADD', NULL, '2026-01-01 11:00:00', 2000.00, 'Transferencia recibida', 2),
('CREDIT_CARD', 'SUBTRACT', '5425233430109903', '2026-01-04 18:45:00', 120.00, 'Compra online', 2),
('TRANSFER', 'ADD', NULL, '2026-01-01 12:00:00', 500.00, 'Transferencia de familiar', 3),
('CREDIT_CARD', 'SUBTRACT', '4716042456789012', '2026-01-05 16:20:00', 35.25, 'Cafetería', 3),
('DOM', 'SUBTRACT', NULL, '2026-01-05 09:00:00', 45.00, 'Domiciliación internet', 3),
('TRANSFER', 'ADD', NULL, '2026-01-01 13:00:00', 5000.00, 'Ingreso inicial', 4),
('CREDIT_CARD', 'SUBTRACT', '5500005555555559', '2026-01-06 20:15:00', 200.00, 'Compra en tienda de electrónica', 4);