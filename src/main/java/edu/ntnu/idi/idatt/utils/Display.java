package edu.ntnu.idi.idatt.utils;

import edu.ntnu.idi.idatt.manager.FridgeManager;
import edu.ntnu.idi.idatt.manager.GroceryManager;
import edu.ntnu.idi.idatt.modules.Grocery;

import java.util.List;

public class Display extends Table {
    private final FridgeManager fm;
    private static final String START = "\n            ---- ";
    private static final String END = " ----\n\n";

    public Display(FridgeManager fm) {
        this.fm = fm;
    }

    public String displayPrice (List<Grocery> list, String title, String colomnTitle, String colomnData) {
        StringBuilder str = new StringBuilder();
        if(list.isEmpty()) {
            str.append(START).append("Ingen varer er lagt til i kjøleskap").append(END);
            str.append("         Legg til varer for å se total prissum\n\n");
        }
        else {
            str.append(buildTable(list, title, colomnTitle, colomnData));

            str.append("            Total brukt på varer: ").append(fm.getTotalPrice()).append(" kr\n\n");
        }
        return str.toString();
    }

    private String buildTable(List<Grocery> list, String title,  String colomnTitle, String colomnData) {
        String[] colTitle = new String[list.size()+1];
        String[] colData = new String[list.size()+1];
        colTitle[0] = colomnTitle;
        colData[0] = colomnData;
        for(int i=0; i < list.size(); i++) {
            final GroceryManager gm = new GroceryManager(list.get(i));
            colTitle[i+1] = list.get(i).getName();
            colData[i+1] = gm.getPricePerQuantity() + " kr";
        }
        return createTable(title, colTitle, colData);
    }

    public String displayPriceUnique(List<Grocery> list, String title,  String colomnTitle, String colomnData) {
        StringBuilder str = new StringBuilder();
        if(list.isEmpty()) {
            str.append(START).append("Ingen varer er lagt til i kjøleskap").append(END);
        }
        else {
            str.append(buildTable(list, title, colomnTitle, colomnData));

            str.append("            Total pengetap på datovarer: ").append(fm.getMoneyLoss()).append(" kr\n\n");
        }
        return str.toString();
    }

    public String list(List<Grocery> list, String altText){
        StringBuilder str = new StringBuilder();
        if (list.isEmpty()) {
            str.append(START).append(altText).append(END);
        }
        else {
            str.append(displayList(list));
        }
        return str.toString();
    }

    public static String menuList(List<Grocery> list, String altText){
        StringBuilder str = new StringBuilder();
        if (list.isEmpty()) {
            str.append(START).append(altText).append(END);
        }
        else {
            str.append(displayMenuList(list));
        }
        return str.toString();
    }

    public String displayGrocery(Grocery g) {
        StringBuilder str = new StringBuilder();
        String table = createTable(g.getName(),
                new String[]{"Mengde", "Pris", "Dato"},
                new String[]{g.getQuantity() + " " + g.getUnit().getAbrev(), g.getPriceToStr(),g.getDateToStr()}
        );
        str.append(table);

        return str.toString();
    }

    public static String displayList(List<Grocery> list) {
        StringBuilder sb = new StringBuilder();
        for (Grocery grocery : list) {
            String table = createTableStatic(grocery.getName(),
                    new String[]{"VareID", "Mengde", "Pris", "Dato"},
                    new String[]{
                            grocery.getGroceryID() + "",
                            grocery.getQuantity() + " " + grocery.getUnit().getAbrev(),
                            grocery.getPriceToStr(),
                            grocery.getDateToStr()
                    }
            );
            sb.append(table);
        }
        return sb.toString();
    }
    public static String displayMenuList(List<Grocery> list) {
        StringBuilder sb = new StringBuilder();
        for (Grocery grocery : list) {
            sb.append("         ")
                    .append(grocery.getName())
                    .append(", ")
                    .append(grocery.getQuantity()).append(" ").append(grocery.getUnit().getAbrev()).append(", ")
                    .append(grocery.getPriceToStr()).append(", ")
                    .append(grocery.getDateToStr())
                    .append("\n");
        }
        sb.append("\n\n");
        return sb.toString();
    }

    public static String displaySearchList(List<Grocery> list) {
        StringBuilder sb = new StringBuilder();
        list.forEach(g -> sb.append("         ")
                    .append(g.getName())
                    .append(", ")
                    .append(g.getDateToStr()).append(", ")
                    .append("vareID: ").append(g.getGroceryID())
                    .append("\n")
        );

        sb.append("\n\n");
        return sb.toString();
    }

    public String dateList(String title, List<Grocery> list, String altText) {
        if (!list.isEmpty()) {
            return super.createDateTable(title, list);
        }
        else {
            return START + altText + END;
        }
    }
}
