package edu.ntnu.idi.idatt.manager;

import edu.ntnu.idi.idatt.modules.Grocery;
import edu.ntnu.idi.idatt.modules.SI;
import edu.ntnu.idi.idatt.utils.AbstractTerminalAction;

/**
 * <strong>Description</strong><br>
 * An immutable class managing the other functionalities of a given {@link Grocery}
 * that does not need to be directly contained in the Grocery class.
 * Rather, this manager class is a bridge between the
 * functionalities needed for the application and the Grocery class itself.
 */
public class GroceryManager extends AbstractTerminalAction {
  private final Grocery grocery;
  private final SI unit;

  /**
   * <strong>Description</strong><br>
   * A constructor instantizing the class and initializing
   * the datafields {@code grocery} and {@code unit}.<br>
   *
   * @param g An object of type {@link Grocery}
   */
  public GroceryManager(Grocery g) {
    this.grocery = g;
    this.unit = this.grocery.getUnit();
  }

  /**
   * <strong>Description</strong><br>
   * A get-method fetching an amount and a unit name based on a String userinput.<br>
   *
   * @param str A user input of type String.
   * @return An array of type String with the amount and unit name as Strings.
   * @throws Exception If formating or unexpected Exceptions occur.
   */
  public static String[] getAmountAndUnit(String str) throws Exception {
    //mengden og enheten av varen
    try {
      String[] res = str.split(" ");
      Double.parseDouble(res[0]);

      return res;
    } catch (NumberFormatException e) {
      throw new NumberFormatException(
          "NumberFormatExeption with following message: " + e.getMessage());
    } catch (Exception e) {
      throw new Exception("Something went wrong: " + e.getMessage());
    }
  }

  /**
   * <strong>Description</strong><br>
   * A get-method to get the value of a Grocery as price per quantity.<br>
   *
   * @return A double representing the price per quantity of the grocery being managed.
   */
  public double getPricePerQuantity() {
    double result;

    if (unit.getAbrev().equals(unit.getUnitForPrice())) {
      result = grocery.getQuantity() * grocery.getPrice();
    } else if (unit.getAbrev().equalsIgnoreCase("g")) {
      result = grocery.getPrice() * (grocery.getQuantity() / 1000);
    } else {
      result = grocery.getPrice() * (grocery.getQuantity() * unit.getConvertionFactor());
    }

    return (double) Math.round(result * 100) / 100;
  }

  /**
   * <strong>Description</strong><br>
   * A method parsing an amount and a unit name to use the
   * {@link Grocery#addAmount(double, SI) addAmount()} method for adding
   * an amount to a Grocery.<br>
   *
   * @param amountAndUnit An array of type String containing an amount and a unit
   *                      name as Strings.
   * @throws Exception Giving a reason and a message of the given Exception.
   */
  public void addAmountGrocery(String[] amountAndUnit) throws Exception {
    try {
      //legg til en mengde
      double addAmount = Double.parseDouble(amountAndUnit[0]);
      SI addUnit = SI_Manager.getUnit(amountAndUnit[1]);
      grocery.addAmount(addAmount, addUnit);
    } catch (Exception e) {
      throw new Exception(
          "Could not add amount to the grocery for the following reason: " + e.getMessage());
    }
  }

  /**
   * <strong>Description</strong><br>
   * A method parsing an amount and a unit name to use the
   * {@link Grocery#removeAmount(double, SI) removeAmount()} method
   * to remove an amount from a Grocery.<br>
   *
   * @param amountAndUnit An array of type String containing an amount and a unit
   *                      name as Strings.
   * @throws Exception Giving a reason and a message of the given Exception.
   */
  public void removeAmountGrocery(String[] amountAndUnit) throws Exception {
    try {
      //trekk fra en mengde
      SI newUnit = SI_Manager.getUnit(amountAndUnit[1]);
      double amount = Double.parseDouble(amountAndUnit[0]);
      grocery.removeAmount(amount, newUnit);
    } catch (Exception e) {
      throw new Exception(
          "Could not remove amount from the grocery for the following reason: " + e.getMessage());
    }
  }
}
