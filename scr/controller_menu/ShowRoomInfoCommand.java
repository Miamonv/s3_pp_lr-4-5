package controller_menu;

import logic.GameRoomService;
import model.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ShowRoomInfoCommand implements Command {
    private GameRoomService service;

    public ShowRoomInfoCommand(GameRoomService service) {
        this.service = service;
    }

    @Override
    public void execute() {
        GameRoom room = service.getActiveRoom();
        if (room == null) {
            System.out.println("Кімната ще не створена. Спочатку створіть її.");
            return;
        }

        List<Toy> toys = room.getToys();

        System.out.println("\n========================================");
        System.out.printf(" КІМНАТА: %s\n", room.getName());
        System.out.printf(" Бюджет: %.2f / %.2f грн (Залишок: %.2f)\n",
                room.getCurrentSpent(), room.getBudgetLimit(), room.getRemainingBudget());
        System.out.println("========================================");

        if (toys.isEmpty()) {
            System.out.println(" (У кімнаті поки що порожньо)");
            return;
        }

        Map<String, List<Toy>> groupedToys = toys.stream()
                .collect(Collectors.groupingBy(toy -> {
                    if (toy instanceof Transport) return "ТРАНСПОРТ";
                    if (toy instanceof Lego) return "LEGO";
                    if (toy instanceof Doll) return "ЛЯЛЬКИ";
                    return "ІНШЕ";
                }));

        for (Map.Entry<String, List<Toy>> entry : groupedToys.entrySet()) {
            String groupName = entry.getKey();
            List<Toy> groupList = entry.getValue();

            System.out.println("\n" + groupName + " (к-сть: " + groupList.size() + "):");
            System.out.println("----------------------------------------");

            for (Toy t : groupList) {
                System.out.printf("  • %-20s | %8.2f грн | %-10s | %-12s\n",
                        t.getName(), t.getPrice(), getDetails(t), t.getSize());
            }
        }
        System.out.println("\n========================================");
    }

    private String getDetails(Toy t) {
        if (t instanceof Transport) {
            return "Швидкість: " + ((Transport) t).getMaxSpeed() + " км/год";
        } else if (t instanceof Lego) {
            return "Деталей: " + ((Lego) t).getNumberOfPieces();
        } else if (t instanceof Doll) {
            return "Матеріал: " + ((Doll) t).getMaterial() + " Волосся: " + ((Doll) t).getHairColor();
        }
        return "";
    }
}