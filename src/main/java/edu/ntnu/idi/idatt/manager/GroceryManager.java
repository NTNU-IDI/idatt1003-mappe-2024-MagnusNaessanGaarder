package edu.ntnu.idi.idatt.manager;

import edu.ntnu.idi.idatt.modules.Grocery;
import edu.ntnu.idi.idatt.utils.AbstractTerminalAction;
import edu.ntnu.idi.idatt.modules.SI;


public class GroceryManager extends AbstractTerminalAction {
    private final Grocery grocery;
    private final SI unit;

    public GroceryManager(Grocery g) {
        this.grocery = g;
        this.unit = this.grocery.getUnit();
    }

    public static String[] getAmountAndUnit(String str) throws Exception {
        //mengden og enheten av varen
        try{
            String[] res =  str.split(" ");
            Double.parseDouble(res[0]);

            return res;
        }
        catch (NumberFormatException e) {
            throw new NumberFormatException("NumberFormatExeption with following message: " + e.getMessage());
        }
        catch (Exception e) {
            throw new Exception("Something went wrong: " + e.getMessage());
        }
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

    public void addAmountGrocery(String[] amountAndUnit) throws Exception {
        try {
            //legg til en mengde
            double addAmount = Double.parseDouble(amountAndUnit[0]);
            SI addUnit = SI_manager.getUnit(amountAndUnit[1]);
            grocery.addAmount(addAmount, addUnit);
        }
        catch (Exception e) {
            throw new Exception("Could not add amount to the grocery for the following reason: " + e.getMessage());
        }
    }

    public void removeAmountGrocery(String[] amountAndUnit) throws Exception {
        try{
            //trekk fra en mengde
            SI newUnit = SI_manager.getUnit(amountAndUnit[1]);
            double amount = Double.parseDouble(amountAndUnit[0]);
            grocery.removeAmount(amount, newUnit);
        }
        catch (Exception e) {
            throw new Exception("Could not remove amount from the grocery for the following reason: " + e.getMessage());
        }
    }
}
