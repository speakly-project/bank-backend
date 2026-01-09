package com.speakly.bank_backend.persistence.dao.impl.entity;

import com.speakly.bank_backend.domain.model.TransactionOrigin;
import com.speakly.bank_backend.domain.model.TransactionType;
import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bank_transaction")
public class BankTransactionJpaEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TransactionOrigin origin;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "credit_card_number")
    private CreditCardJpaEntity creditCard;

    @Column(name = "transaction_date")
    private LocalDateTime transactionDate;

    private BigDecimal amount;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_account_id")
    private BankAccountJpaEntity bankAccount;

    public BankTransactionJpaEntity() {}

    public BankTransactionJpaEntity(Long id, TransactionOrigin origin, TransactionType type,
                                   CreditCardJpaEntity creditCard, LocalDateTime transactionDate,
                                   BigDecimal amount, String description, BankAccountJpaEntity bankAccount) {
        this.id = id;
        this.origin = origin;
        this.type = type;
        this.creditCard = creditCard;
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

    public CreditCardJpaEntity getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(CreditCardJpaEntity creditCard) {
        this.creditCard = creditCard;
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

    public BankAccountJpaEntity getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BankAccountJpaEntity bankAccount) {
        this.bankAccount = bankAccount;
    }
}

