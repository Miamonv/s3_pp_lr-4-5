package console;
import java.util.Scanner;

public class ConsoleView {

    private Scanner scanner;
    public ConsoleView(Scanner scanner) {
        this.scanner = scanner;
    }

    // ANSI кольори для консолі
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";

    public void showMenu() {
        System.out.println(ANSI_YELLOW + "===== ІГРОВА КІМНАТА =====" + ANSI_RESET);

        // керування кімнатою
        System.out.println(ANSI_CYAN + "\n--- Керування Кімнатою ---" + ANSI_RESET);
        System.out.println("1. Сформувати нову кімнату");
        System.out.println("2. Завантажити кімнату з файлу");
        System.out.println("3. Зберегти поточну кімнату у файл");

        // робота з іграшками
        System.out.println(ANSI_CYAN + "\n--- Редагування Іграшок ---" + ANSI_RESET);
        System.out.println("4. Додати нову іграшку");
        System.out.println("5. Редагувати іграшку");
        System.out.println("6. Видалити іграшку");

        // пошук та сортування
        System.out.println(ANSI_CYAN + "\n--- Пошук та Перегляд ---" + ANSI_RESET);
        System.out.println("7. Показати кімнату (Список та Інфо)");
        System.out.println("8. Сортувати іграшки");
        System.out.println("9. Знайти іграшки (Ціна/Вік)");
        System.out.println("10. Показати весь каталог магазину");

        System.out.println(ANSI_RED + "\n0. Вихід" + ANSI_RESET);
        System.out.print("\nВаш вибір: ");

    }

    //для зчитування вводу користувача
    public String getUserInput() {
        String input = "";
        while (input.isEmpty()) {
            if (scanner.hasNextLine()) {
                input = scanner.nextLine().trim();
            } else {
                break;
            }
        }
        return input;
    }

    public void pressEnterToContinue() {
        System.out.println(ANSI_PURPLE + "(Натисніть Enter, щоб продовжити...)" + ANSI_RESET);
        scanner.nextLine();
    }
}



