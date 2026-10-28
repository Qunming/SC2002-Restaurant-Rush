
package customer;

import java.util.List;
import menu.Menu;
import menu.MenuItem;
import order.Order;
import restaurant.Table;

/** Stores one customer's status, satisfaction, table and order. */
public class Customer {
    private static final int STARTING_SATISFACTION = 100;
    private static final int WAITING_SATISFACTION_LOSS = 8;

    private final int id;
    private int satisfaction = STARTING_SATISFACTION;
    private CustomerStatus status = CustomerStatus.WAITING;

    private Table assignedTable;
    private Order order;
    private int servedTurn = -1;
    private int readyToPayTurn = -1;

    public Customer(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Customer ID must be positive.");
        }
        this.id = id;
    }

    public int getId() { return id; }
    public int getSatisfaction() { return satisfaction; }
    public CustomerStatus getStatus() { return status; }
    public Table getTable() { return assignedTable; }
    public Order getOrder() { return order; }
    public int getServedTurn() { return servedTurn; }
    public int getReadyToPayTurn() { return readyToPayTurn; }

    // Stage 2: Customer type (subclasses can override this).
    public String getCustomerType() {
        return "Regular";
    }

    // Stage 2: Different customer types can lose satisfaction differently.
    public int getWaitingSatisfactionLoss() {
        return WAITING_SATISFACTION_LOSS;
    }

    // Stage 2: Regular customers have no discount.
    public int getDiscountPercent() {
        return 0;
    }

    // Food is chosen by the customer, not by the player.
    public MenuItem chooseMenuItem(Menu menu) {
        if (menu == null) {
            throw new IllegalArgumentException("Menu cannot be null.");
        }

        List<MenuItem> items = menu.getItems();
        if (items.isEmpty()) {
            throw new IllegalStateException("Menu has no items.");
        }
        return items.get((id - 1) % items.size());
    }

    public void seatAt(Table table) {
        if (status != CustomerStatus.WAITING || table == null || assignedTable != null) {
            throw new IllegalArgumentException("Customer cannot be seated.");
        }
        assignedTable = table;
        status = CustomerStatus.SEATED;
    }

    public void assignOrder(Order newOrder) {
        if (status != CustomerStatus.SEATED || newOrder == null
                || newOrder.getCustomer() != this || order != null) {
            throw new IllegalArgumentException("Cannot assign order to customer.");
        }
        order = newOrder;
        status = CustomerStatus.ORDERED;
    }

    public void markServed(int currentTurn) {
        if (status != CustomerStatus.ORDERED || currentTurn < 1) {
            throw new IllegalArgumentException("Customer cannot be served.");
        }
        status = CustomerStatus.SERVED;
        servedTurn = currentTurn;
    }

    public void markReadyToPay(int currentTurn) {
        if (status != CustomerStatus.SERVED || currentTurn <= servedTurn) {
            throw new IllegalArgumentException("Customer has not finished eating.");
        }
        status = CustomerStatus.READY_TO_PAY;
        readyToPayTurn = currentTurn;
    }

    public void markPaid() {
        if (status != CustomerStatus.READY_TO_PAY) {
            throw new IllegalArgumentException("Customer is not ready to pay.");
        }
        status = CustomerStatus.PAID;
    }

    public void abandon() {
        if (!isAwaitingService() || satisfaction != 0) {
            throw new IllegalArgumentException("Customer cannot abandon.");
        }
        status = CustomerStatus.ABANDONED;
    }

    public void decreaseWaitingSatisfaction() {
        if (isAwaitingService()) {
            // Stage 2: Use the customer's own satisfaction loss.
            satisfaction = Math.max(0, satisfaction - getWaitingSatisfactionLoss());
        }
    }

    // Called by TableManager after the table has been released.
    public void clearTable() {
        if (status != CustomerStatus.PAID && status != CustomerStatus.ABANDONED) {
            throw new IllegalStateException("Customer has not left the restaurant.");
        }
        assignedTable = null;
    }

    private boolean isAwaitingService() {
        return status == CustomerStatus.WAITING
                || status == CustomerStatus.SEATED
                || status == CustomerStatus.ORDERED;
    }
}
