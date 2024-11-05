package edu.ntnu.idi.idatt;

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

    private static String createWhitespace(int length, int wordLength) {
        int wsLength = length/2 - wordLength/2;
        return " ".repeat(Math.max(0, wsLength));
    }

    public static String createMenuTable(String title, String subTitle) {
        String longBar = "------------------------------------------------------------------------------------------------";

        return longBar + "\n\n" +
                createWhitespace(longBar.length(), title.length()) +
                title + "\n\n" +
                longBar + "\n" +
                createWhitespace("              ".length(), title.length()) +
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
        sb.append(bottomBar);

        return sb.toString();
    }

    private StringBuilder getTableData(String bottomBar) {
        StringBuilder tableData = new StringBuilder();
        for (int i = 0; i < colTitleArr.length; i++) {
            int leftPadding = bottomBar.length()/4 - colTitleArr[i].length()/2 - 1;
            int rightPadding = bottomBar.length()/4 - colData[i].length()/2 - 1;

            String leftStr = String.format("|%" + (leftPadding + colTitleArr[i].length()) + "s", colTitleArr[i]);
            leftStr += String.format("%" + (leftPadding) + "s", "");
            String rightStr = String.format("|%" + (rightPadding + colData[i].length()) + "s", colData[i]);
            rightStr += String.format("%"+ (rightPadding) +"s|", "");

            tableData.append(leftStr);
            tableData.append(rightStr).append("\n");
        }
        return tableData;
    }
}
