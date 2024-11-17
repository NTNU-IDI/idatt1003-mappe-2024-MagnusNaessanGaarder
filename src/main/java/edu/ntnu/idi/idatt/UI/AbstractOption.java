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
                else {
                    System.out.println("Varen har ikke gått ut på dato");
                }
            }
            default -> System.out.println(" - Ugyldig input.\n");
        }
    }
}
