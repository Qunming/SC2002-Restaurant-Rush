package restaurant;

import customer.Customer;

/** Member 2: One table knows its number and occupant. */
public class Table {
    private final int tableNumber;
    private Customer occupant;

    public Table(int tableNumber) {
        if (tableNumber <= 0) {
            throw new IllegalArgumentException("Table number must be positive");
        }
        this.tableNumber = tableNumber;
    }

    public int getNumber() { return tableNumber; }
    public Customer getOccupant() { return occupant; }

    public boolean isAvailable() {
        // TODO M2: Available if no occupant.
        throw new UnsupportedOperationException("TODO M2: table availability");
    }

    public void occupy(Customer customer) {
        // TODO M2: Reject null customer or occupied table.
        throw new UnsupportedOperationException("TODO M2: occupy table");
    }

    public void release() {
        // TODO M2: Set occupant to null. Only TableManager decides WHEN.
        throw new UnsupportedOperationException("TODO M2: release table");
    }
}
