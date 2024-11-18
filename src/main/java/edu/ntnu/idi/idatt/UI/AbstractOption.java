package edu.ntnu.idi.idatt.UI;

import edu.ntnu.idi.idatt.Manager.GroceryManager;
import edu.ntnu.idi.idatt.Modules.Fridge;
import edu.ntnu.idi.idatt.Modules.Grocery;

public abstract class AbstractOption extends AbstractTerminalAction {

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

    protected void changeOptions(Grocery g, Fridge f, String str) {
        final GroceryManager gm = new GroceryManager(g);
        switch (Integer.parseInt(str)) {
            //legg til mengde til varen
            case 1 -> gm.addAmountGrocery();

            //legg til mengde til varen
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
