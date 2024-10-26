package edu.ntnu.idi.idatt;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class FridgeTest {

    @Test
    void addGrocery() {
        SI kg = new SI("Kilogram", "kg","kg","Kilo");
        Fridge fridge = new Fridge();
        Grocery grocery = new Grocery ("Mel", kg, 2, LocalDate.now(), 25, fridge);
        fridge.addGrocery(grocery);

        assertEquals(grocery, fridge.getGrocery(0), "Did not get expected grocery.");
    }

    @Test
    void removeGrocery() {
        SI kg = new SI("Kilogram", "kg","kg","Kilo");
        Fridge fridge = new Fridge();
        Grocery grocery = new Grocery ("Mel", kg, 2, LocalDate.now(), 25, fridge);
        fridge.addGrocery(grocery);

        fridge.removeGrocery(grocery);
        ArrayList<Grocery> emptyList = new ArrayList<Grocery>(0);
        assertEquals(emptyList,fridge.getGroceryList(), "Did not remove grocery as expected.");
    }
}