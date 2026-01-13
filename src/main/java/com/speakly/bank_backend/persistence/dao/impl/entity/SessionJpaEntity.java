package com.speakly.bank_backend.persistence.dao.impl.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;


@Entity
@Table(name = "sessions")
public class SessionJpaEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token", nullable = false)
    private String token;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private ClientJpaEntity client;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public SessionJpaEntity() {}

    public SessionJpaEntity(String token, ClientJpaEntity client, LocalDateTime createdAt) {
        this.client = client;
        this.token = token;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ClientJpaEntity getUser() {
        return client;
    }

    public void setUser(ClientJpaEntity user) {
        this.client = user;
    }

    public ClientJpaEntity getClient() {
        return client;
    }

    public void setClient(ClientJpaEntity client) {
        this.client = client;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }


}
