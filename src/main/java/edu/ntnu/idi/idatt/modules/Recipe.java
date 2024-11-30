package edu.ntnu.idi.idatt.modules;

import java.util.Arrays;
import java.util.List;

public class Recipe extends AbstractRecipe {
  private final Fridge fridge;

  public Recipe(String name,
                String description,
                String[] directions,
                int portion,
                List<Grocery> recipes,
                Fridge fridge) {
    super(name, description, directions, portion, recipes);
    this.fridge = fridge;
  }

  @Override
  public boolean equals(final Object o) {
    try {
      Recipe r = (Recipe) o;
      Class<?> c = o.getClass();
      /*System.out.println(c == Recipe.class);
      System.out.println(r.getName().equalsIgnoreCase(this.getName()));
      System.out.println(r.getDescription().equalsIgnoreCase(this.getDescription()));
      System.out.println(Arrays.equals(r.getDirections(), this.getDirections()));
      System.out.println(r.getPortion() == this.getPortion());
      System.out.println(!getFridge().getGroceryList().stream()
          .filter(g -> this.getRecipes().stream()
          .anyMatch(grocery -> grocery.equals(g))
      ).toList().isEmpty());*/

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

  public Fridge getFridge() {
    return fridge;
  }
}
