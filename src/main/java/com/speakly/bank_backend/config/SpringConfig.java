package com.speakly.bank_backend.config;

import com.speakly.bank_backend.domain.repository.BankAccountRepository;
import com.speakly.bank_backend.domain.repository.BankTransactionRepository;
import com.speakly.bank_backend.domain.repository.ClientRepository;
import com.speakly.bank_backend.domain.repository.CreditCardRepository;
import com.speakly.bank_backend.persistence.dao.BankAccountDao;
import com.speakly.bank_backend.persistence.dao.BankTransactionDao;
import com.speakly.bank_backend.persistence.dao.ClientDao;
import com.speakly.bank_backend.persistence.dao.CreditCardDao;
import com.speakly.bank_backend.persistence.dao.impl.BankAccountDaoImpl;
import com.speakly.bank_backend.persistence.dao.impl.BankTransactionDaoImpl;
import com.speakly.bank_backend.persistence.dao.impl.ClientDaoImpl;
import com.speakly.bank_backend.persistence.dao.impl.CreditCardDaoImpl;
import com.speakly.bank_backend.persistence.repository.BankAccountRepositoryImpl;
import com.speakly.bank_backend.persistence.repository.BankTransactionRepositoryImpl;
import com.speakly.bank_backend.persistence.repository.ClientRepositoryImpl;
import com.speakly.bank_backend.persistence.repository.CreditCardRepositoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {

    @Bean
    public ClientDao clientDao() {
        return new ClientDaoImpl();
    }

    @Bean
    public BankAccountDao bankAccountDao() {
        return new BankAccountDaoImpl();
    }

    @Bean
    public CreditCardDao creditCardDao() {
        return new CreditCardDaoImpl();
    }

    @Bean
    public BankTransactionDao bankTransactionDao() {
        return new BankTransactionDaoImpl();
    }

    @Bean
    public ClientRepository clientRepository(ClientDao clientDao) {
        return new ClientRepositoryImpl(clientDao);
    }

    @Bean
    public BankAccountRepository bankAccountRepository(BankAccountDao bankAccountDao) {
        return new BankAccountRepositoryImpl(bankAccountDao);
    }

    @Bean
    public CreditCardRepository creditCardRepository(CreditCardDao creditCardDao) {
        return new CreditCardRepositoryImpl(creditCardDao);
    }

    @Bean
    public BankTransactionRepository bankTransactionRepository(BankTransactionDao bankTransactionDao) {
        return new BankTransactionRepositoryImpl(bankTransactionDao);
    }
}

