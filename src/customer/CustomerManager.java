
package customer;

import java.util.ArrayList;
import java.util.List;

public class CustomerManager {

    private static final int HAPPY_HOUR_RECOVERY = 15;

    private final List<Customer> customers = new ArrayList<>();

    // register a new customer
    public void addCustomer(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException(
                    "Customer cannot be null");
        }

        if (getCustomerById(customer.getId()) != null) {
            throw new IllegalArgumentException(
                    "Customer ID already exists");
        }

        customers.add(customer);
    }

    // find a customer by id
    public Customer getCustomerById(int customerId) {
        for (int i = 0; i < customers.size(); i++) {
            Customer customer = customers.get(i);

            if (customer.getId() == customerId) {
                return customer;
            }
        }

        return null;
    }

    // get customers waiting for a table
    public List<Customer> getWaitingCustomers() {
        List<Customer> waitingCustomers = new ArrayList<>();

        for (int i = 0; i < customers.size(); i++) {
            Customer customer = customers.get(i);

            if (customer.getStatus() == CustomerStatus.WAITING) {
                waitingCustomers.add(customer);
            }
        }

        return waitingCustomers;
    }

    // get customers who have not paid or abandoned
    public List<Customer> getActiveCustomers() {
        List<Customer> activeCustomers = new ArrayList<>();

        for (int i = 0; i < customers.size(); i++) {
            Customer customer = customers.get(i);
            CustomerStatus status = customer.getStatus();

            if (status != CustomerStatus.PAID
                    && status != CustomerStatus.ABANDONED) {
                activeCustomers.add(customer);
            }
        }

        return activeCustomers;
    }

    // get customers who have paid
    public List<Customer> getPaidCustomers() {
        List<Customer> paidCustomers = new ArrayList<>();

        for (int i = 0; i < customers.size(); i++) {
            Customer customer = customers.get(i);

            if (customer.getStatus() == CustomerStatus.PAID) {
                paidCustomers.add(customer);
            }
        }

        return paidCustomers;
    }

    // find the oldest customer eligible for payment
    public Customer getOldestReadyToPayCustomer(int currentTurn) {
        Customer oldestEligibleCustomer = null;

        for (int i = 0; i < customers.size(); i++) {
            Customer customer = customers.get(i);

            if (customer.getStatus() != CustomerStatus.READY_TO_PAY
                    || customer.getReadyToPayTurn() >= currentTurn) {
                continue;
            }

            if (oldestEligibleCustomer == null
                    || customer.getReadyToPayTurn()
                    < oldestEligibleCustomer.getReadyToPayTurn()) {
                oldestEligibleCustomer = customer;
            }
        }

        return oldestEligibleCustomer;
    }

    // update customers who have finished eating
    public void advanceEating(int currentTurn) {
        for (int i = 0; i < customers.size(); i++) {
            Customer customer = customers.get(i);

            if (customer.getStatus() == CustomerStatus.SERVED
                    && customer.getServedTurn() < currentTurn) {
                customer.markReadyToPay(currentTurn);
            }
        }
    }

    // stage 3: recover satisfaction during happy hour
    public void applyHappyHourRecovery() {
        for (Customer customer : customers) {
            customer.recoverSatisfaction(HAPPY_HOUR_RECOVERY);
        }
    }

    // reduce satisfaction and handle abandonment
    public List<Customer> applyEndTurnSatisfactionLoss() {
        List<Customer> newlyAbandonedCustomers = new ArrayList<>();

        for (int i = 0; i < customers.size(); i++) {
            Customer customer = customers.get(i);
            CustomerStatus status = customer.getStatus();

            boolean isUnserved =
                    status == CustomerStatus.WAITING
                    || status == CustomerStatus.SEATED
                    || status == CustomerStatus.ORDERED;

            if (!isUnserved) {
                continue;
            }

            customer.decreaseWaitingSatisfaction();

            if (customer.getSatisfaction() == 0) {
                customer.abandon();
                newlyAbandonedCustomers.add(customer);
            }
        }

        return newlyAbandonedCustomers;
    }
}
