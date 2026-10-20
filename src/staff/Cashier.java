
package staff;

import controller.GameState;
import customer.Customer;
import order.Order;
import order.OrderStatus;
import restaurant.Table;

/** Automatically processes at most one eligible payment per turn. */
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
            return null; // Already processed one payment this turn.
        }

        Customer customer = gameState.getCustomerManager()
                .getOldestReadyToPayCustomer(currentTurn);
        if (customer == null) {
            return null; // Nobody is eligible to pay yet.
        }

        Order order = customer.getOrder();
        if (order == null || order.getCustomer() != customer
                || order.getStatus() != OrderStatus.SERVED) {
            throw new IllegalStateException("Customer has no valid served order to pay for.");
        }

        // Check table ownership before recording payment to avoid partial updates.
        Table table = customer.getTable();
        if (table == null || table.getOccupant() != customer) {
            throw new IllegalStateException("Customer does not occupy a valid table.");
        }

        // GameState marks the customer PAID and records revenue/satisfaction.
        gameState.recordPayment(customer, order);
        order.markPaid();
        gameState.getTableManager().releaseTable(customer);

        lastPaymentTurn = currentTurn;
        return customer;
    }
}
