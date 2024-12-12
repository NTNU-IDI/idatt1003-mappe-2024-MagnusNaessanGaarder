package edu.ntnu.idi.idatt.modules;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <strong>Description:</strong><br>
 * A class representing a RecipeManager. This class is responsible for managing the recipe and the
 * fridge. It contains methods for checking matching groceries and calculating the average date of
 * the groceries in the fridge that matches the ingredients in the recipe.
 */
public class RecipeManager {
  private final Recipe recipe;
  private final Fridge fridge;

  /**
   * <strong>Description:</strong><br>
   * A constructor for the RecipeManager class.<br>
   *
   * @param recipe An object of type {@link Recipe}.
   *               Initializing the datafield {@code recipe}.
   * @param fridge An object of type {@link Fridge}.
   *               Initializing the datafield {@code fridge}.
   */
  public RecipeManager(final Recipe recipe, final Fridge fridge) {
    this.recipe = recipe;
    this.fridge = fridge;
  }

  /**
   * <strong>Description:</strong><br>
   * A method checking matching groceries in the fridge and the recipe relative to the total amount
   * of ingredients in the recipe.<br>
   *
   * @return a double representing the prosent factor of matching groceries relative to the total
  amount of ingredients.
   */
  public double matchingGroceries() {
    if (recipe.getRecipes().isEmpty()) {
      return 0;
    } else {
      return (double) recipe.getRecipes().stream()
          .filter(g -> fridge.getGroceryList().stream()
              .anyMatch(gr -> gr.getName().equalsIgnoreCase(g.getName())))
          .toList()
          .size() / recipe.getRecipes().size();
    }
  }

  /**
   * <strong>Description:</strong><br>
   * A method calculating the average date of the groceries in the fridge that matches the
   * ingredients in the recipe.
   *
   * @return a LocalDate representing the average date of the groceries in the fridge that
  matches the ingredients in the recipe.
   */
  public LocalDate avrageDate() {
    AtomicInteger avrDay = new AtomicInteger();
    AtomicInteger avrMonth = new AtomicInteger();
    AtomicInteger avrYear = new AtomicInteger();

    AtomicInteger correspondingGroceries = new AtomicInteger();
    recipe.getRecipes().forEach(g -> {
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
      return c == Recipe.class && r.getName().equalsIgnoreCase(recipe.getName())
          && r.getDescription().equalsIgnoreCase(recipe.getDescription())
          && Arrays.equals(recipe.getDirections(), recipe.getDirections())
          && r.getPortion() == recipe.getPortion()
          && fridge.getGroceryList().stream()
          .noneMatch(g -> recipe.getRecipes().stream().anyMatch(grocery ->
              grocery.getName().equals(g.getName())
                  && grocery.getUnit().equals(g.getUnit())
                  && grocery.getQuantity() == g.getQuantity()
          ));

    } catch (NullPointerException | ClassCastException e) {
      return false;
    }
  }
}
