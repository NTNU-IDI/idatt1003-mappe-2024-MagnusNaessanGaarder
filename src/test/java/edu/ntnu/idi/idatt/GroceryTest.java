package edu.ntnu.idi.idatt;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class GroceryTest {

    @Test
    void groceryAddAmount() {
        Grocery grocery = new Grocery("Melk", new SI("Liter", "L","L",""), 10, LocalDate.now(), 10, null);

        grocery.addAmount(10,  new SI("Liter", "L","L",""));

        assertEquals(20, grocery.getQuantity(),"Quantity should be 20, but wasn't");
    }


    @Test
    void groceryFailAddAmount() {
        Grocery grocery = new Grocery("Melk", new SI("Liter", "L","L",""), 10, LocalDate.now(), 10, null);

        assertThrows(IllegalArgumentException.class, () -> grocery.addAmount(-1, new SI("Liter", "L","L","")), "Grocery.addAmount should throw an error on negative values");
    }

    @Test
    void groceryRemoveAmount() {
        Grocery grocery = new Grocery("Melk", new SI("Liter", "L","L",""), 1, LocalDate.now(), 1, null);
        assertEquals(1, grocery.removeAmount(9, new SI("Desiliter", "dL","L","Desi")), "Expected 9 but got another value.");
    }

    @Test
    void groceryFailRemoveAmount() {
        Grocery grocery = new Grocery("Melk", new SI("Liter", "L","L",""), 1, LocalDate.now(), 1, null);
        assertThrows(IllegalArgumentException.class, () -> grocery.removeAmount(-1, new SI("Desiliter", "dL", "L", "Desi")), "Grocery.removeAmount should throw an error on negative values");
    }


    @Test
    void groceryGetDate() {
        Grocery grocery = new Grocery("Melk", new SI("Liter", "L","L",""), 10, LocalDate.now(), 10, null);
        assertEquals("26 oktober 2024", grocery.getDateToStr(), "Expected date does not match return value.");
    }

    @Test
    void groceryConvertUnit() {
        Grocery grocery = new Grocery("Melk", new SI("Desiliter", "dl","L","Desi"), 10, LocalDate.now(), 10, null);
        assertEquals(1,grocery.convertUnit(), "Expected amount to be 1, but wasn't");

        grocery = new Grocery("Mel", new SI("Kilogram", "kg","kg","Kilo"), 0.9, LocalDate.now(), 10, null);
        assertEquals(900,grocery.convertUnit(), "Expected amount to be 900, but wasn't");
    }
}