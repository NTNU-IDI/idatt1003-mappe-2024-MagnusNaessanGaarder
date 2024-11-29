package edu.ntnu.idi.idatt.modules;

import java.util.ArrayList;
import java.util.List;

public class CookBook {
  private final ArrayList<Recipe> recipeList;

  public CookBook() {
    this.recipeList = new ArrayList<>();
  }

  public List<Recipe> getRecipeList() throws IllegalArgumentException {
    return new ArrayList<>(recipeList).stream().toList();
  }

  public void addRecipe(Recipe recipe) {
    if (!recipeList.contains(recipe)) {
      recipeList.add(recipe);
    } else {
      throw new IllegalArgumentException("Duplicate recipe. "
          + "Please add a recipe that is not already added");
    }
  }

  public void removeRecipe(Recipe recipe) throws IllegalArgumentException {
    if (recipeList.contains(recipe)) {
      recipeList.remove(recipe);
    } else {
      throw new IllegalArgumentException("Cannot remove recipe because there is no such recipe"
          + " in the Cook book.");
    }
  }
}
