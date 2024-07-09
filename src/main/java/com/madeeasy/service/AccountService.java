package com.madeeasy.service;

import com.madeeasy.entity.Account;
import com.madeeasy.exception.ATMException;
import com.madeeasy.exception.InvalidAccountException;

public interface AccountService {
    void createAccount() throws ATMException;

    void closeAccount(String accountId) throws ATMException;

    void openAccount(String accountId) throws InvalidAccountException;

    Account login(String accountId, String pin) throws ATMException;

    void deposit(double amount) throws ATMException;

    void withdraw(double amount) throws ATMException;

    void transfer(String toUserId, double amount) throws ATMException;

    void printTransactionHistory();

    void printTotalAmount();

    void quit();
}
