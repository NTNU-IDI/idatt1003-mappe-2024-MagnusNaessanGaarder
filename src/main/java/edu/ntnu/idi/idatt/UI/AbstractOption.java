package edu.ntnu.idi.idatt.UI;

import edu.ntnu.idi.idatt.Manager.GroceryManager;
import edu.ntnu.idi.idatt.Modules.Fridge;
import edu.ntnu.idi.idatt.Modules.Grocery;

/**
 * <strong>Heritage</strong><br>
 * This abstract class inherits from the super class {@link AbstractTerminalAction}.<br><br>
 *
 * <strong>Description:</strong><br>
 * An abstract class for terminal actions. The idea here is that approperiate classes will
 * inherit this abstract class to create abtraction layers, and hide information.<br><br>
 *
 * <Strong>Methods:</Strong><br>
 * {@link #option(String) option(String question)} - Displays a Yes/No dialogue and returns a {@code char}.<br>
 * {@link #menuOption(UserInterface, int) menuOption(UserInterface UI, int userInput)} - Regulates available actions
 * from the UserInterface-object with a switch-statment.<br>
 * {@link #changeOptions(Grocery, Fridge, String) changeOptions(Grocery g, Fridge f, String str)} - Regulates available
 * "grocery-changing" actions.<br>
 * {@link #getExpiredOption(Grocery, Fridge, String) getExpiredOption(Grocery g, Fridge f, String str)} - Displays a
 * Yes/No dialogue for an expired Grocery.<br>
 */
public abstract class AbstractOption extends AbstractTerminalAction {
    /**
     * <strong>Description</strong><br>
     * A method for creating option dialogues.<br><br>
     *
     * @param question A {@link String} containing a question for the Yes/No dialogue.
     * @return A {@code char} of either 'y', 'n' or 'e'. In this case 'y' is ment to
     * represent a Yes form the user, 'n' a No and 'e', error. The Character 'e' can be
     * caught and handled, so we can write an appropriate message without creating a breakpoint.
     */
    public char option(String question) {
        System.out.println(question + " Skriv \"y\" for JA og \"n\" for NEI.");
        String userInput = getInput();
        if (userInput.equalsIgnoreCase("y") || userInput.equalsIgnoreCase("n")) {
            return userInput.toLowerCase().charAt(0);
        }
        else {
            System.out.println("Ugyldig input. Skriv enten \"y\" eller \"n\".\n");
            return 'e';
        }
    }

    /**
     * <strong>Description</strong><br>
     * A method for choosing an appropriate action from a userInput in the main menu.<br>
     * Options:
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
     * @param userInput An userinput of type {@code int}. Used to decide an action from the switch-statment.
     * @param UI Object of class {@link UserInterface}. Used to initiate an action with regard to userInput.
     */
    protected void menuOption(UserInterface UI, int userInput) {
        clearScreen();
        switch (userInput) {
            //legg til
            case 1 -> UI.addToFridge();

            //Fjern
            case 2 -> UI.removeFromFridge();

            //Oversikt over kjøleskapet
            case 3 -> UI.displayFridge();

            //Søk etter vare
            case 4 -> UI.showSearchFridge();

            //Samlet verdi av varer
            case 5 -> UI.showValue();

            //avslutt program
            case 6 -> UI.finish();

            default -> System.out.println("Input er ugyldig. Brukerinputen må være i intervallet [1,6].");
        }
    }

    /**
     * <strong>Description</strong><br>
     * A method for choosing an appropriate action from a userInput in the change-menu.<br>
     * Options:
     * <ul>
     *     <li>Add a amount to a Grocery.</li>
     *     <li>Remove amount from a Grocery.</li>
     *     <li>Check if a Grocery has expired.</li>
     * </ul>
     * <br>
     *
     * @param g An object of type {@link Grocery}.
     * @param f An object of type {@link Fridge}.
     * @param str An object of type {@link String}
     */
    protected void changeOptions(Grocery g, Fridge f, String str) {
        final GroceryManager gm = new GroceryManager(g);
        switch (Integer.parseInt(str)) {
            //legg til mengde til varen
            case 1 -> gm.addAmountGrocery();

            //fjern mengde fra varen
            case 2 -> gm.removeAmountGrocery();

            //sjekk om en vare er gått ut på dato
            case 3 -> {
                if (g.hasExpired()) {
                    getExpiredOption(g, f, str);
                }
                else {
                    System.out.println("Varen har ikke gått ut på dato");
                }
            }
            default -> System.out.println(" - Ugyldig input.\n");
        }
    }

    /**
     * <strong>Description</strong><br>
     * A method for creating option dialogues for the "expired-check" in {@link #changeOptions}<br>
     *
     * @param g An object of type {@link Grocery}.
     * @param f An object of type {@link Fridge}.
     * @param str An object of type {@link String}
     */
    protected void getExpiredOption(Grocery g, Fridge f, String str) {
        System.out.println("Varen har gått ut på dato");
        System.out.println(str);
        char yesNoErr;
        do {
            yesNoErr = option("Ønsker du å slette varen?");
        }
        while (yesNoErr == 'e');

        if (yesNoErr == 'y') {
            f.removeGrocery(g);
        }
    }
}
