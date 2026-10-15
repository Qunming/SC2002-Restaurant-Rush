package menu;

import java.util.ArrayList;
import java.util.List;

/** Member 3: Fixed Stage 1 menu, in agreed food-selection order. */
public class Menu {
    private final List<MenuItem> availableMenuItems = new ArrayList<>();

    public Menu() {
        availableMenuItems.add(new MenuItem("SALAD", "Salad", 900, 1));
        availableMenuItems.add(new MenuItem("BURGER", "Burger", 1500, 1));
        availableMenuItems.add(new MenuItem("PASTA", "Pasta", 1800, 2));
    }

    public List<MenuItem> getItems() {
        return List.copyOf(availableMenuItems);
    }

    public MenuItem getItemByCode(String itemCode) {
        // TODO M3: Find by menu code; return null if missing.
        throw new UnsupportedOperationException("TODO M3: find menu item");
    }
}
