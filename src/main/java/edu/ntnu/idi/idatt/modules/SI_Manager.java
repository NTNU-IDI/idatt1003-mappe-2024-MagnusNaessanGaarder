package edu.ntnu.idi.idatt.modules;

import java.util.List;

/**
 * <strong>Description</strong><br>
 * An immutable class managing the other functionalities of a given {@link SI}
 that does not need to be directly contained in the SI-unit class.
 Rather, this manager class is a bridge between the
 functionalities needed for the application and the SI class itself.
 */
@SuppressWarnings({"checkstyle:AbbreviationAsWordInName", "checkstyle:TypeName"})
public class SI_Manager {

  /**
   * <strong>Description:</strong><br>
   * A static method used to create a Unit from a userInput.<br>
   *
   * @param input A {@link String} supposed to represent a unit name or abbreviation.
   * @return An object of type {@link SI}. If userInput is not a valid, return null.
   */
  public static SI getUnit(String input) {
    input = String.join("", input.split("[\\d\\W]"));

    if (hasValidName(input) || hasValidAbrev(input)) {
      if (hasValidName(input)) {
        return getUnitFromName(input);
      } else {
        return getUnitFromAbrev(input);
      }
    } else {
      throw new IllegalArgumentException(
          "Invalid user input. \"" + input + "\" is not a valid Unit name or abbreviation.");
    }
  }

  /**
   * <strong>Description:</strong><br>
   * A static method for getting a {@link SI}-unit from an abreviation of a unit.<br>
   *
   * @param abrev A {@code String} representing the abreviation of the unit.
   * @return An object of type {@link SI}.
   */
  private static SI getUnitFromAbrev(String abrev) {
    String unit;
    String unitForPrice;
    String prefix;

    if (abrev.equalsIgnoreCase("stk")) {
      unit = "Stykker";
      unitForPrice = "stk";
      prefix = "";
    } else if (abrev.equalsIgnoreCase("ts")) {
      unit = "Teskje";
      unitForPrice = "";
      prefix = "Te";
    } else if (abrev.equalsIgnoreCase("ss")) {
      unit = "Spiseskje";
      unitForPrice = "";
      prefix = "Spise";
    } else if (abrev.equalsIgnoreCase("L")) {
      unit = "Liter";
      unitForPrice = "L";
      prefix = "";
    } else if (abrev.contains("L")) {
      if (abrev.contains("d")) {
        unit = prefix = "Desi";
      } else if (abrev.contains("m")) {
        unit = prefix = "Milli";
      } else {
        unit = prefix = "Centi";
      }
      unitForPrice = "L";
      unit += "liter";
    } else {
      if (abrev.equalsIgnoreCase("g")) {
        unit = "Gram";
        prefix = "";
      } else {
        unit = "Kilogram";
        prefix = "Kilo";
      }
      unitForPrice = "kg";
    }
    return new SI(unit, abrev, unitForPrice, prefix);
  }

  /**
   * <strong>Description:</strong><br>
   * A static method for creating a {@link SI}-unit from a name of a unit.<br>
   *
   * @param unitName A {@code String} representing the name of the unit.
   * @return An object of type {@link SI}.
   */
  private static SI getUnitFromName(String unitName) {
    String abrev;
    String unitForPrice;
    String prefix;

    if (unitName.equalsIgnoreCase("Stykker")) {
      abrev = unitForPrice = "stk";
      prefix = "";
    } else if (unitName.equalsIgnoreCase("Teskje")) {
      abrev = "ts";
      unitForPrice = "";
      prefix = "Te";
    } else if (unitName.equalsIgnoreCase("Spiseskje")) {
      abrev = "ss";
      unitForPrice = "";
      prefix = "Spise";
    } else if (unitName.equalsIgnoreCase("Liter")) {
      abrev = unitForPrice = "L";
      prefix = "";
    } else if (unitName.contains("liter")) {
      if (unitName.contains("Desi")) {
        abrev = "dL";
        prefix = "Desi";
      } else if (unitName.contains("Milli")) {
        abrev = "mL";
        prefix = "Milli";
      } else {
        abrev = "cL";
        prefix = "Centi";
      }
      unitForPrice = "L";
    } else {
      if (unitName.equalsIgnoreCase("Gram")) {
        abrev = "g";
        prefix = "";
      } else {
        abrev = "kg";
        prefix = "Kilo";
      }
      unitForPrice = "kg";
    }
    return new SI(unitName, abrev, unitForPrice, prefix);
  }

  /**
   * <strong>Description:</strong><br>
   * A static method checkin the validity of a given userInput supposed to represent a
   units name.<br>
   *
   * @param userInput A {@link String} that in this case is supposed to represent the name
   *                  of the unit.
   * @return A {@code boolean} based on whether the userInput matches values or keys of the
   HashMap or not.
   */
  private static boolean hasValidName(final String userInput) {
    List<String[]> validUnit = SI.getValidUnits().stream()
        .filter(u -> u[0].equalsIgnoreCase(userInput))
        .toList();

    return !validUnit.isEmpty();
  }

  /**
   * <strong>Description:</strong><br>
   * A static method checkin the validity of a given userInput supposed to represent a units
   abreviation.<br>
   *
   * @param userInput A {@link String} that in this case is supposed to represent the name of
   *                  the unit.
   * @return A {@code boolean} based on whether the userInput matches values or keys of the
   HashMap or not.
   */
  private static boolean hasValidAbrev(final String userInput) {
    List<String[]> validUnit = SI.getValidUnits().stream()
        .filter(u -> u[1].equalsIgnoreCase(userInput))
        .toList();

    return !validUnit.isEmpty();
  }

  /**
   * <strong>Description:</strong><br>
   * A public static method checkin the validity of a given unit.<br>
   *
   * @param unit A {@link SI} that in this case is supposed to represent the unit.
   * @return A {@code boolean} based on whether the userInput matches values or keys of the
   HashMap or not.
   */
  public static boolean hasValidUnit(final SI unit) {
    List<String[]> validUnit = SI.getValidUnits().stream()
        .filter(u ->
            u[0].equalsIgnoreCase(unit.getUnit())
                && u[1].equalsIgnoreCase(unit.getAbrev())
                && u[2].equalsIgnoreCase(unit.getUnitForPrice())
                && u[3].equalsIgnoreCase(unit.getPrefix())
                && u[4].equalsIgnoreCase("" + unit.getConvertionFactor())
        )
        .toList();

    return !validUnit.isEmpty();
  }
}
