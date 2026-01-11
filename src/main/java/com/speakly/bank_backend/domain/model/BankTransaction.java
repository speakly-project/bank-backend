package com.speakly.bank_backend.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BankTransaction {
    private Long id;
    private TransactionOrigin origin;
    private TransactionType type;
    private CreditCard orginCreditCard;
    private LocalDateTime transactionDate;
    private BigDecimal amount;
    private String description;
    private BankAccount bankAccount;

    public BankTransaction(Long id, TransactionOrigin origin, TransactionType type, CreditCard orginCreditCard,
                          LocalDateTime transactionDate, BigDecimal amount, String description, BankAccount bankAccount) {
        this.id = id;
        this.origin = origin;
        this.type = type;
        this.orginCreditCard = orginCreditCard;
        this.transactionDate = transactionDate;
        this.amount = amount;
        this.description = description;
        this.bankAccount = bankAccount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TransactionOrigin getOrigin() {
        return origin;
    }

    public void setOrigin(TransactionOrigin origin) {
        this.origin = origin;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public CreditCard getOrginCreditCard() {
        return orginCreditCard;
    }

    public void setOrginCreditCard(CreditCard orginCreditCard) {
        this.orginCreditCard = orginCreditCard;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }
}
