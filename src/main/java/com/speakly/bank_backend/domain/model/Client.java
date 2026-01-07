package com.speakly.bank_backend.domain.model;

import java.util.List;

public class Client {
    private Long id;
    private String username;
    private String password;
    private String name;
    private String firstSurname;
    private String secondSurname;
    private String dni;
    private String api_token;
    private List<BankAccount> accounts;

    public Client(Long id, String username, String password, String name, String firstSurname, String secondSurname, String dni, String api_token, List<BankAccount> accounts) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.firstSurname = firstSurname;
        this.secondSurname = secondSurname;
        this.dni = dni;
        this.api_token = api_token;
        this.accounts = accounts;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstSurname() {
        return firstSurname;
    }

    public void setFirstSurname(String firstSurname) {
        this.firstSurname = firstSurname;
    }

    public String getSecondSurname() {
        return secondSurname;
    }

    public void setSecondSurname(String secondSurname) {
        this.secondSurname = secondSurname;
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

    public List<BankAccount> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<BankAccount> accounts) {
        this.accounts = accounts;
    }
}
