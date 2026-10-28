
package customer;

/**
 * Stage 2: VIP customer.
 * VIP customers lose less satisfaction while waiting
 * and receive a 10% discount on their orders.
 */
public class VIPCustomer extends Customer {

    private static final int VIP_SATISFACTION_LOSS = 5;
    private static final int VIP_DISCOUNT_PERCENT = 10;

    public VIPCustomer(int id) {
        super(id);
    }

    // Identify this customer as VIP.
    @Override
    public String getCustomerType() {
        return "VIP";
    }

    // VIP customers lose only 5 satisfaction per unserved turn.
    @Override
    public int getWaitingSatisfactionLoss() {
        return VIP_SATISFACTION_LOSS;
    }

    // VIP customers receive a 10% discount.
    @Override
    public int getDiscountPercent() {
        return VIP_DISCOUNT_PERCENT;
    }
}
