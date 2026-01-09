package com.speakly.bank_backend.persistence.dao.impl.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bank_account")
public class BankAccountJpaEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String iban;

    private BigDecimal balance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private ClientJpaEntity client;

    @OneToMany(mappedBy = "bankAccount", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CreditCardJpaEntity> creditCards = new ArrayList<>();

    @OneToMany(mappedBy = "bankAccount", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BankTransactionJpaEntity> transactions = new ArrayList<>();

    public BankAccountJpaEntity() {}

    public BankAccountJpaEntity(Long id, String iban, BigDecimal balance, ClientJpaEntity client) {
        this.id = id;
        this.iban = iban;
        this.balance = balance;
        this.client = client;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public ClientJpaEntity getClient() {
        return client;
    }

    public void setClient(ClientJpaEntity client) {
        this.client = client;
    }

    public List<CreditCardJpaEntity> getCreditCards() {
        return creditCards;
    }

    public void setCreditCards(List<CreditCardJpaEntity> creditCards) {
        this.creditCards.clear();
        if (creditCards != null) {
            this.creditCards.addAll(creditCards);
        }
    }

    public List<BankTransactionJpaEntity> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<BankTransactionJpaEntity> transactions) {
        this.transactions.clear();
        if (transactions != null) {
            this.transactions.addAll(transactions);
        }
    }
}

