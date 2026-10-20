package controller;

import customer.Customer;
import customer.CustomerStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import order.Order;
import order.OrderStatus;
import restaurant.Table;
import staff.Cashier;
import staff.Chef;
import staff.Waiter;
import ui.ConsoleUI;
import ui.InputHandler;

/**
 * Coordinates Stage 1 turns. Always offers all staff actions, but provides
 * the UI with eligible IDs and explains failed choices without consuming tasks.
 * Restaurant rules are enforced by the staff and domain classes.
 */
public class GameController {
    private final GameState gameState;
    private final ConsoleUI consoleUI;
    private final InputHandler inputHandler;
    private final Waiter waiter;
    private final Chef chef;
    private final Cashier cashier;

    private int nextCustomerId = 1;

    public GameController(GameState gameState, ConsoleUI consoleUI,
                          InputHandler inputHandler, Waiter waiter,
                          Chef chef, Cashier cashier) {
        this.gameState = Objects.requireNonNull(gameState, "gameState");
        this.consoleUI = Objects.requireNonNull(consoleUI, "consoleUI");
        this.inputHandler = Objects.requireNonNull(inputHandler, "inputHandler");
        this.waiter = Objects.requireNonNull(waiter, "waiter");
        this.chef = Objects.requireNonNull(chef, "chef");
        this.cashier = Objects.requireNonNull(cashier, "cashier");
    }

    public void run() {
        while (gameState.getCurrentTurn() <= GameConfig.MAX_TURNS) {
            waiter.resetForNewTurn();
            chef.resetForNewTurn();
            playTurn();

            if (gameState.getCurrentTurn() == GameConfig.MAX_TURNS) {
                break;
            }
            gameState.advanceTurn();
        }
        consoleUI.showFinalResult(gameState, hasWon());
    }

    public void playTurn() {
        int currentTurn = gameState.getCurrentTurn();

        if (GameConfig.isArrivalTurn(currentTurn)) {
            gameState.getCustomerManager().addCustomer(new Customer(nextCustomerId++));
        }

        consoleUI.showStartOfTurn(gameState, waiter, chef);
        handleWaiterChoice();
        handleChefChoice();
        processEndOfTurn();
        consoleUI.showEndOfTurn(gameState);
    }

    /** Fixed action numbers, with hints reflecting the current game state. */
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
                        int customerId = inputHandler.readTargetId("customer", seatIds);
                        waiter.seatCustomer(customerId, gameState);
                        consoleUI.showMessage("Seated Customer #" + customerId + ".");
                        return;
                    }
                    case 2: {
                        requireEligible(takeIds,
                                "No seated customer is waiting to place an order.");
                        int customerId = inputHandler.readTargetId("customer", takeIds);
                        waiter.takeOrder(customerId, gameState);
                        Customer customer = gameState.getCustomerManager()
                                .getCustomerById(customerId);
                        Order placed = customer.getOrder();
                        consoleUI.showMessage("Customer #" + customerId
                                + " ordered " + placed.getItem().getName()
                                + " (Order #" + placed.getId() + ").");
                        return;
                    }
                    case 3: {
                        requireEligible(serveIds,
                                "No order was READY before this turn.");
                        int orderId = inputHandler.readTargetId("order", serveIds);
                        waiter.serveOrder(orderId, gameState);
                        consoleUI.showMessage("Served Order #" + orderId + ".");
                        return;
                    }
                    case 4:
                        waiter.waitThisTurn();
                        consoleUI.showMessage("Waiter waits this turn.");
                        return;

                    case 5:
                        consoleUI.showHistory(gameState);
                        // No staff action consumed. Return to Waiter menu.
                        break;

                    default:
                        throw new IllegalArgumentException(
                                "Choose a Waiter action from 1 to 5.");
                }
            } catch (IllegalArgumentException | IllegalStateException ex) {
                consoleUI.showError(ex.getMessage()
                        + " No staff task was used; please choose again.");
            }
        }
    }

    /** Chef choices refresh after Waiter, including orders placed this turn. */
    public void handleChefChoice() {
        while (true) {
            List<Integer> prepareIds = eligiblePrepareOrderIds();
            int choice = inputHandler.readChefChoice(prepareIds);

            try {
                switch (choice) {
                    case 1: {
                        requireEligible(prepareIds,
                                "No order is currently PLACED or PREPARING.");
                        int orderId = inputHandler.readTargetId("order", prepareIds);
                        chef.prepareOrder(orderId, gameState);
                        consoleUI.showMessage("Prepared one unit of Order #"
                                + orderId + ".");
                        return;
                    }
                    case 2:
                        chef.waitThisTurn();
                        consoleUI.showMessage("Chef waits this turn.");
                        return;

                    default:
                        throw new IllegalArgumentException(
                                "Choose a Chef action from 1 to 2.");
                }
            } catch (IllegalArgumentException | IllegalStateException ex) {
                consoleUI.showError(ex.getMessage()
                        + " No staff task was used; please choose again.");
            }
        }
    }

    private void requireEligible(List<Integer> ids, String reason) {
        if (ids.isEmpty()) {
            throw new IllegalStateException(reason);
        }
    }

    private boolean hasFreeTable() {
        for (Table table : gameState.getTableManager().getTables()) {
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
            for (Customer customer : gameState.getCustomerManager().getWaitingCustomers()) {
                ids.add(customer.getId());
            }
        }
        return ids;
    }

    private List<Integer> eligibleTakeOrderCustomerIds() {
        List<Integer> ids = new ArrayList<>();
        for (Customer customer : gameState.getCustomerManager().getActiveCustomers()) {
            if (customer.getStatus() == CustomerStatus.SEATED) {
                ids.add(customer.getId());
            }
        }
        return ids;
    }

    private List<Integer> eligibleServeOrderIds() {
        List<Integer> ids = new ArrayList<>();
        for (Order order : gameState.getOrderManager().getOrders()) {
            if (order.getStatus() == OrderStatus.READY
                    && order.getReadyTurn() < gameState.getCurrentTurn()
                    && order.getCustomer().getStatus() == CustomerStatus.ORDERED) {
                ids.add(order.getId());
            }
        }
        return ids;
    }

    private List<Integer> eligiblePrepareOrderIds() {
        List<Integer> ids = new ArrayList<>();
        for (Order order : gameState.getOrderManager().getOrders()) {
            if (order.getStatus() == OrderStatus.PLACED
                    || order.getStatus() == OrderStatus.PREPARING) {
                ids.add(order.getId());
            }
        }
        return ids;
    }

    /** Existing Stage 1 timing, cashier and abandonment logic is unchanged. */
    public void processEndOfTurn() {
        int currentTurn = gameState.getCurrentTurn();
        gameState.getCustomerManager().advanceEating(currentTurn);
        cashier.processOldestEligiblePayment(gameState, currentTurn);

        List<Customer> newlyAbandoned = gameState.getCustomerManager()
                .applyEndTurnSatisfactionLoss();
        for (Customer customer : newlyAbandoned) {
            gameState.getOrderManager().cancelUnpaidOrderFor(customer);
            gameState.getTableManager().releaseTable(customer);
            gameState.recordAbandonment();
        }
    }

    public boolean hasWon() {
        return gameState.getPaidCustomerCount()
                >= GameConfig.REQUIRED_PAID_CUSTOMERS
                && gameState.getRevenueCents()
                >= GameConfig.REQUIRED_REVENUE_CENTS
                && gameState.getAveragePaidSatisfaction()
                >= GameConfig.REQUIRED_AVERAGE_SATISFACTION;
    }
}
