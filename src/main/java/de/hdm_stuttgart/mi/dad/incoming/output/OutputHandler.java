package de.hdm_stuttgart.mi.dad.incoming.output;

import de.hdm_stuttgart.mi.dad.core.entity.ColumnValue;
import de.hdm_stuttgart.mi.dad.core.entity.Table;
import de.hdm_stuttgart.mi.dad.core.property.Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Scanner;

/**
 * Handles displaying the Output Table
 * For this create one OutputHandler and use the printTable method to print an Output Table
 */

public class OutputHandler {

    private static final Logger log = LoggerFactory.getLogger(OutputHandler.class);

    /**
     * This method prints a Table into the console.
     * Values that are a match to the given properties will be displayed in red
     * <p>
     * This method will start an interaction in the console if there are more than 10 rows in the table.
     *
     * @param table      the output table
     * @param properties a list with values of type Property
     */
    public void printTable(final Table table, final List<Property<?>> properties){

        log.debug("Output Table: {}, Properties: {}", table, properties);

        int numberOfColumns = table.rows().getFirst().columns().size();
        final int[] longest = findLongestStringsOfColumns(table, numberOfColumns);

        final String divider = createDivider(longest, numberOfColumns);
        System.out.print(divider);

        printHeader(table, longest, numberOfColumns);
        System.out.println(divider);

        printRange(table, properties, longest, 0, 9);
        handleUserInput(table, properties, longest);

    }

    /**
     * Creates a divider string based on the longest string lengths of each column.
     *
     * @param longest         an array of the longest string lengths for each column
     * @param numberOfColumns the number of columns in the table
     * @return a divider string
     */
    private String createDivider(final int[] longest, final int numberOfColumns){
        String divider = "";
        for (int i = 0; i < numberOfColumns; i++) {
            divider += "-".repeat(longest[i] + numberOfColumns);
        }
        return divider;
    }

    /**
     * Finds the longest string in each column of a table.
     *
     * @param table           the table to analyze
     * @param numberOfColumns the number of columns in the table
     * @return an array of the longest string lengths for each column
     */
    private int[] findLongestStringsOfColumns(final Table table, int numberOfColumns){
        final int[] longest = new int[numberOfColumns];

        for (int row = 0; row < table.rows().size(); row++) {
            for (int column = 0; column < numberOfColumns; column++) {
                int length = table.rows().get(row).columns().get(column).value().length();
                if(length > longest[column]){
                    longest[column] = length;
                }
            }
        }
        for (int column = 0; column < numberOfColumns; column++) {
            int length = table.rows().getFirst().columns().get(column).name().length();
            if(length > longest[column]){
                longest[column] = length;
            }
        }
        return longest;
    }

    /**
     * Prints the header of a table.
     *
     * @param table           the table to print the header for
     * @param longest         an array of the longest string lengths for each column
     * @param numberOfColumns the number of columns in the table
     */
    private void printHeader(final Table table, int[] longest, int numberOfColumns){
        System.out.println();
        for (int column = 0; column < table.rows().getFirst().columns().size(); column++) {
            System.out.printf("%-"+ (longest[column] + 1) +"s| ", table.rows().getFirst().columns().get(column).name());
        }
        System.out.println();
    }

    /**
     * Prints a range of rows from a table.
     *
     * @param table      the table to print
     * @param properties the properties to highlight in the table
     * @param longest    an array of the longest string lengths for each column
     * @param start      the starting row index
     * @param end        the ending row index
     */
    private void printRange(final Table table, final List<Property<?>> properties, int[] longest, final int start,final int end){
        int numberOfColumns = table.rows().getFirst().columns().size();

        final String divider = createDivider(longest, numberOfColumns);
        System.out.println(divider);

        ColumnValue columnValue;
        for (int row = start; row < table.rows().size() && row <= end; row++) {
            for (int column = 0; column < table.rows().get(row).columns().size(); column++) {
                columnValue = table.rows().get(row).columns().get(column);
                if(new ColumnValueOutput(columnValue, properties).isMatch()){
                    System.out.print("\u001B[31m" + columnValue.value()+"\u001b[0m");
                    System.out.printf("%-" + (longest[column] - columnValue.value().length() + 1 )  + "s| ", "");
                }else {
                    System.out.printf("%-" + (longest[column] + 1) + "s| ", columnValue.value());
                }
            }
            System.out.println();
            System.out.println(divider);
        }
    }

    /**
     * Handles user input for displaying more rows of a table or quitting the program.
     *
     * @param table           the table to print
     * @param properties      the properties to highlight in the table
     * @param longest         an array of the longest string lengths for each column
     */
    private void handleUserInput(final Table table, final List<Property<?>> properties, final int[] longest){
        int index = 10;
        final int tableSize = table.rows().size();
        if(tableSize > 10) {
            System.out.println("Type m for more results. Type q to quit program.");

            final Scanner in = new Scanner(System.in);
            String input;

            boolean running = true;
            while (running) {

                input = in.nextLine();
                switch (input) {
                    case "m" -> {
                        printRange(table, properties, longest, index, index + 9);
                        index += 10;
                        if(index >= tableSize){
                            running = false;
                        }else{
                            System.out.println("Type m for more results. Type q to quit program.");
                        }
                    }
                    case "q" -> running = false;
                    default -> System.out.println("Invalid input. Type m for more results. Type q to quit program.");
                }
            }
        }
    }

}
