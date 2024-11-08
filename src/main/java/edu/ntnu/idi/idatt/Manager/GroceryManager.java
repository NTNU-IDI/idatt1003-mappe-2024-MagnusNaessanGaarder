package edu.ntnu.idi.idatt.Manager;

import edu.ntnu.idi.idatt.Utils.SI;
import edu.ntnu.idi.idatt.modules.Grocery;

import static edu.ntnu.idi.idatt.Client.Client.getInput;

public class GroceryManager {
    final private FridgeManager fm;

    public GroceryManager(FridgeManager fm) {
        this.fm = fm;
    }

    public static String[] getAmountAndUnit() {
        //mengden og enheten av varen
        String userInput = "";
        try{
            do {
                System.out.print("          Skriv mengden på varen (f.eks 2 gram / desiliter / stykker): ");
                userInput = getInput();
            }
            while (!SI_manager.isValidUnit(String.join("",userInput.split(" ")[1])));
        }
        catch (Exception e) {
            System.err.println("Invalid format for unit or amount.");;
        }
        return userInput.split(" ");
    }

    public void addAmountGrocery(Grocery grocery) {
        try {
            //legg til en mengde
            String[] amountAndUnit = getAmountAndUnit();

            SI unit = SI_manager.getUnit(amountAndUnit[1]);
            double amount = Double.parseDouble(amountAndUnit[0]);

            grocery.addAmount(amount, unit);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public void removeAmountGrocery(Grocery grocery) {
        try{
            //trekk fra en mengde
            String[] amountAndUnit = getAmountAndUnit();

            SI unit = SI_manager.getUnit(amountAndUnit[1]);
            double amount = Double.parseDouble(amountAndUnit[0]);

            grocery.removeAmount(amount, unit);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
