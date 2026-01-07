package com.speakly.bank_backend.domain.model;

import java.time.YearMonth;

public class CreditCard {
    private String cardNumber;
    private YearMonth expirationDate;
    private String cvc;
    private String fullName;


    public CreditCard(String cardNumber, YearMonth expirationDate, String cvc, String fullName) {
        this.cardNumber = cardNumber;
        this.expirationDate = expirationDate;
        this.cvc = cvc;
        this.fullName = fullName;
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
        return cvc;
    }

    public void setCvc(String cvc) {
        this.cvc = cvc;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
