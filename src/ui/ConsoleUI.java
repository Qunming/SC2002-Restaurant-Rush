package ui;

import controller.GameState;
import staff.Chef;
import staff.Waiter;

/** Member 1: Displays information. The UI never owns game rules. */
public class ConsoleUI {
    public void showStartOfTurn(GameState gameState, Waiter waiter, Chef chef) {
        System.out.println("Current turn: " + getCurrentTurn());
        System.out.println("Revenue: $" + getRevenueCents());
        System.out.println("Average satisfaction of paid customers: " + getAveragePaidSatisfaction());
        System.out.println("Waiting queue: " + getWaitingCustomers);
        System.out.println("Occupied Tables: " + OccupiedTables);
        System.out.println("Order status: " + OrderStatus);
        System.out.println("Available staff tasks: " + AvailableStaffTasks);
    }

    public void showEndOfTurn(GameState gameState) {
        System.out.println("Turns used: " + getCurrentTurn());
        System.out.println("Revenue: $" + getRevenueCents());
        {System.out.println("Average satisfaction of paid customers :" + getAveragePaidSatisfaction());}
        System.out.println("Served Count: " + getServedCustomerCount());
        System.out.println("Unhappy departures: " + getAbandonedCustomerCount());
    }

    public void showMessage(String message) {
        System.out.println(message);
    }

    public void showError(String errorMessage) {
        System.out.println("Invalid choice: " + errorMessage);
    }

    public void showFinalResult(GameState gameState, boolean victory) {
        if (haswon()) {
            System.out.println("VICTORY!");
        } else {
            System.out.println("DEFEAT");
        }
}
