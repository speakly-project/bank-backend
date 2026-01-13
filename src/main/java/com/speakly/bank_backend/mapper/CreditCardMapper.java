package com.speakly.bank_backend.mapper;

import com.speakly.bank_backend.controller.response.CreditCardResponse;
import com.speakly.bank_backend.domain.model.CreditCard;
import com.speakly.bank_backend.persistence.dao.impl.entity.CreditCardJpaEntity;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class CreditCardMapper {

    private static CreditCardMapper INSTANCE;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");

    private CreditCardMapper() {
    }

    public static CreditCardMapper getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CreditCardMapper();
        }
        return INSTANCE;
    }

    public static CreditCard fromEntityToDomain(CreditCardJpaEntity entity) {
        if (entity == null) {
            return null;
        }
        YearMonth expirationDate = YearMonth.parse(entity.getExpiration_date(), FORMATTER);
        return new CreditCard(
                null,
                entity.getCard_number(),
                expirationDate,
                entity.getCvv(),
                entity.getFull_name(),
                entity.getBankAccount() != null ? BankAccountMapper.fromEntityToModel(entity.getBankAccount()) : null
        );
    }

    public static CreditCardJpaEntity fromDomainToEntity(CreditCard domain) {
        if (domain == null) {
            return null;
        }
        String expirationDateStr = domain.getExpirationDate().format(FORMATTER);
        return new CreditCardJpaEntity(
                domain.getCardNumber(),
                expirationDateStr,
                domain.getCvc(),
                domain.getFullName(),
                domain.getBankAccount() != null ? BankAccountMapper.fromModelToEntity(domain.getBankAccount()) : null
        );
    }

    public CreditCard toDomain(CreditCardJpaEntity entity) {
        return fromEntityToDomain(entity);
    }

    public CreditCardJpaEntity toEntity(CreditCard domain) {
        return fromDomainToEntity(domain);
    }

    public static CreditCardResponse fromCardToCardResponse(CreditCard card) {
        return new CreditCardResponse(
                card.getCardNumber(),
                card.getExpirationDate(),
                card.getCvc(),
                card.getFullName()
        );
    }
}
