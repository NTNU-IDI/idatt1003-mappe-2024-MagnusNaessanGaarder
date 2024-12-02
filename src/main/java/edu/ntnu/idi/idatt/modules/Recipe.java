package edu.ntnu.idi.idatt.modules;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Recipe extends AbstractRecipe {
  private final Fridge fridge;
  private static int advanceID = 1;
  @SuppressWarnings("checkstyle:AbbreviationAsWordInName")
  private final int recipeID;

  public Recipe(String name,
                String description,
                String[] directions,
                int portion,
                List<Grocery> recipes,
                Fridge fridge) {
    super(name, description, directions, portion, recipes);
    this.recipeID = advanceID();
    this.fridge = fridge;
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

  public double matchingGroceries() {
    if (getRecipes().isEmpty()) {
      return 0;
    } else {
      return (double) this.getRecipes().stream()
          .filter(g -> fridge.getGroceryList().stream()
              .anyMatch(gr -> gr.getName().equalsIgnoreCase(g.getName())))
          .toList()
          .size() / getRecipes().size();
    }
  }

  public LocalDate avrageDate() {
    AtomicInteger avrDay = new AtomicInteger();
    AtomicInteger avrMonth = new AtomicInteger();
    AtomicInteger avrYear = new AtomicInteger();

    AtomicInteger correspondingGroceries = new AtomicInteger();
    getRecipes().forEach(g -> {
      Grocery correspondingFridgeItem = fridge.getGroceryList().stream()
          .filter(gr -> gr.getName().equals(g.getName()))
          .min(Comparator.comparing(Grocery::getDate))
          .orElse(null);

      if (correspondingFridgeItem != null) {
        correspondingGroceries.getAndIncrement();
        int day = correspondingFridgeItem.getDate().getDayOfMonth();
        int month = correspondingFridgeItem.getDate().getMonthValue();
        int year = correspondingFridgeItem.getDate().getYear();
        avrDay.addAndGet(day);
        avrMonth.addAndGet(month);
        avrYear.addAndGet(year);
      }
    });

    if (correspondingGroceries.get() == 0) {
      return LocalDate.now().plusYears(100);
    } else {
      avrDay.updateAndGet(v -> v / correspondingGroceries.get());
      avrMonth.updateAndGet(v -> v / correspondingGroceries.get());
      avrYear.updateAndGet(v -> v / correspondingGroceries.get());

      return LocalDate.of(avrYear.get(), avrMonth.get(), avrDay.get());
    }
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
