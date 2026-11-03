
package ui;

import java.util.List;
import java.util.Scanner;

public class InputHandler {

    private final Scanner scanner;

    public InputHandler(Scanner scanner) {
        if (scanner == null) {
            throw new IllegalArgumentException(
                    "Scanner cannot be null.");
        }

        this.scanner = scanner;
    }

    // Read an integer from the user
    public int readInt(String prompt) {
        while (true) {
            System.out.print(
                    AnsiColors.paint(prompt, AnsiColors.CYAN));

            String input = scanner.nextLine().trim();

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException ex) {
                System.out.println(
                        AnsiColors.paint(
                                "Invalid input. Please enter a whole number.",
                                AnsiColors.RED));
            }
        }
    }

    // -------------------------------------------------
    // HAPPY HOUR
    // -------------------------------------------------

    // Let the player activate Happy Hour or save it
    public int readHappyHourChoice() {
        heading("HAPPY HOUR (START OF TURN)");

        System.out.println(
                "  1. Activate now (this turn and the next)");

        System.out.println(
                "  2. Save for a later turn");

        return readMenuChoice(2, "Happy Hour");
    }

    // -------------------------------------------------
    // HOST ACTION
    // -------------------------------------------------

    // Display the customers that the Host can seat
    public int readHostChoice(List<Integer> seatIds) {
        heading("HOST ACTION");

        System.out.println(
                "  1. Seat one waiting customer"
                + eligibleText(seatIds));

        System.out.println("  2. Wait");

        return readMenuChoice(2, "Host");
    }

    // -------------------------------------------------
    // WAITER ACTION
    // -------------------------------------------------

    // Display eligible customers and orders for each task
    public int readWaiterChoice(
            List<Integer> seatIds,
            String seatReason,
            List<Integer> takeOrderIds,
            List<Integer> serveIds) {

        heading("WAITER ACTION");

        // Show which customers can be seated
        if (seatIds.isEmpty()) {
            System.out.println(
                    "  1. Seat customer (Unavailable: "
                    + seatReason + ")");
        } else {
            System.out.println(
                    "  1. Seat customer"
                    + eligibleText(seatIds));
        }

        // Show which customers can place an order
        System.out.println(
                "  2. Take order"
                + eligibleText(takeOrderIds));

        // Show which orders can be served
        System.out.println(
                "  3. Serve order"
                + eligibleText(serveIds));

        System.out.println("  4. Wait");

        // Viewing history does not use a Waiter action
        System.out.println(
                "  5. View order history"
                + " (does not consume a turn)");

        return readMenuChoice(5, "Waiter");
    }

    // -------------------------------------------------
    // CHEF ACTION
    // -------------------------------------------------

    // Display which orders can be prepared
    public int readChefChoice(List<Integer> prepareIds) {
        heading("CHEF ACTION");

        System.out.println(
                "  1. Prepare order"
                + eligibleText(prepareIds));

        System.out.println("  2. Wait");

        return readMenuChoice(2, "Chef");
    }

    // -------------------------------------------------
    // TARGET SELECTION
    // -------------------------------------------------

    // Let the player select an eligible customer or order
    public int readTargetId(
            String targetName, List<Integer> eligibleIds) {

        if (eligibleIds == null || eligibleIds.isEmpty()) {
            throw new IllegalArgumentException(
                    "No eligible " + targetName + " is available.");
        }

        // Automatically select when only one target is eligible
        if (eligibleIds.size() == 1) {
            return eligibleIds.get(0);
        }

        int id = readInt(
                "Enter " + targetName + " ID: ");

        // Reject IDs that are not currently eligible
        if (!eligibleIds.contains(id)) {
            throw new IllegalArgumentException(
                    targetName + " #" + id
                    + " cannot be selected right now.");
        }

        return id;
    }

    // -------------------------------------------------
    // HELPER METHODS
    // -------------------------------------------------

    // Display available IDs or indicate that a task is unavailable
    private String eligibleText(List<Integer> eligibleIds) {

        if (eligibleIds == null || eligibleIds.isEmpty()) {
            return " (Unavailable)";
        }

        return " (Eligible IDs: " + eligibleIds + ")";
    }

    // Make sure the user chooses a valid menu option
    private int readMenuChoice(int max, String staffName) {

        while (true) {
            int choice = readInt(
                    "Choose a " + staffName
                    + " action (1-" + max + "): ");

            if (choice >= 1 && choice <= max) {
                return choice;
            }

            System.out.println(
                    AnsiColors.paint(
                            "Invalid option. Choose a number from 1 to "
                            + max + ".",
                            AnsiColors.RED));
        }
    }

    // Display a menu heading
    private void heading(String title) {

        System.out.println();

        System.out.println(
                AnsiColors.paint(
                        "========== " + title + " ==========",
                        AnsiColors.CYAN + AnsiColors.BOLD));
    }
}
