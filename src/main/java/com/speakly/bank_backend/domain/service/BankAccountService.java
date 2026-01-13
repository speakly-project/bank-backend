package com.speakly.bank_backend.domain.service;

import com.speakly.bank_backend.domain.model.BankAccount;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface BankAccountService {
    Optional<BankAccount> getById(Long id);
    BankAccount getByIban(String iban);
    List<BankAccount> getAll();
    List<BankAccount> getAllByClientId(Long id);
    BankAccount create(BankAccount account);
    BankAccount update(BankAccount account);
    void deleteById(Long id);
    BankAccount addBalance(Long accountId, BigDecimal amount);
    BankAccount subtractBalance(Long accountId, BigDecimal amount);
}

