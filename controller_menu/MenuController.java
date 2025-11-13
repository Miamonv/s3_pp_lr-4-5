package controller_menu;

import java.util.HashMap;
import java.util.Map;

public class MenuController {

    // карта для зберігання зв'язку "ввід" => "команда"
    private Map<String, Command> commands = new HashMap<>();

    /**
     * Метод для реєстрації команди в меню.
     * @param key ключ (що вводить користувач)
     * @param command об'єкт команди
     */
    public void register(String key, Command command) {
        commands.put(key, command);
    }

    /**
     * Виконує команду за ключем.
     * @param key ключ, що ввів користувач
     */
    public void executeCommand(String key) {
        Command command = commands.get(key);

        if (command != null) {
            command.execute();
        } else {
            // команди для такого ключа немає
            System.out.println("Помилка: Невідома команда '" + key + "'");
        }
    }
}