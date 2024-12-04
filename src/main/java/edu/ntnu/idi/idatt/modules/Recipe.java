package edu.ntnu.idi.idatt.modules;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class Recipe extends AbstractRecipe {
  private static int advanceID = 1;
  @SuppressWarnings("checkstyle:AbbreviationAsWordInName")
  private final int recipeID;

  public Recipe(String name,
                String description,
                String[] directions,
                int portion,
                List<Grocery> recipes,
                Fridge fridge) {
    super(name, description, directions, portion, recipes, fridge);
    this.recipeID = advanceID();
  }

  @SuppressWarnings("checkstyle:AbbreviationAsWordInName")
  public static void resetID() {
    advanceID = 1;
  }

  @SuppressWarnings("checkstyle:AbbreviationAsWordInName")
  private static int advanceID() {
    return advanceID++;
  }

  @SuppressWarnings("checkstyle:AbbreviationAsWordInName")
  public int getRecipeID() {
    return recipeID;
  }

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

  @Override
  public double matchingGroceries() {
    return super.matchingGroceries();
  }

  @Override
  public LocalDate avrageDate() {
    return super.avrageDate();
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

  @Override
  public Fridge getFridge() {
    return super.getFridge();
  }
}
