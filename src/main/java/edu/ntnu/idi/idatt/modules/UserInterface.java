package edu.ntnu.idi.idatt.modules;

import edu.ntnu.idi.idatt.manager.CookBookManager;
import edu.ntnu.idi.idatt.manager.FridgeManager;
import edu.ntnu.idi.idatt.manager.GroceryManager;
import edu.ntnu.idi.idatt.manager.SI_Manager;
import edu.ntnu.idi.idatt.utils.AbstractOption;
import edu.ntnu.idi.idatt.utils.AbstractTable;
import edu.ntnu.idi.idatt.utils.Display;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <strong>Heritage</strong><br>
 * This class inherits traits from the super class {@link AbstractOption}.<br><br>
 *
 * <strong>Description</strong><br>
 * A class that handles the Text-based User Interface(TUI) of the application. <br><br>
 *
 * <strong>Datafields</strong><br>
 * {@code str} - A private object of type {@link StringBuilder}, for large
 * and structured Strings.<br>
 * {@code running} - A private boolean determining if the application
 * continues.<br>
 * {@code fridge} - An object of type {@link Fridge} acting as the
 * foodstorage for the application.<br>
 * {@code fm} - An object of type {@link FridgeManager}. Helps managing the Fridge.<br>
 */
public class UserInterface extends AbstractOption {
  //global variables
  private final Fridge fridge;
  private final CookBook cookBook;
  private final FridgeManager fm;
  private final CookBookManager cbm;
  private StringBuilder str;
  private boolean running;

  /**
   * <strong>Description</strong><br>
   * A constructor instantizing the class and initializing the datafields.
   */
  public UserInterface() {
    this.str = new StringBuilder();
    this.fridge = new Fridge();
    this.fm = new FridgeManager(fridge);
    this.cookBook = new CookBook();
    this.cbm = new CookBookManager(cookBook, fridge);
  }

  /**
   * <strong>Description</strong><br>
   * A method used to test four different instances of a grocery.
   */
  public void init() {
    //test av Grocery-objekt
    Grocery.resetID();
    Recipe.resetID();

    final SI g = new SI("Gram", "g", "kg", "");
    final SI kg = new SI("Kilogram", "kg", "kg", "Kilo");
    final SI stk = new SI("Stykker", "stk", "stk", "");
    final SI dl = new SI("Desiliter", "dL", "L", "Desi");
    final SI l = new SI("Liter", "L", "L", "");
    final SI ml = new SI("Milliliter", "mL", "L", "Milli");
    final SI ts = new SI("Teskje", "ts", "", "Te");
    final SI ss = new SI("Spiseskje", "ss", "", "Spise");

    final Grocery grocery1 = new Grocery("Mel", g, 2000, 
        LocalDate.now().minusDays(2), 200, fridge);
    final Grocery grocery2 = new Grocery("Bananer", stk, 1,
        LocalDate.now(), 49.90, fridge);
    final Grocery grocery3 = new Grocery("Mel", g, 500, 
        LocalDate.now().plusDays(4), 200, fridge);
    final Grocery grocery4 = new Grocery("Kraft", l, 0.5, 
        LocalDate.now().plusDays(1), 259.99, fridge);
    final Grocery grocery5 = new Grocery("Melk", dl, 2,
        LocalDate.now().plusYears(1), 1, fridge);
    final Grocery grocery6 = new Grocery("Egg", stk, 3,
        LocalDate.now(), 1, fridge);


    this.fridge.addGrocery(grocery1);
    this.fridge.addGrocery(grocery2);
    this.fridge.addGrocery(grocery3);
    this.fridge.addGrocery(grocery4);
    this.fridge.addGrocery(grocery4);
    this.fridge.addGrocery(grocery5);
    this.fridge.addGrocery(grocery6);

    //tester å legge til og trekke fra fra varer i kjøleskapet
    grocery3.removeAmount(500, g);
    grocery1.addAmount(6, stk);

    final Recipe recipe1 = new Recipe("Banankake", "God!!", 
        new String[] {"ins1:", "ins2:"}, 4,
        new ArrayList<>(Arrays.asList(
            new Grocery("Banan", stk, 2, null, 1, fridge),
            new Grocery("Mel", kg, 0.5, null, 1, fridge),
            new Grocery("Egg", stk, 2, null, 1, fridge),
            new Grocery("Vaniljesukker", ts, 4, null, 1, fridge))), fridge);
    final Recipe recipe2 = new Recipe("Brød", "Luftig!!", 
        new String[] {"ins1:", "ins2:", "ins3:"}, 6,
        new ArrayList<>(Arrays.asList(
            new Grocery("Mel", g, 500, null, 1, fridge),
            new Grocery("Melk", dl, 2, null, 1, fridge),
            new Grocery("Egg", stk, 3, null, 1, fridge),
            new Grocery("Gjær", ss, 1, null, 1, fridge))), fridge);
    final Recipe recipe3 = new Recipe("Penne Al Arabiata", "Spicy og digg!!", 
        new String[] {"ins1:", "ins2:"}, 4,
        new ArrayList<>(Arrays.asList(
            new Grocery("Chilly", stk, 1, null, 1, fridge),
            new Grocery("Olivenolje", ml, 100, null, 1, fridge),
            new Grocery("Hvitløksfedd", stk, 2, null, 1, fridge),
            new Grocery("Hakkede tomater, Boks", stk, 2, null, 1, fridge),
            new Grocery("Persille", ss, 2, null, 1, fridge),
            new Grocery("Salt", ts, 2, null, 1, fridge))), fridge);
    final Recipe recipe4 = new Recipe("Naan Brød", "Deilig!!", 
        new String[]{"ajsd", "askhd"}, 2,
        new ArrayList<>(Arrays.asList(
            new Grocery("Egg", stk, 2, null, 1, fridge),
            new Grocery("Mel", dl, 2, null, 1, fridge),
            new Grocery("Koreander", ts, 1, null, 1, fridge),
            new Grocery("Salt", ts, 1, null, 1, fridge))), fridge);

    cookBook.addRecipe(recipe1);
    cookBook.addRecipe(recipe2);
    cookBook.addRecipe(recipe3);
    cookBook.addRecipe(recipe4);

    this.running = true;
  }

  /**
   * A method used to start the actual application.
   */
  public void start() {
    while (running) {
      showMenu();
    }
  }

  private void showMenu() {

    // Menyvalg i konsollen
    clearScreen();
    str = new StringBuilder();

    final String menuStr =
        AbstractTable.createMenuTable("HOVEDMENY - MATLAGER", "Liste med tilgjengelige varer");
    str.append(menuStr);
    str.append(Display.menuList(fridge.getGroceryList(), "Fant ingen varer i kjøleskapet"))
        .append("\n");

    str.append("            Velg fra listen nedenfor:").append("\n\n");

    str.append("                   [1] Legg til vare.\n");
    str.append("                   [2] Fjern fra vare.\n");
    str.append("                   [3] Oversikt over kjøleskapet.\n");
    str.append("                   [4] Søk etter vare.\n");
    str.append("                   [5] Samlet verdi av varer.\n");
    str.append("                   [6] Oversikt over kokebok.\n");
    str.append("                   [7] Avslutt.\n");
    System.out.println(str);

    try {
      int userInput = Integer.parseInt(getInput());
      super.menuOption(this, userInput);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  /**
   * Determines which action to do based on the usergiven parameter.
   *
   * @param ui a String element given by the user via the {@link UserInterface}-method.
   */
  @Override
  protected void menuOption(UserInterface ui, int userInput) {
    super.menuOption(this, userInput);
  }

  /**
   * <strong>Description:</strong><br>
   * A method for fetching user input and ultimatly adding a new {@link Grocery}
   * based on the user inputs.<br>
   */
  public void addToFridge() {
    str = new StringBuilder();
    final String title =
        AbstractTable.createMenuTable("LEGG TIL VARE", "Fyll ut feltene nedenfor "
            + "for å legge til vare:");
    str.append(title);
    System.out.println(str);


    //navn på varen
    String name;
    System.out.print("          Skriv navnet på varen: ");
    name = getInput();

    //mengden og enheten av varen
    SI measure = null;
    double quantity = 0;

    boolean retry = true;
    while (retry) {
      try {
        String[] amountAndUnit = fetchAmountAndUnit();
        measure = SI_Manager.getUnit(amountAndUnit[1]);
        quantity = Double.parseDouble(amountAndUnit[0]);
        retry = false;
      } catch (Exception e) {
        System.out.println(e.getMessage());
      }
    }

    //Best-før dato
    LocalDate date;
    date = fetchDate();

    //pris
    double price;
    price = fetchPrice(measure);

    //sjekker om brukeren ønsker å legge til varen
    clearScreen();
    str = new StringBuilder();
    Display display = new Display(fm);
    String table = display.createGroceryTable(
        name,
        new String[] {"Mengde", "Best-før dato", "pris"},
        new String[] {quantity + " " + measure.getAbrev(), date + "",
            price + " kr/" + measure.getUnitForPrice()},
        80
    );
    str.append(table);
    System.out.println(str);

    //sjekker om brukeren ønsker å legge til varen i kjøleskapet
    char yesNoErr = 'e';
    while (yesNoErr == 'e') {
      yesNoErr = option("Er du sikker på at du ønsker å legge til denne varen til kjøleskapet?");
    }

    //sjekker om brukeren har svart ja.
    if (yesNoErr == 'y') {
      //sjekker om varen allerede er i kjøleskapet
      Grocery grocery = new Grocery(name, measure, quantity, date, price, fridge);
      fridge.addGrocery(grocery);
    } else {
      //fortesetter med ny laging av vare hvis brukeren ikke ønsker å beholde forrige vare
      addToFridge();
    }
  }

  /**
   * <strong>Description</strong><br>
   * A method for fetching amount and unit name from a user input
   as well as handling exceptions.<br>
   *
   * @return An array of both amount and unit name of a Grocery, as a
   string-type array.
   */
  private String[] fetchAmountAndUnit() {
    String userInput;
    String[] amountAndUnit = new String[0];
    boolean retry = true;
    //går i loop helt til ingen feilmeldinger foregår
    while (retry) {
      try {
        System.out.print("          Skriv mengden på varen (f.eks 2 gram / desiliter / stykker): ");
        userInput = getInputStatic();
        amountAndUnit = GroceryManager.getAmountAndUnit(userInput);
        retry = false;
      } catch (Exception e) {
        System.out.println(e.getMessage());
      }
    }
    return amountAndUnit;
  }

  /**
   * <strong>Description</strong><br>
   * A method for fetching a price of a Grocery from a user input.<br>
   * Important! The price is given in price-amount per main pricepoint unit,
   * and not in price per unit. E.g. 100kr / kg, and not 100kr / g.<br>
   *
   * @param measure An object of type {@link SI} used to get the main
   *                price point unit.
   * @return A {@code Double} representing the price amount per main
   pricepoint unit.
   */
  private double fetchPrice(SI measure) {
    boolean retry = true;
    double price = 0;
    //Går i loop helt til ingen feilmeldinger foregår eller til
    //det registreres at parameteren er ugyldig (Null).
    while (retry) {
      try {
        price = priceFetcher(measure);
        retry = false;
      } catch (Exception e) {
        System.out.println(e.getMessage());
      }
    }
    return price;
  }

  /**
   * <strong>Description</strong><br>
   * A method fetching and formating a price amount from {@link #fetchPrice(SI)}.<br>
   *
   * @param measure An object of type {@link SI} used to get the main
   *                price point unit.
   * @return A {@code double} floating point-value representing the price amount.
   * @throws Exception for handling NumberFormatException or other unforseen exceptions.
   */
  private double priceFetcher(SI measure) throws Exception {
    try {
      //Melding til bruker som spesifiserer hva slags input som forventes
      System.out.printf("          Skriv pris. Oppgi pris i kr/%s f.eks (20 kr/%s): ",
          measure.getUnitForPrice(),
          measure.getUnitForPrice()
      );

      //henter input
      final String userInput = getInput();

      //formaterer og returnerer input
      String priceStr = String.join("", userInput.split("[^,.\\d]"));
      return Double.parseDouble(String.join(".", priceStr.split("[,.]")));

    } catch (NumberFormatException e) {
      //gir feilmelding om noe går galt med formateringen fra tekststreng til double
      throw new NumberFormatException(
          "The given price cannot be parsed to a Double. Please write a valid decimal number."
      );
    } catch (Exception e) {
      //gir en feilmelding om en annen uforutsett feilmelding opptrer
      throw new Exception("Unexpected error while fetching price: " + e.getMessage());
    }
  }

  /**
   * <strong>Description</strong><br>
   * A method for fetching a expiration date for a Grocery from a useri input.
   *
   * @return A {@code LocalDate} expiration date for a grocery.
   */
  private LocalDate fetchDate() {
    boolean retry = true;
    LocalDate date = null;
    while (retry) {
      try {
        System.out.print("          Skriv en Best-før dato på formen DD-MM-YYYY: ");
        date = dateFetcher(getInput());
      } catch (Exception e) {
        System.out.println(e.getMessage());
      }
      if (date != null) {
        retry = false;
      }
    }
    return date;
  }

  /**
   * <strong>Description</strong><br>
   * A method fetching and formating user input for an expiration date
   * from {@link #fetchDate()}.<br>
   *
   * @param userInput A user input of type String representing an unformated
   *                  date.
   * @return A {@code LocalDate} representing the expiration date of a Grocery.
   * @throws Exception for handling exceptions with parsing the string or other
   *                   unforseen exceptions.
   */
  private LocalDate dateFetcher(String userInput) throws Exception {
    try {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
      return LocalDate.parse(userInput, formatter);
    } catch (Exception e) {
      throw new Exception("Ugyldig format - Skriv datoen på formen DD-MM-YYYY.");
    }
  }

  /**
   * <strong>Description:</strong>
   * A method for displaying a list of available groceries and handling
   * commands from the user, as well as handeling Exceptions.
   */
  public void removeFromFridge() {
    try {
      displayRemoveList();
      removeHandler();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  /**
   * <strong>Description:</strong><br>
   * A method for displaying groceries to chose from, then remove an
   * amount from that grocery.
   */
  private void displayRemoveList() {
    str = new StringBuilder();
    final String title = AbstractTable.createMenuTable("FJERN FRA KJØLESKAPET",
        "Bruk en komando nedenfor for å interagere med matvarer i kjøleskapet:");
    str.append(title);

    if (fridge.getGroceryList().isEmpty()) {
      str.append("            ---- Ingen varer er lagt til i kjøleskapet ----");
    } else {
      str.append(Display.displayList(fridge.getGroceryList(), 81));
    }
  }

  /**
   * <strong>Description:</strong><br>
   * A method displaying available commands, and handeling fetched commands.
   */
  private void removeHandler() {
    boolean retry = true;
    while (retry) {
      clearScreen();
      try {
        System.out.println(str);
        System.out.println("""
            
            Skriv "-e" for å gå tilbake til menyen, eller "-remove [Vare ID]" for å fjerne en \
            mengde fra en vare.
            [Vare ID] skal skrives som et tall.""");
        String userInput = getInput();
        retry = removeInputHandler(userInput);
        if (retry) {
          displayRemoveList();
        }
      } catch (Exception e) {
        System.out.println(e.getMessage());
      }
    }
  }

  /**
   * <strong>Description:</strong><br>
   * A method for interpreting a userinput and play a corresponding command
   * from {@link #removeHandler()}.<br>
   *
   * @param userInput A String representing an usergiven command.
   * @return A boolean determening if the {@link #removeHandler()} should repeat.
   * @throws Exception for handling exceptions with parsing the string or other
   *                   unforseen exceptions.
   */
  private boolean removeInputHandler(String userInput) throws Exception {
    if (userInput.equals("-e")) {
      return false;
    } else if (userInput.contains("-remove") && !fm.getExpiredList().isEmpty()) {
      int userInt;
      boolean retry = true;
      while (retry) {
        try {
          if (userInput.equals("-remove")) {
            System.out.print("Skriv en vareID du ønsker å fjerne fra: ");
            String input = getInput();
            userInt = Integer.parseInt(input);
          } else {
            userInt = Integer.parseInt(String.join("", userInput.split("\\D")));
          }

          final int finalUserInt = userInt;
          final Grocery grocery = fridge.getGroceryList().stream()
              .filter(g -> g.getGroceryID() == finalUserInt)
              .findFirst()
              .orElse(null);

          fetchRemove(grocery);
          retry = false;
        } catch (NumberFormatException e) {
          throw new NumberFormatException(
              "Cannot remove from non-digit groceryID. Please enter a valid GroceryID");
        } catch (Exception e) {
          throw new Exception("Unexpected error: " + e.getMessage());
        }
      }
    } else {
      str.append(" - Ugyldig kommando. Skriv enten \"-e\" eller \"-remove\".");
    }
    return true;
  }

  /**
   * <strong>Description:</strong><br>
   * A method fetching a amount and unit name to remove from a Grocery
   * based on a userinput.<br>
   *
   * @param g An object of type {@link Grocery} to remove from.
   */
  private void fetchRemove(Grocery g) {
    boolean retry = true;
    final GroceryManager gm = new GroceryManager(g);
    //Går i loop så lenge brukerinput gir feilmelding
    while (retry) {
      try {
        System.out.print("          Skriv en mengde som skal fjernes fra varen (f.eks 2 gram "
            + "/ desiliter / stykker): ");
        String staticInput = getInputStatic();

        gm.removeAmountGrocery(GroceryManager.getAmountAndUnit(staticInput));
        retry = false;
      } catch (Exception e) {
        System.out.println(e.getMessage());
      }
    }
  }

  /**
   * <strong>Description:</strong><br>
   * A method for displaying the Groceries in the Fridge sorted by date.
   */
  private void displayFridgeList() {
    str = new StringBuilder();
    Display display = new Display(fm);
    str.append(AbstractTable.createMenuTable("KJØLESKAP",
        "Her er en overikt over ulike varer i kjøleskapet sortert etter dato:"));

    //Visning når kjøleskapet er tomt
    if (fridge.getGroceryList().isEmpty()) {
      str.append("            ---- Kjøleskapet er tomt ----\n\n");
    } else {
      //Viser utgåtte varer i en liste
      str.append(
          display.dateList("Utgåtte varer", fm.getExpiredList(), "Ingen varer er gått ut på dato"));
      //Viser nesten utgåtte varer - i og med fokus på bærekraft
      str.append(display.dateList("Varer som holder på å gå ut på dato", fm.getNearExpList(),
          "Ingen varer er nær ved å gå ut på dato"));
      //Viser alle resterende varer med lenger dato
      str.append(
              display.dateList("Resterende varer",
                  fm.getRestGroceryList(),
                  "Ingen resterende varer"))
          .append("\n\n");
    }

    //viser totalt pengetap på datovarer beregnet i kr per måleenhet
    if (!fm.getExpiredList().isEmpty()) {
      str.append(
          display.displayPriceUnique(
              fm.getExpiredList(),
              "Total pengetap på datovarer",
              "Vare",
              "Pris beregnet på mengde"
          )
      );
    }
  }

  /**
   * <strong>Description:</strong><br>
   * A method displaying a given list of Groceries with expiration dates
   * before the given date.<br>
   *
   * @param date A LocalDate determaining the list of Groceries.
   * @param list A List of Groceries with expiration dates before
   *             the given date.
   */
  private void displayFridgeList(LocalDate date, List<Grocery> list) {
    str = new StringBuilder();
    Display display = new Display(fm);
    str.append(AbstractTable.createMenuTable("KJØLESKAP",
        "Her er en overikt over ulike varer i kjøleskapet sortert etter dato:"));

    //visning hvis listen er tom
    if (list.isEmpty()) {
      str.append("            ---- Kjøleskapet inneholder ingen varer med best-før dato før")
          .append(getDateToStr(date))
          .append(" ----\n\n");
    } else {
      //visning av listen
      str.append(display.dateList("Varer med best-før dato før " + getDateToStr(date),
          list,
          "Ingen varer har best-før dato før " + getDateToStr(date)));
    }

    //totalt pengetap på utgåtte varer
    if (!fm.getExpiredList().isEmpty()) {
      str.append(
          display.displayPriceUnique(
              fm.getExpiredList(),
              "Total pengetap på datovarer",
              "Vare",
              "Pris beregnet på mengde"
          )
      );
    }
  }

  /**
   * <strong>Description:</strong><br>
   * A method formating a given date to a parsed string.
   *
   * @param date A date of type LocalDate.
   * @return A parsed date of type String in the format "dd LLLL yyyy".
   */
  private String getDateToStr(LocalDate date) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");
    return formatter.format(date);
  }

  /**
   * <strong>Description:</strong><br>
   * A method both displaying a the content of a Fridge and
   * handeling commands from user input.<br>
   */
  public void displayFridge() {
    displayFridgeList();
    commands();
  }

  /**
   * <strong>Description:</strong><br>
   * A method displaying the total money spent on Groceries in the Fridge.
   */
  public void showValue() {
    str = new StringBuilder();

    str.append(AbstractTable.createMenuTable("SAMLET PRIS PÅ VARER",
        "Under er en oversikt over total prissum på varer:"));

    final Display display = new Display(fm);
    str.append(
        display.displayPrice(
            fridge.getGroceryList(),
            "Prisoversikt",
            "Vare",
            "Pris på mengde",
            80
        )
    );
    System.out.println(str);

    String userInput = "";
    while (!userInput.equals("-e")) {
      System.out.println("Skriv \"-e\" for å gå tilbake til menyen");
      userInput = getInput();
    }
  }

  /**
   * <strong>Description:</strong><br>
   * A method searching through a Fridge for a Grocery. The search is
   * determained by the Grocery's name.
   */
  public void showSearchFridge() {
    clearScreen();
    if (fridge.getGroceryList().isEmpty()) {
      System.out.println(
          "            ---- Ingen varer i kjøleskapet, kan ikke søke etter varer ----");
    } else {
      str = new StringBuilder();
      str.append(
          AbstractTable.createMenuTable(
              "VARESØK",
              "Søk på navnet til en vare i kjøleskapet. Skriv \"-e\" for å gå tilbake "
                  + "til hovedmenyen:"
          )
      );
      search();
    }
  }

  /**
   * <strong>Description:</strong><br>
   * A method fetching a name of a supposed Grocery in the Fridge,
   * which then will be searched for.
   */
  private void search() {
    String userInput;

    boolean retry = true;
    while (retry) {
      clearScreen();
      System.out.println(str);

      userInput = getInput();

      String finalUserInput = userInput;
      int countMax = (int) fridge.getGroceryList().stream()
          .map(Grocery::getName)
          .filter(name -> name.equalsIgnoreCase(finalUserInput))
          .count();

      if (userInput.equals("-e")) {
        retry = false;
      } else {
        try {
          searchHandler(countMax, finalUserInput);
        } catch (Exception e) {
          System.out.println(e.getMessage());
        }
      }
    }
  }

  /**
   * <strong>Description:</strong><br>
   * A method for handeling different outcomes from the search.<br>
   *
   * @param count An integer representing the count of a searched grocery.
   * @param input A String representing the searched name of the Grocery.
   */
  private void searchHandler(final int count, final String input) throws Exception {
    clearScreen();
    if (count == 0) {
      throw new IllegalArgumentException("Varen \""
          + input
          + "\" finnes ikke i kjøleskapet. Pass på å skrive varen navnet på riktig.\n");
    } else if (count == 1) {
      Grocery searchedGrocery = fridge.getGroceryList().stream()
          .filter(g -> g.getName().equalsIgnoreCase(input))
          .findFirst()
          .orElse(null);

      changeGrocery(searchedGrocery);
    } else {
      List<Grocery> searchedGroceryList = fridge.getGroceryList().stream()
          .filter(g -> g.getName().equalsIgnoreCase(input))
          .toList();

      System.out.println("        Velg en av varene i listen ved å skrive en vareID:");
      System.out.println(Display.displaySearchList(searchedGroceryList));

      final String userInput = getInput();
      Grocery chosenGrocery = fridge.getGroceryList().stream()
          .filter(g -> g.getGroceryID() == Integer.parseInt(userInput))
          .findFirst()
          .orElse(null);

      if (chosenGrocery != null) {
        changeGrocery(chosenGrocery);
      } else {
        throw new Exception("Fant ingen varer kalt " + input + " i Kjøleskapet.");
      }
    }
  }

  /**
   * <strong>Description:</strong><br>
   * A method displaying a sub-menu for the options to change a grocery.
   *
   * @param g A chosen Grocery the user can change.
   */
  private void displayChangeList(Grocery g) {
    str = new StringBuilder();
    Display display = new Display(fm);
    str.append(AbstractTable.createMenuTable("ENDRE VARE", "Velg en handling i listen under:"));
    str.append(display.grocery(g, 80));

    str.append("           [1] Legg til en mengde til varen.\n");
    str.append("           [2] Trekk fra en mengde fra varen.\n");
    str.append("           [3] Sjekk om varen har gått ut på dato.\n\n");
  }

  /**
   * <strong>Description:</strong><br>
   * A method for display a changing-menu as well as chandeling commands from the user.<br>
   *
   * @param grocery An object of type {@link Grocery} chosen to change.
   */
  private void changeGrocery(Grocery grocery) {
    clearScreen();
    displayChangeList(grocery);
    boolean retry = true;
    while (retry) {
      try {
        System.out.println(str);
        System.out.println("""
            Skriv "-e" for å gå tilbake til menyen, eller "tall" for å endre på en vare.
            "tall" skal skrives som et heltall i intervallet [1,3].
            
            """);

        retry = changeInputHandler(grocery);
      } catch (IllegalArgumentException e) {
        System.out.println(e.getMessage());
      }
    }
  }

  /**
   * <strong>Description:</strong><br>
   * A method to handle a user input according to legal commands.<br>
   *
   * @param grocery An object of type {@link Grocery} the user has chosen to change.
   * @return A boolean determaining the continuation of the
   method {@link #changeGrocery(Grocery)}.
   * @throws IllegalArgumentException for an illegal command.
   */
  private boolean changeInputHandler(Grocery grocery) throws IllegalArgumentException {
    String userInput = getInput();
    if (userInput.equals("-e")) {
      return false;
    } else if (Integer.parseInt(userInput) >= 1 || Integer.parseInt(userInput) <= 3) {
      changeOptions(grocery, fridge, userInput, this);
      displayChangeList(grocery);
    } else {
      throw new IllegalArgumentException(" - Illegal command: \""
          + userInput
          + "\". Please type a legal command below.\n");
    }
    return true;
  }

  /**
   * <strong>Description</strong><br>
   * A method for creating option dialogues for the "expired-check" in {@link #changeOptions}.<br>
   *
   * @param g   An object of type {@link Grocery} being the grocery being checked.
   * @param f   An object of type {@link Fridge} being the fridge
   *            the Grocery is stored in.
   * @param str An object of type {@link String}.
   */
  public void getExpiredOption(Grocery g, Fridge f, String str) {
    System.out.println(str);
    System.out.println("Varen har gått ut på dato.");
    char yesNoErr;
    do {
      yesNoErr = option("Ønsker du å slette varen?");
    } while (yesNoErr == 'e');

    if (yesNoErr == 'y') {
      f.removeGrocery(g);
    }
  }

  /**
   * <strong>Description:</strong><br>
   * A override of the
   * {@link AbstractOption#changeOptions(Grocery, Fridge, String, UserInterface) changeOption()}
   * from the abstract class {@link AbstractOption}.<br>
   *
   * @param g   An object of type {@link Grocery}.
   * @param f   An object of type {@link Fridge}.
   * @param str An object of type {@link String}.
   * @param ui  An object of the type {@link UserInterface}.
   */
  @Override
  protected void changeOptions(Grocery g, Fridge f, String str, UserInterface ui) {
    super.changeOptions(g, f, str, this);
  }

  /**
   * <strong>Description:</strong><br>
   * A method checking if the user wants to actually quit,
   * then closeing the Scanners and ending the main loop in
   * {@link #start()}.
   */
  public void finish() {
    char yesNoErr = 'e';
    while (yesNoErr == 'e') {
      yesNoErr = option("Vil du virkelig avslutte programmet?");
    }

    if (yesNoErr == 'y') {
      super.close();
      running = false;
    }
  }

  /**
   * <strong>Descriptions:</strong><br>
   * A method displaying available commands and handling an user input
   * according to the available commands.
   */
  private void commands() {
    boolean escape = false;
    str.append("Command log:\n");
    while (!escape) {
      clearScreen();
      System.out.println(str);
      //sjekker hvilke komandoer vi har tilgang til
      if (fridge.getGroceryList().isEmpty()) {
        System.out.println("Skriv \"-e\" for å gå tilbake til menyen");
      } else {
        System.out.println("""
            Skriv "-delete" for å slette en vare. Kommandoen "-delete all" vil slette \
            alle datovarer.
            Skriv "-change" eller "-change [vareID]" for å endre på en vare. "[vareID]" skrives \
            som et tall.
            Skriv "-date [dato]" for å hente alle varer med Best-Før dato før angitte dato.
                Datoen [dato] er ment å skrives på formatet "DD-YY-YYYY".
            Skriv "-e" for å gå tilbake til menyen.""");
      }

      String userInput = getInput();

      //behandler brukerinput
      try {
        escape = userInputHandler(userInput);
      } catch (Exception e) {
        str.append(e.getMessage());
      }
    }
  }

  /**
   * <strong>Description:</strong><br>
   * A method purely for handling the a user input according
   * to available commands.<br>
   *
   * @param userInput A String representing the users
   * @return A boolean determaining the continuation of the {@link #commands()}-method.
   * @throws Exception Exceptions for failed commands or failed
   *                   execution of methods.
   */
  private boolean userInputHandler(String userInput) throws Exception {
    if (userInput.equals("-e")) {
      return true;
    } else if (userInput.equals("-delete") && !fm.getExpiredList().isEmpty()) {
      try {
        deleteItems();
      } catch (Exception e) {
        throw new Exception(e.getMessage());
      }
      displayFridgeList();
    } else if (userInput.equals("-delete all") && !fm.getExpiredList().isEmpty()) {
      for (Grocery expiredItem : fm.getExpiredList()) {
        fridge.removeGrocery(expiredItem);
      }
      System.out.println("Alle datovarer er nå fjernet!\n");
      displayFridgeList();
    } else if (userInput.contains("-change")) {
      changeHandler(userInput);
      return true;
    } else if (userInput.contains("-date")) {
      try {
        LocalDate selectedDate;
        if (userInput.equals("-date")) {
          selectedDate = fetchDate();
        } else {
          String userDate = String.join("-", userInput.split("\\D")).substring(6);
          selectedDate = dateFetcher(userDate);
        }

        List<Grocery> dateList = fm.getDatesBefore(selectedDate);
        displayFridgeList(selectedDate, dateList);
      } catch (Exception e) {
        throw new Exception(e.getMessage());
      }

    } else if (fridge.getGroceryList().isEmpty() && userInput.contains("-delete")) {
      throw new IllegalArgumentException(" - Det finnes ingen datovarer. "
          + "Kan ikke fjerne datovarer fra kjøleskapet.\n");
    } else {
      throw new IllegalArgumentException(" - Ugyldig kommando. \"" + userInput + "\"\n");
    }
    return false;
  }

  /**
   * <strong>Description:</strong><br>
   * A method for handeling a user-given command according to
   * available commands.<br>
   *
   * @param userInput A string representing given command from the user.
   */
  private void changeHandler(String userInput) throws Exception {
    final int userInt;
    if (userInput.equals("-change")) {
      System.out.println("Skriv en vareID du ønsker å endre på:");
      userInput = getInput();
      userInt = Integer.parseInt(userInput);
    } else {
      userInt = Integer.parseInt(String.join("", userInput.split("\\D")));
    }
    final int userIndex = fm.getGroceryListIndex(userInt);

    try {
      if (userIndex >= 0 && userIndex < fridge.getGroceryList().size()) {
        changeGrocery(fridge.getGrocery(userIndex));
        str.append(" - Endret vare ").append(userIndex + 1).append("\n");
      } else {
        throw new IllegalArgumentException(" - Ugyldig vareID. Skriv en annen vareID");
      }
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  /**
   * <strong>Description:</strong><br>
   * A method for displaying a menu for removing a grocery,
   * as well as deleting multiple groceries based on userinput.
   */
  @SuppressWarnings("checkstyle:AbbreviationAsWordInName")
  private void deleteItems() throws Exception {
    try {
      clearScreen();
      System.out.println(
          AbstractTable.createMenuTable(
          "FJERN VARE",
          """
              Skriv inn en vareID fra listen ovenfor for å fjerne en vare fra kjøleskapet.
              
              Skriv flere vareID-er separert av ","(comma) for å fjerne flere varer\
               fra kjøleskapet."""
          )
      );
      Display display = new Display(fm);
      System.out.println(display.dateList("Liste over varer i kjøleskapet", fridge.getGroceryList(),
          "Ingen tilgjengelige varer."));

      String userInput = getInput();
      String[] deleteStrArr = userInput.replaceAll("\\s+", "").split(",");
      int[] deleteArr = Arrays.stream(deleteStrArr).mapToInt(Integer::parseInt).toArray();

      for (int groceryID : deleteArr) {
        Grocery removableGrocery = fridge.getGroceryList().stream()
            .filter(grocery -> grocery.getGroceryID() == groceryID)
            .findFirst()
            .orElse(null);
        if (removableGrocery != null) {
          fridge.removeGrocery(removableGrocery);
        } else {
          System.out.println(
              "Kunne ikke fjerne vare med vareID " + groceryID + " fra kjøleskapet.");
        }
      }

      str.append("Alle gyldige valgte datovarer er nå fjernet!\n");
    } catch (Exception e) {
      throw new Exception("Unexpected error: " + e.getMessage());
    }
  }

  /**
   * <strong>Discription:</strong><br>
   * A class displaying a Recipes in a CoocBook.
   */
  public void displayCookBook() {
    str = new StringBuilder();
    Display display = new Display(cbm);
    str.append(AbstractTable.createMenuTable("KOKEBOK",
        "Her er en overikt over tilgjengelige oppskrifter i kokeboken:"));

    //Visning når kjøleskapet er tomt
    if (cookBook.getRecipeList().isEmpty()) {
      str.append("            ---- Kokeboken er tomt ----\n\n");
    } else {
      //Viser utgåtte varer i en liste
      str.append(
          display.recipeMenuList("Anbefalte oppskrifter:", cbm.getRecommendedRecipes(),
              "Ingen anbeflate ingredienser"));
      //oppskrifter med varer som ikke er tilgjengelig i kjøleskapet
      str.append(display.recipeMenuList("Oppskrifter med ingredienser du ikke har:",
          cbm.getRest(),
          "Ingen resterende oppskrifter"));

      recipeCommands(str.toString());
    }
  }

  private void recipeCommands(String menu) {
    boolean retry = true;
    while (retry) {
      clearScreen();
      System.out.println(menu);
      if (!cookBook.getRecipeList().isEmpty()) {
        System.out.println("""
            Skriv "-add" for å legge til en oppskrift.
            Skriv "-remove [oppskriftID]" eller "-remove" for å fjerne en oppskrift. Kommandoen \
            "-remove all" vil slette alle oppskrifter.
            Skriv "-make [oppskriftID]" eller "-make" for å bruke ingredienser fra kjøleskapet til \
            å lage en oppskrift.
            Skriv "-e" for å gå tilbake til menyen.""");
      } else {
        System.out.println("""
            Skriv "-add" for å legge til en oppskrift.
            Skriv "-e" for å gå tilbake til menyen.""");
      }
      try {
        String userInput = getInput();
        retry = recipeInputHandler(userInput);
      } catch (Exception e) {
        System.out.println(e.getMessage());
      }
    }
  }

  private boolean recipeInputHandler(final String userInput) throws Exception {
    //try-catch for å gjøre kode mer robust
    try {
      if (userInput.equals("-e")) {
        return false;
      } else if (userInput.equals("-add")) {
        //legg til oppskrift i kokebok
        addRecipe();
      } else if (userInput.contains("-remove") && !cookBook.getRecipeList().isEmpty()) {

        //fjern oppskrift
        Display display = new Display(cbm);

        System.out.println(display.recipeMenuList("Tilgjengelige oppskrifter:",
            cbm.getAvailableRecipes(), "Ingen tilgjengelige oppskrifter"));
        final int userId;

        if (userInput.equals("-remove")) {
          System.out.print("Skriv en gyldig oppskriftsID fra listene ovenfor: ");
          userId = Integer.parseInt(getInput());
        } else {
          userId = Integer.parseInt(String.join("", userInput.split("\\D")));
        }

        Recipe removableRescipe = cbm.getRecipe(userId);
        cookBook.removeRecipe(removableRescipe);

      } else if (userInput.contains("-make") && !cookBook.getRecipeList().isEmpty()) {
        System.out.println(userInput.contains("-make"));
        Display display = new Display(cbm);
        System.out.println(display.recipeMenuList("Tilgjengelige oppskrifter",
            cbm.getAvailableRecipes(), "Ingen tilgjengelige oppskrifter"));
        System.out.println(display.recipeMenuList("Anbefalte Oppskrifter",
            cbm.getAvailableRecipes(), "Ingen anbefalte oppskrifter"));

        final int userId;
        if (userInput.equals("-make")) {
          System.out.print("Skriv en gyldig oppskriftsID: ");
          userId = Integer.parseInt(getInput());
        } else {
          userId = Integer.parseInt(String.join("", userInput.split("\\D")));
        }
        cbm.makeRecipe(cbm.getRecipe(userId));
      } else {
        throw new IllegalArgumentException("Illegal command! Please enter a legal command.");
      }

    } catch (NumberFormatException e) {
      throw new NumberFormatException("Could not format " + e.getMessage() + " to a number.");
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException(e.getMessage());
    } catch (Exception e) {
      throw new Exception("Unexpected error: " + e.getMessage());
    }
    return true;
  }

  private void addRecipe() {
    try {
      System.out.print("Skriv et navn på oppskriften: ");
      final String name = getInput();
      System.out.print("Skriv en kort beskrivelse av oppskriften: ");
      final String desc = getInput();
      
      String[] dir = new String[]{};
      boolean retry = true;
      while (retry) {
        try {
          dir = fetchDirections();
          retry = false;
        } catch (Exception e) {
          System.out.println(e.getMessage());
        }
      }
      int portion = 0;
      retry = true;
      while (retry) {
        try {
          portion = fetchPortion();
          retry = false;
        } catch (Exception e) {
          System.out.println(e.getMessage());
        }
      }
      List<Grocery> recipes = fetchRecipes();

      //legg til oppskrift
      cookBook.addRecipe(new Recipe(name, desc, dir, portion, recipes, fridge));
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  private List<Grocery> fetchRecipes() {
    boolean retry = true;
    ArrayList<Grocery> recipes = new ArrayList<>();
    while (retry) {
      try {
        System.out.print("Hvor mange ingredisenser trenger oppskriften? ");
        recipes = new ArrayList<>(getRecipes());
        retry = false;
      } catch (NumberFormatException e) {
        System.out.println(e.getMessage());
      }
    }
    return recipes.stream().toList();
  }

  private String[] fetchDirections() {
    boolean retry = true;
    int instructions;
    String[] dir = new String[]{};

    while (retry) {
      try {
        System.out.print("Hvor mange instruksjoner skal oppskriften ha? ");
        instructions = Integer.parseInt(getInput());
        dir = new String[instructions];
        for (int i = 0; i < instructions; i++) {
          System.out.printf("Skriv introduksjon nr. [%d]: ", i + 1);
          dir[i] = getInput();
        }
        retry = false;
      } catch (NumberFormatException | NegativeArraySizeException e) {
        System.out.println("Illegal input for number of instructions: " + e.getMessage());
      }
    }
    return dir;
  }

  private int fetchPortion() {
    try {
      System.out.print("Hvor mange porsjoner er oppskriften ment for? ");
      return Integer.parseInt(getInput());
    } catch (NumberFormatException e) {
      throw new NumberFormatException("Illegal input for portions. "
          + "Please enter a number for portions");
    }
  }

  private List<Grocery> getRecipes() {
    int repetitions;
    try {
      repetitions = Integer.parseInt(getInput());
    } catch (NumberFormatException e) {
      throw new NumberFormatException("Illegal input for amount of ingredients.");
    }

    int i = 0;
    final ArrayList<Grocery> recipes = new ArrayList<>();
    while (i < repetitions) {
      try {
        recipes.add(getRecipeGrocery());
        i++;
      } catch (IllegalArgumentException e) {
        System.out.println(e.getMessage());
      } catch (Exception e) {
        System.out.println("Unexpected error" + e.getMessage());
      }
    }
    return recipes.stream().toList();
  }

  private Grocery getRecipeGrocery() throws Exception {
    try {
      System.out.print("Skriv navnet på ingrediensen: ");
      String name = getInput();
      System.out.print("Skriv mengden på varen (f.eks 2 gram / desiliter / stykker): ");
      String[] amountAndUnit = GroceryManager.getAmountAndUnit(getInput());
      double quantity = Double.parseDouble(amountAndUnit[0]);
      SI unit = SI_Manager.getUnit(amountAndUnit[1]);

      return new Grocery(name, unit, quantity, null, 1, null);
    } catch (NumberFormatException e) {
      throw new NumberFormatException(e.getMessage());
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }
}
