package edu.ntnu.idi.idatt.modules;

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
 *     <li>{@link #removeAmount(double, SI, Fridge)}</li>
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
   */
  public Grocery(String name,
                 SI measure,
                 double quantity,
                 LocalDate date,
                 double price) {

    if (quantity <= 0 || price <= 0) {
      throw new IllegalArgumentException("Illegal arguments: Cannot have quantity or price "
          + " less or equal to zero.");
    }

    this.name = name;
    this.unit = measure;
    this.quantity = quantity;
    this.bestBefore = date;
    this.price = price;
    this.groceryID = advanceID();

    this.convertUnit();
  }

  @Override
  public boolean equals(final Object o) {
    try {
      Grocery g = (Grocery) o;
      Class<?> c = o.getClass();

      return c == Grocery.class && g.getName().equalsIgnoreCase(this.name)
          && g.getUnit().equals(this.unit)
          && g.getQuantity() == this.quantity
          && g.getDate().isEqual(this.bestBefore)
          && g.getPrice() == this.price;

    } catch (NullPointerException | ClassCastException e) {
      return false;
    }
  }

  /**
   * <strong>Description:</strong><br>
   * A static method for reseting the ID. Primaraly used for testing.<br>
   */
  @SuppressWarnings("checkstyle:AbbreviationAsWordInName")
  public static void resetID() {
    nextID = 1;
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
   (e.g. 11 Mars 2023).<br>
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
   * A set-method for incapsulating and assigning a value to the datafield quantity.
   This spesific method is ment for setting the quantity depending on the convertion between
   units.<br>
   */
  private void setQuantityWithFactor(double quantity) {
    this.quantity *= quantity;
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
    if (SI_Manager.hasValidUnit(unit)) {
      this.unit = unit;
    } else {
      throw new IllegalArgumentException("");
    }
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
    final double grocery_cf = this.unit.getConvertionFactor();

    final String amountUnitAbrev = amountUnit.getAbrev();
    final double amount_cf = amountUnit.getConvertionFactor();

    if (amount > 0 && SI_Manager.hasValidUnit(amountUnit)) {
      if ((groceryUnitAbrev.equals("stk") && !amountUnitAbrev.equals("stk"))) {
        System.out.println("Kan ikke legge til et antall med en annen målenhet enn \"stk\" når "
            + "varen er oppgitt i \"stk\".");
      } else {
        this.setQuantity(
            (double) Math.round((currentQuantity * grocery_cf + amount * amount_cf) * 100) / 100
        );
        convertUnit();
        System.out.println("La til " + amount + " " + amountUnitAbrev + " til varen " + this.name);

      }

    } else if (!SI_Manager.hasValidUnit(amountUnit)) {
      throw new IllegalArgumentException("Illegal argument error: "
          + "Cannot add an amount with invalid unit.");
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
  public void removeAmount(final double amount, final SI amountUnit, final Fridge fridge) {
    /*
     * Important!
     * The only conditions of the method is that the amount must be positive.
     * Both the grocery's unit and the unit of the addition should also be convertable to one
     * another e.g. kg <-> g  and dL <-> L. In the case of the unit "Stykker", both the amount
     * unit and the grocery unit must be "Stykker" for it to work.<br>
     */
    double currentQuantity = this.quantity;

    final String groceryUnitAbrev = this.unit.getAbrev();
    final double grocery_cf = this.unit.getConvertionFactor();

    final String amountUnitAbrev = amountUnit.getAbrev();
    final double amount_cf = amountUnit.getConvertionFactor();

    if (amount > 0 && SI_Manager.hasValidUnit(amountUnit)) {
      //XOR for forkortelse av enhetene. Hvis begge enhetene ikke samsvarer samsvarer med hverandre
      // og en av dem er oppgitt i stykker, kjører denne.
      if ((groceryUnitAbrev.equals("stk") && !amountUnitAbrev.equals("stk"))
          || (!groceryUnitAbrev.equals("stk") && amountUnitAbrev.equals("stk"))) {
        throw new IllegalArgumentException("Kan ikke trekke fra et antall med en annen målenhet "
            + "enn \"stk\" når varen er oppgitt i \"stk\". Varens enhet er gitt som "
            + groceryUnitAbrev + " og brukerens enhet er gitt ved " + amountUnitAbrev + ".");
      } else {
        this.setQuantity(
            (double) Math.round((currentQuantity * grocery_cf - amount * amount_cf) * 100) / 100
        );
        System.out.println("Fjernet " + amount + " " + amountUnitAbrev + " fra varen " + this.name);
        currentQuantity = this.quantity;
      }
      convertUnit();

      if (currentQuantity <= 0) {
        fridge.removeGrocery(this);
        System.out.println("Fjernet vare med vareID " + this.groceryID);
      }
    } else if (!SI_Manager.hasValidUnit(amountUnit)) {
      throw new IllegalArgumentException(
          "Illegal argument error: Cannot remove an amount with invalid unit.");
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
    final String groceryUnit = this.unit.getPrefix();
    try {
      if (this.quantity < 1.0 && groceryUnit.isEmpty()) {
        if (unit.getAbrev().equalsIgnoreCase("L")) {
          convertFromLToDL();
        }
      } else if (this.quantity >= 1000.0 && groceryUnit.isEmpty()) {
        if (this.unit.getAbrev().equalsIgnoreCase("g")) {
          convertFromGToKg();
        }
      } else if (this.quantity < 1.0 && groceryUnit.equalsIgnoreCase("Kilo")) {
        convertFromKgToG();
      } else if (this.quantity >= 10.0 && groceryUnit.equalsIgnoreCase("Desi")) {
        convertFromDLToL();
      } else if (this.quantity < 1.0 && groceryUnit.equalsIgnoreCase("Desi")) {
        convertFromDLToML();
      } else if (this.quantity >= 100 && groceryUnit.equalsIgnoreCase("Milli")) {
        convertFromMLToDL();
      }
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }
  }

  @SuppressWarnings("checkstyle:AbbreviationAsWordInName")
  private void convertFromLToDL() {
    if (unit.getAbrev().equalsIgnoreCase("L")) {
      this.setUnit(SI_Manager.getUnit("dL"));
      this.setQuantityWithFactor(1 / SI.getSiPrefixes().get("Desi"));
    }
  }

  @SuppressWarnings("checkstyle:AbbreviationAsWordInName")
  private void convertFromGToKg() {
    if (this.unit.getAbrev().equalsIgnoreCase("g")) {
      this.setUnit(SI_Manager.getUnit("kg"));
      this.setQuantityWithFactor(1 / SI.getSiPrefixes().get("Kilo"));
    }
  }

  private void convertFromKgToG() {
    this.setUnit(SI_Manager.getUnit("g"));
    this.setQuantityWithFactor(SI.getSiPrefixes().get("Kilo"));
  }

  @SuppressWarnings("checkstyle:AbbreviationAsWordInName")
  private void convertFromDLToL() {
    this.setUnit(SI_Manager.getUnit("L"));
    this.setQuantityWithFactor(SI.getSiPrefixes().get("Desi"));
  }

  @SuppressWarnings("checkstyle:AbbreviationAsWordInName")
  private void convertFromDLToML() {
    this.setUnit(SI_Manager.getUnit("mL"));
    this.setQuantityWithFactor(1 / (SI.getSiPrefixes().get("Milli")
        / SI.getSiPrefixes().get("Desi")));
  }

  @SuppressWarnings("checkstyle:AbbreviationAsWordInName")
  private void convertFromMLToDL() {
    this.setUnit(SI_Manager.getUnit("dL"));
    this.setQuantityWithFactor(1 / (SI.getSiPrefixes().get("Desi")
        / SI.getSiPrefixes().get("Milli")));
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
    str += "            Mengde av varen: " + this.quantity + " " + this.getUnit().getUnit() + ";"
        + System.lineSeparator();
    str += "            Best-før dato: " + this.getDateToStr() + System.lineSeparator();
    str += "            Pris per måleenhet: " + this.getPriceToStr() + System.lineSeparator();

    return str;
  }
}
