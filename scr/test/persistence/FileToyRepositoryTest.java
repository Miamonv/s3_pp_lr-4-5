package persistence;

import model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileToyRepositoryTest {

    private static final String TEST_CATALOG_FILE = "test_catalog.txt";
    private static final String TEST_ROOM_FILE = "test_room.txt";

    @AfterEach
    //прибирання тимчасових файлів після кожного тесту
    void tearDown() {
        new File(TEST_CATALOG_FILE).delete();
        new File(TEST_ROOM_FILE).delete();
    }

    @Test
    void testLoadCatalogFileNotExists() {
        // файлу фізично немає
        FileToyRepository repo = new FileToyRepository("non_existent_file.txt");

        List<Toy> toys = repo.loadCatalog();

        // порожній список
        assertNotNull(toys);
        assertTrue(toys.isEmpty());
    }

    @Test
    void testLoadCatalogSuccess() throws IOException {
        //тимчасовий файл каталогу
        try (PrintWriter writer = new PrintWriter(new FileWriter(TEST_CATALOG_FILE))) {
            writer.println("Doll, Barbie, 100.0, 3, 10, SMALL, Plastic, Blonde");
            writer.println("Transport, HotWheels, 50.0, 3, 8, SMALL, CAR, 100");
            // порожній рядок (має пропустити)
            writer.println("");
            // невалідний рядок (має пропустити, вивести помилку в консоль)
            writer.println("BadData, NoPrice");
        }

        FileToyRepository repo = new FileToyRepository(TEST_CATALOG_FILE);
        List<Toy> toys = repo.loadCatalog();

        // перевірка
        assertEquals(2, toys.size(), "Має завантажити тільки 2 валідні іграшки");
        assertEquals("Barbie", toys.get(0).getName());
        assertEquals("HotWheels", toys.get(1).getName());
    }

    @Test
    void testSaveRoom() throws IOException {
        GameRoom room = new GameRoom("My Room", 500.0);
        Toy doll = new Doll("TestDoll", 100, 5, 10, Size.SMALL, "Plastic", "Red");
        room.addToy(doll);

        FileToyRepository repo = new FileToyRepository("dummy.csv"); // Каталог тут не важливий

        // збереження
        repo.saveRoom(room, TEST_ROOM_FILE);

        // чи файл створився і що всередині
        File file = new File(TEST_ROOM_FILE);
        assertTrue(file.exists());

        // читаю весь файл як один рядок для перевірки вмісту
        String content = Files.readString(Path.of(TEST_ROOM_FILE));

        assertTrue(content.contains("Ім'я кімнати: My Room"));
        assertTrue(content.contains("Бюджет: 500.0"));
        assertTrue(content.contains("--- ІГРАШКИ В КІМНАТІ ---"));
        assertTrue(content.contains("TestDoll"));
    }

    @Test
    void testLoadRoomSuccess() throws IOException {
        // створення тимчасового файлу кімнати
        try (PrintWriter writer = new PrintWriter(new FileWriter(TEST_ROOM_FILE))) {
            writer.println("Ім'я кімнати: Loaded Room");
            writer.println("Бюджет: 2000.0");
            writer.println("Витрачено: 100.0");
            writer.println("--- ІГРАШКИ В КІМНАТІ ---");
            writer.println("Lego, StarWars, 500.0, 8, 14, MEDIUM, Space, 300");
        }

        FileToyRepository repo = new FileToyRepository("dummy.csv");

        // завантаження
        GameRoom room = repo.loadRoom(TEST_ROOM_FILE);

        // перевірка
        assertNotNull(room);
        assertEquals("Loaded Room", room.getName());
        assertEquals(2000.0, room.getBudgetLimit());

        // чи завантажилась іграшка
        assertEquals(1, room.getToys().size());
        assertEquals("StarWars", room.getToys().get(0).getName());

        // має перерахуватись автоматично на основі ціни іграшки (500)
        assertEquals(500.0, room.getCurrentSpent());
    }

    @Test
    void testLoadRoomFileNotFound() {
        FileToyRepository repo = new FileToyRepository("dummy.csv");
        GameRoom room = repo.loadRoom("non_existent_room.txt");
        assertNull(room, "Якщо файлу немає, має повернути null");
    }

    @Test
    void testLoadRoomCorruptedHeader() throws IOException {
        // файл з некоректним заголовком (має спіймати Exception всередині)
        try (PrintWriter writer = new PrintWriter(new FileWriter(TEST_ROOM_FILE))) {
            writer.println("Ім'я кімнати Loaded Room");
            writer.println("Бюджет: 2000.0");
        }

        FileToyRepository repo = new FileToyRepository("dummy.csv");

        GameRoom room = repo.loadRoom(TEST_ROOM_FILE);
        assertNull(room);
    }

    @Test
    void testLoadRoomCorruptedToyLine() throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(TEST_ROOM_FILE))) {
            writer.println("Ім'я кімнати: Test");
            writer.println("Бюджет: 1000.0");
            writer.println("---");
            writer.println("Lego, GoodLego, 100.0, 5, 10, SMALL, City, 50");
            writer.println("Lego GoodLego 100.0 5 10 SMALL City 50"); // має пропустити
        }

        FileToyRepository repo = new FileToyRepository("dummy.csv");
        GameRoom room = repo.loadRoom(TEST_ROOM_FILE);

        assertNotNull(room);
        //має завантажити 1 гарну іграшку і пропустити погану
        assertEquals(1, room.getToys().size());
        assertEquals("GoodLego", room.getToys().get(0).getName());
    }
}