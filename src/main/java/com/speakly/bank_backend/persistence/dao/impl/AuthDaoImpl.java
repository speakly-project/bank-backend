package com.speakly.bank_backend.persistence.dao.impl;

import com.speakly.bank_backend.domain.dto.LoginUserDto;
import com.speakly.bank_backend.exceptions.ResourceNotFoundException;
import com.speakly.bank_backend.persistence.dao.AuthDao;
import com.speakly.bank_backend.persistence.dao.impl.entity.ClientJpaEntity;
import com.speakly.bank_backend.persistence.dao.impl.entity.SessionJpaEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public class AuthDaoImpl implements AuthDao{
    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public Optional<LoginUserDto> findByToken(String token) {
        try {
            String sql = "SELECT new com.speakly.bank_backend.domain.dto.LoginUserDto(u.id, u.username) FROM SessionJpaEntity s JOIN s.client u WHERE s.token = :token";
            LoginUserDto user = entityManager.createQuery(sql, LoginUserDto.class)
                    .setParameter("token", token)
                    .getSingleResult();
            return Optional.ofNullable(user);
        } catch (Exception ex) {
            return Optional.empty();
        }
    }


    @Override
    public UUID createTokenForUser(Long clientId) {
        ClientJpaEntity clientJpaEntity = entityManager.find(ClientJpaEntity.class, clientId);
        if (clientJpaEntity == null) {
            throw new ResourceNotFoundException("User not found with id: " + clientId);
        }
        UUID uuid = UUID.randomUUID();
        LocalDateTime createAt = LocalDateTime.now();

        SessionJpaEntity session = new SessionJpaEntity(uuid.toString(), clientJpaEntity, createAt);
        entityManager.persist(session);
        return uuid;
    }

    @Override
    public void deleteToken(String token) {
        String sql = "DELETE FROM SessionJpaEntity s WHERE s.token = :token";
        entityManager.createQuery(sql)
                .setParameter("token", token)
                .executeUpdate();
    }

    @Override
    public Long count() {
        return entityManager.createQuery("SELECT COUNT(s) FROM SessionJpaEntity s", Long.class)
                .getSingleResult();
    }
}

