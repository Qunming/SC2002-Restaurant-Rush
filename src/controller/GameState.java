package controller;

import customer.Customer;
import customer.CustomerManager;
import menu.Menu;
import order.Order;
import order.OrderManager;
import restaurant.TableManager;

/** Member 1: Holds managers and result statistics, not staff decisions. */
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

    public GameState(CustomerManager customerManager, TableManager tableManager,
                     OrderManager orderManager, Menu menu) {
        this.customerManager = customerManager;
        this.tableManager = tableManager;
        this.orderManager = orderManager;
        this.menu = menu;
    }

    public CustomerManager getCustomerManager() { return customerManager; }
    public TableManager getTableManager() { return tableManager; }
    public OrderManager getOrderManager() { return orderManager; }
    public Menu getMenu() { return menu; }
    public int getCurrentTurn() { return currentTurn; }
    public int getRevenueCents() { return revenueCents; }
    public int getPaidCustomerCount() { return paidCustomerCount; }
    public int getServedCustomerCount() { return servedCustomerCount; }
    public int getAbandonedCustomerCount() { return abandonedCustomerCount; }

    public double getAveragePaidSatisfaction() {
        // TODO M1: Return NaN if nobody paid; otherwise payment-time average.
        throw new UnsupportedOperationException("TODO M1: average paid satisfaction");
    }

    public void advanceTurn() {
        currentTurn++;
    }

    public void recordPayment(Customer customer, Order order) {
        // TODO M1: Add only successfully collected bill, exactly once.
        throw new UnsupportedOperationException("TODO M1: record payment");
    }

    public void recordServing() {
        // TODO M1: Increment successful serving count.
        throw new UnsupportedOperationException("TODO M1: record serving");
    }

    public void recordAbandonment() {
        // TODO M1: Increment abandoned customer count.
        throw new UnsupportedOperationException("TODO M1: record abandonment");
    }
}
