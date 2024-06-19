package de.hdm_stuttgart.mi.dad.outgoing;

import de.hdm_stuttgart.mi.dad.core.entity.ColumnValue;
import de.hdm_stuttgart.mi.dad.core.entity.ColumnValueOutput;
import de.hdm_stuttgart.mi.dad.core.entity.Table;
import de.hdm_stuttgart.mi.dad.core.property.Property;

import javax.swing.*;
import java.awt.*;
import java.io.PrintStream;
import java.util.List;

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.util.Objects;
import java.util.Scanner;

import static java.awt.event.KeyEvent.VK_ENTER;
import static java.awt.event.KeyEvent.VK_ESCAPE;

public class OutputHandler {

    Table table;
    List<Property> properties;
    int[] longest;
    boolean programEnd = false;

    public void printTable(Table table, List<Property> properties){
        this.table = table;
        this.properties = properties;

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
        System.out.println(divider);

        printRange(0, 9);
        int index = 10;
        if(table.rows().size()> 10) {
            System.out.println("Type m for more results. Type q to quit program.");

            Scanner in = new Scanner(System.in);;
            String s;

            while (!programEnd) {
                s = in.nextLine();
                if(s.equals("m")){
                    printRange(index, index+9);
                    index += 9;
                    if(index > table.rows().size()){
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

    private void printRange(int start, int end){
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
