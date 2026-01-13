package com.speakly.bank_backend.mapper;

import com.speakly.bank_backend.controller.response.BankTransactionResponse;
import com.speakly.bank_backend.domain.model.BankTransaction;
import com.speakly.bank_backend.persistence.dao.impl.entity.BankTransactionJpaEntity;

public class BankTransactionMapper {

    private static BankTransactionMapper INSTANCE;

    private BankTransactionMapper() {
    }

    public static BankTransactionMapper getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BankTransactionMapper();
        }
        return INSTANCE;
    }

    public static BankTransaction fromEntityToDomain(BankTransactionJpaEntity entity) {
        if (entity == null) {
            return null;
        }
        return new BankTransaction(
                entity.getId(),
                entity.getOrigin(),
                entity.getType(),
                entity.getCreditCard() != null ? CreditCardMapper.fromEntityToDomain(entity.getCreditCard()) : null,
                entity.getTransactionDate(),
                entity.getAmount(),
                entity.getDescription(),
                entity.getBankAccount() != null ? BankAccountMapper.fromEntityToModel(entity.getBankAccount()) : null
        );
    }

    public static BankTransactionJpaEntity fromDomainToEntity(BankTransaction domain) {
        if (domain == null) {
            return null;
        }
        return new BankTransactionJpaEntity(
                domain.getId(),
                domain.getOrigin(),
                domain.getType(),
                domain.getOrginCreditCard() != null ? CreditCardMapper.fromDomainToEntity(domain.getOrginCreditCard()) : null,
                domain.getTransactionDate(),
                domain.getAmount(),
                domain.getDescription(),
                domain.getBankAccount() != null ? BankAccountMapper.fromModelToEntity(domain.getBankAccount()) : null
        );
    }

    public BankTransaction toDomain(BankTransactionJpaEntity entity) {
        return fromEntityToDomain(entity);
    }

    public BankTransactionJpaEntity toEntity(BankTransaction domain) {
        return fromDomainToEntity(domain);
    }

    public static BankTransactionResponse fromTransactionToTransactionResponse(BankTransaction tx) {
        String originCardNumber = tx.getOrginCreditCard() != null ? tx.getOrginCreditCard().getCardNumber() : null;
        return new BankTransactionResponse(
                tx.getId(),
                tx.getOrigin(),
                tx.getType(),
                originCardNumber,
                tx.getTransactionDate(),
                tx.getAmount(),
                tx.getDescription()
        );
    }
}

