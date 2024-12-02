package edu.ntnu.idi.idatt.modules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractRecipe {
  private final String name;
  private final String description;
  private final String[] directions;
  private final int portion;
  private final HashSet<Grocery> recipes;

  public AbstractRecipe(String name,
                        String description,
                        String[] directions,
                        int portion,
                        List<Grocery> recipes) {
    this.name = name;
    this.description = description;
    this.directions = directions;
    this.portion = portion;
    this.recipes = new HashSet<>(recipes);
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public String[] getDirections() {
    String[] deepCopy = new String[directions.length];
    AtomicInteger i = new AtomicInteger();
    Arrays.asList(directions).forEach(d -> deepCopy[i.getAndIncrement()] = d);
    return deepCopy;
  }

  public int getPortion() {
    return portion;
  }

  public List<Grocery> getRecipes() {
    return new ArrayList<>(recipes).stream().toList();
  }
}
