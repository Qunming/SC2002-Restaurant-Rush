
package customer;

/**
 * Stage 2: Regular customer.
 * Maintains the original Stage 1 customer behaviour.
 */
public class RegularCustomer extends Customer {

    private static final int REGULAR_SATISFACTION_LOSS = 8;

    public RegularCustomer(int id) {
        super(id);
    }

    // Identify this customer as Regular.
    @Override
    public String getCustomerType() {
        return "Regular";
    }

    // Regular customers lose 8 satisfaction per unserved turn.
    @Override
    public int getWaitingSatisfactionLoss() {
        return REGULAR_SATISFACTION_LOSS;
    }

    // Regular customers do not receive a discount.
    @Override
    public int getDiscountPercent() {
        return 0;
    }
}
