
package ui;

import controller.GameConfig;
import controller.GameState;
import customer.Customer;
import customer.CustomerStatus;
import java.util.ArrayList;
import java.util.List;
import order.Order;
import order.OrderStatus;
import restaurant.Table;
import staff.Chef;
import staff.Waiter;

/**
 * Handles the display of the restaurant game.
 *
 * Stage 2:
 * - Displays different customer types.
 * - Displays final order prices, including discounts.
 * - Shows correct remaining turns during gameplay.
 * - Shows completed turns on the final scoreboard.
 *
 * This class only displays information.
 * It does not change the game state.
 */
public class ConsoleUI {

    private static final String RULE =
            "================================================================";

    // -------------------------------------------------
    // START OF TURN
    // -------------------------------------------------

    public void showStartOfTurn(
            GameState state, Waiter waiter, Chef chef) {

        System.out.println();

        System.out.println(
                AnsiColors.paint(RULE, AnsiColors.CYAN));

        System.out.println(
                AnsiColors.paint(
                        " RESTAURANT RUSH  |  TURN "
                        + state.getCurrentTurn()
                        + " / " + GameConfig.MAX_TURNS,
                        AnsiColors.CYAN + AnsiColors.BOLD));

        System.out.println(
                AnsiColors.paint(RULE, AnsiColors.CYAN));

        // false = game is still in progress
        showStatistics(state, false);

        showAlerts(state);
        showTables(state);
        showWaitingQueue(state);
        showActiveOrders(state);
    }

    // -------------------------------------------------
    // SCOREBOARD
    // -------------------------------------------------

    /**
     * Displays the game statistics.
     *
     * isFinalResult = false:
     *     Displays turns remaining.
     *
     * isFinalResult = true:
     *     Displays turns completed instead.
     */
    private void showStatistics(
            GameState state, boolean isFinalResult) {

        section("SCOREBOARD");

        // Check victory requirements
        boolean paidMet =
                state.getPaidCustomerCount()
                >= GameConfig.REQUIRED_PAID_CUSTOMERS;

        boolean revenueMet =
                state.getRevenueCents()
                >= GameConfig.REQUIRED_REVENUE_CENTS;

        boolean satisfactionMet =
                state.getPaidCustomerCount() > 0
                && state.getAveragePaidSatisfaction()
                >= GameConfig.REQUIRED_AVERAGE_SATISFACTION;

        // Revenue
        String revenue = String.format(
                "$%.2f / $%.2f",
                state.getRevenueCents() / 100.0,
                GameConfig.REQUIRED_REVENUE_CENTS / 100.0);

        // Average satisfaction
        String satisfaction =
                state.getPaidCustomerCount() == 0
                ? "N/A / "
                    + GameConfig.REQUIRED_AVERAGE_SATISFACTION
                : String.format(
                        "%.1f / %d",
                        state.getAveragePaidSatisfaction(),
                        GameConfig.REQUIRED_AVERAGE_SATISFACTION);

        System.out.println(
                "  Revenue          "
                + goal(revenue, revenueMet));

        System.out.println(
                "  Paid customers   "
                + goal(
                        state.getPaidCustomerCount()
                        + " / "
                        + GameConfig.REQUIRED_PAID_CUSTOMERS,
                        paidMet));

        System.out.println(
                "  Avg satisfaction "
                + goal(satisfaction, satisfactionMet));

        // Stage 2 refinement:
        // Display turns differently during gameplay
        // and after the final turn.

        if (isFinalResult) {

            // All 18 turns have already been completed.
            System.out.println(
                    "  Turns completed  "
                    + GameConfig.MAX_TURNS
                    + " / "
                    + GameConfig.MAX_TURNS);

        } else {

            // Include the current turn because
            // the player has not finished it yet.
            int turnsRemaining =
                    GameConfig.MAX_TURNS
                    - state.getCurrentTurn()
                    + 1;

            System.out.println(
                    "  Turns remaining  "
                    + turnsRemaining
                    + " (including this turn)");
        }
    }

    // -------------------------------------------------
    // ALERTS
    // -------------------------------------------------

    private void showAlerts(GameState state) {

        section("WHAT NEEDS ATTENTION");

        List<String> alerts = new ArrayList<>();

        // Orders that can be served
        for (Order order :
                state.getOrderManager().getOrders()) {

            if (canServe(order, state.getCurrentTurn())) {

                alerts.add(
                        AnsiColors.paint(
                                "READY TO SERVE",
                                AnsiColors.GREEN)
                        + " - Order #" + order.getId()
                        + " ("
                        + customerLabel(order.getCustomer())
                        + ")");
            }
        }

        // Customers with low satisfaction
        for (Customer customer :
                state.getCustomerManager().getActiveCustomers()) {

            if (isUnserved(customer)
                    && customer.getSatisfaction() <= 30) {

                alerts.add(
                        AnsiColors.paint(
                                "LOW SATISFACTION",
                                AnsiColors.RED)
                        + " - " + customerLabel(customer)
                        + " ("
                        + customer.getSatisfaction()
                        + ")");
            }
        }

        // Waiting customers
        List<Customer> waiting =
                state.getCustomerManager()
                        .getWaitingCustomers();

        if (!waiting.isEmpty()) {

            if (hasFreeTable(state)) {

                alerts.add(
                        AnsiColors.paint(
                                "TABLE AVAILABLE",
                                AnsiColors.GREEN)
                        + " - "
                        + waiting.size()
                        + " customer(s) waiting");

            } else {

                alerts.add(
                        AnsiColors.paint(
                                "QUEUE WAITING",
                                AnsiColors.YELLOW)
                        + " - All tables occupied");
            }
        }

        // Customers eligible for payment
        for (Customer customer :
                state.getCustomerManager().getActiveCustomers()) {

            if (customer.getStatus()
                    == CustomerStatus.READY_TO_PAY
                    && customer.getReadyToPayTurn()
                    < state.getCurrentTurn()) {

                alerts.add(
                        AnsiColors.paint(
                                "PAYMENT DUE",
                                AnsiColors.CYAN)
                        + " - "
                        + customerLabel(customer)
                        + " (cashier automatic)");
            }
        }

        if (alerts.isEmpty()) {

            System.out.println(
                    "  No immediate alerts.");

        } else {

            for (String alert : alerts) {
                System.out.println("  * " + alert);
            }
        }
    }

    // -------------------------------------------------
    // DINING ROOM
    // -------------------------------------------------

    private void showTables(GameState state) {

        section("DINING ROOM");

        for (Table table :
                state.getTableManager().getTables()) {

            Customer customer = table.getOccupant();

            if (customer == null) {

                System.out.println(
                        "  Table "
                        + table.getNumber()
                        + "  "
                        + AnsiColors.paint(
                                "AVAILABLE",
                                AnsiColors.GREEN));

            } else {

                System.out.println(
                        "  Table "
                        + table.getNumber()
                        + "  "
                        + customerLabel(customer)
                        + " | "
                        + customerStatus(customer.getStatus())
                        + " | Satisfaction "
                        + AnsiColors.satisfaction(
                                customer.getSatisfaction()));
            }
        }
    }

    // -------------------------------------------------
    // WAITING QUEUE
    // -------------------------------------------------

    private void showWaitingQueue(GameState state) {

        section("WAITING QUEUE");

        List<Customer> waiting =
                state.getCustomerManager()
                        .getWaitingCustomers();

        if (waiting.isEmpty()) {

            System.out.println("  Empty");
            return;
        }

        for (Customer customer : waiting) {

            System.out.println(
                    "  "
                    + customerLabel(customer)
                    + " | Satisfaction "
                    + AnsiColors.satisfaction(
                            customer.getSatisfaction()));
        }
    }

    // -------------------------------------------------
    // ACTIVE ORDERS
    // -------------------------------------------------

    private void showActiveOrders(GameState state) {

        section("ACTIVE ORDERS");

        int activeCount = 0;
        int paidCount = 0;
        int cancelledCount = 0;

        for (Order order :
                state.getOrderManager().getOrders()) {

            OrderStatus status = order.getStatus();

            if (status == OrderStatus.PAID) {
                paidCount++;
                continue;
            }

            if (status == OrderStatus.CANCELLED) {
                cancelledCount++;
                continue;
            }

            activeCount++;

            String cooking =
                    (status == OrderStatus.PLACED
                    || status == OrderStatus.PREPARING)
                    ? " | Prep left: "
                        + order.getRemainingPreparationUnits()
                    : "";

            System.out.println(
                    "  #" + order.getId()
                    + " | "
                    + customerLabel(order.getCustomer())
                    + " | "
                    + order.getItem().getName()
                    + " | Bill "
                    + money(order.getFinalPriceCents())
                    + " | "
                    + orderStatus(status)
                    + cooking);
        }

        if (activeCount == 0) {

            System.out.println(
                    "  No active orders");
        }

        System.out.println(
                "  Completed: "
                + AnsiColors.paint(
                        Integer.toString(paidCount),
                        AnsiColors.GREEN)
                + " paid | "
                + AnsiColors.paint(
                        Integer.toString(cancelledCount),
                        AnsiColors.RED)
                + " cancelled"
                + "  (history available in Waiter menu)");
    }

    // -------------------------------------------------
    // ORDER HISTORY
    // -------------------------------------------------

    /**
     * Viewing order history does not consume
     * a Waiter action.
     */
    public void showHistory(GameState state) {

        section("ALL ORDERS - HISTORY");

        List<Order> orders =
                state.getOrderManager().getOrders();

        if (orders.isEmpty()) {

            System.out.println(
                    "  No orders yet.");

        } else {

            for (Order order : orders) {

                Customer customer =
                        order.getCustomer();

                String dish = String.format(
                        "%-8s",
                        order.getItem().getName());

                String status =
                        orderStatus(order.getStatus());

                System.out.println(
                        "  Order #" + order.getId()
                        + " | "
                        + customerLabel(customer)
                        + " | "
                        + dish
                        + " | Bill "
                        + money(order.getFinalPriceCents())
                        + " | "
                        + status
                        + " | Sat "
                        + AnsiColors.satisfaction(
                                customer.getSatisfaction()));
            }
        }

        System.out.println(
                "  Total paid: "
                + state.getPaidCustomerCount()
                + " | Abandoned: "
                + state.getAbandonedCustomerCount());

        System.out.println(
                "  (Customers who leave before ordering"
                + " have no order record.)");
    }

    // -------------------------------------------------
    // END OF TURN
    // -------------------------------------------------

    public void showEndOfTurn(GameState state) {

        section(
                "END OF TURN "
                + state.getCurrentTurn());

        // Display N/A when no customer has paid.
        String averageSatisfaction =
                state.getPaidCustomerCount() == 0
                ? "N/A"
                : String.format(
                        "%.1f",
                        state.getAveragePaidSatisfaction());

        System.out.printf(
                "  Revenue $%.2f"
                + " | Paid %d"
                + " | Served %d"
                + " | Abandoned %d"
                + " | Avg satisfaction %s%n",
                state.getRevenueCents() / 100.0,
                state.getPaidCustomerCount(),
                state.getServedCustomerCount(),
                state.getAbandonedCustomerCount(),
                averageSatisfaction);
    }

    // -------------------------------------------------
    // FEEDBACK MESSAGES
    // -------------------------------------------------

    public void showMessage(String message) {

        System.out.println(
                AnsiColors.paint(
                        "  [OK] ",
                        AnsiColors.GREEN)
                + message);
    }

    public void showError(String message) {

        System.out.println(
                AnsiColors.paint(
                        "  [INVALID] " + message,
                        AnsiColors.RED));
    }

    // -------------------------------------------------
    // FINAL GAME RESULT
    // -------------------------------------------------

    public void showFinalResult(
            GameState state, boolean victory) {

        System.out.println();

        System.out.println(
                AnsiColors.paint(RULE, AnsiColors.CYAN));

        System.out.println(
                AnsiColors.paint(
                        victory
                            ? "                     VICTORY!"
                            : "                     DEFEAT!",
                        victory
                            ? AnsiColors.GREEN + AnsiColors.BOLD
                            : AnsiColors.RED + AnsiColors.BOLD));

        System.out.println(
                AnsiColors.paint(RULE, AnsiColors.CYAN));

        // true = final scoreboard
        // Show completed turns instead of remaining turns.
        showStatistics(state, true);

        System.out.println(
                "  Served customers: "
                + state.getServedCustomerCount());

        System.out.println(
                "  Abandoned customers: "
                + state.getAbandonedCustomerCount());
    }

    // -------------------------------------------------
    // HELPER METHODS
    // -------------------------------------------------

    /**
     * Stage 2:
     * Display customer types using polymorphism.
     */
    private String customerLabel(Customer customer) {

        return customer.getCustomerType()
                + " Customer #"
                + customer.getId();
    }

    /**
     * Convert cents into dollars.
     */
    private String money(int cents) {

        return String.format(
                "$%.2f",
                cents / 100.0);
    }

    /**
     * Display a section heading.
     */
    private void section(String heading) {

        System.out.println();

        System.out.println(
                AnsiColors.paint(
                        "[" + heading + "]",
                        AnsiColors.CYAN + AnsiColors.BOLD));
    }

    /**
     * Colour achieved and incomplete goals.
     */
    private String goal(String text, boolean met) {

        return AnsiColors.paint(
                text,
                met
                    ? AnsiColors.GREEN
                    : AnsiColors.YELLOW);
    }

    /**
     * Display customer status with colours.
     */
    private String customerStatus(CustomerStatus status) {

        switch (status) {

            case PAID:
                return AnsiColors.paint(
                        status.name(),
                        AnsiColors.GREEN);

            case ABANDONED:
                return AnsiColors.paint(
                        status.name(),
                        AnsiColors.RED);

            case WAITING:
            case SEATED:
            case ORDERED:
                return AnsiColors.paint(
                        status.name(),
                        AnsiColors.YELLOW);

            case SERVED:
            case READY_TO_PAY:
                return AnsiColors.paint(
                        status.name(),
                        AnsiColors.CYAN);

            default:
                return status.name();
        }
    }

    /**
     * Display order status with colours.
     */
    private String orderStatus(OrderStatus status) {

        switch (status) {

            case READY:
            case PAID:
                return AnsiColors.paint(
                        status.name(),
                        AnsiColors.GREEN);

            case CANCELLED:
                return AnsiColors.paint(
                        status.name(),
                        AnsiColors.RED);

            case PLACED:
            case PREPARING:
                return AnsiColors.paint(
                        status.name(),
                        AnsiColors.YELLOW);

            case SERVED:
                return AnsiColors.paint(
                        status.name(),
                        AnsiColors.CYAN);

            default:
                return status.name();
        }
    }

    /**
     * Check whether any table is available.
     */
    private boolean hasFreeTable(GameState state) {

        for (Table table :
                state.getTableManager().getTables()) {

            if (table.isAvailable()) {
                return true;
            }
        }

        return false;
    }

    /**
     * Check whether the customer is still
     * waiting to be served.
     */
    private boolean isUnserved(Customer customer) {

        return customer.getStatus()
                    == CustomerStatus.WAITING
                || customer.getStatus()
                    == CustomerStatus.SEATED
                || customer.getStatus()
                    == CustomerStatus.ORDERED;
    }

    /**
     * An order can only be served on a turn
     * later than the turn it became READY.
     */
    private boolean canServe(
            Order order, int currentTurn) {

        return order.getStatus() == OrderStatus.READY
                && order.getReadyTurn() < currentTurn;
    }
}
