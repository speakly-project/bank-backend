package com.speakly.bank_backend.persistence.dao;

import com.speakly.bank_backend.domain.model.TransactionOrigin;
import com.speakly.bank_backend.domain.model.TransactionType;
import com.speakly.bank_backend.persistence.dao.impl.entity.BankTransactionJpaEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BankTransactionDao {
    Optional<BankTransactionJpaEntity> findById(Long id);
    List<BankTransactionJpaEntity> findByBankAccountId(Long bankAccountId);
    List<BankTransactionJpaEntity> findByCreditCardNumber(String cardNumber);
    List<BankTransactionJpaEntity> findByOrigin(TransactionOrigin origin);
    List<BankTransactionJpaEntity> findByType(TransactionType type);
    List<BankTransactionJpaEntity> findByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    List<BankTransactionJpaEntity> findByBankAccountIdAndDateRange(Long bankAccountId, LocalDateTime startDate, LocalDateTime endDate);
    List<BankTransactionJpaEntity> findAll();
    BankTransactionJpaEntity save(BankTransactionJpaEntity transaction);
    void deleteById(Long id);
    long count();
}

