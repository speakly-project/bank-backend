package com.speakly.bank_backend.persistence.repository;

import com.speakly.bank_backend.domain.model.BankAccount;
import com.speakly.bank_backend.domain.repository.BankAccountRepository;
import com.speakly.bank_backend.persistence.dao.BankAccountDao;
import com.speakly.bank_backend.mapper.BankAccountMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BankAccountRepositoryImpl implements BankAccountRepository {

    private final BankAccountDao bankAccountDao;

    public BankAccountRepositoryImpl(BankAccountDao bankAccountDao) {
        this.bankAccountDao = bankAccountDao;
    }

    @Override
    public Optional<BankAccount> findById(Long id) {
        return bankAccountDao.findById(id)
                .map(BankAccountMapper::fromEntityToModel);
    }

    @Override
    public Optional<BankAccount> findByIban(String iban) {
        return bankAccountDao.findByIban(iban)
                .map(BankAccountMapper::fromEntityToModel);
    }

    @Override
    public List<BankAccount> findByClientId(Long clientId) {
        return bankAccountDao.findByClientId(clientId).stream()
                .map(BankAccountMapper::fromEntityToModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<BankAccount> findAll() {
        return bankAccountDao.findAll().stream()
                .map(BankAccountMapper::fromEntityToModel)
                .collect(Collectors.toList());
    }

    @Override
    public BankAccount save(BankAccount account) {
        return BankAccountMapper.fromEntityToModel(
                bankAccountDao.save(BankAccountMapper.fromModelToEntity(account))
        );
    }

    @Override
    public void deleteById(Long id) {
        bankAccountDao.deleteById(id);
    }

    @Override
    public boolean existsByIban(String iban) {
        return bankAccountDao.existsByIban(iban);
    }
}

