
package ui;

import java.util.List;
import java.util.Scanner;

/**
 * Handles game menus and player input.
 * All actions stay visible; the controller checks whether they are valid.
 */
public class InputHandler {
    private final Scanner scanner;

    public InputHandler(Scanner scanner) {
        if (scanner == null) {
            throw new IllegalArgumentException("Scanner cannot be null.");
        }
        this.scanner = scanner;
    }

    public int readInt(String prompt) {
        while (true) {
            System.out.print(AnsiColors.paint(prompt, AnsiColors.CYAN));
            String input = scanner.nextLine().trim();

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException ex) {
                System.out.println(AnsiColors.paint(
                        "Invalid input. Please enter a whole number.",
                        AnsiColors.RED));
            }
        }
    }

    // Displays all Waiter actions without availability hints.
    public int readWaiterChoice(List<Integer> seatIds, String seatReason,
                                List<Integer> takeOrderIds, List<Integer> serveIds) {
        heading("WAITER ACTION");

        System.out.println("  1. Seat customer");
        System.out.println("  2. Take order");
        System.out.println("  3. Serve order");
        System.out.println("  4. Wait");
        System.out.println("  5. View order history (does not consume a turn)");

        return readMenuChoice(5, "Waiter");
    }

    // Displays all Chef actions without availability hints.
    public int readChefChoice(List<Integer> prepareIds) {
        heading("CHEF ACTION");

        System.out.println("  1. Prepare order");
        System.out.println("  2. Wait");

        return readMenuChoice(2, "Chef");
    }


    public int readTargetId(String targetName, List<Integer> eligibleIds) {
        if (eligibleIds == null || eligibleIds.isEmpty()) {
            throw new IllegalArgumentException(
                    "No eligible " + targetName + " is available.");
        }

        if (eligibleIds.size() == 1) {
            return eligibleIds.get(0);
        }

        int id = readInt("Enter " + targetName + " ID: ");

        if (!eligibleIds.contains(id)) {
            throw new IllegalArgumentException(
                    targetName + " #" + id
                    + " cannot be selected right now.");
        }

        return id;
    }

    private int readMenuChoice(int max, String staffName) {
        while (true) {
            int choice = readInt("Choose a " + staffName
                    + " action (1-" + max + "): ");

            if (choice >= 1 && choice <= max) {
                return choice;
            }

            System.out.println(AnsiColors.paint(
                    "Invalid option. Choose a number from 1 to " + max + ".",
                    AnsiColors.RED));
        }
    }

    private void heading(String title) {
        System.out.println();

        System.out.println(AnsiColors.paint(
                "========== " + title + " ==========",
                AnsiColors.CYAN + AnsiColors.BOLD));
    }
}
