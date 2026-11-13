
package controller;

import customer.Customer;
import customer.CustomerManager;
import customer.CustomerStatus;
import feedback.ReviewManager;
import menu.Menu;
import order.Order;
import order.OrderManager;
import order.OrderStatus;
import policy.TipCalculator;
import restaurant.TableManager;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

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

    // -1 means Happy Hour has not been used
    private int happyHourStartTurn = -1;

    // -------------------------------------------------
    // ADDITIONAL FEATURE: REVIEWS AND TIPS
    // -------------------------------------------------

    // Stores customer reviews
    private final ReviewManager reviewManager = new ReviewManager();

    // Tracks tips separately from food revenue
    private int totalTipsCents = 0;

    // Prevents the same order from generating tips twice
    private final Set<Integer> tipRecordedOrderIds = new HashSet<>();

    // -------------------------------------------------
    // CONSTRUCTOR
    // -------------------------------------------------

    public GameState(CustomerManager customerManager,
                     TableManager tableManager,
                     OrderManager orderManager,
                     Menu menu) {

        this.customerManager =
                Objects.requireNonNull(customerManager);

        this.tableManager =
                Objects.requireNonNull(tableManager);

        this.orderManager =
                Objects.requireNonNull(orderManager);

        this.menu =
                Objects.requireNonNull(menu);
    }

    // -------------------------------------------------
    // EXISTING GETTERS
    // -------------------------------------------------

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

    // -------------------------------------------------
    // ADDITIONAL FEATURE GETTERS
    // -------------------------------------------------

    // Access the restaurant's customer reviews
    public ReviewManager getReviewManager() {
        return reviewManager;
    }

    // Get total tips earned in cents
    public int getTotalTipsCents() {
        return totalTipsCents;
    }

    // -------------------------------------------------
    // TURN MANAGEMENT
    // -------------------------------------------------

    public void advanceTurn() {
        currentTurn++;
    }

    // -------------------------------------------------
    // STAGE 3: HAPPY HOUR
    // -------------------------------------------------

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

        return Math.max(
                0,
                happyHourStartTurn
                + GameConfig.HAPPY_HOUR_DURATION_TURNS
                - currentTurn);
    }

    public boolean isHappyHourActive() {
        return getHappyHourTurnsRemaining() > 0;
    }

    // -------------------------------------------------
    // EXISTING PAYMENT MANAGEMENT
    // -------------------------------------------------

    // Record the customer's payment
    // Only the order price counts towards the victory target
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

    // -------------------------------------------------
    // ADDITIONAL FEATURE: TIP TRACKING
    // -------------------------------------------------

    // Record the tip for a paid order
    // Must be called after the Cashier marks the order PAID
    // Returns the tip earned from that order
    public int recordTipForPaidOrder(Order order) {

        if (order == null) {
            throw new IllegalArgumentException(
                    "Order cannot be null.");
        }

        // The order must belong to this restaurant
        if (orderManager.getOrderById(order.getId()) != order) {
            throw new IllegalArgumentException(
                    "Order does not belong to this restaurant.");
        }

        // Tips can only be recorded after payment
        if (order.getStatus() != OrderStatus.PAID
                || order.getCustomer().getStatus() != CustomerStatus.PAID) {
            throw new IllegalStateException(
                    "Order must be paid before recording tips.");
        }

        // Prevent duplicate tip recording
        if (tipRecordedOrderIds.contains(order.getId())) {
            throw new IllegalStateException(
                    "Tip has already been processed for this order.");
        }

        // Calculate tip based on cooking quality
        int tipCents = TipCalculator.calculateTipCents(order);

        // Keep tips separate from normal game revenue
        totalTipsCents = Math.addExact(totalTipsCents, tipCents);

        // Mark the order as processed, even if no tip was earned
        tipRecordedOrderIds.add(order.getId());

        return tipCents;
    }

    // -------------------------------------------------
    // EXISTING GAME STATISTICS
    // -------------------------------------------------

    public void recordServing() {
        servedCustomerCount++;
    }

    public void recordAbandonment() {
        abandonedCustomerCount++;
    }
}
