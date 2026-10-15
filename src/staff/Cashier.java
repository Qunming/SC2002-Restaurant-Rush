package staff;

import controller.GameState;
import customer.Customer;

/** Member 4: Automatic payment, at most one customer each turn. */
public class Cashier {
    private int lastPaymentTurn = -1;

    public Customer processOldestEligiblePayment(GameState gameState, int currentTurn) {
        // TODO M4: Select oldest READY_TO_PAY since a previous turn.
        // Mark customer/order PAID; record statistics; release table.
        // Return the paid customer or null if none eligible.
        // Never accept a second payment in the same turn.
        throw new UnsupportedOperationException("TODO M4: automatic payment");
    }
}
