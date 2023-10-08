package User;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UserManagementSystem {
     private static final String FILE_NAME = "user.dat";
    private List<Account> userDatabase;

    public UserManagementSystem() {
        this.userDatabase = new ArrayList<>();
    }

    public void displayMenu() {
        System.out.println("\n====== USER MANAGEMENT SYSTEM ======");
        System.out.println("1. Create a new account");
        System.out.println("2. Login system");
        System.out.println("3. Exit");
        System.out.print(">Choose: ");
    }

    public void addAccount(Account acc) throws Exception {
        validateUsernameAndPassword(acc.getUsername(), acc.getPassword());

        // Load existing accounts from file into the program
        loadAccountsFromFile();

        // Check if the username already exists
        if (userExists(acc.getUsername())) {
            throw new Exception("Username already exists");
        }

        // Add the new account to the program
        userDatabase.add(acc);

        // Append the new account to the file
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME, true))) {
            oos.writeObject(acc);
        }

        System.out.println("Account added successfully");
    }

    public Account find(Account acc) throws Exception {
        validateUsernameAndPassword(acc.getUsername(), acc.getPassword());

        // Load existing accounts from file into the program
        loadAccountsFromFile();

        // Check if the account exists
        for (Account existingAccount : userDatabase) {
            if (existingAccount.getUsername().equals(acc.getUsername())
                    && existingAccount.getPassword().equals(acc.getPassword())) {
                System.out.println("Login successful");
                return existingAccount;
            }
        }

        throw new Exception("Invalid username or password");
    }

    private void validateUsernameAndPassword(String username, String password) throws Exception {
        if (username.length() < 5 || username.contains(" ")) {
            throw new Exception("Username must be at least 5 characters and no spaces");
        }

        if (password.length() < 6 || password.contains(" ")) {
            throw new Exception("Password must be at least 6 characters and no spaces");
        }
    }

    private void loadAccountsFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            while (true) {
                Account acc = (Account) ois.readObject();
                userDatabase.add(acc);
            }
        } catch (EOFException ignored) {
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading accounts from file: " + e.getMessage());
        }
    }

    private boolean userExists(String username) {
        for (Account acc : userDatabase) {
            if (acc.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        UserManagementSystem system = new UserManagementSystem();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            system.displayMenu();
            int choice = scanner.nextInt();

            try {
                switch (choice) {
                    case 1:
                        System.out.print("Enter Username: ");
                        String username = scanner.next();
                        System.out.print("Enter Password: ");
                        String password = scanner.next();
                        system.addAccount(new Account(username, password));
                        break;

                    case 2:
                        System.out.print("Enter Username: ");
                        String loginUsername = scanner.next();
                        System.out.print("Enter Password: ");
                        String loginPassword = scanner.next();
                        system.find(new Account(loginUsername, loginPassword));
                        break;

                    case 3:
                        System.out.println("Exiting the program. Goodbye!");
                        scanner.close();
                        System.exit(0);

                    default:
                        System.out.println("Invalid choice. Please enter a number between 1 and 3.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
    
}
