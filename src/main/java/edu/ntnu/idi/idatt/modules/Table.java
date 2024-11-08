package edu.ntnu.idi.idatt.modules;

public class Table {
    final private String title;
    final private String[] colTitleArr;
    final private String[] colData;

    public Table(String title, String[] colTitles, String[] colData) {
        if (colTitles.length != colData.length) {
            throw new IllegalArgumentException("The number of columns and titles do not match");
        }

        this.title = title;
        this.colTitleArr = colTitles;
        this.colData = colData;
    }

    private static String createWhitespaceTitle(int length, int wordLength) {
        int wsLength = length/2 - wordLength/2;
        return " ".repeat(Math.max(0, wsLength));
    }

    public static String createMenuTable(String title, String subTitle) {
        String longBar = "------------------------------------------------------------------------------------------------";

        return longBar + "\n\n" +
                createWhitespaceTitle(longBar.length(), title.length()) +
                title + "\n\n" +
                longBar + "\n" +
                createWhitespaceTitle("              ".length(), title.length()) +
                subTitle + "\n\n";
    }

    public String createTable() {
        StringBuilder sb = new StringBuilder();
        String longBar = "------------------------";
        StringBuilder bottomBar = new StringBuilder(longBar + longBar);
        bottomBar.append("-".repeat(title.length()));

        sb.append(longBar);
        sb.append(title);
        sb.append(longBar);
        sb.append("\n");

        final StringBuilder tableData = getTableData(bottomBar.toString());
        sb.append(tableData);
        sb.append(bottomBar).append("\n\n");

        return sb.toString();
    }

    private String createWhitespace(String s, int  width) {
        return String.format("%-" + (width-1)  + "s", String.format("%" + (s.length() + (width - s.length()) / 2) + "s", s));
    }

    private StringBuilder getTableData(String bottomBar) {
        StringBuilder tableData = new StringBuilder();
        for (int i = 0; i < colTitleArr.length; i++) {
            int width = bottomBar.length()/2;

            String leftStr = createWhitespace(colTitleArr[i], width);
            String rightStr = createWhitespace(colData[i], width);

            tableData.append("|").append(leftStr).append("|");
            tableData.append(rightStr).append("|\n");
        }
        return tableData;
    }
}
