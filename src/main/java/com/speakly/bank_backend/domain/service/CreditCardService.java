package com.speakly.bank_backend.domain.service;

import com.speakly.bank_backend.domain.model.CreditCard;

import java.util.List;
import java.util.Optional;

public interface CreditCardService {
    Optional<CreditCard> getByCardNumber(String cardNumber);
    List<CreditCard> getAll();
    List<CreditCard> getAllByBankAccountId(Long id);
    CreditCard create(CreditCard creditCard);
    CreditCard update(CreditCard creditCard);
    void deleteByCardNumber(String cardNumber);
}

