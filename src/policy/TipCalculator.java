
package policy;

import order.CookingQuality;
import order.Order;
import order.OrderStatus;

// Additional Feature: Tipping System
// Calculates tips based on cooking quality.
// Tips are separate from the original game revenue.

public final class TipCalculator {

    private static final int PERFECT_TIP_CENTS = 200;

    // Prevent objects of this utility class from being created
    private TipCalculator() {
    }

    // Calculate the tip earned from an order
    public static int calculateTipCents(Order order) {

        if (order == null) {
            throw new IllegalArgumentException(
                    "Order cannot be null.");
        }

        // Only paid orders with PERFECT cooking earn a tip
        if (order.getStatus() == OrderStatus.PAID
                && order.getCookingQuality()
                        == CookingQuality.PERFECT) {

            return PERFECT_TIP_CENTS;
        }

        // No tip for NORMAL food or unpaid orders
        return 0;
    }
}
