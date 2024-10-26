package edu.ntnu.idi.idatt;

import java.util.ArrayList;

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
        if (this.groceryList.contains(grocery)) {
            throw new IllegalArgumentException("Argument not valid! Cannot add two groceries with same values (name and date).");
        }
        else {
            this.groceryList.add(grocery);
        }

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


}
