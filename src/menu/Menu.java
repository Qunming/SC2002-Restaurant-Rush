
package menu;

import java.util.ArrayList;
import java.util.List;

// Stores and manages the available menu items
public class Menu {

    private final List<MenuItem> items = new ArrayList<>();

    // Constructor
    public Menu() {
        items.add(new MenuItem("SALAD", "Salad", 900, 1));
        items.add(new MenuItem("BURGER", "Burger", 1500, 1));
        items.add(new MenuItem("PASTA", "Pasta", 1800, 2));
    }

    // Return all menu items
    public List<MenuItem> getItems() {
        return List.copyOf(items);
    }

    // Find a menu item using its code
    public MenuItem getItemByCode(String code) {
        if (code == null) {
            return null;
        }

        for (MenuItem item : items) {
            if (item.getCode().equalsIgnoreCase(code)) {
                return item;
            }
        }

        return null;
    }
}
