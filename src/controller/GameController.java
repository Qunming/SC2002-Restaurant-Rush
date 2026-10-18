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

        while (true) {
            
            int choice = inputHandler.readWaiterChoice;
    
            try {
                switch (choice) {
                    case 1:
                        if (seatCustomerAction()) {
                            return; 
                        }
                        ui.showError("No available tables or waiting customers.");
                        break;
                    case 2:
                        if (takeOrderAction()) {
                            return;
                        }
                        ui.showError("No seated customers ready to order.");
                        break;
                    case 3:
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
        while (true) {
        int choice = inputHandler.readChefChoice();

        try {
            switch (choice) {
                case 1:
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

    private boolean seatCustomerAction() {
        // 1. Read the customer ID from the player
        int customerId = inputHandler.readCustomerId();
    
        // 2. Find the customer in the backend
        Customer customer = gameState.getCustomerManager().getCustomerById(customerId);
        if (customer == null) {
            throw new IllegalArgumentException("Customer #" + customerId + " does not exist.");
        }
    
        // 3. Call TableManager's seatCustomer method directly
        boolean seated = gameState.getTableManager().seatCustomer(customer);
        
        if (!seated) {
            throw new IllegalStateException("Cannot seat customer. Either no tables are free or they are not waiting.");
        }
    
        ui.showMessage("Successfully seated Customer #" + customerId);
        return true; // Success! Breaks the while(true) loop
    }

    private boolean takeOrderAction() {
        // 1. Ask the player for the customer ID using your InputHandler
        int customerId = inputHandler.readCustomerId();
    
        // 2. Find the customer in the backend
        Customer customer = gameState.getCustomerManager().getCustomerById(customerId);
        if (customer == null) {
            throw new IllegalArgumentException("Customer #" + customerId + " does not exist.");
        }
    
        // 3. Validate that the customer is actually seated (and ready to order)
        if (customer.getStatus() != CustomerStatus.SEATED) {
            throw new IllegalStateException("Customer #" + customerId + " is not seated and cannot place an order.");
        }
    
        // 4. Tell OrderManager to create the order using the customer and the game's menu
        // (Assuming gameState provides access to orderManager and menu)
        gameState.getOrderManager().createOrder(customer, gameState.getMenu());
    
        ui.showMessage("Successfully took order for Customer #" + customerId + "!");
        return true; // Success! Breaks the while(true) loop
    }

    private boolean serveOrderAction() {
        // 1. Check if there are any ready orders available to serve
        List<Order> readyOrders = gameState.getOrderManager().getReadyOrders();
        if (readyOrders.isEmpty()) {
            return false; // No ready orders, show error
        }
    
        // 2. Read the Order ID from the player
        int orderId = inputHandler.readOrderId();
    
        // 3. Find the order in the backend
        Order order = gameState.getOrderManager().getOrderById(orderId);
        if (order == null || !readyOrders.contains(order)) {
            throw new IllegalArgumentException("Invalid Order ID or order is not ready to serve.");
        }
    
        // 4. Serve the order (updates order status to SERVED / hands it to the customer)
        int currentTurn = gameState.getCurrentTurn();
        order.markServed(currentTurn);
    
        ui.showMessage("Successfully served Order #" + orderId + "!");
        return true; // Success! Breaks the while(true) loop
}

    private boolean cookOrderAction() {
        // 1. Check if there are any cookable orders available first
        List<Order> cookableOrders = gameState.getOrderManager().getCookableOrders();
        if (cookableOrders.isEmpty()) {
            return false; // No orders to cook, let the while(true) loop show an error
        }
    
        // 2. Read the Order ID from the player using InputHandler
        int orderId = inputHandler.readOrderId();
    
        // 3. Find the order in the backend
        Order order = gameState.getOrderManager().getOrderById(orderId);
        if (order == null || !cookableOrders.contains(order)) {
            throw new IllegalArgumentException("Invalid Order ID or order cannot be cooked right now.");
        }
    
        // 4. Advance the order's preparation
        int currentTurn = gameState.getCurrentTurn();
        order.prepareOneUnit(currentTurn);
    
        ui.showMessage("Successfully progressed cooking for Order #" + orderId + "!");
        return true; // Success! Breaks the while(true) loop
    }
}
