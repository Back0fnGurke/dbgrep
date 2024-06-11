package de.hdm_stuttgart.mi.dad.outgoing;

import de.hdm_stuttgart.mi.dad.core.entity.ColumnValue;
import de.hdm_stuttgart.mi.dad.core.entity.ColumnValueOutput;
import de.hdm_stuttgart.mi.dad.core.entity.Table;
import de.hdm_stuttgart.mi.dad.core.property.Property;

import java.awt.*;
import java.io.PrintStream;
import java.util.List;

public class OutputHandler {

    public void printTable(Table table, List<Property> properties){
        int numberOfColumns = table.rows().getFirst().columns().size();

        //find largest String for each column
        int[] longest = new int[numberOfColumns];

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

        //print
        String divider = "";
        for (int i = 0; i < numberOfColumns; i++) {
            divider += "-".repeat(longest[i] + numberOfColumns);
        }
        System.out.print(divider);

        System.out.println();
        for (int column = 0; column < table.rows().getFirst().columns().size(); column++) {
            System.out.printf("%-"+longest[column]+"s | ", table.rows().getFirst().columns().get(column).name());
        }
        System.out.println();
        System.out.print(divider);
        System.out.println();
        System.out.print(divider);
        System.out.println();

        ColumnValue columnValue;
        for (int row = 0; row < table.rows().size(); row++) {
            for (int column = 0; column < table.rows().get(row).columns().size(); column++) {
                columnValue = table.rows().get(row).columns().get(column);
                if(new ColumnValueOutput(columnValue, properties).isMatch()){
                    System.out.print("\u001B[31m" + columnValue.value()+"\u001b[0m");
                    System.out.printf("%-" + (longest[column] - columnValue.value().length()) + "s | ", "");
                }else {
                    System.out.printf("%-" + longest[column] + "s | ", columnValue.value());
                }
            }
            System.out.println();
            System.out.print(divider);
            System.out.println();
        }
    }
}
