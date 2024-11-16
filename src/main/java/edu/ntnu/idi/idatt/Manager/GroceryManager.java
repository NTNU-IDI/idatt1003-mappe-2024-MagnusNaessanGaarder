package edu.ntnu.idi.idatt.Manager;

import edu.ntnu.idi.idatt.Modules.Grocery;
import edu.ntnu.idi.idatt.Utils.SI;

import java.util.Scanner;

public class GroceryManager {
    private final Grocery grocery;
    private final SI unit;
    private final Scanner myReader;

    public GroceryManager(Grocery g, Scanner reader) {
        this.grocery = g;
        this.unit = this.grocery.getUnit();
        this.myReader = reader;
    }

    private String getInput() {
        if (myReader.hasNextLine()) {
            return myReader.nextLine();
        }
        return "";
    }

    public String[] getAmountAndUnit() {
        //mengden og enheten av varen
        String userInput = "";
        try{
            do {
                System.out.print("          Skriv mengden på varen (f.eks 2 gram / desiliter / stykker): ");
                userInput = getInput();
            }
            while (!SI_manager.isValidUnit(String.join("", userInput.split(" ")[1])));
        }
        catch (Exception e) {
            System.out.println("Invalid format for unit or amount.");
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
            String[] amountAndUnit = getAmountAndUnit();

            double addAmount = Double.parseDouble(amountAndUnit[0]);
            SI addUnit = SI_manager.getUnit(amountAndUnit[1]);

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
            String[] amountAndUnit = getAmountAndUnit();

            SI unit = SI_manager.getUnit(amountAndUnit[1]);
            double amount = Double.parseDouble(amountAndUnit[0]);

            assert unit != null;
            grocery.removeAmount(amount, unit);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
