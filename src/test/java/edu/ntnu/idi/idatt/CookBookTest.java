package edu.ntnu.idi.idatt;

import static org.junit.jupiter.api.Assertions.*;

import edu.ntnu.idi.idatt.modules.CookBook;
import edu.ntnu.idi.idatt.modules.Grocery;
import edu.ntnu.idi.idatt.modules.Recipe;
import edu.ntnu.idi.idatt.modules.SI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CookBookTest {
  private final SI g = new SI("Gram", "g", "kg", "");
  private final SI kg = new SI("Kilogram", "kg", "kg", "Kilo");
  private final SI stk = new SI("Stykker", "stk", "stk", "");
  private final SI dl = new SI("Desiliter", "dL", "L", "Desi");
  private final SI ml = new SI("Milliliter", "mL", "L", "Milli");
  private final SI ts = new SI("Teskje", "ts", "", "Te");
  private final SI ss = new SI("Spiseskje", "ss", "", "Spise");

  private Recipe recipe1;
  private Recipe recipe2;
  private Recipe recipe3;
  private Recipe recipe4;
  private CookBook cb;

  public Grocery fetchGrocery(String n, SI si, double q, LocalDate d, double p) {
    return new Grocery(n, si, q, d, p);
  }

  public Recipe setRecipe(String name, String desc, String[] dir, int portion, List<Grocery> list) {
    return new Recipe(name, desc, dir, portion, list);
  }

  @BeforeEach
  public void testInit() {
    cb = new CookBook();

    recipe1 = setRecipe("Banankake", "God!!", new String[] {"ins1:", "ins2:"}, 4,
        new ArrayList<>(Arrays.asList(
            fetchGrocery("Banan", stk, 2, null, 1),
            fetchGrocery("Mel", kg, 0.5, null, 1),
            fetchGrocery("Egg", stk, 2, null, 1),
            fetchGrocery("Vaniljesukker", ts, 4, null, 1))));
    recipe2 = setRecipe("Brød", "Luftig!!", new String[] {"ins1:", "ins2:", "ins3:"}, 6,
            new ArrayList<>(Arrays.asList(
                fetchGrocery("Mel", g, 500, null, 1),
                fetchGrocery("Melk", dl, 2, null, 1),
                fetchGrocery("Egg", stk, 3, null, 1),
                fetchGrocery("Gjær", ss, 1, null, 1))));
    recipe3 = setRecipe("Penne Al Arabiata", "Spicy og digg!!", new String[] {"ins1:", "ins2:"}, 4,
            new ArrayList<>(Arrays.asList(
                fetchGrocery("Chilly", stk, 1, null, 1),
                fetchGrocery("Olivenolje", ml, 100, null, 1),
                fetchGrocery("Hvitløksfedd", stk, 2, null, 1),
                fetchGrocery("Hakkede tomater, Boks", stk, 2, null, 1),
                fetchGrocery("Persille", ss, 2, null, 1),
                fetchGrocery("Salt", ts, 2, null, 1))));
    recipe4 = setRecipe("Naan Brød", "Deilig!!", new String[]{"ajsd", "askhd"}, 2,
        new ArrayList<>(Arrays.asList(
            fetchGrocery("Egg", stk, 2, null, 1),
            fetchGrocery("Mel", dl, 2, null, 1),
            fetchGrocery("Koreander", ts, 1, null, 1),
            fetchGrocery("Salt", ts, 1, null, 1))));

    cb.addRecipe(recipe1);
    cb.addRecipe(recipe2);
    cb.addRecipe(recipe3);
  }

  @AfterEach
  public void closeTest() {
    Grocery.resetID();
    Recipe.resetID();
  }

  @Test
  void getRecipeList() {
    ArrayList<Recipe> list = new ArrayList<>(Arrays.asList(
        recipe1,
        recipe2,
        recipe3
    ));
    assertEquals(list, cb.getRecipeList());
  }

  @Test
  void addRecipe() {
    assertDoesNotThrow(() -> cb.addRecipe(recipe4));
    assertEquals(4, cb.getRecipeList().size());
    assertEquals("Naan Brød", cb.getRecipeList().getLast().getName());
  }

  @Test
  void negativeAddRecipe() {
    assertThrows(IllegalArgumentException.class, () -> cb.addRecipe(recipe2));
  }

  @Test
  void removeRecipe() {
    assertDoesNotThrow(() -> cb.removeRecipe(recipe1));
    assertEquals(2, cb.getRecipeList().size());
  }

  @Test
  void negativeRemoveRecipe() {
    assertThrows(IllegalArgumentException.class, () -> cb.removeRecipe(recipe4));
  }
}