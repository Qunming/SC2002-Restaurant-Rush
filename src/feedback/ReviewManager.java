
package feedback;

import customer.Customer;
import customer.CustomerStatus;

import java.util.HashSet;
import java.util.Set;

public class ReviewManager {

    private final Set<Integer> reviewedCustomers = new HashSet<>();

    public Review recordDeparture(Customer customer) {

        if (customer == null) {
            throw new IllegalArgumentException(
                    "Customer cannot be null.");
        }

        if (reviewedCustomers.contains(customer.getId())) {
            throw new IllegalStateException(
                    "Customer already gave feedback.");
        }

        FeedbackType type;
        String comment;

        if (customer.getStatus() == CustomerStatus.ABANDONED) {

            type = FeedbackType.BAD;
            comment = "I waited too long!";

        } else if (customer.getStatus() == CustomerStatus.PAID) {

            if (customer.getOrder() == null) {
                throw new IllegalStateException(
                        "Paid customer has no order.");
            }

            type = customer.getFeedbackType(
                    customer.getOrder().getCookingQuality());

            comment = customer.getFeedbackComment(
                    customer.getOrder().getCookingQuality());

        } else {
            throw new IllegalStateException(
                    "Feedback is only generated on departure.");
        }

        reviewedCustomers.add(customer.getId());

        return new Review(
                customer.getId(),
                customer.getCustomerType(),
                type,
                comment);
    }
}
