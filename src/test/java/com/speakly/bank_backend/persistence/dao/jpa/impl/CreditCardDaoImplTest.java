package com.speakly.bank_backend.persistence.dao.jpa.impl;

import com.speakly.bank_backend.persistence.dao.impl.CreditCardDaoImpl;
import com.speakly.bank_backend.persistence.dao.impl.entity.BankAccountJpaEntity;
import com.speakly.bank_backend.persistence.dao.impl.entity.ClientJpaEntity;
import com.speakly.bank_backend.persistence.dao.impl.entity.CreditCardJpaEntity;
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
@Import(CreditCardDaoImpl.class)
class CreditCardDaoImplTest {

    @Autowired
    private CreditCardDaoImpl creditCardDao;

    @PersistenceContext
    private EntityManager entityManager;

    private ClientJpaEntity client;
    private BankAccountJpaEntity account;
    private CreditCardJpaEntity card1;
    private CreditCardJpaEntity card2;

    @BeforeEach
    void setUp() {
        String suffix = UUID.randomUUID().toString().substring(0, 8);

        client = new ClientJpaEntity(null, "user_" + suffix, "pass", "Juan", "Perez", "Garcia", "1111111" + suffix.charAt(0) + "A", "apikey_" + suffix);
        entityManager.persist(client);

        account = new BankAccountJpaEntity(null, "ES00" + suffix + "000000000000000000", new BigDecimal("10.00"), client);
        entityManager.persist(account);

        card1 = new CreditCardJpaEntity("4532015112830" + suffix.substring(0, 3), "2027-12", "123", "Juan Perez", account);
        card2 = new CreditCardJpaEntity("4532015112830" + suffix.substring(3, 6), "2028-01", "321", "Ana Lopez", account);

        entityManager.persist(card1);
        entityManager.persist(card2);
        entityManager.flush();
    }

    @Test
    void findByCardNumber_existing_returnsCard() {
        Optional<CreditCardJpaEntity> result = creditCardDao.findByCardNumber(card1.getCard_number());
        assertTrue(result.isPresent());
        assertEquals(card1.getCard_number(), result.get().getCard_number());
    }

    @Test
    void findByCardNumber_notFound_returnsEmpty() {
        Optional<CreditCardJpaEntity> result = creditCardDao.findByCardNumber("NO_" + UUID.randomUUID());
        assertTrue(result.isEmpty());
    }

    @Test
    void findByBankAccountId_returnsCards() {
        List<CreditCardJpaEntity> result = creditCardDao.findByBankAccountId(account.getId());
        assertNotNull(result);
        assertTrue(result.size() >= 2);
    }

    @Test
    void save_whenNew_shouldPersist() {
        String suffix = UUID.randomUUID().toString().substring(0, 6);
        CreditCardJpaEntity toSave = new CreditCardJpaEntity("499999999999" + suffix, "2029-01", "999", "New User", account);

        CreditCardJpaEntity saved = creditCardDao.save(toSave);
        entityManager.flush();

        assertEquals(toSave.getCard_number(), saved.getCard_number());
        assertNotNull(entityManager.find(CreditCardJpaEntity.class, toSave.getCard_number()));
    }

    @Test
    void save_whenExists_shouldMerge() {
        card1.setFull_name("UPDATED");
        CreditCardJpaEntity updated = creditCardDao.save(card1);
        entityManager.flush();

        CreditCardJpaEntity fromDb = entityManager.find(CreditCardJpaEntity.class, card1.getCard_number());
        assertEquals(updated.getFull_name(), fromDb.getFull_name());
    }

    @Test
    void findAll_returnsList() {
        List<CreditCardJpaEntity> all = creditCardDao.findAll();
        assertNotNull(all);
        assertTrue(all.size() >= 2);
    }

    @Test
    void existsByCardNumber_works() {
        assertTrue(creditCardDao.existsByCardNumber(card1.getCard_number()));
        assertFalse(creditCardDao.existsByCardNumber("NO_" + UUID.randomUUID()));
    }

    @Test
    void deleteByCardNumber_removesEntity() {
        creditCardDao.deleteByCardNumber(card2.getCard_number());
        entityManager.flush();

        assertNull(entityManager.find(CreditCardJpaEntity.class, card2.getCard_number()));
    }

    @Test
    void count_returnsCorrectValue() {
        long count = creditCardDao.count();
        assertTrue(count >= 2);
    }
}

