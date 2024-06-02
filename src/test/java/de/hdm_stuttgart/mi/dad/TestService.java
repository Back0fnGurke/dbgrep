package de.hdm_stuttgart.mi.dad;

import de.hdm_stuttgart.mi.dad.core.Service;
import de.hdm_stuttgart.mi.dad.core.entity.ColumnValue;
import de.hdm_stuttgart.mi.dad.core.entity.Row;
import de.hdm_stuttgart.mi.dad.core.entity.Table;
import de.hdm_stuttgart.mi.dad.core.exception.IllegalColumnNameException;
import de.hdm_stuttgart.mi.dad.core.exception.IllegalPropertyCombinationException;
import de.hdm_stuttgart.mi.dad.core.exception.IllegalTableNameException;
import de.hdm_stuttgart.mi.dad.core.ports.RepositoryPort;
import de.hdm_stuttgart.mi.dad.core.property.Property;
import de.hdm_stuttgart.mi.dad.core.property.PropertyFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static de.hdm_stuttgart.mi.dad.core.property.PropertyType.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

//TODO: test all

@ExtendWith(MockitoExtension.class)
class TestService {

    @Mock
    RepositoryPort repository;
    Service service;

    @BeforeEach
    void setUp() {
        service = new Service(repository);
    }

    @Test
    void test_searchTroughColumns_valid_parameters_one_property() throws Exception {
        final String tableName1 = "test1";
        final String tableName2 = "test2";
        final String tableName3 = "test3";
        final String columnName1 = "column1";
        final String columnName2 = "column2";
        final String columnName3 = "column3";
        final String columnName4 = "column4";
        final String columnName5 = "column5";
        final ColumnValue column1 = new ColumnValue(columnName1, "test");
        final ColumnValue column2 = new ColumnValue(columnName2, "test");
        final ColumnValue column3 = new ColumnValue(columnName3, "test");
        final ColumnValue column4 = new ColumnValue(columnName4, "test");
        final ColumnValue column5 = new ColumnValue(columnName5, "test");
        final Row row1 = new Row(Arrays.asList(column1, column2, column3));
        final Row row2 = new Row(Arrays.asList(column1, column2, column3, column4));
        final Row row3 = new Row(Arrays.asList(column1, column2, column3, column4, column5));
        final Table table1 = new Table(tableName1, Arrays.asList(row1, row1, row1));
        final Table table2 = new Table(tableName2, Arrays.asList(row2, row2, row2, row2));
        final Table table3 = new Table(tableName3, Arrays.asList(row3, row3, row3, row3, row3));
        final List<String> columnNames1 = Arrays.asList(columnName1, columnName2, columnName3);
        final List<String> columnNames2 = Arrays.asList(columnName1, columnName2, columnName3, columnName4);
        final List<String> columnNames3 = Arrays.asList(columnName1, columnName2, columnName3, columnName4, columnName5);
        final List<Property> properties = List.of(PropertyFactory.getProperty(REGEX, Pattern.compile("")));

        when(repository.findTableNames()).thenReturn(Arrays.asList(tableName1, tableName2, tableName3));
        when(repository.findTableColumnNamesAll(tableName1)).thenReturn(columnNames1);
        when(repository.findTableColumnNamesAll(tableName2)).thenReturn(columnNames2);
        when(repository.findTableColumnNamesAll(tableName3)).thenReturn(columnNames3);
        when(repository.findTableRowsWithProperties(tableName1, columnNames1, properties)).thenReturn(table1);
        when(repository.findTableRowsWithProperties(tableName2, columnNames2, properties)).thenReturn(table2);
        when(repository.findTableRowsWithProperties(tableName3, columnNames3, properties)).thenReturn(table3);


        final List<Table> expected = Arrays.asList(table1, table2, table3);
        assertDoesNotThrow(() -> {
            final List<Table> actual = service.searchThroughColumns(
                    Map.of(
                            tableName1, Arrays.asList(columnName1, columnName2, columnName3),
                            tableName2, Arrays.asList(columnName1, columnName2, columnName3, columnName4),
                            tableName3, Arrays.asList(columnName1, columnName2, columnName3, columnName4, columnName5)
                    ),
                    properties
            );
            assertEquals(expected.size(), actual.size());
            for (Table tableExpected : expected) {
                for (Row rowExpected : tableExpected.rows()) {
                    boolean found = false;
                    for (Table tableActual : actual) {
                        for (Row rowActual : tableActual.rows()) {
                            if (rowActual.columns().size() == rowExpected.columns().size() && tableActual.rows().size() == tableExpected.rows().size()) {
                                found = true;
                                break;
                            }
                        }
                    }
                    assertTrue(found, "actual should contain same number of columns, rows and tables.");
                }
            }
        });
    }

    @Test
    void test_searchTroughColumns_valid_parameters_multiple_properties() throws Exception {
        final String tableName1 = "test1";
        final String tableName2 = "test2";
        final String tableName3 = "test3";
        final String columnName1 = "column1";
        final String columnName2 = "column2";
        final String columnName3 = "column3";
        final String columnName4 = "column4";
        final String columnName5 = "column5";
        final ColumnValue column1 = new ColumnValue(columnName1, "test");
        final ColumnValue column2 = new ColumnValue(columnName2, "test");
        final ColumnValue column3 = new ColumnValue(columnName3, "test");
        final ColumnValue column4 = new ColumnValue(columnName4, "test");
        final ColumnValue column5 = new ColumnValue(columnName5, "test");
        final Row row1 = new Row(Arrays.asList(column1, column2, column3));
        final Row row2 = new Row(Arrays.asList(column1, column2, column3, column4));
        final Row row3 = new Row(Arrays.asList(column1, column2, column3, column4, column5));
        final Table table1 = new Table(tableName1, Arrays.asList(row1, row1, row1));
        final Table table2 = new Table(tableName2, Arrays.asList(row2, row2, row2, row2));
        final Table table3 = new Table(tableName3, Arrays.asList(row3, row3, row3, row3, row3));
        final List<String> columnNames1 = Arrays.asList(columnName1, columnName2, columnName3);
        final List<String> columnNames2 = Arrays.asList(columnName1, columnName2, columnName3, columnName4);
        final List<String> columnNames3 = Arrays.asList(columnName1, columnName2, columnName3, columnName4, columnName5);
        final List<Property> properties1 = Arrays.asList(
                PropertyFactory.getProperty(REGEX, Pattern.compile("")),
                PropertyFactory.getProperty(GREATERDATE, LocalDate.now())
        );
        final List<Property> properties2 = Arrays.asList(
                PropertyFactory.getProperty(REGEX, Pattern.compile("")),
                PropertyFactory.getProperty(GREATERNUMERIC, BigDecimal.valueOf(0))
        );

        when(repository.findTableNames()).thenReturn(Arrays.asList(tableName1, tableName2, tableName3));
        when(repository.findTableColumnNamesDate(tableName1)).thenReturn(columnNames1);
        when(repository.findTableColumnNamesDate(tableName2)).thenReturn(columnNames2);
        when(repository.findTableColumnNamesDate(tableName3)).thenReturn(columnNames3);
        when(repository.findTableRowsWithProperties(tableName1, columnNames1, properties1)).thenReturn(table1);
        when(repository.findTableRowsWithProperties(tableName2, columnNames2, properties1)).thenReturn(table2);
        when(repository.findTableRowsWithProperties(tableName3, columnNames3, properties1)).thenReturn(table3);
        when(repository.findTableColumnNamesNumeric(tableName1)).thenReturn(columnNames1);
        when(repository.findTableColumnNamesNumeric(tableName2)).thenReturn(columnNames2);
        when(repository.findTableColumnNamesNumeric(tableName3)).thenReturn(columnNames3);
        when(repository.findTableRowsWithProperties(tableName1, columnNames1, properties2)).thenReturn(table1);
        when(repository.findTableRowsWithProperties(tableName2, columnNames2, properties2)).thenReturn(table2);
        when(repository.findTableRowsWithProperties(tableName3, columnNames3, properties2)).thenReturn(table3);

        final List<Table> expected1 = Arrays.asList(table1, table2, table3);
        assertDoesNotThrow(() -> {
            final List<Table> actual = service.searchThroughColumns(
                    Map.of(
                            tableName1, Arrays.asList(columnName1, columnName2, columnName3),
                            tableName2, Arrays.asList(columnName1, columnName2, columnName3, columnName4),
                            tableName3, Arrays.asList(columnName1, columnName2, columnName3, columnName4, columnName5)
                    ),
                    properties1
            );
            assertEquals(expected1.size(), actual.size());
            for (Table tableExpected : expected1) {
                for (Row rowExpected : tableExpected.rows()) {
                    boolean found = false;
                    for (Table tableActual : actual) {
                        for (Row rowActual : tableActual.rows()) {
                            if (rowActual.columns().size() == rowExpected.columns().size() && tableActual.rows().size() == tableExpected.rows().size()) {
                                found = true;
                                break;
                            }
                        }
                    }
                    assertTrue(found, "actual should contain same number of columns, rows and tables.");
                }
            }
        });


        final List<Table> expected2 = Arrays.asList(table1, table2, table3);
        assertDoesNotThrow(() -> {
            final List<Table> actual = service.searchThroughColumns(
                    Map.of(
                            tableName1, Arrays.asList(columnName1, columnName2, columnName3),
                            tableName2, Arrays.asList(columnName1, columnName2, columnName3, columnName4),
                            tableName3, Arrays.asList(columnName1, columnName2, columnName3, columnName4, columnName5)
                    ),
                    properties2
            );
            assertEquals(expected2.size(), actual.size());
            for (Table tableExpected : expected2) {
                for (Row rowExpected : tableExpected.rows()) {
                    boolean found = false;
                    for (Table tableActual : actual) {
                        for (Row rowActual : tableActual.rows()) {
                            if (rowActual.columns().size() == rowExpected.columns().size() && tableActual.rows().size() == tableExpected.rows().size()) {
                                found = true;
                                break;
                            }
                        }
                    }
                    assertTrue(found, "actual should contain same number of columns, rows and tables.");
                }
            }
        });
    }

    @Test
    void test_searchTroughColumns_invalid_parameters() throws Exception {
        final String tableName1 = "test1";
        final String tableName2 = "test2";
        final String tableName3 = "test3";
        final String columnName1 = "column1";
        final String columnName2 = "column2";
        final String columnName3 = "column3";
        final List<Property> properties = Arrays.asList(
                PropertyFactory.getProperty(GREATERDATE, LocalDate.now()),
                PropertyFactory.getProperty(GREATERNUMERIC, BigDecimal.valueOf(0))
        );

        assertThrows(IllegalArgumentException.class, () -> service.searchThroughColumns(null, null));
        assertThrows(IllegalArgumentException.class, () -> service.searchThroughColumns(Map.of(), null));
        assertThrows(IllegalArgumentException.class, () -> service.searchThroughColumns(Map.of(tableName1, List.of()), null));
        assertThrows(IllegalArgumentException.class, () -> service.searchThroughColumns(Map.of(tableName1, List.of()), List.of()));
        assertThrows(IllegalPropertyCombinationException.class, () -> service.searchThroughColumns(Map.of(tableName1, List.of()), properties));

        when(repository.findTableNames()).thenReturn(Arrays.asList(tableName1, tableName2));
        when(repository.findTableColumnNamesAll(tableName1)).thenReturn(Arrays.asList(columnName1, columnName2));
        when(repository.findTableColumnNamesNumeric(tableName1)).thenReturn(Arrays.asList(columnName1, columnName2));
        when(repository.findTableColumnNamesDate(tableName1)).thenReturn(Arrays.asList(columnName1, columnName2));

        assertThrows(IllegalTableNameException.class, () -> service.searchThroughColumns(
                Map.of(tableName3, List.of("")),
                List.of(PropertyFactory.getProperty(REGEX, Pattern.compile(""))))
        );
        assertThrows(IllegalColumnNameException.class, () -> service.searchThroughColumns(
                Map.of(tableName1, List.of(columnName3)),
                List.of(PropertyFactory.getProperty(REGEX, Pattern.compile(""))))
        );
        assertThrows(IllegalColumnNameException.class, () -> service.searchThroughColumns(
                Map.of(tableName1, List.of(columnName3)),
                List.of(PropertyFactory.getProperty(GREATERNUMERIC, BigDecimal.valueOf(0))))
        );
        assertThrows(IllegalColumnNameException.class, () -> service.searchThroughColumns(
                Map.of(tableName1, List.of(columnName3)),
                List.of(PropertyFactory.getProperty(GREATERDATE, LocalDate.now())))
        );
    }

    @Test
    void test_searchTroughTable_valid_parameters_one_property() throws Exception {
        final String tableName1 = "test1";
        final String tableName2 = "test2";
        final String tableName3 = "test3";
        final String columnName1 = "column1";
        final String columnName2 = "column2";
        final String columnName3 = "column3";
        final String columnName4 = "column4";
        final String columnName5 = "column5";
        final ColumnValue column1 = new ColumnValue(columnName1, "test");
        final ColumnValue column2 = new ColumnValue(columnName2, "test");
        final ColumnValue column3 = new ColumnValue(columnName3, "test");
        final ColumnValue column4 = new ColumnValue(columnName4, "test");
        final ColumnValue column5 = new ColumnValue(columnName5, "test");
        final Row row1 = new Row(Arrays.asList(column1, column2, column3));
        final Row row2 = new Row(Arrays.asList(column1, column2, column3, column4));
        final Row row3 = new Row(Arrays.asList(column1, column2, column3, column4, column5));
        final Table table1 = new Table(tableName1, Arrays.asList(row1, row1, row1));
        final Table table2 = new Table(tableName2, Arrays.asList(row2, row2, row2, row2));
        final Table table3 = new Table(tableName3, Arrays.asList(row3, row3, row3, row3, row3));
        final List<String> columnNames1 = Arrays.asList(columnName1, columnName2, columnName3);
        final List<String> columnNames2 = Arrays.asList(columnName1, columnName2, columnName3, columnName4);
        final List<String> columnNames3 = Arrays.asList(columnName1, columnName2, columnName3, columnName4, columnName5);
        final List<Property> properties = List.of(PropertyFactory.getProperty(REGEX, Pattern.compile("")));

        when(repository.findTableNames()).thenReturn(Arrays.asList(tableName1, tableName2, tableName3));
        when(repository.findTableColumnNamesAll(tableName1)).thenReturn(columnNames1);
        when(repository.findTableColumnNamesAll(tableName2)).thenReturn(columnNames2);
        when(repository.findTableColumnNamesAll(tableName3)).thenReturn(columnNames3);
        when(repository.findTableRowsWithProperties(tableName1, columnNames1, properties)).thenReturn(table1);
        when(repository.findTableRowsWithProperties(tableName2, columnNames2, properties)).thenReturn(table2);
        when(repository.findTableRowsWithProperties(tableName3, columnNames3, properties)).thenReturn(table3);


        final List<Table> expected = Arrays.asList(table1, table2, table3);
        assertDoesNotThrow(() -> {
            final List<Table> actual = service.searchThroughTables(Arrays.asList(tableName1, tableName2, tableName3), properties);
            assertEquals(expected.size(), actual.size());
            for (Table tableExpected : expected) {
                for (Row rowExpected : tableExpected.rows()) {
                    boolean found = false;
                    for (Table tableActual : actual) {
                        for (Row rowActual : tableActual.rows()) {
                            if (rowActual.columns().size() == rowExpected.columns().size() && tableActual.rows().size() == tableExpected.rows().size()) {
                                found = true;
                                break;
                            }
                        }
                    }
                    assertTrue(found, "actual should contain same number of columns, rows and tables.");
                }
            }
        });
    }

    @Test
    void test_searchTroughTable_valid_parameters_multiple_properties() throws Exception {
        final String tableName1 = "test1";
        final String tableName2 = "test2";
        final String tableName3 = "test3";
        final String columnName1 = "column1";
        final String columnName2 = "column2";
        final String columnName3 = "column3";
        final String columnName4 = "column4";
        final String columnName5 = "column5";
        final ColumnValue column1 = new ColumnValue(columnName1, "test");
        final ColumnValue column2 = new ColumnValue(columnName2, "test");
        final ColumnValue column3 = new ColumnValue(columnName3, "test");
        final ColumnValue column4 = new ColumnValue(columnName4, "test");
        final ColumnValue column5 = new ColumnValue(columnName5, "test");
        final Row row1 = new Row(Arrays.asList(column1, column2, column3));
        final Row row2 = new Row(Arrays.asList(column1, column2, column3, column4));
        final Row row3 = new Row(Arrays.asList(column1, column2, column3, column4, column5));
        final Table table1 = new Table(tableName1, Arrays.asList(row1, row1, row1));
        final Table table2 = new Table(tableName2, Arrays.asList(row2, row2, row2, row2));
        final Table table3 = new Table(tableName3, Arrays.asList(row3, row3, row3, row3, row3));
        final List<String> columnNames1 = Arrays.asList(columnName1, columnName2, columnName3);
        final List<String> columnNames2 = Arrays.asList(columnName1, columnName2, columnName3, columnName4);
        final List<String> columnNames3 = Arrays.asList(columnName1, columnName2, columnName3, columnName4, columnName5);
        final List<Property> properties1 = Arrays.asList(
                PropertyFactory.getProperty(REGEX, Pattern.compile("")),
                PropertyFactory.getProperty(GREATERDATE, LocalDate.now())
        );
        final List<Property> properties2 = Arrays.asList(
                PropertyFactory.getProperty(REGEX, Pattern.compile("")),
                PropertyFactory.getProperty(GREATERNUMERIC, BigDecimal.valueOf(0))
        );

        when(repository.findTableNames()).thenReturn(Arrays.asList(tableName1, tableName2, tableName3));
        when(repository.findTableColumnNamesDate(tableName1)).thenReturn(columnNames1);
        when(repository.findTableColumnNamesDate(tableName2)).thenReturn(columnNames2);
        when(repository.findTableColumnNamesDate(tableName3)).thenReturn(columnNames3);
        when(repository.findTableRowsWithProperties(tableName1, columnNames1, properties1)).thenReturn(table1);
        when(repository.findTableRowsWithProperties(tableName2, columnNames2, properties1)).thenReturn(table2);
        when(repository.findTableRowsWithProperties(tableName3, columnNames3, properties1)).thenReturn(table3);
        when(repository.findTableColumnNamesNumeric(tableName1)).thenReturn(columnNames1);
        when(repository.findTableColumnNamesNumeric(tableName2)).thenReturn(columnNames2);
        when(repository.findTableColumnNamesNumeric(tableName3)).thenReturn(columnNames3);
        when(repository.findTableRowsWithProperties(tableName1, columnNames1, properties2)).thenReturn(table1);
        when(repository.findTableRowsWithProperties(tableName2, columnNames2, properties2)).thenReturn(table2);
        when(repository.findTableRowsWithProperties(tableName3, columnNames3, properties2)).thenReturn(table3);

        final List<Table> expected1 = Arrays.asList(table1, table2, table3);
        assertDoesNotThrow(() -> {
            final List<Table> actual = service.searchThroughTables(Arrays.asList(tableName1, tableName2, tableName3), properties1);
            assertEquals(expected1.size(), actual.size());
            for (Table tableExpected : expected1) {
                for (Row rowExpected : tableExpected.rows()) {
                    boolean found = false;
                    for (Table tableActual : actual) {
                        for (Row rowActual : tableActual.rows()) {
                            if (rowActual.columns().size() == rowExpected.columns().size() && tableActual.rows().size() == tableExpected.rows().size()) {
                                found = true;
                                break;
                            }
                        }
                    }
                    assertTrue(found, "actual should contain same number of columns, rows and tables.");
                }
            }
        });


        final List<Table> expected2 = Arrays.asList(table1, table2, table3);
        assertDoesNotThrow(() -> {
            final List<Table> actual = service.searchThroughTables(Arrays.asList(tableName1, tableName2, tableName3), properties2);
            assertEquals(expected2.size(), actual.size());
            for (Table tableExpected : expected2) {
                for (Row rowExpected : tableExpected.rows()) {
                    boolean found = false;
                    for (Table tableActual : actual) {
                        for (Row rowActual : tableActual.rows()) {
                            if (rowActual.columns().size() == rowExpected.columns().size() && tableActual.rows().size() == tableExpected.rows().size()) {
                                found = true;
                                break;
                            }
                        }
                    }
                    assertTrue(found, "actual should contain same number of columns, rows and tables.");
                }
            }
        });
    }

    @Test
    void test_searchTroughTable_invalid_parameters() throws Exception {
        final String tableName1 = "test1";
        final String tableName2 = "test2";
        final String tableName3 = "test3";
        final List<Property> properties = Arrays.asList(
                PropertyFactory.getProperty(GREATERDATE, LocalDate.now()),
                PropertyFactory.getProperty(GREATERNUMERIC, BigDecimal.valueOf(0))
        );

        assertThrows(IllegalArgumentException.class, () -> service.searchThroughTables(null, null));
        assertThrows(IllegalArgumentException.class, () -> service.searchThroughTables(List.of(), null));
        assertThrows(IllegalArgumentException.class, () -> service.searchThroughTables(List.of(tableName1), null));
        assertThrows(IllegalArgumentException.class, () -> service.searchThroughTables(List.of(tableName1), List.of()));
        assertThrows(IllegalPropertyCombinationException.class, () -> service.searchThroughTables(List.of(""), properties));

        when(repository.findTableNames()).thenReturn(Arrays.asList(tableName1, tableName2));
        assertThrows(IllegalTableNameException.class, () -> service.searchThroughTables(Arrays.asList(tableName1, tableName2, tableName3), List.of(PropertyFactory.getProperty(REGEX, Pattern.compile("")))));
    }

    @Test
    void test_searchTroughWholeDatabase_valid_parameters_one_property() throws Exception {
        final String tableName1 = "test1";
        final String tableName2 = "test2";
        final String tableName3 = "test3";
        final String columnName1 = "column1";
        final String columnName2 = "column2";
        final String columnName3 = "column3";
        final String columnName4 = "column4";
        final String columnName5 = "column5";
        final ColumnValue column1 = new ColumnValue(columnName1, "test");
        final ColumnValue column2 = new ColumnValue(columnName2, "test");
        final ColumnValue column3 = new ColumnValue(columnName3, "test");
        final ColumnValue column4 = new ColumnValue(columnName4, "test");
        final ColumnValue column5 = new ColumnValue(columnName5, "test");
        final Row row1 = new Row(Arrays.asList(column1, column2, column3));
        final Row row2 = new Row(Arrays.asList(column1, column2, column3, column4));
        final Row row3 = new Row(Arrays.asList(column1, column2, column3, column4, column5));
        final Table table1 = new Table(tableName1, Arrays.asList(row1, row1, row1));
        final Table table2 = new Table(tableName2, Arrays.asList(row2, row2, row2, row2));
        final Table table3 = new Table(tableName3, Arrays.asList(row3, row3, row3, row3, row3));
        final List<String> columnNames1 = Arrays.asList(columnName1, columnName2, columnName3);
        final List<String> columnNames2 = Arrays.asList(columnName1, columnName2, columnName3, columnName4);
        final List<String> columnNames3 = Arrays.asList(columnName1, columnName2, columnName3, columnName4, columnName5);
        final List<Property> properties = List.of(PropertyFactory.getProperty(REGEX, Pattern.compile("")));

        when(repository.findTableNames()).thenReturn(Arrays.asList(tableName1, tableName2, tableName3));
        when(repository.findTableColumnNamesAll(tableName1)).thenReturn(columnNames1);
        when(repository.findTableColumnNamesAll(tableName2)).thenReturn(columnNames2);
        when(repository.findTableColumnNamesAll(tableName3)).thenReturn(columnNames3);
        when(repository.findTableRowsWithProperties(tableName1, columnNames1, properties)).thenReturn(table1);
        when(repository.findTableRowsWithProperties(tableName2, columnNames2, properties)).thenReturn(table2);
        when(repository.findTableRowsWithProperties(tableName3, columnNames3, properties)).thenReturn(table3);


        final List<Table> expected = Arrays.asList(table1, table2, table3);
        assertDoesNotThrow(() -> {
            final List<Table> actual = service.searchThroughWholeDatabase(properties);
            assertEquals(expected.size(), actual.size());
            for (Table tableExpected : expected) {
                for (Row rowExpected : tableExpected.rows()) {
                    boolean found = false;
                    for (Table tableActual : actual) {
                        for (Row rowActual : tableActual.rows()) {
                            if (rowActual.columns().size() == rowExpected.columns().size() && tableActual.rows().size() == tableExpected.rows().size()) {
                                found = true;
                                break;
                            }
                        }
                    }
                    assertTrue(found, "actual should contain same number of columns, rows and tables.");
                }
            }
        });
    }

    @Test
    void test_searchTroughWholeDatabase_valid_parameters_multiple_properties() throws Exception {
        final String tableName1 = "test1";
        final String tableName2 = "test2";
        final String tableName3 = "test3";
        final String columnName1 = "column1";
        final String columnName2 = "column2";
        final String columnName3 = "column3";
        final String columnName4 = "column4";
        final String columnName5 = "column5";
        final ColumnValue column1 = new ColumnValue(columnName1, "test");
        final ColumnValue column2 = new ColumnValue(columnName2, "test");
        final ColumnValue column3 = new ColumnValue(columnName3, "test");
        final ColumnValue column4 = new ColumnValue(columnName4, "test");
        final ColumnValue column5 = new ColumnValue(columnName5, "test");
        final Row row1 = new Row(Arrays.asList(column1, column2, column3));
        final Row row2 = new Row(Arrays.asList(column1, column2, column3, column4));
        final Row row3 = new Row(Arrays.asList(column1, column2, column3, column4, column5));
        final Table table1 = new Table(tableName1, Arrays.asList(row1, row1, row1));
        final Table table2 = new Table(tableName2, Arrays.asList(row2, row2, row2, row2));
        final Table table3 = new Table(tableName3, Arrays.asList(row3, row3, row3, row3, row3));
        final List<String> columnNames1 = Arrays.asList(columnName1, columnName2, columnName3);
        final List<String> columnNames2 = Arrays.asList(columnName1, columnName2, columnName3, columnName4);
        final List<String> columnNames3 = Arrays.asList(columnName1, columnName2, columnName3, columnName4, columnName5);
        final List<Property> properties1 = List.of(
                PropertyFactory.getProperty(REGEX, Pattern.compile("")),
                PropertyFactory.getProperty(GREATERDATE, LocalDate.now())
        );
        final List<Property> properties2 = List.of(
                PropertyFactory.getProperty(REGEX, Pattern.compile("")),
                PropertyFactory.getProperty(GREATERNUMERIC, BigDecimal.valueOf(0))
        );

        when(repository.findTableNames()).thenReturn(Arrays.asList(tableName1, tableName2, tableName3));
        when(repository.findTableColumnNamesDate(tableName1)).thenReturn(columnNames1);
        when(repository.findTableColumnNamesDate(tableName2)).thenReturn(columnNames2);
        when(repository.findTableColumnNamesDate(tableName3)).thenReturn(columnNames3);
        when(repository.findTableRowsWithProperties(tableName1, columnNames1, properties1)).thenReturn(table1);
        when(repository.findTableRowsWithProperties(tableName2, columnNames2, properties1)).thenReturn(table2);
        when(repository.findTableRowsWithProperties(tableName3, columnNames3, properties1)).thenReturn(table3);
        when(repository.findTableColumnNamesNumeric(tableName1)).thenReturn(columnNames1);
        when(repository.findTableColumnNamesNumeric(tableName2)).thenReturn(columnNames2);
        when(repository.findTableColumnNamesNumeric(tableName3)).thenReturn(columnNames3);
        when(repository.findTableRowsWithProperties(tableName1, columnNames1, properties2)).thenReturn(table1);
        when(repository.findTableRowsWithProperties(tableName2, columnNames2, properties2)).thenReturn(table2);
        when(repository.findTableRowsWithProperties(tableName3, columnNames3, properties2)).thenReturn(table3);

        final List<Table> expected1 = Arrays.asList(table1, table2, table3);
        assertDoesNotThrow(() -> {
            final List<Table> actual = service.searchThroughWholeDatabase(properties1);
            assertEquals(expected1.size(), actual.size());
            for (Table tableExpected : expected1) {
                for (Row rowExpected : tableExpected.rows()) {
                    boolean found = false;
                    for (Table tableActual : actual) {
                        for (Row rowActual : tableActual.rows()) {
                            if (rowActual.columns().size() == rowExpected.columns().size() && tableActual.rows().size() == tableExpected.rows().size()) {
                                found = true;
                                break;
                            }
                        }
                    }
                    assertTrue(found, "actual should contain same number of columns, rows and tables.");
                }
            }
        });


        final List<Table> expected2 = Arrays.asList(table1, table2, table3);
        assertDoesNotThrow(() -> {
            final List<Table> actual = service.searchThroughWholeDatabase(properties2);
            assertEquals(expected2.size(), actual.size());
            for (Table tableExpected : expected2) {
                for (Row rowExpected : tableExpected.rows()) {
                    boolean found = false;
                    for (Table tableActual : actual) {
                        for (Row rowActual : tableActual.rows()) {
                            if (rowActual.columns().size() == rowExpected.columns().size() && tableActual.rows().size() == tableExpected.rows().size()) {
                                found = true;
                                break;
                            }
                        }
                    }
                    assertTrue(found, "actual should contain same number of columns, rows and tables.");
                }
            }
        });
    }

    @Test
    void test_searchTroughWholeDatabase_invalid_parameters() throws Exception {
        final List<Property> properties = Arrays.asList(
                PropertyFactory.getProperty(GREATERDATE, LocalDate.now()),
                PropertyFactory.getProperty(GREATERNUMERIC, BigDecimal.valueOf(0))
        );

        assertThrows(IllegalArgumentException.class, () -> service.searchThroughWholeDatabase(null));
        assertThrows(IllegalArgumentException.class, () -> service.searchThroughWholeDatabase(List.of()));
        assertThrows(IllegalPropertyCombinationException.class, () -> service.searchThroughWholeDatabase(properties));
    }
}
