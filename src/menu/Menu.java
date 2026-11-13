
package menu;

import java.util.ArrayList;
import java.util.List;

public class Menu {

    private final List<MenuItem> items = new ArrayList<>();

    public Menu() {
        MenuItem salad = new MenuItem(
                "SALAD", "Salad", 900, 1);

        MenuItem burger = new MenuItem(
                "BURGER", "Burger", 1500, 1);

        MenuItem pasta = new MenuItem(
                "PASTA", "Pasta", 1800, 2);

        // register the original menu items
        items.add(salad);
        items.add(burger);
        items.add(pasta);

        // stage 3: add salad and pasta combo
        items.add(new ComboMeal(salad, pasta));
    }

    public List<MenuItem> getItems() {
        return List.copyOf(items);
    }

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
