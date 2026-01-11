package com.speakly.bank_backend.domain.service;

import java.util.Optional;
import java.util.List;

import com.speakly.bank_backend.domain.model.Client;

public interface ClientService {
    Optional<Client> getById(Long id);
    List<Client> getAll();
    Client create(Client client);
    Client update(Client client);
    void deleteById(Long id);
}

