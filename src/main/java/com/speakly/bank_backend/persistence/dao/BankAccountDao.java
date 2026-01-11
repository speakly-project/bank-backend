package com.speakly.bank_backend.persistence.dao;

import com.speakly.bank_backend.persistence.dao.impl.entity.BankAccountJpaEntity;

import java.util.List;
import java.util.Optional;

public interface BankAccountDao {
    Optional<BankAccountJpaEntity> findById(Long id);
    Optional<BankAccountJpaEntity> findByIban(String iban);
    List<BankAccountJpaEntity> findByClientId(Long clientId);
    List<BankAccountJpaEntity> findAll();
    BankAccountJpaEntity save(BankAccountJpaEntity account);
    void deleteById(Long id);
    boolean existsByIban(String iban);
    long count();
}

