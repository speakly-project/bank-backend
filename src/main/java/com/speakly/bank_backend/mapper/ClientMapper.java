package com.speakly.bank_backend.mapper;

import com.speakly.bank_backend.domain.model.Client;
import com.speakly.bank_backend.persistence.dao.impl.entity.ClientJpaEntity;

public class ClientMapper {

    private static ClientMapper INSTANCE;

    private ClientMapper() {
    }

    public static ClientMapper getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ClientMapper();
        }
        return INSTANCE;
    }

    public static Client fromEntityToDomain(ClientJpaEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Client(
                entity.getId(),
                entity.getUsername(),
                entity.getPassword(),
                entity.getName(),
                entity.getFirst_surname(),
                entity.getSecond_surname(),
                entity.getDni(),
                entity.getApi_token(),
                null
        );
    }

    public static ClientJpaEntity fromDomainToEntity(Client domain) {
        if (domain == null) {
            return null;
        }
        return new ClientJpaEntity(
                domain.getId(),
                domain.getUsername(),
                domain.getPassword(),
                domain.getName(),
                domain.getFirstSurname(),
                domain.getSecondSurname(),
                domain.getDni(),
                domain.getApi_token()
        );
    }

    public Client toDomain(ClientJpaEntity entity) {
        return fromEntityToDomain(entity);
    }

    public ClientJpaEntity toEntity(Client domain) {
        return fromDomainToEntity(domain);
    }
}

