package controller_menu;

import logic.GameRoomService;
import model.Toy;
import java.util.List;
import java.util.Scanner;

public class FindToysCommand implements Command {
    private GameRoomService service;
    private Scanner scanner;

    public FindToysCommand(GameRoomService service, Scanner scanner) {
        this.service = service;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        if (service.getActiveRoom() == null) {
            System.out.println("Кімната не створена.");
            return;
        }

        System.out.println("\nПОШУК ІГРАШОК ЗА ДІАПАЗОНОМ");
        try {
            System.out.print("Введіть мінімальну ціну: ");
            double minPrice = Double.parseDouble(scanner.nextLine());

            System.out.print("Введіть максимальну ціну: ");
            double maxPrice = Double.parseDouble(scanner.nextLine());

            System.out.print("Введіть вік дитини: ");
            int age = Integer.parseInt(scanner.nextLine());

            List<Toy> found = service.findToysByRange(minPrice, maxPrice, age);

            if (found.isEmpty()) {
                System.out.println("Нічого не знайдено за такими параметрами.");
            } else {
                System.out.println("\nЗнайдено " + found.size() + " іграшок:");
                for (Toy t : found) {
                    System.out.println(" • " + t.toString());
                }
            }

        } catch (NumberFormatException e) {
            System.out.println("Помилка: Введіть коректні числа.");
        }
    }
}