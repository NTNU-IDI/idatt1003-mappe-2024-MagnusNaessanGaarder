package edu.ntnu.idi.idatt.manager;

import edu.ntnu.idi.idatt.modules.CookBook;
import edu.ntnu.idi.idatt.modules.Fridge;
import edu.ntnu.idi.idatt.modules.Grocery;
import edu.ntnu.idi.idatt.modules.Recipe;
import edu.ntnu.idi.idatt.modules.SI;
import java.util.Comparator;
import java.util.List;

/**
 * <strong>Description:</strong><br>
 * A class representing a CookBookManager. This class is responsible for managing the cook book
 * and the fridge. It contains methods for getting recipes, sorting recipes, getting available
 * recipes, getting recommended recipes, getting the rest of the recipes and making a recipe.
 */
public class CookBookManager {
  private final CookBook cookBook;
  private final Fridge fridge;

  /**
   * <strong>Description:</strong><br>
   * A constructor for the CookBookManager class.<br>
   *
   * @param cookBook An object of type {@link CookBook}.
   *                 Initializing the datafield {@code cookBook}.
   * @param fridge   An object of type {@link Fridge}.
   *                 Initializing the datafield {@code fridge}.
   */
  public CookBookManager(final CookBook cookBook, final Fridge fridge) {
    this.cookBook = cookBook;
    this.fridge = fridge;
  }

  /**
   * <strong>Description:</strong><br>
   * A get-method used to get a recipe from the cook book.<br>
   *
   * @param ID An integer representing the ID of a recipe.
   * @return An object of type {@link Recipe}.
   * @throws IllegalArgumentException If the ID is negative.
   * @throws ArrayIndexOutOfBoundsException If the ID is out of range of the recipes in the cook
   book.
   */
  @SuppressWarnings({"checkstyle:AbbreviationAsWordInName", "checkstyle:ParameterName"})
  public Recipe getRecipe(final int ID) {
    Recipe recipe = cookBook.getRecipeList().stream()
        .filter(r -> r.getRecipeID() == ID)
        .findFirst()
        .orElse(null);
    if (ID < 0) {
      throw new IllegalArgumentException("ID cannot be negative.");
    }
    if (recipe == null) {
      throw new ArrayIndexOutOfBoundsException("The given ID is out of range of the recipes "
          + "in the cook book.");
    }
    return recipe;
  }

  /**
   * <strong>Description:</strong><br>
   * A method used to sort a list of recipes based on the matching groceries and the average date
   * of the matching groceries.<br>
   *
   * @param list A defined constant object of type {@link List} containing objects of type
   *             {@link Recipe}.
   * @return An object of type {@link List} containing objects of type {@link Recipe}.
   */
  private List<Recipe> sortRecipes(List<Recipe> list) {
    return list.stream()
        .sorted(
            Comparator.comparing(Recipe::matchingGroceries)
            .thenComparing(Recipe::avrageDate))
        .toList();
  }

  /**
   * <strong>Description:</strong><br>
   * A method used to get available recipes from the cook book. An available recipe in this case
   is defined as a recipe with 50% or more matching grocery.<br>
   *
   * @return An object of type {@link List} containing objects of type {@link Recipe}.
   */
  public List<Recipe> getAvailableRecipes() {
    return sortRecipes(cookBook.getRecipeList().stream()
        .filter(r -> r.matchingGroceries() >= 0.5)
        .toList());
  }

  /**
   * <strong>Description:</strong><br>
   * A method used to get recommended recipes from the cook book. The method returns the first
   * three available recipes if there are three or more available recipes. If there are less than
   * three available recipes, the method returns the first available recipe.<br>
   *
   * @return An object of type {@link List} containing objects of type {@link Recipe}.
   */
  public List<Recipe> getRecommendedRecipes() {
    return getAvailableRecipes().size() >= 3 ? getAvailableRecipes().subList(0, 3)
        : getAvailableRecipes().subList(0, 1);
  }

  /**
   * <strong>Description:</strong><br>
   * A method used to get the rest of the recipes from the cook book. The rest of the recipes are
   defined as the recipes that does not have 50% or more matching groceries available in the
   fridge.<br>
   *
   * @return An object of type {@link List} containing objects of type {@link Recipe}.
   */
  public List<Recipe> getRest() {
    final List<Recipe> sortedList = getAvailableRecipes();
    return sortRecipes(cookBook.getRecipeList().stream()
        .filter(r -> !sortedList.contains(r))
        .toList());
  }

  /**
   * <strong>Description:</strong><br>
   * A method used to make a recipe. The method removes the groceries from the fridge that are
   required to make the recipe.<br>
   *
   * @param r An object of type {@link Recipe}.
   * @throws IllegalArgumentException If the fridge does not contain any groceries from the recipe,
   or if the recipe cannot be found in the cook book.
   */
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
