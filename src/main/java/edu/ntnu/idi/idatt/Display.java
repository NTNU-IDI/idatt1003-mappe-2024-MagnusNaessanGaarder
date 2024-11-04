package edu.ntnu.idi.idatt;

import java.util.List;

public class Display {
    private final String title;
    private final Fridge fridge;

    public Display(String title, String subTitle, Fridge fridge) {
        this.title = Table.createMenuTable(title, subTitle);
        this.fridge = fridge;
    }

    public String getTitle() {
        return this.title;
    }

    public String displayPrice (List<Grocery> list, String title,  String colomnTitle, String colomnData) {
        StringBuilder str = new StringBuilder();
        if(list.isEmpty()) {
            str.append("         ---- Ingen varer er lagt til i kjøleskap ----\n");
            str.append("         Legg til varer for å se total prissum\n\n");
        }
        else {
            str.append(buildTable(list, title, colomnTitle,colomnData));

            str.append("            Total brukt på varer: ").append(this.fridge.getTotalPrice()).append(" kr\n\n");
        }
        return str.toString();
    }

    private String buildTable (List<Grocery> list, String title,  String colomnTitle, String colomnData) {
        String[] colTitle = new String[list.size()+1];
        String[] colData = new String[list.size()+1];
        colTitle[0] = colomnTitle;
        colData[0] = colomnData;
        for(int i=0; i < list.size(); i++) {
            colTitle[i+1] = list.get(i).getName();
            colData[i+1] = list.get(i).getPricePerQuantity() + " kr";
        }
        Table table = new Table(title, colTitle, colData);
        return table.createTable();
    }

    public String displayPriceUnique (List<Grocery> list, String title,  String colomnTitle, String colomnData) {
        StringBuilder str = new StringBuilder();
        if(list.isEmpty()) {
            str.append("         ---- Ingen varer er lagt til i kjøleskap ----\n");
        }
        else {
            str.append(buildTable(list, title, colomnTitle,colomnData));

            str.append("            Total pengetap på datovarer: ").append(this.fridge.getTotalPrice()).append(" kr\n\n");
        }
        return str.toString();
    }

    public String list (List<Grocery> list){
        StringBuilder str = new StringBuilder();
        if (list.isEmpty()) {
            str.append("            ---- Ingen varer er lagt til i kjøleskapet ----");
        }
        else {
            str.append(displayList(list));
        }
        return str.toString();
    }

    public String grocery(Grocery g) {
        StringBuilder str = new StringBuilder();
        Table groceryTable = new Table(g.getName(),
                new String[]{"Mengde","Pris","Dato"},
                new String[]{g.getQuantity() + " " + g.getUnit().getAbrev(), g.getPriceToStr(),g.getDateToStr()}
        );
        str.append(groceryTable.createTable());

        return str.toString();
    }

    public static String displayList(List<Grocery> list) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            Table groceryTable = new Table(list.get(i).getName(),
                    new String[]{"VareID", "Mengde","Pris","Dato"},
                    new String[]{
                            Integer.toString(i+1),
                            list.get(i).getQuantity()+ " " + list.get(i).getUnit().getAbrev(),
                            list.get(i).getPriceToStr(),
                            list.get(i).getDateToStr()
                    }
            );
            sb.append(groceryTable.createTable());
        }
        return sb.toString();
    }
}
