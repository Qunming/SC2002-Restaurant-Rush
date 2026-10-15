package customer;

import menu.Menu;
import menu.MenuItem;
import order.Order;
import restaurant.Table;

/**
 * Member 2: Concrete customer for Stage 1.
 * Every Stage 1 customer has the SAME satisfaction rule.
 */
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
            throw new IllegalArgumentException("Customer ID must be positive");
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

    public MenuItem chooseMenuItem(Menu menu) {
        // TODO M2: Use a documented ID-based rule.
        // Menu order is SALAD, BURGER, PASTA; same ID -> same food.
        throw new UnsupportedOperationException("TODO M2: choose menu item");
    }

    public void seatAt(Table table) {
        // TODO M2: WAITING -> SEATED; save assigned table.
        throw new UnsupportedOperationException("TODO M2: seat customer");
    }

    public void assignOrder(Order newOrder) {
        // TODO M2: SEATED -> ORDERED; reject an order for a different customer.
        throw new UnsupportedOperationException("TODO M2: assign order");
    }

    public void markServed(int currentTurn) {
        // TODO M2: ORDERED -> SERVED; save served turn.
        throw new UnsupportedOperationException("TODO M2: mark served");
    }

    public void markReadyToPay(int currentTurn) {
        // TODO M2: SERVED -> READY_TO_PAY only if currentTurn > servedTurn.
        throw new UnsupportedOperationException("TODO M2: mark ready to pay");
    }

    public void markPaid() {
        // TODO M2: READY_TO_PAY -> PAID.
        throw new UnsupportedOperationException("TODO M2: mark paid");
    }

    public void abandon() {
        // TODO M2: Unserved customer, satisfaction == 0 -> ABANDONED.
        // Do not release the table or cancel orders here.
        throw new UnsupportedOperationException("TODO M2: abandon customer");
    }

    public void decreaseWaitingSatisfaction() {
        // TODO M2: Only WAITING/SEATED/ORDERED lose 8, min 0.
        // Use WAITING_SATISFACTION_LOSS, not a magic number.
        throw new UnsupportedOperationException("TODO M2: decrease satisfaction");
    }

    private boolean isAwaitingService() {
        return status == CustomerStatus.WAITING
                || status == CustomerStatus.SEATED
                || status == CustomerStatus.ORDERED;
    }
}
