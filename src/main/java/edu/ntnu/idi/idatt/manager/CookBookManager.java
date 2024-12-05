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
    Recipe recipe = cookBook.getRecipeList().stream()
        .filter(r -> r.getRecipeID() == id)
        .findFirst()
        .orElse(null);
    if (id < 0) {
      throw new IllegalArgumentException("ID cannot be negative.");
    }
    if (recipe == null) {
      throw new ArrayIndexOutOfBoundsException("The given ID is out of range of the recipes "
          + "in the cook book.");
    }
    return recipe;
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
        : getAvailableRecipes().subList(0, 1);
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
    if (!cookBook.getRecipeList().contains(r)) {
      throw new IllegalArgumentException("The recipe cannot be found in the cook book. ");
    }
    final List<Grocery> correspondingGroceries = fridge.getGroceryList().stream()
        .filter(grocery -> r.getRecipes().stream()
            .map(Grocery::getName)
            .distinct()
            .anyMatch(name -> grocery.getName().equalsIgnoreCase(name))
        ).toList();

    if (!correspondingGroceries.isEmpty()) {
      r.getRecipes().forEach(g -> {
        final double[] amount = {g.getQuantity()};
        final SI unit = g.getUnit();

        List<Grocery> removableGroceries = correspondingGroceries.stream()
            .filter(gr -> gr.getName().equalsIgnoreCase(g.getName()))
            .toList();

        if (!removableGroceries.isEmpty()) {
          removableGroceries.stream()
              .sorted(Comparator.comparing(Grocery::getDate))
              .toList()
              .forEach(grocery -> {
                if (amount[0] > 0) {
                  grocery.removeAmount(amount[0], unit, fridge);
                  amount[0] -= grocery.getQuantity();
                }
              });
        }
      });
    }

  }
}
