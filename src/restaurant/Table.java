
package restaurant;

import customer.Customer;

public class Table {

    // Table information
    private final int tableNumber;
    private Customer occupant;

    // Constructor
    public Table(int tableNumber) {

        if (tableNumber <= 0) {
            throw new IllegalArgumentException("Table number must be positive");
        }

        this.tableNumber = tableNumber;
    }

    // Getters
    public int getNumber() {
        return tableNumber;
    }

    public Customer getOccupant() {
        return occupant;
    }

    // Check if the table is available
    public boolean isAvailable() {
        return occupant == null;
    }

    // Assign a customer to this table
    public void occupy(Customer customer) {

        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null");
        }

        if (!isAvailable()) {
            throw new IllegalStateException("Table " + tableNumber + " is already occupied");
        }

        occupant = customer;
    }

    // Remove the customer from this table
    public void release() {
        occupant = null;
    }
}
