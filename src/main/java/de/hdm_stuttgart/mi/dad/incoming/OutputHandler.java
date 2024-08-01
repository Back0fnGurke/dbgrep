package de.hdm_stuttgart.mi.dad.incoming;

import de.hdm_stuttgart.mi.dad.core.entity.ColumnValue;
import de.hdm_stuttgart.mi.dad.core.entity.ColumnValueOutput;
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

    Table table;
    List<Property<?>> properties;
    int[] longest;
    boolean programEnd = false;

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
        this.table = table;
        this.properties = properties;
        this.programEnd = false;

        log.debug("Output Table: {}, Properties: {}", table, properties);

        int numberOfColumns = table.rows().getFirst().columns().size();

        //find largest String for each column
        longest = new int[numberOfColumns];

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

        //create divider String depending on longest entries
        String divider = "";
        for (int i = 0; i < numberOfColumns; i++) {
            divider += "-".repeat(longest[i] + numberOfColumns);
        }
        System.out.print(divider);

        //Print first line with column names
        System.out.println();
        for (int column = 0; column < table.rows().getFirst().columns().size(); column++) {
            System.out.printf("%-"+longest[column]+"s | ", table.rows().getFirst().columns().get(column).name());
        }
        System.out.println();
        System.out.println(divider);

        printRange(0, 9);
        int index = 10;
        if(table.rows().size()> 10) {
            System.out.println("Type m for more results. Type q to quit program.");

            Scanner in = new Scanner(System.in);
            String s;

            while (!programEnd) {
                s = in.nextLine();
                if(s.equals("m")){
                    printRange(index, index+9);
                    index += 10;
                    if(index >= table.rows().size()-1){
                        programEnd = true;
                    }else{
                        System.out.println("Type m for more results. Type q to quit program.");
                    }
                }
                if(s.equals("q")){
                    programEnd = true;
                }
            }
        }

    }

    /**
     * prints only a given range of rows from the Output Table
     *
     * @param start
     * @param end
     */
    private void printRange(final int start,final int end){
        int numberOfColumns = table.rows().getFirst().columns().size();

        String divider = "";
        for (int i = 0; i < numberOfColumns; i++) {
            divider += "-".repeat(longest[i] + numberOfColumns);
        }
        System.out.println(divider);

        ColumnValue columnValue;
        for (int row = start; row < table.rows().size() && row <= end; row++) {
            for (int column = 0; column < table.rows().get(row).columns().size(); column++) {
                columnValue = table.rows().get(row).columns().get(column);
                if(new ColumnValueOutput(columnValue, properties).isMatch()){
                    System.out.print("\u001B[31m" + columnValue.value()+"\u001b[0m");
                    System.out.printf("%-" + (longest[column] - columnValue.value().length() )  + "s | ", "");
                }else {
                    System.out.printf("%-" + longest[column] + "s | ", columnValue.value());
                }
            }
            System.out.println();
            System.out.println(divider);
        }
    }

}
