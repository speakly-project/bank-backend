package com.speakly.bank_backend.persistence.dao;

import com.speakly.bank_backend.persistence.dao.impl.entity.CreditCardJpaEntity;

import java.util.List;
import java.util.Optional;

public interface CreditCardDao {
    Optional<CreditCardJpaEntity> findByCardNumber(String cardNumber);
    List<CreditCardJpaEntity> findByBankAccountId(Long bankAccountId);
    List<CreditCardJpaEntity> findAll();
    CreditCardJpaEntity save(CreditCardJpaEntity creditCard);
    void deleteByCardNumber(String cardNumber);
    boolean existsByCardNumber(String cardNumber);
    long count();
}

