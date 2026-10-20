package menu;

import java.util.ArrayList;
import java.util.List;

public class Menu {
    private final List<MenuItem> availableMenuItems = new ArrayList<>();

    // creates the default Stage 1 menu
    public Menu() {
        availableMenuItems.add(
                new MenuItem("SALAD", "Salad", 900, 1));
        availableMenuItems.add(
                new MenuItem("BURGER", "Burger", 1500, 1));
        availableMenuItems.add(
                new MenuItem("PASTA", "Pasta", 1800, 2));
    }

    // returns the available menu items
    public List<MenuItem> getItems() {
        return List.copyOf(availableMenuItems);
    }

    // finds a menu item using its code
    public MenuItem getItemByCode(String itemCode) {
        if (itemCode == null) {
            return null;
        }

        for (MenuItem item : availableMenuItems) {
            if (item.getCode().equalsIgnoreCase(itemCode)) {
                return item;
            }
        }

        return null;
    }
}
