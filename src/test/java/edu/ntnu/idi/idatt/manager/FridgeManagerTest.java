package edu.ntnu.idi.idatt.manager;

import static org.junit.jupiter.api.Assertions.*;

import edu.ntnu.idi.idatt.modules.Fridge;
import edu.ntnu.idi.idatt.modules.Grocery;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FridgeManagerTest {

  Fridge fridge = new Fridge();
  FridgeManager fm = new FridgeManager(fridge);

  final Grocery grocery1 = new Grocery("test1", SI_Manager.getUnit("stk"), 4,
      LocalDate.now(), 2);
  final Grocery grocery2 = new Grocery("test2", SI_Manager.getUnit("L"), 5,
      LocalDate.now().minusDays(4), 100);
  final Grocery grocery3 = new Grocery("test3", SI_Manager.getUnit("dL"), 7,
      LocalDate.now().plusDays(7), 20);
  final Grocery grocery4 = new Grocery("test4", SI_Manager.getUnit("gram"), 2000,
      LocalDate.now().plusDays(1), 189);

  @BeforeEach
  public void initTest() {
    Grocery.resetID();
    fm = new FridgeManager(fridge);
    fridge.addGrocery(grocery1);
    fridge.addGrocery(grocery2);
    fridge.addGrocery(grocery3);
    fridge.addGrocery(grocery4);
  }
  @AfterEach
  public void closeTest() {
    fridge = new Fridge();
    fm = null;
  }

  @Test
  void getGroceryListIndex() {
    assertEquals(0, fm.getGroceryListIndex(1));
  }

  @Test
  void getExpiredList() {
    List<Grocery> expired = fm.getExpiredList();
    assertEquals(grocery2, expired.getFirst());
  }

  @Test
  void getNearExpList() {
    assertEquals(new ArrayList<>(Arrays.asList(grocery1, grocery4)).stream().toList(),
        fm.getNearExpList());
  }

  @Test
  void getRestGroceryList() {
    assertEquals(new ArrayList<>(List.of(grocery3)).stream().toList(),
        fm.getRestGroceryList());
  }

  @Test
  void getDatesBefore() {
    assertEquals(new ArrayList<>(List.of(grocery2)).stream().toList(),
        fm.getDatesBefore(LocalDate.now().minusDays(3)));
    assertEquals(new ArrayList<>(List.of(grocery2, grocery1, grocery4, grocery3)).stream().toList(),
        fm.getDatesBefore(LocalDate.now().plusMonths(1)));
  }

  @Test
  void getMoneyLossStr() {
    String str = "          1 varer er gått ut på dato.\n";
    str += "          Du har tapt 500,00 kr.";
    assertEquals(str, fm.getMoneyLossStr());
  }

  @Test
  void getMoneyLoss() {
    assertEquals(500.00, fm.getMoneyLoss());
  }

  @Test
  void getTotalPrice() {
    assertEquals(900, fm.getTotalPrice());
  }

  @Test
  void getByName() {
    assertEquals(grocery1, fm.getByName("TeSt1").getFirst());
    assertEquals(new ArrayList<>().stream().toList(), fm.getByName("TeSt9"));
  }
}