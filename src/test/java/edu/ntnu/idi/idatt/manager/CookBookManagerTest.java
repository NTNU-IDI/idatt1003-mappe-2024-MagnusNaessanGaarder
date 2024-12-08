package edu.ntnu.idi.idatt.manager;

import static org.junit.jupiter.api.Assertions.*;

import edu.ntnu.idi.idatt.modules.CookBook;
import edu.ntnu.idi.idatt.modules.Fridge;
import edu.ntnu.idi.idatt.modules.Grocery;
import edu.ntnu.idi.idatt.modules.Recipe;
import edu.ntnu.idi.idatt.modules.SI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CookBookManagerTest {
  private final SI g = new SI("Gram", "g", "kg", "");
  private final SI kg = new SI("Kilogram", "kg", "kg", "Kilo");
  private final SI stk = new SI("Stykker", "stk", "stk", "");
  private final SI dl = new SI("Desiliter", "dL", "L", "Desi");
  private final SI ml = new SI("Milliliter", "mL", "L", "Milli");
  private final SI ts = new SI("Teskje", "ts", "", "Te");
  private final SI ss = new SI("Spiseskje", "ss", "", "Spise");

  private Grocery grocery1;
  private Grocery grocery2;
  private Grocery grocery3;
  private Grocery grocery4;
  private Grocery grocery5;
  private Grocery grocery6;

  private Recipe recipe1;
  private Recipe recipe2;
  private Recipe recipe3;
  private Fridge fridge;
  private CookBook cb;
  private CookBookManager cbm;

  public Grocery fetchGrocery(String n, SI si, double q, LocalDate d, double p) {
    return new Grocery(n, si, q, d, p);
  }

  public Recipe setRecipe(String name, String desc, String[] dir, int portion, List<Grocery> list,
                          Fridge f) {
    return new Recipe(name, desc, dir, portion, list, f);
  }

  @BeforeEach
  public void testInit() {
    fridge = new Fridge();
    cb = new CookBook();
    cbm = new CookBookManager(cb, fridge);

    grocery1 = new Grocery("Mel", g, 2000,
        LocalDate.now().minusDays(2), 200);
    grocery2 = new Grocery("Banan", stk, 3,
        LocalDate.now(), 49.90);
    grocery3 = new Grocery("Mel", g, 1000,
        LocalDate.now().plusDays(4), 200);
    grocery4 = new Grocery("Kraft", SI_Manager.getUnit("L"), 0.5,
        LocalDate.now().plusDays(1), 259.99);
    grocery5 = new Grocery("Melk", dl, 2,
        LocalDate.now().plusYears(1), 180.50);
    grocery6 = new Grocery("Egg", stk, 5,
        LocalDate.now(), 1);

    fridge.addGrocery(grocery1);
    fridge.addGrocery(grocery2);
    fridge.addGrocery(grocery3);
    fridge.addGrocery(grocery4);
    fridge.addGrocery(grocery5);
    fridge.addGrocery(grocery6);

    recipe1 = setRecipe("Banankake", "God!!", new String[] {"ins1:", "ins2:"}, 4,
        new ArrayList<>(Arrays.asList(
            fetchGrocery("Banan", stk, 2, null, 1),
            fetchGrocery("Mel", kg, 0.5, null, 1),
            fetchGrocery("Egg", stk, 2, null, 1),
            fetchGrocery("Vaniljesukker", ts, 4, null, 1))),
        fridge);
    recipe2 = setRecipe("Brød", "Luftig!!", new String[] {"ins1:", "ins2:", "ins3:"}, 6,
        new ArrayList<>(Arrays.asList(
            fetchGrocery("Mel", g, 500, null, 1),
            fetchGrocery("Melk", dl, 2, null, 1),
            fetchGrocery("Egg", stk, 3, null, 1),
            fetchGrocery("Gjær", ss, 1, null, 1))),
        fridge);
    recipe3 = setRecipe("Penne Al Arabiata", "Spicy og digg!!", new String[] {"ins1:", "ins2:"}, 4,
        new ArrayList<>(Arrays.asList(
            fetchGrocery("Chilly", stk, 1, null, 1),
            fetchGrocery("Olivenolje", ml, 100, null, 1),
            fetchGrocery("Hvitløksfedd", stk, 2, null, 1),
            fetchGrocery("Hakkede tomater, Boks", stk, 2, null, 1),
            fetchGrocery("Persille", ss, 2, null, 1),
            fetchGrocery("Salt", ts, 2, null, 1))),
        fridge);

    cb.addRecipe(recipe1);
    cb.addRecipe(recipe2);
    cb.addRecipe(recipe3);
  }

  @AfterEach
  public void closeTest() {
    fridge = null;
    cb = null;
    cbm = null;
    Grocery.resetID();
    Recipe.resetID();
  }

  @Test
  void getRecipe() {
    //the content should be the same
    assertEquals(recipe1.toString(), cbm.getRecipe(1).toString());
    assertEquals(recipe2.toString(), cbm.getRecipe(2).toString());
    assertEquals(recipe3.toString(), cbm.getRecipe(3).toString());
  }

  @Test
  void getNegativeRecipe() {
    assertThrows(IllegalArgumentException.class, () -> cbm.getRecipe(-1));
    assertThrows(ArrayIndexOutOfBoundsException.class, () -> cbm.getRecipe(4));
  }

  @Test
  void getAvailableRecipes() {
    final List<Recipe> list = new ArrayList<>(List.of(recipe1, recipe2)).stream().toList();
    assertEquals(list, cbm.getAvailableRecipes());
  }

  @Test
  void getRecommendedRecipes() {
    final List<Recipe> list = new ArrayList<>(List.of(recipe1)).stream().toList();
    assertEquals(list, cbm.getRecommendedRecipes());
  }

  @Test
  void getRest() {
    final List<Recipe> list = new ArrayList<>(List.of(recipe3)).stream().toList();
    assertEquals(list, cbm.getRest());
  }

  @Test
  void makeRecipe() {
    cbm.makeRecipe(recipe1);
    assertEquals(1, grocery2.getQuantity());
    assertEquals(1500, grocery1.getQuantity());
    assertEquals(3, grocery6.getQuantity());
  }

  @Test
  void negativeMakeRecipe() {
    assertThrows(IllegalArgumentException.class, () -> cbm.makeRecipe(new Recipe("test", "test", new String[] {"test"}, 1,
        new ArrayList<>(Collections.singletonList(
            fetchGrocery("test", g, 1, null, 1))), fridge)));

    fridge.removeGrocery(grocery1);
    fridge.removeGrocery(grocery2);
    fridge.removeGrocery(grocery3);
    fridge.removeGrocery(grocery4);
    fridge.removeGrocery(grocery5);
    fridge.removeGrocery(grocery6);

    assertThrows(IllegalArgumentException.class, () -> cbm.makeRecipe(recipe1));
  }
}