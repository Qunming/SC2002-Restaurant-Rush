
package staff;

import controller.GameState;
import customer.Customer;
import customer.CustomerStatus;
import order.Order;
import order.OrderStatus;
import restaurant.Table;

// Collects at most one automatic payment per turn
public class Cashier {
    private int lastPaymentTurn = -1;

    // Process the oldest customer who is ready to pay
    public Customer processOldestEligiblePayment(
            GameState gameState, int currentTurn) {

        if (gameState == null) {
            throw new IllegalArgumentException(
                "Game state cannot be null.");
        }

        if (currentTurn != gameState.getCurrentTurn()) {
            throw new IllegalArgumentException(
                "Payment turn does not match the game turn.");
        }

        if (lastPaymentTurn == currentTurn) {
            return null;
        }

        // Find the oldest customer eligible to pay
        Customer customer = gameState.getCustomerManager()
                .getOldestReadyToPayCustomer(currentTurn);

        if (customer == null) {
            return null;
        }

        Order order = customer.getOrder();

        if (order == null
                || order.getCustomer() != customer
                || order.getStatus() != OrderStatus.SERVED
                || customer.getStatus() != CustomerStatus.READY_TO_PAY) {
            throw new IllegalStateException(
                "Customer has no valid order ready for payment.");
        }

        // Check that the customer still occupies a valid table
        Table table = customer.getTable();

        if (table == null
                || !gameState.getTableManager().getTables().contains(table)
                || table.getOccupant() != customer) {
            throw new IllegalStateException(
                "Customer does not occupy a restaurant table.");
        }

        // Record payment using the locked order price
        gameState.recordPayment(customer, order);
        order.markPaid();

        // Free the table after payment
        gameState.getTableManager().releaseTable(customer);

        lastPaymentTurn = currentTurn;
        return customer;
    }
}
