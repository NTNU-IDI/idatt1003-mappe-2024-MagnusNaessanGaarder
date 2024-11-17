package edu.ntnu.idi.idatt;

import edu.ntnu.idi.idatt.Manager.FridgeManager;
import edu.ntnu.idi.idatt.Modules.Fridge;
import edu.ntnu.idi.idatt.Utils.Display;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DisplayTest {
    private final Fridge fridge = new Fridge();
    private final FridgeManager fm = new FridgeManager(fridge);
    @Test
    void printTable() {
        String title = "Title";
        String[] colTitle = new String[]{"testTittel1","testTittel2"};
        String[] colData = new String[]{"data1","data2"};

        Display testTable = new Display(fm);
        String expectedStr =
                """
                        
                        ------------------------Title------------------------
                        |       testTittel1       |          data1          |
                        |       testTittel2       |          data2          |
                        -----------------------------------------------------
                        
                        """;


        assertEquals(expectedStr, testTable.createTable(title,colTitle,colData));
    }
}