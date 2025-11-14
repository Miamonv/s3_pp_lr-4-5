package main;

import console.ConsoleView;
import controller_menu.*; // імпорту всіх команд

public class Main {

    public static void main(String[] args) {
        ConsoleView view = new ConsoleView();
        MenuController menuController = new MenuController();

        // об'єкти команд
        Command addToy = new AddToyCommand();
        Command sortToys = new SortToysCommand();
        Command findToysByRange = new FindToysCommand();
        Command saveRoom = new SaveRoomCommand();
        Command loadRoom = new LoadRoomCommand();
        Command exit = new ExitCommand();

        // введення реєстрації команд у меню для його перевірки
        menuController.register("1", addToy);
        menuController.register("2", sortToys);
        menuController.register("3", findToysByRange);
        menuController.register("4", saveRoom);
        menuController.register("5", loadRoom);
        menuController.register("0", exit);

        while (true) {
            view.showMenu();
            String userInput = view.getUserInput();
            menuController.executeCommand(userInput); // виконання команди на основі вводу користувача
        }
    }
}