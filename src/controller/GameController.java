
package controller;

import customer.Customer;
import java.util.List;
import java.util.Objects;
import staff.Cashier;
import staff.Chef;
import staff.Waiter;
import ui.ConsoleUI;
import ui.InputHandler;

/**
 * Stage 1 turn coordinator.
 *
 * Coordinates the order of operations but delegates restaurant rules to
 * staff, customer, order, and table classes.
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

    /** Run all 18 turns, then check the three victory requirements. */
    public void run() {
        while (gameState.getCurrentTurn() <= GameConfig.MAX_TURNS) {
            waiter.resetForNewTurn();
            chef.resetForNewTurn();
            playTurn();

            if (gameState.getCurrentTurn() == GameConfig.MAX_TURNS) {
                break; // Do not advance to turn 19.
            }
            gameState.advanceTurn();
        }

        consoleUI.showFinalResult(gameState, hasWon());
    }

    /** Execute one complete turn in the required Stage 1 order. */
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

    /** Let the manager choose one VALID Waiter action, or Wait. */
    public void handleWaiterChoice() {
        while (true) {
            int choice = inputHandler.readWaiterChoice();

            try {
                switch (choice) {
                    case 1: {
                        int customerId = inputHandler.readCustomerId();
                        waiter.seatCustomer(customerId, gameState);
                        consoleUI.showMessage("Seated customer #" + customerId + ".");
                        return;
                    }
                    case 2: {
                        int customerId = inputHandler.readCustomerId();
                        waiter.takeOrder(customerId, gameState);

                        // The customer chooses the food, never the manager.
                        Customer customer = gameState.getCustomerManager()
                                                       .getCustomerById(customerId);
                        if (customer != null && customer.getOrder() != null) {
                            consoleUI.showMessage("Customer #" + customerId
                                    + " ordered " + customer.getOrder().getItem().getName()
                                    + " (order #" + customer.getOrder().getId() + ").");
                        } else {
                            consoleUI.showMessage("Order taken for customer #" + customerId + ".");
                        }
                        return;
                    }
                    case 3: {
                        int orderId = inputHandler.readOrderId();
                        waiter.serveOrder(orderId, gameState);
                        consoleUI.showMessage("Served order #" + orderId + ".");
                        return;
                    }
                    case 4:
                        waiter.waitThisTurn();
                        consoleUI.showMessage("Waiter waits this turn.");
                        return;
                    default:
                        consoleUI.showError("Choose a Waiter action from 1 to 4.");
                }
            } catch (IllegalArgumentException | IllegalStateException e) {
                // Staff must not consume an action when validation fails.
                consoleUI.showError(e.getMessage());
            }
        }
    }

    /** The Chef acts after the Waiter and may prepare a newly placed order. */
    public void handleChefChoice() {
        while (true) {
            int choice = inputHandler.readChefChoice();

            try {
                switch (choice) {
                    case 1: {
                        int orderId = inputHandler.readOrderId();
                        chef.prepareOrder(orderId, gameState);
                        consoleUI.showMessage("Prepared one unit of order #" + orderId + ".");
                        return;
                    }
                    case 2:
                        chef.waitThisTurn();
                        consoleUI.showMessage("Chef waits this turn.");
                        return;
                    default:
                        consoleUI.showError("Choose a Chef action from 1 to 2.");
                }
            } catch (IllegalArgumentException | IllegalStateException e) {
                // Invalid requests can be retried in the SAME turn.
                consoleUI.showError(e.getMessage());
            }
        }
    }

    /** Resolve automatic end-of-turn events in the required order. */
    public void processEndOfTurn() {
        int currentTurn = gameState.getCurrentTurn();

        // Customers served in an earlier turn can finish eating now.
        gameState.getCustomerManager().advanceEating(currentTurn);

        // The Cashier handles at most one eligible payment automatically.
        cashier.processOldestEligiblePayment(gameState, currentTurn);

        // Unserved customers lose satisfaction; remove any new abandonments.
        List<Customer> newlyAbandoned = gameState.getCustomerManager()
                                                .applyEndTurnSatisfactionLoss();
        for (Customer customer : newlyAbandoned) {
            gameState.getOrderManager().cancelUnpaidOrderFor(customer);
            gameState.getTableManager().releaseTable(customer);
            gameState.recordAbandonment();
        }
    }

    /** Victory requires ALL three targets after the 18th turn. */
    public boolean hasWon() {
        return gameState.getPaidCustomerCount() >= GameConfig.REQUIRED_PAID_CUSTOMERS
                && gameState.getRevenueCents() >= GameConfig.REQUIRED_REVENUE_CENTS
                && gameState.getAveragePaidSatisfaction()
                   >= GameConfig.REQUIRED_AVERAGE_SATISFACTION;
    }
}
