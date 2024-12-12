package edu.ntnu.idi.idatt.utils;

import edu.ntnu.idi.idatt.modules.FridgeManager;
import edu.ntnu.idi.idatt.modules.Grocery;
import edu.ntnu.idi.idatt.modules.GroceryManager;
import edu.ntnu.idi.idatt.modules.Recipe;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <strong>Inheritance:</strong><br>
 * This class inherits from the abstract class {@link AbstractTable}.<br><br>
 *
 * <strong>Description:</strong><br>
 * A class with responsiblity for displaying Groceries and lists of Groceries in tables.<br><br>
 *
 * <strong>Datafields</strong>
 * {@code START} - A constant static String surrounding alternative text if a
 * Fridge is empty. Displayed before the string.<br>
 * {@code END} - A constant static String surrounding alternative text if a
 * Fridge is empty. Displayed after the string.<br>
 * {@code fm} - A Fridge Manager-object to get necessary content for some displays.<br>
 */
public class Display extends AbstractTable {
  private static final String START = "\n            ---- ";
  private static final String END = " ----\n\n";
  private final FridgeManager fm;

  /**
   * <strong>Description:</strong><br>
   * A constructor instantizing the class and initializing datafields.<br>
   *
   * @param fm An object of type {@link FridgeManager}.
   */
  public Display(final FridgeManager fm) {
    this.fm = fm;
  }

  /**
   * <strong>Description:</strong><br>
   * A constructor instantizing the class and initializing datafields.<br>
   */
  public Display() {
    this.fm = null;
  }

  /**
   * <strong>Description:</strong><br>
   * A method for displaying a menu list.<br>
   *
   * @param list    A given List of Groceries.
   * @param altText - A String containing an alternative text.
   * @return A string with a table og Groceries or an alt-text.
   */
  public static String menuList(List<Grocery> list, String altText) {
    StringBuilder str = new StringBuilder();
    if (list.isEmpty()) {
      str.append(START).append(altText).append(END);
    } else {
      str.append(displayMenuList(list));
    }
    return str.toString();
  }

  /**
   * <strong>Description:</strong><br>
   * A method displaying and structuring a menu list.
   *
   * @param list A List of Groceries.
   * @return A String containing the structured menu list.
   */
  private static String displayMenuList(List<Grocery> list) {
    StringBuilder sb = new StringBuilder();
    for (Grocery grocery : list) {
      sb.append("         ")
          .append(grocery.getName())
          .append(", ")
          .append(grocery.getQuantity()).append(" ").append(grocery.getUnit().getAbrev())
          .append(", ")
          .append(grocery.getPriceToStr()).append(", ")
          .append(grocery.getDateToStr())
          .append("\n");
    }
    sb.append("\n\n");
    return sb.toString();
  }

  /**
   * <strong>Description:</strong><br>
   * A method for displaying a menu list.<br>
   *
   * @param list A given List of Groceries.
   * @return A string with a table og Groceries or an alt-text.
   */
  public static String displayList(List<Grocery> list, int width) {
    StringBuilder sb = new StringBuilder();

    for (Grocery grocery : list) {
      String table = createTableStatic(grocery.getName(),
          new String[] {"VareID", "Mengde", "Pris", "Dato"},
          new String[][] {
              {
                  grocery.getGroceryID() + "",
                  grocery.getQuantity() + " " + grocery.getUnit().getAbrev(),
                  grocery.getPriceToStr(),
                  grocery.getDateToStr()
              }
          },
          width
      );
      sb.append(table);
    }
    return sb.toString();
  }

  /**
   * <strong>Description:</strong><br>
   * A static method for displaying a list of searched Groceries
   * from the Fridge.<br>
   *
   * @param list A List of Groceries containing searched Groceries.
   * @return A String containing the searched groceries.
   */
  public static String displaySearchList(List<Grocery> list) {
    StringBuilder sb = new StringBuilder();
    list.forEach(g -> sb.append("         ")
        .append(g.getName()).append(", ")
        .append(g.getDateToStr()).append(",    ")
        .append("vareID: ").append(g.getGroceryID())
        .append("\n")
    );

    sb.append("\n\n");
    return sb.toString();
  }

  /**
   * <strong>Description:</strong><br>
   * A method displaying the total price of a given List of Groceries.<br>
   *
   * @param list      A List of Groceries to display.
   * @param title     A String with the title of the table.
   * @param rowTitle1 A String with a title for a column.
   * @param rowTitle2 A String with a title for another column.
   * @param width     An integer representing the width of the table.
   * @return A String displaying the given List of Groceries and the
   amount of money used on them.
   */
  public String displayPrice(List<Grocery> list,
                             String title,
                             String rowTitle1,
                             String rowTitle2,
                             int width) {
    StringBuilder str = new StringBuilder();
    if (list.isEmpty()) {
      str.append(START).append("Ingen varer er lagt til i kjøleskap").append(END);
      str.append("         Legg til varer for å se total prissum\n\n");
    } else {
      str.append(buildTable(list, title, rowTitle1, rowTitle2, width));

      assert fm != null;
      str.append("            Total brukt på varer: ").append(fm.getTotalPrice()).append(" kr\n\n");
    }
    return str.toString();
  }

  /**
   * <strong>Description:</strong><br>
   * A method structuring and creating a table for the total price of a
   * given List of Groceries.<br>
   *
   * @param list      A List of Groceries to display.
   * @param title     A String with the title of the table.
   * @param rowTitle1 A String with a title for a column.
   * @param rowTitle2 A String with a title for another column.
   * @param width     An integer representing the width of the table.
   * @return A String displaying the given List of Groceries and the
   amount of money used on them.
   */
  private String buildTable(List<Grocery> list,
                            String title,
                            String rowTitle1,
                            String rowTitle2,
                            int width) {

    String[] rowTitleArr = new String[] {rowTitle1, rowTitle2};
    String[][] rowDataArr = new String[list.size()][];

    AtomicInteger i = new AtomicInteger();
    list.forEach(g -> {
      final GroceryManager gm = new GroceryManager(g);
      String[] rdArr = new String[2];
      rdArr[0] = g.getName();
      rdArr[1] = gm.getPricePerQuantity() + " kr/" + g.getUnit().getUnitForPrice();
      rowDataArr[i.getAndIncrement()] = rdArr;
    });

    return createTable(title, rowTitleArr, rowDataArr, width);
  }

  /**
   * <strong>Description:</strong><br>
   * A method displaying the total amount of money lost on expired Groceries.<br>
   *
   * @param list      A List of Groceries to display.
   * @param title     A String with the title of the table.
   * @param rowTitle1 A String with a title for a column.
   * @param rowTitle2 A String with a title for another column.
   * @return A String displaying the given List of Groceries and the
   loss of money on expired Groceries.
   */
  public String displayPriceUnique(List<Grocery> list,
                                   String title,
                                   String rowTitle1,
                                   String rowTitle2) {
    StringBuilder str = new StringBuilder();
    if (list.isEmpty()) {
      str.append(START).append("Ingen varer er lagt til i kjøleskap").append(END);
    } else {
      str.append(buildTable(list, title, rowTitle1, rowTitle2, 70));

      assert fm != null;
      str.append("            Total pengetap på datovarer: ").append(fm.getMoneyLoss())
          .append(" kr\n\n");
    }
    return str.toString();
  }

  /**
   * <strong>Description:</strong><br>
   * A method displaying a given Grocery.<br>
   *
   * @param g     An object of type {@code Grocery}.
   * @param width An integer representing the width.
   * @return A String displaying the given Grocery.
   */
  public String grocery(Grocery g, int width) {
    StringBuilder str = new StringBuilder();
    String table = createGroceryTable(g.getName(),
        new String[] {"Mengde", "Pris", "Dato"},
        new String[] {g.getQuantity() + " " + g.getUnit().getAbrev(), g.getPriceToStr(),
            g.getDateToStr()},
        width
    );
    str.append(table);

    return str.toString();
  }

  /**
   * <strong>Description:</strong><br>
   * A method displaying a list of Groceries sorted by date.<br>
   *
   * @param title   A string representing the title of the table.
   * @param list    A List of Groceries.
   * @param altText A string displayed when the list is empty.
   * @return A String displaying the given Groceries.
   */
  public String dateList(String title, List<Grocery> list, String altText) {
    if (!list.isEmpty()) {
      return super.createDateTable(title, list);
    } else {
      return START + altText + END;
    }
  }

  /**
   * <strong>Description:</strong><br>
   * A method displaying a list of Recipes.<br>
   *
   * @param title   A string representing the title of the table.
   * @param list    A List of Recipes.
   * @param altText A string displayed when the list is empty.
   * @return A String displaying the given Recipies.
   */
  public String recipeMenuList(String title, List<Recipe> list, String altText) {
    StringBuilder sb = new StringBuilder();
    sb.append("            ").append(title).append("\n");
    if (list.isEmpty()) {
      sb.append(START).append(altText).append(END);
    } else {
      list.forEach(r -> {
        final String listStart = "   [%d] ";

        sb.append(String.format(listStart, r.getRecipeID()))
            .append(r.getName())
            .append(", ")
            .append(r.getDescription(), 0, Math.min(r.getDescription().length(), 40))
            .append("...");
        sb.append("\n");
      });
      sb.append("\n\n");
    }
    return sb.toString();
  }


  /**
   * <strong>Description:</strong><br>
   * A method displaying the contents of a Recipes.<br>
   *
   * @param recipe  An object of type Recipe containing Groceries.
   * @return A String displaying the given Recipies.
   */

  public String displayRecipe(Recipe recipe) {
    return AbstractTable.createMenuTable("OPPSKRIFT - " + recipe.getName().toUpperCase(),
        recipe.getDescription())
        + super.createRecipeContent(recipe);
  }
}
