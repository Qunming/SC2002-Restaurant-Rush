
package policy;

import order.CookingQuality;
import order.Order;
import order.OrderStatus;

public final class TipCalculator {

    private static final int PERFECT_TIP_CENTS = 200;

    private TipCalculator() {
    }

    public static int calculateTipCents(Order order) {

        if (order == null) {
            throw new IllegalArgumentException(
                    "Order cannot be null.");
        }

        if (order.getStatus() == OrderStatus.PAID
                && order.getCookingQuality() == CookingQuality.PERFECT) {

            return PERFECT_TIP_CENTS;
        }

        return 0;
    }
}
