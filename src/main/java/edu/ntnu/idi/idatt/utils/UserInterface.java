package edu.ntnu.idi.idatt.utils;

import edu.ntnu.idi.idatt.manager.*;
import edu.ntnu.idi.idatt.modules.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

/**
 * <strong>Heritage</strong><br>
 * This class inherits traits from the super class {@link AbstractOption}.<br><br>
 *
 * <strong>Description</strong><br>
 * A class that handles the Text-based User Interface(TUI) of the application. <br><br>
 *
 * <strong>Datafields</strong><br>
 * {@code str} - A private object of type {@link StringBuilder}, for large and structured Strings.<br>
 * {@code running} - A private boolean determining if the application continues.<br>
 * {@code fridge} - An object of type {@link Fridge} acting as the foodstorage for the application.<br>
 * {@code fm} - An object of type {@link FridgeManager}. Helps managing the Fridge.<br>
 */
public class UserInterface extends AbstractOption {
    //global variables
    private StringBuilder str;
    private boolean running;
    private final Fridge fridge;
    private final FridgeManager fm;

    /**
     *<strong>Description</strong><br>
     * A constructor initalizing the food storage({@link Fridge}) and the {@link FridgeManager} to manage the food storage.
     */
    public UserInterface() {
        this.fridge = new Fridge();
        this.fm = new FridgeManager(fridge);
    }

    /**
     * <strong>Description</strong><br>
     * A method used to test four different instances of a grocery.
     */
    public void init() {
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

        this.running = true;
        this.str = new StringBuilder();
    }

    /**
     * A method used to start the actual application.
     */
    public void start() {
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
            super.menuOption(this, userInput);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Determines which action to do based on the usergiven parameter.
     * @param UI a String element given by the user via the {@link UserInterface}-method.
     */
    @Override
    protected void menuOption(UserInterface UI, int userInput) {
        super.menuOption(this, userInput);
    }

    /**
     *
     */
    public void addToFridge() {
        str = new StringBuilder();
        final String title = Table.createMenuTable("LEGG TIL VARE", "Fyll ut feltene nedenfor for å legge til vare:");
        str.append(title);
        System.out.println(str);

        //local variables
        String name;
        SI measure = null;
        double quantity = 0;
        LocalDate date;
        double price;

        //navn på varen
        System.out.print("          Skriv navnet på varen: ");
        name = getInput();

        //mengden og enheten av varen
        boolean retry = true;
        while(retry) {
            try{
                String[] amountAndUnit = fetchAmountAndUnit();
                measure = SI_manager.getUnit(amountAndUnit[1]);
                quantity = Double.parseDouble(amountAndUnit[0]);
                retry = false;
            }
            catch(Exception e) {
                System.out.println(e.getMessage());
            }
        }

        //Best-før dato
        date = fetchDate();

        //pris
        price = fetchPrice(measure);

        //sjekker om brukeren ønsker å legge til varen
        clearScreen();
        str = new StringBuilder();
        Display display = new Display(fm);
        String table = display.createTable(
                name,
                new String[]{"Mengde", "Best-før dato", "pris"},
                new String[]{quantity +  " " + measure.getAbrev(), date + "", price + " kr/" + measure.getUnitForPrice()}
        );
        str.append(table);
        System.out.println(str);

        char yesNoErr = 'e';
        while (yesNoErr == 'e') {
            yesNoErr = option("Er du sikker på at du ønsker å legge til denne varen til kjøleskapet?");
        }

        if (yesNoErr == 'y') {
            //sjekker om varen allerede er i kjøleskapet
            Grocery grocery = new Grocery(name, measure, quantity, date, price, fridge);
            fridge.addGrocery(grocery);
        }
    }

    private String[] fetchAmountAndUnit() {
        String userInput;
        String[] amountAndUnit = new String[0];
        boolean retry = true;
        while (retry) {
            try {
                System.out.print("          Skriv mengden på varen (f.eks 2 gram / desiliter / stykker): ");
                userInput = getInputStatic();
                amountAndUnit = GroceryManager.getAmountAndUnit(userInput);
                retry = false;
            }
            catch(Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return amountAndUnit;
    }

    private double fetchPrice(SI measure) {
        boolean retry = true;
        double price = 0;
        while(retry) {
            try{
                    price = priceFetcher(measure);
                    retry = false;
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return price;
    }

    private double priceFetcher(SI measure) throws Exception {
        try{
            System.out.printf("          Skriv pris. Oppgi pris i kr/%s f.eks (20 kr/%s): ", measure.getUnitForPrice(), measure.getUnitForPrice());
            final String userInput = getInput();
            String priceStr = String.join("", userInput.split("[^,.\\d]"));
            return Double.parseDouble(String.join(".", priceStr.split("[,.]")));
        }
        catch (NumberFormatException e) {
            throw new NumberFormatException("The given price cannot be parsed to a Double. Please write a valid decimal number.");
        }
        catch(Exception e) {
            throw new Exception("Unexpected error while fetching price: " + e.getMessage());
        }
    }

    private LocalDate fetchDate() {
        boolean retry = true;
        LocalDate date = null;
        while (retry) {
            try {
                System.out.print("          Skriv en Best-før dato på formen DD-MM-YYYY: ");
                date = dateFetcher(getInput());
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
            }
            if (date != null) {
                retry = false;
            }
        }
        return date;
    }

    private LocalDate dateFetcher(String userInput) throws Exception {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            return LocalDate.parse(userInput, formatter);
        }
        catch (Exception e) {
            throw new Exception("Ugyldig format - Skriv datoen på formen DD-MM-YYYY.");
        }
    }

    private void displayRemoveList() {
        str = new StringBuilder();
        final String title = Table.createMenuTable("FJERN FRA KJØLESKAPET", "Bruk en komando nedenfor for å interagere med matvarer i kjøleskapet:");
        str.append(title);

        if (fridge.getGroceryList().isEmpty()) {
            str.append("            ---- Ingen varer er lagt til i kjøleskapet ----");
        }
        else {
            str.append(Display.displayList(fridge.getGroceryList()));
        }
        str.append("\n\nKommando log:\n");
    }

    /**
     *
     */
    public void removeFromFridge() {
        try {
            displayRemoveList();
            removeHandler();
        }
        catch (Exception e) {
            str.append(e.getMessage());
        }
    }

    private void removeHandler() {
        boolean retry = true;
        while (retry) {
            clearScreen();
            try {
                System.out.println(str);
                System.out.println("""
                        
                        Skriv "-e" for å gå tilbake til menyen, eller "-remove [Vare ID]" for å fjerne en mengde fra en vare.
                        [Vare ID] skal skrives som et tall.""");
                String userInput = getInput();
                retry = removeInputHandler(userInput);
                if (retry) {
                    displayRemoveList();
                }
            }
            catch(Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private boolean removeInputHandler(String userInput) throws Exception {
        if (userInput.equals("-e")) {
            return false;
        }
        else if (userInput.contains("-remove") && !fm.getExpiredList().isEmpty()) {
            int userInt;
            boolean retry = true;
            while (retry) {
                try {
                    if (userInput.equals("-remove")) {
                        System.out.print("Skriv en vareID du ønsker å fjerne fra: ");
                        String input = getInput();
                        userInt = Integer.parseInt(input);
                    }
                    else {
                        userInt = Integer.parseInt(String.join("", userInput.split("\\D")));
                    }

                    final int finalUserInt = userInt;
                    final Grocery grocery = fridge.getGroceryList().stream()
                            .filter(g -> g.getGroceryID() == finalUserInt)
                            .findFirst()
                            .orElse(null);

                    System.out.print("          Skriv en mengde som skal fjernes fra varen (f.eks 2 gram / desiliter / stykker): ");
                    fetchRemoveAmountAndUnit(grocery);
                    retry = false;
                }
                catch(NumberFormatException e) {
                    throw new NumberFormatException("Cannot remove from non-digit groceryID. Please enter a valid GroceryID");
                }
                catch(Exception e) {
                    throw new Exception("Unexpected error: " + e.getMessage());
                }
            }
        }
        else {
            str.append(" - Ugyldig kommando. Skriv enten \"-e\" eller \"-remove\".");
        }
        return true;
    }

    private void fetchRemoveAmountAndUnit(Grocery g) {
        boolean retry = true;
        final GroceryManager gm = new GroceryManager(g);
        while (retry) {
            try{
                System.out.print("          Skriv en mengde som skal fjernes fra varen (f.eks 2 gram / desiliter / stykker): ");
                String staticInput = getInputStatic();

                gm.removeAmountGrocery(GroceryManager.getAmountAndUnit(staticInput));
                retry = false;
            }
            catch(Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void displayFridgeList() {
        str = new StringBuilder();
        Display display = new Display(fm);
        str.append(Table.createMenuTable("KJØLESKAP", "Her er en overikt over ulike varer i kjøleskapet sortert etter dato:"));

        if (fridge.getGroceryList().isEmpty()) {
            str.append("            ---- Kjøleskapet er tomt ----\n\n");
        }
        else {
            str.append(display.dateList("Utgåtte varer", fm.getExpiredList(), "Ingen varer er gått ut på dato"));
            str.append(display.dateList("Varer som holder på å gå ut på dato", fm.getNearExpList(), "Ingen varer er nær ved å gå ut på dato"));
            str.append(display.dateList("Resterende varer", fm.getRestGroceryList(), "Ingen resterende varer")).append("\n\n");
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
    }

    public void displayFridge() {
        displayFridgeList();
        commands();
    }

    /**
     *
     */
    public void showValue() {
        str = new StringBuilder();

        str.append(Table.createMenuTable("SAMLET PRIS PÅ VARER", "Under er en oversikt over total prissum på varer:"));

        final Display display = new Display(fm);
        str.append(display.displayPrice(fridge.getGroceryList(), "Prisoversikt", "Vare", "Pris på mengde"));
        System.out.println(str);

        String userInput = "";
        while (!userInput.equals("-e")) {
            System.out.println("Skriv \"-e\" for å gå tilbake til menyen");
            userInput = getInput();
        }
    }

    /**
     * Search the {@code Grocery}-objects in the fridge. The {@code Grocery} that is being searched after
     * is determined by an usergiven input, and the {@code name} of the Grocery. In other words, the
     * user can search for Groceries by their names.
     */
    public void showSearchFridge() {
        clearScreen();
        if (fridge.getGroceryList().isEmpty()) {
            System.out.println("            ---- Ingen varer i kjøleskapet, kan ikke søke etter varer ----");
        }
        else {
            str = new StringBuilder();
            str.append(Table.createMenuTable("VARESØK", "Søk på navnet til en vare i kjøleskapet. Skriv \"-e\" for å gå tilbake til hovedmenyen:"));
            str.append("Kommando log:\n");
            search();
        }
    }

    private void search () {
        String userInput;

        while (true) {
            clearScreen();
            System.out.println(str);

            userInput = getInput();

            String finalUserInput = userInput;
            int countMax = (int)fridge.getGroceryList().stream()
                    .map(Grocery::getName)
                    .filter(name -> name.equalsIgnoreCase(finalUserInput))
                    .count();

            if (userInput.equals("-e")) {
                break;
            }
            else if(countMax == 0) {
                str.append("Varen \"").append(userInput).append("\" finnes ikke i kjøleskapet. Pass på å skrive varen navnet på riktig.\n");
            }
            else {
                searchHandler(countMax, finalUserInput);
            }
        }
    }

    private void searchHandler(final int count, final String input) {
        clearScreen();

        if (count == 1) {
            Grocery searchedGrocery = fridge.getGroceryList().stream()
                    .filter(g -> g.getName().equals(input))
                    .findFirst()
                    .orElse(null);

            changeGrocery(searchedGrocery);
        }
        else {
            List<Grocery> searchedGroceryList = fridge.getGroceryList().stream()
                    .filter(g -> g.getName().equals(input))
                    .toList();

            System.out.println("        Velg en av varene i listen ved å skrive en vareID:");
            System.out.println(Display.displaySearchList(searchedGroceryList));

            final String userInput = getInput();
            Grocery chosenGrocery = fridge.getGroceryList().stream()
                    .filter(g -> g.getGroceryID() == Integer.parseInt(userInput))
                    .findFirst()
                    .orElse(null);

            if (chosenGrocery != null) {
                changeGrocery(chosenGrocery);
            }
            else {
                str.append("Ugyldig input: \"").append(input).append("\".\n");
            }
        }

    }

    private void displayChangeList(Grocery g) {
        str = new StringBuilder();
        Display display = new Display(fm);
        str.append(Table.createMenuTable("ENDRE VARE", "Velg en handling i listen under:"));
        str.append(display.displayGrocery(g));

        str.append("           [1] Legg til en mengde til varen.\n");
        str.append("           [2] Trekk fra en mengde fra varen.\n");
        str.append("           [3] Sjekk om varen har gått ut på dato.\n\n");
        str.append("Command log:\n");
    }

    /**
     * @param grocery Instanse av klassen {@link Grocery}. Parameteren {@code grocery}
     *                er matvare-objektet som skal endres med metoden.
     */
    private void changeGrocery(Grocery grocery) {
        displayChangeList(grocery);
        while (true) {
            clearScreen();

            System.out.println(str);
            System.out.println("""
                    Skriv "-e" for å gå tilbake til menyen, eller "tall" for å endre på en vare.
                    "tall" skal skrives som et heltall i intervallet [1,3].
                    
                    """);

            String userInput = getInput();

            if (userInput.equals("-e")) {
                break;
            }
            else if (Integer.parseInt(userInput) >= 1 || Integer.parseInt(userInput) <= 3) {
                changeOptions(grocery, fridge, userInput, this);
                displayChangeList(grocery);
            }
            else {
                str.append(" - Ugyldig kommando: \"").append(userInput).append("\"\n");
            }
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

    @Override
    protected void changeOptions(Grocery g, Fridge f, String str, UserInterface UI) {
        super.changeOptions(g, f, str, this);
    }

    public void finish() {
        char yesNoErr = 'e';
        while (yesNoErr == 'e') {
            yesNoErr = option("Vil du virkelig avslutte programmet?");
        }

        if (yesNoErr == 'y') {
            super.close();
            running = false;
        }
    }

    /**
     * .
     */
    private void commands() {
        boolean escape = false;
        str.append("Command log:\n");
        while (!escape) {
            clearScreen();
            System.out.println(str);
            //sjekker hvilke komandoer vi har tilgang til
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

            String userInput = getInput();

            //behandler brukerinput
            try {
                escape = userInputHandler(userInput);
            }
            catch (Exception e) {
                str.append(e.getMessage());
            }
        }
    }

    private boolean userInputHandler(String userInput) throws Exception {
        if (userInput.equals("-e")) {
            return true;
        }
        else if (userInput.equals("-delete") && !fm.getExpiredList().isEmpty()) {
            try {
                deleteItems();
            }
            catch(Exception e) {
                throw new Exception(e.getMessage());
            }
            displayFridgeList();
        }
        else if (userInput.equals("-delete all") && !fm.getExpiredList().isEmpty()) {
            for(Grocery expiredItem : fm.getExpiredList()) {
                fridge.removeGrocery(expiredItem);
            }
            System.out.println("Alle datovarer er nå fjernet!\n");
            displayFridgeList();
        }
        else if (userInput.contains("-change")) {
            changeHandler(userInput);
            return true;
        }
        else if (fridge.getGroceryList().isEmpty() && userInput.contains("-delete")) {
            throw new IllegalArgumentException(" - Det finnes ingen datovarer. "
                    + "Kan ikke fjerne datovarer fra kjøleskapet.\n");
        }
        else {
            throw new Exception(" - Ugyldig kommando. \"" + userInput + "\"\n");
        }
        return false;
    }

    private void changeHandler(String userInput) {
        final int userInt;
        if (userInput.equals("-change")) {
            System.out.println("Skriv en vareID du ønsker å endre på:");
            userInput = getInput();
            userInt = Integer.parseInt(userInput);
        }
        else {
            userInt = Integer.parseInt(String.join("", userInput.split("\\D")));
        }
        final int userIndex = fm.getGroceryListIndex(userInt);

        if (userIndex >= 0 && userIndex < fridge.getGroceryList().size()) {
            changeGrocery(fm.getGrocery(userIndex));
            str.append(" - Endret vare ").append(userIndex+1).append("\n");
        }
        else {
            str.append(" - Ugyldig vareID. Skriv en annen vareID\n");
        }
    }

    /**
     *
     */
    private void deleteItems() throws Exception {
        try {
            clearScreen();
            System.out.println(Table.createMenuTable("FJERN VARE","Skriv inn en vareID fra listen ovenfor for å fjerne en vare fra kjøleskapet.\n"
                                        + "             Skriv flere vareID-er separert av \",\"(comma) for å fjerne flere varer fra kjøleskapet."));
            Display display = new Display(fm);
            System.out.println(display.dateList("Liste over varer i kjøleskapet", fridge.getGroceryList(), "Ingen tilgjengelige varer."));

            String userInput = getInput();
            String[] deleteStrArr = userInput.replaceAll("\\s+", "").split(",");
            int[] deleteArr = Arrays.stream(deleteStrArr).mapToInt(Integer::parseInt).toArray();

            for (int groceryID : deleteArr) {
                Grocery removableGrocery = fridge.getGroceryList().stream()
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

            str.append("Alle gyldige valgte datovarer er nå fjernet!\n");
        }
        catch (Exception e) {
            throw new Exception("Unexpected error: " + e.getMessage());
        }
    }
}
