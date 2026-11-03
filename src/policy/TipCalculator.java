
package policy;

import customer.Customer;
import customer.VIPCustomer;
import customer.CriticCustomer;
import order.CookingQuality;
import order.Order;
import order.OrderStatus;

// Additional Feature: Tipping System
// Tip amount depends on the customer type.
// Tips are separate from the original game revenue.

public final class TipCalculator {

    private static final int REGULAR_TIP_CENTS = 200;
    private static final int VIP_TIP_CENTS = 300;
    private static final int CRITIC_TIP_CENTS = 500;

    // Prevent objects of this utility class from being created
    private TipCalculator() {
    }

    // Calculate the tip earned from a paid order
    public static int calculateTipCents(Order order) {

        if (order == null) {
            throw new IllegalArgumentException(
                    "Order cannot be null.");
        }

        // Tips are only awarded for paid orders
        // with PERFECT cooking quality
        if (order.getStatus() != OrderStatus.PAID
                || order.getCookingQuality()
                        != CookingQuality.PERFECT) {

            return 0;
        }

        // Determine the customer type
        Customer customer = order.getCustomer();

        // Critic customers give $5
        if (customer instanceof CriticCustomer) {
            return CRITIC_TIP_CENTS;
        }

        // VIP customers give $3
        if (customer instanceof VIPCustomer) {
            return VIP_TIP_CENTS;
        }

        // Regular customers give $2
        return REGULAR_TIP_CENTS;
    }
}
