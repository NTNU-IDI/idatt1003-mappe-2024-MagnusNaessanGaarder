package edu.ntnu.idi.idatt;

import java.util.List;
import java.util.Objects;

public class Table {
    private String title;
    private String[] colTitleArr;
    private String[] colData;

    public Table(String title, String[] colTitles, String[] colData) {
        if (colTitles.length != colData.length) {
            throw new IllegalArgumentException("The number of columns and titles do not match");
        }

        this.title = title;
        this.colTitleArr = colTitles;
        this.colData = colData;
    }

    private static String createWhitespace(int length, int wordLength) {
        int wsLength = length/2 - wordLength;
        return " ".repeat(Math.max(0, wsLength));
    }

    public static String createMenuTable(String title, String subTitle) {
        String longBar = "————————————————————————————————————————————————————————————————————————————————————————————————";

        return longBar + "\n\n" +
                createWhitespace(longBar.length(), title.length()) +
                title + "\n\n" +
                longBar + "\n" +
                createWhitespace("              ".length(), title.length()) +
                subTitle + "\n\n";
    }

    public String createTable() {
        StringBuilder sb = new StringBuilder();
        String longBar = "————————————————————————";
        String bottomBar = longBar + longBar;
        for (int i = 0; i < title.length(); i++) {
            bottomBar += "—";
        }

        sb.append(longBar);
        sb.append(title);
        sb.append(longBar);
        sb.append("\n");

        final StringBuilder tableData = getTableData(bottomBar);
        sb.append(tableData);
        sb.append(bottomBar);

        return sb.toString();
    }

    private StringBuilder getTableData(String bottomBar) {
        StringBuilder tableData = new StringBuilder();
        for (int i = 0; i < colTitleArr.length; i++) {
            int leftPadding = (bottomBar.length()/2 - (colTitleArr[i].length())) / 2;
            int rightPadding = (bottomBar.length()/2 - (colData[i].length())) / 2;

            String leftStr = String.format("|%" + (leftPadding + colTitleArr[i].length()) + "s", colTitleArr[i]);
            leftStr += String.format("%" + (leftPadding) + "s", "");
            String rightStr = String.format("|%" + (rightPadding + colData[i].length()) + "s", colData[i]);
            rightStr += String.format("%"+ (rightPadding) +"s|", "");

            tableData.append(leftStr);
            tableData.append(rightStr + "\n");
        }
        return tableData;
    }
}
