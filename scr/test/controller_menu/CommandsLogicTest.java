package controller_menu;

import logic.GameRoomService;
import model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistence.FileToyRepository;
import persistence.ToyRepository;

import java.io.*;
import java.util.Scanner;

class CommandsLogicTest {

    private GameRoomService service;
    private ByteArrayOutputStream outputStream;

    // оновлення сервісу перед кожним тестом
    @BeforeEach
    void setUp() {
        ToyRepository repository = new FileToyRepository("toys_catalog.csv");
        service = new GameRoomService(repository);
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    void testCreateRoomCommand() {
        String input1 = "TestRoom\n-5\n\n10\n";
        Scanner scanner1 = new Scanner(input1);

        Command command1 = new CreateRoomCommand(service, scanner1);
        command1.execute();

        Assertions.assertTrue(outputStream.toString().contains("Будь ласка, введіть число (наприклад 1000.50)."));
        Assertions.assertTrue(outputStream.toString().contains("Бюджет має бути більше нуля."));
        Assertions.assertNotNull(service.getActiveRoom());
        Assertions.assertEquals("TestRoom", service.getActiveRoom().getName());
        Assertions.assertEquals(10.0, service.getActiveRoom().getBudgetLimit());

    }

    @Test
    void testAddToyCommand() {
        Scanner scanner1 = new Scanner("hol\n2562415\n");
        Command command1 = new AddToyCommand(service, scanner1);
        command1.execute();
        Assertions.assertNull(service.getActiveRoom());

        // ввід: вік "5" та номер іграшки "1"
        service.createRoom("Playroom", 5000.0);
        Scanner scanner2 = new Scanner("5\n1\n");
        Command command2 = new AddToyCommand(service, scanner2);
        command2.execute();

        Assertions.assertEquals(1, service.getActiveRoom().getToys().size());
        Assertions.assertTrue(service.getActiveRoom().getCurrentSpent() > 0);

        //невірний вік abc
        outputStream.reset();
        Scanner scanner3 = new Scanner("abc\n4\n");
        Command command3 = new AddToyCommand(service, scanner3);
        command3.execute();
        Assertions.assertTrue(outputStream.toString().contains("Невірний формат віку."));

        //немає доступних іграшок для віку 100
        outputStream.reset();
        Scanner scanner4 = new Scanner("100\n4\n");
        Command command4 = new AddToyCommand(service, scanner4);
        command4.execute();
        Assertions.assertTrue(outputStream.toString().contains("На жаль, немає доступних іграшок для віку 100 років у межах вашого бюджету."));

        //іграшка 0
        outputStream.reset();
        Scanner scanner5 = new Scanner("5\n0\n");
        Command command5 = new AddToyCommand(service, scanner5);
        command5.execute();
        Assertions.assertEquals(1, service.getActiveRoom().getToys().size()); //перевірка що іграшка не додалась

        //іграшка з номером більшим за наявні
        outputStream.reset();
        Scanner scanner6 = new Scanner("5\n100\n");
        Command command6 = new AddToyCommand(service, scanner6);
        command6.execute();
        Assertions.assertTrue(outputStream.toString().contains("Невірний номер."));
    }

    @Test
    void testRemoveToyCommand() {
        service.createRoom("Delete Test", 1000.0);

        //немає що видаляти
        Command command = new RemoveToyCommand(service, new Scanner("1\n"));
        command.execute();
        Assertions.assertTrue(outputStream.toString().contains("Кімната порожня або не створена. Нічого видаляти."));

        //додавання іграшки для видалення
        outputStream.reset();
        Toy toy = service.getToysForChild(5).getFirst();
        service.addToyToRoom(toy);
        Assertions.assertEquals(1, service.getActiveRoom().getToys().size());

        //не число
        outputStream.reset();
        Command command1 = new RemoveToyCommand(service, new Scanner("abc\n"));
        command1.execute();
        Assertions.assertTrue(outputStream.toString().contains("Введіть число."));

        //номер -100 для видалення
        outputStream.reset();
        Scanner scanner2 = new Scanner("-100\n");
        Command command2 = new RemoveToyCommand(service, scanner2);
        command2.execute();
        Assertions.assertTrue(outputStream.toString().contains("Невірний номер."));

        //номео 0 для видалення
        outputStream.reset();
        Scanner scanner3 = new Scanner("0\n");
        Command command3 = new RemoveToyCommand(service, scanner3);
        command3.execute();
        Assertions.assertEquals(1, service.getActiveRoom().getToys().size()); //перевірка що іграшка не видалилась

        // номер "1" для видалення
        outputStream.reset();
        Scanner scanner4 = new Scanner("1\n");
        Command command4 = new RemoveToyCommand(service, scanner4);
        command4.execute();

        Assertions.assertEquals(0, service.getActiveRoom().getToys().size());
        Assertions.assertEquals(0.0, service.getActiveRoom().getCurrentSpent());
    }

    @Test
    void testSortToysCommand() {
        // немає що сортувати
        Command command1 = new SortToysCommand(service, new Scanner("1\n"));
        command1.execute();
        Assertions.assertTrue(outputStream.toString().contains("Немає іграшок для сортування."));

        service.createRoom("Sort Room", 5000.0);
        outputStream.reset();

        // додавання 4 іграшок для сортування
        Toy doll1 = new Doll("Doll A", 100.0, 3, 8, Size.MEDIUM, "Plastic", "Blonde");
        Toy doll2 = new Doll("Doll B", 150.0, 4, 10, Size.SMALL, "Cloth", "Brunette");
        Toy car1 = new Transport("Car A", 200.0, 5, 12, Size.LARGE, TransportType.CAR, 150);
        Toy car2 = new Transport("Car B", 80.0, 2, 6, Size.SMALL, TransportType.CAR, 100);
        service.addToyToRoom(doll1);
        service.addToyToRoom(doll2);
        service.addToyToRoom(car1);
        service.addToyToRoom(car2);

        // ввід: "1" (Сортування за ціною)
        String input1 = "1\n";
        Scanner scanner = new Scanner(input1);
        Command command = new SortToysCommand(service, scanner);
        command.execute();
        Assertions.assertTrue(outputStream.toString().contains("Відсортовано за ціною."));

        // ввід: "2" (Сортування за назвою)
        outputStream.reset();
        String input2 = "2\n";
        scanner = new Scanner(input2);
        command = new SortToysCommand(service, scanner);
        command.execute();
        Assertions.assertTrue(outputStream.toString().contains("Відсортовано за розміром."));

        // ввід: "3" (ТОП ШВИДКИХ МАШИН)
        outputStream.reset();
        String input3 = "3\n";
        scanner = new Scanner(input3);
        command = new SortToysCommand(service, scanner);
        command.execute();
        Assertions.assertTrue(outputStream.toString().contains("ТОП ШВИДКИХ МАШИН"));

        // ввід: "4" (ЛЯЛЬКИ за кольором волосся)
        outputStream.reset();
        String input4 = "4\n";
        scanner = new Scanner(input4);
        command = new SortToysCommand(service, scanner);
        command.execute();
        Assertions.assertTrue(outputStream.toString().contains("ЛЯЛЬКИ (Сортування за кольором волоссям)"));

        // ввід: "0" (Назад)
        outputStream.reset();
        String input5 = "0\n";
        scanner = new Scanner(input5);
        command = new SortToysCommand(service, scanner);
        command.execute();
        Assertions.assertFalse(outputStream.toString().contains("Відсортовано"));

        // ввід: "99" (Невірний вибір)
        outputStream.reset();
        String input6 = "99\n";
        scanner = new Scanner(input6);
        command = new SortToysCommand(service, scanner);
        command.execute();
        Assertions.assertTrue(outputStream.toString().contains("Невірний вибір."));
    }

    @Test
    void testShowRoomInfoCommand() {
        // кімната не створена
        Command command1 = new ShowRoomInfoCommand(service);
        command1.execute();
        Assertions.assertTrue(outputStream.toString().contains("Кімната ще не створена. Спочатку створіть її."));
        outputStream.reset();

        // створення кімнати та перевірка виводу
        service.createRoom("Visual Test", 1000.0);

        System.setOut(new PrintStream(outputStream));

        Command command2 = new ShowRoomInfoCommand(service);
        command2.execute();

        //System.setOut(System.out);

        Assertions.assertTrue(outputStream.toString().contains("Visual Test"));
        Assertions.assertTrue(outputStream.toString().contains("1000,00 грн"));
        Assertions.assertTrue(outputStream.toString().contains("(У кімнаті поки що порожньо)"));

        // додавання іграшки та перевірка виводу
        Toy doll = new Doll("Visual Doll", 200.0, 3, 8, Size.MEDIUM, "Plastic", "Red");
        Toy lego = new Lego("Visual Lego", 150.0, 4, 10, Size.SMALL, "Star Wars", 300);
        Toy car = new Transport("Visual Car", 250.0, 5, 12, Size.LARGE, TransportType.CAR, 180);
        Toy toy = service.getToysForChild(5).get(3);
        service.addToyToRoom(doll);
        service.addToyToRoom(lego);
        service.addToyToRoom(car);
        service.addToyToRoom(toy);

        outputStream.reset();

        System.setOut(new PrintStream(outputStream));
        Command command3 = new ShowRoomInfoCommand(service);
        command3.execute();
        System.setOut(System.out);
        Assertions.assertTrue(outputStream.toString().contains("Волосся: Red"));
        Assertions.assertTrue(outputStream.toString().contains("Деталей: 300"));
        Assertions.assertTrue(outputStream.toString().contains("Швидкість: 180 км/год"));
    }

    @Test
    void testFindToysCommand() {
        //кімната не створена
        Command command1 = new FindToysCommand(service, new Scanner("0\n10000\n5\n"));
        command1.execute();
        Assertions.assertTrue(outputStream.toString().contains("Кімната не створена."));

        //створення кімнати та додавання іграшки
        service.createRoom("Search Room", 2000.0);
        Toy toy = service.getToysForChild(5).getFirst();
        service.addToyToRoom(toy);

        // Мін ціна 0 -> Макс ціна 10000 -> Вік 5
        String input1 = "0\n10000\n5\n";
        Scanner scanner = new Scanner(input1);
        System.setOut(new PrintStream(outputStream));
        Command command = new FindToysCommand(service, scanner);
        command.execute();
        System.setOut(System.out);

        Assertions.assertTrue(outputStream.toString().contains(toy.getName()));

        //немає такої іграшки
        outputStream.reset();
        String input2 = "10000\n20000\n5\n";
        scanner = new Scanner(input2);
        System.setOut(new PrintStream(outputStream));
        command = new FindToysCommand(service, scanner);
        command.execute();
        System.setOut(System.out);
        Assertions.assertTrue(outputStream.toString().contains("Нічого не знайдено за такими параметрами."));

        //невірний формат числа
        String input3 = "abc\n2000\n5\n";
        scanner = new Scanner(input3);
        outputStream.reset();
        System.setOut(new PrintStream(outputStream));
        command = new FindToysCommand(service, scanner);
        command.execute();
        System.setOut(System.out);
        Assertions.assertTrue(outputStream.toString().contains("Помилка: Введіть коректні числа."));
    }

    @Test
    void testShowCatalogCommand() {
        // Просто викликаємо команду і перевіряємо, чи вивелись категорії
        Command command = new ShowCatalogCommand(service);
        command.execute();

        String output = outputStream.toString();
        Assertions.assertTrue(output.contains("ВЕСЬ КАТАЛОГ МАГАЗИНУ"));
        Assertions.assertTrue(output.contains("ТРАНСПОРТ"));
        Assertions.assertTrue(output.contains("LEGO"));
        Assertions.assertTrue(output.contains("ЛЯЛЬКИ"));
    }

    @Test
    void testEditToyCommand() {
        // кімната не створена
        Command command = new EditToyCommand(service, new Scanner("\n"));
        command.execute();
        Assertions.assertTrue(outputStream.toString().contains("Кімната порожня або не створена."));

        // створення кімнати
        service.createRoom("Edit Room", 5000.0);
        outputStream.reset();

        // кімната порожня
        command.execute();
        Assertions.assertTrue(outputStream.toString().contains("Кімната порожня або не створена."));

        // додавання іграшки для редагування
        Toy originalToy = new Doll("Original Name", 100.0, 3, 6, Size.SMALL, "Plastic", "None");
        service.addToyToRoom(originalToy);
        outputStream.reset();

        // невірний номер іграшки (-1)
        Scanner scanner1 = new Scanner("-1\n");
        command = new EditToyCommand(service, scanner1);
        command.execute();
        Assertions.assertTrue(outputStream.toString().contains("Невірний номер."));

        // невірний формат номера
        outputStream.reset();
        Scanner scanner2 = new Scanner("abc\n");
        command = new EditToyCommand(service, scanner2);
        command.execute();
        Assertions.assertTrue(outputStream.toString().contains("Введіть число."));

        // успішне редагування НАЗВИ
        outputStream.reset();
        Scanner scanner3 = new Scanner("1\n1\nSuper Doll\n");
        command = new EditToyCommand(service, scanner3);
        command.execute();

        Assertions.assertTrue(outputStream.toString().contains("Назву змінено!"));
        Assertions.assertEquals("Super Doll", service.getActiveRoom().getToys().get(0).getName());

        // успішне редагування ЦІНИ
        outputStream.reset();
        Scanner scanner4 = new Scanner("1\n2\n200\n");
        command = new EditToyCommand(service, scanner4);
        command.execute();

        Assertions.assertTrue(outputStream.toString().contains("Ціну оновлено!"));
        Assertions.assertEquals(200.0, service.getActiveRoom().getToys().get(0).getPrice());
        // перевірка чи змінились витрати (було 100, стало 200, різниця +100)
        Assertions.assertEquals(200.0, service.getActiveRoom().getCurrentSpent());

        // встановлення від'ємної ціни
        outputStream.reset();
        Scanner scanner5 = new Scanner("1\n2\n-50\n");
        command = new EditToyCommand(service, scanner5);
        command.execute();
        Assertions.assertTrue(outputStream.toString().contains("Ціна не може бути від'ємною."));

        // спроба підняти ціну вище бюджету (бюджет 5000, поточні витрати 200, ставлю 6000)
        outputStream.reset();
        Scanner scanner6 = new Scanner("1\n2\n6000\n");
        command = new EditToyCommand(service, scanner6);
        command.execute();
        Assertions.assertTrue(outputStream.toString().contains("Не вистачає бюджету"));

        // невірний вибір меню редагування (99)
        outputStream.reset();
        Scanner scanner7 = new Scanner("1\n99\n");
        command = new EditToyCommand(service, scanner7);
        command.execute();
        Assertions.assertTrue(outputStream.toString().contains("Невірний вибір."));
    }

    @Test
    void testSaveRoomCommand() {
        // кімната не створена
        Command command = new SaveRoomCommand(service, new Scanner("\n"));
        command.execute();
        Assertions.assertTrue(outputStream.toString().contains("Немає кімнати для збереження."));

        // успішне збереження
        service.createRoom("SaveTest", 1000.0);
        outputStream.reset();

        String filename = "test_save_room.txt";
        Scanner scanner = new Scanner(filename + "\n");

        command = new SaveRoomCommand(service, scanner);
        command.execute();

        Assertions.assertTrue(outputStream.toString().contains("Кімнату успішно збережено"));

        // перевірка чи файл створився і видаляємо його
        File file = new File(filename);
        Assertions.assertTrue(file.exists());
        file.delete();

        //без .txt в кінці
        outputStream.reset();
        String filename2 = "test_save_room";
        Scanner scanner2 = new Scanner(filename2 + "\n");
        command = new SaveRoomCommand(service, scanner2);
        command.execute();
        Assertions.assertTrue(outputStream.toString().contains("Кімнату успішно збережено"));

        File file2 = new File(filename2 + ".txt");
        Assertions.assertTrue(file2.exists());
        file2.delete();
    }

    @Test
    void testLoadRoomCommand() throws IOException {
        String filename = "test_load_room.txt";

        // спроба завантажити неіснуючий файл
        Scanner scanner1 = new Scanner("non_existent_file\n");
        Command command1 = new LoadRoomCommand(service, scanner1);
        command1.execute();
        Assertions.assertTrue(outputStream.toString().contains("Не вдалося завантажити файл (можливо, він не існує)."));

        // створення реального файлу для тесту
        File file = new File(filename);
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("Назва кімнати: Loaded Room\n");
            writer.write("Бюджет: 5000.0\n");
            writer.write("Витрачено: 100.0\n");
            writer.write("--- ІГРАШКИ В КІМНАТІ ---\n");
            writer.write("Transport, Test Car, 100.0, 3, 10, SMALL, CAR, 100\n");
        }

        // успішне завантаження
        outputStream.reset();
        Scanner scanner2 = new Scanner(filename + "\n");
        Command command2 = new LoadRoomCommand(service, scanner2);
        command2.execute();

        Assertions.assertTrue(outputStream.toString().contains("Кімнату успішно завантажено!"));
        Assertions.assertNotNull(service.getActiveRoom());
        Assertions.assertEquals("Loaded Room", service.getActiveRoom().getName());
        Assertions.assertEquals(5000.0, service.getActiveRoom().getBudgetLimit());
        Assertions.assertEquals(1, service.getActiveRoom().getToys().size());

        file.delete();
    }

    @Test
    void testExitCommand() {
        Command command = new ExitCommand();
        command.execute();
        Assertions.assertNotNull(command);
    }
}