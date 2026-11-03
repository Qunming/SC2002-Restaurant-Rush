
package customer;

import java.util.List;

import menu.Menu;
import menu.MenuItem;
import feedback.FeedbackType;
import order.CookingQuality;

// Critic customers lose satisfaction faster while waiting.
// They prefer the most expensive available menu item.
//
// Additional Feature:
// Critics evaluate the quality of prepared food.

public class CriticCustomer extends Customer {

    private static final int CRITIC_SATISFACTION_LOSS = 12;

    public CriticCustomer(int id) {
        super(id);
    }

    // Identify this customer as a Critic
    @Override
    public String getCustomerType() {
        return "Critic";
    }

    // Critics lose 12 satisfaction per unserved turn
    @Override
    public int getWaitingSatisfactionLoss() {
        return CRITIC_SATISFACTION_LOSS;
    }

    // Critics do not receive a discount
    @Override
    public int getDiscountPercent() {
        return 0;
    }

    // Critics prefer the most expensive menu item
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

    // -------------------------------------------------
    // ADDITIONAL FEATURE: CRITIC CUSTOMER FEEDBACK
    // -------------------------------------------------

    // Critics expect PERFECT cooking quality
    @Override
    public FeedbackType getFeedbackType(
            CookingQuality quality) {

        if (quality == null) {
            throw new IllegalArgumentException(
                    "Cooking quality cannot be null.");
        }

        if (quality == CookingQuality.PERFECT) {
            return FeedbackType.GOOD;
        }

        return FeedbackType.BAD;
    }

    // Generate Critic-specific feedback comments
    @Override
    public String getFeedbackComment(
            CookingQuality quality) {

        FeedbackType feedback = getFeedbackType(quality);

        if (feedback == FeedbackType.GOOD) {
            return "Impressive cooking! A truly excellent dish.";
        }

        return "Disappointing. The food did not meet my standards.";
    }
}
