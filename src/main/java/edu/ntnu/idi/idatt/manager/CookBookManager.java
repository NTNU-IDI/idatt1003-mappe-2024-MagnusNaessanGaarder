package edu.ntnu.idi.idatt.manager;

import edu.ntnu.idi.idatt.modules.CookBook;
import edu.ntnu.idi.idatt.modules.Fridge;
import edu.ntnu.idi.idatt.modules.Grocery;
import edu.ntnu.idi.idatt.modules.Recipe;
import edu.ntnu.idi.idatt.modules.SI;
import java.util.Comparator;
import java.util.List;

public class CookBookManager {
  private final CookBook cookBook;
  private final Fridge fridge;

  public CookBookManager(final CookBook cookBook, final Fridge fridge) {
    this.cookBook = cookBook;
    this.fridge = fridge;
  }

  public Recipe getRecipe(final int id) {
    return cookBook.getRecipeList().stream()
        .filter(r -> r.getRecipeID() == id)
        .findFirst()
        .orElse(null);
  }

  private List<Recipe> sortRecipes(List<Recipe> list) {
    return list.stream()
        .sorted(
            Comparator.comparing(Recipe::matchingGroceries)
            .thenComparing(Recipe::avrageDate))
        .toList();
  }

  public List<Recipe> getAvailableRecipes() {
    return sortRecipes(cookBook.getRecipeList().stream()
        .filter(r -> r.matchingGroceries() >= 0.5)
        .toList());
  }
  public List<Recipe> getRecommendedRecipes() {
    return getAvailableRecipes().size() >= 3 ? getAvailableRecipes().subList(0, 3)
        : getAvailableRecipes().subList(0, getAvailableRecipes().size());
  }

  public List<Recipe> getRest() {
    final List<Recipe> sortedList = getAvailableRecipes();
    return sortRecipes(cookBook.getRecipeList().stream()
        .filter(r -> !sortedList.contains(r))
        .toList());
  }

  public void makeRecipe(Recipe r) {
    if (getRest().contains(r)) {
      throw new IllegalArgumentException("The fridge does not contain any groceries from the "
          + "recipe.");
    }
    r.getRecipes().forEach(g -> {
      final double[] amount = {g.getQuantity()};
      SI unit = g.getUnit();
      if (fridge.getGroceryList().contains(g)) {
        List<Grocery> removableItems = fridge.getGroceryList().stream()
            .filter(grocery -> grocery.getName().equalsIgnoreCase(g.getName()))
            .sorted(Comparator.comparing(Grocery::getDate))
            .toList();
        removableItems.forEach(grocery -> {
          if (amount[0] > 0) {
            grocery.removeAmount(amount[0], unit);
            amount[0] -= grocery.getQuantity();
          }
        });
      }
    });
  }
}
