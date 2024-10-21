package edu.ntnu.idi.idatt;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class GroceryTest {

    @Test
    void groceryAddAmount() {
        Grocery grocery = new Grocery("Melk", new SI("Liter", "L","L",""), 10, LocalDate.now(), 10, null);

        grocery.addAmount(10);

        assertEquals(20, grocery.getQuantity(),"Quantity should be 20, but wasn't");
    }


    @Test
    void groceryFailAddAmount() {

        Grocery grocery = new Grocery("Melk", new SI("Liter", "L","L",""), 10, LocalDate.now(), 10, null);

        assertThrows(IllegalArgumentException.class, () -> grocery.addAmount(-1), "Grocery.addAmount should throw an error on negative values");
    }
}