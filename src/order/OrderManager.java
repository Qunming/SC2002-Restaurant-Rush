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

    // creates an order for a seated customer
    public Order createOrder(Customer customer, Menu menu) {
        if (customer == null || menu == null) {
            throw new IllegalArgumentException(
                    "Customer and menu cannot be null.");
        }

        if (customer.getStatus() != CustomerStatus.SEATED) {
            throw new IllegalStateException(
                    "Only seated customers can place orders.");
        }

        // let customer choose their own food
        MenuItem selectedFood = customer.chooseMenuItem(menu);

        Order newOrder = new Order(
                nextOrderId,
                customer,
                selectedFood);

        // assigns the order to the customer
        customer.assignOrder(newOrder);

        orders.add(newOrder);
        nextOrderId++;

        return newOrder;
    }

    // finds an order using its ID
    public Order getOrderById(int orderId) {
        for (Order order : orders) {
            if (order.getId() == orderId) {
                return order;
            }
        }

        return null;
    }

    // returns all orders
    public List<Order> getOrders() {
        return List.copyOf(orders);
    }

    // returns orders that can be prepared
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

    // returns orders that are ready to be served
    public List<Order> getReadyOrders() {
        List<Order> readyOrders = new ArrayList<>();

        for (Order order : orders) {
            if (order.getStatus() == OrderStatus.READY) {
                readyOrders.add(order);
            }
        }

        return List.copyOf(readyOrders);
    }

    // cancels an unfinished order when its customer abandons the restaurant
    public void cancelUnpaidOrderFor(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException(
                    "Customer cannot be null.");
        }

        if (customer.getStatus() != CustomerStatus.ABANDONED) {
            throw new IllegalStateException(
                    "Only abandoned customers can have their orders cancelled.");
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
