package com.speakly.bank_backend.persistence.dao.impl;

import com.speakly.bank_backend.persistence.dao.BankAccountDao;
import com.speakly.bank_backend.persistence.dao.impl.entity.BankAccountJpaEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;

public class BankAccountDaoImpl implements BankAccountDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<BankAccountJpaEntity> findById(Long id) {
        return Optional.ofNullable(entityManager.find(BankAccountJpaEntity.class, id));
    }

    @Override
    public Optional<BankAccountJpaEntity> findByIban(String iban) {
        try {
            String sql = "SELECT b FROM BankAccountJpaEntity b WHERE b.iban = :iban";
            BankAccountJpaEntity result = entityManager.createQuery(sql, BankAccountJpaEntity.class)
                    .setParameter("iban", iban)
                    .getSingleResult();
            return Optional.of(result);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<BankAccountJpaEntity> findByClientId(Long clientId) {
        String sql = "SELECT b FROM BankAccountJpaEntity b WHERE b.client.id = :clientId";
        TypedQuery<BankAccountJpaEntity> query = entityManager.createQuery(sql, BankAccountJpaEntity.class)
                .setParameter("clientId", clientId);
        return query.getResultList();
    }

    @Override
    public List<BankAccountJpaEntity> findAll() {
        String sql = "SELECT b FROM BankAccountJpaEntity b ORDER BY b.id";
        TypedQuery<BankAccountJpaEntity> query = entityManager.createQuery(sql, BankAccountJpaEntity.class);
        return query.getResultList();
    }

    @Override
    public BankAccountJpaEntity save(BankAccountJpaEntity account) {
        if (account.getId() == null) {
            entityManager.persist(account);
            return account;
        } else {
            return entityManager.merge(account);
        }
    }

    @Override
    public void deleteById(Long id) {
        BankAccountJpaEntity account = entityManager.find(BankAccountJpaEntity.class, id);
        if (account != null) {
            entityManager.remove(account);
        }
    }

    @Override
    public boolean existsByIban(String iban) {
        String sql = "SELECT COUNT(b) FROM BankAccountJpaEntity b WHERE b.iban = :iban";
        Long count = entityManager.createQuery(sql, Long.class)
                .setParameter("iban", iban)
                .getSingleResult();
        return count > 0;
    }

    @Override
    public long count() {
        return entityManager.createQuery("SELECT COUNT(b) FROM BankAccountJpaEntity b", Long.class)
                .getSingleResult();
    }
}

