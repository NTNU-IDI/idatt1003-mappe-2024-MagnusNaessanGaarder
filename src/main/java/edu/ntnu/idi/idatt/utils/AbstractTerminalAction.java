package edu.ntnu.idi.idatt.utils;

import java.util.Scanner;

/**
 * <strong>Description:</strong><br>
 * An abstract class for terminal actions. The idea here is that approperiate classes will
 * inherit this abstract class to create abtraction layers, and hide information.<br><br>
 *
 * <Strong>Datafields:</Strong><br>
 * {@code scanner} - {@link Scanner}-object that is used to fetch user info ({@link AbstractTerminalAction#getInput() getInput()}).<br>
 * {@code scannerStatic} - {@link Scanner}-object that is used to fetch user info while being static ({@link AbstractTerminalAction#getInputStatic() getInputStatic()}).<br><br>
 *
 * <Strong>Methods:</Strong><br>
 * {@link AbstractTerminalAction#getInput() getInput()} - Fetches userInput.<br>
 * {@link AbstractTerminalAction#getInputStatic() getInputStatic()} - Fetches userInput while beeing static.<br>
 * {@link AbstractTerminalAction#close() getInputStatic()} - Closes both Scanner-objects ({@code scanner} and {@code scannerStatic}).<br>
 * {@link AbstractTerminalAction#clearScreen() clearScreen()} - "Cleans" the terminal.<br>
 */
public abstract class AbstractTerminalAction {
  private static final Scanner scannerStatic = new Scanner(System.in);
  private final Scanner scanner = new Scanner(System.in);

  /**
   * <strong>Description</strong><br>
   * Essentially fetches an input-string statically based on if the {@code Scanner}-object
   * still is running. Workes with static methods.
   *
   * @return a text String based on the object {@code Scanner}
   * which is refered as {@code myReader}. The method {@code .nextLine()} returns
   * a String value of the user input in the terminal on lineseparation.
   */
  protected static String getInputStatic() {
    if (scannerStatic.hasNextLine()) {
      return scannerStatic.nextLine();
    }
    return "";
  }

  /**
   * <strong>Description</strong><br>
   * Essentially fetches an input-string based on if the {@code Scanner}-object
   * still is running. Workes with static methods.
   *
   * @return a text String based on the object {@code Scanner}
   * which is refered as {@code myReader}. The method {@code .nextLine()} returns
   * a String value of the user input in the terminal on lineseparation.
   */
  protected String getInput() {
    if (scanner.hasNextLine()) {
      return scanner.nextLine();
    }
    return "";
  }

  /**
   * <strong>Description</strong><br>
   * Closes both active {@link Scanner}-objects: {@code scanner} and {@code scannerStatic}.
   * The idea behind this method is to correctly close the Scanner-objects, so that there would not be any data-leaks.
   */
  protected void close() {
    scanner.close();
    scannerStatic.close();
  }

  /**
   * <strong>Description:</strong><br>
   * Effectivly clears the screen in the command line terminal.
   */
  protected void clearScreen() {
    System.out.print("\033[H\033[2J");
    System.out.flush();
  }
}
