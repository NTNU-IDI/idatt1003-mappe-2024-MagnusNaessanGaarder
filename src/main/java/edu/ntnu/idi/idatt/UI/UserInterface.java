package edu.ntnu.idi.idatt.UI;


import edu.ntnu.idi.idatt.Modules.*;
import edu.ntnu.idi.idatt.Utils.SI;

import java.time.LocalDate;

public class UserInterface {
    public UserInterface () {

    }

    /**
     * A method used to test four different instances of a grocery.
     */
    public void start() {
        final Fridge fridge = new Fridge();

        final SI unit1 = new SI("Gram","g","kg","Kilo");
        final SI unit2 = new SI("Stykker","stk","stk","");
        final SI unit3= new SI("Desiliter","dL","L","Desi");
        final SI unit4 = new SI("Liter","L","L","");


        final Grocery grocery1 = new Grocery("Mel",unit1,1000, LocalDate.now().minusDays(2),200,fridge);
        final Grocery grocery2 = new Grocery("Bananer",unit2,18,LocalDate.now(),49.90,fridge);
        final Grocery grocery3 = new Grocery("Melk",unit3,20,LocalDate.now().plusDays(4),50,fridge);
        final Grocery grocery4 = new Grocery("Kraft",unit4,0.5,LocalDate.now().plusDays(1),259.99,fridge);
    }

    /**
     * A method used to start the actual application.
     */
    public void init() {

    }
}
