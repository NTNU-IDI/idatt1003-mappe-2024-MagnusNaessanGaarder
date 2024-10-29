package edu.ntnu.idi.idatt;

import java.util.HashMap;

public class SI {

    //Datafelt med globale variabler innenfor SI-enhet-scopet.
    private String unit;
    private String prefix;
    private String unitAbrev;
    private String unitForPrice;
    private double convertionFactor;

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

    //get metode for å få SI_PREFIX hashmapet
    public static HashMap<String, Double> getSIPrefixes() {
        return SI_PREFIXES;
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