package controller;

import customer.Customer;
import java.util.List;
import staff.Cashier;
import staff.Chef;
import staff.Waiter;
import ui.ConsoleUI;
import ui.InputHandler;

/** Member 1: Coordinates the stages of one turn; rules stay with their owners. */
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
        this.gameState = gameState;
        this.consoleUI = consoleUI;
        this.inputHandler = inputHandler;
        this.waiter = waiter;
        this.chef = chef;
        this.cashier = cashier;
    }

    public void run() {
        while (gameState.getCurrentTurn() <= GameConfig.MAX_TURNS) {
            waiter.resetForNewTurn();
            chef.resetForNewTurn();
            playTurn();

            if (gameState.getCurrentTurn() == GameConfig.MAX_TURNS) {
                break;  // Display final state as turn 18, not turn 19.
            }
            gameState.advanceTurn();
        }
        consoleUI.showFinalResult(gameState, hasWon());
    }

    public void playTurn() {
        int currentTurn = gameState.getCurrentTurn();

        // Arrival must happen BEFORE the start-of-turn dashboard.
        if (GameConfig.isArrivalTurn(currentTurn)) {
            gameState.getCustomerManager().addCustomer(new Customer(nextCustomerId++));
        }

        consoleUI.showStartOfTurn(gameState, waiter, chef);
        handleWaiterChoice();
        handleChefChoice();
        processEndOfTurn();
        consoleUI.showEndOfTurn(gameState);
    }

    public void handleWaiterChoice() {
        // TODO M1: Read Seat / Take Order / Serve / Wait.
        // Invalid staff actions must retry without consuming a turn.
        throw new UnsupportedOperationException("TODO M1: handle waiter choice");
    }

    public void handleChefChoice() {
        // TODO M1: Read Prepare / Wait, AFTER the waiter's action.
        throw new UnsupportedOperationException("TODO M1: handle chef choice");
    }

    public void processEndOfTurn() {
        int currentTurn = gameState.getCurrentTurn();

        // Exact order: eating -> eligible payment -> satisfaction -> cleanup.
        gameState.getCustomerManager().advanceEating(currentTurn);
        cashier.processOldestEligiblePayment(gameState, currentTurn);
        List<Customer> newlyAbandoned =
                gameState.getCustomerManager().applyEndTurnSatisfactionLoss();
        for (Customer customer : newlyAbandoned) {
            gameState.getOrderManager().cancelUnpaidOrderFor(customer);
            gameState.getTableManager().releaseTable(customer);
            gameState.recordAbandonment();
        }
    }

    public boolean hasWon() {
        // TODO M1: >= 4 PAID, >= 4500 cents, avg satisfaction >= 60.
        throw new UnsupportedOperationException("TODO M1: victory conditions");
    }
}
