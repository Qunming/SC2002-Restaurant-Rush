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
        while (true) {
            ui.showWaiterMenu(); // Displays choices: 1. Seat, 2. Take Order, 3. Serve, 4. Wait
            int choice = inputHandler.readInt("Enter waiter action: ");
    
            try {
                switch (choice) {
                    case 1:
                        // Seat customer logic
                        if (seatCustomerAction()) {
                            return; // Success! Exit loop and end turn action
                        }
                        ui.showError("No available tables or waiting customers.");
                        break;
                    case 2:
                        // Take order logic
                        if (takeOrderAction()) {
                            return;
                        }
                        ui.showError("No seated customers ready to order.");
                        break;
                    case 3:
                        // Serve order logic
                        if (serveOrderAction()) {
                            return;
                        }
                        ui.showError("No ready orders available to serve.");
                        break;
                    case 4:
                        ui.showMessage("Waiter is waiting this turn.");
                        return; // Valid skip, exit loop
                    default:
                        ui.showError("Invalid choice. Please pick between 1 and 4.");
                }
            } catch (IllegalArgumentException | IllegalStateException e) {
                // Catch any backend rules violation and retry without crashing or consuming a turn
                ui.showError(e.getMessage());
            }
        }
    }
    

    public void handleChefChoice() {
        // TODO M1: Read Prepare / Wait, AFTER the waiter's action.
        while (true) {
        ui.showChefMenu(); // Displays choices: 1. Cook Order, 2. Wait
        int choice = inputHandler.readInt("Enter chef action: ");

        try {
            switch (choice) {
                case 1:
                    // Cook/Prepare order logic
                    if (cookOrderAction()) {
                        return; // Success! Exit loop
                    }
                    ui.showError("No cookable orders available.");
                    break;
                case 2:
                    ui.showMessage("Chef is waiting this turn.");
                    return; // Valid skip, exit loop
                default:
                    ui.showError("Invalid choice. Please pick 1 or 2.");
            }
        } catch (IllegalArgumentException | IllegalStateException e) {
            ui.showError(e.getMessage());
        }
    }
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
        return state.getPaidCustomerCount() >= GameConfig.REQUIRED_PAID_CUSTOMERS &&
           state.getRevenueCents() >= GameConfig.REQUIRED_REVENUE_CENTS &&
           state.getAveragePaidSatisfaction() >= GameConfig.REQUIRED_AVERAGE_SATISFACTION;
    }
}
