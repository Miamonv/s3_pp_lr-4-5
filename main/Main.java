package main;

import console.ConsoleView;
import controller_menu.*; // Імпортуємо всі наші команди

public class Main {

    public static void main(String[] args) {
        ConsoleView view = new ConsoleView();
        MenuController menuController = new MenuController();

        // об'єкти команд
        Command addToy = new AddToyCommand();
        Command sortToys = new SortToysCommand();
        Command exit = new ExitCommand();

        // введення реєстрації команд у меню для його перевірки
        menuController.register("1", addToy);
        menuController.register("2", sortToys);
        menuController.register("0", exit);

        while (true) {
            view.showMenu();
            String userInput = view.getUserInput();
            menuController.executeCommand(userInput); // Виконання команди на основі вводу користувача
        }
    }
}