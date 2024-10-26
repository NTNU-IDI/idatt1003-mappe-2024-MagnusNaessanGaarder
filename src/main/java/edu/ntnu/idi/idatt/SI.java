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
        SI_PREFIXES.put("", 1.0);         // No prefix = base unit
        SI_PREFIXES.put("Milli", 0.001);  // 1 base unit = 1000 millibases
        SI_PREFIXES.put("Centi", 0.01);   // 1 base unit = 100 centibases
        SI_PREFIXES.put("Desi", 0.1);     // 1 base unit = 10 decibases
        SI_PREFIXES.put("Kilo", 1000.0);  // 1 kilobase = 1000 base units
    }

    //konstruktør for klassen SI
    public SI(String unit, String unitShort, String unitForPrice, String prefix) {
        this.unit = unit;
        this.unitAbrev = unitShort;
        this.unitForPrice = unitForPrice;
        this.prefix = prefix;

        this.convertionFactor = SI_PREFIXES.getOrDefault(prefix,1.0);
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