package com.speakly.bank_backend.domain.service.impl;

import com.speakly.bank_backend.domain.model.BankAccount;
import com.speakly.bank_backend.domain.repository.BankAccountRepository;
import com.speakly.bank_backend.domain.service.BankAccountService;
import com.speakly.bank_backend.exceptions.BusinessException;
import com.speakly.bank_backend.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class BankAccountServiceImpl implements BankAccountService {

    private final BankAccountRepository bankAccountRepository;

    public BankAccountServiceImpl(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    @Override
    public Optional<BankAccount> getById(Long id) {
        return bankAccountRepository.findById(id);
    }

    @Override
    public BankAccount getByIban(String iban) {
        return bankAccountRepository.findByIban(iban)
                .orElseThrow(() -> new ResourceNotFoundException("Bank account not found with IBAN: " + iban));
    }

    @Override
    public List<BankAccount> getAll() {return bankAccountRepository.findAll();}

    @Override
    public List<BankAccount> getAllByClientId(Long id) {
        return bankAccountRepository.findByClientId(id);
    }

    @Override
    @Transactional
    public BankAccount create(BankAccount account) {
        if (bankAccountRepository.existsByIban(account.getIBAN())) {
            throw new BusinessException("IBAN already exists: " + account.getIBAN());
        }
        validateInitialBalance(account.getBalance());
        return bankAccountRepository.save(account);
    }

    @Override
    @Transactional
    public BankAccount update(BankAccount account) {
        if (account.getId() == null) {
            throw new IllegalArgumentException("Account ID cannot be null for update");
        }
        verifyAccountExists(account.getId());
        return bankAccountRepository.save(account);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        verifyAccountExists(id);
        bankAccountRepository.deleteById(id);
    }

    @Override
    @Transactional
    public BankAccount addBalance(Long accountId, BigDecimal amount) {
        validatePositiveAmount(amount);
        BankAccount account = findAccountById(accountId);
        BigDecimal newBalance = account.getBalance().add(amount);
        account.setBalance(newBalance);
        return bankAccountRepository.save(account);
    }

    @Override
    @Transactional
    public BankAccount subtractBalance(Long accountId, BigDecimal amount) {
        validatePositiveAmount(amount);
        BankAccount account = findAccountById(accountId);
        validateSufficientBalance(account, amount);
        BigDecimal newBalance = account.getBalance().subtract(amount);
        account.setBalance(newBalance);
        return bankAccountRepository.save(account);
    }


    private void validateInitialBalance(BigDecimal balance) {
        if (balance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative");
        }
    }

    private void validatePositiveAmount(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
    }

    private void validateSufficientBalance(BankAccount account, BigDecimal amount) {
        if (account.getBalance().compareTo(amount) < 0) {
            throw new BusinessException("Insufficient balance");
        }
    }

    private void verifyAccountExists(Long id) {
        if (bankAccountRepository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Bank account not found with id: " + id);
        }
    }

    private BankAccount findAccountById(Long id) {
        return bankAccountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bank account not found with id: " + id));
    }
}

