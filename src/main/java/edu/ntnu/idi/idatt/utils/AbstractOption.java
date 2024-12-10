package edu.ntnu.idi.idatt.utils;

import static edu.ntnu.idi.idatt.modules.GroceryManager.getAmountAndUnit;

import edu.ntnu.idi.idatt.modules.Fridge;
import edu.ntnu.idi.idatt.modules.Grocery;
import edu.ntnu.idi.idatt.modules.GroceryManager;
import edu.ntnu.idi.idatt.views.UserInterface;

/**
 * <strong>Heritage</strong><br>
 * This abstract class inherits from the super class {@link AbstractTerminalAction}.<br><br>
 *
 * <strong>Description:</strong><br>
 * An abstract class for terminal actions to create abtraction layers, and hide information.<br><br>
 */
public abstract class AbstractOption extends AbstractTerminalAction {
  /**
   * <strong>Description</strong><br>
   * A method for creating option dialogues.<br><br>
   *
   * @param question A {@link String} containing a question for the Yes/No dialogue.
   * @return A {@code char} of either 'y', 'n' or 'e'. The 'y' and 'n' is treated as YES
   and NO options. The Character 'e' can be handled to write an appropriate message
   without creating a breakpoint.
   */
  public char option(String question) {
    System.out.println(question + " Skriv \"y\" for JA og \"n\" for NEI.");
    String userInput = getInput();
    if (userInput.equalsIgnoreCase("y") || userInput.equalsIgnoreCase("n")) {
      return userInput.toLowerCase().charAt(0);
    } else {
      System.out.println("Ugyldig input. Skriv enten \"y\" eller \"n\".\n");
      return 'e';
    }
  }

  /**
   * <strong>Description</strong><br>
   * A method for choosing an appropriate action from a userInput in the main menu.<br><br>
   * <i>Options:</i>
   * <ul>
   *     <li>Add a Grocery to a Fridge</li>
   *     <li>Remove a Grocery from a Fridge</li>
   *     <li>Overview of a Fridge</li>
   *     <li>Search after a name on a Grocery in a Fridge</li>
   *     <li>Display total value of all items in Fridge</li>
   *     <li>Close the application</li>
   * </ul>
   * <br>
   *
   * @param userInput An userinput of type {@code int}. Used to decide an action from
   *                  the switch-statment.
   * @param ui        Object of class {@link UserInterface}. Used to initiate an action
   *                  with regard to userInput.
   */
  protected void menuOption(final UserInterface ui, int userInput) {
    clearScreen();
    switch (userInput) {
      //legg til
      case 1 -> ui.addToFridge();

      //Fjern
      case 2 -> ui.removeFromFridge();

      //Oversikt over kjøleskapet
      case 3 -> ui.displayFridge();

      //Søk etter vare
      case 4 -> ui.showSearchFridge();

      //Samlet verdi av varer
      case 5 -> ui.showValue();

      //oversikt over kokebok
      case 6 -> ui.displayCookBook();

      //avslutt program
      case 7 -> ui.finish();

      default -> System.out.println("Input er ugyldig. Brukerinputen må være i intervallet [1,8].");
    }
  }

  /**
   * <strong>Description</strong><br>
   * A method for choosing an appropriate action from a userInput in the change-menu.<br><br>
   * <i>Options:</i>
   * <ul>
   *     <li>Add an amount to a Grocery.</li>
   *     <li>Remove amount from a Grocery.</li>
   *     <li>Check if a Grocery has expired.</li>
   * </ul>
   * <br>
   *
   * @param g   An object of type {@link Grocery}.
   * @param f   An object of type {@link Fridge}.
   * @param str An object of type {@link String}
   */
  protected void changeOptions(final Grocery g,
                               final Fridge f,
                               final String str,
                               final UserInterface ui) {
    final GroceryManager gm = new GroceryManager(g);
    switch (Integer.parseInt(str)) {
      //legg til mengde til varen
      case 1 -> {
        try {
          clearScreen();
          System.out.println(AbstractTable.createMenuTable("LEGG TIL " + g.getName().toUpperCase(),
              "Fyll ut deltaljer om varen du skal legge til:"));
          String[] amountAndUnit = getAmountAndUnit(getInput());

          gm.addAmountGrocery(amountAndUnit);
        } catch (Exception e) {
          System.out.println(e.getMessage());
        }
      }

      //fjern mengde fra varen
      case 2 -> {
        try {
          clearScreen();
          System.out.println(AbstractTable.createMenuTable("FJERN FRA " + g.getName().toUpperCase(),
              "Fyll ut deltaljer om varen du skal fjerne fra:"));
          String[] amountAndUnit = getAmountAndUnit(getInput());
          gm.removeAmountGrocery(amountAndUnit, f);
        } catch (Exception e) {
          System.out.println(e.getMessage());
        }
      }

      //sjekk om en vare er gått ut på dato
      case 3 -> {
        if (g.hasExpired()) {
          ui.getExpiredOption(g, f, str);
        } else {
          System.out.println("Varen har ikke gått ut på dato");
        }
      }
      default -> System.out.println(" - Ugyldig input.\n");
    }
  }

  /**
   * <strong>Description</strong><br>
   * A method for creating option dialogues for the "expired-check" in {@link #changeOptions}.<br>
   *
   * @return A {@code String} of an userinput from a scanner in the
   {@link AbstractTerminalAction} class.
   */
  @Override
  public String getInput() {
    return super.getInput();
  }
}
