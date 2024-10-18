package src.main.java.edu.ntnu.idi.idatt;

import java.util.HashMap;

public class SI {
    private HashMap<String, String> messureList;

    public SI() {
        messureList = new HashMap<String, String>();
        messureList.put("Stykker", "stk");
        messureList.put("Gram", "g");
        messureList.put("Kilo", "kg");
        messureList.put("Desiliter", "dl");
        messureList.put("Liter", "l");
    }

    public String getMessurement(final String str) {
        return messureList.get(str);
    }

    public void addSImessure(final String name, final String abbrev) {
        messureList.put(name, abbrev);
    }
}