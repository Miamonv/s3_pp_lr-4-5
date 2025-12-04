package controller_menu;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MenuControllerTest {

    @Test
    void testRegisterAndExecuteCommand() {
        MenuController controller = new MenuController();
        final boolean[] wasExecuted = {false};

        Command dummyCommand = new Command() {
            @Override
            public void execute() {
                wasExecuted[0] = true;
            }
        };

        controller.register("1", dummyCommand);
        controller.executeCommand("1");

        Assertions.assertTrue(wasExecuted[0], "Команда мала виконатися після натискання '1'");
    }

    @Test
    void testExecuteUnknownCommand() {
        MenuController controller = new MenuController();
        Assertions.assertDoesNotThrow(() -> controller.executeCommand("999"));
    }
}