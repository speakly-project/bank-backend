package com.speakly.bank_backend.domain.service;

import com.speakly.bank_backend.domain.model.BankTransaction;

import java.util.List;
import java.util.Optional;

public interface BankTransactionService {
    Optional<BankTransaction> getById(Long id);
    List<BankTransaction> getAll();
    List<BankTransaction> getAllByBankAccountId(Long bankAccountId);
    BankTransaction create(BankTransaction transaction);
    void deleteById(Long id);
}

