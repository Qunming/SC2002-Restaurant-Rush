
package feedback;

import customer.Customer;
import customer.CustomerStatus;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// Manages customer reviews after they leave the restaurant.
// Prevents duplicate reviews and maintains review history.

public class ReviewManager {

    private final List<Review> reviews = new ArrayList<>();
    private final Set<Integer> reviewedCustomers = new HashSet<>();

    // Record feedback when a customer leaves the restaurant
    public Review recordDeparture(
            Customer customer,
            FeedbackType feedbackType,
            String comment) {

        if (customer == null) {
            throw new IllegalArgumentException(
                    "Customer cannot be null.");
        }

        // Only customers who have left may submit reviews
        CustomerStatus status = customer.getStatus();

        if (status != CustomerStatus.PAID
                && status != CustomerStatus.ABANDONED) {
            throw new IllegalStateException(
                    "Customer has not left the restaurant.");
        }

        // Prevent the same customer from reviewing twice
        if (reviewedCustomers.contains(customer.getId())) {
            throw new IllegalStateException(
                    "Customer has already given feedback.");
        }

        // Abandoned customers must give negative feedback
        if (status == CustomerStatus.ABANDONED
                && feedbackType != FeedbackType.BAD) {
            throw new IllegalArgumentException(
                    "Abandoned customers must give BAD feedback.");
        }

        // Create a review with the customer's information
        Review review = new Review(
                customer.getId(),
                customer.getCustomerType(),
                feedbackType,
                comment
        );

        // Save the review
        reviews.add(review);
        reviewedCustomers.add(customer.getId());

        return review;
    }

    // Retrieve all recorded reviews
    public List<Review> getReviews() {
        return List.copyOf(reviews);
    }

    // Check whether a customer has already given feedback
    public boolean hasReviewed(int customerId) {
        return reviewedCustomers.contains(customerId);
    }

    // Return the total number of reviews
    public int getReviewCount() {
        return reviews.size();
    }
}
