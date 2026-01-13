package com.speakly.bank_backend.domain.service.impl;

import com.speakly.bank_backend.domain.model.BankTransaction;
import com.speakly.bank_backend.domain.model.TransactionOrigin;
import com.speakly.bank_backend.domain.repository.BankTransactionRepository;
import com.speakly.bank_backend.domain.service.BankTransactionService;
import com.speakly.bank_backend.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class BankTransactionServiceImpl implements BankTransactionService {

    private final BankTransactionRepository bankTransactionRepository;

    public BankTransactionServiceImpl(BankTransactionRepository bankTransactionRepository) {
        this.bankTransactionRepository = bankTransactionRepository;
    }

    @Override
    public Optional<BankTransaction> getById(Long id) {
        return bankTransactionRepository.findById(id);
    }

    @Override
    public List<BankTransaction> getAll() {
        return bankTransactionRepository.findAll();
    }

    @Override
    public List<BankTransaction> getAllByBankAccountId(Long bankAccountId) {
        return bankTransactionRepository.findByBankAccountId(bankAccountId);
    }

    @Override
    @Transactional
    public BankTransaction create(BankTransaction transaction) {
        validateTransaction(transaction);
        setTransactionDateIfNull(transaction);
        return bankTransactionRepository.save(transaction);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        verifyTransactionExists(id);
        bankTransactionRepository.deleteById(id);
    }

    private void validateTransaction(BankTransaction transaction) {
        if (transaction.getOrigin() == null) {
            throw new IllegalArgumentException("Transaction origin is required");
        }
        if (transaction.getType() == null) {
            throw new IllegalArgumentException("Transaction type is required");
        }
        if (transaction.getAmount() == null || transaction.getAmount().signum() <= 0) {
            throw new IllegalArgumentException("Transaction amount must be positive");
        }
        if (transaction.getOrigin() == TransactionOrigin.CREDIT_CARD && transaction.getOrginCreditCard() == null) {
            throw new IllegalArgumentException("Credit card is required for CREDIT_CARD transactions");
        }
    }

    private void setTransactionDateIfNull(BankTransaction transaction) {
        if (transaction.getTransactionDate() == null) {
            transaction.setTransactionDate(LocalDateTime.now());
        }
    }

    private void verifyTransactionExists(Long id) {
        if (bankTransactionRepository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Transaction not found with id: " + id);
        }
    }
}

