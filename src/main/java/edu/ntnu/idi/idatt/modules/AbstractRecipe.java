package edu.ntnu.idi.idatt.modules;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractRecipe {
  private final Fridge fridge;
  private final String name;
  private final String description;
  private final String[] directions;
  private final int portion;
  private final HashSet<Grocery> recipes;

  protected AbstractRecipe(String name,
                        String description,
                        String[] directions,
                        int portion,
                        List<Grocery> recipes,
                        Fridge fridge) {
    this.name = name;
    this.description = description;
    this.directions = directions;
    this.portion = portion;
    this.recipes = new HashSet<>(recipes);
    this.fridge = fridge;
  }

  public double matchingGroceries() {
    if (recipes.isEmpty()) {
      return 0;
    } else {
      return (double) recipes.stream()
          .filter(g -> fridge.getGroceryList().stream()
              .anyMatch(gr -> gr.getName().equalsIgnoreCase(g.getName())))
          .toList()
          .size() / recipes.size();
    }
  }

  public LocalDate avrageDate() {
    AtomicInteger avrDay = new AtomicInteger();
    AtomicInteger avrMonth = new AtomicInteger();
    AtomicInteger avrYear = new AtomicInteger();

    AtomicInteger correspondingGroceries = new AtomicInteger();
    getRecipes().forEach(g -> {
      Grocery correspondingFridgeItem = fridge.getGroceryList().stream()
          .filter(gr -> gr.getName().equals(g.getName()))
          .min(Comparator.comparing(Grocery::getDate))
          .orElse(null);

      if (correspondingFridgeItem != null) {
        correspondingGroceries.getAndIncrement();
        int day = correspondingFridgeItem.getDate().getDayOfMonth();
        int month = correspondingFridgeItem.getDate().getMonthValue();
        int year = correspondingFridgeItem.getDate().getYear();
        avrDay.addAndGet(day);
        avrMonth.addAndGet(month);
        avrYear.addAndGet(year);
      }
    });

    if (correspondingGroceries.get() == 0) {
      return LocalDate.now().plusYears(100);
    } else {
      avrDay.updateAndGet(v -> v / correspondingGroceries.get());
      avrMonth.updateAndGet(v -> v / correspondingGroceries.get());
      avrYear.updateAndGet(v -> v / correspondingGroceries.get());

      return LocalDate.of(avrYear.get(), avrMonth.get(), avrDay.get());
    }
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public String[] getDirections() {
    String[] deepCopy = new String[directions.length];
    AtomicInteger i = new AtomicInteger();
    Arrays.asList(directions).forEach(d -> deepCopy[i.getAndIncrement()] = d);
    return deepCopy;
  }

  public int getPortion() {
    return portion;
  }

  public List<Grocery> getRecipes() {
    return new ArrayList<>(recipes).stream().toList();
  }

  public Fridge getFridge() {
    return fridge;
  }
}
