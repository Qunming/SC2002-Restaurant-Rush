
    // Display the restaurant status at the start of each turn
    // Follow the same interface structure as Stage 2

    public void showStartOfTurn(
            GameState state, Waiter waiter, Chef chef) {

        System.out.println();

        System.out.println(
                AnsiColors.paint(RULE, AnsiColors.CYAN));

        System.out.println(
                AnsiColors.paint(
                        " RESTAURANT RUSH  |  TURN "
                        + state.getCurrentTurn()
                        + " / " + GameConfig.MAX_TURNS,
                        AnsiColors.CYAN + AnsiColors.BOLD));

        System.out.println(
                AnsiColors.paint(RULE, AnsiColors.CYAN));

        // Display existing Stage 2 information
        showStatistics(state, false);
        showAlerts(state);
        showTables(state);
        showWaitingQueue(state);
        showActiveOrders(state);
    }
