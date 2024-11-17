package edu.ntnu.idi.idatt.Utils;
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
        return longBar + "\n"
               + createWhitespaceTitle(longBar.length(), title.length())
               + title + "\n"
               + longBar + "\n"
               + createWhitespaceTitle(12 * 2, 0)
               + subTitle + "\n\n";
    }

    public String createTable(String title, String[] colTitleArr, String[] colData) {
        StringBuilder sb = new StringBuilder();
        String longBar = "------------------------";
        StringBuilder bottomBar = new StringBuilder(longBar + longBar);
        bottomBar.append("-".repeat(title.length()));

        sb.append("\n").append(longBar);
        sb.append(title);
        sb.append(longBar);
        sb.append("\n");

        final StringBuilder tableData = getTableData(colTitleArr, colData, bottomBar.toString());
        sb.append(tableData);
        sb.append(bottomBar).append("\n\n");

        return sb.toString();
    }

    public static String createTableStatic(String title, String[] colTitleArr, String[] colData) {
        StringBuilder sb = new StringBuilder();
        String longBar = "------------------------";
        StringBuilder bottomBar = new StringBuilder(longBar + longBar);
        bottomBar.append("-".repeat(title.length()));

        sb.append("\n").append(longBar);
        sb.append(title);
        sb.append(longBar);
        sb.append("\n");

        final StringBuilder tableData = getTableDataStatic(colTitleArr, colData, bottomBar.toString());
        sb.append(tableData);
        sb.append(bottomBar).append("\n\n");

        return sb.toString();
    }

    private String createWhitespace(String s, int  width) {
        return String.format("%-" + (width - 1) + "s",
                String.format("%" + (s.length() + (width - s.length()) / 2) + "s", s));
    }

    private static String createWhitespaceStatic(String s, int  width) {
        return String.format("%-" + (width - 1) + "s",
                String.format("%" + (s.length() + (width - s.length()) / 2) + "s", s));
    }

    private StringBuilder getTableData(String[] colTitleArr, String[] colData, String bottomBar) {
        StringBuilder tableData = new StringBuilder();
        for (int i = 0; i < colTitleArr.length; i++) {
            int width = bottomBar.length() / 2;

            String leftStr = createWhitespace(colTitleArr[i], width);
            String rightStr = createWhitespace(colData[i], width);

            tableData.append("|").append(leftStr).append("|");
            tableData.append(rightStr).append("|\n");
        }
        return tableData;
    }

    private static StringBuilder getTableDataStatic(String[] colTitleArr, String[] colData, String bottomBar) {
        StringBuilder tableData = new StringBuilder();
        for (int i = 0; i < colTitleArr.length; i++) {
            int width = bottomBar.length() / 2;

            String leftStr = createWhitespaceStatic(colTitleArr[i], width);
            String rightStr = createWhitespaceStatic(colData[i], width);

            tableData.append("|").append(leftStr).append("|");
            tableData.append(rightStr).append("|\n");
        }
        return tableData;
    }
}
