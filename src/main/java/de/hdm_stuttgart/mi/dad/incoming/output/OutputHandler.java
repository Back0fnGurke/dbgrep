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

public class OutputHandler {

    private static final Logger log = LoggerFactory.getLogger(OutputHandler.class);

    private static final int PAGE_SIZE = 10;
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String BOLD = "\u001B[1m";
    private static final String RESET = "\u001B[0m";

    private OutputHandler() {
    }

    public static void handleOutput(final List<Table> resultTables, final List<Property<?>> properties) {
        log.debug("Called with resultTables: {}, properties: {}", resultTables, properties);
        if (isNoResultsFound(resultTables)) {
            System.out.println(BOLD + RED + "No results found." + RESET);
            return;
        }

        final Scanner scanner = new Scanner(System.in);
        final List<Page> pages = createPages(resultTables, properties);
        int currentPage = 1;
        final int totalPages = pages.size();

        displayPage(pages.getFirst(), currentPage, totalPages);

        if (totalPages > 1) {
            handlePagination(scanner, pages, currentPage, totalPages);
        }

        scanner.close();
        log.debug("Completed");
    }

    private static boolean isNoResultsFound(final List<Table> resultTables) {
        log.debug("Called with resultTables: {}", resultTables);
        final boolean noResults = resultTables.isEmpty() || resultTables.stream().allMatch(table -> table.rows().isEmpty());
        log.debug("Returning: {}", noResults);
        return noResults;
    }

    private static void displayPage(final Page page, final int currentPage, final int totalPages) {
        log.debug("Called with page: {}, currentPage: {}, totalPages: {}", page, currentPage, totalPages);
        System.out.println(System.lineSeparator() + page.content.trim());
        System.out.printf(BOLD + YELLOW + "Page %d of %d " + RESET + System.lineSeparator(), currentPage, totalPages);
        System.out.printf(System.lineSeparator() + GREEN + "Enter " + BOLD + BLUE + "N" + RESET + GREEN + " for next page, " + BOLD + BLUE + "P" + RESET + GREEN + " for previous page, " + BOLD + BLUE + "Q" + RESET + GREEN + " to quit:" + RESET);
        log.debug("Completed");
    }

    private static void handlePagination(final Scanner scanner, final List<Page> pages, int currentPage, final int totalPages) {
        log.debug("Called with currentPage: {}, totalPages: {}", currentPage, totalPages);
        boolean isRunning = true;
        while (isRunning) {
            final String input = scanner.nextLine().toUpperCase();
            log.debug("User input: {}", input);
            switch (input) {
                case "N":
                    if (currentPage < totalPages) {
                        currentPage++;
                        displayPage(pages.get(currentPage - 1), currentPage, totalPages);
                    } else {
                        System.out.println(RED + BOLD + "You are on the last page." + RESET);
                    }
                    break;
                case "P":
                    if (currentPage > 1) {
                        currentPage--;
                        displayPage(pages.get(currentPage - 1), currentPage, totalPages);
                    } else {
                        System.out.println(RED + BOLD + "You are on the first page." + RESET);
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
        log.debug("Completed");
    }

    private static List<Page> createPages(final List<Table> resultTables, final List<Property<?>> properties) {
        log.debug("Called with resultTables: {}, properties: {}", resultTables, properties);
        final List<Page> pages = new ArrayList<>();
        int pageCounter = 1;

        for (final Table table : resultTables) {
            if (!table.rows().isEmpty()) {
                final int totalPagesForTable = (int) Math.ceil((double) table.rows().size() / PAGE_SIZE);
                for (int pageNum = 0; pageNum < totalPagesForTable; pageNum++) {
                    final List<Row> currentPageRows = table.rows().subList(pageNum * PAGE_SIZE, Math.min((pageNum + 1) * PAGE_SIZE, table.rows().size()));
                    final String pageContent = buildPageContent(table, currentPageRows, properties);
                    pages.add(new Page(pageCounter++, pageContent));
                }
            }
        }

        log.debug("Returning pages: {}", pages);
        return pages;
    }

    private static String buildPageContent(final Table table, final List<Row> currentPageRows, final List<Property<?>> properties) {
        log.debug("Called with table: {}, currentPageRows: {}, properties: {}", table, currentPageRows, properties);
        final int[] longestColumnLengths = findLongestStringsOfColumns(table.rows());
        final String divider = createDivider(longestColumnLengths);

        final StringBuilder content = new StringBuilder();
        content.append("Table name: ").append(BOLD).append(table.name().toUpperCase()).append(RESET)
                .append(System.lineSeparator())
                .append(divider)
                .append(System.lineSeparator())
                .append(buildHeader(table.rows().getFirst().columns(), longestColumnLengths))
                .append(divider)
                .append(System.lineSeparator())
                .append(buildOutputTableRange(currentPageRows, properties, longestColumnLengths, divider, currentPageRows.size() - 1))
                .append(System.lineSeparator());

        log.debug("Returning content: {}", content);
        return content.toString();
    }

    private static String createDivider(final int[] longest) {
        log.debug("Called with longest: {}", longest);
        final String divider = "-".repeat(IntStream.of(longest).sum() + (3 * longest.length) - 1);
        log.debug("Returning divider: {}", divider);
        return divider;
    }

    private static int[] findLongestStringsOfColumns(final List<Row> rows) {
        log.debug("Called with rows: {}", rows);
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
            final int headerLength = rows.getFirst().columns().get(col).name().length();
            if (headerLength > longest[col]) {
                longest[col] = headerLength;
            }
        }

        log.debug("Returning longest: {}", longest);
        return longest;
    }

    private static String buildHeader(final List<ColumnValue> headerColumns, final int[] longest) {
        log.debug("Called with headerColumns: {}, longest: {}", headerColumns, longest);
        final StringBuilder header = new StringBuilder();
        for (int col = 0; col < headerColumns.size(); col++) {
            header.append(String.format("%%-%ds| ", longest[col] + 1).formatted(headerColumns.get(col).name()));
        }
        header.append(System.lineSeparator());
        log.debug("Returning header: {}", header);
        return header.toString();
    }

    private static String buildOutputTableRange(final List<Row> rows, final List<Property<?>> properties, final int[] longest, final String divider, final int end) {
        log.debug("Called with rows: {}, properties: {}, longest: {}, divider: {}, end: {}", rows, properties, longest, divider, end);
        final StringBuilder output = new StringBuilder();
        for (int row = 0; row < rows.size() && row <= end; row++) {
            output.append(divider).append(System.lineSeparator());
            output.append(buildRowContent(rows.get(row), properties, longest));
        }
        output.append(divider);
        log.debug("Returning output: {}", output);
        return output.toString();
    }

    private static String buildRowContent(final Row row, final List<Property<?>> properties, final int[] longest) {
        log.debug("Called with row: {}, properties: {}, longest: {}", row, properties, longest);
        final StringBuilder rowContent = new StringBuilder();
        final List<ColumnValue> columns = row.columns();
        for (int col = 0; col < columns.size(); col++) {
            String columnValue = String.valueOf(columns.get(col).value());
            if (ValueMatcher.isMatch(columnValue, properties)) {
                columnValue = RED + columnValue + RESET;
            }
            rowContent.append(String.format("%%-%ds| ", longest[col] + 1).formatted(columnValue));
        }
        log.debug("Returning rowContent: {}", rowContent);
        return rowContent.append(System.lineSeparator()).toString();
    }

    private record Page(int page, String content) {
    }
}
