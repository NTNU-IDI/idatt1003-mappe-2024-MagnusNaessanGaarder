package edu.ntnu.idi.idatt.modules;

import java.util.Arrays;
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
  private static int advanceID = 1;
  @SuppressWarnings("checkstyle:AbbreviationAsWordInName")
  private final int recipeID;
  private final String name;
  private final String description;
  private final String[] directions;
  private final int portion;


  /**
   * <strong>Description:</strong><br>
   * A constructor for the abstract recipe class.<br>
   *
   * @param name        A String representing the name of the recipe.
   * @param description A String containing a description for the recipe.
   * @param directions  An Array of Strings containing instructions for creating the recipe.
   * @param portion     An integer represening the portions per person the ingredients
   *                    cover when you make the recipe.
   */
  protected AbstractRecipe(final String name,
                           final String description,
                           final String[] directions,
                           final int portion) {
    this.name = name;
    this.description = description;
    this.directions = directions;
    this.portion = portion;
    this.recipeID = advanceID();
  }

  /**
   * <strong>Description:</strong><br>
   * A method that resets the ID of the Recipe class. Used mostly for testing purposes.
   */
  @SuppressWarnings("checkstyle:AbbreviationAsWordInName")
  public static void resetID() {
    advanceID = 1;
  }

  /**
   * <strong>Description:</strong><br>
   * A method that advances the ID of the Recipe class.
   *
   * @return An integer representing the ID of the Recipe.
   */
  @SuppressWarnings("checkstyle:AbbreviationAsWordInName")
  private static int advanceID() {
    return advanceID++;
  }

  /**
   * <strong>Description:</strong><br>
   * A get-method used to get the ID of the Recipe.
   *
   * @return An integer representing the ID of the Recipe.
   */
  @SuppressWarnings("checkstyle:AbbreviationAsWordInName")
  public int getRecipeID() {
    return recipeID;
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
}
