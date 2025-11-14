package console;
import java.util.Scanner;

public class ConsoleView {

    private Scanner scanner = new Scanner(System.in);

    // ANSI кольори для консолі
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";

    public void showMenu() {
        //додати очищення консолі і потім введення нової
        //можливо розділити меню на кілька частин
        //наприклад дії окремо для іграшок, окремо для кімнати?
        System.out.println("\n===== ІГРОВА КІМНАТА ====="); //!!!додати кольори в меню або подумати за іньерфейс
        System.out.println("1. Додати нову іграшку");
        System.out.println("2. Сортувати іграшки");
        System.out.println("3. Знайти іграшки за діапазоном");
        System.out.println("4. Зберегти кімнату у файл");
        System.out.println("5. Завантажити кімнату з файлу");
        System.out.println("0. Вихід");
        System.out.print("Ваш вибір: ");
    }

    public String getUserInput() {
        return scanner.nextLine();
    }

    public void showMessage(String message) {
        System.out.println(message);
    }

    public void returnToMenu() {
        System.out.println("Повернення до головного меню...");
        ConsoleView consoleView = new ConsoleView();
        consoleView.showMenu();
    }
}



