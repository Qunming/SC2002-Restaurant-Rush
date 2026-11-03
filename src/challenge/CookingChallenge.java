
package challenge;

import java.util.Scanner;
import order.CookingQuality;

// Additional Feature: Cooking Timing Challenge
// Player must stop the timer close to the target time.
// The result determines the cooking quality of an order.

public class CookingChallenge {

    private static final long TARGET_TIME_NANOS =
            3_000_000_000L;

    private static final long PERFECT_TOLERANCE_NANOS =
            500_000_000L;

    private final Scanner scanner;

    // Use the same Scanner as the main game
    public CookingChallenge(Scanner scanner) {

        if (scanner == null) {
            throw new IllegalArgumentException(
                    "Scanner cannot be null.");
        }

        this.scanner = scanner;
    }

    // -------------------------------------------------
    // COOKING CHALLENGE
    // -------------------------------------------------

    // Run one timing challenge
    // Return PERFECT or NORMAL based on the player's timing
    public CookingQuality playChallenge() {

        System.out.println();
        System.out.println("========== COOKING CHALLENGE ==========");

        System.out.println(
                "Try to stop the timer as close to 3 seconds as possible.");

        System.out.println(
                "Stop within 0.5 seconds of the target for PERFECT cooking.");

        // Wait for the player to start
        System.out.println();
        System.out.print("Press ENTER to start the timer...");
        scanner.nextLine();

        System.out.println();
        System.out.println("TIMER STARTED!");

        System.out.print(
                "Press ENTER again when you think 3 seconds have passed...");

        // Measure the elapsed time using a monotonic clock
        long startTime = System.nanoTime();

        scanner.nextLine();

        long elapsedTime = System.nanoTime() - startTime;

        // Convert nanoseconds to seconds for display
        double elapsedSeconds = elapsedTime / 1_000_000_000.0;

        System.out.println();
        System.out.printf(
                "Your timing: %.2f seconds%n",
                elapsedSeconds);

        // Check the difference from the target time
        long difference = Math.abs(
                elapsedTime - TARGET_TIME_NANOS);

        if (difference <= PERFECT_TOLERANCE_NANOS) {

            System.out.println(
                    "PERFECT! The dish was cooked perfectly!");

            return CookingQuality.PERFECT;
        }

        System.out.println(
                "NORMAL! The dish was prepared successfully.");

        return CookingQuality.NORMAL;
    }
}
