package de.hdm_stuttgart.mi.dad;

import de.hdm_stuttgart.mi.dad.core.entity.ColumnValue;
import de.hdm_stuttgart.mi.dad.core.entity.Row;
import de.hdm_stuttgart.mi.dad.core.entity.Table;
import de.hdm_stuttgart.mi.dad.core.property.Property;
import de.hdm_stuttgart.mi.dad.core.property.PropertyFactory;
import de.hdm_stuttgart.mi.dad.outgoing.OutputHandler;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import static de.hdm_stuttgart.mi.dad.core.property.PropertyType.*;

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
                        new ColumnValue("Name", "Sarah-Jane Lillian Long Long Long "),
                        new ColumnValue("Age", "22")
                ))
        ));

        List<Property> properties = List.of(
                PropertyFactory.getProperty(LIKE, Pattern.compile("Harry"))
        );

        outputHandler.printTable(table, properties);

    }
}
