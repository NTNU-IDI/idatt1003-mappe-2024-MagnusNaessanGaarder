package edu.ntnu.idi.idatt.modules;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * <strong>Description:</strong><br>
 * A class representing a recipe. This class is a subclass of {@link AbstractRecipe}.
 */
public class Recipe extends AbstractRecipe {
  private final HashSet<Grocery> recipes;

  /**
   * <strong>Description:</strong><br>
   * A constructor for the Recipe class.<br>
   *
   * @param name        A String representing the name of the recipe.
   * @param description A String containing a description for the recipe.
   * @param directions  An Array of Strings containing instructions for creating the recipe.
   * @param portion     An integer represening the portions per person the ingredients
   *                    cover when you make the recipe.
   * @param recipes     A List containing Groceries required to make the recipe.
   */
  public Recipe(String name,
                String description,
                String[] directions,
                int portion,
                List<Grocery> recipes) {
    super(name, description, directions, portion);
    this.recipes = new HashSet<>(recipes);
  }

  /**
   * <strong>Description:</strong><br>
   * A get-method for the groceries required to make the recipe.<br>
   *
   * @return a List containing Groceries required to make the recipe.
   */
  public List<Grocery> getRecipes() {
    return new ArrayList<>(recipes).stream().toList();
  }
}
