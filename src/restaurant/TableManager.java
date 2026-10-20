
package restaurant;

import customer.Customer;
import customer.CustomerStatus;
import java.util.ArrayList;
import java.util.List;

/** Manages table allocation and release. */
public class TableManager {
    private final List<Table> tables = new ArrayList<>();

    public TableManager(int numberOfTables) {
        if (numberOfTables <= 0) {
            throw new IllegalArgumentException("Number of tables must be positive.");
        }
        for (int number = 1; number <= numberOfTables; number++) {
            tables.add(new Table(number));
        }
    }

    public List<Table> getTables() {
        return List.copyOf(tables);
    }

    public Table findAvailableTable() {
        for (Table table : tables) {
            if (table.isAvailable()) {
                return table;
            }
        }
        return null;
    }

    public boolean seatCustomer(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null.");
        }
        if (customer.getStatus() != CustomerStatus.WAITING || customer.getTable() != null) {
            return false;
        }

        Table table = findAvailableTable();
        if (table == null) {
            return false;
        }

        table.occupy(customer);
        customer.seatAt(table);
        return true;
    }

    public void releaseTable(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null.");
        }
        if (customer.getStatus() != CustomerStatus.PAID
                && customer.getStatus() != CustomerStatus.ABANDONED) {
            throw new IllegalStateException("Customer has not paid or abandoned.");
        }

        Table table = customer.getTable();
        if (table == null) {
            return; // A customer who was never seated has no table to release.
        }
        if (!tables.contains(table) || table.getOccupant() != customer) {
            throw new IllegalStateException("Customer does not occupy the assigned table.");
        }

        table.release();
        customer.clearTable(); // Keep both sides of the relationship consistent.
    }
}
