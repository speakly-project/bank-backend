package com.speakly.bank_backend.domain.service.impl;

import com.speakly.bank_backend.domain.model.Client;
import com.speakly.bank_backend.domain.repository.ClientRepository;
import com.speakly.bank_backend.exceptions.BusinessException;
import com.speakly.bank_backend.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceImplTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientServiceImpl clientService;

    private Client client;

    @BeforeEach
    void setUp() {
        client = new Client(
                1L,
                "juan.perez",
                "secret",
                "Juan",
                "Pérez",
                "García",
                "12345678A",
                "apikey1",
                List.of()
        );
    }

    @Test
    void getAll_delegates() {
        when(clientRepository.findAll()).thenReturn(List.of(client));

        List<Client> result = clientService.getAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(clientRepository).findAll();
    }

    @Test
    void getById_delegates() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        Optional<Client> result = clientService.getById(1L);

        assertTrue(result.isPresent());
        verify(clientRepository).findById(1L);
    }

    @Test
    void getByApiKey_delegates() {
        when(clientRepository.findByApiKey("apikey1")).thenReturn(Optional.of(client));

        Optional<Client> result = clientService.getByApiKey("apikey1");

        assertTrue(result.isPresent());
        verify(clientRepository).findByApiKey("apikey1");
    }

    @Test
    void getByName_notFound_throws() {
        when(clientRepository.findByUsername("nope")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> clientService.getByName("nope"));
        verify(clientRepository).findByUsername("nope");
    }

    @Nested
    class CreateTests {

        @Test
        void create_valid_saves() {
            when(clientRepository.existsByUsername(client.getUsername())).thenReturn(false);
            when(clientRepository.existsByDni(client.getDni())).thenReturn(false);
            when(clientRepository.save(any(Client.class))).thenAnswer(inv -> inv.getArgument(0));

            Client result = clientService.create(client);

            assertNotNull(result);
            verify(clientRepository).existsByUsername(client.getUsername());
            verify(clientRepository).existsByDni(client.getDni());
            verify(clientRepository).save(any(Client.class));
        }

        @Test
        void create_duplicateUsername_throwsBusinessException() {
            when(clientRepository.existsByUsername(client.getUsername())).thenReturn(true);

            assertThrows(BusinessException.class, () -> clientService.create(client));
            verify(clientRepository).existsByUsername(client.getUsername());
            verify(clientRepository, never()).save(any());
        }

        @Test
        void create_duplicateDni_throwsBusinessException() {
            when(clientRepository.existsByUsername(client.getUsername())).thenReturn(false);
            when(clientRepository.existsByDni(client.getDni())).thenReturn(true);

            assertThrows(BusinessException.class, () -> clientService.create(client));
            verify(clientRepository).existsByUsername(client.getUsername());
            verify(clientRepository).existsByDni(client.getDni());
            verify(clientRepository, never()).save(any());
        }
    }

    @Nested
    class UpdateTests {

        @Test
        void update_nullId_throwsIllegalArgumentException() {
            Client invalid = new Client(
                    null,
                    "juan.perez",
                    "secret",
                    "Juan",
                    "Pérez",
                    "García",
                    "12345678A",
                    "apikey1",
                    List.of()
            );

            assertThrows(IllegalArgumentException.class, () -> clientService.update(invalid));
            verifyNoInteractions(clientRepository);
        }

        @Test
        void update_notFound_throwsResourceNotFoundException() {
            when(clientRepository.findById(1L)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> clientService.update(client));
            verify(clientRepository).findById(1L);
            verify(clientRepository, never()).save(any());
        }

        @Test
        void update_duplicateDni_forOtherClient_throwsBusinessException() {
            when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

            Client other = new Client(
                    2L,
                    "other",
                    "secret",
                    "Other",
                    "Surname",
                    "Two",
                    client.getDni(),
                    "apikey2",
                    List.of()
            );
            when(clientRepository.findByUsername(client.getUsername())).thenReturn(Optional.of(client));
            when(clientRepository.findByDni(client.getDni())).thenReturn(Optional.of(other));

            assertThrows(BusinessException.class, () -> clientService.update(client));
            verify(clientRepository).findById(1L);
            verify(clientRepository).findByDni(client.getDni());
        }

        @Test
        void update_valid_saves() {
            when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
            when(clientRepository.findByUsername(client.getUsername())).thenReturn(Optional.of(client));
            when(clientRepository.findByDni(client.getDni())).thenReturn(Optional.of(client));
            when(clientRepository.save(any(Client.class))).thenAnswer(inv -> inv.getArgument(0));

            Client result = clientService.update(client);

            assertNotNull(result);
            verify(clientRepository).findById(1L);
            verify(clientRepository).save(any(Client.class));
        }
    }

    @Nested
    class DeleteTests {

        @Test
        void delete_notFound_throwsResourceNotFoundException() {
            when(clientRepository.findById(1L)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> clientService.deleteById(1L));
            verify(clientRepository).findById(1L);
            verify(clientRepository, never()).deleteById(any());
        }

        @Test
        void delete_existing_deletes() {
            when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

            clientService.deleteById(1L);

            verify(clientRepository).findById(1L);
            verify(clientRepository).deleteById(1L);
        }
    }
}

