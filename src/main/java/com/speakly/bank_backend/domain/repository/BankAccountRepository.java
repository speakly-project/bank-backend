package com.speakly.bank_backend.domain.repository;

import com.speakly.bank_backend.domain.model.BankAccount;

import java.util.List;
import java.util.Optional;

public interface BankAccountRepository {
    Optional<BankAccount> findById(Long id);
    Optional<BankAccount> findByIban(String iban);
    List<BankAccount> findByClientId(Long clientId);
    List<BankAccount> findAll();
    BankAccount save(BankAccount account);
    void deleteById(Long id);
    boolean existsByIban(String iban);
}

