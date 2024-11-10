package edu.ntnu.idi.idatt.Manager;

import edu.ntnu.idi.idatt.Utils.SI;
import edu.ntnu.idi.idatt.modules.Fridge;
import edu.ntnu.idi.idatt.modules.Grocery;

import static edu.ntnu.idi.idatt.Client.Client.getInput;

public class GroceryManager {
    private final Fridge fridge;
    private final Grocery grocery;
    private final SI unit;

    public GroceryManager(Grocery g) {
        this.grocery = g;
        this.fridge = this.grocery.getFridge();
        this.unit = this.grocery.getUnit();

        convertUnit();
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

    private void convertUnit() {
        final double groceryQuantity = grocery.getQuantity();
        final String groceryUnit = unit.getPrefix();

        if (groceryQuantity < 1.0 && groceryUnit.isEmpty()) {
            grocery.setUnit(new SI("Desiliter", "dL","L", "Desi"));
            grocery.setQuantity(groceryQuantity*10);
        }
        else if (groceryQuantity >= 10.0 && groceryUnit.equals("Desi")) {
            grocery.setUnit(new SI("Liter", "L", "L","Desi"));
            grocery.setQuantity(groceryQuantity/10);
        }
        else if (groceryQuantity < 1.0 && groceryUnit.equals("Desi")) {
            grocery.setUnit(new SI("Milliliter", "mL", "L","Milli"));
            grocery.setQuantity(groceryQuantity*100);
        }
        else if (groceryQuantity >= 100 && groceryUnit.equals("Milli")) {
            grocery.setUnit(new SI("Desiliter", "dL", "L","Desi"));
            grocery.setQuantity(groceryQuantity/100);
        }
        else if (groceryQuantity < 1.0 && groceryUnit.equals("Kilo")) {
            grocery.setUnit(new SI("Gram", "g", "kg", ""));
            grocery.setQuantity(groceryQuantity*1000);
        }
        else if (groceryQuantity >= 1000.0 && groceryUnit.equals("Gram")) {
            grocery.setUnit(new SI("Kilogram", "kg", "kg", "Kilo"));
            grocery.setQuantity(groceryQuantity/1000);
        }
    }

    public void addAmount(final double amount, final SI amountUnit) {
        final double currentQuantity = grocery.getQuantity();
        final String groceryUnitAbrev = grocery.getUnit().getAbrev();
        final String amountUnitAbrev = amountUnit.getAbrev();
        final double amountCF = amountUnit.getConvertionFactor();
        final double groceryCF = unit.getConvertionFactor();

        if (amount > 0) {
            if (groceryUnitAbrev.equals("stk") || amountUnitAbrev.equals("stk")) {
                System.err.println("Kan ikke legge til et antall med en annen målenhet enn \"stk\" når varen er oppgitt i \"stk\".");
                return;
            }
            else {
                grocery.setQuantity(
                    (double)(Math.round((currentQuantity*groceryCF + amount*amountCF)*100))/100
                );
            }
            convertUnit();
            System.out.println("Fjernet fra vare med vareID " + grocery.getGroceryID());
        }
        else {
            throw new IllegalArgumentException("Illegal argument error: Cannot add a negative amount.");
        }
    }

    public void removeAmount(final double amount, SI amountUnit) {
        double currentQuantity = grocery.getQuantity();
        final String groceryUnitAbrev = unit.getAbrev();
        final String amountUnitAbrev = amountUnit.getAbrev();
        final double amountCF = amountUnit.getConvertionFactor();
        final double groceryCF = unit.getConvertionFactor();

        if (amount > 0) {
            if (groceryUnitAbrev.equals("stk") || amountUnitAbrev.equals("stk")) {
                System.err.println("Kan ikke trekke fra et antall med en annen målenhet enn \"stk\" når varen er oppgitt i \"stk\".");
                return;
            }
            else {
                grocery.setQuantity(
                    (double)(Math.round((currentQuantity*groceryCF - amount*amountCF)*100))/100
                );
                currentQuantity = grocery.getQuantity();
            }
            convertUnit();

            if (currentQuantity <= 0) {
                fridge.removeGrocery(grocery);
            }
            System.out.println("Fjernet fra vare med vareID " + grocery.getGroceryID());
        }
        else {
            throw new IllegalArgumentException("Illegal argument error: Cannot remove a negative amount.");
        }
    }

    public double getPricePerQuantity() {
        double result;
        if (unit.getAbrev().equals(unit.getUnitForPrice())) {
            result = grocery.getQuantity() * grocery.getPrice();
        }
        else {
            result = grocery.getPrice() * (grocery.getQuantity()*unit.getConvertionFactor());
        }

        return result;
    }

    public void addAmountGrocery() {
        try {
            //legg til en mengde
            String[] amountAndUnit = getAmountAndUnit();

            SI unit = SI_manager.getUnit(amountAndUnit[1]);
            double amount = Double.parseDouble(amountAndUnit[0]);

            addAmount(amount, unit);
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

            removeAmount(amount, unit);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
