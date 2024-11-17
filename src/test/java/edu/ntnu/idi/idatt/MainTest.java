package edu.ntnu.idi.idatt;

import edu.ntnu.idi.idatt.Utils.SI;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static edu.ntnu.idi.idatt.Manager.Abstract_SI_manager.getUnit;
import static edu.ntnu.idi.idatt.Manager.Abstract_SI_manager.isValidUnit;
import static org.junit.jupiter.api.Assertions.*;

class MainTest {
    @Test
    void testAddToFridgeDate() {
        //enhet og mengde test
        SI meassure;
        double quantity;

        String[] testRes = testQuantityUnit();

        meassure = getUnit(String.join("",testRes[0].split(" ")));
        String quantityStr = String.join("", testRes[1].split(" "));
        quantity = Double.parseDouble(String.join(",", quantityStr.split("/[,.]/gm")));


        SI testUnit = new SI("Liter","L","L","");

        assertEquals(testUnit,meassure);
        assertEquals(2.0, quantity);


        //Best-før dato-test
        assertEquals(LocalDate.of(2025,1,1), testDate("01-01-2025"), "Expected Date of 21-03-2003, but got a different value.");
        assertThrows(RuntimeException.class, () -> testDate("11-02-20033\n"), "Expected an RuntimeException, but got something else.");

        //Pris
        String userInput = "20,0 kr";
        String priceStr = String.join("", userInput.split("[^,.\\d]"));
        double price = Double.parseDouble(String.join(".", priceStr.split("[,.]")));
        assertEquals(20.0, price);
    }

    @Test
    void testGetUnit(){
        SI testUnit = new SI("Stykker","stk","stk","");
        assertEquals(testUnit,getUnit("Stykker"));
    }

    @Test
    void testValidUnit(){
        assertTrue(isValidUnit("Stykker"));
    }

    private String[] testQuantityUnit() {
        //mengden og enheten av varen
        String userInput;
        try{
            do {
                userInput = "2.0 liter";
            }
            while (!isValidUnit(String.join("",userInput.split(" ")[1])));
        }
        catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        String[] res = userInput.split(" ");

        return new String[]{res[1], res[0]};
    }

    private LocalDate testDate(String input) {
        try{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            return LocalDate.parse(input, formatter);
        }
        catch (Exception e) {
            throw new RuntimeException("Ugyldig format");
        }
    }
}