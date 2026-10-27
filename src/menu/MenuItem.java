
package menu;

// Stores the details of a menu item
public class MenuItem {

    private final String code;
    private final String name;
    private final int priceCents;
    private final int preparationUnits;

    // Constructor
    public MenuItem(String code, String name, int priceCents,
                    int preparationUnits) {
        if (code == null || code.isBlank()
                || name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                "Code and name cannot be empty.");
        }

        if (priceCents < 0 || preparationUnits <= 0) {
            throw new IllegalArgumentException(
                "Invalid price or preparation units.");
        }

        this.code = code;
        this.name = name;
        this.priceCents = priceCents;
        this.preparationUnits = preparationUnits;
    }

    // Getters
    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getPriceCents() {
        return priceCents;
    }

    public int getPreparationUnits() {
        return preparationUnits;
    }

    // Display the menu item
    @Override
    public String toString() {
        return code + " - " + name;
    }
}
