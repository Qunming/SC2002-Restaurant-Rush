
package menu;

public class ComboMeal extends MenuItem {

    private final MenuItem firstItem;
    private final MenuItem secondItem;

    public ComboMeal(MenuItem firstItem, MenuItem secondItem) {
        super(
                createCode(firstItem, secondItem),
                createName(firstItem, secondItem),
                calculatePrice(firstItem, secondItem),
                calculatePreparationUnits(firstItem, secondItem)
        );

        if (firstItem == secondItem) {
            throw new IllegalArgumentException(
                    "Combo must contain two different menu items.");
        }

        this.firstItem = firstItem;
        this.secondItem = secondItem;
    }

    public MenuItem getFirstItem() {
        return firstItem;
    }

    public MenuItem getSecondItem() {
        return secondItem;
    }

    private static String createCode(MenuItem first, MenuItem second) {
        validateItems(first, second);

        return "COMBO_" + first.getCode()
                + "_" + second.getCode();
    }

    private static String createName(MenuItem first, MenuItem second) {
        validateItems(first, second);

        return first.getName() + " + "
                + second.getName() + " Combo";
    }

    // combo price is 90% of the combined price
    private static int calculatePrice(MenuItem first, MenuItem second) {
        validateItems(first, second);

        long combinedPrice = (long) first.getPriceCents()
                + second.getPriceCents();

        return Math.toIntExact((combinedPrice * 90 + 50) / 100);
    }

    // preparation units are the sum of both items
    private static int calculatePreparationUnits(
            MenuItem first, MenuItem second) {

        validateItems(first, second);

        return Math.addExact(
                first.getPreparationUnits(),
                second.getPreparationUnits());
    }

    private static void validateItems(MenuItem first, MenuItem second) {
        if (first == null || second == null) {
            throw new IllegalArgumentException(
                    "Combo items cannot be null.");
        }
    }
}
