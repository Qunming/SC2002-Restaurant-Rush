
package controller;

import customer.Customer;
import customer.CustomerStatus;
import customer.RegularCustomer;
import customer.VIPCustomer;
import customer.CriticCustomer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import order.Order;
import order.OrderStatus;
import restaurant.Table;
import staff.Cashier;
import staff.Chef;
import staff.Host;
import staff.Waiter;
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

    private int nextCustomerId = 1;

    public GameController(GameState gameState, ConsoleUI consoleUI,
                          InputHandler inputHandler, Waiter waiter,
                          Host host, Chef chef, Cashier cashier) {
        this.gameState = Objects.requireNonNull(gameState, "gameState");
        this.consoleUI = Objects.requireNonNull(consoleUI, "consoleUI");
        this.inputHandler = Objects.requireNonNull(inputHandler, "inputHandler");
        this.waiter = Objects.requireNonNull(waiter, "waiter");
        this.host = Objects.requireNonNull(host, "host");
        this.chef = Objects.requireNonNull(chef, "chef");
        this.cashier = Objects.requireNonNull(cashier, "cashier");
    }

    public void run() {
        while (gameState.getCurrentTurn() <= GameConfig.MAX_TURNS) {
            waiter.resetForNewTurn();
            host.resetForNewTurn();
            chef.resetForNewTurn();
            playTurn();

            if (gameState.getCurrentTurn() == GameConfig.MAX_TURNS) {
                break;
            }
            gameState.advanceTurn();
        }
        consoleUI.showFinalResult(gameState, hasWon());
    }

    // turn sequence
    public void playTurn() {
        int currentTurn = gameState.getCurrentTurn();

        if (GameConfig.isArrivalTurn(currentTurn)) {
            int customerId = nextCustomerId++;
            gameState.getCustomerManager().addCustomer(
                    createArrivingCustomer(customerId));
        }

        handleHappyHourChoice();

        if (gameState.isHappyHourActive()) {
            gameState.getCustomerManager().applyHappyHourRecovery();
        }

        consoleUI.showStartOfTurn(gameState, waiter, chef);
        showHappyHourStatus();
        handleHostChoice();
        handleWaiterChoice();
        handleChefChoice();
        processEndOfTurn();
        consoleUI.showEndOfTurn(gameState);
    }

    // happy hour activation
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

    private void showHappyHourStatus() {
        if (gameState.isHappyHourActive()) {
            int remaining = gameState.getHappyHourTurnsRemaining();

            consoleUI.showMessage("Happy Hour ACTIVE: " + remaining
                    + " turn(s) remaining (including this turn)."
                    + " New orders: 20% off; unserved customers: +15 satisfaction.");

        } else if (gameState.hasHappyHourBeenUsed()) {
            consoleUI.showMessage(
                    "Happy Hour has ended and cannot be activated again.");

        } else {
            consoleUI.showMessage(
                    "Happy Hour is available to activate on a later turn.");
        }
    }

    // host action
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
                                "Host seated Customer #" + customerId + ".");
                        return;
                    }

                    case 2:
                        host.waitThisTurn();
                        consoleUI.showMessage("Host waits this turn.");
                        return;

                    default:
                        throw new IllegalArgumentException(
                                "Choose a Host action from 1 to 2.");
                }

            } catch (IllegalArgumentException | IllegalStateException ex) {
                consoleUI.showError(ex.getMessage()
                        + " No Host task was used; please choose again.");
            }
        }
    }

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

    // waiter action
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

                        Customer customer = gameState.getCustomerManager()
                                .getCustomerById(customerId);

                        Order placed = customer.getOrder();

                        consoleUI.showMessage(
                                customer.getCustomerType()
                                + " Customer #" + customerId
                                + " ordered " + placed.getItem().getName()
                                + " (Order #" + placed.getId()
                                + ", bill "
                                + String.format("$%.2f",
                                        placed.getFinalPriceCents() / 100.0)
                                + ").");
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
                        consoleUI.showMessage("Waiter waits this turn.");
                        return;

                    case 5:
                        consoleUI.showHistory(gameState);
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

    // chef action
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

                        chef.prepareOrder(orderId, gameState);

                        consoleUI.showMessage(
                                "Prepared one unit of Order #" + orderId + ".");
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
            for (Customer customer :
                    gameState.getCustomerManager().getWaitingCustomers()) {
                ids.add(customer.getId());
            }
        }
        return ids;
    }

    private List<Integer> eligibleTakeOrderCustomerIds() {
        List<Integer> ids = new ArrayList<>();

        for (Customer customer :
                gameState.getCustomerManager().getActiveCustomers()) {

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
                    && order.getCustomer().getStatus()
                            == CustomerStatus.ORDERED) {
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

    // end of turn
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
