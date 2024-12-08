package edu.ntnu.idi.idatt;

import edu.ntnu.idi.idatt.modules.UserInterface;

/**
 * <strong>Description</strong><br>
 * The main client class for the application.
 This application keeps track of Groceries in a Fridge / Food Storage<br>
 *
 * @author Magnus Naessan Gaarder.
 * @version 2.0.0
 */
public class StorageApplication {
  /**
   * <strong>Description</strong><br>
   * The main method of the application. This method always initiates every time the program runs
   * and starts the application.
   */
  public static void main(final String[] args) {
    final UserInterface ui = new UserInterface();
    ui.init();
    ui.start();
  }
}
