package menu;

public class MenuItem {
    private final String code;
    private final String name;
    private final int priceCents;
    private final int preparationUnits;

    // creates a menu item with its details
    public MenuItem(String code, String name, int priceCents,
            int preparationUnits) {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("Code cannot be empty.");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be empty.");
        }
        if (priceCents < 0) {
            throw new IllegalArgumentException("Price cannot be negative.");
        }
        if (preparationUnits <= 0) {
            throw new IllegalArgumentException(
                    "Preparation units must be positive.");
        }

        this.code = code;
        this.name = name;
        this.priceCents = priceCents;
        this.preparationUnits = preparationUnits;
    }

    // getters
    // returns the item code
    public String getCode() {
        return code;
    }

    // returns the item name
    public String getName() {
        return name;
    }

    // returns the price in cents
    public int getPriceCents() {
        return priceCents;
    }

    // returns the number of preparation units needed
    public int getPreparationUnits() {
        return preparationUnits;
    }
}
