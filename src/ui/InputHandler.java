package ui;

import java.util.Scanner;

/** Member 1: Reads input only, never enforces restaurant business rules. */
public class InputHandler {
    private final Scanner scanner;

    public InputHandler(Scanner scanner) {
        this.scanner = scanner;
    }

    public int readInt(String prompt) {
        // TODO M1: Re-prompt on non-integer input without crashing.
        throw new UnsupportedOperationException("TODO M1: read integer");
    }

    public int readWaiterChoice() {
        // TODO M1: 1 Seat, 2 Take Order, 3 Serve, 4 Wait.
        throw new UnsupportedOperationException("TODO M1: waiter menu");
    }

    public int readChefChoice() {
        // TODO M1: 1 Prepare Order, 2 Wait.
        throw new UnsupportedOperationException("TODO M1: chef menu");
    }

    public int readCustomerId() {
        // TODO M1: Ask for existing customer ID; staff handles validation.
        throw new UnsupportedOperationException("TODO M1: customer ID input");
    }

    public int readOrderId() {
        // TODO M1: Ask for an order ID; staff handles validation.
        throw new UnsupportedOperationException("TODO M1: order ID input");
    }
}
