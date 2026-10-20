
package staff;

import controller.GameState;
import customer.Customer;
import customer.CustomerStatus;
import order.Order;
import order.OrderStatus;

/** member 4: one valid seat / take order / serve / wait per turn. */
public class Waiter {
    private boolean actedThisTurn = false;

    // reset the waiter at the start of each turn
    public void resetForNewTurn() {
        actedThisTurn = false;
    }

    public boolean hasActedThisTurn() {
        return actedThisTurn;
    }

    // seat a waiting customer at an available table
    public void seatCustomer(int customerId, GameState gameState) {
        if (gameState == null) {
            throw new IllegalArgumentException("Game state cannot be null.");
        }
        if (actedThisTurn) {
            throw new IllegalStateException("Waiter has already acted this turn.");
        }

        Customer customer = gameState.getCustomerManager()
                                     .getCustomerById(customerId);

        if (customer == null) {
            throw new IllegalArgumentException("Customer not found.");
        }
        if (customer.getStatus() != CustomerStatus.WAITING) {
            throw new IllegalStateException("Customer is not waiting.");
        }

        // use the table manager to seat the customer
        boolean seated = gameState.getTableManager().seatCustomer(customer);

        if (!seated) {
            throw new IllegalStateException("No available table.");
        }

        actedThisTurn = true;
    }

    // take an order from a seated customer
    public void takeOrder(int customerId, GameState gameState) {
        if (gameState == null) {
            throw new IllegalArgumentException("Game state cannot be null.");
        }
        if (actedThisTurn) {
            throw new IllegalStateException("Waiter has already acted this turn.");
        }

        Customer customer = gameState.getCustomerManager()
                                     .getCustomerById(customerId);

        if (customer == null) {
            throw new IllegalArgumentException("Customer not found.");
        }
        if (customer.getStatus() != CustomerStatus.SEATED) {
            throw new IllegalStateException("Customer must be seated first.");
        }

        // let the customer choose their food automatically
        gameState.getOrderManager().createOrder(
                customer, gameState.getMenu());

        actedThisTurn = true;
    }

    // serve an order that was ready before this turn
    public void serveOrder(int orderId, GameState gameState) {
        if (gameState == null) {
            throw new IllegalArgumentException("Game state cannot be null.");
        }
        if (actedThisTurn) {
            throw new IllegalStateException("Waiter has already acted this turn.");
        }

        Order order = gameState.getOrderManager().getOrderById(orderId);

        if (order == null) {
            throw new IllegalArgumentException("Order not found.");
        }
        if (order.getStatus() != OrderStatus.READY
                || order.getReadyTurn() >= gameState.getCurrentTurn()) {
            throw new IllegalStateException(
                    "Order must be ready from an earlier turn.");
        }

        Customer customer = order.getCustomer();

        if (customer.getStatus() != CustomerStatus.ORDERED) {
            throw new IllegalStateException("Customer cannot be served yet.");
        }

        // update the order and customer after serving
        order.markServed(gameState.getCurrentTurn());
        customer.markServed(gameState.getCurrentTurn());
        gameState.recordServing();

        actedThisTurn = true;
    }

    // use the waiter's action to wait
    public void waitThisTurn() {
        if (actedThisTurn) {
            throw new IllegalStateException("Waiter has already acted this turn.");
        }

        actedThisTurn = true;
    }
}
