package com.speakly.bank_backend.persistence.dao.jpa.impl;

import com.speakly.bank_backend.domain.model.TransactionOrigin;
import com.speakly.bank_backend.domain.model.TransactionType;
import com.speakly.bank_backend.persistence.dao.impl.BankTransactionDaoImpl;
import com.speakly.bank_backend.persistence.dao.impl.entity.BankAccountJpaEntity;
import com.speakly.bank_backend.persistence.dao.impl.entity.BankTransactionJpaEntity;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(BankTransactionDaoImpl.class)
class BankTransactionDaoImplTest {

    @Autowired
    private BankTransactionDaoImpl bankTransactionDao;

    @PersistenceContext
    private EntityManager entityManager;

    private BankAccountJpaEntity account;
    private CreditCardJpaEntity card;
    private BankTransactionJpaEntity tx1;
    private BankTransactionJpaEntity tx2;

    @BeforeEach
    void setUp() {
        String suffix = UUID.randomUUID().toString().substring(0, 8);

        ClientJpaEntity client = new ClientJpaEntity(null, "user_" + suffix, "pass", "Juan", "Perez", "Garcia", "1111111" + suffix.charAt(0) + "A", "apikey_" + suffix);
        entityManager.persist(client);

        account = new BankAccountJpaEntity(null, "ES00" + suffix + "000000000000000000", new BigDecimal("10.00"), client);
        entityManager.persist(account);

        card = new CreditCardJpaEntity("4532015112830" + suffix.substring(0, 3), "2027-12", "123", "Juan Perez", account);
        entityManager.persist(card);

        tx1 = new BankTransactionJpaEntity(null, TransactionOrigin.TRANSFER, TransactionType.ADD, null,
                LocalDateTime.now().minusDays(1), new BigDecimal("1.00"), "Ingreso", account);
        tx2 = new BankTransactionJpaEntity(null, TransactionOrigin.CREDIT_CARD, TransactionType.SUBTRACT, card,
                LocalDateTime.now(), new BigDecimal("2.00"), "Compra", account);

        entityManager.persist(tx1);
        entityManager.persist(tx2);
        entityManager.flush();
    }

    @Test
    void findById_notFound_returnsEmpty() {
        Optional<BankTransactionJpaEntity> result = bankTransactionDao.findById(999L);
        assertTrue(result.isEmpty());
    }

    @Test
    void findByBankAccountId_returnsOrderedList() {
        List<BankTransactionJpaEntity> result = bankTransactionDao.findByBankAccountId(account.getId());
        assertNotNull(result);
        assertTrue(result.size() >= 2);
    }

    @Test
    void findByCreditCardNumber_returnsList() {
        List<BankTransactionJpaEntity> result = bankTransactionDao.findByCreditCardNumber(card.getCard_number());
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void findByOrigin_returnsList() {
        List<BankTransactionJpaEntity> result = bankTransactionDao.findByOrigin(TransactionOrigin.TRANSFER);
        assertNotNull(result);
        assertTrue(result.stream().anyMatch(t -> t.getOrigin() == TransactionOrigin.TRANSFER));
    }

    @Test
    void findByType_returnsList() {
        List<BankTransactionJpaEntity> result = bankTransactionDao.findByType(TransactionType.SUBTRACT);
        assertNotNull(result);
        assertTrue(result.stream().anyMatch(t -> t.getType() == TransactionType.SUBTRACT));
    }

    @Test
    void findByDateRange_returnsList() {
        LocalDateTime start = LocalDateTime.now().minusDays(2);
        LocalDateTime end = LocalDateTime.now().plusDays(1);

        List<BankTransactionJpaEntity> result = bankTransactionDao.findByDateRange(start, end);
        assertNotNull(result);
        assertTrue(result.size() >= 2);
    }

    @Test
    void findByBankAccountIdAndDateRange_returnsList() {
        LocalDateTime start = LocalDateTime.now().minusDays(2);
        LocalDateTime end = LocalDateTime.now().plusDays(1);

        List<BankTransactionJpaEntity> result = bankTransactionDao.findByBankAccountIdAndDateRange(account.getId(), start, end);
        assertNotNull(result);
        assertTrue(result.size() >= 2);
    }

    @Test
    void findAll_returnsList() {
        List<BankTransactionJpaEntity> all = bankTransactionDao.findAll();
        assertNotNull(all);
        assertTrue(all.size() >= 2);
    }

    @Test
    void save_whenIdIsNull_shouldPersist() {
        BankTransactionJpaEntity toSave = new BankTransactionJpaEntity(null, TransactionOrigin.DOM, TransactionType.ADD, null,
                LocalDateTime.now(), new BigDecimal("3.00"), "Recibo", account);

        BankTransactionJpaEntity saved = bankTransactionDao.save(toSave);
        entityManager.flush();

        assertNotNull(saved.getId());
        assertEquals(toSave.getDescription(), saved.getDescription());
    }

    @Test
    void save_whenIdExists_shouldMerge() {
        tx1.setDescription("UPDATED");
        BankTransactionJpaEntity updated = bankTransactionDao.save(tx1);
        entityManager.flush();

        BankTransactionJpaEntity fromDb = entityManager.find(BankTransactionJpaEntity.class, tx1.getId());
        assertEquals(updated.getDescription(), fromDb.getDescription());
    }

    @Test
    void deleteById_removesEntity() {
        bankTransactionDao.deleteById(tx2.getId());
        entityManager.flush();

        assertNull(entityManager.find(BankTransactionJpaEntity.class, tx2.getId()));
    }

    @Test
    void count_returnsCorrectValue() {
        long count = bankTransactionDao.count();
        assertTrue(count >= 2);
    }
}
