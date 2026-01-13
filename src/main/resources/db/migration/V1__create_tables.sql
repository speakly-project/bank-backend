-- Tabla de clientes (un cliente puede tener muchas cuentas bancarias)
CREATE TABLE client (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(100) NOT NULL,
    first_surname VARCHAR(100) NOT NULL,
    second_surname VARCHAR(100),
    dni VARCHAR(20) NOT NULL UNIQUE,
    api_key VARCHAR(255)
);

-- Tabla de cuentas bancarias (pertenece a un cliente, puede tener muchas tarjetas de crédito)
CREATE TABLE bank_account (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    iban VARCHAR(34) NOT NULL UNIQUE,
    balance DECIMAL(30, 2) NOT NULL DEFAULT 0.00,
    client_id BIGINT NOT NULL,
    FOREIGN KEY (client_id) REFERENCES client(id) ON DELETE CASCADE
);

-- Tabla de tarjetas de crédito (pertenece a una cuenta bancaria)
CREATE TABLE credit_card (
    card_number VARCHAR(20) PRIMARY KEY,
    expiration_date VARCHAR(7) NOT NULL,
    cvv VARCHAR(4) NOT NULL,
    full_name VARCHAR(200) NOT NULL,
    bank_account_id BIGINT NOT NULL,
    FOREIGN KEY (bank_account_id) REFERENCES bank_account(id) ON DELETE RESTRICT
);

-- Tabla de transacciones bancarias
CREATE TABLE bank_transaction (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    origin VARCHAR(50) NOT NULL,
    type VARCHAR(50) NOT NULL,
    credit_card_number VARCHAR(20),
    transaction_date TIMESTAMP NOT NULL,
    amount DECIMAL(15, 2) NOT NULL,
    description VARCHAR(500),
    bank_account_id BIGINT NOT NULL,
    FOREIGN KEY (credit_card_number) REFERENCES credit_card(card_number) ON DELETE SET NULL,
    FOREIGN KEY (bank_account_id) REFERENCES bank_account(id) ON DELETE CASCADE
);