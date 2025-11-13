package controller_menu;

public class SortToysCommand implements Command {
    @Override
    public void execute() {
        System.out.println("-> Виконується команда 'Сортувати іграшки'...");
    }
}