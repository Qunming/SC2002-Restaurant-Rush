package order;

import customer.Customer;
import menu.MenuItem;

public class Order {
    private final int id;
    private final Customer customer;
    private final MenuItem menuItem;
    private final int priceCents;

    private int completedPreparationUnits = 0;
    private OrderStatus status = OrderStatus.PLACED;

    private int readyTurn = -1;
    private int servedTurn = -1;

    // creates an order and records its price
    public Order(int id, Customer customer, MenuItem menuItem) {
        if (id <= 0) {
            throw new IllegalArgumentException(
                    "Order ID must be positive.");
        }
        if (customer == null || menuItem == null) {
            throw new IllegalArgumentException(
                    "Customer and menu item cannot be null.");
        }

        this.id = id;
        this.customer = customer;
        this.menuItem = menuItem;
        this.priceCents = menuItem.getPriceCents();
    }

    // getters
    // returns the order ID
    public int getId() {
        return id;
    }

    // returns the customer who placed the order
    public Customer getCustomer() {
        return customer;
    }

    // returns the ordered food item
    public MenuItem getItem() {
        return menuItem;
    }

    // returns the final price in cents
    public int getFinalPriceCents() {
        return priceCents;
    }

    // returns the current order status
    public OrderStatus getStatus() {
        return status;
    }

    // returns the turn when the order became ready
    public int getReadyTurn() {
        return readyTurn;
    }

    // returns the turn when the order was served
    public int getServedTurn() {
        return servedTurn;
    }

    // returns the remaining preparation units
    public int getRemainingPreparationUnits() {
        return Math.max(
                0,
                menuItem.getPreparationUnits() - completedPreparationUnits);
    }

    // completes one preparation unit
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

        if (getRemainingPreparationUnits() == 0) {
            status = OrderStatus.READY;
            readyTurn = currentTurn;
        } else {
            status = OrderStatus.PREPARING;
        }
    }

    // marks order as served
    public void markServed(int currentTurn) {
        if (status != OrderStatus.READY) {
            throw new IllegalStateException(
                    "Only ready orders can be served.");
        }

        if (currentTurn <= readyTurn) {
            throw new IllegalStateException(
                    "Order must be ready from an earlier turn.");
        }

        if (currentTurn < 1) {
            throw new IllegalArgumentException(
                    "Turn number must be positive.");
        }

        status = OrderStatus.SERVED;
        servedTurn = currentTurn;
    }

    // marks order as paid
    public void markPaid() {
        if (status != OrderStatus.SERVED) {
            throw new IllegalStateException(
                    "Only served orders can be paid.");
        }

        status = OrderStatus.PAID;
    }

    // cancels order that has not been served
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
