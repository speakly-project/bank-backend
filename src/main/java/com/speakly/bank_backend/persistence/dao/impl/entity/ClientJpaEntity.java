package com.speakly.bank_backend.persistence.dao.impl.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "client")
public class ClientJpaEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    private String name;

    private String first_surname;

    private String second_surname;

    private String dni;

    @Column(name = "api_key")
    private String api_token;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BankAccountJpaEntity> accounts = new ArrayList<>();

    public ClientJpaEntity() {
    }

    public ClientJpaEntity(Long id, String username, String password, String name, String first_surname, String second_surname, String dni, String api_token) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.first_surname = first_surname;
        this.second_surname = second_surname;
        this.dni = dni;
        this.api_token = api_token;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirst_surname() {
        return first_surname;
    }

    public void setFirst_surname(String first_surname) {
        this.first_surname = first_surname;
    }

    public String getSecond_surname() {
        return second_surname;
    }

    public void setSecond_surname(String second_surname) {
        this.second_surname = second_surname;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getApi_token() {
        return api_token;
    }

    public void setApi_token(String api_token) {
        this.api_token = api_token;
    }

    public List<BankAccountJpaEntity> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<BankAccountJpaEntity> accounts) {
        this.accounts = accounts;
    }
}

