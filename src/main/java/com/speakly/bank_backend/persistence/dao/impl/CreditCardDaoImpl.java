package com.speakly.bank_backend.persistence.dao.impl;

import com.speakly.bank_backend.persistence.dao.CreditCardDao;
import com.speakly.bank_backend.persistence.dao.impl.entity.CreditCardJpaEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;

public class CreditCardDaoImpl implements CreditCardDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<CreditCardJpaEntity> findByCardNumber(String cardNumber) {
        return Optional.ofNullable(entityManager.find(CreditCardJpaEntity.class, cardNumber));
    }

    @Override
    public List<CreditCardJpaEntity> findByBankAccountId(Long bankAccountId) {
        String sql = "SELECT c FROM CreditCardJpaEntity c WHERE c.bankAccount.id = :bankAccountId";
        TypedQuery<CreditCardJpaEntity> query = entityManager.createQuery(sql, CreditCardJpaEntity.class)
                .setParameter("bankAccountId", bankAccountId);
        return query.getResultList();
    }

    @Override
    public List<CreditCardJpaEntity> findAll() {
        String sql = "SELECT c FROM CreditCardJpaEntity c ORDER BY c.card_number";
        TypedQuery<CreditCardJpaEntity> query = entityManager.createQuery(sql, CreditCardJpaEntity.class);
        return query.getResultList();
    }

    @Override
    public CreditCardJpaEntity save(CreditCardJpaEntity creditCard) {
        if (creditCard.getCard_number() == null || findByCardNumber(creditCard.getCard_number()).isEmpty()) {
            entityManager.persist(creditCard);
            return creditCard;
        } else {
            return entityManager.merge(creditCard);
        }
    }

    @Override
    public void deleteByCardNumber(String cardNumber) {
        CreditCardJpaEntity creditCard = entityManager.find(CreditCardJpaEntity.class, cardNumber);
        if (creditCard != null) {
            entityManager.remove(creditCard);
        }
    }

    @Override
    public boolean existsByCardNumber(String cardNumber) {
        String sql = "SELECT COUNT(c) FROM CreditCardJpaEntity c WHERE c.card_number = :cardNumber";
        Long count = entityManager.createQuery(sql, Long.class)
                .setParameter("cardNumber", cardNumber)
                .getSingleResult();
        return count > 0;
    }

    @Override
    public long count() {
        return entityManager.createQuery("SELECT COUNT(c) FROM CreditCardJpaEntity c", Long.class)
                .getSingleResult();
    }
}
