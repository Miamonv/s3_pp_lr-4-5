package controller_menu;

import logic.GameRoomService;
import model.Toy;
import java.util.List;
import java.util.Scanner;

public class EditToyCommand implements Command {
    private GameRoomService service;
    private Scanner scanner;

    public EditToyCommand(GameRoomService service, Scanner scanner) {
        this.service = service;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        if (service.getActiveRoom() == null || service.getActiveRoom().getToys().isEmpty()) {
            System.out.println("Кімната порожня або не створена.");
            return;
        }

        System.out.println("\nРЕДАГУВАННЯ ІГРАШКИ");
        List<Toy> toys = service.getActiveRoom().getToys();

        for (int i = 0; i < toys.size(); i++) {
            System.out.printf("%d. %s (%.2f грн)\n", i + 1, toys.get(i).getName(), toys.get(i).getPrice());
        }

        System.out.print("Введіть номер іграшки для редагування: ");
        try {
            int index = Integer.parseInt(scanner.nextLine()) - 1;
            if (index < 0 || index >= toys.size()) {
                System.out.println("Невірний номер.");
                return;
            }

            Toy selectedToy = toys.get(index);
            System.out.println("Вибрано: " + selectedToy.getName());

            System.out.println("Що змінюємо?");
            System.out.println("1. Назву");
            System.out.println("2. Ціну");
            System.out.print("> ");
            String editChoice = scanner.nextLine();

            switch (editChoice) {
                case "1":
                    System.out.print("Введіть нову назву: ");
                    String newName = scanner.nextLine();
                    if (!newName.isBlank()) {
                        selectedToy.setName(newName);
                        System.out.println("Назву змінено!");
                    }
                    break;
                case "2":
                    System.out.print("Введіть нову ціну: ");
                    double newPrice = Double.parseDouble(scanner.nextLine());
                    if (newPrice < 0) {
                        System.out.println("Ціна не може бути від'ємною.");
                    } else {
                        service.updateToyPrice(selectedToy, newPrice);
                        System.out.println("Ціну оновлено! Бюджет перераховано.");
                    }
                    break;
                default:
                    System.out.println("Невірний вибір.");
            }

        } catch (NumberFormatException e) {
            System.out.println("Введіть число.");
        } catch (Exception e) {
            System.out.println("Помилка: " + e.getMessage());
        }
    }
}