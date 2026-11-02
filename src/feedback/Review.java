
package feedback;

public class Review {

    private final int customerId;
    private final String customerType;
    private final FeedbackType feedbackType;
    private final String comment;

    public Review(int customerId, String customerType,
                  FeedbackType feedbackType, String comment) {

        this.customerId = customerId;
        this.customerType = customerType;
        this.feedbackType = feedbackType;
        this.comment = comment;
    }

    public int getCustomerId() {
        return customerId;
    }

    public FeedbackType getFeedbackType() {
        return feedbackType;
    }

    public String getComment() {
        return comment;
    }

    public String toDisplayString() {
        return customerType + " Customer #" + customerId
                + " [" + feedbackType + "]: \""
                + comment + "\"";
    }
}
