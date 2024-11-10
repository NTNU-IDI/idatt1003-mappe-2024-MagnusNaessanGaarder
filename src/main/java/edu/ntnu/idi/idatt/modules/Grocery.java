package edu.ntnu.idi.idatt.modules;

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

import edu.ntnu.idi.idatt.Utils.SI;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Grocery {
    private static int nextID = 0;
    private final int groceryID;
    private final String name;
    private SI unit;
    private double quantity;
    private final LocalDate bestBefore;
    private final double price;
    private final Fridge fridge;

    public Grocery (String name, SI measure, double quantity, LocalDate date, double price, Fridge fridge) {
        this.name = name;
        this.unit = measure;
        this.quantity = quantity;
        this.bestBefore = date;
        this.price = price;
        this.fridge = fridge;
        this.groceryID = advanceID();
    }

    private static int advanceID() {
        return ++nextID;
    }

    public int getGroceryID() {
        return this.groceryID;
    }

    public Fridge getFridge() {
        return fridge;
    }

    public String getName() {
        return name;
    }

    public String getDateToStr() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");
        return formatter.format(bestBefore);
    }
    public LocalDate getDate() {
        return bestBefore;
    }

    public String getPriceToStr() {
        return String.format("%.2f kr/%s", this.price, this.unit.getAbrev());
    }

    public double getPrice() {
        return price;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity){
        this.quantity = quantity;
    }

    public SI getUnit() {
        return unit;
    }
    public void setUnit(SI unit) {
        this.unit = unit;
    }

    public boolean hasExpired() {
        return this.bestBefore.isBefore(LocalDate.now());
    }
}
