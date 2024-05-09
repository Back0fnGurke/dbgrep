package de.hdm_stuttgart.mi.dad;

import de.hdm_stuttgart.mi.dad.core.entity.ColumnValue;
import de.hdm_stuttgart.mi.dad.core.entity.Row;
import de.hdm_stuttgart.mi.dad.core.entity.Table;
import de.hdm_stuttgart.mi.dad.outgoing.OutputHandler;

import java.util.ArrayList;
import java.util.Arrays;

public class TestOutput {

    public static void main(final String[] args) {

        OutputHandler outputHandler = new OutputHandler();
        Table table = new Table("Test", Arrays.asList(
                new Row(Arrays.asList(
                        new ColumnValue("ID", "1"),
                        new ColumnValue("Name", "Harry"),
                        new ColumnValue("Age", "30")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("ID", "2"),
                        new ColumnValue("Name", "Sarah-Jane"),
                        new ColumnValue("Age", "22")
                ))
        ));

        outputHandler.printTable(table);

    }
}
