package edu.ntnu.idi.idatt.Modules;

import edu.ntnu.idi.idatt.Manager.GroceryManager;

import java.util.ArrayList;

public class Fridge {
    final private ArrayList<Grocery> groceryList;

    public Fridge() {
        this.groceryList = new ArrayList<>(0);
    }
    public ArrayList<Grocery> getGroceryList() {
        return this.groceryList;
    }

    public void addGrocery(final Grocery grocery) {
        final GroceryManager gm = new GroceryManager(grocery);
        gm.convertUnit();
        if (!groceryList.isEmpty()) {
            for (Grocery g : this.groceryList) {
                if (g.equals(grocery) || (g.getName().equalsIgnoreCase(grocery.getName()) && g.getDate().equals(grocery.getDate()))) {
                    gm.addAmount(grocery.getQuantity(), grocery.getUnit());
                    return;
                }
            }
        }
        this.groceryList.add(grocery);
    }

    public void removeGrocery(final Grocery grocery) {
        if (groceryList.contains(grocery)) {
            final int index = groceryList.indexOf(grocery);
            if (index != -1) {
                groceryList.remove(index);
            }
            else {
                throw new IllegalArgumentException("Argument not valid! Cannot remove grocery from grocery list.");
            }
        }
        else {
            throw new IllegalArgumentException("Cannot remove grocery \"" + grocery.getName()
                + "\" from Fridge. Grocery does not currently exist in Fridge.");
        }
    }
}
