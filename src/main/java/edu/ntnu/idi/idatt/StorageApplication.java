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

import edu.ntnu.idi.idatt.modules.UserInterface;

/**
 * <strong>Description</strong><br>
 * The main client class for the application.<br><br>
 * <p>
 * {@link #main} - Main method to start the application.
 */
public class StorageApplication {

  /**
   * <strong>Description</strong><br>
   * The main method of the application. This method always initiates every time the program runs
   * and starts the application.
   */
  public static void main(final String[] args) {
    final UserInterface UI = new UserInterface();

    UI.init();
    UI.start();
  }
}
