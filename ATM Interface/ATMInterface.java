import java.util.HashMap;
import java.util.Map; //used to give view interface by using key and value
import java.util.Scanner; //used to take user input 

class Account {
    private String userId;
    private String userPin;
    private double balance;
    private StringBuilder transactionHistory;

    public Account(String userId, String userPin, double initialBalance) { //constructor
        this.userId = userId;
        this.userPin = userPin;
        this.balance = initialBalance;
        this.transactionHistory = new StringBuilder();
    }

    public String getUserId() { //getter method
        return userId;
    }

    public boolean verifyPin(String userPin) {
        return this.userPin.equals(userPin);
    }

    public double getBalance() { //getter method
        return balance;
    }

    public void deposit(double amount) {
        balance += amount;
        transactionHistory.append(String.format("Deposited: $%.2f\n", amount));
    }

    public void withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            transactionHistory.append(String.format("Withdrawn: $%.2f\n", amount));
        } else {
            System.out.println("Insufficient balance or invalid amount.");
        }
    }

    public void transfer(Account recipient, double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            recipient.deposit(amount);
            transactionHistory.append(String.format("Transferred: $%.2f to %s\n", amount, recipient.getUserId()));
        } else {
            System.out.println("Insufficient balance or invalid amount.");
        }
    }

    public String getTransactionHistory() { //getter method
        return transactionHistory.toString();
    }
}

public class ATMInterface { //main class
    private Map<String, Account> accounts;
    private Account currentUser;
    private Scanner scanner;

    public ATMInterface() {
        this.accounts = new HashMap<>();
        //we have set sample accounts and pin numbers
        accounts.put("user1", new Account("user1", "1234", 1000.0));
        accounts.put("user2", new Account("user2", "5678", 500.0));
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("Welcome to the ATM!");

        while (true) {
            System.out.print("Enter Your ID: ");
            String userId = scanner.nextLine();
            System.out.print("Enter Your PIN: ");
            String userPin = scanner.nextLine();

            if (authenticateUser(userId, userPin)) {  //if id and pin matches then it get to system otherwise shows invalid user
                showMenu();
                break;
            } else {
                System.out.println("Invalid User ID or PIN. Please try again.");
            }
        }
    }

    private boolean authenticateUser(String userId, String userPin) {
        Account account = accounts.get(userId);
        if (account != null && account.verifyPin(userPin)) {
            currentUser = account;
            return true;
        }
        return false;
    }

    private void showMenu() {
        System.out.println("Login Successful..!");
        while (true) {
            System.out.println("\nSelect an option:");
            System.out.println("1. Check Balance");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer Money");
            System.out.println("5. Transactions History");
            System.out.println("6. Quit");

            int choice = Integer.parseInt(scanner.nextLine()); //taking input choice from user

            switch (choice) { //uses switch cases for only one case the user can choice
                case 1:
                    System.out.println("Balance: $" + currentUser.getBalance());
                    break;
                case 2:
                    System.out.print("Enter the amount to deposit: $");
                    double depositAmount = Double.parseDouble(scanner.nextLine());
                    currentUser.deposit(depositAmount);
                    break;
                case 3:
                    System.out.print("Enter the amount to withdraw: $");
                    double withdrawAmount = Double.parseDouble(scanner.nextLine());
                    currentUser.withdraw(withdrawAmount);
                    break;
                case 4:
                    System.out.print("Enter the recipient's User ID: ");
                    String recipientId = scanner.nextLine();
                    System.out.print("Enter the amount to transfer: $");
                    double transferAmount = Double.parseDouble(scanner.nextLine());
                    Account recipient = accounts.get(recipientId);
                    if (recipient != null) {
                        currentUser.transfer(recipient, transferAmount);
                    } else {
                        System.out.println("Recipient User ID not found.");
                    }
                    break;
                case 5:
                    System.out.println("Transaction History:\n" + currentUser.getTransactionHistory());
                    break;
                case 6:
                    System.out.println("Thank you for using the ATM. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    public static void main(String[] args) {
        ATMInterface atm = new ATMInterface(); //created the an object of ATMInteface class
        atm.start(); //calling the start method
    }
}