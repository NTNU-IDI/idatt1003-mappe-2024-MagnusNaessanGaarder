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

    public SI getUnit() {
        return unit;
    }

    public double convertUnit () {
        if (this.quantity < 1.0 && this.unit.getPrefix().isEmpty()) {
            this.unit = new SI("Desiliter", "dL","L", "Desi");
            this.quantity *= 10;
        }
        else if (this.quantity >= 10.0 && this.unit.getPrefix().equals("Desi")) {
            this.unit = new SI("Liter", "L", "L","Desi");
            this.quantity /= 10;
        }
        else if (this.quantity < 1.0 && this.unit.getPrefix().equals("Desi")) {
            this.unit = new SI("Milliliter", "mL", "L","Milli");
            this.quantity *= 100;
        }
        else if (this.quantity >= 100 && this.unit.getPrefix().equals("Milli")) {
            this.unit = new SI("Desiliter", "dL", "L","Desi");
            this.quantity /= 100;
        }
        else if (this.quantity < 1.0 && this.unit.getPrefix().equals("Kilo")) {
            this.unit = new SI("Gram", "g", "kg", "");
            this.quantity *= 1000;
        }
        else if (this.quantity >= 1000.0 && this.unit.getPrefix().equals("Gram")) {
            this.unit = new SI("Kilogram", "kg", "kg", "Kilo");
            this.quantity /= 1000;
        }

        return quantity;
    }

    public void addAmount(final double amount, final SI amountUnit) {
        if (amount > 0) {
            if (this.unit.getAbrev().equals("stk") && amountUnit.getAbrev().equals("stk")) {
                this.quantity += amount;
            }
            else if (this.unit.getAbrev().equals("stk") || amountUnit.getAbrev().equals("stk")) {
                System.err.println("Kan ikke legge til et antall med en annen målenhet enn \"stk\" når varen er oppgitt i \"stk\".");
            }
            else {
                this.quantity =  (double)(Math.round((this.quantity*unit.getConvertionFactor()+amount*amountUnit.getConvertionFactor())*100))/100;
                convertUnit();
            }
        }
        else {
            throw new IllegalArgumentException("Illegal argument error: Cannot add a negative amount.");
        }
    }

    public double removeAmount(final double amount, SI amountUnit) {
        if (amount > 0) {
            if (this.unit.getAbrev().equals("stk") && amountUnit.getAbrev().equals("stk")) {
                this.quantity -= amount;
            }
            else if (this.unit.getAbrev().equals("stk") || amountUnit.getAbrev().equals("stk")) {
                System.err.println("Kan ikke trekke fra et antall med en annen målenhet enn \"stk\" når varen er oppgitt i \"stk\".");
            }
            else {
                this.quantity = (double)(Math.round((this.quantity*unit.getConvertionFactor()-amount*amountUnit.getConvertionFactor())*100))/100;
                convertUnit();
            }

            if (this.quantity <= 0) {
                this.fridge.removeGrocery(this);
                return 0;
            }
            return quantity;
        }
        else {
            throw new IllegalArgumentException("Illegal argument error: Cannot remove a negative amount.");
        }
    }

    public double getPricePerQuantity() {
        double result;
        if (unit.getAbrev().equals(unit.getUnitForPrice())) {
            result = quantity * price;
        }
        else {
            result = price * (quantity*unit.getConvertionFactor());
        }

        return result;
    }

    public boolean hasExpired() {
        return this.bestBefore.isBefore(LocalDate.now());
    }
}
