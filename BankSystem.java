import java.util.Scanner;

// User Defined Exception (fixed message)
class InsufficientBalanceException extends Exception {
    InsufficientBalanceException() {
        super("Balance is less than withdrawal amount");
    }
}

// Bank Account Class
class BankAccount {
    private int balance;

    // Constructor with initial balance
    BankAccount(int initialBalance) {
        this.balance = initialBalance;
    }

    // Deposit Method
    synchronized void deposit(int amount) {
        balance += amount;
        System.out.println("Deposited: " + amount);
        System.out.println("Balance after deposit: " + balance);
    }

    // Withdraw Method
    synchronized void withdraw(int amount) throws InsufficientBalanceException {
        if (amount > balance) {
            throw new InsufficientBalanceException();
        }
        balance -= amount;
        System.out.println("Withdrawn: " + amount);
        System.out.println("Balance after withdrawal: " + balance);
    }

    // Getter for balance
    synchronized int getBalance() {
        return balance;
    }
}

// Deposit Thread
class DepositThread extends Thread {
    BankAccount acc;
    int amount;

    DepositThread(BankAccount acc, int amount) {
        this.acc = acc;
        this.amount = amount;
    }

    public void run() {
        acc.deposit(amount);
    }
}

// Withdraw Thread
class WithdrawThread extends Thread {
    BankAccount acc;
    int amount;

    WithdrawThread(BankAccount acc, int amount) {
        this.acc = acc;
        this.amount = amount;
    }

    public void run() {
        try {
            acc.withdraw(amount);
        } catch (InsufficientBalanceException e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }
}

// Main Class
public class BankSystem {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        // Fixed initial balance known to user
        int initialBalance = 1000;
        BankAccount acc = new BankAccount(initialBalance);

        while (true) {
            System.out.println("\nCurrent Balance: " + acc.getBalance());
            System.out.println("Choose operation: 1. Deposit  2. Withdraw  3. Exit");
            int choice = sc.nextInt();

            if (choice == 1) {
                System.out.print("Enter deposit amount: ");
                int depositAmt = sc.nextInt();
                DepositThread t1 = new DepositThread(acc, depositAmt);
                t1.start();
                try {
                    t1.join(); // Wait for deposit to finish
                } catch (InterruptedException e) {
                    System.out.println("Thread interrupted");
                }
            } else if (choice == 2) {
                System.out.print("Enter withdraw amount: ");
                int withdrawAmt = sc.nextInt();
                WithdrawThread t2 = new WithdrawThread(acc, withdrawAmt);
                t2.start();
                try {
                    t2.join(); // Wait for withdraw to finish
                } catch (InterruptedException e) {
                    System.out.println("Thread interrupted");
                }
            } else if (choice == 3) {
                System.out.println("Exiting. Final Balance: " + acc.getBalance());
                break;
            } else {
                System.out.println("Invalid choice. Please select 1, 2, or 3.");
            }
        }

        sc.close();
    }
}