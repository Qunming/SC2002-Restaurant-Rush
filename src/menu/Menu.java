package menu;

import java.util.ArrayList;
import java.util.List;

public class Menu {
    private final List<MenuItem> availableMenuItems = new ArrayList<>();

    public Menu() {
        availableMenuItems.add(
                new MenuItem("SALAD", "Salad", 900, 1));
        availableMenuItems.add(
                new MenuItem("BURGER", "Burger", 1500, 1));
        availableMenuItems.add(
                new MenuItem("PASTA", "Pasta", 1800, 2));
    }

    public List<MenuItem> getItems() {
        return List.copyOf(availableMenuItems);
    }

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
