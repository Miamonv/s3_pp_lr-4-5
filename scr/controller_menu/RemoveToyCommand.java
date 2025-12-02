package controller_menu;

import logic.GameRoomService;
import model.Toy;
import java.util.List;
import java.util.Scanner;

public class RemoveToyCommand implements Command {
    private GameRoomService service;
    private Scanner scanner;

    public RemoveToyCommand(GameRoomService service, Scanner scanner) {
        this.service = service;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        if (service.getActiveRoom() == null || service.getActiveRoom().getToys().isEmpty()) {
            System.out.println("Кімната порожня або не створена. Нічого видаляти.");
            return;
        }

        System.out.println("\nВИДАЛЕННЯ ІГРАШКИ");
        List<Toy> toys = service.getActiveRoom().getToys();

        for (int i = 0; i < toys.size(); i++) {
            System.out.printf("%d. %s (%.2f грн)\n", i + 1, toys.get(i).getName(), toys.get(i).getPrice());
        }

        System.out.print("Введіть номер іграшки для видалення (0 для відміни): ");
        try {
            String input = scanner.nextLine();
            int index = Integer.parseInt(input);

            if (index == 0) return;

            if (index > 0 && index <= toys.size()) {
                Toy removed = service.removeToyFromRoom(index - 1);
                System.out.println("Видалено: " + removed.getName());
                System.out.println("Бюджет оновлено. Доступно: " + service.getActiveRoom().getRemainingBudget());
            } else {
                System.out.println("Невірний номер.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Введіть число.");
        }
    }
}