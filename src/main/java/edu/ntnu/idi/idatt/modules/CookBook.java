package edu.ntnu.idi.idatt.modules;

import java.util.ArrayList;
import java.util.List;

public class CookBook {
  private final ArrayList<Recipe> recipeList;

  public CookBook() {
    this.recipeList = new ArrayList<>();
  }

  public List<Recipe> getRecipeList() {
    return new ArrayList<>(recipeList).stream().toList();
  }

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
