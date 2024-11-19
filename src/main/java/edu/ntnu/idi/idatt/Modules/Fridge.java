package edu.ntnu.idi.idatt.Modules;

import java.util.ArrayList;
import java.util.List;

/**
 * <strong>Description:</strong><br>
 * A mutable class that is supposed to act as a Fridge. This Fridge-class
 * stores Grocery-objects in an {@code ArrayList} of type {@link Grocery}.<br><br>
 *
 * <strong>Datafields:</strong><br>
 * {@code groceryList} - An {@link ArrayList} with content of type {@link Grocery}.<br><br>
 *
 * <strong>Methods:</strong><br>
 * {@link #getGroceryList} - Get-method, fetches groceryList of all available Grocerys stored in the fridge
 * with incapsulation.<br>
 * {@link #addGrocery} - Method for adding a Grocery in the Fridge.<br>
 * {@link #removeGrocery} - Method for removing a Grocery from the Fridge.<br>
 * {@link #toString()} - Method for dumping the contents of the object as a string. For debugging or just
 * simly displaying the whole Fridge.<br>
 */
public class Fridge {
    private final ArrayList<Grocery> groceryList;

    public Fridge() {
        this.groceryList = new ArrayList<>(0);
    }

    /**
     * <strong>Description:</strong><br>
     * Get-method for the {@link Fridge}-object. To ensure as much incaptulation as possible,
     * get- and set-methods should be used to access datafields of other classes. Datafields such
     * as{@code #groceryList} should also be private and final if possible.
     *
     * @return An object of type {@link List}, which is more accessable and abstract through polymorpism
     * than an ArrayList would be.
     */
    public List<Grocery> getGroceryList() {
        return this.groceryList.stream().toList();
    }

    /**
     * <strong>Description:</strong><br>
     * A method that effectively adds a {@link Grocery} to the {@code Fridge}. If the {@code groceryList}
     * is empty or does not contain a matching Grocery, the Grocery is added to the Fridge. If the groceryList
     * has a matching Grocery, the Grocerys amount and unit is added to the existing Grocery via the
     * {@link Grocery#addAmount addAmount()} method.
     *
     * @param grocery An Object of type {@link Grocery} that is going to be added to the list.
     */
    public void addGrocery(final Grocery grocery) {
        if (!groceryList.isEmpty()) {
            final Grocery matchingGrocery = this.groceryList.stream()
                    .filter(g -> g.equals(grocery) || (g.getName().equalsIgnoreCase(grocery.getName()) && g.getDate().equals(grocery.getDate())))
                    .findFirst()
                    .orElse(null);

            if (matchingGrocery != null) {
                matchingGrocery.addAmount(grocery.getQuantity(), grocery.getUnit());
                return;
            }
        }
        this.groceryList.add(grocery);
    }

    /**
     * <strong>Description:</strong><br>
     * A method for removing a {@link Grocery} from the {@code Fridge}.
     * Checking if the grocery is in the {@code groceryList} before removal.<br>
     *
     * @param grocery An object of type {@link Grocery}. Represents the supposed Grocery which
     *                will be removed from the Fridge.
     */
    public void removeGrocery(final Grocery grocery) {
        if (groceryList.contains(grocery)) {
            final int index = groceryList.indexOf(grocery);
            if (index != -1) {
                groceryList.remove(index);
            }
            else {
                throw new IllegalArgumentException("Argument not valid! Cannot remove " + grocery.getName() + " from Grocery list!");
            }
        }
        else {
            throw new IllegalArgumentException("Cannot remove grocery \"" + grocery.getName()
                + "\" from Fridge. Grocery does not currently exist in Fridge.");
        }
    }

    /**
     * <strong>Description:</strong><br>
     * Overridden method for writing the class as a String.<br>
     *
     * @return The {@link StringBuilder} as a String. Effectively a {@code String} of the content of the Fridge.
     */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("\nKlasse Fridge;\n");
        str.append("    Innhold:");
        for(Grocery g : this.groceryList) {
            str.append(g.toString()).append("\n\n");
        }
        return str.toString();
    }
}
