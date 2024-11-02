package edu.ntnu.idi.idatt;

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

import org.w3c.dom.ls.LSOutput;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Client {

    public static Scanner myReader = new Scanner(System.in);
    public static StringBuilder str = new StringBuilder();
    public static boolean running = true;

    public static Fridge fridge = new Fridge();

    public static String getInput() {
        String data = "";
        while (myReader.hasNextLine()) {
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
            case 1 -> {
                //legg til

            }
            case 2 -> {
                //Fjern

            }
            case 3 -> {
                //Oversikt over kjøleskapet
                displayFridge();
            }
            case 4 -> {
                //Oversikt over datovarer
                displayByDate();
            }
            case 5 -> {
                //Samlet verdi av varer
            }
            case 6 -> {
                finish();
            }
            default -> {
                clearScreen();
            }
        }
    }

    public static void displayList(List<Grocery> list) {
        for (int i = 0; i < list.size(); i++) {
            Table groceryTable = new Table(list.get(i).getName(),
                    new String[]{"VareID", "Mengde","Pris","Dato"},
                    new String[]{
                            Integer.toString(i+1),
                            list.get(i).getQuantity()+ " " + list.get(i).getUnit().getAbrev(),
                            list.get(i).getPriceToStr(),
                            list.get(i).getDateToStr()
                    }
            );
            str.append(groceryTable.createTable());
        }
    }

    public static void finish() {
        while (true) {
            System.out.println("Vil du virkelig avslutte programmet? Skriv \"y\" for JA eller \"n\" for NEI.");
            String userInput = getInput();
            if(userInput.equalsIgnoreCase("y")) {
                running = false;
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
        String topBar = "-------------------------------------------------------------------------------------------------------------------------\n";
        str.append(topBar).append("                                                       DATOVARER\n").append(topBar);
        str.append("                                  Her er en liste med datovarer i kjøleskapet:\n\n");

        if (fridge.getExpiredList().isEmpty()) {
            str.append("            ---- Ingen varer er gått ut på dato i kjøleskapet ----\n\n");
        }
        else {
            displayList(fridge.getExpiredList());
        }

        if (fridge.getNearExpList().isEmpty()) {
            str.append("            ---- Ingen varer er i ferd med å gå ut på dato i kjøleskapet ----\n\n");
        }
        else {
            displayList(fridge.getNearExpList());
        }

        if (fridge.getRestGroceryList().isEmpty()) {
            str.append("            ---- Ingen varer med lenger holdbarhetstid i kjøleskapet ----\n\n");
        }
        else {
            displayList(fridge.getRestGroceryList());
        }

        if (!fridge.getGroceryList().isEmpty()) {
            String[] colTitle = new String[fridge.getGroceryList().size() + 1];
            colTitle[0] = "Vare";

            String[] colData = new String[fridge.getGroceryList().size() + 1];
            colData[0] = "Pris";

            for (int i = 1; i < fridge.getExpiredList().size(); i++) {
                colTitle[i] = fridge.getExpiredList().get(i).getName();
                colData[i] = fridge.getExpiredList().get(i).getPriceToStr();
            }

            Table groceryTable = new Table("Total pengetap av datovarer",
                    colTitle,
                    colData
            );
            str.append(groceryTable.createTable());
            str.append(fridge.getMoneyLoss());
        }

        System.out.println(str);


        while (true) {
            str = new StringBuilder();

            if (fridge.getExpiredList().isEmpty()) {
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
            else if (userInput.equals("-delete") && !fridge.getExpiredList().isEmpty()) {
                try {
                    clearScreen();
                    displayList(fridge.getExpiredList());
                    System.out.println("Skriv inn en vareID fra listen ovenfor for å fjerne en vare fra kjøleskapet." +
                            "Skriv flere vareID-er separert av \",\"(comma) for å fjerne flere varer fra kjøleskapet.");

                    userInput = getInput();
                    String[] deleteStrArr = userInput.replaceAll("\\s+","").split(",");
                    int[] deleteArr = Arrays.stream(deleteStrArr).mapToInt(Integer::parseInt).toArray();

                    for (int groceryIndex : deleteArr) {
                        Grocery removableGrocery = fridge.getExpiredList().get(groceryIndex - 1);
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
            else if (userInput.equals("-delete all") && !fridge.getExpiredList().isEmpty()) {
                clearScreen();
                for(Grocery expiredItem : fridge.getExpiredList()) {
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
        String topBar = "-------------------------------------------------------------------------------------------------------------------------\n";
        str.append(topBar).append("                                                       KJØLESKAP\n").append(topBar);
        str.append("                                  Her er en liste med tilgjengelige varer i kjøleskapet:\n\n");

        if (fridge.getGroceryList().isEmpty()) {
            str.append("            ---- Ingen varer er lagt til i kjøleskapet ----");
        }
        else {
            for (int i = 0; i < fridge.getGroceryList().size(); i++) {
                Table groceryTable = new Table(fridge.getGrocery(i).getName(),
                        new String[]{"VareID", "Mengde","Pris","Dato"},
                        new String[]{
                                Integer.toString(i+1),
                                fridge.getGrocery(i).getQuantity()+ " " + fridge.getGrocery(i).getUnit().getAbrev(),
                                fridge.getGrocery(i).getPriceToStr(),
                                fridge.getGrocery(i).getDateToStr()
                        }
                );
                str.append(groceryTable.createTable());
            }
        }
        System.out.println(str);

        System.out.println("Skriv \"-e\" for å gå tilbake til menyen, eller \"-change [Vare ID]\" for å endre på en vare.\n"
        + "[Vare ID] skal skrives som et tall.");
        String userInput = getInput();
        while (true) {
            if (userInput.equals("-e")) {
                break;
            }
            else if (userInput.contains("-change")) {
                int userIndex = Integer.parseInt(String.join("", userInput.split("-change"))) - 1;

                if (userIndex >= 0 && userIndex < fridge.getGroceryList().size()) {
                    changeGrocery(userIndex);
                    break;
                }
            }
        }
    }

    public static void changeGrocery(int index) {
        clearScreen();
        final Grocery grocery = fridge.getGrocery(index);

        str = new StringBuilder();
        String topBar = "-------------------------------------------------------------------------------------------------------------------------\n";
        str.append(topBar).append("                                                       ENDRE VARE\n").append(topBar).append("\n");

        Table groceryTable = new Table(grocery.getName(),
                new String[]{"Mengde","Pris","Dato"},
                new String[]{grocery.getQuantity() + " " + grocery.getUnit().getAbrev(), grocery.getPriceToStr(),grocery.getDateToStr()}
        );

        str.append(groceryTable.createTable());
        str.append("\n\n");

        str.append("                                  Velg en handling i listen under:\n\n");

        str.append("           [1] Legg til en mengde til varen.");
        str.append("           [2] Trekk fra en mengde fra varen.");
        str.append("           [3] Sjekk om varen har gått ut på dato.");
        System.out.println(str);

        System.out.println("Skriv \"-e\" for å gå tilbake til menyen, eller \"tall\" for å endre på en vare.\n"
                + "\"tall\" skal skrives som et heltall i intervallet [1,3].");
        String userInput = "";
        do {
            userInput = getInput();
        }
        while (Integer.parseInt(userInput) < 1 || Integer.parseInt(userInput) > 3 || !userInput.equals("-e"));

        if (!userInput.equals("-e")) {
            switch (Integer.parseInt(userInput)) {
                case 1 -> {
                    //legg til en mengde
                    int amount = Integer.parseInt(str.toString());
                    SI unit = getUnit();

                    grocery.addAmount(amount, unit);
                }
                case 2 -> {
                    //trekk fra en mengde
                    int amount = Integer.parseInt(str.toString());
                    SI unit = getUnit();

                    grocery.removeAmount(amount, unit);
                }
                case 3 -> {
                    //sjekk om vare er gått ut på dato
                }
                default -> {
                    break;
                }
            }
        }
    }

    public static SI getUnit() {
        String unit, abrev, unitForPrice, prefix;
        unit = "";
        abrev = "";
        unitForPrice = "";
        prefix = "";

        String userInput = "";
        do {
            userInput = getInput();
            if (isValidUnit(userInput)) {
                unit = userInput;
                if (userInput.equalsIgnoreCase("Stykker")) {
                    prefix = "";
                    abrev = userInput.toLowerCase().charAt(0) + "stk";
                    unitForPrice = "stk";
                }
                else if (userInput.contains("gram")){
                    prefix = String.join("",userInput.split("gram"));
                    abrev = userInput.toLowerCase().charAt(0) + "g";
                    unitForPrice = "L";
                }
                else if (userInput.contains("liter")) {
                    prefix = String.join("",userInput.split("liter"));
                    abrev = userInput.toLowerCase().charAt(0) + "L";
                    unitForPrice = "L";
                }
                else {
                    prefix = "";
                    if (userInput.equals("Gram")) {
                        abrev = unitForPrice = "g";
                    }
                    else {
                        abrev = unitForPrice = "L";
                    }
                }
            }
        }
        while(isValidUnit(userInput));

        return new SI(unit,abrev,unitForPrice,prefix);
    }

    public static boolean isValidUnit(String userInput) {
        String prefix;
        if(!userInput.toLowerCase().contains("liter") && !userInput.toLowerCase().contains("gram") && !userInput.equalsIgnoreCase("Stykker")) {
            return false;
        }
        else if(userInput.toLowerCase().contains("liter")) {
            prefix = String.join("",userInput.toLowerCase().split("liter"));
        }
        else if(userInput.toLowerCase().contains("gram")) {
            prefix = String.join("",userInput.toLowerCase().split("gram"));
        }
        else {
            prefix = "";
        }

        for (String hashKey : SI.getSIPrefixes().keySet()) {
            if(hashKey.toLowerCase().equals(prefix)) {
                return true;
            }
        }
        return false;
    }

    public static void main(final String[] args) {
        str = new StringBuilder();

        while (running) {
            clearScreen();

            // Menyvalg i konsollen
            final String topBar = "-----------------------------------------------------------------------------------------------------------------------------------\n";

            str.append(topBar + "\n                                                         HOVEDMENY - MATLAGER\n\n" + topBar);
            str.append("           Bruk en kommando for å navigere. For å få en overikt over tilgjengelige kommandoer skriv \"-help\".\n");
            str.append("           Velg et alternativ under ved å skrive et tall:\n\n");

            str.append("                   [1] Legg til vare.\n");
            str.append("                   [2] Fjern vare.\n");
            str.append("                   [3] Oversikt over kjøleskapet.\n");
            str.append("                   [4] Oversikt over datovarer.\n");
            str.append("                   [5] Samlet verdi av varer.\n");
            str.append("                   [6] Avslutt.\n");
            System.out.println(str);

            int userInput = 0;
            do {
                userInput = Integer.parseInt(getInput());
                if (userInput < 0 || userInput > 6) {
                    System.err.println("Input er for stor eller for liten. Brukerinputen må være i intervallet [0,5].");
                }
            }
            while (userInput < 0 || userInput > 6);

            menuOption(userInput);

            // Legg til vare
            str = new StringBuilder();
            str.append(topBar + "                                                  LEGG TIL EN VARE I KJØLESKAPET\n" + topBar);
            str.append("           Skriv navnet på varen du ønsker å legge til:\n\n");
            str.append("           Varenavn: ");
            str.append("\n");

            // Legg til en måleenhet til varen
            str.append(topBar + "                                                         VELG ET MÅLEENHET\n" + topBar);
            str.append("           Velg menden av  under ved å skrive et tall:\n\n");

            str.append("                   [1] Stykker (stk)\n");
            str.append("                   [2] Gram (g)\n");
            str.append("                   [3] Kilo (kg)\n");
            str.append("                   [4] Desiliter (dl)\n");
            str.append("                   [5] Liter (l)\n");
            str.append("                   [6] Legg til måleenhet\n");

            // Legg til mengde
            str.append(topBar + "                                                   LEGG TIL EN MENGDE TIL VAREN\n" + topBar);
            str.append("           Skriv antallet {måleenhet} du øsnker å legge til av varen i kjøleskapet:\n\n");
            str.append("           Varenavn: ");

            // Best før dato
            str.append(topBar + "                                                           BEST FØR DATO\n" + topBar);
            str.append("           Skriv \"Best Før\"-datoen til varen. Skriv datoen på formen: DD.MM.ÅÅÅÅ\n\n");
            str.append("           Best-før dato: ");

            // Pris på vare
            str.append(topBar + "                                                           PRIS PÅ VARE\n" + topBar);
            str.append("           Skriv prisen på varen som et tall under. Prisen oppgis som pris per kg eller l.\n\n");
            str.append("           Pris: ");
        }

        myReader.close();
    }
}
