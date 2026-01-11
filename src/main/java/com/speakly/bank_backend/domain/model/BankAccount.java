package com.speakly.bank_backend.domain.model;

import java.math.BigDecimal;

public class BankAccount {
    private Long id;
    private String IBAN;
    private BigDecimal balance;
    private Client client;

    public BankAccount(Long id, String IBAN, BigDecimal balance, Client client) {
        this.id = id;
        this.IBAN = IBAN;
        this.balance = balance;
        this.client = client;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIBAN() {
        return IBAN;
    }

    public void setIBAN(String IBAN) {
        this.IBAN = IBAN;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
