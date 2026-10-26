
package order;

import customer.Customer;
import customer.CustomerStatus;
import java.util.ArrayList;
import java.util.List;
import menu.Menu;
import menu.MenuItem;

// Manages order creation, retrieval, and cancellation
public class OrderManager {

    private final List<Order> orders = new ArrayList<>();
    private int nextOrderId = 1;

    // Create an order for a seated customer
    ```java
// Create an order for a seated customer
public Order createOrder(Customer customer, Menu menu) {
    if (customer == null || menu == null) {
        throw new IllegalArgumentException(
            "Customer and menu cannot be null.");
    }

    if (customer.getStatus() != CustomerStatus.SEATED) {
        throw new IllegalStateException(
            "Only seated customers can place orders.");
    }

    // Prevent a customer from ordering twice
    if (customer.getOrder() != null) {
        throw new IllegalStateException(
            "Customer already has an order.");
    }

    // Customer automatically chooses food
    MenuItem selectedFood = customer.chooseMenuItem(menu);

    if (selectedFood == null) {
        throw new IllegalStateException(
            "Customer did not select a menu item.");
    }

    // Create the order with the customer's discount
    Order newOrder = new Order(
        nextOrderId,
        customer,
        selectedFood
    );

    // Assign the order to the customer
    customer.assignOrder(newOrder);

    // Store the new order
    orders.add(newOrder);
    nextOrderId++;

    return newOrder;
}
```

    // Find an order using its ID
    public Order getOrderById(int orderId) {
        for (Order order : orders) {
            if (order.getId() == orderId) {
                return order;
            }
        }

        return null;
    }

    // Return all orders
    public List<Order> getOrders() {
        return List.copyOf(orders);
    }

    // Return orders that can still be cooked
    public List<Order> getCookableOrders() {
        List<Order> cookableOrders = new ArrayList<>();

        for (Order order : orders) {
            if (order.getStatus() == OrderStatus.PLACED
                    || order.getStatus() == OrderStatus.PREPARING) {
                cookableOrders.add(order);
            }
        }

        return List.copyOf(cookableOrders);
    }

    // Return orders that are ready to serve
    public List<Order> getReadyOrders() {
        List<Order> readyOrders = new ArrayList<>();

        for (Order order : orders) {
            if (order.getStatus() == OrderStatus.READY) {
                readyOrders.add(order);
            }
        }

        return List.copyOf(readyOrders);
    }

    // Cancel an unpaid order when its customer abandons
    public void cancelUnpaidOrderFor(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException(
                "Customer cannot be null.");
        }

        if (customer.getStatus() != CustomerStatus.ABANDONED) {
            throw new IllegalStateException(
                "Only abandoned customers can have orders cancelled.");
        }

        for (Order order : orders) {

            // Find the customer's order
            if (order.getCustomer() == customer) {

                OrderStatus status = order.getStatus();

                // Only cancel orders that have not been served
                if (status == OrderStatus.PLACED
                        || status == OrderStatus.PREPARING
                        || status == OrderStatus.READY) {
                    order.cancel();
                }

                // Each customer has only one order in Stage 1
                return;
            }
        }
    }
}
