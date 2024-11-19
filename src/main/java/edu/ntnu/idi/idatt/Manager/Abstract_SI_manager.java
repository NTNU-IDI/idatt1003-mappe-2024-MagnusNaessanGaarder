package edu.ntnu.idi.idatt.Manager;

import edu.ntnu.idi.idatt.UI.AbstractOption;
import edu.ntnu.idi.idatt.Utils.SI;

/**
 * <strong>Heritage:</strong><br>
 * This abstract class inherits from {@link AbstractOption}.<br><br>
 *
 * <strong>Description:</strong><br>
 * An abstract class with with an objective to manage conversion, and checks on a unit ({@link SI}).
 * The idea behind this class is to manange all tasks related to the SI-class that is not supposed to
 * be managed directly inside the SI-class. Another reason is to create abstraction.<br><br>
 *
 * <strong>Methods:</strong><br>
 * {@link #getUnit} - A static method that returns a {@code SI}-object based on a userInput
 * {@link #getUnit} - A static method that checks if a userinput is a valid unit and returns a {@code boolean}.
 */
public abstract class Abstract_SI_manager extends AbstractOption {

    /**
     * <strong>Description:</strong><br>
     * A method used to get a unit from a userInput. The userInput is interpreted as either a
     * abreviation or the full name of the unit. Based on what is given as the userInput,
     * the method finds if the unit currently exists and then creates and returns an approperiate
     * {@link SI}-object.<br>
     *
     * @param input A {@link String} supposed to represent a unit name or abbreviation.
     * @return An object of type {@link SI}. Based on userInput. If userInput is not a valid
     * userinput, return null.
     */
    public static SI getUnit(String input) {
        input = String.join("", input.split("[\\d\\W]"));

        String unit;
        String abrev;
        String unitForPrice;
        String prefix;

        if (isValidUnit(input)) {
            unit = input;
            if (input.equalsIgnoreCase(SI.getValidUnit().get("Stykker")) || input.equalsIgnoreCase("Stykker")) {
                prefix = "";
                abrev = unitForPrice = "stk";
            }
            else if (input.equalsIgnoreCase(SI.getValidUnit().get("Desiliter")) || input.equalsIgnoreCase("Desiliter")){
                prefix = "Desi";
                abrev = "dL";
                unitForPrice = "L";
            }
            else if (input.equalsIgnoreCase(SI.getValidUnit().get("Milliliter")) || input.equalsIgnoreCase("Milliliter")) {
                prefix = "Milli";
                abrev = "mL";
                unitForPrice = "L";
            }
            else if (input.equalsIgnoreCase(SI.getValidUnit().get("Centiliter")) || input.equalsIgnoreCase("Centiliter")) {
                prefix = "Centi";
                abrev = "cL";
                unitForPrice = "L";
            }
            else if (input.equalsIgnoreCase(SI.getValidUnit().get("Liter")) || input.equalsIgnoreCase("Liter")) {
                prefix = "";
                abrev = "L";
                unitForPrice = "L";
            }
            else if (input.equalsIgnoreCase(SI.getValidUnit().get("Gram")) || input.equalsIgnoreCase("Gram")) {
                prefix = "";
                abrev = "g";
                unitForPrice = "kg";
            }
            else {
                prefix = "Kilo";
                abrev = "kg";
                unitForPrice = "kg";
            }
            return new SI(unit,abrev,unitForPrice,prefix);
        }
        return null;
    }

    /**
     * <strong>Description:</strong><br>
     * A method checkin the validity of a given userInput. Here the userInput is compared to
     * a {@code HashMap} {@link SI#getValidUnit() getValidUnit()}. The HashMap contains both
     * unit names and abreviations which is checked against the isValidUnit().<br>
     *
     * @param userInput A {@link String} that is supposed to represent either a unit name or abbreviation.
     * @return A {@code boolean} based on whether the userInput matches values or keys of the HashMap or not.
     */
    public static boolean isValidUnit(String userInput) {
        for (String hashVal: SI.getValidUnit().values()) {
            if (hashVal.equalsIgnoreCase(userInput)) {
                return true;
            }
        }

        //sjekker validitet. Hvis prefiksen er en gyldig prefiks i SI-klassen, returner true. Ellers, false
        for (String hashKey : SI.getValidUnit().keySet()) {
            if(hashKey.equalsIgnoreCase(userInput)) {
                return true;
            }
        }
        return false;
    }
}
