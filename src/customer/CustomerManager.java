package customer;

import java.util.ArrayList;
import java.util.List;

/** Member 2: Manages ALL customers and their queue/eating rules. */
public class CustomerManager {
    private final List<Customer> customers = new ArrayList<>();

    public void addCustomer(Customer customer) {
        // TODO M2: Reject null and duplicate IDs; preserve arrival order.
        throw new UnsupportedOperationException("TODO M2: add customer");
    }

    public Customer getCustomerById(int customerId) {
        // TODO M2: Return matching Customer; return null if ID is unknown.
        throw new UnsupportedOperationException("TODO M2: find customer");
    }

    public List<Customer> getWaitingCustomers() {
        // TODO M2: WAITING customers, in arrival order; no modifiable internal list.
        throw new UnsupportedOperationException("TODO M2: waiting customers");
    }

    public List<Customer> getActiveCustomers() {
        // TODO M2: Exclude PAID and ABANDONED customers.
        throw new UnsupportedOperationException("TODO M2: active customers");
    }

    public List<Customer> getPaidCustomers() {
        // TODO M2: PAID customers only.
        throw new UnsupportedOperationException("TODO M2: paid customers");
    }

    public Customer getOldestReadyToPayCustomer(int currentTurn) {
        // TODO M2: Earliest readyToPayTurn < currentTurn; tied -> earlier arrival.
        // Return null if no customer is eligible.
        throw new UnsupportedOperationException("TODO M2: eligible payment customer");
    }

    public void advanceEating(int currentTurn) {
        // TODO M2: SERVED in an earlier turn -> READY_TO_PAY now.
        throw new UnsupportedOperationException("TODO M2: advance eating");
    }

    public List<Customer> applyEndTurnSatisfactionLoss() {
        // TODO M2: Apply waiting penalty; call abandon() at zero.
        // Return ONLY newly abandoned customers for controller cleanup.
        throw new UnsupportedOperationException("TODO M2: end-of-turn satisfaction");
    }
}
