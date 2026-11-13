
package challenge;

import java.util.Locale;
import java.util.Scanner;

import menu.ComboMeal;
import menu.MenuItem;
import order.CookingQuality;

// Additional Feature: Cooking Timing Challenge
// Different dishes have different target times.
// The player must stop the timer near the target.

public class CookingChallenge {

    private static final long NANOS_PER_SECOND =
            1_000_000_000L;

    private static final long PERFECT_TOLERANCE_NANOS =
            500_000_000L;

    private static final int COMBO_TIME_BONUS_SECONDS = 1;

    private final Scanner scanner;

    // Share the same Scanner with the main game
    public CookingChallenge(Scanner scanner) {

        if (scanner == null) {
            throw new IllegalArgumentException(
                    "Scanner cannot be null.");
        }

        this.scanner = scanner;
    }

    // -------------------------------------------------
    // DETERMINE TARGET TIME
    // -------------------------------------------------

    // Return the target time based on the menu item
    public int getTargetSeconds(MenuItem item) {

        if (item == null) {
            throw new IllegalArgumentException(
                    "Menu item cannot be null.");
        }

        // Calculate combo timing from its two dishes
        if (item instanceof ComboMeal) {

            ComboMeal combo = (ComboMeal) item;

            int firstTime =
                    getTargetSeconds(combo.getFirstItem());

            int secondTime =
                    getTargetSeconds(combo.getSecondItem());

            // Combos receive a 1-second timing reduction
            return Math.max(
                    1,
                    firstTime + secondTime
                    - COMBO_TIME_BONUS_SECONDS);
        }

        // Individual dish target times
        switch (item.getCode().toUpperCase(Locale.ROOT)) {

            case "SALAD":
                return 2;

            case "BURGER":
                return 3;

            case "PASTA":
                return 5;

            default:
                throw new IllegalArgumentException(
                        "No cooking challenge time for: "
                        + item.getName());
        }
    }

    // -------------------------------------------------
    // COOKING CHALLENGE
    // -------------------------------------------------

    // Run the timing challenge for the selected dish
    public CookingQuality playChallenge(MenuItem item) {

        int targetSeconds = getTargetSeconds(item);

        long targetTimeNanos =
                targetSeconds * NANOS_PER_SECOND;

        double minimumTime = targetSeconds - 0.5;
        double maximumTime = targetSeconds + 0.5;

        System.out.println();
        System.out.println(
                "========== COOKING CHALLENGE ==========");

        System.out.println(
                "Dish: " + item.getName());

        System.out.println(
                "Target time: " + targetSeconds + " seconds");

        System.out.printf(
                "PERFECT range: %.2f to %.2f seconds%n",
                minimumTime, maximumTime);

        System.out.println();
        System.out.println(
                "Stop the timer as close to the target as possible.");

        // Wait for the player to start
        System.out.println();
        System.out.print(
                "Press ENTER to start the timer...");

        scanner.nextLine();

        System.out.println();
        System.out.println("TIMER STARTED!");

        System.out.print(
                "Press ENTER again to stop the timer...");

        // Measure elapsed time
        long startTime = System.nanoTime();

        scanner.nextLine();

        long elapsedTime =
                System.nanoTime() - startTime;

        // Convert nanoseconds to seconds
        double elapsedSeconds =
                elapsedTime / (double) NANOS_PER_SECOND;

        System.out.println();

        System.out.printf(
                "Your timing: %.2f seconds%n",
                elapsedSeconds);

        // Determine how close the player was to the target
        long difference =
                Math.abs(elapsedTime - targetTimeNanos);

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
