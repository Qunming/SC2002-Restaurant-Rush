package ui;

import java.util.Scanner;

/** Member 1: Reads input only, never enforces restaurant business rules. */
public class InputHandler {
    private final Scanner scanner;

    public InputHandler(Scanner scanner) {
        this.scanner = scanner;
    }

    public int readInt(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid input. " + prompt);
            scanner.next(); // Clear the invalid input
        }
        int value = scanner.nextInt();
        scanner.nextLine(); // Consume leftover newline
        return value;
    }

    public int readWaiterChoice() {
        System.out.println("1. Seat one queued customer at a free table");
        System.out.println("2. Take one seated customer’s order");
        System.out.println("3. Serve one READY order");
        System.out.println("4. Wait");
        System.out.print("Enter your choice (1-4): ");

        while (!scanner.hasNextInt()) {
            System.out.print("Invalid input. Please enter a number between 1 and 4: ");
            scanner.next(); // Clear the invalid input
        }

        int choice = scanner.nextInt();
        scanner.nextLine(); 
        return waiterChoice;
    }

    public int readChefChoice() {
        // TODO M1: 1 Prepare Order, 2 Wait.
        System.out.println("1. One order to advance by one preparation unit");
        System.out.println("2. Wait");
        System.out.print("Enter your choice (1-2): ");

        while (!scanner.hasNextInt()) {
            System.out.print("Invalid input. Please enter 1 or 2: ");
            scanner.next(); // Clear the invalid input
        }

        int choice = scanner.nextInt();
        scanner.nextLine(); 
        return chefChoice;
    }

    public int readCustomerId() {
        // TODO M1: Ask for existing customer ID; staff handles validation.
        throw new UnsupportedOperationException("TODO M1: customer ID input");
    }

    public int readOrderId() {
        // TODO M1: Ask for an order ID; staff handles validation.
        throw new UnsupportedOperationException("TODO M1: order ID input");
    }
}
