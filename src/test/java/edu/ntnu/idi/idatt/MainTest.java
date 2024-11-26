package edu.ntnu.idi.idatt;

import edu.ntnu.idi.idatt.modules.SI;
import edu.ntnu.idi.idatt.manager.SI_Manager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {
    private static SI testUnit;
    @BeforeAll
    static void instantice() {
        testUnit = new SI("Liter","L","L","");
    }

    @Test
    void testAddToFridgeDate() {
        //enhet og mengde test
        SI meassure;
        double quantity;

        String[] testRes = {"Liter", "2.0"};

        meassure = SI_Manager.getUnit(String.join("",testRes[0].split(" ")));
        String quantityStr = String.join("", testRes[1].split(" "));
        quantity = Double.parseDouble(String.join(",", quantityStr.split("/[,.]/gm")));

        assertEquals(testUnit,meassure);
        assertEquals(2.0, quantity);
    }

    @Test
    void testDate() {
        //Best-før dato-test
        assertEquals(LocalDate.of(2025,1,1), testDate("01-01-2025"), "Expected Date of 21-03-2003, but got a different value.");
        assertThrows(RuntimeException.class, () -> testDate("11-02-20033\n"), "Expected an RuntimeException, but got something else.");
    }

    @Test
    void testPrice() {
        //Pris
        String userInput = "20,0 kr";
        String priceStr = String.join("", userInput.split("[^,.\\d]"));
        double price = Double.parseDouble(String.join(".", priceStr.split("[,.]")));
        assertEquals(20.0, price);
    }

    @Test
    void testGetUnit(){
        SI testUnit = new SI("Stykker","stk","stk","");
        assertEquals(testUnit, SI_Manager.getUnit("Stykker"));
    }

    @Test
    void testValidUnit(){
        assertThrows(IllegalArgumentException.class, () -> SI_Manager.getUnit("sdfkjsdkjfhs"));
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