package order;

import customer.Customer;
import menu.MenuItem;

/** Member 3: One selected food item and its cooking/payment progress. */
public class Order {
    private final int id;
    private final Customer customer;
    private final MenuItem menuItem;
    private final int priceCents;
    private int completedPreparationUnits = 0;
    private OrderStatus status = OrderStatus.PLACED;
    private int readyTurn = -1;
    private int servedTurn = -1;

    public Order(int id, Customer customer, MenuItem menuItem) {
        this.id = id;
        this.customer = customer;
        this.menuItem = menuItem;
        // Stage 1 uses full menu price; the bill is fixed at order time.
        this.priceCents = menuItem.getPriceCents();
    }

    public int getId() { return id; }
    public Customer getCustomer() { return customer; }
    public MenuItem getItem() { return menuItem; }
    public int getFinalPriceCents() { return priceCents; }
    public OrderStatus getStatus() { return status; }
    public int getReadyTurn() { return readyTurn; }
    public int getServedTurn() { return servedTurn; }

    public int getRemainingPreparationUnits() {
        // TODO M3: Total required minus completed.
        throw new UnsupportedOperationException("TODO M3: remaining cooking units");
    }

    public void prepareOneUnit(int currentTurn) {
        // TODO M3: PLACED/PREPARING -> READY when final unit is complete.
        throw new UnsupportedOperationException("TODO M3: cook one unit");
    }

    public void markServed(int currentTurn) {
        // TODO M3: Only READY from a PREVIOUS turn -> SERVED.
        throw new UnsupportedOperationException("TODO M3: serve order");
    }

    public void markPaid() {
        // TODO M3: SERVED -> PAID after the cashier collects payment.
        throw new UnsupportedOperationException("TODO M3: mark order paid");
    }

    public void cancel() {
        // TODO M3: Cancel only unpaid orders for abandoned customers.
        throw new UnsupportedOperationException("TODO M3: cancel unpaid order");
    }
}
