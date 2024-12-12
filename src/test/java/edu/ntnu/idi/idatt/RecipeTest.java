package edu.ntnu.idi.idatt;

import static org.junit.jupiter.api.Assertions.*;

import edu.ntnu.idi.idatt.modules.Fridge;
import edu.ntnu.idi.idatt.modules.Grocery;
import edu.ntnu.idi.idatt.modules.Recipe;
import edu.ntnu.idi.idatt.modules.RecipeManager;
import edu.ntnu.idi.idatt.modules.SI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RecipeTest {
  private final SI g = new SI("Gram", "g", "kg", "");
  private final SI stk = new SI("Stykker", "stk", "stk", "");
  private final SI l = new SI("Liter", "L", "L", "");

  private Recipe recipe;
  private Fridge fridge;
  private Grocery grocery1;
  private Grocery grocery2;
  private Grocery grocery3;

  public Grocery fetchGrocery(String n, SI si, double q, LocalDate d, double p) {
    return new Grocery(n, si, q, d, p);
  }

  public void setRecipe(String name, String desc, String[] dir, int portion, List<Grocery> list) {
    recipe = new Recipe(name, desc, dir, portion, list);
  }

  @BeforeEach
  public void initTest() {
    fridge = new Fridge();
    grocery1 = fetchGrocery("Mel", g, 2000, LocalDate.now().minusDays(2),
        200);
    grocery2 = fetchGrocery("Bananer", stk, 18, LocalDate.now(), 49.90);
    grocery3 = fetchGrocery("Kraft", l, 0.5, LocalDate.now().plusDays(1),
        259.99);

    fridge.addGrocery(grocery1);
    fridge.addGrocery(grocery2);
    fridge.addGrocery(grocery3);

    setRecipe(
        "Banankake",
        "God kake.",
        new String[]{"Instruksjon 1:", "Instruksjon 2:", "Instruksjon 3:"},
        4,
        new ArrayList<>(List.of(new Grocery[] {
            grocery1,
            grocery2,
            fetchGrocery("Sjokolade", g, 500, LocalDate.now(), 149.50)
        }))
    );
  }

  @AfterEach
  public void closeTest() {
    fridge = null;
    grocery1 = grocery2 = grocery3 = null;
    recipe = null;
    Grocery.resetID();
    Recipe.resetID();
  }

  @Test
  void matchingGroceries() {
    final RecipeManager rm = new RecipeManager(recipe, fridge);
    assertEquals((double) 2 / 3, rm.matchingGroceries());
  }

  @Test
  void getRecipeID() {
    assertEquals(1, recipe.getRecipeID());
    setRecipe(
        "Banankake",
        "God kake.",
        new String[]{"Instruks 1:", "Instruks 2:", "Instruks 3:"},
        4,
        new ArrayList<>(List.of(new Grocery[] {
            grocery1,
            grocery2,
            fetchGrocery("Sjokolade", g, 500, LocalDate.now(), 149.50)
        }))
    );
    assertEquals(2, recipe.getRecipeID());
  }

  @Test
  void getName() {
    assertEquals("Banankake", recipe.getName());
  }

  @Test
  void getDescription() {
    assertEquals("God kake.", recipe.getDescription());
  }

  @Test
  void getDirections() {
    String[] strArr = new String[] {"Instruksjon 1:", "Instruksjon 2:", "Instruksjon 3:"};
    assertArrayEquals(recipe.getDirections(), strArr,
        "Forventet: " + Arrays.toString(strArr) + ", men fikk: " +
            Arrays.toString(recipe.getDirections()));
  }

  @Test
  void getPortion() {
    assertEquals(4, recipe.getPortion());
  }

  @Test
  void getRecipes() {
    Grocery.resetID();
    grocery1 = fetchGrocery("Mel", g, 2000, LocalDate.now().minusDays(2),
        200);
    grocery2 = fetchGrocery("Bananer", stk, 18, LocalDate.now(), 49.90);
    fetchGrocery("Bananer", stk, 18, LocalDate.now(), 49.90);
    grocery3 = fetchGrocery("Sjokolade", g, 500, LocalDate.now(), 149.50);

    ArrayList<Grocery> list = new ArrayList<>();
    list.add(grocery1);
    list.add(grocery2);
    list.add(grocery3);

    //has equal content, but for some reason assertion fails'
    assertEquals(list.stream().toList(), recipe.getRecipes());
  }
}