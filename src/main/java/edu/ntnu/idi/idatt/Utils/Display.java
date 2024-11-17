package edu.ntnu.idi.idatt.Utils;

import edu.ntnu.idi.idatt.Manager.FridgeManager;
import edu.ntnu.idi.idatt.Manager.GroceryManager;
import edu.ntnu.idi.idatt.Modules.Grocery;

import java.util.List;

public class Display extends Table {
    private final FridgeManager fm;

    public Display(FridgeManager fm) {
        this.fm = fm;
    }

    public String displayPrice (List<Grocery> list, String title, String colomnTitle, String colomnData) {
        StringBuilder str = new StringBuilder();
        if(list.isEmpty()) {
            str.append("\n         ---- Ingen varer er lagt til i kjøleskap ----\n");
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
            str.append("         ---- Ingen varer er lagt til i kjøleskap ----\n");
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
            str.append("            ---- ").append(altText).append(" ----\n\n");
        }
        else {
            str.append(displayList(list));
        }
        return str.toString();
    }

    public static String menuList(List<Grocery> list, String altText){
        StringBuilder str = new StringBuilder();
        if (list.isEmpty()) {
            str.append("\n            ---- ").append(altText).append(" ----\n\n");
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
        for (Grocery grocery : list) {
            sb.append("         ")
                    .append(grocery.getName())
                    .append(", ")
                    .append(grocery.getDateToStr()).append(", ")
                    .append("vareID: ").append(grocery.getGroceryID())
                    .append("\n");
        }
        sb.append("\n\n");
        return sb.toString();
    }
}
