
    // Additional Feature: Show a red alert when a customer abandons
    public void showCustomerAbandonment(Customer customer) {

        // Identify whether the customer occupied a table
        String location;

        if (customer.getTable() != null) {
            location = "Table " + customer.getTable().getNumber();
        } else {
            location = "the waiting queue";
        }

        // Display the entire abandonment message in red
        System.out.println(
                AnsiColors.paint(
                        "  [ABANDONED] "
                        + customer.getCustomerType()
                        + " Customer #" + customer.getId()
                        + " reached 0 satisfaction and left "
                        + location + "!",
                        AnsiColors.RED));
    }
