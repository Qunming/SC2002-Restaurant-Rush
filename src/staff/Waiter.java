package staff;

import controller.GameState;

/** Member 4: One valid Seat / Take Order / Serve / Wait per turn. */
public class Waiter {
    private boolean actedThisTurn = false;

    public void resetForNewTurn() { actedThisTurn = false; }
    public boolean hasActedThisTurn() { return actedThisTurn; }

    public void seatCustomer(int customerId, GameState gameState) {
        // TODO M4: Validate request, ask TableManager to seat, consume action on success.
        throw new UnsupportedOperationException("TODO M4: waiter seating");
    }

    public void takeOrder(int customerId, GameState gameState) {
        // TODO M4: Validate SEATED customer; OrderManager.createOrder(customer, menu).
        // Newly created order can be cooked in this same turn.
        throw new UnsupportedOperationException("TODO M4: waiter taking order");
    }

    public void serveOrder(int orderId, GameState gameState) {
        // TODO M4: Check order READY from earlier turn, mark order and customer served.
        throw new UnsupportedOperationException("TODO M4: waiter serving");
    }

    public void waitThisTurn() {
        // TODO M4: Consume one waiter action by waiting.
        throw new UnsupportedOperationException("TODO M4: waiter waiting");
    }
}
