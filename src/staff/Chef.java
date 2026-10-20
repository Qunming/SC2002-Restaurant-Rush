
package staff;

import controller.GameState;
import order.Order;
import order.OrderStatus;

/** member 4: one preparation unit per turn or wait. */
public class Chef {
    private boolean actedThisTurn = false;

    // reset the chef at the start of each turn
    public void resetForNewTurn() {
        actedThisTurn = false;
    }

    public boolean hasActedThisTurn() {
        return actedThisTurn;
    }

    // prepare one cooking unit for an order
    public void prepareOrder(int orderId, GameState gameState) {
        if (gameState == null) {
            throw new IllegalArgumentException("Game state cannot be null.");
        }
        if (actedThisTurn) {
            throw new IllegalStateException("Chef has already acted this turn.");
        }

        Order order = gameState.getOrderManager().getOrderById(orderId);

        if (order == null) {
            throw new IllegalArgumentException("Order not found.");
        }
        if (order.getStatus() != OrderStatus.PLACED
                && order.getStatus() != OrderStatus.PREPARING) {
            throw new IllegalStateException(
                    "Order is not available for preparation.");
        }

        // advance cooking by one unit
        order.prepareOneUnit(gameState.getCurrentTurn());

        actedThisTurn = true;
    }

    // use the chef's action to wait
    public void waitThisTurn() {
        if (actedThisTurn) {
            throw new IllegalStateException("Chef has already acted this turn.");
        }

        actedThisTurn = true;
    }
}
