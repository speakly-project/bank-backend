package com.speakly.bank_backend.domain.model;

import java.time.YearMonth;

public class CreditCard {
    private Long id;
    private String cardNumber;
    private YearMonth expirationDate;
    private String cvv;
    private String fullName;
    private BankAccount bankAccount;

    public CreditCard(Long id, String cardNumber, YearMonth expirationDate, String cvc, String fullName, BankAccount bankAccount) {
        this.id = id;
        this.cardNumber = cardNumber;
        this.expirationDate = expirationDate;
        this.cvv = cvc;
        this.fullName = fullName;
        this.bankAccount = bankAccount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public YearMonth getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(YearMonth expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getCvc() {
        return cvv;
    }

    public void setCvc(String cvc) {
        this.cvv = cvc;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }
}
