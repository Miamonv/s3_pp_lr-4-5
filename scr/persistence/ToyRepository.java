package persistence;

import model.GameRoom;
import model.Toy;
import java.util.List;

public interface ToyRepository {
    List<Toy> loadCatalog(); // Завантажити всі можливі іграшки магазину
    void saveRoom(GameRoom room, String filename); // Зберегти зібрану кімнату
    GameRoom loadRoom(String filename);     // Завантажити кімнату з файлу
}