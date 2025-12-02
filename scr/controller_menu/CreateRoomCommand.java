package controller_menu;

import logic.GameRoomService;
import java.util.Scanner;

public class CreateRoomCommand implements Command {
    private GameRoomService service;
    private Scanner scanner;

    public CreateRoomCommand(GameRoomService service, Scanner scanner) {
        this.service = service;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        System.out.println("\nСТВОРЕННЯ НОВОЇ КІМНАТИ");

        System.out.print("Введіть назву кімнати: ");
        String name = scanner.nextLine();

        while (true) {
            System.out.print("Введіть бюджет кімнати (грн): ");
            String budgetStr = scanner.nextLine();
            try {
                double budget = Double.parseDouble(budgetStr);
                if (budget <= 0) {
                    System.out.println("Бюджет має бути більше нуля.");
                    continue;
                }

                service.createRoom(name, budget);
                System.out.println("Кімнату '" + name + "' успішно створено!");
                break;
            } catch (NumberFormatException e) {
                System.out.println("Будь ласка, введіть число (наприклад 1000.50).");
            }
        }
    }
}