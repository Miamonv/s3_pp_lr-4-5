package controller_menu;

import logic.GameRoomService;
import java.util.Scanner;

public class SaveRoomCommand implements Command {
    private GameRoomService service;
    private Scanner scanner;

    public SaveRoomCommand(GameRoomService service, Scanner scanner) {
        this.service = service;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        if (service.getActiveRoom() == null) {
            System.out.println("Немає кімнати для збереження.");
            return;
        }

        System.out.print("Введіть назву файлу (напр. my_room.txt): ");
        String filename = scanner.nextLine();

        if (!filename.endsWith(".txt")) {
            filename += ".txt";
        }

        service.saveCurrentRoom(filename);
    }
}