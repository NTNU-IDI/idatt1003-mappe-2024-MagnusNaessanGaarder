package edu.ntnu.idi.idatt.Manager;

import edu.ntnu.idi.idatt.modules.Fridge;
import edu.ntnu.idi.idatt.modules.Grocery;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

public class FridgeManager {
    final private Fridge fridge;
    final private ArrayList<Grocery> groceryList;

    public FridgeManager (Fridge fridge) {
        this.fridge = fridge;
        this.groceryList = this.fridge.getGroceryList();
    }

    public Grocery getGrocery(final int index) {
        return fridge.getGroceryList().get(index);
    }

    public int getGroceryListIndex(final int groceryID) {
        return IntStream.range(0, fridge.getGroceryList().size())
                .filter(i -> groceryList.get(i).getGroceryID() == groceryID)
                .findFirst()
                .orElse(-1);
    }

    public Grocery search(final String name) {
        for (Grocery g : groceryList) {
            if (g.getName().equals(name)) {
                return g;
            }
        }
        return null;
    }

    public void addGrocery(final Grocery grocery) {
        for (Grocery groceryItem : groceryList) {
            if (groceryItem.equals(grocery)) {
                fridge.getGroceryList().get(groceryList.indexOf(grocery)).addAmount(grocery.getQuantity(), grocery.getUnit());
                return;
            }
        }

        groceryList.add(grocery);
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
            throw new IllegalArgumentException("Cannot remove grocery \"" +
                                               grocery.getName() + "\" from Fridge. Grocery does not currently exist in Fridge.");
        }
    }

    private List<Grocery> getListSortedDate(List<Grocery> list) {
        if (list.isEmpty() || list.size() == 1) {
            return list;
        }

        return list.stream()
                .sorted(Comparator.comparing(Grocery::getDate))
                .toList();
    }

    public List<Grocery> getExpiredList() {
        if (groceryList.isEmpty()) {
            return fridge.getGroceryList();
        }
        return this.getListSortedDate(
                this.groceryList.stream()
                        .filter(Grocery::hasExpired)
                        .toList());
    }

    public List<Grocery> getNearExpList() {
        return this.getListSortedDate(
                groceryList.stream()
                        .filter(g -> g.getDate().isAfter(LocalDate.now()) && g.getDate().isBefore(LocalDate.now().plusDays(4)))
                        .toList());
    }

    public List<Grocery> getRestGroceryList() {
        return this.getListSortedDate(
                groceryList.stream()
                        .filter(g -> g.getDate().isAfter(LocalDate.now().plusDays(3)))
                        .toList());
    }

    public String getMoneyLoss() {
        List<Grocery> expiredGroceries = getExpiredList();

        if (expiredGroceries.isEmpty()) {
            return "Ingen varer er gått ut på dato";
        }

        double sum = 0.0;
        for (Grocery grocery : expiredGroceries) {
            sum += grocery.getPricePerQuantity();
        }
        String str = "%d varer er gått ut på dato.\n";
        str += "Du har tapt %.2f kr.";

        return String.format(str, expiredGroceries.size(), sum);
    }

    public double getTotalPrice() {
        double sum = 0;
        for(Grocery g : groceryList) {
            sum += g.getPricePerQuantity();
        }
        return sum;
    }
}
