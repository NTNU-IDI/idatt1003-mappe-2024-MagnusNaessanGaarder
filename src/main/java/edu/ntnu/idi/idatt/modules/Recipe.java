package edu.ntnu.idi.idatt.modules;

import java.util.Arrays;
import java.util.List;

/**
 * <strong>Description:</strong><br>
 * A class representing a recipe. This class is a subclass of {@link AbstractRecipe}.
 */
public class Recipe extends AbstractRecipe {
  private static int advanceID = 1;
  @SuppressWarnings("checkstyle:AbbreviationAsWordInName")
  private final int recipeID;

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
   * @param fridge      An object of type Fridge.
   */
  public Recipe(String name,
                String description,
                String[] directions,
                int portion,
                List<Grocery> recipes,
                Fridge fridge) {
    super(name, description, directions, portion, recipes, fridge);
    this.recipeID = advanceID();
  }

  /**
   * <strong>Description:</strong><br>
   * A method that resets the ID of the Recipe class. Used mostly for testing purposes.
   */
  @SuppressWarnings("checkstyle:AbbreviationAsWordInName")
  public static void resetID() {
    advanceID = 1;
  }

  /**
   * <strong>Description:</strong><br>
   * A method that advances the ID of the Recipe class.
   *
   * @return An integer representing the ID of the Recipe.
   */
  @SuppressWarnings("checkstyle:AbbreviationAsWordInName")
  private static int advanceID() {
    return advanceID++;
  }

  /**
   * <strong>Description:</strong><br>
   * A get-method used to get the ID of the Recipe.
   *
   * @return An integer representing the ID of the Recipe.
   */
  @SuppressWarnings("checkstyle:AbbreviationAsWordInName")
  public int getRecipeID() {
    return recipeID;
  }

  /**
   * <strong>Description:</strong><br>
   * An override of a method that checks if two Recipe objects are equal. The method checks if the
   * objects are of the same class, if the name, description, directions, portion and groceries are
   * equal.
   *
   * @param o An object of type Object.
   * @return A boolean value. True if the objects have equal content, false if they do not.
   */
  @Override
  public boolean equals(final Object o) {
    try {
      Recipe r = (Recipe) o;
      Class<?> c = o.getClass();
      return c == Recipe.class && r.getName().equalsIgnoreCase(super.getName())
          && r.getDescription().equalsIgnoreCase(super.getDescription())
          && Arrays.equals(r.getDirections(), super.getDirections())
          && r.getPortion() == super.getPortion()
          && getFridge().getGroceryList().stream()
              .noneMatch(g -> super.getRecipes().stream().anyMatch(grocery ->
                  grocery.getName().equals(g.getName())
                  && grocery.getUnit().equals(g.getUnit())
                  && grocery.getQuantity() == g.getQuantity()
              ))
          && r.getFridge().equals(this.getFridge());

    } catch (NullPointerException | ClassCastException e) {
      return false;
    }
  }
}
