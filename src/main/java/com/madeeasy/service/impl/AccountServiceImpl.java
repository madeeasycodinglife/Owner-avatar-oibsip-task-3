package com.madeeasy.service.impl;

import com.madeeasy.dao.AccountDAO;
import com.madeeasy.dao.TransactionDAO;
import com.madeeasy.dao.impl.AccountDAOImpl;
import com.madeeasy.dao.impl.TransactionDAOImpl;
import com.madeeasy.dao.impl.UserDAOImpl;
import com.madeeasy.entity.Account;
import com.madeeasy.entity.Role;
import com.madeeasy.entity.TransactionEntity;
import com.madeeasy.entity.User;
import com.madeeasy.exception.ATMException;
import com.madeeasy.exception.InsufficientFundsException;
import com.madeeasy.exception.InvalidAccountException;
import com.madeeasy.exception.UnauthorizedAccessException;
import com.madeeasy.service.AccountService;
import lombok.Data;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
@Data
public class AccountServiceImpl implements AccountService {


    private AccountDAO accountDAO = new AccountDAOImpl();
    private UserDAOImpl userDAO = new UserDAOImpl();
    private TransactionDAO transactionDAO = new TransactionDAOImpl();
    private Account currentAccount;
    Scanner scanner = new Scanner(System.in);


    @Override
    public void createAccount() throws ATMException {

        System.out.print("Admin / User : ");
        String role = scanner.nextLine();

        if (role.equalsIgnoreCase("admin")) {
            createAdminAccount();
        } else {
            createUserAccount();
        }
    }

    private void createAdminAccount() {
        System.out.print("Enter Your Name : ");
        String name = scanner.nextLine();
        System.out.print("Enter Your Email : ");
        String email = scanner.nextLine();
        System.out.print("Enter Your Phone : ");
        String phone = scanner.nextLine();
        System.out.print("Enter Your PIN : ");
        String pin = scanner.nextLine();
        System.out.print("Enter Your Address : ");
        String address = scanner.nextLine();
        System.out.print("Enter Initial Deposit : ");
        double initialDeposit = scanner.nextDouble();


        User user = User.builder()
                .id(generateRandomUserId())
                .name(name)
                .email(email)
                .phone(phone)
                .pin(pin)
                .address(address)
                .roles(List.of(Role.ADMIN))
                .build();

        // Create a SimpleDateFormat object with desired format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        TransactionEntity transaction = TransactionEntity.builder()
                .id(generateRandomTransactionId())
                .date(Timestamp.valueOf(dateFormat.format(new Date())))
                .type("Initial Deposit")
                .amount(initialDeposit)
                .build();

        Account newAccount = Account.builder()
                .id(generateRandomAccountId())
                .balance(initialDeposit)
                .user(user)
                .isActive(true)
                .build();

        transaction.setAccount(newAccount);

        newAccount.setTransactions(new ArrayList<>(List.of(transaction)));
        accountDAO.save(newAccount);
        scanner.nextLine();
    }

    private void createUserAccount() {
        System.out.print("Enter Your Name : ");
        String name = scanner.nextLine();
        System.out.print("Enter Your Email : ");
        String email = scanner.nextLine();
        System.out.print("Enter Your Phone : ");
        String phone = scanner.nextLine();
        System.out.print("Enter Your PIN : ");
        String pin = scanner.nextLine();
        System.out.print("Enter Your Address : ");
        String address = scanner.nextLine();
        System.out.print("Enter Initial Deposit : ");
        double initialDeposit = scanner.nextDouble();


        User user = User.builder()
                .id(generateRandomUserId())
                .name(name)
                .email(email)
                .phone(phone)
                .pin(pin)
                .address(address)
                .roles(List.of(Role.USER))
                .build();


        // Create a SimpleDateFormat object with desired format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        TransactionEntity transaction = TransactionEntity.builder()
                .id(generateRandomTransactionId())
                .date(Timestamp.valueOf(dateFormat.format(new Date())))
                .type("Initial Deposit")
                .amount(initialDeposit)
                .build();

        Account newAccount = Account.builder()
                .id(generateRandomAccountId())
                .balance(initialDeposit)
                .user(user)
                .isActive(true)
                .build();

        transaction.setAccount(newAccount);

        newAccount.setTransactions(new ArrayList<>(List.of(transaction)));
        accountDAO.save(newAccount);
    }

    @Override
    public void closeAccount(String accountId) throws ATMException {
        Account account = accountDAO.findByAccountId(accountId);
        if (account == null) {
            throw new InvalidAccountException("Account does not exist.");
        }
        if (!account.isActive()) {
            throw new InvalidAccountException("Account is already closed.");
        }

        account.setActive(false);
        accountDAO.save(account);
        System.out.println();
    }

    @Override
    public void openAccount(String accountId) throws InvalidAccountException {
        Account account = accountDAO.findByAccountId(accountId);
        if (account == null) {
            throw new InvalidAccountException("Account does not exist.");
        }
        if (account.isActive()) {
            throw new InvalidAccountException("Account is already Opened.");
        }

        account.setActive(true);
        accountDAO.save(account);

    }

    @Override
    public Account login(String accountId, String pin) throws ATMException {
        Account account = accountDAO.findByAccountId(accountId);
        if (account == null) {
            throw new InvalidAccountException("Invalid account.");
        }
        if (!account.getUser().getPin().equals(pin)) {
            throw new UnauthorizedAccessException("Unauthorized access.");
        }
        if (!account.isActive()) {
            throw new InvalidAccountException("Account is closed.");
        }
        currentAccount = account;
        return account;
    }

    @Override
    public void deposit(double amount) throws ATMException {
        if (currentAccount == null) {
            throw new UnauthorizedAccessException("Please login first.");
        }
        currentAccount.setBalance(currentAccount.getBalance() + amount);

        // Create a SimpleDateFormat object with desired format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        TransactionEntity transaction = new TransactionEntity();
        transaction.setId(generateRandomTransactionId());
        transaction.setAccount(currentAccount);
        transaction.setDate(Timestamp.valueOf(dateFormat.format(new Date())));
        transaction.setType("Deposit");
        transaction.setAmount(amount);

        currentAccount.getTransactions().add(transaction);
        accountDAO.save(currentAccount);
    }

    @Override
    public void withdraw(double amount) throws ATMException {
        if (currentAccount == null) {
            throw new UnauthorizedAccessException("Please login first.");
        }
        if (currentAccount.getBalance() < amount) {
            throw new InsufficientFundsException("Insufficient funds.");
        }


        currentAccount.setBalance(currentAccount.getBalance() - amount);

        // Create a SimpleDateFormat object with desired format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        TransactionEntity transaction = new TransactionEntity();
        transaction.setId(generateRandomTransactionId());
        transaction.setAccount(currentAccount);
        transaction.setDate(Timestamp.valueOf(dateFormat.format(new Date())));
        transaction.setType("Withdraw");
        transaction.setAmount(amount);


        currentAccount.getTransactions().add(transaction);

        accountDAO.save(currentAccount);
    }

    @Override
    public void transfer(String toAccountId, double amount) throws ATMException {
        if (currentAccount == null) {
            throw new UnauthorizedAccessException("Please login first.");
        }
        if (currentAccount.getBalance() < amount) {
            throw new InsufficientFundsException("Insufficient funds.");
        }
        Account targetAccount = accountDAO.findByAccountId(toAccountId);
        if (targetAccount == null) {
            throw new InvalidAccountException("Invalid destination account.");
        }
        if (!targetAccount.isActive()) {
            throw new InvalidAccountException("Destination account is closed.");
        }

        currentAccount.setBalance(currentAccount.getBalance() - amount);
        targetAccount.setBalance(targetAccount.getBalance() + amount);

        // Create a SimpleDateFormat object with desired format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        TransactionEntity transaction = new TransactionEntity();
        transaction.setId(generateRandomTransactionId());
        transaction.setAccount(currentAccount);
        transaction.setDate(Timestamp.valueOf(dateFormat.format(new Date())));
        transaction.setType("Transfer");
        transaction.setAmount(amount);
        transaction.setRelatedAccountId(toAccountId);  // Set related account ID
        currentAccount.getTransactions().add(transaction);

        TransactionEntity toTransaction = new TransactionEntity();
        toTransaction.setId(generateRandomTransactionId());
        toTransaction.setAccount(targetAccount);
        toTransaction.setDate(Timestamp.valueOf(dateFormat.format(new Date())));
        toTransaction.setType("Received");
        toTransaction.setAmount(amount);
        toTransaction.setRelatedAccountId(currentAccount.getId());  // Set related account ID
        targetAccount.getTransactions().add(toTransaction);

        accountDAO.save(currentAccount);
        accountDAO.save(targetAccount);
    }

    @Override
    public void printTransactionHistory() {
        if (currentAccount != null) {
            System.out.println("\n\033[1;34m||===============================================================================================================||\033[0m");
            System.out.println("\033[1;32m||                                                  Transaction History                                          ||\033[0m");
            System.out.println("\033[1;34m||===============================================================================================================||\033[0m");
            System.out.println("\033[1;33m  Date & Time                |      Type           |   Amount     |         Related Account \033[0m");
            System.out.println("\033[1;34m||---------------------------------------------------------------------------------------------------------------||\033[0m");

            for (TransactionEntity transaction : this.currentAccount.getTransactions()) {
                System.out.printf(
                        "\033[1;36m  %-24s       %-15s     %9.2f         %-15s \033[0m\n",
                        transaction.getDate().toString(),
                        transaction.getType(),
                        transaction.getAmount(),
                        transaction.getRelatedAccountId() != null ? transaction.getRelatedAccountId() : "N/A"
                );
            }

            System.out.println("\033[1;34m||================================================================================================================|\033[0m\n");
        } else {
            System.out.println("\033[1;31mNo transactions found.\033[0m");
        }
    }



    @Override
    public void printTotalAmount() {
        if (currentAccount != null) {
            System.out.println("\n\033[1;34m||====================================================||");
            System.out.println("\033[1;32m||                  Account Balance                   ||");
            System.out.println("\033[1;34m||====================================================||");

            System.out.printf(
                    "\033[1;36m              Total Balance: %,.2f               \n",
                    currentAccount.getBalance()
            );

            System.out.println("\033[1;34m||====================================================||\n");
        } else {
            System.out.println("\033[1;31mNo account is currently logged in.\033[0m");
        }
    }


    @Override
    public void quit() {
        currentAccount = null;
        System.out.print("Session ended.");
    }

    private String generateRandomUserId() {
        return "USER" + UUID.randomUUID().toString();
    }

    private String generateRandomAccountId() {
        return "ACNT" + UUID.randomUUID().toString();
    }

    private String generateRandomTransactionId() {
        return "TRXN" + UUID.randomUUID().toString();
    }
}
