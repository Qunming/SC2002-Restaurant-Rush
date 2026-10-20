
package staff;

import controller.GameState;
import customer.Customer;
import customer.CustomerStatus;
import order.Order;
import order.OrderStatus;

/** Performs one successful seating, ordering, serving, or waiting action per turn. */
public class Waiter {
    private boolean actedThisTurn = false;

    public void resetForNewTurn() {
        actedThisTurn = false;
    }

    public boolean hasActedThisTurn() {
        return actedThisTurn;
    }

    // Seat one waiting customer at a free table.
    public void seatCustomer(int customerId, GameState gameState) {
        checkAvailable();
        checkGameState(gameState);

        Customer customer = gameState.getCustomerManager().getCustomerById(customerId);
        if (customer == null) {
            throw new IllegalArgumentException("Customer #" + customerId + " does not exist.");
        }
        if (customer.getStatus() != CustomerStatus.WAITING) {
            throw new IllegalStateException("Customer #" + customerId + " is not waiting for a table.");
        }
        if (!gameState.getTableManager().seatCustomer(customer)) {
            throw new IllegalStateException("No free table is available.");
        }

        actedThisTurn = true;
    }

    // Take an order; the customer chooses the dish automatically.
    public void takeOrder(int customerId, GameState gameState) {
        checkAvailable();
        checkGameState(gameState);

        Customer customer = gameState.getCustomerManager().getCustomerById(customerId);
        if (customer == null) {
            throw new IllegalArgumentException("Customer #" + customerId + " does not exist.");
        }

        // OrderManager checks that the customer is seated and has no order.
        gameState.getOrderManager().createOrder(customer, gameState.getMenu());
        actedThisTurn = true;
    }

    // Serve an order that was READY before this turn.
    public void serveOrder(int orderId, GameState gameState) {
        checkAvailable();
        checkGameState(gameState);

        Order order = gameState.getOrderManager().getOrderById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("Order #" + orderId + " does not exist.");
        }

        int currentTurn = gameState.getCurrentTurn();
        if (order.getStatus() != OrderStatus.READY || order.getReadyTurn() >= currentTurn) {
            throw new IllegalStateException("Order #" + orderId + " is not ready to serve this turn.");
        }

        Customer customer = order.getCustomer();
        if (customer.getOrder() != order || customer.getStatus() != CustomerStatus.ORDERED) {
            throw new IllegalStateException("Customer and order states do not match.");
        }

        // Keep the order and customer states in sync.
        order.markServed(currentTurn);
        customer.markServed(currentTurn);
        gameState.recordServing();
        actedThisTurn = true;
    }

    // Waiting also uses up the Waiter's action.
    public void waitThisTurn() {
        checkAvailable();
        actedThisTurn = true;
    }

    private void checkAvailable() {
        if (actedThisTurn) {
            throw new IllegalStateException("Waiter has already acted this turn.");
        }
    }

    private void checkGameState(GameState gameState) {
        if (gameState == null) {
            throw new IllegalArgumentException("Game state cannot be null.");
        }
    }
}
