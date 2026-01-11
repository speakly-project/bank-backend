package com.speakly.bank_backend.domain.repository;

import com.speakly.bank_backend.domain.model.CreditCard;

import java.util.List;
import java.util.Optional;

public interface CreditCardRepository {
    Optional<CreditCard> findByCardNumber(String cardNumber);
    List<CreditCard> findByBankAccountId(Long bankAccountId);
    List<CreditCard> findAll();
    CreditCard save(CreditCard creditCard);
    void deleteByCardNumber(String cardNumber);
    boolean existsByCardNumber(String cardNumber);
}

