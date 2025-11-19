package main;

import console.ConsoleView;
import controller_menu.*; // імпорту всіх команд

public class Main {

    public static void main(String[] args) {
        ConsoleView view = new ConsoleView();
        MenuController menuController = new MenuController();

        // =============== Об'єкти команд ===================
        // управління кімнатою
        Command createRoom = new CreateRoomCommand();
        Command loadRoom = new LoadRoomCommand();
        Command saveRoom = new SaveRoomCommand();

        // робота з іграшками
        Command addToy = new AddToyCommand();
        Command editToy = new EditToyCommand();
        Command removeToy = new RemoveToyCommand();

        // аналіз та перегляд
        Command showAllToys = new ShowAllToysCommand();
        Command sortToys = new SortToysCommand();
        Command findToys = new FindToysCommand();
        Command showStats = new ShowStatsCommand();
        Command showRoomInfo = new ShowRoomInfoCommand();

        // і вихід
        Command exit = new ExitCommand();

        // введення реєстрації команд у меню для його перевірки!!
        menuController.register("1", createRoom);
        menuController.register("2", loadRoom);
        menuController.register("3", saveRoom);
        menuController.register("4", addToy);
        menuController.register("5", editToy);
        menuController.register("6", removeToy);
        menuController.register("7", showAllToys);
        menuController.register("8", sortToys);
        menuController.register("9", findToys);
        menuController.register("10", showStats);
        menuController.register("11", showRoomInfo);
        menuController.register("0", exit);

        while (true) {
            view.showMenu();
            String userInput = view.getUserInput();
            menuController.executeCommand(userInput); // виконання команди на основі вводу користувача
            if (!userInput.equals("0")) {
                view.pressEnterToContinue();
            }
        }
    }
}