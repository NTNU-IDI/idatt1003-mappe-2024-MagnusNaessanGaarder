package edu.ntnu.idi.idatt.utils;

import edu.ntnu.idi.idatt.modules.Grocery;

import java.util.List;

/**
 *
 */
public abstract class Table {
    /**
     *
     */
    private static String createWhitespaceTitle(int length, int wordLength) {
        int wsLength = length / 2 - wordLength / 2;
        return " ".repeat(Math.max(0, wsLength));
    }

    public static String createMenuTable(String title, String subTitle) {
        String longBar = "\n\n------------------------------------------------------------------------------------------------";
        return longBar + "\n\n"
               + createWhitespaceTitle(longBar.length(), title.length())
               + title
               + longBar + "\n"
               + createWhitespaceTitle(12 * 2, 0)
               + subTitle + "\n\n";
    }

    public String createTable(String title, String[] colTitleArr, String[] colData) {
        StringBuilder sb = new StringBuilder();
        StringBuilder bottomBar = new StringBuilder();
        bottomBar.append("-".repeat(2 * 24 + title.length()));
        String longBar = "-".repeat((bottomBar.length() / 2) - (title.length() / 2));

        sb.append("\n").append(longBar);
        sb.append(title);
        while(sb.length() <= bottomBar.length()) {
            sb.append("-");
        }
        sb.append("\n");

        final StringBuilder tableData = getTableData(colTitleArr, colData, bottomBar.toString());
        sb.append(tableData);
        sb.append(bottomBar).append("\n\n");

        return sb.toString();
    }

    public String createDateTable(String title, List<Grocery> list) {
        StringBuilder sb = new StringBuilder();
        String longBar = "-".repeat(86/2 - title.length()/2);
        String bottomBar = "-".repeat(86);

        //Title of table
        sb.append("\n").append(longBar);
        sb.append(title);
        while (sb.length() <= bottomBar.length()) {
            sb.append("-");
        }
        sb.append("\n");

        //| ID | name | quantity unit | price / unit | Best-before |
        String ID_str = center("ID", (bottomBar.length()* 5 / 100));
        String name_str = center("Navn", (bottomBar.length() * 20 / 100));
        String quantity_str = center("Mengde", (bottomBar.length() * 20 / 100));
        String price_str = center("Pris", (bottomBar.length() * 25 / 100));
        String best_before_str = center("Best-før", (bottomBar.length() *  30 / 100 - 4));

        int[] lengths = new int[]{
                ID_str.length(),
                name_str.length(),
                quantity_str.length(),
                price_str.length(),
                best_before_str.length()
        };

        String str = "|%s|%s|%s|%s|%s|";
        sb.append(String.format(str,
                ID_str,
                name_str,
                quantity_str,
                price_str,
                best_before_str
        ));
        sb.append("\n").append(bottomBar).append("\n");

        final StringBuilder tableData = getTableData(lengths, list);
        sb.append(tableData);
        sb.append(bottomBar).append("\n\n");

        return sb.toString();
    }

    public static String createTableStatic(String title, String[] colTitleArr, String[] colData) {
        StringBuilder sb = new StringBuilder();
        StringBuilder bottomBar = new StringBuilder();
        bottomBar.append("-".repeat(2 * 24 + title.length()));
        String longBar = "-".repeat((bottomBar.length() / 2) - (title.length() / 2));

        sb.append("\n").append(longBar);
        sb.append(title);
        while(sb.length() <= bottomBar.length()) {
            sb.append("-");
        }
        sb.append("\n");

        final StringBuilder tableData = getTableDataStatic(colTitleArr, colData, bottomBar.toString());
        sb.append(tableData);
        sb.append(bottomBar).append("\n\n");

        return sb.toString();
    }

    private String center(String s, int size) {
        if (s == null || size <= s.length())
            return s;

        StringBuilder sb = new StringBuilder(size);
        sb.append(" ".repeat((size - s.length()) / 2));
        sb.append(s);
        while (sb.length() < size) {
            sb.append(" ");
        }
        return sb.toString();
    }

    private static String centerStatic(String s, int size) {
        if (s == null || size <= s.length())
            return s;

        StringBuilder sb = new StringBuilder(size);
        sb.append(" ".repeat((size - s.length()) / 2));
        sb.append(s);
        while (sb.length() < size) {
            sb.append(" ");
        }
        return sb.toString();
    }

    private StringBuilder getTableData(String[] colTitleArr, String[] colData, String bottomBar) {
        StringBuilder tableData = new StringBuilder();
        for (int i = 0; i < colTitleArr.length; i++) {
            int width = bottomBar.length() / 2;

            String leftStr = center(colTitleArr[i], width - 1);
            String rightStr = center(colData[i], width - 1);

            tableData.append("|").append(leftStr).append("|");
            tableData.append(rightStr).append("|\n");
        }
        return tableData;
    }

    private StringBuilder getTableData(int[] lengths, List<Grocery> list) {
        StringBuilder tableData = new StringBuilder();
        for (Grocery g : list) {
            String ID_str = center(g.getGroceryID() + "", lengths[0]);
            String name_str = center(g.getName(), lengths[1]);
            String quantity_str = center(g.getQuantity() + " " + g.getUnit().getAbrev(), lengths[2]);
            String price_str = center(g.getPriceToStr(), lengths[3]);
            String best_before_str = center(g.getDateToStr(), lengths[4]);

            String str = "|%s|%s|%s|%s|%s|";
            String rowData = String.format(
                    str,
                    ID_str,
                    name_str,
                    quantity_str,
                    price_str,
                    best_before_str
            );
            tableData.append(rowData).append("\n");
        }
        return tableData;
    }

    private static StringBuilder getTableDataStatic(String[] colTitleArr, String[] colData, String bottomBar) {
        StringBuilder tableData = new StringBuilder();
        for (int i = 0; i < colTitleArr.length; i++) {
            int width = bottomBar.length() / 2;

            String leftStr = centerStatic(colTitleArr[i], width - 1);
            String rightStr = centerStatic(colData[i], width - 1);

            tableData.append("|").append(leftStr).append("|");
            tableData.append(rightStr).append("|\n");
        }
        return tableData;
    }
}
