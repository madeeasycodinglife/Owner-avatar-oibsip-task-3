package com.madeeasy;

import com.madeeasy.color.Color;
import com.madeeasy.entity.Account;
import com.madeeasy.entity.Role;
import com.madeeasy.exception.ATMException;
import com.madeeasy.service.impl.AccountServiceImpl;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class ATMInterfaceApplication {

    private static AccountServiceImpl accountService = new AccountServiceImpl();
    private static Scanner scanner = new Scanner(System.in);
    private static boolean isLoggedIn = false;


    private static void printWelcomeMessage() {
        System.out.println(Color.BOLD_BLUE + "╔═══════════════════════════════════════╗" + Color.RESET);
        System.out.println(Color.BOLD_BLUE + "║" + Color.BOLD_GREEN + "        Welcome to Oasis Infobyte      " + Color.BOLD_BLUE + "║" + Color.RESET);
        System.out.println(Color.BOLD_BLUE + "╠═══════════════════════════════════════╣" + Color.RESET);
        System.out.println(Color.BOLD_BLUE + "║" + Color.BOLD_GREEN + "               ATM System              " + Color.BOLD_BLUE + "║" + Color.RESET);
        System.out.println(Color.BOLD_BLUE + "╚═══════════════════════════════════════╝" + Color.RESET);
    }


    public static void main(String[] args) {
        printWelcomeMessage();
        printInitialMenuForNotLoggedIn();
    }

    private static void printInitialMenuForNotLoggedIn() {
        while (true) {
            try {
                System.out.println("\033[1;34m╔════════════════════════╗\033[0m");
                System.out.println("\033[1;34m║                        ║\033[0m");
                System.out.println("\033[1;34m║   \033[1;32m1. Create Account    \033[1;34m║\033[0m");
                System.out.println("\033[1;34m║   \033[1;36m2. Login             \033[1;34m║\033[0m");
                System.out.println("\033[1;34m║   \033[1;31m3. Quit              \033[1;34m║\033[0m");
                System.out.println("\033[1;34m║                        ║\033[0m");
                System.out.println("\033[1;34m╚════════════════════════╝\033[0m");

                System.out.print("✍\uFE0F " + Color.BOLD_PURPLE + "Enter choice: " + Color.RESET);

                try {
                    int choice = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    switch (choice) {

                        case 1:
                            accountService.createAccount();
                            break;
                        case 2:
                            System.out.print("Enter Account ID: ");
                            String loginUserId = scanner.nextLine();
                            System.out.print("Enter PIN: ");
                            String loginPin = scanner.nextLine();
                            Account loggedInAccount = accountService.login(loginUserId, loginPin);
                            System.out.println("Login successful.");
                            System.out.println(loggedInAccount.getUser().getRoles());
                            List<Role> roles = loggedInAccount.getUser().getRoles();
                            String role1 = String.valueOf(roles.getFirst());
                            if (role1.equalsIgnoreCase("ADMIN")) {
                                // for user and admin
                                showATMMenuForAdmin();
                                isLoggedIn = true;
                            } else {
                                // only for user
                                showATMMenuForUser();
                                isLoggedIn = true;
                            }
                            break;

                        case 3:
                            System.out.print("Exiting... Goodbye!");
                            System.exit(0);
                        default:
                            System.out.print("Invalid choice.");
                    }
                } catch (ATMException e) {
                    System.out.println(e.getMessage());
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Consume the invalid input
            }
        }
    }

    private static void printInitialMenuForAdminLoggedIn() {
        while (true) {
            printWelcomeMessage();
            System.out.println("\033[1;34m╔════════════════════════╗\033[0m");
            System.out.println("\033[1;34m║                        ║\033[0m");
            System.out.println("\033[1;34m║   \033[1;32m1. Create Account    \033[1;34m║\033[0m");
            System.out.println("\033[1;34m║   \033[1;36m2. LogOut            \033[1;34m║\033[0m");
            System.out.println("\033[1;34m║   \033[1;31m3. Quit              \033[1;34m║\033[0m");
            System.out.println("\033[1;34m║                        ║\033[0m");
            System.out.println("\033[1;34m╚════════════════════════╝\033[0m");

            System.out.print("✍\uFE0F " + Color.BOLD_PURPLE + "Enter choice: " + Color.RESET);
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            try {
                switch (choice) {

                    case 1:
                        accountService.createAccount();
                        break;
                    case 2:
                        isLoggedIn = false;
                        printInitialMenuForNotLoggedIn();
                        break;
                    case 3:
                        System.out.print("Exiting... Goodbye!");
                        System.exit(0);
                    default:
                        System.out.print("Invalid choice.");
                }
            } catch (ATMException e) {
                System.out.print(e.getMessage());
            }
        }
    }

    private static void printInitialMenuForUserLoggedIn() {
        while (true) {
            printWelcomeMessage();
            System.out.println("\033[1;34m╔════════════════════════╗\033[0m");
            System.out.println("\033[1;34m║                        ║\033[0m");
            System.out.println("\033[1;34m║   \033[1;36m1. LogOut            \033[1;34m║\033[0m");
            System.out.println("\033[1;34m║   \033[1;31m2. Quit              \033[1;34m║\033[0m");
            System.out.println("\033[1;34m║                        ║\033[0m");
            System.out.println("\033[1;34m╚════════════════════════╝\033[0m");

            System.out.print("✍\uFE0F " + Color.BOLD_PURPLE + "Enter choice: " + Color.RESET);
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    isLoggedIn = false;
                    printInitialMenuForNotLoggedIn();
                    break;
                case 2:
                    System.out.print("Exiting... Goodbye!");
                    System.exit(0);
                default:
                    System.out.print("Invalid choice.");
            }
        }
    }

    private static void showATMMenuForAdmin() {
        while (true) {
            printWelcomeMessage();
            System.out.println(Color.BOLD_BLUE + "\n╔════════════════════════════════════╗");
            System.out.println("║     " + Color.BOLD_GREEN + "Administrator Menu" + Color.BOLD_BLUE + "             ║");
            System.out.println("╠════════════════════════════════════╣");
            System.out.println("║ " + Color.BOLD_CYAN + "1. Transactions History            " + Color.BOLD_BLUE + "║");
            System.out.println("║ " + Color.BOLD_GREEN + "2. Withdraw                        " + Color.BOLD_BLUE + "║");
            System.out.println("║ " + Color.BOLD_YELLOW + "3. Deposit                         " + Color.BOLD_BLUE + "║");
            System.out.println("║ " + Color.BOLD_GREEN + "4. Transfer                        " + Color.BOLD_BLUE + "║");
            System.out.println("║ " + Color.BOLD_PURPLE + "5. Create Account                  " + Color.BOLD_BLUE + "║");
            System.out.println("║ " + Color.BOLD_GREEN + "6. Close Account                   " + Color.BOLD_BLUE + "║");
            System.out.println("║ " + Color.BOLD_YELLOW + "7. Check Balance                   " + Color.BOLD_BLUE + "║");
            System.out.println("║ " + Color.BOLD_GREEN + "8. Re-Open Account                 " + Color.BOLD_BLUE + "║");
            System.out.println("║ " + Color.BOLD_PURPLE + "9. Back                            " + Color.BOLD_BLUE + "║");
            System.out.println("║ " + Color.RED + "10. Quit                           " + Color.BOLD_BLUE + "║");
            System.out.println("╚════════════════════════════════════╝");
            try {
                System.out.print("✍\uFE0F " + Color.BOLD_PURPLE + "Enter choice: " + Color.RESET);
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                try {
                    switch (choice) {
                        case 1:
                            System.out.print("Enter Account ID : ");
                            String accountIdOfTheOwner = scanner.nextLine();
                            System.out.print("Enter password : ");
                            String passwordOfTheOwner = scanner.nextLine();

                            Account loggedInAccountOfTheOwner = accountService.login(accountIdOfTheOwner, passwordOfTheOwner);
                            if (loggedInAccountOfTheOwner == null) {
                                System.out.print("Invalid Account Id or Password");
                                break;
                            }

                            accountService.setCurrentAccount(loggedInAccountOfTheOwner);

                            accountService.printTransactionHistory();
                            break;
                        case 2:
                            System.out.print("Enter Account ID : ");
                            String accountId = scanner.nextLine();
                            System.out.print("Enter password : ");
                            String password = scanner.nextLine();

                            Account loggedInAccount = accountService.login(accountId, password);
                            if (loggedInAccount == null) {
                                System.out.print("Invalid Account Id or Password");
                                break;
                            }

                            accountService.setCurrentAccount(loggedInAccount);
                            System.out.print("Enter amount to withdraw: ");
                            double withdrawAmount = scanner.nextDouble();
                            accountService.withdraw(withdrawAmount);
                            break;
                        case 3:
                            System.out.print("Enter Account ID : ");
                            String accountIdOwner = scanner.nextLine();
                            System.out.print("Enter password : ");
                            String passwordOfOwner = scanner.nextLine();

                            Account loggedInAccountOfOwner = accountService.login(accountIdOwner, passwordOfOwner);
                            if (loggedInAccountOfOwner == null) {
                                System.out.print("Invalid Account Id or Password");
                                break;
                            }

                            accountService.setCurrentAccount(loggedInAccountOfOwner);
                            System.out.print("Enter amount to deposit: ");
                            double depositAmount = scanner.nextDouble();
                            accountService.deposit(depositAmount);
                            break;
                        case 4:

                            System.out.print("Enter Account ID : ");
                            String accountIdOfOwnerForTransfer = scanner.nextLine();
                            System.out.print("Enter password : ");
                            String passwordOfOwnerForTransfer = scanner.nextLine();

                            Account loggedInAccountOfOwnerForTransfer = accountService.login(accountIdOfOwnerForTransfer, passwordOfOwnerForTransfer);
                            if (loggedInAccountOfOwnerForTransfer == null) {
                                System.out.print("Invalid Account Id or Password");
                                break;
                            }

                            accountService.setCurrentAccount(loggedInAccountOfOwnerForTransfer);

                            System.out.print("Enter destination Account ID: ");
                            String toUserId = scanner.nextLine();
                            System.out.print("Enter amount to transfer: ");
                            double transferAmount = scanner.nextDouble();
                            accountService.transfer(toUserId, transferAmount);
                            break;

                        case 5:
                            accountService.createAccount();
                            System.out.print("Account created successfully.");
                            break;
                        case 6:
                            System.out.print("Enter Account ID to close: ");
                            String closeAccountId = scanner.nextLine();
                            accountService.closeAccount(closeAccountId);
                            break;
                        case 7:

                            System.out.print("Enter Account ID : ");
                            String accountIdOfTheOwnerToCheckTotalBalance = scanner.nextLine();
                            System.out.print("Enter password : ");
                            String passwordOfOwnerToCheckTotalBalance = scanner.nextLine();

                            Account loggedInAccountOfTheOwnerToCheckBalance = accountService.login(accountIdOfTheOwnerToCheckTotalBalance, passwordOfOwnerToCheckTotalBalance);
                            if (loggedInAccountOfTheOwnerToCheckBalance == null) {
                                System.out.print("Invalid Account Id or Password");
                                break;
                            }

                            accountService.setCurrentAccount(loggedInAccountOfTheOwnerToCheckBalance);

                            accountService.printTotalAmount();
                            break;
                        case 8:
                            System.out.print("Enter Account Id to Re-Open : ");
                            String reOpenAccountId = scanner.nextLine();
                            accountService.openAccount(reOpenAccountId);
                            break;
                        case 9:
                            ATMInterfaceApplication.printInitialMenuForAdminLoggedIn();
                            break;
                        case 10:
                            accountService.quit();
                            System.exit(0);
                        default:
                            System.out.print("Invalid choice.");
                            scanner.nextLine();
                    }
                } catch (ATMException e) {
                    System.out.print(Color.RED + e.getMessage() + Color.RESET);
                    System.out.println();
                }
            } catch (InputMismatchException e) {
                System.out.print(Color.RED + "Invalid choice." + Color.RESET);
                System.out.println();
                scanner.nextLine(); // Consume invalid input
            }
        }
    }

    private static void showATMMenuForUser() {
        while (true) {
            printWelcomeMessage();
            System.out.println(Color.BOLD_BLUE + "\n╔════════════════════════════════════╗");
            System.out.println("║         " + Color.BOLD_GREEN + "User Menu" + Color.BOLD_BLUE + "                  ║");
            System.out.println("╠════════════════════════════════════╣");
            System.out.println("║ " + Color.BOLD_CYAN + "1. Transactions History            " + Color.BOLD_BLUE + "║");
            System.out.println("║ " + Color.BOLD_GREEN + "2. Withdraw                        " + Color.BOLD_BLUE + "║");
            System.out.println("║ " + Color.BOLD_YELLOW + "3. Deposit                         " + Color.BOLD_BLUE + "║");
            System.out.println("║ " + Color.BOLD_GREEN + "4. Transfer                        " + Color.BOLD_BLUE + "║");
            System.out.println("║ " + Color.BOLD_CYAN + "5. Check Balance                   " + Color.BOLD_BLUE + "║");
            System.out.println("║ " + Color.BOLD_PURPLE + "6. Back                            " + Color.BOLD_BLUE + "║");
            System.out.println("║ " + Color.RED + "7. Quit                            " + Color.BOLD_BLUE + "║");
            System.out.println("╚════════════════════════════════════╝" + Color.RESET);


            try {
                System.out.print("✍\uFE0F " + Color.BOLD_PURPLE + "Enter choice: " + Color.RESET);
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                try {
                    switch (choice) {
                        case 1:
                            accountService.printTransactionHistory();
                            break;
                        case 2:
                            System.out.print("Enter amount to withdraw: ");
                            double withdrawAmount = scanner.nextDouble();
                            accountService.withdraw(withdrawAmount);
                            break;
                        case 3:
                            System.out.print("Enter amount to deposit: ");
                            double depositAmount = scanner.nextDouble();
                            accountService.deposit(depositAmount);
                            break;
                        case 4:
                            System.out.print("Enter destination Account ID: ");
                            String toUserId = scanner.nextLine();
                            System.out.print("Enter amount to transfer: ");
                            double transferAmount = scanner.nextDouble();
                            accountService.transfer(toUserId, transferAmount);
                            break;

                        case 5:
                            accountService.printTotalAmount();
                            break;
                        case 6:
                            ATMInterfaceApplication.printInitialMenuForUserLoggedIn();
                            break;
                        case 7:
                            accountService.quit();
                            System.exit(0);
                        default:
                            System.out.print("Invalid choice.");
                    }
                } catch (ATMException e) {
                    System.out.print(e.getMessage());
                }
            } catch (InputMismatchException e) {
                System.out.print("Invalid choice.");
                scanner.nextLine();
                System.out.println();
            }
        }
    }

}