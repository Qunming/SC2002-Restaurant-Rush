
package customer;

import java.util.List;
import menu.Menu;
import menu.MenuItem;

/**
 * Stage 2: Critic customer.
 * Critic customers lose satisfaction faster while waiting
 * and prefer the most expensive available menu item.
 */
public class CriticCustomer extends Customer {

    private static final int CRITIC_SATISFACTION_LOSS = 12;

    public CriticCustomer(int id) {
        super(id);
    }

    // Identify this customer as a Critic.
    @Override
    public String getCustomerType() {
        return "Critic";
    }

    // Critic customers lose 12 satisfaction per unserved turn.
    @Override
    public int getWaitingSatisfactionLoss() {
        return CRITIC_SATISFACTION_LOSS;
    }

    // Critic customers do not receive a discount.
    @Override
    public int getDiscountPercent() {
        return 0;
    }

    // Critic customers prefer the most expensive menu item.
    @Override
    public MenuItem chooseMenuItem(Menu menu) {

        if (menu == null) {
            throw new IllegalArgumentException("Menu cannot be null.");
        }

        List<MenuItem> items = menu.getItems();

        if (items.isEmpty()) {
            throw new IllegalStateException("Menu has no items.");
        }

        MenuItem preferredItem = items.get(0);

        for (MenuItem item : items) {

            if (item.getPriceCents() > preferredItem.getPriceCents()) {
                preferredItem = item;
            }
        }

        return preferredItem;
    }
}
