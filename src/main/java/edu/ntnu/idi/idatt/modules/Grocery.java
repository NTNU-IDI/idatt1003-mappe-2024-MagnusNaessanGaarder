package edu.ntnu.idi.idatt.modules;

/*
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

 # Matoppskrift:
 Implementer en klasse som representerer en oppskrift. En oppskrift skal minimum inneholde
 følgende informasjon:
 • Navn på rett
 • En kort beskrivelse av retten
 • Fremgangsmåte (et tekstfelt)
 • En liste over matvarer/ingredienser som inngår for å lage retten. Her må du selv velge
 hviken klasse fra JDK’en du tenker passer (ArrayList, HashMap, HashSet osv). Begrunn
 valget i rapporten.
 • Antall personer oppskriften er myntet på (ofte er 4 standard)

 # Kokebok:
 Opprett en klasse som representerer en Kokebok (engelsk: Cookbook). Du må selv bestemme
 hvilken klasse fra Java biblioteket du vil bruke (ArrayList, HashMap osv) (husk å begrunn valget
 i rapporten din).
 Du skal selv tenke ut og implementere fornuftige metoder i en slik klasse for å kunne oppfylle
 brukerkravene under (fra prosjektbeskrivelsen).

 # Brukerkrav:
 Som bruker må jeg kunne:
 • Opprette en matoppskrift (for en matrett). En matoppskrift (engelsk: Recipe) består
 typisk av følgende elementer: Et navn på oppskriften, en kort beskrivelse av hva
 oppskriften lager, en fremgangsmåte og en liste av ingredienser (inkludert mengde).
 • Sjekke om kjøleskapet inneholder nok varer/ingredienser til å lage en bestemt matrett.
 • Legge oppskriften inn i en kokebok for senere bruk.
 • Få forslag til hvilke retter som kan lages fra rettene i kokeboken med
 varene/ingrediensene som finnes i kjøleskapet. (Avansert!)
 NB! Det er ikke sikkert at alle disse funksjonene lar seg løse bare med klassen for oppskrift og
 klassen for kokebok. I så fall må du vurdere om du trenger flere klasser for å løse kravene (NB!
 Vi tenker her på forretningslogikken, ikke brukergrensesnittet).
*/

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


/**
 * <strong>Description:</strong><br>
 * A mutable class supposed to represent a {@code Grocery} which can be stored in a{@code Fridge}.
 * While this class is mutable, the focus of this class is to keep the datafields as immutable as
 * possible. This means that any other datafields other than datafields specified to be changeble in
 * the exam-description, will be kept immutable within reason.<br><br>
 *
 * <strong>Datafields:</strong><br>
 * {@code nextID} - A private static integer that determines the non-static groceryID via
 * {@link #advanceID()}.<br>
 * {@code groceryID} - A immutable and unique integer that can identify any instanticed Grocery.<br>
 * {@code name} - A immutable String that is supposed to represent the name of the Grocery.<br>
 * {@code unit} - A mutable object of type {@link SI} that is supposed to represent the SI-unit of
 * the Grocery.<br>
 * {@code quantity} - A mutable {@code double} that is supposed to represent the SI-unit of the
 * Grocery.<br>
 * {@code bestBefore} - A immutable object of type {@link LocalDate} that is supposed to represent
 * the expiration
 * date of the Grocery.<br>
 * {@code price} - A immutable {@code double} that is supposed to represent price of the Grocery
 * per approperiate
 * unit for price (e.g. kg/ L / stk).<br>
 * {@code fridge} - A immutable object of type {@link Fridge} that is supposed to represent the
 * SI-unit of the Grocery.<br>
 * <br>
 *
 * <strong>Methods:</strong>
 * <ul>
 *     <li>{@link #advanceID()}</li>
 *     <li>{@link #getGroceryID()}</li>
 *     <li>{@link #getName()}</li>
 *     <li>{@link #getDateToStr()}</li>
 *     <li>{@link #getDate()}</li>
 *     <li>{@link #getPriceToStr()}</li>
 *     <li>{@link #getPrice()}</li>
 *     <li>{@link #getQuantity()}</li>
 *     <li>{@link #setQuantity(double)}</li>
 *     <li>{@link #getUnit()}</li>
 *     <li>{@link #setUnit(SI)}</li>
 *     <li>{@link #hasExpired()}</li>
 *     <li>{@link #addAmount(double, SI)}</li>
 *     <li>{@link #removeAmount(double, SI)}</li>
 *     <li>{@link #convertUnit()}</li>
 *     <li>{@link #toString()}</li>
 * </ul>
 */
public class Grocery {
  private static int nextID = 1;
  @SuppressWarnings("checkstyle:AbbreviationAsWordInName")
  private final int groceryID;
  private final String name;
  private final LocalDate bestBefore;
  private final double price;
  private final Fridge fridge;
  private SI unit;
  private double quantity;

  //Kovnerterer enhet ved konstruksjon for å forsikre at måleenheten er riktig

  /**
   * <strong>Description:</strong><br>
   * A constructor instancing the class and initializing
   * the datafields.<br>
   *
   * @param name     The name of the Grocery.
   * @param measure  The unit of the Grocery.
   * @param quantity The quantity/ amount of the Grocery.
   * @param date     The expiration date of the Grocery
   * @param price    the price amount of the Grocery
   * @param fridge   the Fridge the grocery is being stored in.
   */
  public Grocery(String name,
                 SI measure,
                 double quantity,
                 LocalDate date,
                 double price,
                 Fridge fridge) {
    this.name = name;
    this.unit = measure;
    this.quantity = quantity;
    this.bestBefore = date;
    this.price = price;
    this.fridge = fridge;
    this.groceryID = advanceID();

    convertUnit();
  }

  /**
   * <strong>Description:</strong><br>
   * A static method for advancing the ID statically and give unique ID's for instanticed
   objects.<br>
   *
   * @return The value of the static datafield {@code nextID}. The datafield post-increment
   (e.g. foo++).
   */
  @SuppressWarnings("checkstyle:AbbreviationAsWordInName")
  private static int advanceID() {
    return nextID++;
  }

  /**
   * <strong>Description:</strong><br>
   * A get-method for incapsulating the datafield groceryID.<br>
   *
   * @return An integer with the value of groceryID.
   */
  @SuppressWarnings("checkstyle:AbbreviationAsWordInName")
  public int getGroceryID() {
    return this.groceryID;
  }

  /**
   * <strong>Description:</strong><br>
   * A get-method for incapsulating the datafield name.<br>
   *
   * @return An {@link String} with the value of name.
   */
  public String getName() {
    return name;
  }

  /**
   * <strong>Description:</strong><br>
   * A method for formating the datafield bestBefore to the format "dd LLLL yyyy"
   (e.g. 11 mars 2023).<br>
   *
   * @return A {@link String} with the value of the formated bestBefore.
   */
  public String getDateToStr() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");
    return formatter.format(bestBefore);
  }

  /**
   * <strong>Description:</strong><br>
   * A get-method for incapsulating the datafield bestBefore.<br>
   *
   * @return A {@link LocalDate} with the value of bestBefore.
   */
  public LocalDate getDate() {
    return bestBefore;
  }

  /**
   * <strong>Description:</strong><br>
   * A method for formating the datafield price to "price kr/unitForPrice". The unit for price is
   e.g. L, kg, stk.<br>
   *
   * @return A {@link String} with the value of the formated price.
   */
  public String getPriceToStr() {
    return String.format("%.2f kr/%s", this.price, this.unit.getUnitForPrice());
  }

  /**
   * <strong>Description:</strong><br>
   * A get-method for incapsulating the datafield price.<br>
   *
   * @return A {@code double} with the value price.
   */
  public double getPrice() {
    return price;
  }

  /**
   * <strong>Description:</strong><br>
   * A get-method for incapsulating the datafield quantity.<br>
   *
   * @return A {@code double} with the value of quantity.
   */
  public double getQuantity() {
    return quantity;
  }

  /**
   * <strong>Description:</strong><br>
   * A set-method for incapsulating and assigning a value to the datafield quantity.<br>
   */
  private void setQuantity(double quantity) {
    this.quantity = quantity;
  }

  /**
   * <strong>Description:</strong><br>
   * A get-method for incapsulating the datafield unit.<br>
   *
   * @return An object of type {@link SI} with the value of unit.
   */
  public SI getUnit() {
    return unit;
  }

  /**
   * <strong>Description:</strong><br>
   * A set-method for incapsulating and assigning a value to the datafield unit.<br>
   */
  private void setUnit(SI unit) {
    this.unit = unit;
  }

  /**
   * <strong>Description:</strong><br>
   * A method to check if the Grocery has expired by comparing to {@code LocalDate.now()}.<br>
   *
   * @return A {@code boolean} with regard to the comparison. Returns true if the bestBefore
   datafield is after today's date. Else, false.
   */
  public boolean hasExpired() {
    return this.bestBefore.isBefore(LocalDate.now());
  }

  /**
   * <strong>Description:</strong><br>
   * A method for adding an amount to with a unit, to this Grocery.
   * The unique feature of this method is that it converts two different units
   * to a common unit, and then converts back to the grocery's original unit.<br>
   *
   * @param amount     A {@code double} representing the amount that will be added to the Grocery.
   * @param amountUnit An object of the type {@link SI} representing the unit of the amount that
   *                   will be added to the Grocery.
   */
  public void addAmount(final double amount, final SI amountUnit) {
    /*
     * Important!
     * The only conditions of the method is that the amount must be positive.
     * Both the grocery's unit and the unit of the addition should also be convertable to one
     * another e.g. kg <-> g  and dL <-> L. In the case of the unit "Stykker", both the amount unit
     * and the grocery unit must be "Stykker" for it to work.<br>
     */

    final double currentQuantity = this.quantity;

    final String groceryUnitAbrev = this.unit.getAbrev();
    final String amountUnitAbrev = amountUnit.getAbrev();

    final double amount_cf = amountUnit.getConvertionFactor();
    final double grocery_cf = this.unit.getConvertionFactor();

    if (amount > 0) {
      if (groceryUnitAbrev.equals("stk") || amountUnitAbrev.equals("stk")) {
        System.out.println("Kan ikke legge til et antall med en annen målenhet enn \"stk\" når "
            + "varen er oppgitt i \"stk\".");
      } else {
        if (groceryUnitAbrev.equals("kg")) {
          this.setQuantity(
              (double) (
                  Math.round(
                          (currentQuantity * grocery_cf + amount * amount_cf)
                        / grocery_cf)
                      * 100)
                    / 100
          );
        } else {
          this.setQuantity(
              (double) (Math.round((currentQuantity * grocery_cf + amount * amount_cf) * 100)) / 100
          );
        }
        convertUnit();
        System.out.println("La til " + amount + " " + amountUnitAbrev + " til varen " + this.name);

      }

    } else {
      throw new IllegalArgumentException("Illegal argument error: Cannot add a negative amount.");
    }
  }

  /**
   * <strong>Description:</strong><br>
   * A method for subtracting an amount with a unit form this Grocery.
   * This method works simmilarly to {@link #addAmount(double, SI)} method, exept
   * it removes itself from a {@link Fridge} if the amount is below or equal to 0 after the
   subtraction.<br>
   *
   * @param amount     A {@code double} representing the amount that will be added to the Grocery.
   * @param amountUnit An object of the type {@link SI} representing the unit of the amount that
   *                   will be added to the Grocery.
   */
  public void removeAmount(final double amount, SI amountUnit) {
    /*
     * Important!
     * The only conditions of the method is that the amount must be positive.
     * Both the grocery's unit and the unit of the addition should also be convertable to one
     * another e.g. kg <-> g  and dL <-> L. In the case of the unit "Stykker", both the amount
     * unit and the grocery unit must be "Stykker" for it to work.<br>
     */

    double currentQuantity = this.quantity;
    final String groceryUnitAbrev = this.unit.getAbrev();
    final String amountUnitAbrev = amountUnit.getAbrev();
    final double amount_cf = amountUnit.getConvertionFactor();
    final double grocery_cf = this.unit.getConvertionFactor();

    if (amount > 0) {
      //XOR for forkortelse av enhetene. Hvis begge enhetene ikke samsvarer samsvarer med hverandre
      // og en av dem er oppgitt i stykker, kjører denne.
      if ((groceryUnitAbrev.equals("stk") && !amountUnitAbrev.equals("stk"))
          || (!groceryUnitAbrev.equals("stk") && amountUnitAbrev.equals("stk"))) {
        System.err.println("Kan ikke trekke fra et antall med en annen målenhet enn \"stk\" når "
                + "varen er oppgitt i \"stk\".");
        return;
      } else {
        if (groceryUnitAbrev.equals("kg")) {
          this.setQuantity(
              (double) (Math.round(
                  ((currentQuantity * grocery_cf - amount * amount_cf) / grocery_cf) * 100)) / 100
          );
        } else {
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
    } else {
      throw new IllegalArgumentException(
          "Illegal argument error: Cannot remove a negative amount.");
    }
  }

  /**
   * <strong>Description:</strong><br>
   * A method for conversion between convertable units in certain intervals.
   * The quantity of the grocery will also be ajusted accordingly to the unit convertion.
   */
  private void convertUnit() {
    final double groceryQuantity = this.quantity;
    final String groceryUnit = this.unit.getPrefix();

    if (groceryQuantity < 1.0 && groceryUnit.isEmpty()) {
      if (unit.getAbrev().equalsIgnoreCase("L")) {
        this.setUnit(new SI("Desiliter", "dL", "L", "Desi"));
        this.setQuantity(groceryQuantity * 10);
      }
    } else if (groceryQuantity >= 1000.0 && groceryUnit.isEmpty()) {
      if (unit.getAbrev().equalsIgnoreCase("g")) {
        this.setUnit(new SI("Kilogram", "kg", "kg", "Kilo"));
        this.setQuantity(groceryQuantity / 1000);
      }
    } else if (groceryQuantity < 1.0 && groceryUnit.equalsIgnoreCase("Kilo")) {
      this.setUnit(new SI("Gram", "g", "kg", ""));
      this.setQuantity(groceryQuantity * 1000);
    } else if (groceryQuantity >= 10.0 && groceryUnit.equalsIgnoreCase("Desi")) {
      this.setUnit(new SI("Liter", "L", "L", "Desi"));
      this.setQuantity(groceryQuantity / 10);
    } else if (groceryQuantity < 1.0 && groceryUnit.equalsIgnoreCase("Desi")) {
      this.setUnit(new SI("Milliliter", "mL", "L", "Milli"));
      this.setQuantity(groceryQuantity * 100);
    } else if (groceryQuantity >= 100 && groceryUnit.equalsIgnoreCase("Milli")) {
      this.setUnit(new SI("Desiliter", "dL", "L", "Desi"));
      this.setQuantity(groceryQuantity / 100);
    }
  }

  /**
   * <strong>Description:</strong><br>
   * Overridden method for writing the class as a String.<br>
   *
   * @return The {@link StringBuilder} as a String. Effectively a {@code String} of the content
   of the Grocery.
   */
  @Override
  public String toString() {
    String str = "        Klasse Grocery;" + System.lineSeparator();
    str += "            VareID: " + this.groceryID + System.lineSeparator();
    str += "            Navn på varen: " + this.name + ";" + System.lineSeparator();
    str += "            Mengde av varen: " + this.quantity + this.getUnit().getUnit() + ";"
        + System.lineSeparator();
    str += "            Best-før dato: " + this.getDateToStr() + System.lineSeparator();
    str += "            Pris per måleenhet: " + this.getPriceToStr() + System.lineSeparator();

    return str;
  }
}
