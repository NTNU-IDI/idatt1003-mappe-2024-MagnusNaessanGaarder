package edu.ntnu.idi.idatt.Utils;

import java.util.HashMap;
import java.util.Map;

public class SI {
    @Override
    public boolean equals(Object o) {
        try {
            Class<?> c = o.getClass();
            if (c == SI.class && ((SI) o).getUnit().equalsIgnoreCase(this.unit) &&
                     ((SI) o).getPrefix().equalsIgnoreCase(this.prefix) &&
                     ((SI) o).getAbrev().equalsIgnoreCase(this.unitAbrev) &&
                     ((SI) o).getUnitForPrice().equalsIgnoreCase(this.unitForPrice) &&
                     ((SI) o).getConvertionFactor() == this.convertionFactor) {

                    return ((SI) o).getUnit().equalsIgnoreCase(this.unit);
            }

            return false;
        }
        catch (NullPointerException e) {
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

    private static final HashMap<String,String> VALID_UNIT = new HashMap<>();
    static {
        VALID_UNIT.put("Stykker","stk");
        VALID_UNIT.put("Desiliter", "dL");
        VALID_UNIT.put("Milliliter", "mL");
        VALID_UNIT.put("Centiliter", "cL");
        VALID_UNIT.put("Liter", "");
        VALID_UNIT.put("Gram", "g");
        VALID_UNIT.put("Kilogram", "kg");
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

    public static Map<String,String> getValidUnit() {
        return VALID_UNIT;
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

    //
    public double getConvertionFactor() {
        return this.convertionFactor;
    }
}