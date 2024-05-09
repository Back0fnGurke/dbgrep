package de.hdm_stuttgart.mi.dad.outgoing;

import de.hdm_stuttgart.mi.dad.core.entity.Table;

public class OutputHandler {

    public void printTable(Table table){
        int numberOfColumns = table.rows().getFirst().columns().size();
        for (int i = 0; i < numberOfColumns; i++) {
            System.out.print("-----------------------");
        }
        System.out.println();
        for (int column = 0; column < table.rows().getFirst().columns().size(); column++) {
            System.out.printf("%-20s | ", table.rows().getFirst().columns().get(column).name());
        }
        System.out.println();
        for (int i = 0; i < numberOfColumns; i++) {
            System.out.print("-----------------------");
        }
        System.out.println();
        for (int i = 0; i < numberOfColumns; i++) {
            System.out.print("-----------------------");
        }
        System.out.println();
        for (int row = 0; row < table.rows().size(); row++) {
            for (int column = 0; column < table.rows().get(row).columns().size(); column++) {
                System.out.printf("%-20s | ",table.rows().get(row).columns().get(column).value());
            }
            System.out.println();
            for (int i = 0; i < numberOfColumns; i++) {
                System.out.print("-----------------------");
            }
            System.out.println();
        }
    }
}
