
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

    // -1 means happy hour has not been used
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

    public double getAveragePaidSatisfaction() {
        if (paidCustomerCount == 0) {
            return 0.0;
        }
        return (double) totalPaidSatisfaction / paidCustomerCount;
    }

    public void advanceTurn() {
        currentTurn++;
    }

    public boolean canActivateHappyHour() {
        return happyHourStartTurn == -1
                && currentTurn >= 1
                && currentTurn <= GameConfig.MAX_TURNS;
    }

    public void activateHappyHour() {
        if (!canActivateHappyHour()) {
            throw new IllegalStateException(
                    "Happy Hour has already been used or the game has ended.");
        }
        happyHourStartTurn = currentTurn;
    }

    public boolean hasHappyHourBeenUsed() {
        return happyHourStartTurn != -1;
    }

    public int getHappyHourTurnsRemaining() {
        if (happyHourStartTurn == -1) {
            return 0;
        }
        return Math.max(0, happyHourStartTurn
                + GameConfig.HAPPY_HOUR_DURATION_TURNS - currentTurn);
    }

    public boolean isHappyHourActive() {
        return getHappyHourTurnsRemaining() > 0;
    }

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

        customer.markPaid();
        revenueCents += order.getFinalPriceCents();
        totalPaidSatisfaction += customer.getSatisfaction();
        paidCustomerCount++;
    }

    public void recordServing() {
        servedCustomerCount++;
    }

    public void recordAbandonment() {
        abandonedCustomerCount++;
    }
}
