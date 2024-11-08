package edu.ntnu.idi.idatt.modules;

import java.util.ArrayList;

public class Fridge {
    final private ArrayList<Grocery> groceryList;
    final private int NEAR_EXPIRATION = 3;

    public Fridge() {
        this.groceryList = new ArrayList<>(0);
    }

    public ArrayList<Grocery> getGroceryList() {
        return this.groceryList;
    }
}
