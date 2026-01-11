package com.speakly.bank_backend.domain.service.impl;

import com.speakly.bank_backend.domain.model.CreditCard;
import com.speakly.bank_backend.domain.repository.CreditCardRepository;
import com.speakly.bank_backend.domain.service.CreditCardService;
import com.speakly.bank_backend.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

public class CreditCardServiceImpl implements CreditCardService {

    private final CreditCardRepository creditCardRepository;

    public CreditCardServiceImpl(CreditCardRepository creditCardRepository) {
        this.creditCardRepository = creditCardRepository;
    }

    @Override
    public Optional<CreditCard> getByCardNumber(String cardNumber) {
        return creditCardRepository.findByCardNumber(cardNumber);
    }

    @Override
    public List<CreditCard> getAll() {
        return creditCardRepository.findAll();
    }

    @Override
    @Transactional
    public CreditCard create(CreditCard creditCard) {
        if (creditCardRepository.existsByCardNumber(creditCard.getCardNumber())) {
            throw new IllegalArgumentException("Card number already exists: " + creditCard.getCardNumber());
        }
        validateNotExpired(creditCard);
        return creditCardRepository.save(creditCard);
    }

    @Override
    @Transactional
    public CreditCard update(CreditCard creditCard) {
        verifyCreditCardExists(creditCard.getCardNumber());
        return creditCardRepository.save(creditCard);
    }

    @Override
    @Transactional
    public void deleteByCardNumber(String cardNumber) {
        verifyCreditCardExists(cardNumber);
        creditCardRepository.deleteByCardNumber(cardNumber);
    }

    private void validateNotExpired(CreditCard creditCard) {
        YearMonth currentMonth = YearMonth.now();
        if (creditCard.getExpirationDate().isBefore(currentMonth)) {
            throw new IllegalArgumentException("Credit card is expired");
        }
    }

    private void verifyCreditCardExists(String cardNumber) {
        if (creditCardRepository.findByCardNumber(cardNumber).isEmpty()) {
            throw new ResourceNotFoundException("Credit card not found with number: " + cardNumber);
        }
    }
}

