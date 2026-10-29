
package controller;

import customer.Customer;
import customer.CustomerManager;
import customer.CustomerStatus;
import menu.Menu;
import order.Order;
import order.OrderManager;
import order.OrderStatus;
import restaurant.TableManager;
import java.util.Objects;

// Stores the current turn, shared managers, and game statistics.
public class GameState {
    private final CustomerManager customerManager;
    private final TableManager tableManager;
    private final OrderManager orderManager;
    private final Menu menu;

    private int currentTurn = 1;
    private int revenueCents = 0;
    private int paidCustomerCount = 0;
    private int totalPaidSatisfaction = 0;
    private int servedCustomerCount = 0;
    private int abandonedCustomerCount = 0;

    // Stage 3: -1 means Happy Hour has never been activated.
    // Keeping the activation turn also determines the two active turns.
    private int happyHourStartTurn = -1;

    public GameState(CustomerManager customerManager,
                     TableManager tableManager,
                     OrderManager orderManager,
                     Menu menu) {
        this.customerManager = Objects.requireNonNull(customerManager);
        this.tableManager = Objects.requireNonNull(tableManager);
        this.orderManager = Objects.requireNonNull(orderManager);
        this.menu = Objects.requireNonNull(menu);
    }

    public CustomerManager getCustomerManager() {
        return customerManager;
    }

    public TableManager getTableManager() {
        return tableManager;
    }

    public OrderManager getOrderManager() {
        return orderManager;
    }

    public Menu getMenu() {
        return menu;
    }

    public int getCurrentTurn() {
        return currentTurn;
    }

    public int getRevenueCents() {
        return revenueCents;
    }

    public int getPaidCustomerCount() {
        return paidCustomerCount;
    }

    public int getServedCustomerCount() {
        return servedCustomerCount;
    }

    public int getAbandonedCustomerCount() {
        return abandonedCustomerCount;
    }

    // Calculate the average satisfaction of paid customers.
    public double getAveragePaidSatisfaction() {
        if (paidCustomerCount == 0) {
            return 0.0;
        }
        return (double) totalPaidSatisfaction / paidCustomerCount;
    }

    // Move to the next turn.
    public void advanceTurn() {
        currentTurn++;
    }

    // Stage 3: The manager can activate once, at the beginning of a turn.
    public boolean canActivateHappyHour() {
        return happyHourStartTurn == -1
                && currentTurn >= 1
                && currentTurn <= GameConfig.MAX_TURNS;
    }

    // Stage 3: Activate Happy Hour for the current and next turn.
    public void activateHappyHour() {
        if (!canActivateHappyHour()) {
            throw new IllegalStateException(
                    "Happy Hour has already been used or the game has ended.");
        }
        happyHourStartTurn = currentTurn;
    }

    // Stage 3: Check whether Happy Hour was previously activated.
    public boolean hasHappyHourBeenUsed() {
        return happyHourStartTurn != -1;
    }

    // Stage 3: Two turns on activation, one on the next, then zero.
    public int getHappyHourTurnsRemaining() {
        if (happyHourStartTurn == -1) {
            return 0;
        }
        return Math.max(0, happyHourStartTurn
                + GameConfig.HAPPY_HOUR_DURATION_TURNS - currentTurn);
    }

    // Stage 3: Check whether the promotion is currently active.
    public boolean isHappyHourActive() {
        return getHappyHourTurnsRemaining() > 0;
    }

    // Record a customer's payment (original Stage 2 rules).
    public void recordPayment(Customer customer, Order order) {
        if (customer == null || order == null) {
            throw new IllegalArgumentException(
                    "Customer and order cannot be null.");
        }

        if (customer.getOrder() != order
                || order.getCustomer() != customer) {
            throw new IllegalArgumentException(
                    "Order does not belong to this customer.");
        }

        if (customer.getStatus() != CustomerStatus.READY_TO_PAY
                || order.getStatus() != OrderStatus.SERVED) {
            throw new IllegalStateException(
                    "Customer and order are not ready for payment.");
        }

        // Mark the customer as paid.
        customer.markPaid();

        // Add the locked order price to the revenue.
        revenueCents += order.getFinalPriceCents();

        // Record satisfaction when the customer pays.
        totalPaidSatisfaction += customer.getSatisfaction();
        paidCustomerCount++;
    }

    // Record a customer being served.
    public void recordServing() {
        servedCustomerCount++;
    }

    // Record a customer leaving without paying.
    public void recordAbandonment() {
        abandonedCustomerCount++;
    }
}
