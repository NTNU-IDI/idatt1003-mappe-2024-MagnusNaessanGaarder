package edu.ntnu.idi.idatt;

import edu.ntnu.idi.idatt.UI.Table;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TableTest {
    @Test
    void printTable() {
        String title = "Title";
        String[] colTitle = new String[]{"testTittel1","testTittel2"};
        String[] colData = new String[]{"data1","data2"};

        Table testTable = new Table(title,colTitle,colData);
        String expectedStr =
                """
                        ------------------------Title------------------------
                        |       testTittel1       |          data1          |
                        |       testTittel2       |          data2          |
                        -----------------------------------------------------
                        
                        """;


        assertEquals(expectedStr, testTable.createTable());
    }
}