
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

// Handles the display of the restaurant game.
// Displays customer types, order prices, statistics,
// Happy Hour information, and game results.
//
// This class only displays information.
// It does not modify the game state.

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

        // Display the current game information
        showStatistics(state, false);
        showAlerts(state);
        showTables(state);
        showWaitingQueue(state);
        showActiveOrders(state);
    }

    // -------------------------------------------------
    // SCOREBOARD
    // -------------------------------------------------

    // Display current game statistics.
    // false means the game is still running.
    // true means the game has ended.

    private void showStatistics(
            GameState state, boolean isFinalResult) {

        section("SCOREBOARD");

        // Check whether payment target is met
        boolean paidMet =
                state.getPaidCustomerCount()
                >= GameConfig.REQUIRED_PAID_CUSTOMERS;

        // Check whether revenue target is met
        boolean revenueMet =
                state.getRevenueCents()
                >= GameConfig.REQUIRED_REVENUE_CENTS;

        // Check whether satisfaction target is met
        boolean satisfactionMet =
                state.getPaidCustomerCount() > 0
                && state.getAveragePaidSatisfaction()
                >= GameConfig.REQUIRED_AVERAGE_SATISFACTION;

        // Format revenue in dollars
        String revenue = String.format(
                "$%.2f / $%.2f",
                state.getRevenueCents() / 100.0,
                GameConfig.REQUIRED_REVENUE_CENTS / 100.0);

        // Format average satisfaction
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

        // Display completed turns after the game ends
        if (isFinalResult) {

            System.out.println(
                    "  Turns completed  "
                    + GameConfig.MAX_TURNS
                    + " / "
                    + GameConfig.MAX_TURNS);

        } else {

            // Include the current unfinished turn
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

    // Display important alerts for the current turn

    private void showAlerts(GameState state) {

        section("WHAT NEEDS ATTENTION");

        List<String> alerts = new ArrayList<>();

        // Check for orders ready to serve
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

        // Check for customers with low satisfaction
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

        // Check whether customers are waiting
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

        // Check whether any customer can pay
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

        // Display all alerts
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

    // Display table availability and occupants

    private void showTables(GameState state) {

        section("DINING ROOM");

        for (Table table :
                state.getTableManager().getTables()) {

            Customer customer = table.getOccupant();

            if (customer == null) {

                // Empty table
                System.out.println(
                        "  Table "
                        + table.getNumber()
                        + "  "
                        + AnsiColors.paint(
                                "AVAILABLE",
                                AnsiColors.GREEN));

            } else {

                // Occupied table
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

    // Display customers waiting to be seated

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

    // Display all active orders and their progress

    private void showActiveOrders(GameState state) {

        section("ACTIVE ORDERS");

        int activeCount = 0;
        int paidCount = 0;
        int cancelledCount = 0;

        for (Order order :
                state.getOrderManager().getOrders()) {

            OrderStatus status = order.getStatus();

            // Count completed orders
            if (status == OrderStatus.PAID) {
                paidCount++;
                continue;
            }

            // Count cancelled orders
            if (status == OrderStatus.CANCELLED) {
                cancelledCount++;
                continue;
            }

            activeCount++;

            // Show remaining cooking units if needed
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
            System.out.println("  No active orders");
        }

        // Display completed and cancelled order counts
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

    // Display all orders, including paid and cancelled orders.
    // Viewing history does not consume a Waiter action.

    public void showHistory(GameState state) {

        section("ALL ORDERS - HISTORY");

        List<Order> orders =
                state.getOrderManager().getOrders();

        if (orders.isEmpty()) {

            System.out.println("  No orders yet.");

        } else {

            for (Order order : orders) {

                Customer customer = order.getCustomer();

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

    // Display end-of-turn game statistics

    public void showEndOfTurn(GameState state) {

        section(
                "END OF TURN "
                + state.getCurrentTurn());

        // Show N/A if no customer has paid
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
    // STAGE 3 - HAPPY HOUR
    // -------------------------------------------------

    // Display the current Happy Hour status
    // The discount percentage is obtained from Order

    public void showHappyHourStatus(GameState state) {

        if (state.isHappyHourActive()) {

            int remaining =
                    state.getHappyHourTurnsRemaining();

            showMessage(
                    "Happy Hour ACTIVE: " + remaining
                    + " turn(s) remaining (including this turn)."
                    + " New orders: "
                    + Order.HAPPY_HOUR_DISCOUNT_PERCENT
                    + "% off; unserved customers: +15 satisfaction.");

        } else if (state.hasHappyHourBeenUsed()) {

            showMessage(
                    "Happy Hour has ended and cannot be activated again.");

        } else {

            showMessage(
                    "Happy Hour is available to activate on a later turn.");
        }
    }

    // -------------------------------------------------
    // STAGE 3 - ORDER FEEDBACK
    // -------------------------------------------------

    // Display the order placed by a customer
    // Order formatting is handled by the UI instead of GameController

    public void showPlacedOrder(Customer customer) {

        Order placed = customer.getOrder();

        showMessage(
                customer.getCustomerType()
                + " Customer #" + customer.getId()
                + " ordered " + placed.getItem().getName()
                + " (Order #" + placed.getId()
                + ", bill "
                + String.format(
                        "$%.2f",
                        placed.getFinalPriceCents() / 100.0)
                + ").");
    }

    // -------------------------------------------------
    // FEEDBACK MESSAGES
    // -------------------------------------------------

    // Display a successful action message

    public void showMessage(String message) {

        System.out.println(
                AnsiColors.paint(
                        "  [OK] ",
                        AnsiColors.GREEN)
                + message);
    }

    // Display an invalid action message

    public void showError(String message) {

        System.out.println(
                AnsiColors.paint(
                        "  [INVALID] " + message,
                        AnsiColors.RED));
    }

    // -------------------------------------------------
    // CUSTOMER ABANDONMENT ALERT
    // -------------------------------------------------

    // Display a red message for a newly abandoned customer.
    // Called before the customer table is released.
    // Works for queue customers and seated customers.

    public void showCustomerAbandonment(Customer customer) {

        if (customer == null) {
            throw new IllegalArgumentException(
                    "Customer cannot be null.");
        }

        if (customer.getStatus() != CustomerStatus.ABANDONED) {
            throw new IllegalArgumentException(
                    "Customer has not abandoned.");
        }

        String location;

        if (customer.getTable() == null) {

            // Customer was waiting in the queue
            location = "abandoned the queue!";

        } else {

            // Customer was seated at a table
            location = "left Table "
                    + customer.getTable().getNumber()
                    + "!";
        }

        String message =
                "  [ABANDONED] "
                + customerLabel(customer)
                + " reached 0 satisfaction and "
                + location;

        System.out.println(
                AnsiColors.paint(message, AnsiColors.RED));
    }

    // -------------------------------------------------
    // FINAL GAME RESULT
    // -------------------------------------------------

    // Display victory or defeat at the end of the game

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

        // Display the final scoreboard
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

    // Display customer type and ID
    // Uses polymorphism to obtain the correct customer type

    private String customerLabel(Customer customer) {

        return customer.getCustomerType()
                + " Customer #"
                + customer.getId();
    }

    // Convert cents into dollars

    private String money(int cents) {

        return String.format(
                "$%.2f",
                cents / 100.0);
    }

    // Display a section heading

    private void section(String heading) {

        System.out.println();

        System.out.println(
                AnsiColors.paint(
                        "[" + heading + "]",
                        AnsiColors.CYAN + AnsiColors.BOLD));
    }

    // Colour achieved and incomplete goals

    private String goal(String text, boolean met) {

        return AnsiColors.paint(
                text,
                met
                    ? AnsiColors.GREEN
                    : AnsiColors.YELLOW);
    }

    // Display customer status with colours

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

    // Display order status with colours

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

    // Check whether any table is available

    private boolean hasFreeTable(GameState state) {

        for (Table table :
                state.getTableManager().getTables()) {

            if (table.isAvailable()) {
                return true;
            }
        }

        return false;
    }

    // Check whether a customer is still unserved

    private boolean isUnserved(Customer customer) {

        return customer.getStatus()
                    == CustomerStatus.WAITING
                || customer.getStatus()
                    == CustomerStatus.SEATED
                || customer.getStatus()
                    == CustomerStatus.ORDERED;
    }

    // Check whether an order can be served
    // Reuse the eligibility rule from Order.java

    private boolean canServe(
            Order order, int currentTurn) {

        return order.canBeServed(currentTurn);
    }
}
