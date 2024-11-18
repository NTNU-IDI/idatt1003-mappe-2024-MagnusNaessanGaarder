package edu.ntnu.idi.idatt.UI;

import java.util.Scanner;

public abstract class AbstractTerminalAction {
    private final Scanner scanner = new Scanner(System.in);
    private static final Scanner scannerStatic = new Scanner(System.in);

    /**
     * Essentially fetches an input-string based on if the {@code Scanner}-object
     * still is running.
     * @return a text String based on the object {@code Scanner}
     * which is refered as {@code myReader}. The method {@code .nextLine()} returns
     * a String value of the user input in the terminal on lineseparation.
     */
    protected String getInput() {
        if(scanner.hasNextLine()) {
            return scanner.nextLine();
        }
        return "";
    }
    protected static String getInputStatic() {
        if(scannerStatic.hasNextLine()) {
            return scannerStatic.nextLine();
        }
        return "";
    }

    protected void close() {
        scanner.close();
        scannerStatic.close();
    }

    /**
     *Effectivly clears the screen in the command line terminal.
     */
    protected void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
