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

import edu.ntnu.idi.idatt.modules.Display;
import edu.ntnu.idi.idatt.Manager.FridgeManager;
import edu.ntnu.idi.idatt.Manager.GroceryManager;
import edu.ntnu.idi.idatt.Manager.SI_manager;
import edu.ntnu.idi.idatt.Utils.SI;
import edu.ntnu.idi.idatt.modules.Fridge;
import edu.ntnu.idi.idatt.modules.Grocery;
import edu.ntnu.idi.idatt.modules.Table;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.Arrays;

public class Client {

    final private static Scanner myReader = new Scanner(System.in);
    private static StringBuilder str = new StringBuilder();
    private static boolean running = true;

    final private static Fridge fridge = new Fridge();
    final private static FridgeManager fm = new FridgeManager(fridge);

    public static String getInput() {
        String data = "";
        if (myReader.hasNextLine()) {
            data = myReader.nextLine();
            return data;
        }
        return data;
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void menuOption(int userInput) {
        clearScreen();
        switch (userInput) {
            //legg til
            case 1 -> addToFridge();

            //Fjern
            case 2 -> removeFromFridge();

            //Oversikt over kjøleskapet
            case 3 -> displayFridge();

            //Oversikt over datovarer
            case 4 -> displayByDate();

            //Samlet verdi av varer
            case 5 -> displayValue();

            //avslutt program
            case 6 -> finish();

            default -> System.err.println("Input er ugyldig. Brukerinputen må være i intervallet [0,5].");
        }
    }

    public static void displayValue () {
        str = new StringBuilder();
        final Display display = new Display("SAMLET PRIS PÅ VARER", "Under er en oversikt over total prissum på varer:", fridge);
        str.append(display.getTitle());
        str.append(display.displayPrice(fridge.getGroceryList(),"Prisoversikt","Vare","Pris på mengde"));
        System.out.println(str);

        System.out.println("Skriv \"-e\" for å gå tilbake til menyen");
        while (true) {
            String userInput = getInput();
            if (userInput.equals("-e")) {
                break;
            }
        }
    }

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
        System.out.printf("          Skriv pris. Oppgi pris i kr/%s f.eks (20 kr/%s): ", measure.getUnitForPrice(), measure.getUnitForPrice());
        userInput = getInput();
        String priceStr = String.join("", userInput.split("[^,.\\d]"));
        price = Double.parseDouble(String.join(".", priceStr.split("[,.]")));

        //sjekker om brukeren ønsker å legge til varen
        clearScreen();
        str = new StringBuilder();
        Table table = new Table(
                name,
                new String[]{"Mengde","Best-før dato","pris"},
                new String[]{quantity +  " " + measure.getAbrev(), date + "", price + " kr/" + measure.getUnitForPrice()}
        );
        str.append(table.createTable());

        while(true) {
            System.out.println(str);
            System.out.println("Er du sikker på at du ønsker å legge til denne varen til kjøleskapet? Skriv \"y\" for JA eller \"n\" for NEI.");
            userInput = getInput();
            if (userInput.equalsIgnoreCase("y")) {
                //sjekker om varen allerede er i kjøleskapet
                Grocery grocery = new Grocery(name,measure,quantity,date,price,fridge);
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

    public static void displayByDate() {

        str = new StringBuilder();
        Display display = new Display("DATOVARER", "Her er en liste med datovarer i kjøleskapet:", fridge);
        str.append(display.getTitle());

        str.append("Utgåtte varer:");
        str.append(display.list(fm.getExpiredList(), "Ingen varer er gått ut på dato"));
        str.append("Varer som holder på å gå ut på dato:");
        str.append(display.list(fm.getNearExpList(), "Ingen varer er nær ved å gå ut på dato"));
        str.append("Resterende varer:");
        str.append(display.list(fm.getRestGroceryList(), "Ingen resterende varer"));

        if (!fridge.getGroceryList().isEmpty()) {
            str.append(display.displayPriceUnique(fm.getExpiredList(),"Total pengetap på datovarer", "Vare", "Pris beregnet på mengde"));
            str.append(fm.getMoneyLoss());
        }

        System.out.println(str);


        while (true) {
            str = new StringBuilder();

            if (fm.getExpiredList().isEmpty()) {
                System.out.println("Skriv \"-e\" for å gå tilbake til menyen");
            }
            else {
                System.out.println("Skriv \"-e\" for å gå tilbake til menyen, eller \"-delete\" for å slette en vare.\n"
                        + "Skriv \"-delete all\" for å slette alle utgåtte varer.");
            }

            String userInput = getInput();
            if (userInput.equals("-e")) {
                break;
            }
            else if (userInput.equals("-delete") && !fm.getExpiredList().isEmpty()) {
                try {
                    clearScreen();
                    Display.displayList(fm.getExpiredList());
                    System.out.println("Skriv inn en vareID fra listen ovenfor for å fjerne en vare fra kjøleskapet." +
                            "Skriv flere vareID-er separert av \",\"(comma) for å fjerne flere varer fra kjøleskapet.");

                    userInput = getInput();
                    String[] deleteStrArr = userInput.replaceAll("\\s+","").split(",");
                    int[] deleteArr = Arrays.stream(deleteStrArr).mapToInt(Integer::parseInt).toArray();

                    for (int groceryIndex : deleteArr) {
                        Grocery removableGrocery = fm.getExpiredList().get(groceryIndex - 1);
                        if (fridge.getGroceryList().contains(removableGrocery)) {
                            fridge.removeGrocery(removableGrocery);
                        }
                        else {
                            System.err.println("Kunne ikke fjerne "  + removableGrocery.getName() + " fra kjøleskapet.");
                        }
                    }

                    System.out.println("Alle gyldige valgte datovarer er nå fjernet!\n");
                }
                catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
            else if (userInput.equals("-delete all") && !fm.getExpiredList().isEmpty()) {
                clearScreen();
                for(Grocery expiredItem : fm.getExpiredList()) {
                    fridge.removeGrocery(expiredItem);
                }
                System.out.println("Alle datovarer er nå fjernet!\n");
            }
            else if (fridge.getGroceryList().isEmpty() && userInput.contains("-delete")) {
                clearScreen();
                System.err.println("Det finnes ingen datovarer. Kan ikke fjerne datovarer fra kjøleskapet.");
            }
            else {
                throw new IllegalArgumentException("Ugyldig kommando. \"\"");
            }
        }
    }

    public static void displayFridge() {
        str = new StringBuilder();
        Display display = new Display("KJØLESKAP","Her er en liste med tilgjengelige varer i kjøleskapet:", fridge);

        str.append(display.getTitle());
        str.append(display.list(fridge.getGroceryList(), "Fant ingen varer i kjøleskapet"));
        System.out.println(str);

        while (true) {
            System.out.println("Skriv \"-e\" for å gå tilbake til menyen, eller \"-change [Vare ID]\" for å endre på en vare.\n"
                               + "[Vare ID] skal skrives som et tall.");
            String userInput = getInput();
            int userIndex = -1;

            if (userInput.equals("-e")) {
                break;
            }
            else if (userInput.equals("-change")) {
                System.out.println("Skriv en vareID du ønsker å fjerne fra:");
                userInput = getInput();
                int userInt = Integer.parseInt(userInput);
                userIndex = fm.getGroceryListIndex(userInt);
            }
            else if (userInput.contains("-change")) {
                int userInt = Integer.parseInt(String.join("", userInput.split("[^0-9]")));
                userIndex = fm.getGroceryListIndex(userInt);
            }
            else {
                System.err.println("Ugyldig kommando. Skriv enten \"-e\" eller \"-change\".");
            }

            if (userIndex >= 0 && userIndex < fridge.getGroceryList().size()) {
                clearScreen();
                changeGrocery(fm.getGrocery(userIndex));
                System.out.println("Endret vare " + (userIndex+1));
            }
            else {
                clearScreen();
                System.err.println("Ugyldig vareID");
            }
        }
    }

    public static void changeGrocery(Grocery grocery) {
        clearScreen();

        str = new StringBuilder();
        Display display = new Display("ENDRE VARE","Velg en handling i listen under:", fridge);
        str.append(display.getTitle());
        str.append(display.grocery(grocery));

        str.append("           [1] Legg til en mengde til varen.\n");
        str.append("           [2] Trekk fra en mengde fra varen.\n");
        str.append("           [3] Sjekk om varen har gått ut på dato.\n");
        System.out.println(str);


        String userInput;
        do {
            System.out.println("Skriv \"-e\" for å gå tilbake til menyen, eller \"tall\" for å endre på en vare.\n"
                    + "\"tall\" skal skrives som et heltall i intervallet [1,3].");
            userInput = getInput();
            final GroceryManager gm = new GroceryManager(grocery);

            if (userInput.equals("-e")) {
                break;
            }
            switch (Integer.parseInt(userInput)) {
                //legg til mengde til varen
                case 1 -> gm.addAmountGrocery();

                //legg til mengde til varen
                case 2 -> gm.removeAmountGrocery();
                case 3 -> {
                    //sjekk om en vare er gått ut på dato
                    try {
                        if (grocery.hasExpired()) {
                            System.out.println("Varen har gått ut på dato");

                            while (true) {
                                System.out.println("Ønsker du å slette varen? Skriv \"y\" for JA og \"n\" for NEI.");
                                userInput = getInput();
                                if (userInput.equalsIgnoreCase("y")) {
                                    fridge.removeGrocery(grocery);
                                    break;
                                } else if (userInput.equalsIgnoreCase("n")) {
                                    break;
                                } else {
                                    System.err.println("Ugyldig input. Skriv enten \"y\" eller \"n\".");
                                }
                            }
                        } else {
                            System.out.println("Varen har ikke gått ut på dato");
                        }
                    } catch (Exception e) {
                        System.err.println(e.getMessage());
                    }
                }
                default -> System.err.println("Ugyldig input. ");
            }
        }
        while (Integer.parseInt(userInput) < 1 || Integer.parseInt(userInput) > 3);

    }

    public static void main(final String[] args) {

        while (running) {

            // Menyvalg i konsollen
            clearScreen();
            str = new StringBuilder();

            final String menuStr = Table.createMenuTable("HOVEDMENY - MATLAGER","Velg et alternativ under ved å skrive et tall:");
            str.append(menuStr);
            str.append("                   [1] Legg til vare.\n");
            str.append("                   [2] Fjern vare.\n");
            str.append("                   [3] Oversikt over kjøleskapet.\n");
            str.append("                   [4] Oversikt over datovarer.\n");
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
