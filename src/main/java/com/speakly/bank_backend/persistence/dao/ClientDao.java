
package com.speakly.bank_backend.persistence.dao;
import java.util.Optional;
import java.util.List;

import com.speakly.bank_backend.persistence.dao.impl.entity.ClientJpaEntity;

public interface ClientDao {
    boolean existsByDni(String dni);
    boolean existsByUsername(String username);
    void deleteById(Long id);
    ClientJpaEntity save(ClientJpaEntity client);
    List<ClientJpaEntity> findAll();
    Optional<ClientJpaEntity> findByApiKey(String apiToken);
    Optional<ClientJpaEntity> findByDni(String dni);
    Optional<ClientJpaEntity> findByUsername(String username);
    Optional<ClientJpaEntity> findById(Long id);
}

