
package app;

import challenge.CookingChallenge;
import controller.GameController;
import controller.GameState;
import customer.CustomerManager;
import menu.Menu;
import order.OrderManager;
import restaurant.TableManager;
import staff.Cashier;
import staff.Chef;
import staff.Host;
import staff.Waiter;
import ui.ConsoleUI;
import ui.InputHandler;

import java.util.Scanner;

import static controller.GameConfig.TABLE_COUNT;

public class Main {

    public static void main(String[] args) {

        // Create the existing restaurant components
        CustomerManager customerManager = new CustomerManager();
        TableManager tableManager = new TableManager(TABLE_COUNT);
        OrderManager orderManager = new OrderManager();
        Menu menu = new Menu();

        GameState gameState = new GameState(
                customerManager, tableManager, orderManager, menu);

        // Create restaurant staff
        Waiter waiter = new Waiter();
        Host host = new Host();
        Chef chef = new Chef();
        Cashier cashier = new Cashier();

        ConsoleUI consoleUI = new ConsoleUI();

        // Additional Feature: Use one shared Scanner
        Scanner scanner = new Scanner(System.in);

        InputHandler inputHandler = new InputHandler(scanner);

        CookingChallenge cookingChallenge =
                new CookingChallenge(scanner);

        // Pass the cooking challenge into GameController
        GameController controller = new GameController(
                gameState,
                consoleUI,
                inputHandler,
                waiter,
                host,
                chef,
                cashier,
                cookingChallenge);

        controller.run();
    }
}
