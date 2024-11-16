package edu.ntnu.idi.idatt.Modules;

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

    public Grocery(String name, SI measure, double quantity, LocalDate date, double price, Fridge fridge) {
        this.name = name;
        this.unit = measure;
        this.quantity = quantity;
        this.bestBefore = date;
        this.price = price;
        this.fridge = fridge;
        this.groceryID = advanceID();

        convertUnit();
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
        return String.format("%.2f kr/%s", this.price, this.unit.getUnitForPrice());
    }

    public double getPrice() {
        return price;
    }

    public double getQuantity() {
        return quantity;
    }

    private void setQuantity(double quantity){
        this.quantity = quantity;
    }

    public SI getUnit() {
        return unit;
    }

    private void setUnit(SI unit) {
        this.unit = unit;
    }

    public boolean hasExpired() {
        return this.bestBefore.isBefore(LocalDate.now());
    }

    public void addAmount(final double amount, final SI amountUnit) {
        final double currentQuantity = this.quantity;
        final String groceryUnitAbrev = this.unit.getAbrev();
        final String amountUnitAbrev = amountUnit.getAbrev();
        final double amount_cf = amountUnit.getConvertionFactor();
        final double grocery_cf = this.unit.getConvertionFactor();

        if (amount > 0) {
            if (groceryUnitAbrev.equals("stk") || amountUnitAbrev.equals("stk")) {
                System.out.println("Kan ikke legge til et antall med en annen målenhet enn \"stk\" når varen er oppgitt i \"stk\".");
            }
            else {
                if (groceryUnitAbrev.equals("kg")) {
                    this.setQuantity(
                            (double) (Math.round(((currentQuantity * grocery_cf + amount * amount_cf) / grocery_cf) * 100)) / 100
                    );
                }
                else {
                    this.setQuantity(
                            (double) (Math.round((currentQuantity * grocery_cf + amount * amount_cf) * 100)) / 100
                    );
                }
                convertUnit();
                System.out.println("La til " + amount + " " + amountUnitAbrev + " til varen " + this.name);

            }

        }
        else {
            throw new IllegalArgumentException("Illegal argument error: Cannot add a negative amount.");
        }
    }


    public void removeAmount(final double amount, SI amountUnit) {
        double currentQuantity = this.quantity;
        final String groceryUnitAbrev = this.unit.getAbrev();
        final String amountUnitAbrev = amountUnit.getAbrev();
        final double amount_cf = amountUnit.getConvertionFactor();
        final double grocery_cf = this.unit.getConvertionFactor();

        if (amount > 0) {
            //XOR for forkortelse av enhetene. Hvis begge enhetene ikke samsvarer samsvarer med hverandre og en av dem er oppgitt i stykker, kjører denne.
            if ((groceryUnitAbrev.equals("stk") && !amountUnitAbrev.equals("stk")) || (!groceryUnitAbrev.equals("stk") && amountUnitAbrev.equals("stk"))) {
                System.err.println("Kan ikke trekke fra et antall med en annen målenhet enn \"stk\" når varen er oppgitt i \"stk\".");
                return;
            }
            else {
                if (groceryUnitAbrev.equals("kg")) {
                    this.setQuantity(
                            (double) (Math.round(((currentQuantity * grocery_cf - amount * amount_cf) / grocery_cf) * 100)) / 100
                    );
                }
                else {
                    this.setQuantity(
                            (double) (Math.round((currentQuantity * grocery_cf - amount * amount_cf) * 100)) / 100
                    );
                }
                System.out.println("Fjernet " + amount + " " + amountUnitAbrev + " fra varen " + this.name);
                currentQuantity = this.quantity;
            }
            convertUnit();

            if (currentQuantity <= 0) {
                fridge.removeGrocery(this);
                System.out.println("Fjernet vare med vareID " + this.groceryID);
            }
        }
        else {
            throw new IllegalArgumentException("Illegal argument error: Cannot remove a negative amount.");
        }
    }

    private void convertUnit() {
        final double groceryQuantity = this.quantity;
        final String groceryUnit = this.unit.getPrefix();

        if (groceryQuantity < 1.0 && groceryUnit.isEmpty()) {
            if (unit.getAbrev().equalsIgnoreCase("L")) {
                this.setUnit(new SI("Desiliter", "dL", "L", "Desi"));
                this.setQuantity(groceryQuantity * 10);
            }
        }
        else if (groceryQuantity >= 1000.0 && groceryUnit.isEmpty()) {
            if (unit.getAbrev().equalsIgnoreCase("g")) {
                this.setUnit(new SI("Kilogram", "kg", "kg", "Kilo"));
                this.setQuantity(groceryQuantity / 1000);
            }
        }
        else if (groceryQuantity < 1.0 && groceryUnit.equalsIgnoreCase("Kilo")) {
            this.setUnit(new SI("Gram", "g", "kg", ""));
            this.setQuantity(groceryQuantity * 1000);
        }
        else if (groceryQuantity >= 10.0 && groceryUnit.equalsIgnoreCase("Desi")) {
            this.setUnit(new SI("Liter", "L", "L", "Desi"));
            this.setQuantity(groceryQuantity / 10);
        }
        else if (groceryQuantity < 1.0 && groceryUnit.equalsIgnoreCase("Desi")) {
            this.setUnit(new SI("Milliliter", "mL", "L", "Milli"));
            this.setQuantity(groceryQuantity * 100);
        }
        else if (groceryQuantity >= 100 && groceryUnit.equalsIgnoreCase("Milli")) {
            this.setUnit(new SI("Desiliter", "dL", "L", "Desi"));
            this.setQuantity(groceryQuantity / 100);
        }
    }

    @Override
    public String toString() {
        String str = "        Klasse Grocery;" + System.lineSeparator();
        str += "            VareID: " + this.groceryID + System.lineSeparator();
        str += "            Navn på varen: " + this.name + ";" + System.lineSeparator();
        str += "            Mengde av varen: " + this.quantity + this.getUnit().getUnit() + ";" + System.lineSeparator();
        str += "            Best-før dato: " + this.getDateToStr() + System.lineSeparator();
        str += "            Pris per måleenhet: " + this.getPriceToStr() + System.lineSeparator();

        return str;
    }
}
