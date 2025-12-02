package controller_menu;
import logic.GameRoomService;

public class ShowCatalogCommand implements Command {
    private GameRoomService service;

    public ShowCatalogCommand(GameRoomService service) {
        this.service = service;
    }

    @Override
    public void execute() {
        service.showWholeCatalog();
    }
}