package edu.ntnu.idi.idatt;

import edu.ntnu.idi.idatt.modules.SI;
import edu.ntnu.idi.idatt.modules.Fridge;
import edu.ntnu.idi.idatt.modules.Grocery;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class GroceryTest {
    final Fridge fridge = new Fridge();
    final Grocery grocery = new Grocery("Melk", new SI("Liter", "L","L",""), 10, LocalDate.now(), 10, fridge);

    @Test
    void groceryGetName() {
        assertEquals("Melk", grocery.getName(), "Name should be Melk, but wasn't");
    }


    @Test
    void groceryAddAmount() {
        grocery.addAmount(10,  new SI("Liter", "L","L",""));

        assertEquals(20, grocery.getQuantity(),"Quantity should be 20, but wasn't");
    }


    @Test
    void groceryFailAddAmount() {
        assertThrows(IllegalArgumentException.class, () -> grocery.addAmount(-1, new SI("Liter", "L","L","")), "Grocery.addAmount should throw an error on negative values");
    }

    @Test
    void groceryRemoveAmount() {
        Grocery g = new Grocery("Melk", new SI("Liter", "L","L",""), 1, LocalDate.now(), 1, null);

        g.removeAmount(9, new SI("Desiliter", "dL","L","Desi"));
        assertEquals(1, g.getQuantity(), "Expected 9 but got another value.");
    }

    @Test
    void groceryGetQuantity() {
        Grocery g = new Grocery("Melk", new SI("Liter", "L","L",""), 1, LocalDate.now(), 1, null);
        assertEquals(1, g.getQuantity(), "Quantity should be 1, but wasn't");
    }

    @Test
    void groceryGetPrice() {
        Grocery g = new Grocery("Melk", new SI("Liter", "L","L",""), 1, LocalDate.now(), 1, null);
        assertEquals(1, g.getPrice(), "Price should be 1, but wasn't");
    }

    @Test
    void groceryGetPriceToStr() {
        Grocery g = new Grocery("Melk", new SI("Liter", "L","L",""), 1, LocalDate.now(), 1, null);
        assertEquals("1,00 kr/L", g.getPriceToStr(), "PriceToStr should display the price as \"1,00 kr/L\", but didn't");
    }

    @Test
    void groceryFailRemoveAmount() {
        final Grocery g = new Grocery("Melk", new SI("Liter", "L","L",""), 1, LocalDate.now(), 1, null);

        assertThrows(IllegalArgumentException.class, () -> g.removeAmount(-1, new SI("Desiliter", "dL", "L", "Desi")), "Grocery.removeAmount() should throw an error on negative values");
    }

    @Test
    void groceryGetDate() {
        Grocery g = new Grocery("Melk", new SI("Liter", "L","L",""), 10, LocalDate.now(), 10, null);
        assertEquals(LocalDate.now(), g.getDate(), "Date should be " + grocery.getDate() + ", but wasn't");
    }

    @Test
    void groceryGetDateToStr() {
        Grocery g = new Grocery("Melk", new SI("Liter", "L","L",""), 10, LocalDate.of(2024,10,26), 10, null);
        assertEquals("26 oktober 2024", g.getDateToStr(), "Expected date does not match return value.");
    }


    @Test
    void groceryConvertUnit() {
        Grocery g = new Grocery("Melk", new SI("Desiliter", "dl","L","Desi"), 0.9, LocalDate.now(), 10, null);

        assertEquals(90.0,g.getQuantity(), "Expected amount to be 90.0, but wasn't");

        g = new Grocery("Mel", new SI("Kilogram", "kg","kg","Kilo"), 0.9, LocalDate.now(), 10, null);
        assertEquals(900,g.getQuantity(), "Expected amount to be 900, but wasn't");
    }

    @Test
    void groceryHasExpired() {
        Grocery grocery1 = new Grocery("Melk", new SI("Desiliter", "dl","L","Desi"), 9, LocalDate.now(), 10, null);
        assertFalse(grocery1.hasExpired(), "Expected grocery to be expired");

        Grocery grocery2 = new Grocery("Melk", new SI("Desiliter", "dl","L","Desi"), 9, LocalDate.of(2023,12,24), 10, null);
        assertTrue(grocery2.hasExpired(), "Expected grocery to be expired");
    }
}