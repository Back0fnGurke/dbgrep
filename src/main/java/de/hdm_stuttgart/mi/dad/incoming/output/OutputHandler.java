package de.hdm_stuttgart.mi.dad.incoming.output;

import de.hdm_stuttgart.mi.dad.core.entity.ColumnValue;
import de.hdm_stuttgart.mi.dad.core.entity.Row;
import de.hdm_stuttgart.mi.dad.core.entity.Table;
import de.hdm_stuttgart.mi.dad.core.property.Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;

/**
 * Handles displaying the Output Table
 * For this create one OutputHandler and use the printTable method to print an Output Table
 */

public class OutputHandler {

    private static final Logger log = LoggerFactory.getLogger(OutputHandler.class);

    private static final int PAGE_SIZE = 10;
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String BOLD = "\u001B[1m";
    private static final String RESET = "\u001B[0m";

    public static void handleOutput(final List<Table> resultTables, final List<Property<?>> properties) {
        if (isNoResultsFound(resultTables)) {
            System.out.println(BOLD + RED + "No results found." + RESET);
            return;
        }

        final Scanner scanner = new Scanner(System.in);
        final List<Page> pages = createPages(resultTables, properties);
        int currentPage = 1;
        int totalPages = pages.size();

        displayPage(pages.getFirst(), currentPage, totalPages);

        if (totalPages > 1) {
            handlePagination(scanner, pages, currentPage, totalPages);
        }

        scanner.close();
    }

    private static boolean isNoResultsFound(final List<Table> resultTables) {
        return resultTables.isEmpty() || resultTables.stream().allMatch(table -> table.rows().isEmpty());
    }

    private static void displayPage(Page page, int currentPage, int totalPages) {
        System.out.println("\n" + page.content.trim());
        System.out.printf(BOLD + YELLOW + "Page %d of %d " + RESET + "\n", currentPage, totalPages);
        System.out.printf("%n" + GREEN + "Enter " + BOLD + BLUE + "N" + RESET + GREEN + " for next page, " + BLUE + "P" + RESET + GREEN + " for previous page, " + BLUE + "Q" + RESET + GREEN + " to quit:" + RESET);
    }

    private static void handlePagination(final Scanner scanner, final List<Page> pages, int currentPage, int totalPages) {
        boolean isRunning = true;
        while (isRunning) {
            final String input = scanner.nextLine().toUpperCase();
            switch (input) {
                case "N":
                    if (currentPage < totalPages) {
                        currentPage++;
                        displayPage(pages.get(currentPage - 1), currentPage, totalPages);
                    } else {
                        System.out.println(RED + BOLD + "You are already on the last page." + RESET);
                    }
                    break;
                case "P":
                    if (currentPage > 1) {
                        currentPage--;
                        displayPage(pages.get(currentPage - 1), currentPage, totalPages);
                    } else {
                        System.out.println(RED + BOLD + "You are already on the first page." + RESET);
                    }
                    break;
                case "Q":
                    isRunning = false;
                    System.out.println(BOLD + GREEN + "Exiting pagination. Goodbye!" + RESET);
                    break;
                default:
                    System.out.println(RED + BOLD + "Invalid input." + RESET + " Please enter " + BLUE + BOLD + "N" + RESET + " for next page, " + BLUE + BOLD + "P" + RESET + " for previous page, " + BLUE + BOLD + "Q" + RESET + " to quit.");
            }
        }
    }

    private static List<Page> createPages(final List<Table> resultTables, final List<Property<?>> properties) {
        final List<Page> pages = new ArrayList<>();
        int pageCounter = 1;

        for (Table table : resultTables) {
            if (!table.rows().isEmpty()) {
                int totalPagesForTable = (int) Math.ceil((double) table.rows().size() / PAGE_SIZE);
                for (int pageNum = 0; pageNum < totalPagesForTable; pageNum++) {
                    final List<Row> currentPageRows = table.rows().subList(pageNum * PAGE_SIZE, Math.min((pageNum + 1) * PAGE_SIZE, table.rows().size()));
                    final String pageContent = buildPageContent(table, currentPageRows, properties);
                    pages.add(new Page(pageCounter++, pageContent));
                }
            }
        }

        return pages;
    }

    private static String buildPageContent(final Table table, final List<Row> currentPageRows, final List<Property<?>> properties) {
        final int[] longestColumnLengths = findLongestStringsOfColumns(table.rows());
        final String divider = createDivider(longestColumnLengths);

        StringBuilder content = new StringBuilder();
        content.append("Table name: ").append(BOLD).append(table.name().toUpperCase()).append(RESET)
                .append(System.lineSeparator())
                .append(divider)
                .append(System.lineSeparator())
                .append(buildHeader(table.rows().getFirst().columns(), longestColumnLengths))
                .append(divider)
                .append(System.lineSeparator())
                .append(buildOutputTableRange(currentPageRows, properties, longestColumnLengths, divider, currentPageRows.size() - 1))
                .append(System.lineSeparator());

        return content.toString();
    }

    private static String createDivider(final int[] longest) {
        return "-".repeat(IntStream.of(longest).sum() + (3 * longest.length) - 1);
    }

    private static int[] findLongestStringsOfColumns(final List<Row> rows) {
        final int[] longest = new int[rows.getFirst().columns().size()];

        for (final Row row : rows) {
            for (int col = 0; col < longest.length; col++) {
                final String columnValue = row.columns().get(col).value();
                final int length = (columnValue == null) ? 4 : columnValue.length();
                if (length > longest[col]) {
                    longest[col] = length;
                }
            }
        }

        for (int col = 0; col < longest.length; col++) {
            int headerLength = rows.getFirst().columns().get(col).name().length();
            if (headerLength > longest[col]) {
                longest[col] = headerLength;
            }
        }

        return longest;
    }

    private static String buildHeader(final List<ColumnValue> headerColumns, int[] longest) {
        StringBuilder header = new StringBuilder();
        for (int col = 0; col < headerColumns.size(); col++) {
            header.append(String.format("%%-%ds| ", longest[col] + 1).formatted(headerColumns.get(col).name()));
        }
        header.append(System.lineSeparator());
        return header.toString();
    }

    private static String buildOutputTableRange(final List<Row> rows, final List<Property<?>> properties, final int[] longest, final String divider, final int end) {
        StringBuilder output = new StringBuilder();
        for (int row = 0; row < rows.size() && row <= end; row++) {
            output.append(divider).append(System.lineSeparator());
            output.append(buildRowContent(rows.get(row), properties, longest));
        }
        output.append(divider);
        return output.toString();
    }

    private static String buildRowContent(final Row row, final List<Property<?>> properties, final int[] longest) {
        StringBuilder rowContent = new StringBuilder();
        List<ColumnValue> columns = row.columns();
        for (int col = 0; col < columns.size(); col++) {
            String columnValue = String.valueOf(columns.get(col).value());
            if (ValueMatcher.isMatch(columnValue, properties)) {
                columnValue = RED + columnValue + RESET;
            }
            rowContent.append(String.format("%%-%ds| ", longest[col] + 1).formatted(columnValue));
        }
        return rowContent.append(System.lineSeparator()).toString();
    }

    private record Page(int page, String content) {
    }
}
