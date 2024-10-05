package de.hdm_stuttgart.mi.dad.incoming;

import de.hdm_stuttgart.mi.dad.core.entity.ColumnValue;
import de.hdm_stuttgart.mi.dad.core.entity.Row;
import de.hdm_stuttgart.mi.dad.core.entity.Table;
import de.hdm_stuttgart.mi.dad.core.property.Property;
import de.hdm_stuttgart.mi.dad.core.property.properties.PropertyFactory;
import de.hdm_stuttgart.mi.dad.incoming.output.OutputHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import static de.hdm_stuttgart.mi.dad.core.property.PropertyType.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TestOutputHandler {

    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String BOLD = "\u001B[1m";
    private static final String RESET = "\u001B[0m";

    private final PrintStream standardOut = System.out;
    private ByteArrayOutputStream outputStreamCaptor;
    private ByteArrayInputStream testIn;

    @BeforeEach
    public void setUp() {
        outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    public void tearDown() throws IOException {
        System.setOut(standardOut);
        System.setIn(System.in);
        if (testIn != null) {
            testIn.close();
        }
    }

    void provideInput(String... data) {
        final String input = String.join(System.lineSeparator(), data);
        testIn = new ByteArrayInputStream(input.getBytes());
        System.setIn(testIn);
    }

    @Test
    void testHandleOutput_NoResults() {
        final List<Table> resultTables = List.of();
        final List<Property<?>> properties = List.of();

        OutputHandler.handleOutput(resultTables, properties);

        final String expected = BOLD + RED + "No results found." + RESET + System.lineSeparator();
        assertEquals(expected, outputStreamCaptor.toString());
    }

    @Test
    void testHandleOutput_WithoutPagination() {
        final Table table = new Table("Test", Arrays.asList(
                new Row(Arrays.asList(
                        new ColumnValue("ID", "1"),
                        new ColumnValue("Name", "Harry"),
                        new ColumnValue("Age", "30")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("ID", "2"),
                        new ColumnValue("Name", "Barry"),
                        new ColumnValue("Age", "25")
                ))
        ));
        final List<Table> resultTables = List.of(table);
        final List<Property<?>> properties = List.of(
                PropertyFactory.createProperty(LIKE, Pattern.compile("Harry"))
        );

        OutputHandler.handleOutput(resultTables, properties);

        final String expected = System.lineSeparator() + "Table name: " + BOLD + "TEST" + RESET + System.lineSeparator() +
                "------------------" + System.lineSeparator() +
                "ID | Name  | Age | " + System.lineSeparator() +
                "------------------" + System.lineSeparator() +
                "------------------" + System.lineSeparator() +
                "1  | " + RED + "Harry" + RESET + "| 30  | " + System.lineSeparator() +
                "------------------" + System.lineSeparator() +
                "2  | Barry | 25  | " + System.lineSeparator() +
                "------------------" + System.lineSeparator() +
                BOLD + YELLOW + "Page 1 of 1 " + RESET + System.lineSeparator() + System.lineSeparator() +
                GREEN + "Enter " + BOLD + BLUE + "N" + RESET + GREEN + " for next page, " + BOLD + BLUE + "P" + RESET + GREEN + " for previous page, " + BOLD + BLUE + "Q" + RESET + GREEN + " to quit:" + RESET;

        assertEquals(expected, outputStreamCaptor.toString());
    }

    @Test
    void testHandleOutput_WithPagination() {
        final Table table = new Table("Test", Arrays.asList(
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
        final List<Table> resultTables = List.of(table);
        final List<Property<?>> properties = List.of(
                PropertyFactory.createProperty(EQUAL, BigDecimal.valueOf(1))
        );

        provideInput("n" + System.lineSeparator() + "q");
        OutputHandler.handleOutput(resultTables, properties);

        final String expected = "Table name: " + BOLD + "TEST" + RESET + System.lineSeparator() +
                "----------------------------------------------" + System.lineSeparator()
                + "ID | Name                              | Age | " + System.lineSeparator()
                + "----------------------------------------------" + System.lineSeparator()
                + "----------------------------------------------" + System.lineSeparator()
                + RED + "1" + RESET + "| Harry                             | 30  | " + System.lineSeparator()
                + "----------------------------------------------" + System.lineSeparator()
                + "2  | Barry                             | 25  | " + System.lineSeparator()
                + "----------------------------------------------" + System.lineSeparator()
                + "3  | Sarah-Jane Lillian Long Long Long | 22  | " + System.lineSeparator()
                + "----------------------------------------------" + System.lineSeparator()
                + "4  | Matt                              | 22  | " + System.lineSeparator()
                + "----------------------------------------------" + System.lineSeparator()
                + "5  | Xanxia                            | 22  | " + System.lineSeparator()
                + "----------------------------------------------" + System.lineSeparator()
                + "6  | Kate                              | 22  | " + System.lineSeparator()
                + "----------------------------------------------" + System.lineSeparator()
                + "7  | Flora                             | 22  | " + System.lineSeparator()
                + "----------------------------------------------" + System.lineSeparator()
                + "8  | Bloom                             | 22  | " + System.lineSeparator()
                + "----------------------------------------------" + System.lineSeparator()
                + "9  | Aisha                             | 22  | " + System.lineSeparator()
                + "----------------------------------------------" + System.lineSeparator()
                + "10 | Musa                              | 22  | " + System.lineSeparator()
                + "----------------------------------------------" + System.lineSeparator()
                + BOLD + YELLOW + "Page 1 of 2 " + RESET + System.lineSeparator() + System.lineSeparator()
                + GREEN + "Enter " + BOLD + BLUE + "N" + RESET + GREEN + " for next page, " + BOLD + BLUE + "P" + RESET + GREEN + " for previous page, " + BOLD + BLUE + "Q" + RESET + GREEN + " to quit:" + RESET + System.lineSeparator()
                + "Table name: " + BOLD + "TEST" + RESET + System.lineSeparator()
                + "----------------------------------------------" + System.lineSeparator()
                + "ID | Name                              | Age | " + System.lineSeparator()
                + "----------------------------------------------" + System.lineSeparator()
                + "----------------------------------------------" + System.lineSeparator()
                + "11 | Suthek                            | 99  | " + System.lineSeparator()
                + "----------------------------------------------" + System.lineSeparator()
                + "12 | Anubis                            | 98  | " + System.lineSeparator()
                + "----------------------------------------------" + System.lineSeparator()
                + BOLD + YELLOW + "Page 2 of 2 " + RESET + System.lineSeparator() + System.lineSeparator()
                + GREEN + "Enter " + BOLD + BLUE + "N" + RESET + GREEN + " for next page, " + BOLD + BLUE + "P" + RESET + GREEN + " for previous page, " + BOLD + BLUE + "Q" + RESET + GREEN + " to quit:" + RESET
                + BOLD + GREEN + "Exiting. Goodbye!" + RESET;

        assertEquals(expected, outputStreamCaptor.toString().trim());
    }

    @Test
    void testHandleOutput_WithUserInteractionQuit() {
        final Table table = new Table("Test", Arrays.asList(
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
        final List<Table> resultTables = List.of(table);
        final List<Property<?>> properties = List.of(
                PropertyFactory.createProperty(RANGE_NUMERIC, new BigDecimal[]{BigDecimal.valueOf(2), BigDecimal.valueOf(3)})
        );

        provideInput("q");
        OutputHandler.handleOutput(resultTables, properties);

        final String expected = System.lineSeparator() + "Table name: " + BOLD + "TEST" + RESET + System.lineSeparator() +
                "----------------------------------------------" + System.lineSeparator()
                + "ID | Name                              | Age | " + System.lineSeparator()
                + "----------------------------------------------" + System.lineSeparator()
                + "----------------------------------------------" + System.lineSeparator()
                + "1  | Harry                             | 30  | " + System.lineSeparator()
                + "----------------------------------------------" + System.lineSeparator()
                + RED + "2" + RESET + "| Barry                             | 25  | " + System.lineSeparator()
                + "----------------------------------------------" + System.lineSeparator()
                + RED + "3" + RESET + "| Sarah-Jane Lillian Long Long Long | 22  | " + System.lineSeparator()
                + "----------------------------------------------" + System.lineSeparator()
                + "4  | Matt                              | 22  | " + System.lineSeparator()
                + "----------------------------------------------" + System.lineSeparator()
                + "5  | Xanxia                            | 22  | " + System.lineSeparator()
                + "----------------------------------------------" + System.lineSeparator()
                + "6  | Kate                              | 22  | " + System.lineSeparator()
                + "----------------------------------------------" + System.lineSeparator()
                + "7  | Flora                             | 22  | " + System.lineSeparator()
                + "----------------------------------------------" + System.lineSeparator()
                + "8  | Bloom                             | 22  | " + System.lineSeparator()
                + "----------------------------------------------" + System.lineSeparator()
                + "9  | Aisha                             | 22  | " + System.lineSeparator()
                + "----------------------------------------------" + System.lineSeparator()
                + "10 | Musa                              | 22  | " + System.lineSeparator()
                + "----------------------------------------------" + System.lineSeparator()
                + BOLD + YELLOW + "Page 1 of 2 " + RESET + System.lineSeparator() + System.lineSeparator()
                + GREEN + "Enter " + BOLD + BLUE + "N" + RESET + GREEN + " for next page, " + BOLD + BLUE + "P" + RESET + GREEN + " for previous page, " + BOLD + BLUE + "Q" + RESET + GREEN + " to quit:" + RESET
                + BOLD + GREEN + "Exiting. Goodbye!" + RESET + System.lineSeparator();

        assertEquals(expected, outputStreamCaptor.toString());
    }

    @Test
    void testHandleOutput_WithUserInteractionPrevious() {
        final Table table1 = new Table("Test1", Arrays.asList(
                new Row(Arrays.asList(
                        new ColumnValue("ID", "1"),
                        new ColumnValue("Name", "Harry"),
                        new ColumnValue("Age", "30")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("ID", "2"),
                        new ColumnValue("Name", "Barry"),
                        new ColumnValue("Age", "25")
                ))
        ));
        final Table table2 = new Table("Test2", Arrays.asList(
                new Row(Arrays.asList(
                        new ColumnValue("ID", "1"),
                        new ColumnValue("Name", "Sarah"),
                        new ColumnValue("Age", "22")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("ID", "2"),
                        new ColumnValue("Name", "Matt"),
                        new ColumnValue("Age", "22")
                ))
        ));
        final List<Table> resultTables = List.of(table1, table2);
        final List<Property<?>> properties = List.of(
                PropertyFactory.createProperty(REGEX, Pattern.compile("Harry"))
        );

        provideInput("n" + System.lineSeparator() + "p" + System.lineSeparator() + "q");
        OutputHandler.handleOutput(resultTables, properties);

        final String expected = System.lineSeparator() + "Table name: " + BOLD + "TEST1" + RESET + System.lineSeparator() +
                "------------------" + System.lineSeparator() +
                "ID | Name  | Age | " + System.lineSeparator() +
                "------------------" + System.lineSeparator() +
                "------------------" + System.lineSeparator() +
                "1  | " + RED + "Harry" + RESET + "| 30  | " + System.lineSeparator() +
                "------------------" + System.lineSeparator() +
                "2  | Barry | 25  | " + System.lineSeparator() +
                "------------------" + System.lineSeparator() +
                BOLD + YELLOW + "Page 1 of 2 " + RESET + System.lineSeparator() + System.lineSeparator() +
                GREEN + "Enter " + BOLD + BLUE + "N" + RESET + GREEN + " for next page, " + BOLD + BLUE + "P" + RESET + GREEN + " for previous page, " + BOLD + BLUE + "Q" + RESET + GREEN + " to quit:" + RESET + System.lineSeparator() +
                "Table name: " + BOLD + "TEST2" + RESET + System.lineSeparator() +
                "------------------" + System.lineSeparator() +
                "ID | Name  | Age | " + System.lineSeparator() +
                "------------------" + System.lineSeparator() +
                "------------------" + System.lineSeparator() +
                "1  | Sarah | 22  | " + System.lineSeparator() +
                "------------------" + System.lineSeparator() +
                "2  | Matt  | 22  | " + System.lineSeparator() +
                "------------------" + System.lineSeparator() +
                BOLD + YELLOW + "Page 2 of 2 " + RESET + System.lineSeparator() + System.lineSeparator() +
                GREEN + "Enter " + BOLD + BLUE + "N" + RESET + GREEN + " for next page, " + BOLD + BLUE + "P" + RESET + GREEN + " for previous page, " + BOLD + BLUE + "Q" + RESET + GREEN + " to quit:" + RESET + System.lineSeparator() +
                "Table name: " + BOLD + "TEST1" + RESET + System.lineSeparator() +
                "------------------" + System.lineSeparator() +
                "ID | Name  | Age | " + System.lineSeparator() +
                "------------------" + System.lineSeparator() +
                "------------------" + System.lineSeparator() +
                "1  | " + RED + "Harry" + RESET + "| 30  | " + System.lineSeparator() +
                "------------------" + System.lineSeparator() +
                "2  | Barry | 25  | " + System.lineSeparator() +
                "------------------" + System.lineSeparator() +
                BOLD + YELLOW + "Page 1 of 2 " + RESET + System.lineSeparator() + System.lineSeparator() +
                GREEN + "Enter " + BOLD + BLUE + "N" + RESET + GREEN + " for next page, " + BOLD + BLUE + "P" + RESET + GREEN + " for previous page, " + BOLD + BLUE + "Q" + RESET + GREEN + " to quit:" + RESET +
                BOLD + GREEN + "Exiting. Goodbye!" + RESET + System.lineSeparator();

        assertEquals(expected, outputStreamCaptor.toString());
    }
}
