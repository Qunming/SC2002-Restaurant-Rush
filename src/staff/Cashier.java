
package staff;

import controller.GameState;
import customer.Customer;
import order.Order;
import order.OrderStatus;
import restaurant.Table;

/** Collects at most one automatic payment per turn. */
public class Cashier {
    private int lastPaymentTurn = -1;

    public Customer processOldestEligiblePayment(GameState gameState, int currentTurn) {
        if (gameState == null) {
            throw new IllegalArgumentException("Game state cannot be null.");
        }
        if (currentTurn != gameState.getCurrentTurn()) {
            throw new IllegalArgumentException("Payment turn does not match the game turn.");
        }
        if (lastPaymentTurn == currentTurn) {
            return null;
        }

        // Only customers ready to pay since an earlier turn are eligible.
        Customer customer = gameState.getCustomerManager()
                .getOldestReadyToPayCustomer(currentTurn);
        if (customer == null) {
            return null;
        }

        Order order = customer.getOrder();
        if (order == null || order.getCustomer() != customer
                || order.getStatus() != OrderStatus.SERVED) {
            throw new IllegalStateException("Customer has no valid served order.");
        }

        // Validate ownership before recording money or changing payment status.
        Table table = customer.getTable();
        if (table == null
                || !gameState.getTableManager().getTables().contains(table)
                || table.getOccupant() != customer) {
            throw new IllegalStateException("Customer does not occupy a restaurant table.");
        }

        // GameState marks the customer PAID and records revenue/satisfaction.
        gameState.recordPayment(customer, order);
        order.markPaid();
        gameState.getTableManager().releaseTable(customer);

        lastPaymentTurn = currentTurn;
        return customer;
    }
}
