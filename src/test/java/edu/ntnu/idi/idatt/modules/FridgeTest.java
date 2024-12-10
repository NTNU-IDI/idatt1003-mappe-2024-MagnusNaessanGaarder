package edu.ntnu.idi.idatt.modules;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class FridgeTest {
    private final Fridge fridge = new Fridge();
    private final FridgeManager fm = new FridgeManager(fridge);

    @Test
    void fridgeGetGroceryList() {
        //Lager et kjøleskap, en SI-enhet og en vare. Legger til varen i kjøleskapet
        SI kg = new SI("Kilogram", "kg","kg","Kilo");

        Grocery grocery = new Grocery ("Mel", kg, 2, LocalDate.now(), 25);
        fridge.addGrocery(grocery);

        //lager en test-liste som inneholder varen lagd ovenfor.
        ArrayList<Grocery> testList = new ArrayList<>();
        testList.add(grocery);

        //tester om test-listen samsvarer med listen over varer i kjøleskapet.
        assertIterableEquals(testList, fridge.getGroceryList(),"Expected Arraylist with groceryObject, but got something else");
    }

    @Test
    void fridgeGetGrocery() {
        //Lager et kjøleskap, en SI-enhet og en vare. Legger til varen i kjøleskapet
        SI kg = new SI("Kilogram", "kg","kg","Kilo");
        SI stk = new SI("Stykker", "stk", "stk","");
        Grocery grocery1 = new Grocery ("Egg", stk, 18, LocalDate.now(), 25);
        Grocery grocery2 = new Grocery ("Mel", kg, 2, LocalDate.now(), 25);
        fridge.addGrocery(grocery1);
        fridge.addGrocery(grocery2);


        //tester om test-listen samsvarer med listen over varer i kjøleskapet.
        assertEquals(grocery1, fridge.getGrocery(0),"Expected Arraylist with groceryObject, but got something else");
        assertEquals(grocery2, fridge.getGrocery(1),"Expected Arraylist with groceryObject, but got something else");
    }

    @Test
    void fridgeAddGrocery() {
        SI kg = new SI("Kilogram", "kg","kg","Kilo");
        Grocery grocery = new Grocery ("Mel", kg, 2, LocalDate.now(), 25);
        fridge.addGrocery(grocery);

        assertEquals(grocery, fridge.getGrocery(0), "Did not get expected grocery.");
    }

    @Test
    void fridgeRemoveGrocery() {
        SI kg = new SI("Kilogram", "kg","kg","Kilo");
        Grocery grocery = new Grocery ("Mel", kg, 2, LocalDate.now(), 25);
        fridge.addGrocery(grocery);

        fridge.removeGrocery(grocery);
        ArrayList<Grocery> emptyList = new ArrayList<>(0);
        assertEquals(emptyList,fridge.getGroceryList(), "Did not remove grocery as expected.");
    }

    @Test
    void fridgeGetMoneyLoss() {
        SI L = new SI("Liter", "L","L","");
        SI dL = new SI("Desiliter", "dL","L","Desi");
        SI stk = new SI("Stykker", "stk","stk","");

        Grocery grocery1 = new Grocery ("Melk", L, 1, LocalDate.now(), 49);
        Grocery grocery2 = new Grocery ("Melk", dL, 5, LocalDate.now(), 49);
        Grocery grocery3 = new Grocery ("Coca Cola", dL, 7.5, LocalDate.of(2023, 3,21), 25);
        Grocery grocery4 = new Grocery ("Egg", stk, 18, LocalDate.of(2024,9,30), 3);

        fridge.addGrocery(grocery1);
        fridge.addGrocery(grocery2);
        fridge.addGrocery(grocery3);
        fridge.addGrocery(grocery4);

        assertEquals("          2 varer er gått ut på dato.\n          Du har tapt 72,75 kr.",fm.getMoneyLossStr(), "Did not get expected money loss.");
        assertEquals(1.5, fridge.getGroceryList().getFirst().getQuantity());
    }
}