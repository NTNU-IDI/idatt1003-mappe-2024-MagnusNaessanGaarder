package edu.ntnu.idi.idatt;

import edu.ntnu.idi.idatt.Manager.FridgeManager;
import edu.ntnu.idi.idatt.Manager.GroceryManager;
import edu.ntnu.idi.idatt.Utils.SI;
import edu.ntnu.idi.idatt.Modules.Fridge;
import edu.ntnu.idi.idatt.Modules.Grocery;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class GroceryTest {
    final Fridge fridge = new Fridge();
    final Grocery grocery = new Grocery("Melk", new SI("Liter", "L","L",""), 10, LocalDate.now(), 10, fridge);
    final FridgeManager fm = new FridgeManager(fridge);

    @Test
    void groceryGetName() {
        final GroceryManager gm = new GroceryManager(grocery);
        assertEquals("Melk", grocery.getName(), "Name should be Melk, but wasn't");
    }


    @Test
    void groceryAddAmount() {
        final GroceryManager gm = new GroceryManager(grocery);
        gm.addAmount(10,  new SI("Liter", "L","L",""));

        assertEquals(20, grocery.getQuantity(),"Quantity should be 20, but wasn't");
    }


    @Test
    void groceryFailAddAmount() {
        final GroceryManager gm = new GroceryManager(grocery);

        assertThrows(IllegalArgumentException.class, () -> gm.addAmount(-1, new SI("Liter", "L","L","")), "Grocery.addAmount should throw an error on negative values");
    }

    @Test
    void groceryRemoveAmount() {
        Grocery grocery = new Grocery("Melk", new SI("Liter", "L","L",""), 1, LocalDate.now(), 1, null);
        final GroceryManager gm = new GroceryManager(grocery);
        gm.removeAmount(9, new SI("Desiliter", "dL","L","Desi"));
        assertEquals(1, grocery.getQuantity(), "Expected 9 but got another value.");
    }

    @Test
    void groceryGetQuantity() {
        Grocery grocery = new Grocery("Melk", new SI("Liter", "L","L",""), 1, LocalDate.now(), 1, null);
        assertEquals(1, grocery.getQuantity(), "Quantity should be 1, but wasn't");
    }

    @Test
    void groceryGetPrice() {
        Grocery grocery = new Grocery("Melk", new SI("Liter", "L","L",""), 1, LocalDate.now(), 1, null);
        assertEquals(1, grocery.getPrice(), "Price should be 1, but wasn't");
    }

    @Test
    void groceryGetPriceToStr() {
        Grocery grocery = new Grocery("Melk", new SI("Liter", "L","L",""), 1, LocalDate.now(), 1, null);
        assertEquals("1,00 kr/L", grocery.getPriceToStr(), "PriceToStr should display the price as \"1,00 kr/L\", but didn't");
    }

    @Test
    void groceryFailRemoveAmount() {
        Grocery grocery = new Grocery("Melk", new SI("Liter", "L","L",""), 1, LocalDate.now(), 1, null);
        final GroceryManager gm = new GroceryManager(grocery);
        assertThrows(IllegalArgumentException.class, () -> gm.removeAmount(-1, new SI("Desiliter", "dL", "L", "Desi")), "Grocery.removeAmount should throw an error on negative values");
    }

    @Test
    void groceryGetDate() {
        Grocery grocery = new Grocery("Melk", new SI("Liter", "L","L",""), 10, LocalDate.now(), 10, null);
        assertEquals(LocalDate.now(), grocery.getDate(), "Date should be " + grocery.getDate() + ", but wasn't");
    }

    @Test
    void groceryGetDateToStr() {
        Grocery grocery = new Grocery("Melk", new SI("Liter", "L","L",""), 10, LocalDate.of(2024,10,26), 10, null);
        assertEquals("26 oktober 2024", grocery.getDateToStr(), "Expected date does not match return value.");
    }


    @Test
    void groceryConvertUnit() {
        Grocery grocery = new Grocery("Melk", new SI("Desiliter", "dl","L","Desi"), 0.9, LocalDate.now(), 10, null);
        final GroceryManager gm = new GroceryManager(grocery);
        assertEquals(90.0,grocery.getQuantity(), "Expected amount to be 90.0, but wasn't");

        grocery = new Grocery("Mel", new SI("Kilogram", "kg","kg","Kilo"), 0.9, LocalDate.now(), 10, null);
        final GroceryManager gm1 = new GroceryManager(grocery);
        assertEquals(900,grocery.getQuantity(), "Expected amount to be 900, but wasn't");
    }

    @Test
    void groceryHasExpired() {
        Grocery grocery1 = new Grocery("Melk", new SI("Desiliter", "dl","L","Desi"), 9, LocalDate.now(), 10, null);
        assertFalse(grocery1.hasExpired(), "Expected grocery to be expired");

        Grocery grocery2 = new Grocery("Melk", new SI("Desiliter", "dl","L","Desi"), 9, LocalDate.of(2023,12,24), 10, null);
        assertTrue(grocery2.hasExpired(), "Expected grocery to be expired");
    }
}