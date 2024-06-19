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
                        new ColumnValue("Name", "Barry"),
                        new ColumnValue("Age", "25")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("ID", "3"),
                        new ColumnValue("Name", "Sarah-Jane Lillian Long Long Long "),
                        new ColumnValue("Age", "22")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("ID", "4"),
                        new ColumnValue("Name", "Matt "),
                        new ColumnValue("Age", "22")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("ID", "5"),
                        new ColumnValue("Name", "Xanxia "),
                        new ColumnValue("Age", "22")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("ID", "6"),
                        new ColumnValue("Name", "Kate "),
                        new ColumnValue("Age", "22")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("ID", "7"),
                        new ColumnValue("Name", "Flora "),
                        new ColumnValue("Age", "22")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("ID", "8"),
                        new ColumnValue("Name", "Bloom "),
                        new ColumnValue("Age", "22")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("ID", "9"),
                        new ColumnValue("Name", "Aisha "),
                        new ColumnValue("Age", "22")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("ID", "10"),
                        new ColumnValue("Name", "Musa "),
                        new ColumnValue("Age", "22")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("ID", "11"),
                        new ColumnValue("Name", "Stella "),
                        new ColumnValue("Age", "22")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("ID", "12"),
                        new ColumnValue("Name", "Tecna "),
                        new ColumnValue("Age", "22")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("ID", "13"),
                        new ColumnValue("Name", "Susan "),
                        new ColumnValue("Age", "22")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("ID", "14"),
                        new ColumnValue("Name", "Suthek "),
                        new ColumnValue("Age", "10000")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("ID", "15"),
                        new ColumnValue("Name", "Anubis "),
                        new ColumnValue("Age", "100")
                ))

        ));

        List<Property> properties = List.of(
                PropertyFactory.getProperty(LIKE, Pattern.compile("Harry"))
        );

        outputHandler.printTable(table, properties);

    }
}
