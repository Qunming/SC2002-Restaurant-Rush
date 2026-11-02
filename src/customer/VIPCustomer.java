
package customer;

import feedback.FeedbackType;
import order.CookingQuality;

public class VIPCustomer extends Customer {

    private static final int VIP_SATISFACTION_LOSS = 5;
    private static final int VIP_DISCOUNT_PERCENT = 10;

    public VIPCustomer(int id) {
        super(id);
    }

    @Override
    public String getCustomerType() {
        return "VIP";
    }

    @Override
    public int getWaitingSatisfactionLoss() {
        return VIP_SATISFACTION_LOSS;
    }

    @Override
    public int getDiscountPercent() {
        return VIP_DISCOUNT_PERCENT;
    }

    // Addtional Feature: VIP customers expect PERFECT food
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
            return "Excellent preparation!";
        }

        return "Disappointing. I expected better quality.";
    }
}
