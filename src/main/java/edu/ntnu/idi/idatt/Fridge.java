package edu.ntnu.idi.idatt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

public class Fridge {
    private ArrayList<Grocery> groceryList;

    public Fridge(final Grocery grocery) {
        this.groceryList = new ArrayList<>(0);
        this.groceryList.add(grocery);
    }

    public Fridge() {
        this.groceryList = new ArrayList<>(0);
    }

    public ArrayList<Grocery> getGroceryList() {
        return this.groceryList;
    }

    public Grocery getGrocery(int index) {
        return groceryList.get(index);
    }

    public void addGrocery(final Grocery grocery) {
        for (Grocery groceryItem : groceryList) {
            if (groceryItem.equals(grocery)) {
                this.groceryList.get(groceryList.indexOf(grocery)).addAmount(grocery.getQuantity(), grocery.getUnit());
                return;
            }
        }

        this.groceryList.add(grocery);
    }

    public void removeGrocery(final Grocery grocery) {
        if (this.groceryList.contains(grocery)) {
            int index = this.groceryList.indexOf(grocery);
            if (index != -1) {
                this.groceryList.remove(index);
            }
            else {
                throw new IllegalArgumentException("Argument not valid! Cannot remove grocery from grocery list.");
            }
        }
        else {
            throw new IllegalArgumentException("Cannot remove grocery \"" +
                    grocery.getName() + "\" from Fridge. Grocery does not currently exist in Fridge.");
        }
    }

    public String getMoneyLoss() {
        ArrayList<Grocery> expiredGroceries = new ArrayList<>();
        for (Grocery grocery : groceryList) {
            if (grocery.hasExpired()) {
                expiredGroceries.add(grocery);
            }
        }

        //Funker ikke med stream
        /*ArrayList<Grocery> expiredGroceries = Stream.of(groceryList)
                .filter(grocery -> grocery.hasExpired());*/

        if (expiredGroceries.isEmpty()) {
            return "Ingen varer er gått ut på dato";
        }

        double sum = 0.0;
        for (Grocery grocery : expiredGroceries) {
            if (grocery.getUnit().getAbrev().equals(grocery.getUnit().getUnitForPrice())) {
                sum += grocery.getQuantity() * grocery.getPrice();
            }
            else {
                sum += grocery.getPrice() * (grocery.getQuantity()*grocery.getUnit().getConvertionFactor());
            }
        }
        String str = "%d varer er gått ut på dato.\n";
        str += "Du har tapt %.2f kr.";

        return String.format(str, expiredGroceries.size(), sum);
    }
}
