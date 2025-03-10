package edu.ntnu.idi.idatt.utils;

import edu.ntnu.idi.idatt.modules.Grocery;
import edu.ntnu.idi.idatt.modules.Recipe;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 *<strong>Description:</strong><br>
 * An abstract class creating tables of strings to display content
 for the application.
 */
public abstract class AbstractTable {
  /**
   * <strong>Description:</strong><br>
   * A static method creating whitespace for a title
   in a menu table.<br>
   *
   * @param length The length of the menu table as an integer.
   * @param wordLength The length of the title for the
   *                   menu table as an integer.
   * @return A String containing whitespace adequate to center
   the title for the menu table.
   */
  private static String createWhitespaceTitle(int length, int wordLength) {
    int wsLength = length / 2 - wordLength / 2;
    return " ".repeat(Math.max(0, wsLength));
  }

  /**
   * <strong>Description:</strong><br>
   * A static method for creating a table for a menu.<br>
   *
   * @param title A String containing the title of the menu table.
   * @param subTitle A String containing the subtitle of the
   *                 menu table.
   * @return A String containing the menu table.
   */
  public static String createMenuTable(String title, String subTitle) {
    String longBar = """
        
        
        --------------------------------------------\
        ----------------------------------------------------""";
    return longBar + "\n\n"
        + createWhitespaceTitle(longBar.length(), title.length())
        + title
        + longBar + "\n"
        + createWhitespaceTitle(12 * 2, 0)
        + subTitle + "\n\n";
  }

  /**
   * <strong>Description:</strong><br>
   * A static method for displaying a table titles for a menu.<br>
   *
   * @param title A string containing the main title of the table
   * @param width An integer representing the width of the table.
   * @return A string of the title and row titles of the table.
   */
  private static String tableTitle(String title, int width, String bottomBar) {
    StringBuilder sb = new StringBuilder();
    String longBar = "-".repeat(width / 2 - (title.length() / 2));

    sb.append("\n").append(longBar);
    sb.append(title);
    while (sb.length() <= bottomBar.length()) {
      sb.append("-");
    }
    sb.append("\n");
    return sb.toString();
  }

  /**
   * <strong>Description:</strong><br>
   * A static method for creating a table.<br>
   *
   * @param title       A String containing the title of the table.
   * @param rowTitleArr An array of type String containing the titles of the table.
   * @param rowDataArr  An array of type String containing the data of the table.
   * @return A String containing the table.
   */
  public static String createTableStatic(String title,
                                         String[] rowTitleArr,
                                         String[][] rowDataArr,
                                         final int width) {
    StringBuilder sb = new StringBuilder();
    String bottomBar = "-".repeat(width);
    sb.append(tableTitle(title, width, bottomBar));
    sb.append(getTableData(rowTitleArr, rowDataArr, bottomBar));
    sb.append(bottomBar).append("\n\n");

    return sb.toString();
  }

  /**
   * <strong>Description:</strong><br>
   * A static method for creating a table.<br>
   *
   * @param title       A String containing the title of the table.
   * @param rowTitleArr An array of type String containing the titles of the table.
   * @param rowDataArr  An array of type String containing the data of the table.
   * @return A String containing the table.
   */
  public static String createGroceryTableStatic(String title,
                                                String[] rowTitleArr,
                                                String[] rowDataArr,
                                                final int width) {
    StringBuilder sb = new StringBuilder();
    String bottomBar = "-".repeat(width);
    sb.append(tableTitle(title, width, bottomBar));
    sb.append(getGroceryTableData(rowTitleArr, rowDataArr, bottomBar));
    sb.append(bottomBar).append("\n\n");

    return sb.toString();
  }

  /**
   *<strong>Description:</strong><br>
   * A static method for centering a String with equal
   whitespace on left and right.<br>
   *
   * @param s The String being centered by whitespace.
   * @param length The total width the string will be centered
   *             in as an integer.
   * @return A String centered by whitespace.
   */
  private static String center(String s, int length) {
    if (s == null || length <= s.length()) {
      return s;
    }

    StringBuilder sb = new StringBuilder(length);
    sb.append(" ".repeat((length - s.length()) / 2));
    sb.append(s);
    while (sb.length() < length) {
      sb.append(" ");
    }
    return sb.toString();
  }

  /**
   * <strong>Description:</strong><br>
   * A static method for creating the contents and structure of a table.<br>
   *
   * @param rowTitleArr An array of type String containing the titles of the table.
   * @param rowDataArr  An array of type String containing the data of the table.
   * @param bottomBar   A String containing a "-"-bar with the lenght of the table.
   * @return A String containing the structured table data.
   */
  private static String getTableData(String[] rowTitleArr,
                                     String[][] rowDataArr,
                                     String bottomBar) {
    StringBuilder tableData = new StringBuilder();
    final int width = bottomBar.length() / rowTitleArr.length;

    tableData.append("|");
    int[] i = {0};

    Arrays.asList(rowTitleArr).forEach(r -> {
      if (i[0] == 0) {
        tableData.append(center(r, width - 2));
        i[0]++;
      } else {
        tableData.append(center(r, width - 1));
      }
      tableData.append("|");
    });

    i[0] = 0;
    tableData.append("\n").append(bottomBar).append("\n");
    Arrays.asList(rowDataArr).forEach(r -> {
      tableData.append("|");
      Arrays.asList(r).forEach(rr -> {
        if (i[0] == 0) {
          tableData.append(center(rr, width - 2));
          i[0]++;
        } else {
          tableData.append(center(rr, width - 1));
        }
        tableData.append("|");
      });
      tableData.append("\n");
    });

    return tableData.toString();
  }

  /**
   * <strong>Description:</strong><br>
   * Creating the structure and content of a unique table for
   displaying a list of Groceries based on date.
   *
   * @param lengths An array of integers containing the
   *                lengths of column sections.
   * @param list A List of Groceries being displayed in a table.
   * @return A string of the structured table of the groceries.
   */
  private String getTableData(int[] lengths, List<Grocery> list) {
    StringBuilder tableData = new StringBuilder();
    for (Grocery g : list) {
      String idStr = center(g.getGroceryID() + "", lengths[0]);
      String nameStr = center(g.getName(), lengths[1]);
      String quantityStr = center(g.getQuantity() + " " + g.getUnit().getAbrev(), lengths[2]);
      String priceStr = center(g.getPriceToStr(), lengths[3]);
      String bestBeforeStr = center(g.getDateToStr(), lengths[4]);

      String str = "|%s|%s|%s|%s|%s|";
      String rowData = String.format(
          str,
          idStr,
          nameStr,
          quantityStr,
          priceStr,
          bestBeforeStr
      );
      tableData.append(rowData).append("\n");
    }
    return tableData.toString();
  }

  /**
   * <strong>Description:</strong><br>
   * A static method for creating the contents and structure of a table.<br>
   *
   * @param rowTitleArr An array of type String containing the titles of the table.
   * @param rowDataArr  An array of type String containing the data of the table.
   * @param bottomBar   A String containing a "-"-bar with the lenght of the table.
   * @return A String containing the structured table data.
   */
  private static String getGroceryTableData(String[] rowTitleArr,
                                     String[] rowDataArr,
                                     String bottomBar) {
    StringBuilder tableData = new StringBuilder();
    final int width = bottomBar.length() / rowTitleArr.length;

    tableData.append("|");
    Arrays.asList(rowTitleArr).forEach(r -> tableData.append(center(r, width - 1)).append("|"));
    tableData.append("\n").append(bottomBar).append("\n|");
    Arrays.asList(rowDataArr).forEach(r -> tableData.append(center(r, width - 1)).append("|"));
    tableData.append("\n");

    return tableData.toString();
  }

  /**
   *<strong>Description:</strong><br>
   * A non-static method for creating a table.<br>
   *
   * @param title A String containing the title of the table.
   * @param rowTitleArr An array of type String containing the titles of the table.
   * @param rowDataArr An array of type String containing the data of the table.
   * @return A String containing the structured table data.
   */
  public String createTable(String title, String[] rowTitleArr, String[][] rowDataArr, int width) {
    return createTableStatic(title, rowTitleArr, rowDataArr, width);
  }

  /**
   *<strong>Description:</strong><br>
   * A non-static method for creating a table.<br>
   *
   * @param title A String containing the title of the table.
   * @param colTitleArr An array of type String containing the titles of the table.
   * @param colDataArr An array of type String containing the data of the table.
   * @return A String containing the structured table data.
   */
  public String createGroceryTable(String title,
                                   String[] colTitleArr,
                                   String[] colDataArr,
                                   int width) {
    return createGroceryTableStatic(title, colTitleArr, colDataArr, width);
  }

  /**
   * <strong>Description:</strong><br>
   * Creating a unique table for displaying a list of Groceries
   based on date.
   *
   * @param title A string containing the title of the table.
   * @param list A List of Groceries being displayed in a table.
   * @return A string of the table of the groceries.
   */
  public String createDateTable(String title, List<Grocery> list) {
    StringBuilder sb = new StringBuilder();
    String longBar = "-".repeat(86 / 2 - title.length() / 2);
    String bottomBar = "-".repeat(86);

    //Title of table
    sb.append("\n").append(longBar);
    sb.append(title);
    while (sb.length() <= bottomBar.length()) {
      sb.append("-");
    }
    sb.append("\n");

    //| ID | name | quantity unit | price / unit | Best-before |
    String idStr = center("ID", Math.round((float) (86 * 5) / 100) - 2);
    String nameStr = center("Navn", Math.round((float) (86 * 20) / 100) - 1);
    String quantityStr = center("Mengde", Math.round((float) (86 * 20) / 100) - 1);
    String priceStr = center("Pris", Math.round((float) (86 * 25) / 100) - 1);
    String bestBeforeStr = center("Best-før", Math.round((float) (86 * 30) / 100) - 1);

    int[] lengths = new int[] {
        idStr.length(),
        nameStr.length(),
        quantityStr.length(),
        priceStr.length(),
        bestBeforeStr.length()
    };

    //formatering av streng
    String str = "|%s|%s|%s|%s|%s|";
    sb.append(String.format(
        str,
        idStr,
        nameStr,
        quantityStr,
        priceStr,
        bestBeforeStr
    ));
    sb.append("\n").append(bottomBar).append("\n");

    sb.append(getTableData(lengths, list));
    sb.append(bottomBar).append("\n\n");

    return sb.toString();
  }

  /**
   * <strong>Description:</strong><br>
   * Creating a String of contents from a Recipe.<br>
   *
   * @param recipe A given object of type {@link Recipe} in which contents of the object is
   *               going to be displayed.
   * @return A {@code String} of the content in the recipe.
   */
  public String createRecipeContent(Recipe recipe) {
    StringBuilder sb = new StringBuilder();

    //ingredienser
    sb.append("------------Ingredienser------------\n\n");
    recipe.getRecipes().forEach(g -> sb.append("   - ")
        .append(g.getName()).append(", ").append(g.getQuantity()).append(" ")
        .append(g.getUnit().getAbrev()).append("\n"));
    //porsjoner
    sb.append("\n").append(" Porsjoner: ").append(recipe.getPortion()).append("\n\n");
    //instruksjoner
    sb.append("\n Slik gjør du:\n");
    AtomicReference<Integer> i = new AtomicReference<>(1);
    Arrays.asList(recipe.getDirections()).forEach(d -> sb.append("  (")
        .append(i.getAndSet(i.get() + 1)).append(")")
        .append(" ").append(d).append(".").append("\n"));

    sb.append("\n");

    return sb.toString();
  }
}
