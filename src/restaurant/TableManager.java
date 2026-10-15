package restaurant;

import customer.Customer;
import java.util.ArrayList;
import java.util.List;

/** Member 2: Allocates and releases numbered tables. */
public class TableManager {
    private final List<Table> tables = new ArrayList<>();

    public TableManager(int numberOfTables) {
        if (numberOfTables <= 0) {
            throw new IllegalArgumentException("Must create at least one table");
        }
        for (int tableNumber = 1; tableNumber <= numberOfTables; tableNumber++) {
            tables.add(new Table(tableNumber));
        }
    }

    public List<Table> getTables() {
        return List.copyOf(tables);
    }

    public Table findAvailableTable() {
        // TODO M2: Find first available table; return null if full.
        throw new UnsupportedOperationException("TODO M2: find available table");
    }

    public boolean seatCustomer(Customer customer) {
        // TODO M2: Only WAITING customer with a free table can be seated.
        // Ensure Table.occupy and Customer.seatAt both succeed.
        // Return false on predictable failure (no table, wrong status).
        throw new UnsupportedOperationException("TODO M2: allocate table");
    }

    public void releaseTable(Customer customer) {
        // TODO M2: Release assigned table only if this customer is its occupant
        // and their status is PAID or ABANDONED.
        throw new UnsupportedOperationException("TODO M2: release customer's table");
    }
}
