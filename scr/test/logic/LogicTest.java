package logic;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistence.ToyRepository;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LogicTest {

    private GameRoomService service;
    private ByteArrayOutputStream outputStream;

    private Toy doll;
    private Toy car;
    private Toy lego;

    @BeforeEach
    void setUp() {
        //створення моків
        doll = new Doll("Barbie", 200.0, 3, 10, Size.SMALL, "Plastic", "Blonde");
        car = new Transport("HotWheels", 100.0, 3, 12, Size.SMALL, TransportType.CAR, 150);
        lego = new Lego("StarWars", 500.0, 8, 14, Size.MEDIUM, "Space", 300);

        List<Toy> mockCatalog = new ArrayList<>();
        mockCatalog.add(doll);
        mockCatalog.add(car);
        mockCatalog.add(lego);

        // створення фейкового репозиторію
        ToyRepository mockRepository = new ToyRepository() {
            @Override
            public List<Toy> loadCatalog() {
                return new ArrayList<>(mockCatalog);
            }

            @Override
            public GameRoom loadRoom(String filename) {
                if (filename.equals("valid_file.txt")) {
                    GameRoom room = new GameRoom("Loaded Room", 5000.0);
                    room.addToy(doll);
                    return room;
                }
                return null;
            }

            @Override
            public void saveRoom(GameRoom room, String filename) {
            }
        };

        service = new GameRoomService(mockRepository);

        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    void testCreateAndGetRoom() {
        service.createRoom("Test Room", 1000.0);
        assertNotNull(service.getActiveRoom());
        assertEquals("Test Room", service.getActiveRoom().getName());
        assertEquals(1000.0, service.getActiveRoom().getBudgetLimit());
    }

    @Test
    void testSaveRoom() {
        // збереження без створеної кімнати
        service.saveCurrentRoom("test.txt");

        // успішне збереження
        service.createRoom("Save Room", 1000.0);
        service.saveCurrentRoom("test.txt");
        assertTrue(outputStream.toString().contains("Кімнату успішно збережено"));
    }

    @Test
    void testLoadRoom() {
        // невдале завантаження
        boolean resultFail = service.loadRoomFromFile("invalid.txt");
        assertFalse(resultFail);

        // успішне завантаження (з моком)
        boolean resultSuccess = service.loadRoomFromFile("valid_file.txt");
        assertTrue(resultSuccess);
        assertNotNull(service.getActiveRoom());
        assertEquals("Loaded Room", service.getActiveRoom().getName());
        assertEquals(1, service.getActiveRoom().getToys().size());
    }

    @Test
    void testAddAndRemoveToy() {
        service.createRoom("Playroom", 1000.0);

        // додавання
        service.addToyToRoom(doll);
        assertEquals(1, service.getActiveRoom().getToys().size());
        assertEquals(200.0, service.getActiveRoom().getCurrentSpent());

        // видалення
        Toy removed = service.removeToyFromRoom(0);
        assertEquals(doll, removed);
        assertEquals(0, service.getActiveRoom().getToys().size());
        assertEquals(0.0, service.getActiveRoom().getCurrentSpent());
    }

    @Test
    void testRemoveFromNullRoom() {
        // якщо кімната не створена, має повернути null
        assertNull(service.removeToyFromRoom(0));
    }

    @Test
    void testUpdateToyPriceSuccess() throws Exception {
        service.createRoom("Rich Room", 1000.0);
        service.addToyToRoom(doll); // ціна 200, витрачено 200, залишок 800

        // піднімаю ціну до 300 (різниця +100, бюджет дозволяє)
        service.updateToyPrice(doll, 300.0);

        assertEquals(300.0, doll.getPrice());
        assertEquals(300.0, service.getActiveRoom().getCurrentSpent());
    }

    @Test
    void testUpdateToyPriceFail() {
        service.createRoom("Poor Room", 200.0);
        service.addToyToRoom(doll); // ціна 200, витрачено 200, залишок 0

        // спроба підняти ціну, коли грошей немає
        Exception exception = assertThrows(Exception.class, () -> {
            service.updateToyPrice(doll, 250.0);
        });

        assertEquals("Не вистачає бюджету для збільшення ціни!", exception.getMessage());
    }

    @Test
    void testUpdateToyPriceDecrease() throws Exception {
        service.createRoom("Test", 500);
        service.addToyToRoom(doll); // 200

        // зменшую ціну (різниця від'ємна)
        service.updateToyPrice(doll, 150.0);

        assertEquals(150.0, service.getActiveRoom().getCurrentSpent());
    }

    @Test
    void testGetToysForChild() {
        // кімната не створена
        assertTrue(service.getToysForChild(5).isEmpty());

        // кімната створена, але маленький бюджет
        service.createRoom("Budget Room", 150.0);
        // doll (200) - дорого
        // car (100) - ок
        // lego (500) - дорого

        //тобто маж бути тільки car
        List<Toy> result = service.getToysForChild(3); // вік 3 підходить для doll і car

        assertEquals(1, result.size());
        assertEquals("HotWheels", result.get(0).getName());
    }

    @Test
    void testFindToysByRange() {
        if (service.getActiveRoom() == null) {
            assertTrue(service.findToysByRange(0, 1000, 5).isEmpty());
        }

        service.createRoom("Search Room", 1000.0);
        service.addToyToRoom(doll); // 200, 3-10 років
        service.addToyToRoom(car);  // 100, 3-12 років
        service.addToyToRoom(lego); // 500, 8-14 років

        // шукаємо: ціна 0-300, вік 5
        // doll (ок), car (ок), lego (ціна не ок)
        List<Toy> result = service.findToysByRange(0, 300, 5);

        assertEquals(2, result.size()); //doll і car
        assertTrue(result.contains(doll));
        assertTrue(result.contains(car));
        assertFalse(result.contains(lego));
    }

    @Test
    void testSortByPrice() {
        // перевірка на null
        service.sortRoomByPrice();

        service.createRoom("Sort Room", 1000.0);
        service.addToyToRoom(lego); // 500
        service.addToyToRoom(car);  // 100
        service.addToyToRoom(doll); // 200

        service.sortRoomByPrice();

        List<Toy> sorted = service.getActiveRoom().getToys();
        assertEquals(car, sorted.get(0)); // 100
        assertEquals(doll, sorted.get(1)); // 200
        assertEquals(lego, sorted.get(2)); // 500
    }

    @Test
    void testSortBySize() {
        // перевірка на null
        service.sortRoomBySize();

        service.createRoom("Size Room", 1000.0);
        service.addToyToRoom(lego); // MEDIUM
        service.addToyToRoom(car);  // SMALL

        service.sortRoomBySize();

        // SMALL < MEDIUM < LARGE
        assertEquals(Size.SMALL, service.getActiveRoom().getToys().get(0).getSize()); // car
        assertEquals(Size.MEDIUM, service.getActiveRoom().getToys().get(1).getSize()); // lego
    }

    @Test
    void testShowTransportsBySpeed() {
        service.createRoom("Race Room", 1000.0);
        service.addToyToRoom(car); // 150 km/h
        Toy fastCar = new Transport("F1", 500, 5, 10, Size.LARGE, TransportType.CAR, 300);
        service.addToyToRoom(fastCar);

        service.showTransportsBySpeed();

        String output = outputStream.toString();
        // має спочатку вивести швидшу (300), потім повільнішу (150)
        assertTrue(output.indexOf("300") < output.indexOf("150"));
        assertTrue(output.contains("F1"));
    }

    @Test
    void testShowDollsByHairColor() {
        // кімната не створена
        service.showDollsByHairColor();

        service.createRoom("Doll Room", 1000.0);
        Toy brunette = new Doll("Brunette", 100, 3, 5, Size.SMALL, "P", "Brown");
        Toy blonde = new Doll("Blonde", 100, 3, 5, Size.SMALL, "P", "Blonde");

        service.addToyToRoom(brunette);
        service.addToyToRoom(blonde);

        service.showDollsByHairColor();

        String output = outputStream.toString();
        // сортування String: Blonde (B) йде перед Brown (Br)
        assertTrue(output.indexOf("Blonde") < output.indexOf("Brown"));
        assertTrue(output.contains("ЛЯЛЬКИ"));
    }

    @Test
    void testShowWholeCatalog() {
        // нормальний вивід
        service.showWholeCatalog();
        String output = outputStream.toString();

        assertTrue(output.contains("ВЕСЬ КАТАЛОГ МАГАЗИНУ"));
        assertTrue(output.contains("ТРАНСПОРТ"));
        assertTrue(output.contains("HotWheels"));
        assertTrue(output.contains("LEGO"));
        assertTrue(output.contains("StarWars"));

        // порожній каталог
        outputStream.reset();
        ToyRepository emptyRepo = new ToyRepository() {
            @Override public List<Toy> loadCatalog() { return new ArrayList<>(); }
            @Override public GameRoom loadRoom(String f) { return null; }
            @Override public void saveRoom(GameRoom r, String f) {}
        };
        GameRoomService emptyService = new GameRoomService(emptyRepo);

        emptyService.showWholeCatalog();
        assertTrue(outputStream.toString().contains("Каталог порожній"));
    }
}