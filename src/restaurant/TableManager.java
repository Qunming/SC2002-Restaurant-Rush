
package restaurant;

import customer.Customer;
import customer.CustomerStatus;
import java.util.ArrayList;
import java.util.List;

public class TableManager {

    // Store the 2 tables in a list
    private final List<Table> tables = new ArrayList<>();

    // Constructor
    public TableManager(int numberOfTables) {

        if (numberOfTables <= 0) {
            throw new IllegalArgumentException("Number of tables must be positive");
        }

        for (int tableNumber = 1; tableNumber <= numberOfTables; tableNumber++) {
            tables.add(new Table(tableNumber));
        }
    }

    // Return the list of tables
    public List<Table> getTables() {
        return List.copyOf(tables);     // To not expose internal mutable list
    }

    // Find the first available table
    public Table findAvailableTable() {

        for (int i = 0; i < tables.size(); i++) {

            Table table = tables.get(i);

            if (table.isAvailable()) {
                return table;
            }
        }

        return null;
    }

    // Seat a waiting customer at an available table
    public boolean seatCustomer(Customer customer) {

        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null");
        }
        
        //The customer must be WAITING and not already have an assigned table.
        if (customer.getStatus() != CustomerStatus.WAITING || customer.getTable() != null) {
            return false;
        }

        Table availableTable = findAvailableTable();

        if (availableTable == null) {
            return false;
        }

        availableTable.occupy(customer);
        customer.seatAt(availableTable);

        return true;
    }

    // Release a table after payment or abandonment
    public void releaseTable(Customer customer) {

        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null");
        }

        CustomerStatus status = customer.getStatus();

        if (status != CustomerStatus.PAID && status != CustomerStatus.ABANDONED) {

            throw new IllegalStateException("Customer has not paid or abandoned");
        }

        Table assignedTable = customer.getTable();

        if (assignedTable == null) {
            return;
        }

        if (!tables.contains(assignedTable) || assignedTable.getOccupant() != customer) {

            throw new IllegalStateException(
                "Customer does not occupy the assigned table");
        }

        assignedTable.release();
    }
}
