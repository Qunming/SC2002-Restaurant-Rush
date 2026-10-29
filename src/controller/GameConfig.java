
package controller;

import java.util.Set;

/** Game-wide settings shared by all stages. */
public final class GameConfig {
    public static final int MAX_TURNS = 18;
    public static final int TABLE_COUNT = 2;
    public static final int REQUIRED_PAID_CUSTOMERS = 4;
    public static final int REQUIRED_REVENUE_CENTS = 4500;
    public static final int REQUIRED_AVERAGE_SATISFACTION = 60;

    // Stage 3: Happy Hour lasts for the activation turn
    // and the following turn.
    public static final int HAPPY_HOUR_DURATION_TURNS = 2;

    private static final Set<Integer> CUSTOMER_ARRIVAL_TURNS =
            Set.of(1, 4, 7, 10, 13, 16);

    private GameConfig() { }

    public static boolean isArrivalTurn(int turnNumber) {
        return CUSTOMER_ARRIVAL_TURNS.contains(turnNumber);
    }
}
