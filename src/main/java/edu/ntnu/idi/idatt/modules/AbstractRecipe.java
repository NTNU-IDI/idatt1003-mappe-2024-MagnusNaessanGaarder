package edu.ntnu.idi.idatt.modules;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <strong>Description:</strong><br>
 * An abstract class representing a recipe. The usecase for this class is to create
 * layers of abstraction where this class is inherited by recipe class. In this process,
 * methods and variables are hided, simplifying the Recipe-class substantially.<br>
 *
 * <strong>Datafield</strong><br>
 * <ul>
 *   <li>{@code fridge} - An object of type {@link Fridge}.</li>
 *   <li>{@code name} - A String representing the name of the recipe.</li>
 *   <li>{@code description} - A String with the description of the recipe.</li>
 *   <li>{@code directions} - A String Array containing Strings with instructions
 *   for making the recipe.</li>
 *   <li>{@code portion} - An integer representing the portions the recipe is ment to cover.</li>
 *   <li>{@code recipes} - An HashSet containing multiple Groceries
 *   required to make the recipe.</li>
 * </ul>
 */
public abstract class AbstractRecipe {
  private final Fridge fridge;
  private final String name;
  private final String description;
  private final String[] directions;
  private final int portion;
  private final HashSet<Grocery> recipes;


  /**
   * <strong>Description:</strong><br>
   * A constructor for the abstract recipe class.<br>
   *
   * @param name        A String representing the name of the recipe.
   * @param description A String containing a description for the recipe.
   * @param directions  An Array of Strings containing instructions for creating the recipe.
   * @param portion     An integer represening the portions per person the ingredients
   *                    cover when you make the recipe.
   * @param recipes     A List containing Groceries required to make the recipe.
   * @param fridge      An object of type Fridge.
   */
  protected AbstractRecipe(final String name,
                           final String description,
                           final String[] directions,
                           final int portion,
                           final List<Grocery> recipes,
                           final Fridge fridge) {
    this.name = name;
    this.description = description;
    this.directions = directions;
    this.portion = portion;
    this.recipes = new HashSet<>(recipes);
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
    if (recipes.isEmpty()) {
      return 0;
    } else {
      return (double) recipes.stream()
          .filter(g -> fridge.getGroceryList().stream()
              .anyMatch(gr -> gr.getName().equalsIgnoreCase(g.getName())))
          .toList()
          .size() / recipes.size();
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

  /**
   * <strong>Description:</strong><br>
   * A get-method for the name of the recipe.<br>
   *
   * @return a String representing the name of the recipe.
   */
  public String getName() {
    return name;
  }

  /**
   * <strong>Description:</strong><br>
   * A get-method for the description of the recipe.<br>
   *
   * @return a String representing the description of the recipe.
   */
  public String getDescription() {
    return description;
  }

  /**
   * <strong>Description:</strong><br>
   * A get-method for the directions of the recipe.<br>
   *
   * @return a String Array containing the directions of the recipe.
   */
  public String[] getDirections() {
    String[] deepCopy = new String[directions.length];
    AtomicInteger i = new AtomicInteger();
    Arrays.asList(directions).forEach(d -> deepCopy[i.getAndIncrement()] = d);
    return deepCopy;
  }

  /**
   * <strong>Description:</strong><br>
   * A get-method for the portions the recipe is ment for.<br>
   *
   * @return an integer representing the portions for the recipe.
   */
  public int getPortion() {
    return portion;
  }

  /**
   * <strong>Description:</strong><br>
   * A get-method for the groceries required to make the recipe.<br>
   *
   * @return a List containing Groceries required to make the recipe.
   */
  public List<Grocery> getRecipes() {
    return new ArrayList<>(recipes).stream().toList();
  }

  /**
   * <strong>Description:</strong><br>
   * A get-method for the fridge.<br>
   *
   * @return an object of type Fridge.
   */
  public Fridge getFridge() {
    return fridge;
  }
}
