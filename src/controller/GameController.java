
package controller;

import challenge.CookingChallenge;
import customer.Customer;
import customer.CustomerStatus;
import customer.RegularCustomer;
import customer.VIPCustomer;
import customer.CriticCustomer;
import feedback.FeedbackType;
import feedback.Review;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import order.CookingQuality;
import order.Order;
import order.OrderStatus;
import restaurant.Table;
import staff.Cashier;
import staff.Chef;
import staff.Host;
import staff.Waiter;
import ui.AnsiColors;
import ui.ConsoleUI;
import ui.InputHandler;

public class GameController {

    private final GameState gameState;
    private final ConsoleUI consoleUI;
    private final InputHandler inputHandler;
    private final Waiter waiter;
    private final Host host;
    private final Chef chef;
    private final Cashier cashier;
    private final CookingChallenge cookingChallenge;

    private int nextCustomerId = 1;

    // Original constructor retained for compatibility
    public GameController(GameState gameState,
                          ConsoleUI consoleUI,
                          InputHandler inputHandler,
                          Waiter waiter,
                          Host host,
                          Chef chef,
                          Cashier cashier) {

        this(gameState, consoleUI, inputHandler,
                waiter, host, chef, cashier, null);
    }

    // Constructor with cooking challenge
    public GameController(GameState gameState,
                          ConsoleUI consoleUI,
                          InputHandler inputHandler,
                          Waiter waiter,
                          Host host,
                          Chef chef,
                          Cashier cashier,
                          CookingChallenge cookingChallenge) {

        this.gameState =
                Objects.requireNonNull(gameState, "gameState");

        this.consoleUI =
                Objects.requireNonNull(consoleUI, "consoleUI");

        this.inputHandler =
                Objects.requireNonNull(inputHandler, "inputHandler");

        this.waiter =
                Objects.requireNonNull(waiter, "waiter");

        this.host =
                Objects.requireNonNull(host, "host");

        this.chef =
                Objects.requireNonNull(chef, "chef");

        this.cashier =
                Objects.requireNonNull(cashier, "cashier");

        // Prevent the cooking challenge from being skipped
        this.cookingChallenge = Objects.requireNonNull(
                cookingChallenge,
                "CookingChallenge is missing. Check Main.java.");
    }

    // -------------------------------------------------
    // GAME LOOP
    // -------------------------------------------------

    public void run() {

        while (gameState.getCurrentTurn()
                <= GameConfig.MAX_TURNS) {

            waiter.resetForNewTurn();
            host.resetForNewTurn();
            chef.resetForNewTurn();

            playTurn();

            if (gameState.getCurrentTurn()
                    == GameConfig.MAX_TURNS) {
                break;
            }

            gameState.advanceTurn();
        }

        consoleUI.showFinalResult(gameState, hasWon());
    }

    // -------------------------------------------------
    // TURN SEQUENCE
    // -------------------------------------------------

    public void playTurn() {

        int currentTurn = gameState.getCurrentTurn();

        if (GameConfig.isArrivalTurn(currentTurn)) {

            int customerId = nextCustomerId++;

            gameState.getCustomerManager().addCustomer(
                    createArrivingCustomer(customerId));
        }

        handleHappyHourChoice();

        if (gameState.isHappyHourActive()) {
            gameState.getCustomerManager()
                    .applyHappyHourRecovery();
        }

        consoleUI.showStartOfTurn(gameState, waiter, chef);
        consoleUI.showHappyHourStatus(gameState);

        handleHostChoice();
        handleWaiterChoice();
        handleChefChoice();

        processEndOfTurn();

        consoleUI.showEndOfTurn(gameState);
    }

    // -------------------------------------------------
    // HAPPY HOUR
    // -------------------------------------------------

    public void handleHappyHourChoice() {

        if (!gameState.canActivateHappyHour()) {
            return;
        }

        int choice = inputHandler.readHappyHourChoice();

        if (choice == 1) {

            gameState.activateHappyHour();

            consoleUI.showMessage(
                    "Happy Hour activated for this turn and next turn.");
        }
    }

    // -------------------------------------------------
    // HOST ACTION
    // -------------------------------------------------

    public void handleHostChoice() {

        while (true) {

            List<Integer> seatIds = eligibleSeatCustomerIds();
            String seatReason = seatUnavailableReason();

            int choice = inputHandler.readHostChoice(seatIds);

            try {

                switch (choice) {

                    case 1: {

                        requireEligible(seatIds, seatReason);

                        int customerId = inputHandler.readTargetId(
                                "customer", seatIds);

                        host.seatCustomer(customerId, gameState);

                        consoleUI.showMessage(
                                "Host seated Customer #"
                                + customerId + ".");

                        return;
                    }

                    case 2:

                        host.waitThisTurn();

                        consoleUI.showMessage(
                                "Host waits this turn.");

                        return;

                    default:

                        throw new IllegalArgumentException(
                                "Choose a Host action from 1 to 2.");
                }

            } catch (IllegalArgumentException
                    | IllegalStateException ex) {

                consoleUI.showError(
                        ex.getMessage()
                        + " No Host task was used; please choose again.");
            }
        }
    }

    // Create different customer types
    private Customer createArrivingCustomer(int customerId) {

        switch (customerId) {

            case 2:
                return new VIPCustomer(customerId);

            case 4:
                return new CriticCustomer(customerId);

            default:
                return new RegularCustomer(customerId);
        }
    }

    // -------------------------------------------------
    // WAITER ACTION
    // -------------------------------------------------

    public void handleWaiterChoice() {

        while (true) {

            List<Integer> seatIds = eligibleSeatCustomerIds();
            List<Integer> takeIds = eligibleTakeOrderCustomerIds();
            List<Integer> serveIds = eligibleServeOrderIds();

            String seatReason = seatUnavailableReason();

            int choice = inputHandler.readWaiterChoice(
                    seatIds, seatReason, takeIds, serveIds);

            try {

                switch (choice) {

                    case 1: {

                        requireEligible(seatIds, seatReason);

                        int customerId = inputHandler.readTargetId(
                                "customer", seatIds);

                        waiter.seatCustomer(customerId, gameState);

                        consoleUI.showMessage(
                                "Seated Customer #" + customerId + ".");

                        return;
                    }

                    case 2: {

                        requireEligible(takeIds,
                                "No seated customer is waiting to place an order.");

                        int customerId = inputHandler.readTargetId(
                                "customer", takeIds);

                        waiter.takeOrder(customerId, gameState);

                        Customer customer =
                                gameState.getCustomerManager()
                                        .getCustomerById(customerId);

                        consoleUI.showPlacedOrder(customer);

                        return;
                    }

                    case 3: {

                        requireEligible(serveIds,
                                "No order was READY before this turn.");

                        int orderId = inputHandler.readTargetId(
                                "order", serveIds);

                        waiter.serveOrder(orderId, gameState);

                        consoleUI.showMessage(
                                "Served Order #" + orderId + ".");

                        return;
                    }

                    case 4:

                        waiter.waitThisTurn();

                        consoleUI.showMessage(
                                "Waiter waits this turn.");

                        return;

                    case 5:

                        // Viewing history does not use a Waiter action
                        consoleUI.showHistory(gameState);

                        break;

                    default:

                        throw new IllegalArgumentException(
                                "Choose a Waiter action from 1 to 5.");
                }

            } catch (IllegalArgumentException
                    | IllegalStateException ex) {

                consoleUI.showError(
                        ex.getMessage()
                        + " No staff task was used; please choose again.");
            }
        }
    }

    // -------------------------------------------------
    // CHEF ACTION
    // -------------------------------------------------

    public void handleChefChoice() {

        while (true) {

            List<Integer> prepareIds = eligiblePrepareOrderIds();

            int choice = inputHandler.readChefChoice(prepareIds);

            try {

                switch (choice) {

                    case 1: {

                        requireEligible(prepareIds,
                                "No order is currently PLACED or PREPARING.");

                        int orderId = inputHandler.readTargetId(
                                "order", prepareIds);

                        Order order =
                                gameState.getOrderManager()
                                        .getOrderById(orderId);

                        // Check the target time for this dish
                        cookingChallenge.getTargetSeconds(
                                order.getItem());

                        // Prepare one unit, as in Stage 3
                        chef.prepareOrder(orderId, gameState);

                        consoleUI.showMessage(
                                "Prepared one unit of Order #"
                                + orderId + ".");

                        // Trigger one challenge on the final unit
                        if (order.getStatus() == OrderStatus.READY
                                && !order.isCookingQualityRecorded()) {

                            consoleUI.showMessage(
                                    "Order #" + orderId
                                    + " has finished preparation.");

                            // Different target times for each dish
                            // Salad: 2 seconds
                            // Burger: 3 seconds
                            // Pasta: 5 seconds
                            // Salad + Pasta Combo: 6 seconds
                            CookingQuality quality =
                                    cookingChallenge.playChallenge(
                                            order.getItem());

                            order.recordCookingQuality(quality);

                            consoleUI.showMessage(
                                    "Order #" + orderId
                                    + " cooking quality: "
                                    + quality + ".");
                        }

                        return;
                    }

                    case 2:

                        chef.waitThisTurn();

                        consoleUI.showMessage(
                                "Chef waits this turn.");

                        return;

                    default:

                        throw new IllegalArgumentException(
                                "Choose a Chef action from 1 to 2.");
                }

            } catch (IllegalArgumentException
                    | IllegalStateException ex) {

                consoleUI.showError(
                        ex.getMessage()
                        + " Please choose again.");
            }
        }
    }

    // -------------------------------------------------
    // ELIGIBILITY CHECKS
    // -------------------------------------------------

    private void requireEligible(
            List<Integer> ids, String reason) {

        if (ids.isEmpty()) {
            throw new IllegalStateException(reason);
        }
    }

    private boolean hasFreeTable() {

        for (Table table :
                gameState.getTableManager().getTables()) {

            if (table.isAvailable()) {
                return true;
            }
        }

        return false;
    }

    private String seatUnavailableReason() {

        if (!hasFreeTable()) {
            return "All tables are occupied.";
        }

        return "There are no customers waiting for a table.";
    }

    private List<Integer> eligibleSeatCustomerIds() {

        List<Integer> ids = new ArrayList<>();

        if (hasFreeTable()) {

            for (Customer customer :
                    gameState.getCustomerManager()
                            .getWaitingCustomers()) {

                ids.add(customer.getId());
            }
        }

        return ids;
    }

    private List<Integer> eligibleTakeOrderCustomerIds() {

        List<Integer> ids = new ArrayList<>();

        for (Customer customer :
                gameState.getCustomerManager()
                        .getActiveCustomers()) {

            if (customer.getStatus() == CustomerStatus.SEATED) {
                ids.add(customer.getId());
            }
        }

        return ids;
    }

    private List<Integer> eligibleServeOrderIds() {

        List<Integer> ids = new ArrayList<>();

        for (Order order :
                gameState.getOrderManager().getOrders()) {

            if (order.canBeServed(gameState.getCurrentTurn())
                    && order.getCustomer().getStatus()
                            == CustomerStatus.ORDERED) {

                ids.add(order.getId());
            }
        }

        return ids;
    }

    private List<Integer> eligiblePrepareOrderIds() {

        List<Integer> ids = new ArrayList<>();

        for (Order order :
                gameState.getOrderManager().getOrders()) {

            if (order.canBePrepared()) {
                ids.add(order.getId());
            }
        }

        return ids;
    }

    // -------------------------------------------------
    // END OF TURN
    // -------------------------------------------------

    public void processEndOfTurn() {

        int currentTurn = gameState.getCurrentTurn();

        // Existing eating progress
        gameState.getCustomerManager()
                .advanceEating(currentTurn);

        // Cashier processes payment and tips
        Customer paidCustomer =
                cashier.processOldestEligiblePayment(
                        gameState, currentTurn);

        // Generate a review after successful payment
        if (paidCustomer != null) {
            recordCustomerReview(paidCustomer);
        }

        // Reduce satisfaction and find newly abandoned customers
        List<Customer> newlyAbandoned =
                gameState.getCustomerManager()
                        .applyEndTurnSatisfactionLoss();

        for (Customer customer : newlyAbandoned) {

            // Display red abandonment message for:
            // 1. Customers waiting in the queue
            // 2. Customers seated at a table
            //
            // Must happen BEFORE the table is released
            consoleUI.showCustomerAbandonment(customer);

            // Cancel any unfinished unpaid order
            gameState.getOrderManager()
                    .cancelUnpaidOrderFor(customer);

            // Release the table if one was assigned
            // Safe for queue customers without tables
            gameState.getTableManager()
                    .releaseTable(customer);

            // Update abandonment statistics
            gameState.recordAbandonment();

            // Generate BAD customer feedback
            recordCustomerReview(customer);
        }
    }

    // -------------------------------------------------
    // CUSTOMER REVIEWS
    // -------------------------------------------------

    private void recordCustomerReview(Customer customer) {

        Review review;

        if (customer.getStatus() == CustomerStatus.ABANDONED) {

            review = gameState.getReviewManager()
                    .recordDeparture(
                            customer,
                            FeedbackType.BAD,
                            "I waited too long!");

        } else if (customer.getStatus() == CustomerStatus.PAID) {

            Order order = customer.getOrder();

            CookingQuality quality =
                    order.getCookingQuality();

            // Polymorphism determines customer feedback
            FeedbackType feedback =
                    customer.getFeedbackType(quality);

            String comment =
                    customer.getFeedbackComment(quality);

            review = gameState.getReviewManager()
                    .recordDeparture(
                            customer,
                            feedback,
                            comment);

        } else {

            throw new IllegalStateException(
                    "Customer must leave before giving feedback.");
        }

        // White review message without the green [OK] prefix
        System.out.println(
                AnsiColors.paint(
                        "  [REVIEW] " + review.toDisplayString(),
                        "\u001B[37m"));
    }

    // -------------------------------------------------
    // VICTORY CONDITIONS
    // -------------------------------------------------

    public boolean hasWon() {

        return gameState.getPaidCustomerCount()
                >= GameConfig.REQUIRED_PAID_CUSTOMERS
                && gameState.getRevenueCents()
                >= GameConfig.REQUIRED_REVENUE_CENTS
                && gameState.getAveragePaidSatisfaction()
                >= GameConfig.REQUIRED_AVERAGE_SATISFACTION;
    }
}
