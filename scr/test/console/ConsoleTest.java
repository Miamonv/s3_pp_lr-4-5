package console;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.Scanner;

public class ConsoleTest {

    @Test
    void testConsoleView() {
        ConsoleView view = new ConsoleView(new Scanner("1\n"));
        Assertions.assertNotNull(view);
    }

    @Test
    void testMenuDisplay() {
        ConsoleView view = new ConsoleView(new Scanner("1\n"));
        view.showMenu();
    }

    @Test
    void testUserInput() {
        String simulatedInput1 = "2\n\n";
        Scanner input1 = new Scanner(simulatedInput1);
        ConsoleView view1 = new ConsoleView(input1);
        String res1 = view1.getUserInput();
        Assertions.assertEquals("2", res1);

        String simulatedInput2 = "\n\n\n\n\n\n\n2\n\n";
        Scanner input2 = new Scanner(simulatedInput2);
        ConsoleView view2 = new ConsoleView(input2);
        String res2 = view2.getUserInput();

        String simulatedInput3 = "\n  \n";
        Scanner input3 = new Scanner(simulatedInput3);
        ConsoleView view3 = new ConsoleView(input3);
        String res3 = view3.getUserInput();

        Assertions.assertEquals("", res3);
        view2.pressEnterToContinue();

    }
}
