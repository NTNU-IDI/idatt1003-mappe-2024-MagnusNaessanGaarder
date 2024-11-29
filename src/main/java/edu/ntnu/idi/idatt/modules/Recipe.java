package edu.ntnu.idi.idatt.modules;

import java.util.HashSet;
import java.util.List;

public class Recipe extends AbstractRecipe {
  Fridge fridge;
  public Recipe(String name,
                String description,
                String[] directions,
                int portion,
                HashSet<Grocery> recipes,
                Fridge fridge) {
    super(name, description, directions, portion, recipes);
    this.fridge = fridge;
  }

  public int matchingGroceries(List<Grocery> list) {
    return this.getRecipes().stream()
        .filter(list::contains)
        .toList()
        .size();
  }

  @Override
  public int getRecipeID() {
    return super.getRecipeID();
  }

  @Override
  public String getName() {
    return super.getName();
  }

  @Override
  public String getDescription() {
    return super.getDescription();
  }

  @Override
  public String[] getDirections() {
    return super.getDirections();
  }

  @Override
  public int getPortion() {
    return super.getPortion();
  }

  @Override
  public List<Grocery> getRecipes() {
    return super.getRecipes();
  }
}
