
package feedback;

// Stores the feedback given by a customer.
// Each review contains the customer's ID, type,
// feedback category, and written comment.

public class Review {

    private final int customerId;
    private final String customerType;
    private final FeedbackType feedbackType;
    private final String comment;

    // Constructor
    public Review(int customerId, String customerType,
                  FeedbackType feedbackType, String comment) {

        if (customerId <= 0) {
            throw new IllegalArgumentException(
                    "Customer ID must be positive.");
        }

        if (customerType == null || customerType.isBlank()) {
            throw new IllegalArgumentException(
                    "Customer type cannot be empty.");
        }

        if (feedbackType == null) {
            throw new IllegalArgumentException(
                    "Feedback type cannot be null.");
        }

        if (comment == null || comment.isBlank()) {
            throw new IllegalArgumentException(
                    "Feedback comment cannot be empty.");
        }

        this.customerId = customerId;
        this.customerType = customerType;
        this.feedbackType = feedbackType;
        this.comment = comment;
    }

    // Return the customer's ID
    public int getCustomerId() {
        return customerId;
    }

    // Return the customer's type
    public String getCustomerType() {
        return customerType;
    }

    // Return the feedback category
    public FeedbackType getFeedbackType() {
        return feedbackType;
    }

    // Return the written comment
    public String getComment() {
        return comment;
    }

    // Format the review for display
    public String toDisplayString() {
        return customerType + " Customer #" + customerId
                + " [" + feedbackType + "]: \""
                + comment + "\"";
    }
}
