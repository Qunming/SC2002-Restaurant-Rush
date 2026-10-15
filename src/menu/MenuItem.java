package menu;

/** Member 3: One food item with price stored as integer cents. */
public class MenuItem {
    private final String code;
    private final String name;
    private final int priceCents;
    private final int preparationUnits;

    public MenuItem(String code, String name, int priceCents, int preparationUnits) {
        this.code = code;
        this.name = name;
        this.priceCents = priceCents;
        this.preparationUnits = preparationUnits;
    }

    public String getCode() { return code; }
    public String getName() { return name; }
    public int getPriceCents() { return priceCents; }
    public int getPreparationUnits() { return preparationUnits; }
}
