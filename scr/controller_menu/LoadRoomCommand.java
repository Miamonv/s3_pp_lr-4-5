package controller_menu;

import logic.GameRoomService;
import java.util.Scanner;

public class LoadRoomCommand implements Command {
    private GameRoomService service;
    private Scanner scanner;

    public LoadRoomCommand(GameRoomService service, Scanner scanner) {
        this.service = service;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        System.out.println("\nЗАВАНТАЖЕННЯ КІМНАТИ");
        System.out.print("Введіть назву файлу (напр. my_room.txt): ");
        String filename = scanner.nextLine();

        if (!filename.endsWith(".txt")) {
            filename += ".txt";
        }

        try {
            boolean success = service.loadRoomFromFile(filename);
            if (success) {
                System.out.println("Кімнату успішно завантажено!");
                new ShowRoomInfoCommand(service).execute();
            } else {
                System.out.println("Не вдалося завантажити файл (можливо, він не існує).");
            }
        } catch (Exception e) {
            System.out.println("Критична помилка при читанні файлу: " + e.getMessage());
        }
    }
}