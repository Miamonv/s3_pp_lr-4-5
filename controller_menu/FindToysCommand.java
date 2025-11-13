package controller_menu;

public class FindToysCommand implements Command {
    @Override
    public void execute() {
        System.out.println("-> Виконується команда 'Знайти іграшки'...");
    }
}