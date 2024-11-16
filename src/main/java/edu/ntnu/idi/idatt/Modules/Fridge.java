package edu.ntnu.idi.idatt.Modules;

import edu.ntnu.idi.idatt.Manager.GroceryManager;

import java.util.ArrayList;
import java.util.List;

public class Fridge {
    private final ArrayList<Grocery> groceryList;

    public Fridge() {
        this.groceryList = new ArrayList<>(0);
    }
    public List<Grocery> getGroceryList() {
        return this.groceryList.stream().toList();
    }

    public void addGrocery(final Grocery grocery) {
        if (!groceryList.isEmpty()) {
            final Grocery matchingGrocery = this.groceryList.stream()
                    .filter(g -> g.equals(grocery) || (g.getName().equalsIgnoreCase(grocery.getName()) && g.getDate().equals(grocery.getDate())))
                    .findFirst()
                    .orElse(null);

            if (matchingGrocery != null) {
                grocery.addAmount(grocery.getQuantity(), grocery.getUnit());
                return;
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

    @Override
    public String toString() {
        String str = "\nKlasse Fridge;\n";
        str += "    Innhold:";
        for(Grocery g : this.groceryList) {
            str += g.toString() + "\n\n";
        }
        return str;
    }
}
