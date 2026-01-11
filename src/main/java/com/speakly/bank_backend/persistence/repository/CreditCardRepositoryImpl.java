package com.speakly.bank_backend.persistence.repository;

import com.speakly.bank_backend.domain.model.CreditCard;
import com.speakly.bank_backend.domain.repository.CreditCardRepository;
import com.speakly.bank_backend.persistence.dao.CreditCardDao;
import com.speakly.bank_backend.mapper.CreditCardMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CreditCardRepositoryImpl implements CreditCardRepository {

    private final CreditCardDao creditCardDao;

    public CreditCardRepositoryImpl(CreditCardDao creditCardDao) {
        this.creditCardDao = creditCardDao;
    }

    @Override
    public Optional<CreditCard> findByCardNumber(String cardNumber) {
        return creditCardDao.findByCardNumber(cardNumber)
                .map(CreditCardMapper::fromEntityToDomain);
    }

    @Override
    public List<CreditCard> findByBankAccountId(Long bankAccountId) {
        return creditCardDao.findByBankAccountId(bankAccountId).stream()
                .map(CreditCardMapper::fromEntityToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<CreditCard> findAll() {
        return creditCardDao.findAll().stream()
                .map(CreditCardMapper::fromEntityToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public CreditCard save(CreditCard creditCard) {
        return CreditCardMapper.fromEntityToDomain(
                creditCardDao.save(CreditCardMapper.fromDomainToEntity(creditCard))
        );
    }

    @Override
    public void deleteByCardNumber(String cardNumber) {
        creditCardDao.deleteByCardNumber(cardNumber);
    }

    @Override
    public boolean existsByCardNumber(String cardNumber) {
        return creditCardDao.existsByCardNumber(cardNumber);
    }
}

