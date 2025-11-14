package controller_menu;

public class AddToyCommand implements Command {
    @Override
    public void execute() {
        System.out.println("Виконується команда 'Додати іграшку'...");
    }
}