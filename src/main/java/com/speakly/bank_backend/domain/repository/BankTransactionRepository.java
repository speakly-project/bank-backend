package com.speakly.bank_backend.domain.repository;

import com.speakly.bank_backend.domain.model.BankTransaction;
import com.speakly.bank_backend.domain.model.TransactionOrigin;
import com.speakly.bank_backend.domain.model.TransactionType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BankTransactionRepository {
    Optional<BankTransaction> findById(Long id);
    List<BankTransaction> findByBankAccountId(Long bankAccountId);
    List<BankTransaction> findByCreditCardNumber(String cardNumber);
    List<BankTransaction> findByOrigin(TransactionOrigin origin);
    List<BankTransaction> findByType(TransactionType type);
    List<BankTransaction> findByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    List<BankTransaction> findByBankAccountIdAndDateRange(Long bankAccountId, LocalDateTime startDate, LocalDateTime endDate);
    List<BankTransaction> findAll();
    BankTransaction save(BankTransaction transaction);
    void deleteById(Long id);
}

