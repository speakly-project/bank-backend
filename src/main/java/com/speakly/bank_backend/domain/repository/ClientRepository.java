package com.speakly.bank_backend.domain.repository;

import com.speakly.bank_backend.domain.model.Client;

import java.util.List;
import java.util.Optional;

public interface ClientRepository {
    Optional<Client> findById(Long id);
    Optional<Client> findByUsername(String username);
    Optional<Client> findByDni(String dni);
    Optional<Client> findByApiKey(String apiKey);
    List<Client> findAll();
    Client save(Client client);
    void deleteById(Long id);
    boolean existsByUsername(String username);
    boolean existsByDni(String dni);
}

