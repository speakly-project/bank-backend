package com.speakly.bank_backend.mapper;

import com.speakly.bank_backend.domain.model.BankAccount;
import com.speakly.bank_backend.persistence.dao.impl.entity.BankAccountJpaEntity;

public class BankAccountMapper {

    private static BankAccountMapper INSTANCE;

    private BankAccountMapper() {
    }

    public static BankAccountMapper getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BankAccountMapper();
        }
        return INSTANCE;
    }

    public static BankAccount fromEntityToModel(BankAccountJpaEntity entity) {
        if (entity == null) {
            return null;
        }
        return new BankAccount(
                entity.getId(),
                entity.getIban(),
                entity.getBalance(),
                entity.getClient() != null ? ClientMapper.fromEntityToDomain(entity.getClient()) : null
        );
    }

    public static BankAccountJpaEntity fromModelToEntity(BankAccount model) {
        if (model == null) {
            return null;
        }
        return new BankAccountJpaEntity(
                model.getId(),
                model.getIBAN(),
                model.getBalance(),
                model.getClient() != null ? ClientMapper.fromDomainToEntity(model.getClient()) : null
        );
    }

    public BankAccount toDomain(BankAccountJpaEntity entity) {
        return fromEntityToModel(entity);
    }

    public BankAccountJpaEntity toEntity(BankAccount domain) {
        return fromModelToEntity(domain);
    }
}

