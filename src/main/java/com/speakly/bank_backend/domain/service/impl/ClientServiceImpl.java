package com.speakly.bank_backend.domain.service.impl;

import com.speakly.bank_backend.domain.model.Client;
import com.speakly.bank_backend.domain.repository.ClientRepository;
import com.speakly.bank_backend.domain.service.ClientService;
import com.speakly.bank_backend.exceptions.BusinessException;
import com.speakly.bank_backend.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public Optional<Client> getById(Long id) {
        return clientRepository.findById(id);
    }

    @Override
    public List<Client> getAll() {
        return clientRepository.findAll();
    }

    @Override
    @Transactional
    public Client create(Client client) {
        if (clientRepository.existsByUsername(client.getUsername())) {
            throw new BusinessException("Username already exists: " + client.getUsername());
        }
        if (clientRepository.existsByDni(client.getDni())) {
            throw new BusinessException("DNI already exists: " + client.getDni());
        }
        return clientRepository.save(client);
    }

    @Override
    @Transactional
    public Client update(Client client) {
        if (client.getId() == null) {
            throw new IllegalArgumentException("Client ID cannot be null for update");
        }
        verifyClientExists(client.getId());

        clientRepository.findByUsername(client.getUsername())
                .filter(c -> !c.getId().equals(client.getId()))
                .ifPresent(c -> {
                    throw new BusinessException("Username already exists: " + client.getUsername());
                });

        clientRepository.findByDni(client.getDni())
                .filter(c -> !c.getId().equals(client.getId()))
                .ifPresent(c -> {
                    throw new BusinessException("DNI already exists: " + client.getDni());
                });

        return clientRepository.save(client);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        verifyClientExists(id);
        clientRepository.deleteById(id);
    }


    private void verifyClientExists(Long id) {
        if (clientRepository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Client not found with id: " + id);
        }
    }
}
