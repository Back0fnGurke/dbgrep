package de.hdm_stuttgart.mi.dad.incoming.output;

import de.hdm_stuttgart.mi.dad.core.entity.ColumnValue;
import de.hdm_stuttgart.mi.dad.core.entity.Row;
import de.hdm_stuttgart.mi.dad.core.entity.Table;
import de.hdm_stuttgart.mi.dad.core.property.Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;

/**
 * Handles displaying the Output Table
 * For this create one OutputHandler and use the printTable method to print an Output Table
 */

public class OutputHandler {

    private static final Logger log = LoggerFactory.getLogger(OutputHandler.class);

    private static final int PAGE_SIZE = 5;
    private static final String RED = "\u001B[31m";
    private static final String END = "\u001b[0m";

    private OutputHandler() {
    }

    /**
     * This method prints a Table into the console.
     * Values that are a match to the given properties will be displayed in red
     * <p>
     * This method will start an interaction in the console if there are more than 10 rows in the table.
     *
     * @param table      the output table
     * @param properties a list with values of type Property
     */
    public static void printTable(final Table table, final List<Property<?>> properties, final int countOfRemainingTables) {
        log.debug("Output Table: {}, Properties: {}", table, properties);

        final List<Row> rows = table.rows();
        final int[] longest = findLongestStringsOfColumns(rows);

        final String divider = createDivider(longest);

        final StringBuilder output = new StringBuilder();

        output.append(System.lineSeparator())
                .append("Table name: ").append(table.name().toUpperCase())
                .append(System.lineSeparator())
                .append(divider)
                .append(System.lineSeparator())
                .append(buildHeader(rows.getFirst().columns(), longest))
                .append(divider)
                .append(System.lineSeparator())
                .append(buildOutputTableRange(rows, properties, longest, divider, 0, PAGE_SIZE - 1))
                .append(System.lineSeparator());

        System.out.print(output);
        handleUserInput(rows, properties, longest, divider, countOfRemainingTables);
    }

    /**
     * Creates a divider string based on the longest string lengths of each column.
     *
     * @param longest an array of the longest string lengths for each column
     * @return a divider string
     */
    private static String createDivider(final int[] longest) {
        final StringBuilder divider = new StringBuilder();
        divider.repeat("-", IntStream.of(longest).sum() + (3 * longest.length) - 1);
        return divider.toString();
    }

    /**
     * Finds the longest string in each column of a table.
     *
     * @param rows the rows of a table to analyze
     * @return an array of the longest string lengths for each column
     */
    private static int[] findLongestStringsOfColumns(final List<Row> rows) {
        final int[] longest = new int[rows.getFirst().columns().size()];

        for (final Row row : rows) {
            for (int column = 0; column < longest.length; column++) {
                final String columnValue = row.columns().get(column).value();
                final int length = (columnValue == null) ? 4 : columnValue.length(); // 4 = length of string null
                if (length > longest[column]) {
                    longest[column] = length;
                }
            }
        }
        for (int column = 0; column < longest.length; column++) {
            int length = rows.getFirst().columns().get(column).name().length();
            if (length > longest[column]) {
                longest[column] = length;
            }
        }
        return longest;
    }

    /**
     * Prints the header of a table.
     *
     * @param headerColumns a List of columns of the table to print the header for
     * @param longest       an array of the longest string lengths for each column
     */
    private static String buildHeader(final List<ColumnValue> headerColumns, int[] longest) {
        final StringBuilder header = new StringBuilder();
        for (int column = 0; column < headerColumns.size(); column++) {
            header.append(String.format(String.format("%%-%ds| ", longest[column] + 1), headerColumns.get(column).name()));
        }
        header.append(System.lineSeparator());
        return header.toString();
    }

    /**
     * Builds a string containing a range of rows from a table.
     *
     * @param rows       rows of the table to print
     * @param properties the properties to highlight in the table
     * @param longest    an array of the longest string lengths for each column
     * @param divider    the divider string
     * @param start      the starting row index
     * @param end        the ending row index
     */
    private static String buildOutputTableRange(final List<Row> rows, final List<Property<?>> properties, final int[] longest, final String divider, final int start, final int end) {
        final StringBuilder output = new StringBuilder();

        for (int row = start; row < rows.size() && row <= end; row++) {
            output.append(divider)
                    .append(System.lineSeparator());

            final List<ColumnValue> columns = rows.get(row).columns();
            for (int column = 0; column < columns.size(); column++) {

                String columnValue = columns.get(column).value();
                if (columnValue == null) columnValue = "null";

                if (ValueMatcher.isMatch(columnValue, properties)) {
                    final String value = RED + columnValue + END;
                    output.append(String.format(buildColumnFormatSpecifier(longest[column] + RED.length() + END.length() + 1), value));
                } else {
                    output.append(String.format(buildColumnFormatSpecifier(longest[column] + 1), columnValue));
                }
            }
            output.append(System.lineSeparator());
        }
        output.append(divider);
        return output.toString();
    }

    /**
     * Builds a format specifier string for a column with the given width.
     *
     * @param width the width of the column
     * @return a format specifier string for a column
     */
    private static String buildColumnFormatSpecifier(final int width) {
        return String.format("%%-%ds| ", width);
    }

    /**
     * Handles user input for displaying more rows of a table or quitting the program.
     *
     * @param rows       rows of the table to print
     * @param properties the properties to highlight in the table
     * @param longest    an array of the longest string lengths for each column
     * @param divider    the divider string
     */
    private static void handleUserInput(final List<Row> rows, final List<Property<?>> properties, final int[] longest, final String divider, final int countOfRemainingTables) {
        int index = PAGE_SIZE;
        final int tableSize = rows.size();
        if (tableSize > PAGE_SIZE) {
            printUserInputInfo(tableSize, index, countOfRemainingTables);

            final Scanner in = new Scanner(System.in);
            String input;

            boolean tableIsRunning = true;
            while (tableIsRunning) {

                input = in.nextLine();
                switch (input) {
                    case "m" -> {
                        System.out.println(buildOutputTableRange(rows, properties, longest, divider, index, index + PAGE_SIZE - 1));
                        index += PAGE_SIZE;
                        if (index >= tableSize) {
                            tableIsRunning = false;
                        } else {
                            printUserInputInfo(tableSize, index, countOfRemainingTables);
                        }
                    }
                    case "c" -> tableIsRunning = false;
                    case "q" -> System.exit(0);
                    default -> {
                        System.out.println("Invalid input.");
                        printUserInputInfo(tableSize, index, countOfRemainingTables);
                    }
                }
            }
        }
    }

    /**
     * Prints a message about possible user input commands.
     *
     * @param tableSize              count of matching entries
     * @param index                  count of already printed entries
     * @param countOfRemainingTables count of remaining tables that have not been printed
     */
    private static void printUserInputInfo(int tableSize, int index, int countOfRemainingTables) {
        String message = "There are" + tableSize + "matching entries from this table.";
        message += "Of these, " + index + " have been printed. \n";
        message += "Type m for more entries. ";
        if (countOfRemainingTables > 1) {
            message += "Type c to close this table and print the next table. There are " + countOfRemainingTables + " remaining tables.";
        } else if (countOfRemainingTables == 1) {
            message += "Type c to close this table and print the next table. There is " + countOfRemainingTables + " remaining table.";
        }
        message += "Type q to quit.";
        System.out.println(message);
    }
}
