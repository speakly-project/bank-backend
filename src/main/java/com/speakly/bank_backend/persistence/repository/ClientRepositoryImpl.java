package com.speakly.bank_backend.persistence.repository;

import com.speakly.bank_backend.domain.model.Client;
import com.speakly.bank_backend.domain.repository.ClientRepository;
import com.speakly.bank_backend.persistence.dao.ClientDao;
import com.speakly.bank_backend.mapper.ClientMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ClientRepositoryImpl implements ClientRepository {

    private final ClientDao clientDao;

    public ClientRepositoryImpl(ClientDao clientDao) {
        this.clientDao = clientDao;
    }

    @Override
    public Optional<Client> findById(Long id) {
        return clientDao.findById(id)
                .map(ClientMapper::fromEntityToDomain);
    }

    @Override
    public Optional<Client> findByUsername(String username) {
        return clientDao.findByUsername(username)
                .map(ClientMapper::fromEntityToDomain);
    }

    @Override
    public Optional<Client> findByDni(String dni) {
        return clientDao.findByDni(dni)
                .map(ClientMapper::fromEntityToDomain);
    }

    @Override
    public Optional<Client> findByApiToken(String apiToken) {
        return clientDao.findByApiToken(apiToken)
                .map(ClientMapper::fromEntityToDomain);
    }

    @Override
    public List<Client> findAll() {
        return clientDao.findAll().stream()
                .map(ClientMapper::fromEntityToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Client save(Client client) {
        return ClientMapper.fromEntityToDomain(
                clientDao.save(ClientMapper.fromDomainToEntity(client))
        );
    }

    @Override
    public void deleteById(Long id) {
        clientDao.deleteById(id);
    }

    @Override
    public boolean existsByUsername(String username) {
        return clientDao.existsByUsername(username);
    }

    @Override
    public boolean existsByDni(String dni) {
        return clientDao.existsByDni(dni);
    }
}

