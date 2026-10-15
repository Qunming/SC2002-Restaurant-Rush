package controller;

import java.util.Set;

/** Member 1: Stage 1 game-wide constants; no future-stage settings. */
public final class GameConfig {
    public static final int MAX_TURNS = 18;
    public static final int TABLE_COUNT = 2;
    public static final int REQUIRED_PAID_CUSTOMERS = 4;
    public static final int REQUIRED_REVENUE_CENTS = 4500;
    public static final int REQUIRED_AVERAGE_SATISFACTION = 60;
    private static final Set<Integer> CUSTOMER_ARRIVAL_TURNS = Set.of(1, 4, 7, 10, 13, 16);

    private GameConfig() { }

    public static boolean isArrivalTurn(int turnNumber) {
        return CUSTOMER_ARRIVAL_TURNS.contains(turnNumber);
    }
}
