package edu.ntnu.idi.idatt;

import edu.ntnu.idi.idatt.modules.SI;
import edu.ntnu.idi.idatt.modules.Fridge;
import edu.ntnu.idi.idatt.modules.Grocery;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class GroceryTest {
  private Grocery grocery;
  final private SI liter = new SI("Liter", "L","L","");
  private Fridge fridge;

  public void fetchGrocery(String n, SI si, double q, LocalDate d, double p, Fridge f) {
    grocery = new Grocery(n, si, q, d, p, f);
  }

  @BeforeEach
  public void initTest() {
    fetchGrocery("Melk", liter, 10, LocalDate.now(), 10, fridge);
    fridge = new Fridge();
  }

  @AfterEach
  public void closeTest() {
    fridge = null;
    grocery = null;
    Grocery.resetID();
  }

  @Test
  void testGroceryID() {
    assertEquals(1, grocery.getGroceryID(), "Expected another value for the "
        + "GroceryID: " + grocery.getGroceryID());

    initTest();
    assertEquals(2, grocery.getGroceryID(), "Expected another value for the "
        + "GroceryID: " + grocery.getGroceryID());
  }

  @Test
  void groceryGetName() {
    assertEquals("Melk", grocery.getName(), "Name should be "
        + grocery.getName() + ", but wasn't");
  }

  @Test
  void groceryAddAmount() {
    assertDoesNotThrow(() -> grocery.addAmount(
        10, new SI("Liter", "L","L","")
    ));
    assertEquals(20, grocery.getQuantity(),"Quantity should be 20, but wasn't");
  }

  @Test
  void negativeAddAmount() {
    fetchGrocery("Melk", liter, 10, LocalDate.now(), 10, fridge);
    assertThrows(IllegalArgumentException.class, () -> grocery.addAmount(-10, liter),
        "Quantity should be 20, but wasn't");
  }

  @Test
  void groceryFailAddAmount() {
    //tester feil mengde
    assertThrows(IllegalArgumentException.class, () ->
            grocery.addAmount(-1, new SI("Liter", "L","L","")),
          "Grocery.addAmount should throw an error on negative values");

    //Tester feil enhet
    assertThrows(IllegalArgumentException.class, () ->
            grocery.addAmount(1, new SI("sdjkfskd", "sdfs","dsf","sdkf")),
          "Grocery.addAmount should throw an error on negative values");
  }

  @Test
  void groceryRemoveAmount() {
    fetchGrocery("Melk", liter, 1, LocalDate.now(), 1, fridge);
    grocery.removeAmount(9,
        new SI("Desiliter", "dL","L","Desi"));
    assertEquals(1, grocery.getQuantity(), "Expected 1 but got another value.");
  }

  @Test
  void failRemoveAmount() {
    fetchGrocery("Melk", liter, 1, LocalDate.now(), 1, fridge);
    assertThrows(IllegalArgumentException.class, () -> grocery.removeAmount(-1, liter),
        "Grocery.removeAmount() should throw an error on negative values");
  }

  @Test
  void groceryGetQuantity() {
    fetchGrocery("Melk", liter, 1, LocalDate.now(), 10, fridge);
    assertEquals(1, grocery.getQuantity(), "Quantity should be 1, but wasn't");
  }

  @Test
  void groceryGetPrice() {
    fetchGrocery("Melk", liter, 1, LocalDate.now(), 1, fridge);
    assertEquals(1, grocery.getPrice(), "Price should be 1, but wasn't");
  }

  @Test
  void groceryGetPriceToStr() {
    fetchGrocery("Melk", liter, 1, LocalDate.now(), 2.25, fridge);
    assertEquals("2,25 kr/L", grocery.getPriceToStr(),
        "PriceToStr should display the price as \"2,25 kr/L\", but didn't");
  }

  @Test
  void groceryGetDate() {
    fetchGrocery("Melk", liter, 10, LocalDate.now(), 10, null);
    assertEquals(LocalDate.now(), grocery.getDate(), "Date should be " + grocery.getDate() + ", but wasn't");
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