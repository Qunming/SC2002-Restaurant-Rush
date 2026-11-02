
package staff;

import controller.GameState;
import customer.Customer;
import customer.CustomerStatus;

public class Host {
    private boolean actedThisTurn = false;

    // reset the host at the start of each turn
    public void resetForNewTurn() {
        actedThisTurn = false;
    }

    public boolean hasActedThisTurn() {
        return actedThisTurn;
    }

    // seat one waiting customer
    public void seatCustomer(int customerId, GameState gameState) {
        if (gameState == null) {
            throw new IllegalArgumentException(
                    "Game state cannot be null.");
        }

        if (actedThisTurn) {
            throw new IllegalStateException(
                    "Host has already acted this turn.");
        }

        Customer customer = gameState.getCustomerManager()
                .getCustomerById(customerId);

        if (customer == null) {
            throw new IllegalArgumentException(
                    "Customer not found.");
        }

        if (customer.getStatus() != CustomerStatus.WAITING) {
            throw new IllegalStateException(
                    "Customer is not waiting.");
        }

        // use the existing table manager to seat the customer
        boolean seated = gameState.getTableManager()
                .seatCustomer(customer);

        if (!seated) {
            throw new IllegalStateException(
                    "No available table.");
        }

        actedThisTurn = true;
    }

    // use the host's action to wait
    public void waitThisTurn() {
        if (actedThisTurn) {
            throw new IllegalStateException(
                    "Host has already acted this turn.");
        }

        actedThisTurn = true;
    }
}
