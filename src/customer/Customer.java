
package customer;

import java.util.List;
import menu.Menu;
import menu.MenuItem;
import order.Order;
import restaurant.Table;

public class Customer {

    // Constants
    private static final int STARTING_SATISFACTION = 100;
    private static final int WAITING_SATISFACTION_LOSS = 8;

    // Customer information
    private final int id;
    private int satisfaction = STARTING_SATISFACTION;
    private CustomerStatus status = CustomerStatus.WAITING;

    // Customer's assigned table and order
    private Table assignedTable;
    private Order order;

    // Track when the customer was served and finished eating
    private int servedTurn = -1;
    private int readyToPayTurn = -1;

    // Constructor
    public Customer(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Customer ID must be positive");
        }

        this.id = id;
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getSatisfaction() {
        return satisfaction;
    }

    public CustomerStatus getStatus() {
        return status;
    }

    public Table getTable() {
        return assignedTable;
    }

    public Order getOrder() {
        return order;
    }

    public int getServedTurn() {
        return servedTurn;
    }

    public int getReadyToPayTurn() {
        return readyToPayTurn;
    }

    // Customer selects food automatically
    public MenuItem chooseMenuItem(Menu menu) {

        if (menu == null) {
            throw new IllegalArgumentException(
                "Menu cannot be null"
            );
        }

        List<MenuItem> availableMenuItems = menu.getItems();    // Create a list of available menu items

        if (availableMenuItems.isEmpty()) {
            throw new IllegalArgumentException("Menu is empty");
        }

        int selectedIndex = (id - 1) % availableMenuItems.size();

        MenuItem selectedDish = availableMenuItems.get(selectedIndex);

        return selectedDish;
    }

    // Change customer status from WAITING to SEATED
    public void seatAt(Table table) {

        if (status != CustomerStatus.WAITING || table == null) {
            throw new IllegalArgumentException("Customer cannot be seated");
        }

        assignedTable = table;
        status = CustomerStatus.SEATED;
    }

    // Change customer status from SEATED to ORDERED
    public void assignOrder(Order newOrder) {

        if (status != CustomerStatus.SEATED         //If customer not seated
                || newOrder == null                 // Customer has yet to order
                || newOrder.getCustomer() != this)  //checks whether the order belongs to the customer
                {
                    throw new IllegalArgumentException("Cannot assign order to customer");
        }

        order = newOrder;
        status = CustomerStatus.ORDERED;
    }

    // Mark customer as served
    public void markServed(int currentTurn) {

        if (status != CustomerStatus.ORDERED || currentTurn < 1) {
            throw new IllegalArgumentException("Customer cannot be served");
        }

        status = CustomerStatus.SERVED;
        servedTurn = currentTurn;
    }

    // Customer finishes eating on a later turn
    public void markReadyToPay(int currentTurn) {

        if (status != CustomerStatus.SERVED || currentTurn <= servedTurn) {
            throw new IllegalArgumentException("Customer has not finished eating");
        }

        status = CustomerStatus.READY_TO_PAY;
        readyToPayTurn = currentTurn;
    }

    // Mark customer as paid
    public void markPaid() {

        if (status != CustomerStatus.READY_TO_PAY) {
            throw new IllegalArgumentException("Customer is not ready to pay");
        }

        status = CustomerStatus.PAID;
    }

    // Customer abandons the restaurant
    public void abandon() {

        if (!isAwaitingService() || satisfaction != 0) {
            throw new IllegalArgumentException("Customer cannot abandon");
        }

        status = CustomerStatus.ABANDONED;
    }

    // Decrease satisfaction while customer is unserved
    public void decreaseWaitingSatisfaction() {

        if (isAwaitingService()) {
            satisfaction = Math.max(0,satisfaction - WAITING_SATISFACTION_LOSS);    // ENsures satisfaction level is >= 0
        }
    }

    // Check whether the customer is still awaiting service
    private boolean isAwaitingService() {

        return status == CustomerStatus.WAITING
                || status == CustomerStatus.SEATED
                || status == CustomerStatus.ORDERED;
    }
}
