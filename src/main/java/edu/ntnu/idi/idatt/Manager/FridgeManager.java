package edu.ntnu.idi.idatt.Manager;

import edu.ntnu.idi.idatt.Modules.Fridge;
import edu.ntnu.idi.idatt.Modules.Grocery;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

public class FridgeManager {
    private final Fridge fridge;

    public FridgeManager (Fridge fridge) {
        this.fridge = fridge;
    }

    public Grocery getGrocery(final int index) {
        if (fridge.getGroceryList().isEmpty()) {
            return null;
        }
        else if (index < 0 || index >= fridge.getGroceryList().size()) {
            return null;
        }
        else {
            return fridge.getGroceryList().get(index);
        }
    }

    public int getGroceryListIndex(final int groceryID) {
        return IntStream.range(0, fridge.getGroceryList().size())
                .filter(i -> fridge.getGroceryList().get(i).getGroceryID() == groceryID)
                .findFirst()
                .orElse(-1);
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
        if (fridge.getGroceryList().isEmpty()) {
            return fridge.getGroceryList();
        }
        return this.getListSortedDate(
                fridge.getGroceryList().stream()
                        .filter(Grocery::hasExpired)
                        .toList());
    }

    public List<Grocery> getNearExpList() {
        return this.getListSortedDate(
                fridge.getGroceryList().stream()
                        .filter(g -> g.getDate().isAfter(LocalDate.now()) && g.getDate().isBefore(LocalDate.now().plusDays(4)))
                        .toList());
    }

    public List<Grocery> getRestGroceryList() {
        return this.getListSortedDate(
                fridge.getGroceryList().stream()
                        .filter(g -> g.getDate().isAfter(LocalDate.now().plusDays(3)))
                        .toList());
    }

    public String getMoneyLossStr() {
        List<Grocery> expiredGroceries = getExpiredList();
        if (expiredGroceries.isEmpty()) {
            return "Ingen varer er gått ut på dato";
        }
        String str = "          %d varer er gått ut på dato.\n";
        str +=       "          Du har tapt %.2f kr.";

        return String.format(str, expiredGroceries.size(), this.getMoneyLoss());
    }

    public double getMoneyLoss() {
        List<Grocery> expiredGroceries = getExpiredList();

        double sum = 0;
        for (Grocery g : expiredGroceries) {
            final GroceryManager gm = new GroceryManager(g);
            sum += gm.getPricePerQuantity();
        }

        return sum;
    }

    public double getTotalPrice() {
        double sum = 0;
        for(Grocery g : fridge.getGroceryList()) {
            final GroceryManager gm = new GroceryManager(g);
            sum += gm.getPricePerQuantity();
        }
        return sum;
    }
}
