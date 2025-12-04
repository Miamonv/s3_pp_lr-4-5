package controller_menu;

import logic.GameRoomService;
import model.Toy;

import java.util.List;
import java.util.Scanner;

public class AddToyCommand implements Command {
    private GameRoomService service;
    private Scanner scanner;

    public AddToyCommand(GameRoomService service, Scanner scanner) {
        this.service = service;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        if (service.getActiveRoom() == null) {
            System.out.println("Спочатку створіть кімнату!");
            return;
        }

        System.out.println("\n--- Додавання іграшки ---");
        System.out.println("Бюджету залишилось: " + service.getActiveRoom().getRemainingBudget());

        // вік конкретної дитини
        int childAge = 0;
        try {
            System.out.print("Введіть вік дитини, для якої обираєте іграшку: ");
            String ageInput = scanner.nextLine();
            childAge = Integer.parseInt(ageInput);
        } catch (NumberFormatException e) {
            System.out.println("Невірний формат віку.");
            return;
        }

        List<Toy> suitableToys = service.getToysForChild(childAge);

        if (suitableToys.isEmpty()) {
            System.out.println("На жаль, немає доступних іграшок для віку " + childAge +
                    " років у межах вашого бюджету.");
            return;
        }

        // відфільтрований список
        System.out.println("--- Доступні варіанти: ---");
        for (int i = 0; i < suitableToys.size(); i++) {
            Toy t = suitableToys.get(i);
            System.out.printf("%d. %s (%.2f грн) [%d-%d років]\n",
                    i + 1, t.getName(), t.getPrice(), t.getMinAge(), t.getMaxAge());
        }

        // вибір
        System.out.print("Оберіть номер (0 для відміни): ");
        try {
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice == 0) return;

            if (choice > 0 && choice <= suitableToys.size()) {
                Toy selected = suitableToys.get(choice - 1);
                service.addToyToRoom(selected);
                System.out.println("Успішно додано: " + selected.getName());
            } else {
                System.out.println("Невірний номер.");
            }
        } catch (Exception e) {
            System.out.println("Помилка: " + e.getMessage());
        }
    }
}