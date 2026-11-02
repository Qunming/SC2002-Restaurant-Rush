
package customer;

import java.util.List;

import menu.Menu;
import menu.MenuItem;
import feedback.FeedbackType;
import order.CookingQuality;

public class CriticCustomer extends Customer {

    private static final int CRITIC_SATISFACTION_LOSS = 12;

    public CriticCustomer(int id) {
        super(id);
    }

    @Override
    public String getCustomerType() {
        return "Critic";
    }

    @Override
    public int getWaitingSatisfactionLoss() {
        return CRITIC_SATISFACTION_LOSS;
    }

    @Override
    public int getDiscountPercent() {
        return 0;
    }

    // Addtional Feature: Critic reviews cooking quality
    @Override
    public FeedbackType getFeedbackType(CookingQuality quality) {

        if (quality == null) {
            throw new IllegalArgumentException(
                    "Cooking quality cannot be null.");
        }

        if (quality == CookingQuality.PERFECT) {
            return FeedbackType.GOOD;
        }

        return FeedbackType.BAD;
    }

    @Override
    public String getFeedbackComment(CookingQuality quality) {

        if (getFeedbackType(quality) == FeedbackType.GOOD) {
            return "Outstanding preparation!";
        }

        return "The cooking quality was unacceptable.";
    }

    // Stage 2: Critic prefers the most expensive food
    @Override
    public MenuItem chooseMenuItem(Menu menu) {

        if (menu == null) {
            throw new IllegalArgumentException(
                    "Menu cannot be null.");
        }

        List<MenuItem> items = menu.getItems();

        if (items.isEmpty()) {
            throw new IllegalStateException(
                    "Menu has no items.");
        }

        MenuItem preferredItem = items.get(0);

        for (MenuItem item : items) {

            if (item.getPriceCents()
                    > preferredItem.getPriceCents()) {

                preferredItem = item;
            }
        }

        return preferredItem;
    }
}
