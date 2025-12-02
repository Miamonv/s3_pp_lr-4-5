package controller_menu;

import logic.GameRoomService;
import java.util.Scanner;

public class SortToysCommand implements Command {
    private GameRoomService service;
    private Scanner scanner;

    public SortToysCommand(GameRoomService service, Scanner scanner) {
        this.service = service;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        if (service.getActiveRoom() == null || service.getActiveRoom().getToys().isEmpty()) {
            System.out.println("Немає іграшок для сортування.");
            return;
        }

        System.out.println("\nСОРТУВАННЯ ІГРАШОК");
        System.out.println("1. За ЦІНОЮ (від дешевих)");
        System.out.println("2. За РОЗМІРОМ (Small -> Large)");
        System.out.println("3. Тільки Транспорт за ШВИДКІСТЮ (від швидких)");
        System.out.println("4. Тільки Ляльки за КОЛЬОРОМ ВОЛОССЯ");
        System.out.println("0. Назад");

        System.out.print("Ваш вибір: ");
        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                service.sortRoomByPrice();
                System.out.println("Відсортовано за ціною.");
                break;
            case "2":
                service.sortRoomBySize();
                System.out.println("Відсортовано за розміром.");
                break;
            case "3":
                System.out.println("ТОП ШВИДКИХ МАШИН");
                service.showTransportsBySpeed();
                return;
            case "4":
                service.showDollsByHairColor();
                return;
            case "0":
                return;
            default:
                System.out.println("Невірний вибір.");
                return;
        }

        // після сортування автоматично показуємо результат
        new ShowRoomInfoCommand(service).execute();
    }
}