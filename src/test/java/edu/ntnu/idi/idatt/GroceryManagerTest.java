package edu.ntnu.idi.idatt;

import static org.junit.jupiter.api.Assertions.*;

import edu.ntnu.idi.idatt.modules.Fridge;
import edu.ntnu.idi.idatt.modules.Grocery;
import edu.ntnu.idi.idatt.modules.GroceryManager;
import edu.ntnu.idi.idatt.modules.SI_Manager;
import java.time.LocalDate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GroceryManagerTest {

  Fridge fridge = new Fridge();
  GroceryManager gm;

  final Grocery grocery = new Grocery("test1", SI_Manager.getUnit("Liter"), 4,
      LocalDate.now(), 2);

  @BeforeEach
  public void initTest() {
    Grocery.resetID();
    fridge.addGrocery(grocery);
    gm = new GroceryManager(grocery);
  }
  @AfterEach
  public void closeTest() {
    fridge = new Fridge();
    gm = null;
  }

  @Test
  void getPricePerQuantity() {
    assertEquals(8.0, gm.getPricePerQuantity());
  }

  @Test
  void addAmountGrocery() {
    String[] str = new String[]{"2","Desiliter"};
    try {
      gm.addAmountGrocery(str);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    assertEquals(4.2, grocery.getQuantity());
  }

  @Test
  void removeAmountGrocery() {
    String[] str = new String[]{"2","L"};
    try {
      gm.removeAmountGrocery(str);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    assertEquals(2, grocery.getQuantity());

    try {
      gm.removeAmountGrocery(str);
      if (grocery.getQuantity() <= 0) {
        fridge.removeGrocery(grocery);
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    assertEquals(0, grocery.getQuantity());
    assertFalse(fridge.getGroceryList().contains(grocery));
  }
}