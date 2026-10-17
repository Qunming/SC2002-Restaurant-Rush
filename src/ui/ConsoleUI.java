package ui;

import controller.GameState;
import staff.Chef;
import staff.Waiter;

/** Member 1: Displays information. The UI never owns game rules. */
public class ConsoleUI {
    public void showStartOfTurn(GameState gameState, Waiter waiter, Chef chef) {
        // TODO M1: Turn, arrivals, queue, table occupancy, satisfaction,
        // orders, revenue, paid-customer satisfaction average, staff options.
        throw new UnsupportedOperationException("TODO M1: start-of-turn display");
    }

    public void showEndOfTurn(GameState gameState) {
        // TODO M1: Show served/abandoned/paid counts, revenue and satisfaction.
        throw new UnsupportedOperationException("TODO M1: end-of-turn display");
    }

    public void showMessage(String message) {
        System.out.println(message);
    }

    public void showError(String errorMessage) {
        System.out.println("Invalid choice: " + errorMessage);
    }

    public void showFinalResult(GameState gameState, GameController.haswon()) {
        // TODO M1: Print end of turn 18 result, VICTORY or DEFEAT.
        throw new UnsupportedOperationException("TODO M1: final results");
    }
}
