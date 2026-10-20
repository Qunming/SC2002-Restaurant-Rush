package ui;

import java.util.Scanner;

/** Reads player input only; staff classes enforce game rules. */
public class InputHandler {
    private final Scanner scanner;

    public InputHandler(Scanner scanner) {
        if (scanner == null) {
            throw new IllegalArgumentException("Scanner cannot be null.");
        }
        this.scanner = scanner;
    }

    /** Retry invalid text without losing an action. */
    public int readInt(String prompt) {
        while (true) {
            System.out.print(AnsiColors.paint(prompt, AnsiColors.CYAN));
            String text = scanner.nextLine().trim();
            try {
                return Integer.parseInt(text);
            } catch (NumberFormatException ex) {
                System.out.println(AnsiColors.paint(
                        "  Please enter a whole number.", AnsiColors.RED));
            }
        }
    }

    public int readWaiterChoice() {
        System.out.println();
        System.out.println(AnsiColors.paint(
                "[WAITER ACTION]", AnsiColors.CYAN + AnsiColors.BOLD));
        System.out.println("  1. Seat a waiting customer");
        System.out.println("  2. Take a seated customer's order");
        System.out.println("  3. Serve a READY order");
        System.out.println("  4. Wait");
        System.out.println("  5. View order history (FREE - no turn used)");
        return readInt("Choose a Waiter action (1-5): ");
    }

    public int readChefChoice() {
        System.out.println();
        System.out.println(AnsiColors.paint(
                "[CHEF ACTION]", AnsiColors.CYAN + AnsiColors.BOLD));
        System.out.println("  1. Prepare one unit of an order");
        System.out.println("  2. Wait");
        return readInt("Choose a Chef action (1-2): ");
    }

    public int readCustomerId() {
        return readInt("Enter customer ID: ");
    }

    public int readOrderId() {
        return readInt("Enter order ID: ");
    }
}
