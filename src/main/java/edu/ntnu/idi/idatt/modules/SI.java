package edu.ntnu.idi.idatt.modules;

//imports
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <strong>Description:</strong><br>
 * An immutable class representing an SI unit.
 * This class contains static content for unit-conversion,
 and
 */
@SuppressWarnings("checkstyle:AbbreviationAsWordInName")
public class SI {
  /**
   * <strong>Description</strong><br>
   * Static datafield of a HashMap with values of prefixes and unit conversion factors.
   This will be used for unit conversion.
   */
  private static final HashMap<String, Double> SI_PREFIXES = new HashMap<>();

  /**
   * <strong>Description</strong><br>
   * Static datafield of an ArrayList of valid unit names.
   */
  private static final ArrayList<String> VALID_NAME = new ArrayList<>();

  /**
   * <strong>Description</strong><br>
   * Static datafield of an ArrayList of valid unit abbreviations.
   */
  private static final ArrayList<String> VALID_ABREV = new ArrayList<>();


  /*Hjelp av ChatGPT - lager et HashMap med en static block som initialiserer
   * ulike verdier til HashMap-et. I dette tilfellet er verdiene i HashMap-et
   * ment å brukes til konvertering mellom ulike målinger ved hjelp av prefiksen.
   */
  //initialisering av gyldige prefixer for konvertering mellom måleenheter
  static {
    SI_PREFIXES.put("", 1.0);         // uten prefix = 1 basisenhet
    SI_PREFIXES.put("Milli", 0.001);  // 1 basisenhet = 1000 millienheter
    SI_PREFIXES.put("Centi", 0.01);   // 1 basisenhet = 100 centienheter
    SI_PREFIXES.put("Desi", 0.1);     // 1 basisenhet = 10 desienheter
    SI_PREFIXES.put("Kilo", 1000.0);  // 1 kiloenhet = 1000 basisenheter
    SI_PREFIXES.put("Te", 0.015);     // 1 basisenhet = ca. 67 desienheter
    SI_PREFIXES.put("Spise", 0.005);  // 1 basisenhet = 200 centienheter
  }

  //initialisering av gyldige navn til måleenheter for VALID_NAME
  static {
    VALID_NAME.add("Stykker");
    VALID_NAME.add("Milliliter");
    VALID_NAME.add("Centiliter");
    VALID_NAME.add("Liter");
    VALID_NAME.add("Gram");
    VALID_NAME.add("Kilogram");
    VALID_NAME.add("Teskje");
    VALID_NAME.add("Spiseskje");
  }

  //initialisering av gyldige forkortelser til måleenheter for VALID_ABREV
  static {
    VALID_ABREV.add("stk");
    VALID_ABREV.add("mL");
    VALID_ABREV.add("cL");
    VALID_ABREV.add("");
    VALID_ABREV.add("g");
    VALID_ABREV.add("kg");
    VALID_ABREV.add("ts");
    VALID_ABREV.add("ss");
  }

  //Datafelt med globale variabler innenfor SI-enhet-scopet.
  private final String unit;
  private final String prefix;
  private final String unitAbrev;
  private final String unitForPrice;
  private final double convertionFactor;

  /**
   * <strong>Description:</strong><br>
   * A constructor instantizing the class and initializing
   the datafields.<br>
   *
   * @param unit The name of the unit of datatype String.
   * @param unitShort The abbreviation of the name of the string
   *                  of datatype String.
   * @param unitForPrice The abbreviation of the units name used
   *                     for the price. Should be either kg, L or stk.
   *                     Datafield has String as the datatype.
   * @param prefix The prefix of the units name. E.g. Kilo or Desi.
   *               Used in conversions between units.
   */
  public SI(String unit, String unitShort, String unitForPrice, String prefix) {
    this.unit = unit;
    this.unitAbrev = unitShort;
    this.unitForPrice = unitForPrice;
    this.prefix = prefix;

    //Angir en konverteringsfaktor avhengig av prefixen til enheten.
    //Hvis enheten ikke finnes i SI_PREFIXES blir faktoren 1.
    this.convertionFactor = SI_PREFIXES.getOrDefault(prefix, 1.0);
  }

  /**
   * <strong>Description:</strong><br>
   * A get-method for the ArrayList of valid unit names.<br>
   *
   * @return A {@link List} of Strings being the valid unit names.
   */
  public static List<String> getValidName() {
    return VALID_NAME;
  }

  /**
   * <strong>Description:</strong><br>
   * A get-method for the ArrayList of valid abbreviations of unit names.<br>
   *
   * @return A {@link List} of Strings being the valid abbreviations
   of unit names.
   */
  public static List<String> getValidAbrev() {
    return VALID_ABREV;
  }

  /**
   * <strong>Description:</strong><br>
   * A method overriding the usual
   {@link Object#equals(Object o) equals(Object obj)} method.
   * This method is manually set to compare contents of the SI-
   object rather than if its the same object.<br>
   *
   * @param o An unknown object.
   * @return A boolean based on the comparison.
   */
  @Override
  public boolean equals(final Object o) {
    try {
      SI s = (SI) o;
      Class<?> c = o.getClass();
      return c == SI.class && s.getUnit().equalsIgnoreCase(this.unit)
          && s.getPrefix().equalsIgnoreCase(this.prefix)
          && s.getAbrev().equalsIgnoreCase(this.unitAbrev)
          && s.getUnitForPrice().equalsIgnoreCase(this.unitForPrice)
          && s.getConvertionFactor() == this.convertionFactor;
    } catch (NullPointerException | ClassCastException e) {
      return false;
    }
  }

  /**
   * <strong>Description:</strong><br>
   * A get-method for the units name.<br>
   *
   * @return A {@code String} of the units name.
   */
  public String getUnit() {
    return this.unit;
  }

  /**
   * <strong>Description:</strong><br>
   * A get-method for the abbreviation of the units name.<br>
   *
   * @return A {@code String} of the abbreviation of the units name.
   */
  public String getAbrev() {
    return this.unitAbrev;
  }

  /**
   * <strong>Description:</strong><br>
   * A get-method for the base unit for price.<br>
   *
   * @return A {@code String} of the base unit for price.
   */
  public String getUnitForPrice() {
    return this.unitForPrice;
  }

  /**
   * <strong>Description:</strong><br>
   * A get-method for the units prefix.<br>
   *
   * @return A {@code String} of the units prefix.
   */
  public String getPrefix() {
    return this.prefix;
  }

  /**
   * <strong>Description:</strong>
   * Get-method for the convertion factor of the unit.
   *
   * @return The conversion factor as a double floating point digit.
   */
  public double getConvertionFactor() {
    return this.convertionFactor;
  }

  /**
   * <strong>Description:</strong><br>
   * A method overriding the usual {@link Object#toString() toString()}.
   * It is used for debugging an will now be desplayed in the TUI.
   *
   * @return A string dumping information about the instantized object
   of the class.
   */
  @Override
  public String toString() {
    String str = "Klasse SI;\n";
    str += "Name: " + this.unit + "\n";
    str += "Abreviation: " + this.unitAbrev + "\n";
    str += "Unit for Price: " + this.unitForPrice + "\n";
    str += "Prefix: " + this.prefix + "\n";
    str += "Convertion Factor: " + this.convertionFactor + "\n";
    return str;
  }
}