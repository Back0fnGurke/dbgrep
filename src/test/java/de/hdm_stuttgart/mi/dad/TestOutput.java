package de.hdm_stuttgart.mi.dad;

import de.hdm_stuttgart.mi.dad.core.entity.ColumnValue;
import de.hdm_stuttgart.mi.dad.core.entity.Row;
import de.hdm_stuttgart.mi.dad.core.entity.Table;
import de.hdm_stuttgart.mi.dad.core.property.Property;
import de.hdm_stuttgart.mi.dad.core.property.PropertyFactory;
import de.hdm_stuttgart.mi.dad.outgoing.OutputHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import static de.hdm_stuttgart.mi.dad.core.property.PropertyType.LIKE;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestOutput {

    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
        System.setIn(System.in);
    }

    void provideInput(String data) {
        ByteArrayInputStream testIn = new ByteArrayInputStream(data.getBytes());
        System.setIn(testIn);
    }

    @Test
    public void testOutput() {

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
                        new ColumnValue("Name", "Sarah-Jane Lillian Long Long Long"),
                        new ColumnValue("Age", "22")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("ID", "4"),
                        new ColumnValue("Name", "Matt"),
                        new ColumnValue("Age", "22")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("ID", "5"),
                        new ColumnValue("Name", "Xanxia"),
                        new ColumnValue("Age", "22")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("ID", "6"),
                        new ColumnValue("Name", "Kate "),
                        new ColumnValue("Age", "22")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("ID", "7"),
                        new ColumnValue("Name", "Flora"),
                        new ColumnValue("Age", "22")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("ID", "8"),
                        new ColumnValue("Name", "Bloom"),
                        new ColumnValue("Age", "22")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("ID", "9"),
                        new ColumnValue("Name", "Aisha"),
                        new ColumnValue("Age", "22")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("ID", "10"),
                        new ColumnValue("Name", "Musa"),
                        new ColumnValue("Age", "22")
                ))

        ));

        List<Property> properties = List.of(
                PropertyFactory.createProperty(LIKE, Pattern.compile("Harry"))
        );

        outputHandler.printTable(table, properties);

        String exspected =
                "-----------------------------------------------" + System.lineSeparator()
                        + "ID | Name                              | Age | " + System.lineSeparator()
                        + "-----------------------------------------------" + System.lineSeparator()
                        + "-----------------------------------------------" + System.lineSeparator()
                        + "1  | \u001B[31mHarry\u001b[0m                             | 30  | " + System.lineSeparator()
                        + "-----------------------------------------------" + System.lineSeparator()
                        + "2  | Barry                             | 25  | " + System.lineSeparator()
                        + "-----------------------------------------------" + System.lineSeparator()
                        + "3  | Sarah-Jane Lillian Long Long Long | 22  | " + System.lineSeparator()
                        + "-----------------------------------------------" + System.lineSeparator()
                        + "4  | Matt                              | 22  | " + System.lineSeparator()
                        + "-----------------------------------------------" + System.lineSeparator()
                        + "5  | Xanxia                            | 22  | " + System.lineSeparator()
                        + "-----------------------------------------------" + System.lineSeparator()
                        + "6  | Kate                              | 22  | " + System.lineSeparator()
                        + "-----------------------------------------------" + System.lineSeparator()
                        + "7  | Flora                             | 22  | " + System.lineSeparator()
                        + "-----------------------------------------------" + System.lineSeparator()
                        + "8  | Bloom                             | 22  | " + System.lineSeparator()
                        + "-----------------------------------------------" + System.lineSeparator()
                        + "9  | Aisha                             | 22  | " + System.lineSeparator()
                        + "-----------------------------------------------" + System.lineSeparator()
                        + "10 | Musa                              | 22  | " + System.lineSeparator()
                        + "-----------------------------------------------";

        assertEquals(exspected, outputStreamCaptor.toString()
                .trim());
    }

    @Test
    public void testOutputwithInteraction() {

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
                        new ColumnValue("Name", "Sarah-Jane Lillian Long Long Long"),
                        new ColumnValue("Age", "22")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("ID", "4"),
                        new ColumnValue("Name", "Matt"),
                        new ColumnValue("Age", "22")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("ID", "5"),
                        new ColumnValue("Name", "Xanxia"),
                        new ColumnValue("Age", "22")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("ID", "6"),
                        new ColumnValue("Name", "Kate "),
                        new ColumnValue("Age", "22")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("ID", "7"),
                        new ColumnValue("Name", "Flora"),
                        new ColumnValue("Age", "22")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("ID", "8"),
                        new ColumnValue("Name", "Bloom"),
                        new ColumnValue("Age", "22")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("ID", "9"),
                        new ColumnValue("Name", "Aisha"),
                        new ColumnValue("Age", "22")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("ID", "10"),
                        new ColumnValue("Name", "Musa"),
                        new ColumnValue("Age", "22")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("ID", "11"),
                        new ColumnValue("Name", "Suthek"),
                        new ColumnValue("Age", "99")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("ID", "12"),
                        new ColumnValue("Name", "Anubis"),
                        new ColumnValue("Age", "98")
                ))

        ));

        List<Property> properties = List.of(
                PropertyFactory.createProperty(LIKE, Pattern.compile("Harry"))
        );

        provideInput("m");
        outputHandler.printTable(table, properties);

        String exspected =
                "-----------------------------------------------" + System.lineSeparator()
                        + "ID | Name                              | Age | " + System.lineSeparator()
                        + "-----------------------------------------------" + System.lineSeparator()
                        + "-----------------------------------------------" + System.lineSeparator()
                        + "1  | \u001B[31mHarry\u001b[0m                             | 30  | " + System.lineSeparator()
                        + "-----------------------------------------------" + System.lineSeparator()
                        + "2  | Barry                             | 25  | " + System.lineSeparator()
                        + "-----------------------------------------------" + System.lineSeparator()
                        + "3  | Sarah-Jane Lillian Long Long Long | 22  | " + System.lineSeparator()
                        + "-----------------------------------------------" + System.lineSeparator()
                        + "4  | Matt                              | 22  | " + System.lineSeparator()
                        + "-----------------------------------------------" + System.lineSeparator()
                        + "5  | Xanxia                            | 22  | " + System.lineSeparator()
                        + "-----------------------------------------------" + System.lineSeparator()
                        + "6  | Kate                              | 22  | " + System.lineSeparator()
                        + "-----------------------------------------------" + System.lineSeparator()
                        + "7  | Flora                             | 22  | " + System.lineSeparator()
                        + "-----------------------------------------------" + System.lineSeparator()
                        + "8  | Bloom                             | 22  | " + System.lineSeparator()
                        + "-----------------------------------------------" + System.lineSeparator()
                        + "9  | Aisha                             | 22  | " + System.lineSeparator()
                        + "-----------------------------------------------" + System.lineSeparator()
                        + "10 | Musa                              | 22  | " + System.lineSeparator()
                        + "-----------------------------------------------" + System.lineSeparator()
                        + "Type m for more results. Type q to quit program." + System.lineSeparator()
                        + "-----------------------------------------------" + System.lineSeparator()
                        + "11 | Suthek                            | 99  | " + System.lineSeparator()
                        + "-----------------------------------------------" + System.lineSeparator()
                        + "12 | Anubis                            | 98  | " + System.lineSeparator()
                        + "-----------------------------------------------";

        assertEquals(exspected, outputStreamCaptor.toString()
                .trim());
    }

    @Test
    public void testOutputwithQuitInteraction() {

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
                        new ColumnValue("Name", "Sarah-Jane Lillian Long Long Long"),
                        new ColumnValue("Age", "22")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("ID", "4"),
                        new ColumnValue("Name", "Matt"),
                        new ColumnValue("Age", "22")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("ID", "5"),
                        new ColumnValue("Name", "Xanxia"),
                        new ColumnValue("Age", "22")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("ID", "6"),
                        new ColumnValue("Name", "Kate "),
                        new ColumnValue("Age", "22")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("ID", "7"),
                        new ColumnValue("Name", "Flora"),
                        new ColumnValue("Age", "22")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("ID", "8"),
                        new ColumnValue("Name", "Bloom"),
                        new ColumnValue("Age", "22")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("ID", "9"),
                        new ColumnValue("Name", "Aisha"),
                        new ColumnValue("Age", "22")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("ID", "10"),
                        new ColumnValue("Name", "Musa"),
                        new ColumnValue("Age", "22")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("ID", "11"),
                        new ColumnValue("Name", "Suthek"),
                        new ColumnValue("Age", "99")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("ID", "12"),
                        new ColumnValue("Name", "Anubis"),
                        new ColumnValue("Age", "98")
                ))

        ));

        List<Property> properties = List.of(
                PropertyFactory.createProperty(LIKE, Pattern.compile("Harry"))
        );

        provideInput("q");
        outputHandler.printTable(table, properties);

        String exspected =
                "-----------------------------------------------" + System.lineSeparator()
                        + "ID | Name                              | Age | " + System.lineSeparator()
                        + "-----------------------------------------------" + System.lineSeparator()
                        + "-----------------------------------------------" + System.lineSeparator()
                        + "1  | \u001B[31mHarry\u001b[0m                             | 30  | " + System.lineSeparator()
                        + "-----------------------------------------------" + System.lineSeparator()
                        + "2  | Barry                             | 25  | " + System.lineSeparator()
                        + "-----------------------------------------------" + System.lineSeparator()
                        + "3  | Sarah-Jane Lillian Long Long Long | 22  | " + System.lineSeparator()
                        + "-----------------------------------------------" + System.lineSeparator()
                        + "4  | Matt                              | 22  | " + System.lineSeparator()
                        + "-----------------------------------------------" + System.lineSeparator()
                        + "5  | Xanxia                            | 22  | " + System.lineSeparator()
                        + "-----------------------------------------------" + System.lineSeparator()
                        + "6  | Kate                              | 22  | " + System.lineSeparator()
                        + "-----------------------------------------------" + System.lineSeparator()
                        + "7  | Flora                             | 22  | " + System.lineSeparator()
                        + "-----------------------------------------------" + System.lineSeparator()
                        + "8  | Bloom                             | 22  | " + System.lineSeparator()
                        + "-----------------------------------------------" + System.lineSeparator()
                        + "9  | Aisha                             | 22  | " + System.lineSeparator()
                        + "-----------------------------------------------" + System.lineSeparator()
                        + "10 | Musa                              | 22  | " + System.lineSeparator()
                        + "-----------------------------------------------" + System.lineSeparator()
                        + "Type m for more results. Type q to quit program.";

        assertEquals(exspected, outputStreamCaptor.toString()
                .trim());
    }

    @Test
    public void testOutputwithMultipleTables() {

        OutputHandler outputHandler = new OutputHandler();
        Table table1 = new Table("Test1", Arrays.asList(
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
                        new ColumnValue("Name", "Sarah-Jane Lillian Long Long Long"),
                        new ColumnValue("Age", "22")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("ID", "4"),
                        new ColumnValue("Name", "Lara"),
                        new ColumnValue("Age", "22")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("ID", "5"),
                        new ColumnValue("Name", "Xanxia"),
                        new ColumnValue("Age", "22")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("ID", "6"),
                        new ColumnValue("Name", "Kate "),
                        new ColumnValue("Age", "22")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("ID", "7"),
                        new ColumnValue("Name", "Flora"),
                        new ColumnValue("Age", "22")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("ID", "8"),
                        new ColumnValue("Name", "Bloom"),
                        new ColumnValue("Age", "22")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("ID", "9"),
                        new ColumnValue("Name", "Aisha"),
                        new ColumnValue("Age", "22")
                ))

        ));

        Table table2 = new Table("Test2", Arrays.asList(
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
                        new ColumnValue("Name", "Sarah-Jane Lillian Long Long Long"),
                        new ColumnValue("Age", "22")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("ID", "4"),
                        new ColumnValue("Name", "Matt"),
                        new ColumnValue("Age", "22")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("ID", "5"),
                        new ColumnValue("Name", "Xanxia"),
                        new ColumnValue("Age", "22")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("ID", "6"),
                        new ColumnValue("Name", "Kate "),
                        new ColumnValue("Age", "22")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("ID", "7"),
                        new ColumnValue("Name", "Flora"),
                        new ColumnValue("Age", "22")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("ID", "8"),
                        new ColumnValue("Name", "Bloom"),
                        new ColumnValue("Age", "22")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("ID", "9"),
                        new ColumnValue("Name", "Aisha"),
                        new ColumnValue("Age", "22")
                ))

        ));

        List<Property> properties = List.of(
                PropertyFactory.createProperty(LIKE, Pattern.compile("Harry"))
        );

        outputHandler.printTable(table1, properties);
        outputHandler.printTable(table2, properties);

        String exspected =
                "-----------------------------------------------" + System.lineSeparator()
                        + "ID | Name                              | Age | " + System.lineSeparator()
                        + "-----------------------------------------------" + System.lineSeparator()
                        + "-----------------------------------------------" + System.lineSeparator()
                        + "1  | \u001B[31mHarry\u001b[0m                             | 30  | " + System.lineSeparator()
                        + "-----------------------------------------------" + System.lineSeparator()
                        + "2  | Barry                             | 25  | " + System.lineSeparator()
                        + "-----------------------------------------------" + System.lineSeparator()
                        + "3  | Sarah-Jane Lillian Long Long Long | 22  | " + System.lineSeparator()
                        + "-----------------------------------------------" + System.lineSeparator()
                        + "4  | Lara                              | 22  | " + System.lineSeparator()
                        + "-----------------------------------------------" + System.lineSeparator()
                        + "5  | Xanxia                            | 22  | " + System.lineSeparator()
                        + "-----------------------------------------------" + System.lineSeparator()
                        + "6  | Kate                              | 22  | " + System.lineSeparator()
                        + "-----------------------------------------------" + System.lineSeparator()
                        + "7  | Flora                             | 22  | " + System.lineSeparator()
                        + "-----------------------------------------------" + System.lineSeparator()
                        + "8  | Bloom                             | 22  | " + System.lineSeparator()
                        + "-----------------------------------------------" + System.lineSeparator()
                        + "9  | Aisha                             | 22  | " + System.lineSeparator()
                        + "-----------------------------------------------" + System.lineSeparator()
                        + "-----------------------------------------------" + System.lineSeparator()
                        + "ID | Name                              | Age | " + System.lineSeparator()
                        + "-----------------------------------------------" + System.lineSeparator()
                        + "-----------------------------------------------" + System.lineSeparator()
                        + "1  | \u001B[31mHarry\u001b[0m                             | 30  | " + System.lineSeparator()
                        + "-----------------------------------------------" + System.lineSeparator()
                        + "2  | Barry                             | 25  | " + System.lineSeparator()
                        + "-----------------------------------------------" + System.lineSeparator()
                        + "3  | Sarah-Jane Lillian Long Long Long | 22  | " + System.lineSeparator()
                        + "-----------------------------------------------" + System.lineSeparator()
                        + "4  | Matt                              | 22  | " + System.lineSeparator()
                        + "-----------------------------------------------" + System.lineSeparator()
                        + "5  | Xanxia                            | 22  | " + System.lineSeparator()
                        + "-----------------------------------------------" + System.lineSeparator()
                        + "6  | Kate                              | 22  | " + System.lineSeparator()
                        + "-----------------------------------------------" + System.lineSeparator()
                        + "7  | Flora                             | 22  | " + System.lineSeparator()
                        + "-----------------------------------------------" + System.lineSeparator()
                        + "8  | Bloom                             | 22  | " + System.lineSeparator()
                        + "-----------------------------------------------" + System.lineSeparator()
                        + "9  | Aisha                             | 22  | " + System.lineSeparator()
                        + "-----------------------------------------------";


        assertEquals(exspected, outputStreamCaptor.toString()
                .trim());
    }

    @Test
    public void testOutputWithMultipleTablesAndInput() {

        OutputHandler outputHandler = new OutputHandler();
        Table table1 = new Table("Test1", Arrays.asList(
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
                        new ColumnValue("Name", "Sarah-Jane Lillian Long Long Long"),
                        new ColumnValue("Age", "22")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("ID", "4"),
                        new ColumnValue("Name", "Lara"),
                        new ColumnValue("Age", "22")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("ID", "5"),
                        new ColumnValue("Name", "Xanxia"),
                        new ColumnValue("Age", "22")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("ID", "6"),
                        new ColumnValue("Name", "Kate "),
                        new ColumnValue("Age", "22")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("ID", "7"),
                        new ColumnValue("Name", "Flora"),
                        new ColumnValue("Age", "22")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("ID", "8"),
                        new ColumnValue("Name", "Bloom"),
                        new ColumnValue("Age", "22")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("ID", "9"),
                        new ColumnValue("Name", "Aisha"),
                        new ColumnValue("Age", "22")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("ID", "10"),
                        new ColumnValue("Name", "Decimo"),
                        new ColumnValue("Age", "22")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("ID", "11"),
                        new ColumnValue("Name", "Uno"),
                        new ColumnValue("Age", "22")
                ))

        ));

        Table table2 = new Table("Test2", Arrays.asList(
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
                        new ColumnValue("Name", "Sarah-Jane Lillian Long Long Long"),
                        new ColumnValue("Age", "22")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("ID", "4"),
                        new ColumnValue("Name", "Matt"),
                        new ColumnValue("Age", "22")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("ID", "5"),
                        new ColumnValue("Name", "Xanxia"),
                        new ColumnValue("Age", "22")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("ID", "6"),
                        new ColumnValue("Name", "Kate "),
                        new ColumnValue("Age", "22")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("ID", "7"),
                        new ColumnValue("Name", "Flora"),
                        new ColumnValue("Age", "22")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("ID", "8"),
                        new ColumnValue("Name", "Bloom"),
                        new ColumnValue("Age", "22")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("ID", "9"),
                        new ColumnValue("Name", "Aisha"),
                        new ColumnValue("Age", "22")
                ))

        ));

        List<Property> properties = List.of(
                PropertyFactory.createProperty(LIKE, Pattern.compile("Harry"))
        );

        provideInput("m");

        outputHandler.printTable(table1, properties);
        outputHandler.printTable(table2, properties);

        String exspected =
                "-----------------------------------------------" + System.lineSeparator()
                        + "ID | Name                              | Age | " + System.lineSeparator()
                        + "-----------------------------------------------" + System.lineSeparator()
                        + "-----------------------------------------------" + System.lineSeparator()
                        + "1  | \u001B[31mHarry\u001b[0m                             | 30  | " + System.lineSeparator()
                        + "-----------------------------------------------" + System.lineSeparator()
                        + "2  | Barry                             | 25  | " + System.lineSeparator()
                        + "-----------------------------------------------" + System.lineSeparator()
                        + "3  | Sarah-Jane Lillian Long Long Long | 22  | " + System.lineSeparator()
                        + "-----------------------------------------------" + System.lineSeparator()
                        + "4  | Lara                              | 22  | " + System.lineSeparator()
                        + "-----------------------------------------------" + System.lineSeparator()
                        + "5  | Xanxia                            | 22  | " + System.lineSeparator()
                        + "-----------------------------------------------" + System.lineSeparator()
                        + "6  | Kate                              | 22  | " + System.lineSeparator()
                        + "-----------------------------------------------" + System.lineSeparator()
                        + "7  | Flora                             | 22  | " + System.lineSeparator()
                        + "-----------------------------------------------" + System.lineSeparator()
                        + "8  | Bloom                             | 22  | " + System.lineSeparator()
                        + "-----------------------------------------------" + System.lineSeparator()
                        + "9  | Aisha                             | 22  | " + System.lineSeparator()
                        + "-----------------------------------------------" + System.lineSeparator()
                        + "10 | Decimo                            | 22  | " + System.lineSeparator()
                        + "-----------------------------------------------" + System.lineSeparator()
                        + "Type m for more results. Type q to quit program."+ System.lineSeparator()
                        + "-----------------------------------------------" + System.lineSeparator()
                        + "11 | Uno                               | 22  | " + System.lineSeparator()
                        + "-----------------------------------------------" + System.lineSeparator()
                        + "-----------------------------------------------" + System.lineSeparator()
                        + "ID | Name                              | Age | " + System.lineSeparator()
                        + "-----------------------------------------------" + System.lineSeparator()
                        + "-----------------------------------------------" + System.lineSeparator()
                        + "1  | \u001B[31mHarry\u001b[0m                             | 30  | " + System.lineSeparator()
                        + "-----------------------------------------------" + System.lineSeparator()
                        + "2  | Barry                             | 25  | " + System.lineSeparator()
                        + "-----------------------------------------------" + System.lineSeparator()
                        + "3  | Sarah-Jane Lillian Long Long Long | 22  | " + System.lineSeparator()
                        + "-----------------------------------------------" + System.lineSeparator()
                        + "4  | Matt                              | 22  | " + System.lineSeparator()
                        + "-----------------------------------------------" + System.lineSeparator()
                        + "5  | Xanxia                            | 22  | " + System.lineSeparator()
                        + "-----------------------------------------------" + System.lineSeparator()
                        + "6  | Kate                              | 22  | " + System.lineSeparator()
                        + "-----------------------------------------------" + System.lineSeparator()
                        + "7  | Flora                             | 22  | " + System.lineSeparator()
                        + "-----------------------------------------------" + System.lineSeparator()
                        + "8  | Bloom                             | 22  | " + System.lineSeparator()
                        + "-----------------------------------------------" + System.lineSeparator()
                        + "9  | Aisha                             | 22  | " + System.lineSeparator()
                        + "-----------------------------------------------";


        assertEquals(exspected, outputStreamCaptor.toString()
                .trim());
    }
}
