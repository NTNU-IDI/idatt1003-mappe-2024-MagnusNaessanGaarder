package edu.ntnu.idi.idatt.modules;

import java.util.ArrayList;
import java.util.List;

/**
 * <strong>Description:</strong><br>
 * A class representing a Cook. This class collects Objects of type {@link Recipe}.<br>
 *
 * <strong>Datafield:</strong><br>
 * <ul>
 *   <li>{@code recipeList} - A ArrayList containing objects of type {@link Recipe}.</li>
 * </ul>
 */
public class CookBook {
  private final ArrayList<Recipe> recipeList;

  /**
   * <strong>Description:</strong><br>
   * Constructor for the CookBook class. Initializes the {@code recipeList} ArrayList.
   */
  public CookBook() {
    this.recipeList = new ArrayList<>();
  }

  /**
   * <strong>Description:</strong><br>
   * A method that returns a copy of the {@code recipeList} ArrayList.
   *
   * @return A copy of the {@code recipeList} ArrayList.
   */
  public List<Recipe> getRecipeList() {
    return new ArrayList<>(recipeList).stream().toList();
  }

  /**
   * <strong>Description:</strong><br>
   * A method that adds a {@link Recipe} object to the {@code recipeList} ArrayList.
   *
   * @param recipe A {@link Recipe} object.
   */
  public void addRecipe(final Recipe recipe) {
    boolean containsRecipe = recipeList.stream()
        .anyMatch(r -> r.equals(recipe));

    if (!containsRecipe) {
      recipeList.add(recipe);
    } else {
      throw new IllegalArgumentException("Duplicate recipe. "
          + "Please add a recipe that is not already added");
    }
  }

  /**
   * <strong>Description:</strong><br>
   * A method that removes a {@link Recipe} object from the {@code recipeList} ArrayList.
   *
   * @param recipe A {@link Recipe} object.
   * @throws IllegalArgumentException If the {@code recipe} object does not exist in the
   *                                  {@code recipeList} ArrayList.
   */
  public void removeRecipe(Recipe recipe) throws IllegalArgumentException {
    boolean containsRecipe = recipeList.stream()
        .anyMatch(r -> r.equals(recipe));

    if (containsRecipe) {
      recipeList.remove(recipe);
    } else {
      throw new IllegalArgumentException("Cannot remove recipe because there is no such recipe"
          + " in the Cook book.");
    }
  }
}
