package edu.ntnu.idi.idatt.Client;

/*
 # Nivå 1:
 Som bruker må jeg kunne:
     • Opprette en ny vare/ingrediens (engelsk: Grocery eller Ingredient)
     • Legge varen i kjøleskapet/varelageret (engelsk: Fridge/FoodStorage). Her må man kunne
     angi mengden av varen, f.eks. 4 liter med melk, 12 stk egg osv.
     • Søke etter en vare (for å se om du har varen i kjøleskapet)
     • Ta ut/fjerne en mengde av en vare fra kjøleskapet.
     • Skrive ut en oversikt over varer i kjøleskapet
     • Skrive ut en oversikt over varer som har gått ut på dato, og samlet verdi (i kroner) på disse
     varene.
     • Beregne samlet verdi på samtlige varer i kjøleskapet.

 # Nivå 2:
 Applikasjonen utvides fra Nivå 1 med følgende funksjonlalitet:
 Som bruker må jeg kunne:
     • Opprette en matoppskrift (for en matrett). En matoppskrift (engelsk: Recipe) består
     typisk av følgende elementer: Et navn på oppskriften, en kort beskrivelse av hva
     oppskriften lager, en fremgangsmåte og en liste av ingredienser (inkludert mengde).
     • Sjekke om kjøleskapet inneholder nok varer/ingredienser til å lage matretten.
     • Legge oppskriften inn i en kokebok for senere bruk.
     • Få forslag til hvilke retter som kan lages fra rettene i kokeboken med
     varene/ingrediensene som finnes i kjøleskapet. (Avansert!)


 En vare/ingrediens kan bestå av følgende informasjon:
     • Navn på vare, f.eks. «Lettmelk»
     • Mengde av varen, f.eks. 1,5
     • Måleenhet, f.eks. «liter»
     • Best-før-dato (her kan du se litt på klassen java.util.Date, og
     java.text.SimpleDateFormater)
     • Pris/kostnad i norske kroner pr enhet.
*/


import edu.ntnu.idi.idatt.Manager.FridgeManager;
import edu.ntnu.idi.idatt.Manager.GroceryManager;
import edu.ntnu.idi.idatt.Manager.SI_manager;
import edu.ntnu.idi.idatt.UI.Display;
import edu.ntnu.idi.idatt.Modules.Fridge;
import edu.ntnu.idi.idatt.Modules.Grocery;
import edu.ntnu.idi.idatt.UI.Table;
import edu.ntnu.idi.idatt.UI.UserInterface;
import edu.ntnu.idi.idatt.Utils.SI;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import java.util.Arrays;


public class Main {

    private static final Scanner myReader = new Scanner(System.in);
    private static StringBuilder str = new StringBuilder();
    private static boolean running = true;

    private static final Fridge fridge = new Fridge();
    private static final FridgeManager fm = new FridgeManager(fridge);

    /**
     * Essentially fetches an input-string based on if the {@code Scanner}-object
     * still is running.
     * @return a text String based on the object {@code Scanner}
     * which is refered as {@code myReader}. The method {@code .nextLine()} returns
     * a String value of the user input in the terminal on lineseparation.
     */
    public static String getInput() {
        if (myReader.hasNextLine()) {
            return myReader.nextLine();
        }
        return "";
    }

    /**
     *Effectivly clears the screen in the command line terminal.
     */
    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * Determines which action to do based on the usergiven parameter.
     * @param userInput a String element given by the user via the {@link Main#getInput() getInput()}-method.
     */
    public static void menuOption(int userInput) {
        clearScreen();
        switch (userInput) {
            //legg til
            case 1 -> addToFridge();

            //Fjern
            case 2 -> removeFromFridge();

            //Oversikt over kjøleskapet
            case 3 -> displayFridge();

            //Søk etter vare
            case 4 -> searchFridge();

            //Samlet verdi av varer
            case 5 -> displayValue();

            //avslutt program
            case 6 -> finish();

            default -> System.err.println("Input er ugyldig. Brukerinputen må være i intervallet [1,6].");
        }
    }

    /**
     * Search the {@code Grocery}-objects in the fridge. The {@code Grocery} that is being searched after
     * is determined by an usergiven input, and the {@code name} of the Grocery. In other words, the
     * user can search for Groceries by their names.
     */
    public static void searchFridge() {
        clearScreen();
        if (fridge.getGroceryList().isEmpty()) {
            System.out.println("            ---- Ingen varer i kjøleskapet, kan ikke søke etter varer ----");
        }
        else {
            str = new StringBuilder();
            final Display display = new Display("VARESØK", "Søk på navnet til en vare i kjøleskapet. Skriv \"-e\" for å gå tilbake til hovedmenyen:", fridge);
            str.append(display.getTitle());
            System.out.println(str);

            String userInput = getInput();
            boolean checker = false;

            while (true) {
                clearScreen();
                System.out.println(str);

                if (checker) {
                    userInput = getInput();
                }
                else {
                    str.append("Kommando log:\n");
                }

                String finalUserInput = userInput;
                int countMax = (int)fridge.getGroceryList().stream()
                        .map(Grocery::getName)
                        .filter(name -> name.equals(finalUserInput))
                        .count();

                if (userInput.equals("-e")) {
                    break;
                }
                else if(countMax == 0) {
                    str.append("Varen ").append(userInput).append(" finnes ikke i kjøleskapet.\n");
                }
                else if (countMax == 1) {
                    Grocery searchedGrocery = fridge.getGroceryList().stream()
                            .filter(g -> g.getName().equals(finalUserInput))
                            .findFirst()
                            .orElse(null);

                    changeGrocery(searchedGrocery);
                    break;
                }
                else {
                    clearScreen();
                    List<Grocery> searchedGroceryList = fridge.getGroceryList().stream()
                            .filter(g -> g.getName().equals(finalUserInput))
                            .toList();

                    System.out.println("        Velg en av varene i listen ved å skrive en vareID:");
                    System.out.println(Display.displaySearchList(searchedGroceryList));

                    final String input = getInput();
                    Grocery chosenGrocery = fridge.getGroceryList().stream()
                            .filter(g -> g.getGroceryID() == Integer.parseInt(input))
                            .findFirst()
                            .orElse(null);

                    if (chosenGrocery != null) {
                        changeGrocery(chosenGrocery);
                        break;
                    }
                    else {
                        str.append("Ugyldig input: \"").append(input).append("\".\n");
                    }
                }
                checker = true;
            }
        }
    }

    /**
     *
     */
    public static void displayValue() {
        str = new StringBuilder();
        final Display display = new Display("SAMLET PRIS PÅ VARER", "Under er en oversikt over total prissum på varer:", fridge);
        str.append(display.getTitle());
        str.append(display.displayPrice(fridge.getGroceryList(), "Prisoversikt", "Vare", "Pris på mengde"));
        System.out.println(str);

        System.out.println("Skriv \"-e\" for å gå tilbake til menyen");
        while (true) {
            String userInput = getInput();
            if (userInput.equals("-e")) {
                break;
            }
        }
    }

    /**
     *
     */
    public static void removeFromFridge() {
        str = new StringBuilder();
        final String title = Table.createMenuTable("FJERN FRA KJØLESKAPET", "Bruk en komando nedenfor for å interagere med matvarer i kjøleskapet:");
        str.append(title);

        if (fridge.getGroceryList().isEmpty()) {
            str.append("            ---- Ingen varer er lagt til i kjøleskapet ----");
        }
        else {
            str.append(Display.displayList(fridge.getGroceryList()));
        }
        System.out.println(str);

        while (true) {
            System.out.println("Skriv \"-e\" for å gå tilbake til menyen, eller \"-remove [Vare ID]\" for å endre på en vare.\n"
                               + "[Vare ID] skal skrives som et tall.");
            String userInput = getInput();
            int userIndex = -1;

            if (userInput.equals("-e")) {
                break;
            }
            else if (userInput.equals("-remove")) {
                System.out.println("Skriv en vareID du ønsker å fjerne fra:");
                userInput = getInput();
                int userInt = Integer.parseInt(userInput);
                userIndex = fm.getGroceryListIndex(userInt);
            }
            else if (userInput.contains("-remove")) {
                int userInt = Integer.parseInt(String.join("", userInput.split("[^0-9]")));
                userIndex = fm.getGroceryListIndex(userInt);
            }
            else {
                System.err.println("Ugyldig kommando. Skriv enten \"-e\" eller \"-remove\".");
            }

            if (userIndex >= 0 && userIndex < fridge.getGroceryList().size()) {
                clearScreen();
                Grocery grocery = fm.getGrocery(userIndex);
                final GroceryManager gm = new GroceryManager(grocery);

                gm.removeAmountGrocery();
            }
            else {
                clearScreen();
                System.err.println("Ugyldig vareID");
            }
        }
    }

    /**
     *
     */
    public static void addToFridge() {

        str = new StringBuilder();
        final String title = Table.createMenuTable("LEGG TIL VARE", "Fyll ut feltene nedenfor for å legge til vare:");
        str.append(title);
        System.out.println(str);

        String name;
        SI measure;
        double quantity;
        LocalDate date = null;
        double price;

        //navn på varen
        System.out.print("          Skriv navnet på varen: ");
        name = getInput();

        //mengden og enheten av varen
        String[] amountAndUnit = GroceryManager.getAmountAndUnit();
        measure = SI_manager.getUnit(amountAndUnit[1]);
        quantity = Double.parseDouble(amountAndUnit[0]);

        //Best-før dato
        String userInput;
        boolean valid = false;
        do {
            System.out.print("          Skriv en Best-før dato på formen DD-MM-YYYY: ");
            userInput = getInput();
            try{
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                date = LocalDate.parse(userInput, formatter);
                valid = true;
            }
            catch (Exception e) {
                System.err.println("Ugyldig format");
            }
        }
        while (!valid);

        //pris
        assert measure != null;
        System.out.printf("          Skriv pris. Oppgi pris i kr/%s f.eks (20 kr/%s): ", measure.getUnitForPrice(), measure.getUnitForPrice());
        userInput = getInput();
        String priceStr = String.join("", userInput.split("[^,.\\d]"));
        price = Double.parseDouble(String.join(".", priceStr.split("[,.]")));

        //sjekker om brukeren ønsker å legge til varen
        clearScreen();
        str = new StringBuilder();
        Table table = new Table(
                name,
                new String[]{"Mengde", "Best-før dato", "pris"},
                new String[]{quantity +  " " + measure.getAbrev(), date + "", price + " kr/" + measure.getUnitForPrice()}
        );
        str.append(table.createTable());

        while(true) {
            System.out.println(str);
            System.out.println("Er du sikker på at du ønsker å legge til denne varen til kjøleskapet? Skriv \"y\" for JA eller \"n\" for NEI.");
            userInput = getInput();
            if (userInput.equalsIgnoreCase("y")) {
                //sjekker om varen allerede er i kjøleskapet
                Grocery grocery = new Grocery(name, measure, quantity, date, price, fridge);
                fridge.addGrocery(grocery);
                break;
            }
            else if (userInput.equalsIgnoreCase("n")) {
                break;
            }
            else {
                System.err.println("Ugyldig input. Svar \"y\", eller \"n\".");
            }
        }
    }

    public static void finish() {
        while (true) {
            System.out.println("Vil du virkelig avslutte programmet? Skriv \"y\" for JA eller \"n\" for NEI.");
            String userInput = getInput();
            if(userInput.equalsIgnoreCase("y")) {
                running = false;
                myReader.close();
                break;
            }
            else if (userInput.equalsIgnoreCase("n")) {
                break;
            }
            else {
                System.err.println("Ugyldig brukerinput.");
            }
        }
    }

    /**
     * @param grocery Instanse av klassen {@link Grocery}. Parameteren {@code grocery}
     *                er matvare-objektet som skal endres med metoden.
     */
    public static void changeGrocery(Grocery grocery) {
        clearScreen();

        str = new StringBuilder();
        Display display = new Display("ENDRE VARE", "Velg en handling i listen under:", fridge);
        str.append(display.getTitle());
        str.append(display.grocery(grocery));

        str.append("           [1] Legg til en mengde til varen.\n");
        str.append("           [2] Trekk fra en mengde fra varen.\n");
        str.append("           [3] Sjekk om varen har gått ut på dato.\n");
        System.out.println(str);

        System.out.println("""
                    Skriv "-e" for å gå tilbake til menyen, eller "tall" for å endre på en vare.
                    "tall" skal skrives som et heltall i intervallet [1,3].
                    
                    """);

        String userInput = getInput();
        boolean checker = false;

        while (true) {
            clearScreen();
            System.out.println(str);
            System.out.println("""
                    Skriv "-e" for å gå tilbake til menyen, eller "tall" for å endre på en vare.
                    "tall" skal skrives som et heltall i intervallet [1,3].
                    
                    """);
            System.out.println("\n\n");

            if (checker) {
                userInput = getInput();
            }
            else {
                str.append("Command log:\n");
            }


            if (userInput.equals("-e")) {
                break;
            }

            else if (Integer.parseInt(userInput) >= 1 || Integer.parseInt(userInput) <= 3) {
                final GroceryManager gm = new GroceryManager(grocery);
                switch (Integer.parseInt(userInput)) {
                    //legg til mengde til varen
                    case 1 -> gm.addAmountGrocery();

                    //legg til mengde til varen
                    case 2 -> gm.removeAmountGrocery();

                    //sjekk om en vare er gått ut på dato
                    case 3 -> {
                        try {
                            if (grocery.hasExpired()) {
                                System.out.println("Varen har gått ut på dato");

                                while (true) {
                                    System.out.println("Ønsker du å slette varen? Skriv \"y\" for JA og \"n\" for NEI.");
                                    userInput = getInput();
                                    if (userInput.equalsIgnoreCase("y")) {
                                        fridge.removeGrocery(grocery);
                                        break;
                                    }
                                    else if (userInput.equalsIgnoreCase("n")) {
                                        break;
                                    }
                                    else {
                                        str.append("Ugyldig input. Skriv enten \"y\" eller \"n\".\n");
                                    }
                                }
                            }
                            else {
                                System.out.println("Varen har ikke gått ut på dato");
                            }
                        }
                        catch (Exception e) {
                            str.append(e.getMessage()).append("\n");
                        }
                    }
                    default -> str.append(" - Ugyldig input.\n");
                }
            }
            else {
                str.append(" - Ugyldig kommando: \"").append(userInput).append("\"\n");
            }
            checker = true;
        }
    }

    public static void displayFridge() {
        str = new StringBuilder();
        Display display = new Display("KJØLESKAP", "Her er en overikt over ulike varer i kjøleskapet:", fridge);
        str.append(display.getTitle());

        if (fridge.getGroceryList().isEmpty()) {
            str.append("            ---- Kjøleskapet er tomt ----\n\n");
        }
        else {
            str.append("        Utgåtte varer:\n\n");
            str.append(display.list(fm.getExpiredList(), "Ingen varer er gått ut på dato"));
            str.append("        Varer som holder på å gå ut på dato:\n\n");
            str.append(display.list(fm.getNearExpList(), "Ingen varer er nær ved å gå ut på dato"));
            str.append("        Resterende varer:\n\n");
            str.append(display.list(fm.getRestGroceryList(), "Ingen resterende varer")).append("\n\n");
        }

        if (!fm.getExpiredList().isEmpty()) {
            str.append(
                    display.displayPriceUnique(
                            fm.getExpiredList(),
                            "Total pengetap på datovarer",
                            "Vare",
                            "Pris beregnet på mengde"
                    )
            );
        }
        System.out.println(str);
        System.out.println("""
                        Skriv "-delete" for å slette en vare. Kommandoen "-delete all" vil slette alle utgåtte varer.
                        Skriv "-change" eller "-change [vareID]" for å endre på en vare. "[vareID]" skrives som et tall.
                        Skriv "-e" for å gå tilbake til menyen.""");
        String userInput = getInput();
        commands(userInput);
    }

    /**
     * @param input ment å representere brukerinput av datatypen {@code String}.
     */
    public static void commands(String input) {
        String userInput = input;
        boolean checker = false;

        while (true) {
            clearScreen();
            System.out.println(str);
            System.out.println("\n\n");

            //sjekker hvilken dialog-option vi skal skrive ut
            if (fridge.getGroceryList().isEmpty()) {
                System.out.println("Skriv \"-e\" for å gå tilbake til menyen");
            }
            else if (fm.getExpiredList().isEmpty()) {
                System.out.println("""
                        Skriv "-change" eller "-change [vareID]" for å endre på en vare. "[vareID]" skrives som et tall.
                        Skriv "-e" for å gå tilbake til menyen.""");
            }
            else {
                System.out.println("""
                        Skriv "-delete" for å slette en vare. Kommandoen "-delete all" vil slette alle utgåtte varer.
                        Skriv "-change" eller "-change [vareID]" for å endre på en vare. "[vareID]" skrives som et tall.
                        Skriv "-e" for å gå tilbake til menyen.""");
            }

            //Sjekker om checker er sann (bare hvis while har loopet minst en gang). Henter ny brukerinput
            if (checker) {
                userInput = getInput();
            }
            else {
                str.append("Command log:\n");
            }

            //behandler brukerinput
            int userIndex;
            if (userInput.equals("-e")) {
                break;
            }
            else if (userInput.equals("-delete") && !fm.getExpiredList().isEmpty()) {
                deleteItems(userInput);
            }
            else if (userInput.equals("-delete all") && !fm.getExpiredList().isEmpty()) {
                for(Grocery expiredItem : fm.getExpiredList()) {
                    fridge.removeGrocery(expiredItem);
                }
                str.append("Alle datovarer er nå fjernet!\n");
            }
            else if (userInput.contains("-change")) {
                if (userInput.equals("-change")) {
                    System.out.println("Skriv en vareID du ønsker å fjerne fra:");
                    userInput = getInput();
                }
                int userInt = Integer.parseInt(userInput);
                userIndex = fm.getGroceryListIndex(userInt);

                if (userIndex >= 0 && userIndex < fridge.getGroceryList().size()) {
                    changeGrocery(fm.getGrocery(userIndex));
                    str.append(" - Endret vare ").append(userIndex+1).append("\n");
                }
                else {
                    str.append(" - Ugyldig vareID.\n");
                }
            }
            else if (fridge.getGroceryList().isEmpty() && userInput.contains("-delete")) {
                str.append(" - Det finnes ingen datovarer. "
                           + "Kan ikke fjerne datovarer fra kjøleskapet.\n");
            }
            else {
                str.append(" - Ugyldig kommando. \"").append(userInput).append("\"\n");
            }
            checker = true;
        }
    }

    /**
     * @param input ment å representere brukerinput av datatypen {@code String}.
     */
    public static void deleteItems(String input) {
        try {
            clearScreen();
            Display.displayList(fm.getExpiredList());
            System.out.println("Skriv inn en vareID fra listen ovenfor for å fjerne en vare fra kjøleskapet."
                               + "Skriv flere vareID-er separert av \",\"(comma) for å fjerne flere varer fra kjøleskapet.");

            input = getInput();
            String[] deleteStrArr = input.replaceAll("\\s+", "").split(",");
            int[] deleteArr = Arrays.stream(deleteStrArr).mapToInt(Integer::parseInt).toArray();

            for (int groceryID : deleteArr) {
                Grocery removableGrocery = fridge.getGroceryList().stream()
                        .filter(Grocery::hasExpired)
                        .filter(grocery -> grocery.getGroceryID() == groceryID)
                        .findFirst()
                        .orElse(null);
                if (removableGrocery != null) {
                    fridge.removeGrocery(removableGrocery);
                }
                else {
                    System.err.println("Kunne ikke fjerne vare med vareID "  + groceryID + " fra kjøleskapet.");
                }
            }

            System.out.println("Alle gyldige valgte datovarer er nå fjernet!\n");
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * The main method of the application. This method always initiates at every time the program runs.
     */
    public static void main(final String[] args) {
        final UserInterface UI = new UserInterface();

        UI.start();
        UI.init();

        while (running) {

            // Menyvalg i konsollen
            clearScreen();
            str = new StringBuilder();

            final String menuStr = Table.createMenuTable("HOVEDMENY - MATLAGER", "Liste med tilgjengelige varer");
            str.append(menuStr);
            str.append(Display.menuList(fridge.getGroceryList(), "Fant ingen varer i kjøleskapet")).append("\n");

            str.append("            Velg fra listen nedenfor:").append("\n\n");

            str.append("                   [1] Legg til vare.\n");
            str.append("                   [2] Fjern vare.\n");
            str.append("                   [3] Oversikt over kjøleskapet.\n");
            str.append("                   [4] Søk etter vare.\n");
            str.append("                   [5] Samlet verdi av varer.\n");
            str.append("                   [6] Avslutt.\n");
            System.out.println(str);

            try {
                int userInput = Integer.parseInt(getInput());
                menuOption(userInput);
            }
            catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }
}
