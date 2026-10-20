
package staff;

import controller.GameState;
import order.Order;
import order.OrderStatus;

/** Prepares at most one cooking unit per turn, or waits. */
public class Chef {
    private boolean actedThisTurn = false;

    public void resetForNewTurn() {
        actedThisTurn = false;
    }

    public boolean hasActedThisTurn() {
        return actedThisTurn;
    }

    // Prepare one unit of an existing order.
    public void prepareOrder(int orderId, GameState gameState) {
        if (actedThisTurn) {
            throw new IllegalStateException("Chef has already acted this turn.");
        }
        if (gameState == null) {
            throw new IllegalArgumentException("Game state cannot be null.");
        }

        Order order = gameState.getOrderManager().getOrderById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("Order #" + orderId + " does not exist.");
        }
        if (order.getStatus() != OrderStatus.PLACED
                && order.getStatus() != OrderStatus.PREPARING) {
            throw new IllegalStateException("Order #" + orderId + " cannot be cooked.");
        }

        order.prepareOneUnit(gameState.getCurrentTurn());
        actedThisTurn = true;
    }

    // Waiting also uses up the Chef's action.
    public void waitThisTurn() {
        if (actedThisTurn) {
            throw new IllegalStateException("Chef has already acted this turn.");
        }
        actedThisTurn = true;
    }
}
