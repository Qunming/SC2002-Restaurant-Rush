
package ui;

import controller.GameState;
import customer.Customer;
import java.util.List;
import order.Order;
import restaurant.Table;
import staff.Chef;
import staff.Waiter;

/** Prints game information without performing restaurant actions. */
public class ConsoleUI {

    public void showStartOfTurn(GameState gameState, Waiter waiter, Chef chef) {
        System.out.println("\n========== TURN " + gameState.getCurrentTurn() + " ==========");
        showStatistics(gameState);

        System.out.println("\nWaiting queue:");
        List<Customer> waiting = gameState.getCustomerManager().getWaitingCustomers();
        if (waiting.isEmpty()) {
            System.out.println("  None");
        } else {
            for (Customer customer : waiting) {
                System.out.println("  Customer #" + customer.getId()
                        + " | Satisfaction: " + customer.getSatisfaction());
            }
        }

        System.out.println("\nTables:");
        for (Table table : gameState.getTableManager().getTables()) {
            Customer occupant = table.getOccupant();
            if (occupant == null) {
                System.out.println("  Table " + table.getNumber() + ": Available");
            } else {
                System.out.println("  Table " + table.getNumber()
                        + ": Customer #" + occupant.getId()
                        + " (" + occupant.getStatus() + ")");
            }
        }

        System.out.println("\nActive customers:");
        List<Customer> active = gameState.getCustomerManager().getActiveCustomers();
        if (active.isEmpty()) {
            System.out.println("  None");
        } else {
            for (Customer customer : active) {
                System.out.println("  Customer #" + customer.getId()
                        + " | " + customer.getStatus()
                        + " | Satisfaction: " + customer.getSatisfaction());
            }
        }

        System.out.println("\nOrders:");
        List<Order> orders = gameState.getOrderManager().getOrders();
        if (orders.isEmpty()) {
            System.out.println("  None");
        } else {
            for (Order order : orders) {
                System.out.println("  Order #" + order.getId()
                        + " | Customer #" + order.getCustomer().getId()
                        + " | " + order.getItem().getName()
                        + " | " + order.getStatus());
            }
        }

        // These are action types. The staff classes check whether an action is valid.
        System.out.println("\nStaff action choices (subject to eligibility):");
        System.out.println("  Waiter: " + (waiter.hasActedThisTurn()
                ? "Already acted" : "Seat / Take Order / Serve / Wait"));
        System.out.println("  Chef: " + (chef.hasActedThisTurn()
                ? "Already acted" : "Prepare / Wait"));
        System.out.println("  Cashier: Automatic payment (maximum 1 per turn)");
    }

    public void showEndOfTurn(GameState gameState) {
        System.out.println("\n----- END OF TURN " + gameState.getCurrentTurn() + " -----");
        System.out.println("Turns used: " + gameState.getCurrentTurn());
        showStatistics(gameState);
        System.out.println("Served customers: " + gameState.getServedCustomerCount());
        System.out.println("Abandoned customers: " + gameState.getAbandonedCustomerCount());
    }

    public void showMessage(String message) {
        System.out.println(message);
    }

    public void showError(String errorMessage) {
        System.out.println("Invalid action: " + errorMessage);
    }

    public void showFinalResult(GameState gameState, boolean victory) {
        System.out.println("\n========== GAME OVER ==========");
        System.out.println(victory ? "VICTORY!" : "DEFEAT!");
        System.out.println("Turns used: " + gameState.getCurrentTurn());
        showStatistics(gameState);
        System.out.println("Served customers: " + gameState.getServedCustomerCount());
        System.out.println("Abandoned customers: " + gameState.getAbandonedCustomerCount());
    }

    private void showStatistics(GameState gameState) {
        System.out.printf("Revenue: $%.2f%n", gameState.getRevenueCents() / 100.0);
        System.out.println("Paid customers: " + gameState.getPaidCustomerCount());

        if (gameState.getPaidCustomerCount() == 0) {
            System.out.println("Average paid satisfaction: N/A");
        } else {
            System.out.printf("Average paid satisfaction: %.1f%n",
                    gameState.getAveragePaidSatisfaction());
        }
    }
}
