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

import java.util.Scanner;

public class Client {

    public static Scanner myReader = new Scanner(System.in);

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

    public static void mainOption(int userInput) {
        switch (userInput) {
            case 1 -> {}
            case 2 -> {}
            case 3 -> {}
            case 4 -> {}
            case 5 -> {}
            default -> {
                clearScreen();
            }
        }
    }


    public static void main(final String[] args) {
        StringBuilder str = new StringBuilder();
        boolean running = true;


        while (running) {
            clearScreen();

            // Menyvalg i konsollen
            final String topBar = "-----------------------------------------------------------------------------------------------------------------------------------\n";

            str.append(topBar + "\n                                                         HOVEDMENY - MATLAGER\n\n" + topBar);
            str.append("           Bruk en kommando for å navigere. For å få en overikt over tilgjengelige kommandoer skriv \"-help\".\n");
            str.append("           Velg et alternativ under ved å skrive et tall:\n\n");

            str.append("                   [1] Oversikt over kjøleskapet.\n");
            str.append("                   [2] Oversikt over datovarer.\n");
            str.append("                   [3] Legg til vare.\n");
            str.append("                   [4] Fjern vare.\n");
            str.append("                   [5] Samlet verdi av varer.\n");
            System.out.println(str);

            int userInput = 0;
            do {
                userInput = Integer.parseInt(getInput());
                if (userInput < 0 || userInput > 5) {
                    System.err.println("Input er for stor eller for liten. Brukerinputen må være i intervallet [0,5].");
                }
            }
            while (userInput < 0 || userInput > 5);

            //menuOption();

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
