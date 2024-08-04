package de.hdm_stuttgart.mi.dad;

import de.hdm_stuttgart.mi.dad.core.entity.ColumnValue;
import de.hdm_stuttgart.mi.dad.core.entity.Row;
import de.hdm_stuttgart.mi.dad.core.entity.Table;
import de.hdm_stuttgart.mi.dad.core.exception.ServiceException;
import de.hdm_stuttgart.mi.dad.core.ports.ServicePort;
import de.hdm_stuttgart.mi.dad.core.property.Property;
import de.hdm_stuttgart.mi.dad.core.property.PropertyType;
import de.hdm_stuttgart.mi.dad.core.property.properties.PropertyFactory;
import de.hdm_stuttgart.mi.dad.incoming.input.InputHandler;
import de.hdm_stuttgart.mi.dad.incoming.output.OutputHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Pattern;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TestInputHandler {
    @Mock
    OutputHandler mockOutputHandler;
    @Mock
    ServicePort mockServicePort;
    InputHandler inputHandler;

    @BeforeEach
    void setUp() {
        reset(mockOutputHandler);
        reset(mockServicePort);
        inputHandler = new InputHandler(mockServicePort, mockOutputHandler);
    }

    @Test
    void testPropertyList() throws ServiceException, IOException {
        List<Table> tables = new ArrayList<>();
        when(mockServicePort.searchThroughWholeDatabase(anyList())).thenReturn(tables);

        String[] argsWithNoProperty = {};
        inputHandler.handleInput(argsWithNoProperty);
        verify(mockServicePort).searchThroughWholeDatabase(Collections.emptyList());

        String[] argsWithOneProperty = {"--like", "test"};
        inputHandler.handleInput(argsWithOneProperty);
        verify(mockServicePort).searchThroughWholeDatabase(List.of(PropertyFactory.createProperty(PropertyType.LIKE, Pattern.compile("test"))));

        String[] argsWithMultipleProperty = {"--like", "test", "--regex", "regex", "--equal", "10", "--greater", "14", "--range", "14.6,18"};
        inputHandler.handleInput(argsWithMultipleProperty);
        List<Property<?>> multipleProperty = List.of(PropertyFactory.createProperty(PropertyType.LIKE, Pattern.compile("test")),
                PropertyFactory.createProperty(PropertyType.REGEX, Pattern.compile("regex")),
                PropertyFactory.createProperty(PropertyType.EQUAL, new BigDecimal("10")),
                PropertyFactory.createProperty(PropertyType.GREATER_NUMERIC, new BigDecimal("14")),
                PropertyFactory.createProperty(PropertyType.RANGE_NUMERIC, new BigDecimal[]{new BigDecimal("14.6"), new BigDecimal("18")}));
        verify(mockServicePort).searchThroughWholeDatabase(multipleProperty);

        String[] argsWithMultipleSameProperty = {"--like", "test1", "--like", "test2", "--regex", "regex"};
        inputHandler.handleInput(argsWithMultipleSameProperty);
        List<Property<?>> multipleSameProperty = List.of(PropertyFactory.createProperty(PropertyType.LIKE, Pattern.compile("test1")),
                PropertyFactory.createProperty(PropertyType.LIKE, Pattern.compile("test2")),
                PropertyFactory.createProperty(PropertyType.REGEX, Pattern.compile("regex")));
        verify(mockServicePort).searchThroughWholeDatabase(multipleSameProperty);
    }

    @Test
    void testSearchWholeDatabase() throws ServiceException, IOException {
        String[] args = {"--like", "test"};
        List<Property<?>> propertyLike = List.of(PropertyFactory.createProperty(PropertyType.LIKE, Pattern.compile("test")));
        List<Table> tables = List.of(new Table("table1", List.of(new Row(List.of(new ColumnValue("column", "test"))))),
                new Table("table2", List.of(new Row(List.of(new ColumnValue("column", "test"))))));
        when(mockServicePort.searchThroughWholeDatabase(anyList())).thenReturn(tables);

        inputHandler.handleInput(args);

        verify(mockServicePort).searchThroughWholeDatabase(propertyLike);
        verify(mockOutputHandler).printTable(tables.getFirst(), propertyLike);
        verify(mockOutputHandler).printTable(tables.get(1), propertyLike);
    }

    @Test
    void testSearchSpecificTables() throws ServiceException, IOException {
        String[] args = {"--table", "table1", "--table", "table2", "--like", "test"};
        List<Property<?>> propertyLike = List.of(PropertyFactory.createProperty(PropertyType.LIKE, Pattern.compile("test")));
        List<Table> tables = List.of(new Table("table1", List.of(new Row(List.of(new ColumnValue("column", "test"))))),
                new Table("table2", List.of(new Row(List.of(new ColumnValue("column", "test"))))));
        when(mockServicePort.searchThroughTables(anyList(), anyList())).thenReturn(tables);

        inputHandler.handleInput(args);

        List<String> tableString = List.of("table1", "table2");
        verify(mockServicePort).searchThroughTables(tableString, propertyLike);
        verify(mockOutputHandler).printTable(tables.getFirst(), propertyLike);
        verify(mockOutputHandler).printTable(tables.get(1), propertyLike);
    }

    @Test
    void testSearchSpecificColumns() throws ServiceException, IOException {
        String[] args = {"--column", "table1.col1", "--column", "table1.col2", "--column", "table2.col3", "--like", "test"};
        List<Property<?>> propertyLike = List.of(PropertyFactory.createProperty(PropertyType.LIKE, Pattern.compile("test")));
        List<Table> tables = List.of(new Table("table1", List.of(new Row(List.of(new ColumnValue("column", "test"))))),
                new Table("table2", List.of(new Row(List.of(new ColumnValue("column", "test"))))));
        when(mockServicePort.searchThroughColumns(anyMap(), anyList())).thenReturn(tables);

        inputHandler.handleInput(args);

        Map<String, List<String>> columnsByTable = new HashMap<>();
        columnsByTable.put("table1", List.of("col1", "col2"));
        columnsByTable.put("table2", List.of("col3"));
        verify(mockServicePort).searchThroughColumns(columnsByTable, propertyLike);
        verify(mockOutputHandler).printTable(tables.getFirst(), propertyLike);
        verify(mockOutputHandler).printTable(tables.get(1), propertyLike);
    }

    @Test
    void testSearchSpecificColumnAndTable()  throws ServiceException, IOException {
        String[] args = {"--column", "table1.col1", "--table", "table2", "--like", "test"};
        List<Property<?>> propertyLike = List.of(PropertyFactory.createProperty(PropertyType.LIKE, Pattern.compile("test")));
        List<Table> table1 = List.of(new Table("table1", List.of(new Row(List.of(new ColumnValue("column", "test"))))));
        List<Table> table2 = List.of(new Table("table2", List.of(new Row(List.of(new ColumnValue("column", "test"))))));
        when(mockServicePort.searchThroughTables(anyList(), anyList())).thenReturn(table1);
        when(mockServicePort.searchThroughColumns(anyMap(), anyList())).thenReturn(table2);

        inputHandler.handleInput(args);

        Map<String, List<String>> columnsByTable = new HashMap<>();
        columnsByTable.put("table1", List.of("col1"));
        verify(mockServicePort).searchThroughColumns(columnsByTable, propertyLike);
        List<String> tableString = List.of("table2");
        verify(mockServicePort).searchThroughTables(tableString, propertyLike);
        verify(mockOutputHandler).printTable(table1.getFirst(), propertyLike);
        verify(mockOutputHandler).printTable(table2.getFirst(), propertyLike);
    }

    @Test
    void testPrintManual() throws IOException, ServiceException {
        String[] args = {"--help"};
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        inputHandler.handleInput(args);

        assert (outContent.toString().contains("DESCRIPTION"));
        assert (outContent.toString().contains("PARAMETERS FOR MATCHING"));
    }
}
