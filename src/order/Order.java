
package order;

import customer.Customer;
import menu.MenuItem;

public class Order {

    // Single source of truth for the Stage 3 promotion
    public static final int HAPPY_HOUR_DISCOUNT_PERCENT = 20;

    private final int id;
    private final Customer customer;
    private final MenuItem menuItem;
    private final int priceCents;

    private int completedPreparationUnits = 0;
    private OrderStatus status = OrderStatus.PLACED;

    private int readyTurn = -1;
    private int servedTurn = -1;

    // Additional Feature: Cooking quality
    private CookingQuality cookingQuality = CookingQuality.NORMAL;
    private boolean cookingQualityRecorded = false;

    // Original constructor for orders without Happy Hour
    public Order(int id, Customer customer, MenuItem menuItem) {
        this(id, customer, menuItem, false);
    }

    // Stage 3: Constructor supporting Happy Hour
    public Order(int id, Customer customer, MenuItem menuItem,
                 boolean happyHourActive) {

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

        int discountPercent = customer.getDiscountPercent();

        if (discountPercent < 0 || discountPercent > 100) {
            throw new IllegalArgumentException(
                    "Discount must be between 0 and 100.");
        }

        // Apply VIP discount first
        long finalPrice = (
                (long) menuItem.getPriceCents()
                * (100 - discountPercent) + 50) / 100;

        // Apply Happy Hour discount after VIP discount
        if (happyHourActive) {
            finalPrice = (finalPrice
                    * (100 - HAPPY_HOUR_DISCOUNT_PERCENT) + 50) / 100;
        }

        // Lock the final price when the order is created
        this.priceCents = Math.toIntExact(finalPrice);
    }

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

    public int getRemainingPreparationUnits() {
        return menuItem.getPreparationUnits()
                - completedPreparationUnits;
    }

    // -------------------------------------------------
    // ADDITIONAL FEATURE: COOKING QUALITY
    // -------------------------------------------------

    // Return the cooking quality of this order
    public CookingQuality getCookingQuality() {
        return cookingQuality;
    }

    // Check whether cooking quality has been recorded
    public boolean isCookingQualityRecorded() {
        return cookingQualityRecorded;
    }

    // Record the cooking challenge result
    // The order must have completed preparation
    // Cooking quality can only be recorded once
    public void recordCookingQuality(CookingQuality quality) {

        if (quality == null) {
            throw new IllegalArgumentException(
                    "Cooking quality cannot be null.");
        }

        if (status != OrderStatus.READY) {
            throw new IllegalStateException(
                    "Order must be ready before recording cooking quality.");
        }

        if (cookingQualityRecorded) {
            throw new IllegalStateException(
                    "Cooking quality has already been recorded.");
        }

        cookingQuality = quality;
        cookingQualityRecorded = true;
    }

    // -------------------------------------------------
    // EXISTING ORDER LIFECYCLE
    // -------------------------------------------------

    // Check whether an order can be prepared
    public boolean canBePrepared() {
        return status == OrderStatus.PLACED
                || status == OrderStatus.PREPARING;
    }

    // Check whether an order can be served
    public boolean canBeServed(int currentTurn) {
        return status == OrderStatus.READY
                && currentTurn > readyTurn;
    }

    // Chef prepares one unit per action
    public void prepareOneUnit(int currentTurn) {

        if (currentTurn < 1) {
            throw new IllegalArgumentException(
                    "Turn number must be positive.");
        }

        if (!canBePrepared()) {
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

        if (!canBeServed(currentTurn)) {
            throw new IllegalStateException(
                    "Order must be ready from an earlier turn.");
        }

        status = OrderStatus.SERVED;
        servedTurn = currentTurn;
    }

    // Mark the order as paid
    public void markPaid() {

        if (status != OrderStatus.SERVED) {
            throw new IllegalStateException(
                    "Only served orders can be paid.");
        }

        status = OrderStatus.PAID;
    }

    // Cancel an unserved order
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
