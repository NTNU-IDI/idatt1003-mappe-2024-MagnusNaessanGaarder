package edu.ntnu.idi.idatt.manager;

import edu.ntnu.idi.idatt.modules.Fridge;
import edu.ntnu.idi.idatt.modules.Grocery;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

/**
 * <strong>Description</strong><br>
 * An immutable class managing the other functionalities of the {@link Fridge}
 * that does not need to be directly contained in the Fridge class.
 * Rather, this manager-class is a bridge between the
 * functionalities needed for the application and the Fridge class itself.
 */
public class FridgeManager {
  private final Fridge fridge;

  /**
   * <strong>Description:</strong><br>
   * A constructor instantizing the FridgeManager class and
   * initializing the datafields.<br>
   *
   * @param fridge An object of type {@link Fridge}.
   *               Initializing the datafield {@code fridge}.
   */
  public FridgeManager(Fridge fridge) {
    this.fridge = fridge;
  }

  /**
   * <strong>Description:</strong><br>
   * A get-method used to get an index from a given ID of a Grocery.<br>
   *
   * @param groceryID A defined constant integer representing the ID
   *                  of a Grocery.
   * @return An integer representing the index of an existing Grocery
   in the Fridge. If no grocery is found, -1 will be returned.
   */
  @SuppressWarnings("checkstyle:AbbreviationAsWordInName")
  public int getGroceryListIndex(final int groceryID) {
    return IntStream.range(0, fridge.getGroceryList().size())
        .filter(i -> fridge.getGroceryList().get(i).getGroceryID() == groceryID).findFirst()
        .orElse(-1);
  }

  /**
   * <strong>Description:</strong><br>
   * A get-method used to get a given list sorted by date (oldest -> newest).<br>
   *
   * @param list A defined constant object of type {@link List} containing
   *             objects of type {@link Grocery}.
   * @return An object of type {@link List} containing objects of type {@link Grocery}.
   */
  private List<Grocery> getListSortedDate(final List<Grocery> list) {
    if (list.isEmpty() || list.size() == 1) {
      return list;
    }
    return list.stream().sorted(Comparator.comparing(Grocery::getDate)).toList();
  }

  /**
   * <strong>Description:</strong><br>
   * A get-method used to collect all expired groceries from the Fridge.<br>
   *
   * @return An object of type {@link List} containing objects of type {@link Grocery}.
   */
  public List<Grocery> getExpiredList() {
    if (fridge.getGroceryList().isEmpty()) {
      return fridge.getGroceryList();
    }
    return this.getListSortedDate(
        fridge.getGroceryList().stream().filter(Grocery::hasExpired).toList());
  }

  /**
   * <strong>Description:</strong><br>
   * A get-method used to collect all nearly expiring groceries from the Fridge.<br>
   *
   * @return An object of type {@link List} containing objects of type {@link Grocery}.
   */
  public List<Grocery> getNearExpList() {
    return this.getListSortedDate(fridge.getGroceryList().stream().filter(
        g -> g.getDate().isAfter(LocalDate.now().minusDays(1))
            && g.getDate().isBefore(LocalDate.now().plusDays(4))).toList());
  }

  /**
   * <strong>Description:</strong><br>
   * A get-method used to collect all groceries that has a longer expiration
   date from the Fridge.<br>
   *
   * @return An object of type {@link List} containing objects of type {@link Grocery}.
   */
  public List<Grocery> getRestGroceryList() {
    return this.getListSortedDate(fridge.getGroceryList().stream()
        .filter(g -> g.getDate().isAfter(LocalDate.now().plusDays(3))).toList());
  }

  /**
   * <strong>Description:</strong><br>
   * A get-method used to collect all groceries expiring before a given date
   * from the Fridge.<br>
   *
   * @param date A date as a LocalDate object.
   * @return An object of type {@link List} containing objects of type {@link Grocery}.
   */
  public List<Grocery> getDatesBefore(LocalDate date) {
    return this.getListSortedDate(
        fridge.getGroceryList().stream().filter(g -> g.getDate().isBefore(date)).toList());
  }

  /**
   * <strong>Description:</strong><br>
   * A get-method used to calculate the total moneyloss on expired Groceries
   * from the Fridge.<br>
   *
   * @return A String displaying how many groceries has expired
   and the total money loss on those groceries.
   */
  public String getMoneyLossStr() {
    List<Grocery> expiredGroceries = getExpiredList();
    if (expiredGroceries.isEmpty()) {
      return "Ingen varer er gått ut på dato";
    }
    String str = "          %d varer er gått ut på dato.\n";
    str += "          Du har tapt %.2f kr.";

    return String.format(str, expiredGroceries.size(), this.getMoneyLoss());
  }

  /**
   * <strong>Description:</strong><br>
   * A get-method used to calculate the money loss on all expired groceries
   * in the Fridge.<br>
   *
   * @return A double floating point value with the sum of all the groceries
   price per quaantity.
   */
  public double getMoneyLoss() {
    List<Grocery> expiredGroceries = getExpiredList();

    double sum = 0;
    for (Grocery g : expiredGroceries) {
      final GroceryManager gm = new GroceryManager(g);
      sum += gm.getPricePerQuantity();
    }

    return sum;
  }

  /**
   * <strong>Description:</strong><br>
   * A get-method used to calculate the money spent on all the groceries
   * in the Fridge.<br>
   *
   * @return A double floating point value with the sum of all the groceries
   price per quaantity.
   */
  public double getTotalPrice() {
    double sum = 0;
    for (Grocery g : fridge.getGroceryList()) {
      final GroceryManager gm = new GroceryManager(g);
      sum += gm.getPricePerQuantity();
      //Debugging
      //System.out.println("Sum: " + sum);
    }
    return sum;
  }

  /**
   * <strong>Description:</strong><br>
   * A method for searching through a grocery list after groceries with a specified name.<br>
   *
   * @param name A {@link String} containing the name of the grocery being searched for.
   * @return A {@link List} of Groceries with the specified name.
   */
  public List<Grocery> getByName(final String name) {
    return this.getListSortedDate(fridge.getGroceryList().stream()
        .filter(g -> g.getName().equalsIgnoreCase(name))
        .toList());
  }
}
