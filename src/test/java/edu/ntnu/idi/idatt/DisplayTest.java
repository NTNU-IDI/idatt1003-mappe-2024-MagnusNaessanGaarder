package edu.ntnu.idi.idatt;

import edu.ntnu.idi.idatt.manager.FridgeManager;
import edu.ntnu.idi.idatt.modules.Fridge;
import edu.ntnu.idi.idatt.utils.Display;
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
                        |       testTittel1       |       testTittel2       |
                        -----------------------------------------------------
                        |          data1          |          data2          |
                        -----------------------------------------------------
                        
                        """;


        assertEquals(expectedStr, testTable.createGroceryTable(title,colTitle,colData, 53));
    }
}