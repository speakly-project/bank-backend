package com.speakly.bank_backend.persistence.dao.impl;

import com.speakly.bank_backend.persistence.dao.ClientDao;
import com.speakly.bank_backend.persistence.dao.impl.entity.ClientJpaEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;

public class ClientDaoImpl implements ClientDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<ClientJpaEntity> findById(Long id) {
        return Optional.ofNullable(entityManager.find(ClientJpaEntity.class, id));
    }

    @Override
    public Optional<ClientJpaEntity> findByUsername(String username) {
        try {
            String sql = "SELECT c FROM ClientJpaEntity c WHERE c.username = :username";
            ClientJpaEntity result = entityManager.createQuery(sql, ClientJpaEntity.class)
                    .setParameter("username", username)
                    .getSingleResult();
            return Optional.of(result);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<ClientJpaEntity> findByDni(String dni) {
        try {
            String sql = "SELECT c FROM ClientJpaEntity c WHERE c.dni = :dni";
            ClientJpaEntity result = entityManager.createQuery(sql, ClientJpaEntity.class)
                    .setParameter("dni", dni)
                    .getSingleResult();
            return Optional.of(result);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<ClientJpaEntity> findByApiKey(String apiToken) {
        try {
            String sql = "SELECT c FROM ClientJpaEntity c WHERE c.api_token = :apiToken";
            ClientJpaEntity result = entityManager.createQuery(sql, ClientJpaEntity.class)
                    .setParameter("apiToken", apiToken)
                    .getSingleResult();
            return Optional.of(result);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<ClientJpaEntity> findAll() {
        String sql = "SELECT c FROM ClientJpaEntity c ORDER BY c.id";
        TypedQuery<ClientJpaEntity> query = entityManager.createQuery(sql, ClientJpaEntity.class);
        return query.getResultList();
    }

    @Override
    public ClientJpaEntity save(ClientJpaEntity client) {
        if (client.getId() == null) {
            entityManager.persist(client);
            return client;
        } else {
            return entityManager.merge(client);
        }
    }

    @Override
    public void deleteById(Long id) {
        ClientJpaEntity client = entityManager.find(ClientJpaEntity.class, id);
        if (client != null) {
            entityManager.remove(client);
        }
    }

    @Override
    public boolean existsByUsername(String username) {
        String sql = "SELECT COUNT(c) FROM ClientJpaEntity c WHERE c.username = :username";
        Long count = entityManager.createQuery(sql, Long.class)
                .setParameter("username", username)
                .getSingleResult();
        return count > 0;
    }

    @Override
    public boolean existsByDni(String dni) {
        String sql = "SELECT COUNT(c) FROM ClientJpaEntity c WHERE c.dni = :dni";
        Long count = entityManager.createQuery(sql, Long.class)
                .setParameter("dni", dni)
                .getSingleResult();
        return count > 0;
    }
}

