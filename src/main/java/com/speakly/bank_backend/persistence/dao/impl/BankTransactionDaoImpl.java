package com.speakly.bank_backend.persistence.dao.impl;

import com.speakly.bank_backend.domain.model.TransactionOrigin;
import com.speakly.bank_backend.domain.model.TransactionType;
import com.speakly.bank_backend.persistence.dao.BankTransactionDao;
import com.speakly.bank_backend.persistence.dao.impl.entity.BankTransactionJpaEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class BankTransactionDaoImpl implements BankTransactionDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<BankTransactionJpaEntity> findById(Long id) {
        return Optional.ofNullable(entityManager.find(BankTransactionJpaEntity.class, id));
    }

    @Override
    public List<BankTransactionJpaEntity> findByBankAccountId(Long bankAccountId) {
        String sql = "SELECT t FROM BankTransactionJpaEntity t WHERE t.bankAccount.id = :bankAccountId ORDER BY t.transactionDate DESC";
        TypedQuery<BankTransactionJpaEntity> query = entityManager.createQuery(sql, BankTransactionJpaEntity.class)
                .setParameter("bankAccountId", bankAccountId);
        return query.getResultList();
    }

    @Override
    public List<BankTransactionJpaEntity> findByCreditCardNumber(String cardNumber) {
        String sql = "SELECT t FROM BankTransactionJpaEntity t WHERE t.creditCard.card_number = :cardNumber ORDER BY t.transactionDate DESC";
        TypedQuery<BankTransactionJpaEntity> query = entityManager.createQuery(sql, BankTransactionJpaEntity.class)
                .setParameter("cardNumber", cardNumber);
        return query.getResultList();
    }

    @Override
    public List<BankTransactionJpaEntity> findByOrigin(TransactionOrigin origin) {
        String sql = "SELECT t FROM BankTransactionJpaEntity t WHERE t.origin = :origin ORDER BY t.transactionDate DESC";
        TypedQuery<BankTransactionJpaEntity> query = entityManager.createQuery(sql, BankTransactionJpaEntity.class)
                .setParameter("origin", origin);
        return query.getResultList();
    }

    @Override
    public List<BankTransactionJpaEntity> findByType(TransactionType type) {
        String sql = "SELECT t FROM BankTransactionJpaEntity t WHERE t.type = :type ORDER BY t.transactionDate DESC";
        TypedQuery<BankTransactionJpaEntity> query = entityManager.createQuery(sql, BankTransactionJpaEntity.class)
                .setParameter("type", type);
        return query.getResultList();
    }

    @Override
    public List<BankTransactionJpaEntity> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        String sql = "SELECT t FROM BankTransactionJpaEntity t WHERE t.transactionDate BETWEEN :startDate AND :endDate ORDER BY t.transactionDate DESC";
        TypedQuery<BankTransactionJpaEntity> query = entityManager.createQuery(sql, BankTransactionJpaEntity.class)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate);
        return query.getResultList();
    }

    @Override
    public List<BankTransactionJpaEntity> findByBankAccountIdAndDateRange(Long bankAccountId, LocalDateTime startDate, LocalDateTime endDate) {
        String sql = "SELECT t FROM BankTransactionJpaEntity t WHERE t.bankAccount.id = :bankAccountId AND t.transactionDate BETWEEN :startDate AND :endDate ORDER BY t.transactionDate DESC";
        TypedQuery<BankTransactionJpaEntity> query = entityManager.createQuery(sql, BankTransactionJpaEntity.class)
                .setParameter("bankAccountId", bankAccountId)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate);
        return query.getResultList();
    }

    @Override
    public List<BankTransactionJpaEntity> findAll() {
        String sql = "SELECT t FROM BankTransactionJpaEntity t ORDER BY t.transactionDate DESC";
        TypedQuery<BankTransactionJpaEntity> query = entityManager.createQuery(sql, BankTransactionJpaEntity.class);
        return query.getResultList();
    }

    @Override
    public BankTransactionJpaEntity save(BankTransactionJpaEntity transaction) {
        if (transaction.getId() == null) {
            entityManager.persist(transaction);
            return transaction;
        } else {
            return entityManager.merge(transaction);
        }
    }

    @Override
    public void deleteById(Long id) {
        BankTransactionJpaEntity transaction = entityManager.find(BankTransactionJpaEntity.class, id);
        if (transaction != null) {
            entityManager.remove(transaction);
        }
    }

    @Override
    public long count() {
        return entityManager.createQuery("SELECT COUNT(t) FROM BankTransactionJpaEntity t", Long.class)
                .getSingleResult();
    }
}

