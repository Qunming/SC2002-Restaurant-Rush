package order;

import customer.Customer;
import menu.MenuItem;

// Stores an order's details and preparation progress
public class Order {

    private final int id;
    private final Customer customer;
    private final MenuItem menuItem;
    private final int priceCents;

    private int completedPreparationUnits = 0;
    private OrderStatus status = OrderStatus.PLACED;

    private int readyTurn = -1;
    private int servedTurn = -1;

    // Constructor
    public Order(int id, Customer customer, MenuItem menuItem) {
        if (id <= 0) {
            throw new IllegalArgumentException("Order ID must be positive.");
        }

        if (customer == null || menuItem == null) {
            throw new IllegalArgumentException(
                "Customer and menu item cannot be null.");
        }

        this.id = id;
        this.customer = customer;
        this.menuItem = menuItem;

        // Apply the customer's discount and fix the price
        int discountPercent = customer.getDiscountPercent();
        this.priceCents = (menuItem.getPriceCents()
                * (100 - discountPercent) + 50) / 100;
    }

    // Getters
    public int getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public MenuItem getItem() {
        return menuItem;
    }

    public int getFinalPriceCents() {
        return priceCents;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public int getReadyTurn() {
        return readyTurn;
    }

    public int getServedTurn() {
        return servedTurn;
    }

    // Calculate remaining cooking units
    public int getRemainingPreparationUnits() {
        return menuItem.getPreparationUnits() - completedPreparationUnits;
    }

    // Chef prepares one unit per action
    public void prepareOneUnit(int currentTurn) {
        if (currentTurn < 1) {
            throw new IllegalArgumentException(
                "Turn number must be positive.");
        }

        if (status != OrderStatus.PLACED
                && status != OrderStatus.PREPARING) {
            throw new IllegalStateException(
                "Only placed or preparing orders can be cooked.");
        }

        completedPreparationUnits++;

        // Check whether all cooking units are completed
        if (getRemainingPreparationUnits() == 0) {
            status = OrderStatus.READY;
            readyTurn = currentTurn;
        } else {
            status = OrderStatus.PREPARING;
        }
    }

    // Waiter serves the order
    public void markServed(int currentTurn) {
        if (currentTurn < 1) {
            throw new IllegalArgumentException(
                "Turn number must be positive.");
        }

        if (status != OrderStatus.READY) {
            throw new IllegalStateException(
                "Only ready orders can be served.");
        }

        // Cannot serve an order on the same turn it becomes READY
        if (currentTurn <= readyTurn) {
            throw new IllegalStateException(
                "Order must be ready from an earlier turn.");
        }

        status = OrderStatus.SERVED;
        servedTurn = currentTurn;
    }

    // Mark order as paid after Cashier processes payment
    public void markPaid() {
        if (status != OrderStatus.SERVED) {
            throw new IllegalStateException(
                "Only served orders can be paid.");
        }

        status = OrderStatus.PAID;
    }

    // Cancel an unserved order when customer abandons
    public void cancel() {
        if (status != OrderStatus.PLACED
                && status != OrderStatus.PREPARING
                && status != OrderStatus.READY) {
            throw new IllegalStateException(
                "Only unserved orders can be cancelled.");
        }

        status = OrderStatus.CANCELLED;
    }
}
```
