package com.speakly.bank_backend.config;

import com.speakly.bank_backend.domain.repository.*;
import com.speakly.bank_backend.domain.service.*;
import com.speakly.bank_backend.domain.service.impl.*;
import com.speakly.bank_backend.domain.usecase.CardTransactionUseCase;
import com.speakly.bank_backend.filters.AuthFilter;
import com.speakly.bank_backend.persistence.dao.*;
import com.speakly.bank_backend.persistence.dao.impl.*;
import com.speakly.bank_backend.persistence.repository.*;
import com.speakly.bank_backend.usecase.CardTransactionUseCaseImpl;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {

    @Bean
    public FilterRegistrationBean<AuthFilter> authFilter(AuthService authService) {
        FilterRegistrationBean<AuthFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new AuthFilter(authService));
        registration.addUrlPatterns("/*");
        return registration;
    }

    @Bean
    public AuthDao authDao() {
        return new AuthDaoImpl();
    }

    @Bean
    public AuthRepository authRepository(AuthDao authDao){
        return new AuthRepositoryImpl(authDao);
    }

    @Bean
    public AuthService authService(ClientRepository userRepository, AuthRepository authRepository) {
        return new AuthServiceImpl(userRepository, authRepository);
    }

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

    @Bean
    public ClientService clientService(ClientRepository clientRepository) {
        return new ClientServiceImpl(clientRepository);
    }

    @Bean
    public BankAccountService bankAccountService(BankAccountRepository bankAccountRepository) {
        return new BankAccountServiceImpl(bankAccountRepository);
    }

    @Bean
    public CreditCardService creditCardService(CreditCardRepository creditCardRepository) {
        return new CreditCardServiceImpl(creditCardRepository);
    }

    @Bean
    public BankTransactionService bankTransactionService(BankTransactionRepository bankTransactionRepository) {
        return new BankTransactionServiceImpl(bankTransactionRepository);
    }

    @Bean
    public CardTransactionUseCase cardTransactionUseCase(CreditCardService creditCardService,
                                                         BankAccountService bankAccountService,
                                                         BankTransactionService bankTransactionService,
                                                         ClientService clientService) {
        return new CardTransactionUseCaseImpl(creditCardService, bankAccountService, bankTransactionService, clientService);
    }
}

