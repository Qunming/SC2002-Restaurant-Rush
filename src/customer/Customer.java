
package customer;

import java.util.List;
import menu.Menu;
import menu.MenuItem;
import order.Order;
import order.CookingQuality;
import feedback.FeedbackType;
import restaurant.Table;

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
            throw new IllegalArgumentException(
                    "Customer ID must be positive.");
        }
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getSatisfaction() {
        return satisfaction;
    }

    public CustomerStatus getStatus() {
        return status;
    }

    public Table getTable() {
        return assignedTable;
    }

    public Order getOrder() {
        return order;
    }

    public int getServedTurn() {
        return servedTurn;
    }

    public int getReadyToPayTurn() {
        return readyToPayTurn;
    }

    public String getCustomerType() {
        return "Regular";
    }

    public int getWaitingSatisfactionLoss() {
        return WAITING_SATISFACTION_LOSS;
    }

    public int getDiscountPercent() {
        return 0;
    }

    // Customer chooses their own food
    public MenuItem chooseMenuItem(Menu menu) {
        if (menu == null) {
            throw new IllegalArgumentException(
                    "Menu cannot be null.");
        }

        List<MenuItem> items = menu.getItems();

        if (items.isEmpty()) {
            throw new IllegalStateException(
                    "Menu has no items.");
        }

        return items.get((id - 1) % items.size());
    }

    // Seat the customer at a table
    public void seatAt(Table table) {
        if (status != CustomerStatus.WAITING
                || table == null
                || assignedTable != null) {
            throw new IllegalArgumentException(
                    "Customer cannot be seated.");
        }

        assignedTable = table;
        status = CustomerStatus.SEATED;
    }

    // Assign an order to the customer
    public void assignOrder(Order newOrder) {
        if (status != CustomerStatus.SEATED
                || newOrder == null
                || newOrder.getCustomer() != this
                || order != null) {
            throw new IllegalArgumentException(
                    "Cannot assign order to customer.");
        }

        order = newOrder;
        status = CustomerStatus.ORDERED;
    }

    // Mark the customer as served
    public void markServed(int currentTurn) {
        if (status != CustomerStatus.ORDERED
                || currentTurn < 1) {
            throw new IllegalArgumentException(
                    "Customer cannot be served.");
        }

        status = CustomerStatus.SERVED;
        servedTurn = currentTurn;
    }

    // Mark the customer as ready for payment
    public void markReadyToPay(int currentTurn) {
        if (status != CustomerStatus.SERVED
                || currentTurn <= servedTurn) {
            throw new IllegalArgumentException(
                    "Customer has not finished eating.");
        }

        status = CustomerStatus.READY_TO_PAY;
        readyToPayTurn = currentTurn;
    }

    // Mark the customer as paid
    public void markPaid() {
        if (status != CustomerStatus.READY_TO_PAY) {
            throw new IllegalArgumentException(
                    "Customer is not ready to pay.");
        }

        status = CustomerStatus.PAID;
    }

    // Customer abandons the restaurant
    public void abandon() {
        if (!isAwaitingService()
                || satisfaction != 0) {
            throw new IllegalArgumentException(
                    "Customer cannot abandon.");
        }

        status = CustomerStatus.ABANDONED;
    }

    // Reduce satisfaction based on customer type
    public void decreaseWaitingSatisfaction() {
        if (isAwaitingService()) {
            satisfaction = Math.max(
                    0,
                    satisfaction - getWaitingSatisfactionLoss());
        }
    }

    // Stage 3: Recover satisfaction during Happy Hour
    public void recoverSatisfaction(int recoveryPoints) {
        if (recoveryPoints < 0) {
            throw new IllegalArgumentException(
                    "Recovery points cannot be negative.");
        }

        if (isAwaitingService()) {
            satisfaction += Math.min(
                    recoveryPoints,
                    STARTING_SATISFACTION - satisfaction);
        }
    }

    // -------------------------------------------------
    // ADDITIONAL FEATURE: CUSTOMER FEEDBACK
    // -------------------------------------------------

    // Determine feedback based on cooking quality
    // Regular customers accept normal food
    // Subclasses can override this behaviour
    public FeedbackType getFeedbackType(
            CookingQuality quality) {

        if (quality == null) {
            throw new IllegalArgumentException(
                    "Cooking quality cannot be null.");
        }

        if (quality == CookingQuality.PERFECT) {
            return FeedbackType.GOOD;
        }

        return FeedbackType.MEDIOCRE;
    }

    // Generate the customer's feedback comment
    // Subclasses can override this method
    public String getFeedbackComment(
            CookingQuality quality) {

        FeedbackType feedback = getFeedbackType(quality);

        if (feedback == FeedbackType.GOOD) {
            return "Delicious! Compliments to the chef!";
        }

        return "It was alright.";
    }

    // -------------------------------------------------
    // EXISTING TABLE MANAGEMENT
    // -------------------------------------------------

    // Clear table reference after leaving the restaurant
    public void clearTable() {
        if (status != CustomerStatus.PAID
                && status != CustomerStatus.ABANDONED) {
            throw new IllegalStateException(
                    "Customer has not left the restaurant.");
        }

        assignedTable = null;
    }

    // Check whether the customer is awaiting service
    private boolean isAwaitingService() {
        return status == CustomerStatus.WAITING
                || status == CustomerStatus.SEATED
                || status == CustomerStatus.ORDERED;
    }
}
