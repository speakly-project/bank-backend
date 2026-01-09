package com.speakly.bank_backend.persistence.dao.impl.entity;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "credit_card")
public class CreditCardJpaEntity implements Serializable {

    @Id
    private String card_number;

    private String expiration_date;

    private String cvc;

    private String full_name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_account_id")
    private BankAccountJpaEntity bank_account;

    public CreditCardJpaEntity() {}

    public CreditCardJpaEntity(String cardNumber, String expirationDate, String cvc,
                              String fullName, BankAccountJpaEntity bankAccount) {
        this.card_number = cardNumber;
        this.expiration_date = expirationDate;
        this.cvc = cvc;
        this.full_name = fullName;
        this.bank_account = bankAccount;
    }

    public String getCard_number() {
        return card_number;
    }

    public void setCard_number(String card_number) {
        this.card_number = card_number;
    }

    public String getExpiration_date() {
        return expiration_date;
    }

    public void setExpiration_date(String expiration_date) {
        this.expiration_date = expiration_date;
    }

    public String getCvc() {
        return cvc;
    }

    public void setCvc(String cvc) {
        this.cvc = cvc;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public BankAccountJpaEntity getBank_account() {
        return bank_account;
    }

    public void setBank_account(BankAccountJpaEntity bank_account) {
        this.bank_account = bank_account;
    }
}

