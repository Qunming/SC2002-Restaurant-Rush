
package order;

import customer.Customer;
import customer.CustomerStatus;
import java.util.ArrayList;
import java.util.List;
import menu.Menu;
import menu.MenuItem;

public class OrderManager {

    private final List<Order> orders = new ArrayList<>();
    private int nextOrderId = 1;

    // original order creation method
    public Order createOrder(Customer customer, Menu menu) {
        return createOrder(customer, menu, false);
    }

    // stage 3: create an order with optional happy hour discount
    public Order createOrder(Customer customer, Menu menu,
                             boolean happyHourActive) {
        if (customer == null || menu == null) {
            throw new IllegalArgumentException(
                    "Customer and menu cannot be null.");
        }

        if (customer.getStatus() != CustomerStatus.SEATED) {
            throw new IllegalStateException(
                    "Only seated customers can place orders.");
        }

        if (customer.getOrder() != null) {
            throw new IllegalStateException(
                    "Customer already has an order.");
        }

        // customer automatically chooses the food
        MenuItem selectedFood = customer.chooseMenuItem(menu);

        if (selectedFood == null) {
            throw new IllegalStateException(
                    "Customer did not select a menu item.");
        }

        // final price is calculated when the order is created
        Order newOrder = new Order(
                nextOrderId,
                customer,
                selectedFood,
                happyHourActive
        );

        customer.assignOrder(newOrder);

        orders.add(newOrder);
        nextOrderId++;

        return newOrder;
    }

    public Order getOrderById(int orderId) {
        for (Order order : orders) {
            if (order.getId() == orderId) {
                return order;
            }
        }

        return null;
    }

    public List<Order> getOrders() {
        return List.copyOf(orders);
    }

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

    public List<Order> getReadyOrders() {
        List<Order> readyOrders = new ArrayList<>();

        for (Order order : orders) {
            if (order.getStatus() == OrderStatus.READY) {
                readyOrders.add(order);
            }
        }

        return List.copyOf(readyOrders);
    }

    // cancel an unpaid order when the customer abandons
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
            if (order.getCustomer() == customer) {
                OrderStatus status = order.getStatus();

                if (status == OrderStatus.PLACED
                        || status == OrderStatus.PREPARING
                        || status == OrderStatus.READY) {
                    order.cancel();
                }

                return;
            }
        }
    }
}
