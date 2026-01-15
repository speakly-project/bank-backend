package com.speakly.bank_backend.persistence.dao.jpa.impl;

import com.speakly.bank_backend.persistence.dao.impl.BankAccountDaoImpl;
import com.speakly.bank_backend.persistence.dao.impl.entity.BankAccountJpaEntity;
import com.speakly.bank_backend.persistence.dao.impl.entity.ClientJpaEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(BankAccountDaoImpl.class)
class BankAccountDaoImplTest {

    @Autowired
    private BankAccountDaoImpl bankAccountDao;

    @PersistenceContext
    private EntityManager entityManager;

    private ClientJpaEntity client;
    private BankAccountJpaEntity a1;
    private BankAccountJpaEntity a2;

    @BeforeEach
    void setUp() {
        String suffix = UUID.randomUUID().toString().substring(0, 8);

        client = new ClientJpaEntity(null, "user_" + suffix, "pass", "Juan", "Perez", "Garcia", "1111111" + suffix.charAt(0) + "A", "apikey_" + suffix);
        entityManager.persist(client);

        a1 = new BankAccountJpaEntity(null, "ES00" + suffix + "000000000000000000", new BigDecimal("10.00"), client);
        a2 = new BankAccountJpaEntity(null, "ES99" + suffix + "000000000000000000", new BigDecimal("20.00"), client);

        entityManager.persist(a1);
        entityManager.persist(a2);
        entityManager.flush();
    }

    @Test
    void findById_notFound_returnsEmpty() {
        Optional<BankAccountJpaEntity> result = bankAccountDao.findById(999L);
        assertTrue(result.isEmpty());
    }

    @Test
    void findByIban_existing_returnsAccount() {
        Optional<BankAccountJpaEntity> result = bankAccountDao.findByIban(a1.getIban());
        assertTrue(result.isPresent());
        assertEquals(a1.getIban(), result.get().getIban());
    }

    @Test
    void findByClientId_returnsAccounts() {
        List<BankAccountJpaEntity> result = bankAccountDao.findByClientId(client.getId());
        assertNotNull(result);
        assertTrue(result.size() >= 2);
    }

    @Test
    void save_whenIdIsNull_shouldPersist() {
        BankAccountJpaEntity toSave = new BankAccountJpaEntity(null, "ES11" + UUID.randomUUID().toString().substring(0, 8) + "000000000000000000", new BigDecimal("1.00"), client);
        BankAccountJpaEntity saved = bankAccountDao.save(toSave);
        entityManager.flush();

        assertNotNull(saved.getId());
        assertEquals(toSave.getIban(), saved.getIban());
    }

    @Test
    void save_whenIdExists_shouldMerge() {
        a1.setBalance(new BigDecimal("999.00"));
        BankAccountJpaEntity updated = bankAccountDao.save(a1);
        entityManager.flush();

        BankAccountJpaEntity fromDb = entityManager.find(BankAccountJpaEntity.class, a1.getId());
        assertEquals(updated.getBalance(), fromDb.getBalance());
    }

    @Test
    void findAll_shouldReturnList() {
        List<BankAccountJpaEntity> all = bankAccountDao.findAll();
        assertNotNull(all);
        assertTrue(all.size() >= 2);
    }

    @Test
    void existsByIban_returnsTrueWhenExists() {
        assertTrue(bankAccountDao.existsByIban(a1.getIban()));
        assertFalse(bankAccountDao.existsByIban("NO_IBAN_" + UUID.randomUUID()));
    }

    @Test
    void deleteById_removesEntity() {
        bankAccountDao.deleteById(a2.getId());
        entityManager.flush();

        assertNull(entityManager.find(BankAccountJpaEntity.class, a2.getId()));
    }

    @Test
    void count_returnsCorrectValue() {
        long count = bankAccountDao.count();
        assertTrue(count >= 2);
    }
}

