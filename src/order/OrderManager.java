package order;

import customer.Customer;
import java.util.ArrayList;
import java.util.List;
import menu.Menu;

/** Member 3: Creates orders, finds them, and cancels abandoned ones. */
public class OrderManager {
    private final List<Order> orders = new ArrayList<>();
    private int nextOrderId = 1;

    public Order createOrder(Customer customer, Menu menu) {
        // TODO M3: Validate SEATED; ask Customer.chooseMenuItem(menu).
        // Create Order(nextOrderId++, customer, selectedFood), save it,
        // call customer.assignOrder(order), and return it.
        throw new UnsupportedOperationException("TODO M3: create order");
    }

    public Order getOrderById(int orderId) {
        // TODO M3: Return the matching order; return null if missing.
        throw new UnsupportedOperationException("TODO M3: find order");
    }

    public List<Order> getOrders() {
        return List.copyOf(orders);
    }

    public List<Order> getCookableOrders() {
        // TODO M3: PLACED/PREPARING orders only.
        throw new UnsupportedOperationException("TODO M3: cookable orders");
    }

    public List<Order> getReadyOrders() {
        // TODO M3: READY orders only.
        throw new UnsupportedOperationException("TODO M3: ready orders");
    }

    public void cancelUnpaidOrderFor(Customer customer) {
        // TODO M3: Cancel any unfinished/unpaid order for an abandoned customer.
        throw new UnsupportedOperationException("TODO M3: cancel customer's order");
    }
}
