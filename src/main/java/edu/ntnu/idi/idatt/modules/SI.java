package edu.ntnu.idi.idatt.modules;

import java.util.*;

public class SI {
    @Override
    public boolean equals(Object o) {
        try {
            SI s = (SI) o;
            Class<?> c = o.getClass();
            if (c == SI.class && s.getUnit().equalsIgnoreCase(this.unit) &&
                     s.getPrefix().equalsIgnoreCase(this.prefix) &&
                     s.getAbrev().equalsIgnoreCase(this.unitAbrev) &&
                     s.getUnitForPrice().equalsIgnoreCase(this.unitForPrice) &&
                     s.getConvertionFactor() == this.convertionFactor) {

                    return s.getUnit().equalsIgnoreCase(this.unit);
            }

            return false;
        }
        catch (NullPointerException | ClassCastException e) {
            return false;
        }
    }

    //Datafelt med globale variabler innenfor SI-enhet-scopet.
    private final String unit;
    private final String prefix;
    private final String unitAbrev;
    private final String unitForPrice;
    private final double convertionFactor;

    /*Hjelp av ChatGPT - lager et HashMap med en static block som initialiserer
    * ulike verdier til HashMap-et. I dette tilfellet er verdiene i HashMap-et
    * ment å brukes til konvertering mellom ulike målinger ved hjelp av prefiksen.
    */

    /**
     * HashMap with values of prefixes and unit conversion factors.
     */
    private static final HashMap<String, Double> SI_PREFIXES = new HashMap<>();
    static {
        SI_PREFIXES.put("", 1.0);         // uten prefix = base unit
        SI_PREFIXES.put("Milli", 0.001);  // 1 basisenhet = 1000 millienheter
        SI_PREFIXES.put("Centi", 0.01);   // 1 basisenhet = 100 centienheter
        SI_PREFIXES.put("Desi", 0.1);     // 1 basisenhet = 10 desienheter
        SI_PREFIXES.put("Kilo", 1000.0);  // 1 kiloenhet = 1000 basisenheter
        SI_PREFIXES.put("Teskje", 0.015);     // 1 basisenhet = ca. 67 desienheter
        SI_PREFIXES.put("Spiseskje", 0.005);  // 1 basisenhet = 200 centienheter
    }

    /**
     * HashMap with ArrayLists of valid units
     */
    private static final ArrayList<String> VALID_NAME = new ArrayList<>();
    static {
        VALID_NAME.add("Stykker");
        VALID_NAME.add("Milliliter");
        VALID_NAME.add("Centiliter");
        VALID_NAME.add("Liter");
        VALID_NAME.add("Gram");
        VALID_NAME.add("Kilogram");
    }

    private static final ArrayList<String> VALID_ABREV = new ArrayList<>();
    static {
        VALID_ABREV.add("stk");
        VALID_ABREV.add("mL");
        VALID_ABREV.add("cL");
        VALID_ABREV.add("");
        VALID_ABREV.add("g");
        VALID_ABREV.add("kg");
    }


    //konstruktør for klassen SI
    public SI(String unit, String unitShort, String unitForPrice, String prefix) {
        this.unit = unit;
        this.unitAbrev = unitShort;
        this.unitForPrice = unitForPrice;
        this.prefix = prefix;

        if (unitAbrev.equals("stk")) {
            this.convertionFactor = 1;
        }
        else {
            this.convertionFactor = SI_PREFIXES.getOrDefault(prefix,1.0);
        }

    }

    public static ArrayList<String> getValidName() {
        return VALID_NAME;
    }

    public static ArrayList<String> getValidAbrev() {
        return VALID_ABREV;
    }

    //get metode for å få SI-enheten
    public String getUnit() {
        return this.unit;
    }

    //get metode for å få forkortelsen av SI-enheten
    public String getAbrev() {
        return this.unitAbrev;
    }

    //get metode for å få måleenheten til prisen per måleenhet.
    public String getUnitForPrice() {
        return this.unitForPrice;
    }

    //get metode for å få prefiksen til SI-enheten
    public String getPrefix() {
        return this.prefix;
    }

    /**
     * Get-method for convertionfactor
     * @return the conversion factor.
     */
    public double getConvertionFactor() {
        return this.convertionFactor;
    }

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