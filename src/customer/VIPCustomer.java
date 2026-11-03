
package customer;

import feedback.FeedbackType;
import order.CookingQuality;

// VIP customers receive special benefits.
// They lose less satisfaction while waiting
// and receive a discount on their orders.
//
// Additional Feature:
// VIP customers expect PERFECT food quality.

public class VIPCustomer extends Customer {

    private static final int VIP_SATISFACTION_LOSS = 5;
    private static final int VIP_DISCOUNT_PERCENT = 10;

    public VIPCustomer(int id) {
        super(id);
    }

    // Identify this customer as VIP
    @Override
    public String getCustomerType() {
        return "VIP";
    }

    // VIP customers lose only 5 satisfaction per unserved turn
    @Override
    public int getWaitingSatisfactionLoss() {
        return VIP_SATISFACTION_LOSS;
    }

    // VIP customers receive a 10% discount
    @Override
    public int getDiscountPercent() {
        return VIP_DISCOUNT_PERCENT;
    }

    // -------------------------------------------------
    // ADDITIONAL FEATURE: VIP CUSTOMER FEEDBACK
    // -------------------------------------------------

    // VIP customers expect PERFECT cooking quality
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

    // Generate VIP-specific feedback comments
    @Override
    public String getFeedbackComment(CookingQuality quality) {

        FeedbackType feedback = getFeedbackType(quality);

        if (feedback == FeedbackType.GOOD) {
            return "Excellent quality! Worth the VIP treatment!";
        }

        return "I expected much better food quality.";
    }
}
