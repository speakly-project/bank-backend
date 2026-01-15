package com.speakly.bank_backend.persistence.dao.jpa.impl;

import com.speakly.bank_backend.persistence.dao.impl.ClientDaoImpl;
import com.speakly.bank_backend.persistence.dao.impl.entity.ClientJpaEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(ClientDaoImpl.class)
class ClientDaoImplTest {

    @Autowired
    private ClientDaoImpl clientDao;

    @PersistenceContext
    private EntityManager entityManager;

    private ClientJpaEntity c1;
    private ClientJpaEntity c2;

    @BeforeEach
    void setUp() {
        String suffix = UUID.randomUUID().toString().substring(0, 8);

        c1 = new ClientJpaEntity(null, "user_" + suffix, "pass", "Juan", "Perez", "Garcia", "1111111" + suffix.charAt(0) + "A", "apikey_" + suffix);
        c2 = new ClientJpaEntity(null, "user2_" + suffix, "pass", "Ana", "Lopez", "Diaz", "2222222" + suffix.charAt(0) + "B", "apikey2_" + suffix);

        entityManager.persist(c1);
        entityManager.persist(c2);
        entityManager.flush();
    }

    @Test
    void findById_notFound_returnsEmpty() {
        Optional<ClientJpaEntity> result = clientDao.findById(999L);
        assertTrue(result.isEmpty());
    }

    @Test
    void findByUsername_existing_returnsClient() {
        Optional<ClientJpaEntity> result = clientDao.findByUsername(c1.getUsername());
        assertTrue(result.isPresent());
        assertEquals(c1.getUsername(), result.get().getUsername());
    }

    @Test
    void findByDni_existing_returnsClient() {
        Optional<ClientJpaEntity> result = clientDao.findByDni(c1.getDni());
        assertTrue(result.isPresent());
        assertEquals(c1.getDni(), result.get().getDni());
    }

    @Test
    void findByApiKey_existing_returnsClient() {
        Optional<ClientJpaEntity> result = clientDao.findByApiKey(c1.getApi_token());
        assertTrue(result.isPresent());
        assertEquals(c1.getApi_token(), result.get().getApi_token());
    }

    @Test
    void save_whenIdIsNull_shouldPersist() {
        String suffix = UUID.randomUUID().toString().substring(0, 8);
        ClientJpaEntity toSave = new ClientJpaEntity(null, "new_" + suffix, "pass", "X", "Y", "Z", "3333333" + suffix.charAt(0) + "C", "apikey3_" + suffix);

        ClientJpaEntity saved = clientDao.save(toSave);
        entityManager.flush();

        assertNotNull(saved.getId());
        assertEquals(toSave.getUsername(), saved.getUsername());
    }

    @Test
    void save_whenIdExists_shouldMerge() {
        c1.setName("UPDATED");
        ClientJpaEntity updated = clientDao.save(c1);
        entityManager.flush();

        ClientJpaEntity fromDb = entityManager.find(ClientJpaEntity.class, c1.getId());
        assertEquals(updated.getName(), fromDb.getName());
    }

    @Test
    void findAll_shouldReturnList() {
        List<ClientJpaEntity> all = clientDao.findAll();
        assertNotNull(all);
        assertTrue(all.size() >= 2);
    }

    @Test
    void existsByUsername_returnsTrueWhenExists() {
        assertTrue(clientDao.existsByUsername(c1.getUsername()));
        assertFalse(clientDao.existsByUsername("NO_EXISTE_" + UUID.randomUUID()));
    }

    @Test
    void existsByDni_returnsTrueWhenExists() {
        assertTrue(clientDao.existsByDni(c1.getDni()));
        assertFalse(clientDao.existsByDni("NO_DNI_" + UUID.randomUUID()));
    }

    @Test
    void deleteById_removesEntity() {
        clientDao.deleteById(c2.getId());
        entityManager.flush();

        assertNull(entityManager.find(ClientJpaEntity.class, c2.getId()));
    }
}

