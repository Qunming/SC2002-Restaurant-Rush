
package ui;

import java.util.Scanner;

/** Reads player input. Game rules are checked by other classes. */
public class InputHandler {
    private final Scanner scanner;

    public InputHandler(Scanner scanner) {
        if (scanner == null) {
            throw new IllegalArgumentException("Scanner cannot be null");
        }
        this.scanner = scanner;
    }

    /** Keep asking until the player enters a valid whole number. */
    public int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a whole number.");
            }
        }
    }

    public int readWaiterChoice() {
        System.out.println("\nWaiter actions:");
        System.out.println("1. Seat customer");
        System.out.println("2. Take order");
        System.out.println("3. Serve order");
        System.out.println("4. Wait");
        return readInt("Choose a Waiter action (1-4): ");
    }

    public int readChefChoice() {
        System.out.println("\nChef actions:");
        System.out.println("1. Prepare one unit of an order");
        System.out.println("2. Wait");
        return readInt("Choose a Chef action (1-2): ");
    }

    public int readCustomerId() {
        return readInt("Enter customer ID: ");
    }

    public int readOrderId() {
        return readInt("Enter order ID: ");
    }
}
