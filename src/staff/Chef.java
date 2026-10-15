package staff;

import controller.GameState;

/** Member 4: One preparation UNIT per turn or Wait. */
public class Chef {
    private boolean actedThisTurn = false;

    public void resetForNewTurn() { actedThisTurn = false; }
    public boolean hasActedThisTurn() { return actedThisTurn; }

    public void prepareOrder(int orderId, GameState gameState) {
        // TODO M4: Validate the order; prepare exactly one cooking unit.
        throw new UnsupportedOperationException("TODO M4: chef cooking");
    }

    public void waitThisTurn() {
        // TODO M4: Consume one chef action by waiting.
        throw new UnsupportedOperationException("TODO M4: chef waiting");
    }
}
