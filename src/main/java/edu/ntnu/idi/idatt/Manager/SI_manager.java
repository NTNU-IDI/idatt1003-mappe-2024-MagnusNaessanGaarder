package edu.ntnu.idi.idatt.Manager;

import edu.ntnu.idi.idatt.Utils.SI;

public class SI_manager {

    public static SI getUnit(String input) {
        input = String.join("", input.split("[\\d\\s+\\W]"));

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
        else{
            return null;
        }
    }

    public static boolean isValidUnit(String userInput) {

        //fjerner eventuelle tall som kom med input fra brukerinput
        String prefix = String.join("", userInput.split("[\\d\\s+\\W]"));

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
