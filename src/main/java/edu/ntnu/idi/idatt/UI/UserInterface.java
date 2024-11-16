package edu.ntnu.idi.idatt.UI;


import edu.ntnu.idi.idatt.Client.Main;
import edu.ntnu.idi.idatt.Manager.FridgeManager;
import edu.ntnu.idi.idatt.Manager.GroceryManager;
import edu.ntnu.idi.idatt.Manager.SI_manager;
import edu.ntnu.idi.idatt.Modules.*;
import edu.ntnu.idi.idatt.Utils.SI;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class UserInterface {
    private final Scanner myReader;
    private StringBuilder str;
    private boolean running;

    private final Fridge fridge;
    private final FridgeManager fm;

    public UserInterface () {
        this.myReader = new Scanner(System.in);
        this.str = new StringBuilder();
        this.running = true;
        this.fridge = new Fridge();
        this.fm = new FridgeManager(fridge);;
    }

    /**
     * Essentially fetches an input-string based on if the {@code Scanner}-object
     * still is running.
     * @return a text String based on the object {@code Scanner}
     * which is refered as {@code myReader}. The method {@code .nextLine()} returns
     * a String value of the user input in the terminal on lineseparation.
     */
    private String getInput() {
        if (myReader.hasNextLine()) {
            return myReader.nextLine();
        }
        return "";
    }

    /**
     *Effectivly clears the screen in the command line terminal.
     */
    private void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * A method used to test four different instances of a grocery.
     */
    public void start() {

        //test av Grocery-objekt
        final SI G = new SI("Gram","g","kg","");
        final SI STK = new SI("Stykker","stk","stk","");
        final SI L = new SI("Liter","L","L","");

        final Grocery grocery1 = new Grocery("Mel", G,2000, LocalDate.now().minusDays(2),200,fridge);
        final Grocery grocery2 = new Grocery("Bananer", STK,18,LocalDate.now(),49.90,fridge);
        final Grocery grocery3 = new Grocery("Mel", G,500,LocalDate.now().plusDays(4),200,fridge);
        final Grocery grocery4 = new Grocery("Kraft", L,0.5,LocalDate.now().plusDays(1),259.99,fridge);

        this.fridge.addGrocery(grocery1);
        this.fridge.addGrocery(grocery2);
        this.fridge.addGrocery(grocery3);
        this.fridge.addGrocery(grocery4);
        this.fridge.addGrocery(grocery4);

        grocery1.addAmount(500, G);
        grocery2.removeAmount(18, STK);

        //Hvis metodene gjør det rett bør utskriften være noe liknende dette:
        /*
        *
        * Klasse Fridge;
        *   Innhold:
        *       Klasse Grocery;
        *           VareID: 1;
        *           Navn på varen: Mel;
        *           Mengde av varen: 2,5kg;
        *           Best-før dato: {LocalDate.now().minusDays(2)};
        *           Pris per måleenhet: 200 kr/kg;
        *
        *       Klasse Grocery;
        *           VareID: 3;
        *           Navn på varen: Mel;
        *           Mengde av varen: 500g;
        *           Best-før dato: {LocalDate.now().plusDays(4)};
        *           Pris per måleenhet: 200 kr/kg;
        *
        *       Klasse Grocery;
        *           VareID: 4;
        *           Navn på varen: Kraft;
        *           Mengde av varen: 5dL;
        *           Best-før dato: {LocalDate.now().plusDays(1)};
        *           Pris per måleenhet: 259,99 kr/L;
        *
        */

        System.out.println(this.fridge.toString());
    }

    /**
     * A method used to start the actual application.
     */
    public void init() {
        while(running) {
            showMenu();
        }
    }

    private void showMenu() {

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
            System.out.println(e.getMessage());
        }
    }

    /**
     * Determines which action to do based on the usergiven parameter.
     * @param userInput a String element given by the user via the {@link UserInterface#getInput() getInput()}-method.
     */
    private void menuOption(int userInput) {
        clearScreen();
        switch (userInput) {
            //legg til
            case 1 -> addToFridge();

            //Fjern
            case 2 -> removeFromFridge();

            //Oversikt over kjøleskapet
            case 3 -> displayFridge();

            //Søk etter vare
            case 4 -> showSearchFridge();

            //Samlet verdi av varer
            case 5 -> showValue();

            //avslutt program
            case 6 -> finish();

            default -> System.out.println("Input er ugyldig. Brukerinputen må være i intervallet [1,6].");
        }
    }

    /**
     *
     */
    private void addToFridge() {

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
        System.out.println("          Skriv navnet på varen: ");
        name = getInput();

        //mengden og enheten av varen
        String[] amountAndUnit = GroceryManager.getAmountAndUnit();
        measure = SI_manager.getUnit(amountAndUnit[1]);
        quantity = Double.parseDouble(amountAndUnit[0]);

        //Best-før dato
        String userInput;
        boolean valid = false;
        do {
            System.out.println("          Skriv en Best-før dato på formen DD-MM-YYYY: ");
            userInput = getInput();
            try{
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                date = LocalDate.parse(userInput, formatter);
                valid = true;
            }
            catch (Exception e) {
                System.out.println("Ugyldig format");
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
            if (userInput.equalsIgnoreCase("y") || userInput.equalsIgnoreCase("n")) {
                if (userInput.equalsIgnoreCase("y")) {
                    //sjekker om varen allerede er i kjøleskapet
                    Grocery grocery = new Grocery(name, measure, quantity, date, price, fridge);
                    fridge.addGrocery(grocery);
                }
                break;
            }
            else {
                System.out.println("Ugyldig input. Svar \"y\", eller \"n\".");
            }
        }
    }

    /**
     *
     */
    private void removeFromFridge() {
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
                int userInt = Integer.parseInt(String.join("", userInput.split("\\D")));
                userIndex = fm.getGroceryListIndex(userInt);
            }
            else {
                System.out.println("Ugyldig kommando. Skriv enten \"-e\" eller \"-remove\".");
            }

            if (userIndex >= 0 && userIndex < fridge.getGroceryList().size()) {
                clearScreen();
                Grocery grocery = fm.getGrocery(userIndex);
                final GroceryManager gm = new GroceryManager(grocery, myReader);

                gm.removeAmountGrocery();
            }
            else {
                clearScreen();
                System.out.println("Ugyldig vareID");
            }
        }
    }

    private void displayFridge() {
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
     *
     */
    private void showValue() {
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
     * Search the {@code Grocery}-objects in the fridge. The {@code Grocery} that is being searched after
     * is determined by an usergiven input, and the {@code name} of the Grocery. In other words, the
     * user can search for Groceries by their names.
     */
    private void showSearchFridge() {
        clearScreen();
        if (fridge.getGroceryList().isEmpty()) {
            System.out.println("            ---- Ingen varer i kjøleskapet, kan ikke søke etter varer ----");
        }
        else {
            str = new StringBuilder();
            final Display display = new Display("VARESØK", "Søk på navnet til en vare i kjøleskapet. Skriv \"-e\" for å gå tilbake til hovedmenyen:", fridge);
            str.append(display.getTitle());
            System.out.println(str);
            search();
        }
    }

    private void search () {
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
                }
                else {
                    str.append("Ugyldig input: \"").append(input).append("\".\n");
                }
            }
            checker = true;
        }
    }

    /**
     * @param grocery Instanse av klassen {@link Grocery}. Parameteren {@code grocery}
     *                er matvare-objektet som skal endres med metoden.
     */
    private void changeGrocery(Grocery grocery) {
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
                final GroceryManager gm = new GroceryManager(grocery, myReader);
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
                                    if (userInput.equalsIgnoreCase("y") || userInput.equalsIgnoreCase("n")) {
                                        if (userInput.equalsIgnoreCase("y")) {
                                            fridge.removeGrocery(grocery);
                                        }
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

    private void finish() {
        while (true) {
            System.out.println("Vil du virkelig avslutte programmet? Skriv \"y\" for JA eller \"n\" for NEI.");
            String userInput = getInput();
            if(userInput.equalsIgnoreCase("y") || userInput.equalsIgnoreCase("n")) {
                if (userInput.equalsIgnoreCase("y")) {
                    running = false;
                    myReader.close();
                }
                break;
            }
            else {
                System.out.println("Ugyldig brukerinput.");
            }
        }
    }


    /**
     * @param input ment å representere brukerinput av datatypen {@code String}.
     */
    private void commands(String input) {
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
                deleteItems();
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
     *
     */
    private void deleteItems() {
        try {
            clearScreen();
            Display.displayList(fm.getExpiredList());
            System.out.println("Skriv inn en vareID fra listen ovenfor for å fjerne en vare fra kjøleskapet."
                    + "Skriv flere vareID-er separert av \",\"(comma) for å fjerne flere varer fra kjøleskapet.");

            String userInput = getInput();
            String[] deleteStrArr = userInput.replaceAll("\\s+", "").split(",");
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
                    System.out.println("Kunne ikke fjerne vare med vareID "  + groceryID + " fra kjøleskapet.");
                }
            }

            System.out.println("Alle gyldige valgte datovarer er nå fjernet!\n");
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
