
package staff;

import controller.GameState;
import customer.Customer;
import order.Order;
import order.OrderStatus;
import restaurant.Table;

/** automatically processes at most one payment per turn. */
public class Cashier {
    private int lastPaymentTurn = -1;

    // process the oldest customer who is eligible to pay
    public Customer processOldestEligiblePayment(
            GameState gameState, int currentTurn) {

        if (gameState == null) {
            throw new IllegalArgumentException("Game state cannot be null.");
        }
        if (currentTurn != gameState.getCurrentTurn()) {
            throw new IllegalArgumentException(
                    "Payment turn does not match the game turn.");
        }
        if (lastPaymentTurn == currentTurn) {
            return null; // already processed one payment this turn
        }

        Customer customer = gameState.getCustomerManager()
                .getOldestReadyToPayCustomer(currentTurn);

        if (customer == null) {
            return null; // nobody is eligible to pay yet
        }

        Order order = customer.getOrder();

        if (order == null || order.getCustomer() != customer
                || order.getStatus() != OrderStatus.SERVED) {
            throw new IllegalStateException(
                    "Customer has no valid served order to pay for.");
        }

        // check that the customer still occupies their table
        Table table = customer.getTable();

        if (table == null || table.getOccupant() != customer) {
            throw new IllegalStateException(
                    "Customer does not occupy a valid table.");
        }

        // record the payment and update the order
        gameState.recordPayment(customer, order);
        order.markPaid();

        // free the table after payment
        gameState.getTableManager().releaseTable(customer);

        lastPaymentTurn = currentTurn;
        return customer;
    }
}
