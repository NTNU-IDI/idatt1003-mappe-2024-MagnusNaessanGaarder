package edu.ntnu.idi.idatt.Manager;

import edu.ntnu.idi.idatt.Modules.Grocery;
import edu.ntnu.idi.idatt.UI.AbstractTerminalAction;
import edu.ntnu.idi.idatt.Utils.SI;
import edu.ntnu.idi.idatt.Utils.Table;


public class GroceryManager extends AbstractTerminalAction {
    private final Grocery grocery;
    private final SI unit;

    public GroceryManager(Grocery g) {
        this.grocery = g;
        this.unit = this.grocery.getUnit();
    }

    public static String[] getAmountAndUnit() {
        //mengden og enheten av varen
        String userInput = "";
        try{
            System.out.print("          Skriv mengden på varen (f.eks 2 gram / desiliter / stykker): ");
            do {
                userInput = getInputStatic();

                if (!Abstract_SI_manager.isValidUnit(String.join("", userInput.split(" ")[1]))) {
                    System.out.println("Ugyldig måleenhet på varen. Skriv inn et tall, mellomrom og en gyldig måleenhet");
                }
            }
            while (!Abstract_SI_manager.isValidUnit(String.join("", userInput.split(" ")[1])));
        }
        catch (NumberFormatException e) {
            System.out.println("Ugyldig mengde på varen. Skriv inn et tall, mellomrom og en gyldig måleenhet.");
        }
        catch (Exception e) {
            System.out.println("Uventet feil: \"" + e.getMessage() + "\"");
        }
        return userInput.split(" ");
    }

    public double getPricePerQuantity() {
        double result;
        if (unit.getAbrev().equals(unit.getUnitForPrice())) {
            result = grocery.getQuantity() * grocery.getPrice();
        }
        else if (unit.getAbrev().equalsIgnoreCase("g")) {
            result = grocery.getPrice() * (grocery.getQuantity() / unit.getConvertionFactor());
        }
        else {
            result = grocery.getPrice() * (grocery.getQuantity() * unit.getConvertionFactor());
        }

        return (double) Math.round(result * 100) / 100;
    }

    public void addAmountGrocery() {
        try {
            //legg til en mengde
            clearScreen();
            System.out.println(Table.createMenuTable("LEGG TIL " + grocery.getName().toUpperCase(), "Fyll ut deltaljer om varen du skal legge til:"));
            String[] amountAndUnit = getAmountAndUnit();

            double addAmount = Double.parseDouble(amountAndUnit[0]);
            SI addUnit = Abstract_SI_manager.getUnit(amountAndUnit[1]);

            assert addUnit != null;
            grocery.addAmount(addAmount, addUnit);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public void removeAmountGrocery() {
        try{
            //trekk fra en mengde
            clearScreen();
            System.out.println(Table.createMenuTable("FJERN FRA " + grocery.getName().toUpperCase(), "Fyll ut deltaljer om varen du skal fjerne fra:"));
            String[] amountAndUnit = getAmountAndUnit();

            SI newUnit = Abstract_SI_manager.getUnit(amountAndUnit[1]);
            double amount = Double.parseDouble(amountAndUnit[0]);

            assert newUnit != null;
            grocery.removeAmount(amount, newUnit);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
