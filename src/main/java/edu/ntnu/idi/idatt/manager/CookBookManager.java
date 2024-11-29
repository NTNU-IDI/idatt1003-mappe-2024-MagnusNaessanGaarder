package edu.ntnu.idi.idatt.manager;

import edu.ntnu.idi.idatt.modules.CookBook;
import edu.ntnu.idi.idatt.modules.Fridge;
import edu.ntnu.idi.idatt.modules.Recipe;
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

  public List<Recipe> getRecommendedRecipes() {
    if (cookBook.getRecipeList().isEmpty()) {
      return cookBook.getRecipeList();
    } else {
      List<Recipe> sortedList = cookBook.getRecipeList().stream()
          .sorted(Comparator.comparing(
              r -> r.matchingGroceries(fridge.getGroceryList())
          ))
          .toList();
      if (sortedList.size() < 3) {
        return sortedList.subList(0, sortedList.size() - 1);
      } else {
        return sortedList.subList(0, 3);
      }
    }
  }

  public List<Recipe> getRest() {
    if (cookBook.getRecipeList().isEmpty()) {
      return cookBook.getRecipeList();
    } else {
      final List<Recipe> sortedList = getRecommendedRecipes();
      return cookBook.getRecipeList().stream()
          .filter(r -> !sortedList.contains(r))
          .toList();
    }
  }
}
