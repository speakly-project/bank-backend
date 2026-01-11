package com.speakly.bank_backend.persistence.repository;

import com.speakly.bank_backend.domain.model.BankTransaction;
import com.speakly.bank_backend.domain.model.TransactionOrigin;
import com.speakly.bank_backend.domain.model.TransactionType;
import com.speakly.bank_backend.domain.repository.BankTransactionRepository;
import com.speakly.bank_backend.persistence.dao.BankTransactionDao;
import com.speakly.bank_backend.mapper.BankTransactionMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BankTransactionRepositoryImpl implements BankTransactionRepository {

    private final BankTransactionDao bankTransactionDao;

    public BankTransactionRepositoryImpl(BankTransactionDao bankTransactionDao) {
        this.bankTransactionDao = bankTransactionDao;
    }

    @Override
    public Optional<BankTransaction> findById(Long id) {
        return bankTransactionDao.findById(id)
                .map(BankTransactionMapper::fromEntityToDomain);
    }

    @Override
    public List<BankTransaction> findByBankAccountId(Long bankAccountId) {
        return bankTransactionDao.findByBankAccountId(bankAccountId).stream()
                .map(BankTransactionMapper::fromEntityToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<BankTransaction> findByCreditCardNumber(String cardNumber) {
        return bankTransactionDao.findByCreditCardNumber(cardNumber).stream()
                .map(BankTransactionMapper::fromEntityToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<BankTransaction> findByOrigin(TransactionOrigin origin) {
        return bankTransactionDao.findByOrigin(origin).stream()
                .map(BankTransactionMapper::fromEntityToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<BankTransaction> findByType(TransactionType type) {
        return bankTransactionDao.findByType(type).stream()
                .map(BankTransactionMapper::fromEntityToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<BankTransaction> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return bankTransactionDao.findByDateRange(startDate, endDate).stream()
                .map(BankTransactionMapper::fromEntityToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<BankTransaction> findByBankAccountIdAndDateRange(Long bankAccountId, LocalDateTime startDate, LocalDateTime endDate) {
        return bankTransactionDao.findByBankAccountIdAndDateRange(bankAccountId, startDate, endDate).stream()
                .map(BankTransactionMapper::fromEntityToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<BankTransaction> findAll() {
        return bankTransactionDao.findAll().stream()
                .map(BankTransactionMapper::fromEntityToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public BankTransaction save(BankTransaction transaction) {
        return BankTransactionMapper.fromEntityToDomain(
                bankTransactionDao.save(BankTransactionMapper.fromDomainToEntity(transaction))
        );
    }

    @Override
    public void deleteById(Long id) {
        bankTransactionDao.deleteById(id);
    }
}

