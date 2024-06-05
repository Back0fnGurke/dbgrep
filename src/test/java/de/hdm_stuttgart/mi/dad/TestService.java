package de.hdm_stuttgart.mi.dad;

import de.hdm_stuttgart.mi.dad.core.Service;
import de.hdm_stuttgart.mi.dad.core.entity.ColumnValue;
import de.hdm_stuttgart.mi.dad.core.entity.Row;
import de.hdm_stuttgart.mi.dad.core.entity.Table;
import de.hdm_stuttgart.mi.dad.core.exception.IllegalColumnNameException;
import de.hdm_stuttgart.mi.dad.core.exception.IllegalTableNameException;
import de.hdm_stuttgart.mi.dad.core.exception.ServiceException;
import de.hdm_stuttgart.mi.dad.core.ports.RepositoryPort;
import de.hdm_stuttgart.mi.dad.core.property.Property;
import de.hdm_stuttgart.mi.dad.core.property.PropertyFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static de.hdm_stuttgart.mi.dad.core.property.PropertyType.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TestService {

    @Mock
    RepositoryPort repository;
    Service service;

    @BeforeEach
    void setUp() {
        reset(repository);
        service = new Service(repository);
    }

    @Test
    void test_searchThroughColumns_one_property() throws Exception {
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
        final Row row1 = new Row(List.of(column1, column2, column3));
        final Row row2 = new Row(List.of(column1, column2, column3, column4));
        final Row row3 = new Row(List.of(column1, column2, column3, column4, column5));
        final Table table1 = new Table(tableName1, List.of(row1, row1, row1));
        final Table table2 = new Table(tableName2, List.of(row2, row2, row2, row2));
        final Table table3 = new Table(tableName3, List.of(row3, row3, row3, row3, row3));
        final List<String> columnNames1 = List.of(columnName1, columnName2, columnName3);
        final List<String> columnNames2 = List.of(columnName1, columnName2, columnName3, columnName4);
        final List<String> columnNames3 = List.of(columnName1, columnName2, columnName3, columnName4, columnName5);
        final Property property = PropertyFactory.getProperty(REGEX, Pattern.compile(""));

        when(repository.findTableColumnNamesAll(tableName1)).thenReturn(columnNames1);
        when(repository.findTableColumnNamesAll(tableName2)).thenReturn(columnNames2);
        when(repository.findTableColumnNamesAll(tableName3)).thenReturn(columnNames3);
        when(repository.findTableRowsWithProperties(tableName1, Map.of(property, columnNames1))).thenReturn(table1);
        when(repository.findTableRowsWithProperties(tableName2, Map.of(property, columnNames2))).thenReturn(table2);
        when(repository.findTableRowsWithProperties(tableName3, Map.of(property, columnNames3))).thenReturn(table3);

        final List<Table> expected = List.of(table1, table2, table3);
        final List<Table> actual = service.searchThroughColumns(
                Map.of(tableName1, columnNames1, tableName2, columnNames2, tableName3, columnNames3),
                List.of(property)
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
    }

    @Test
    void test_searchThroughColumns_multiple_properties() throws Exception {
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
        final Row row1 = new Row(List.of(column1, column2, column3));
        final Row row2 = new Row(List.of(column1, column2, column3, column4));
        final Row row3 = new Row(List.of(column1, column2, column3, column4, column5));
        final Table table1 = new Table(tableName1, List.of(row1, row1, row1));
        final Table table2 = new Table(tableName2, List.of(row2, row2, row2, row2));
        final Table table3 = new Table(tableName3, List.of(row3, row3, row3, row3, row3));
        final List<String> columnNames1 = List.of(columnName1, columnName2, columnName3);
        final List<String> columnNames2 = List.of(columnName1, columnName2, columnName3, columnName4);
        final List<String> columnNames3 = List.of(columnName1, columnName2, columnName3, columnName4, columnName5);
        final Property property1 = PropertyFactory.getProperty(REGEX, Pattern.compile(""));
        final Property property2 = PropertyFactory.getProperty(GREATERDATE, LocalDate.now());
        final Property property3 = PropertyFactory.getProperty(GREATERNUMERIC, BigDecimal.valueOf(0));
        final List<Property> properties1 = List.of(property1, property2);
        final List<Property> properties2 = List.of(property1, property3);

        when(repository.findTableColumnNamesAll(tableName1)).thenReturn(columnNames1);
        when(repository.findTableColumnNamesDate(tableName1)).thenReturn(columnNames1);
        when(repository.findTableColumnNamesNumeric(tableName1)).thenReturn(columnNames1);
        when(repository.findTableColumnNamesAll(tableName2)).thenReturn(columnNames2);
        when(repository.findTableColumnNamesDate(tableName2)).thenReturn(columnNames2);
        when(repository.findTableColumnNamesNumeric(tableName2)).thenReturn(columnNames2);
        when(repository.findTableColumnNamesAll(tableName3)).thenReturn(columnNames3);
        when(repository.findTableColumnNamesDate(tableName3)).thenReturn(columnNames3);
        when(repository.findTableColumnNamesNumeric(tableName3)).thenReturn(columnNames3);
        when(repository.findTableRowsWithProperties(tableName1, Map.of(property1, columnNames1, property2, columnNames1))).thenReturn(table1);
        when(repository.findTableRowsWithProperties(tableName2, Map.of(property1, columnNames2, property2, columnNames2))).thenReturn(table2);
        when(repository.findTableRowsWithProperties(tableName3, Map.of(property1, columnNames3, property2, columnNames3))).thenReturn(table3);
        when(repository.findTableRowsWithProperties(tableName1, Map.of(property1, columnNames1, property3, columnNames1))).thenReturn(table1);
        when(repository.findTableRowsWithProperties(tableName2, Map.of(property1, columnNames2, property3, columnNames2))).thenReturn(table2);
        when(repository.findTableRowsWithProperties(tableName3, Map.of(property1, columnNames3, property3, columnNames3))).thenReturn(table3);

        final List<Table> expected1 = List.of(table1, table2, table3);
        final List<Table> actual1 = service.searchThroughColumns(
                Map.of(tableName1, columnNames1, tableName2, columnNames2, tableName3, columnNames3),
                properties1
        );
        assertEquals(expected1.size(), actual1.size());
        for (Table tableExpected : expected1) {
            for (Row rowExpected : tableExpected.rows()) {
                boolean found = false;
                for (Table tableActual : actual1) {
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

        final List<Table> expected2 = List.of(table1, table2, table3);
        final List<Table> actual2 = service.searchThroughColumns(
                Map.of(tableName1, columnNames1, tableName2, columnNames2, tableName3, columnNames3),
                properties2
        );
        assertEquals(expected2.size(), actual2.size());
        for (Table tableExpected : expected2) {
            for (Row rowExpected : tableExpected.rows()) {
                boolean found = false;
                for (Table tableActual : actual2) {
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
    }

    @Test
    void test_searchThroughColumns_throws() throws SQLException {
        final String tableName1 = "test1";
        final String tableName2 = "test2";
        final String tableName3 = "test3";
        final String tableName4 = "test4";
        final String columnName1 = "column1";
        final String columnName2 = "column2";
        final String columnName3 = "column3";
        final String columnName4 = "column4";
        final String columnName5 = "column5";
        final List<String> columnNames1 = List.of(columnName1, columnName2, columnName3);
        final List<String> columnNames2 = List.of(columnName1, columnName2, columnName3, columnName4);
        final List<String> columnNames3 = List.of(columnName1, columnName2, columnName3, columnName4, columnName5);
        final Property property1 = PropertyFactory.getProperty(REGEX, Pattern.compile(""));
        final Property property2 = PropertyFactory.getProperty(GREATERDATE, LocalDate.now());
        final Property property3 = PropertyFactory.getProperty(GREATERNUMERIC, BigDecimal.valueOf(0));
        final List<Property> properties1 = List.of(property1, property2);
        final List<Property> properties2 = List.of(property2);
        final List<Property> properties3 = List.of(property3);

        when(repository.findTableColumnNamesAll(tableName1)).thenReturn(columnNames1);
        when(repository.findTableColumnNamesDate(tableName1)).thenReturn(columnNames1);
        when(repository.findTableColumnNamesDate(tableName2)).thenReturn(columnNames2);
        when(repository.findTableColumnNamesNumeric(tableName3)).thenReturn(columnNames3);
        when(repository.findTableRowsWithProperties(tableName1, Map.of(property1, columnNames1, property2, columnNames1))).thenThrow(SQLException.class);
        when(repository.findTableRowsWithProperties(tableName2, Map.of(property2, columnNames2))).thenThrow(SQLException.class);
        when(repository.findTableRowsWithProperties(tableName3, Map.of(property3, columnNames3))).thenThrow(SQLException.class);
        assertThrows(ServiceException.class, () -> service.searchThroughColumns(Map.of(tableName1, columnNames1), properties1));
        assertThrows(ServiceException.class, () -> service.searchThroughColumns(Map.of(tableName2, columnNames2), properties2));
        assertThrows(ServiceException.class, () -> service.searchThroughColumns(Map.of(tableName3, columnNames3), properties3));

        when(repository.findTableColumnNamesAll(tableName4)).thenThrow(SQLException.class);
        assertThrows(ServiceException.class, () -> service.searchThroughColumns(Map.of(tableName4, columnNames3), properties1));
    }

    @Test
    void test_searchThroughTable_one_property() throws Exception {
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
        final Row row1 = new Row(List.of(column1, column2, column3));
        final Row row2 = new Row(List.of(column1, column2, column3, column4));
        final Row row3 = new Row(List.of(column1, column2, column3, column4, column5));
        final Table table1 = new Table(tableName1, List.of(row1, row1, row1));
        final Table table2 = new Table(tableName2, List.of(row2, row2, row2, row2));
        final Table table3 = new Table(tableName3, List.of(row3, row3, row3, row3, row3));
        final List<String> columnNames1 = List.of(columnName1, columnName2, columnName3);
        final List<String> columnNames2 = List.of(columnName1, columnName2, columnName3, columnName4);
        final List<String> columnNames3 = List.of(columnName1, columnName2, columnName3, columnName4, columnName5);
        final Property property = PropertyFactory.getProperty(REGEX, Pattern.compile(""));

        when(repository.findTableColumnNamesAll(tableName1)).thenReturn(columnNames1);
        when(repository.findTableColumnNamesAll(tableName2)).thenReturn(columnNames2);
        when(repository.findTableColumnNamesAll(tableName3)).thenReturn(columnNames3);
        when(repository.findTableRowsWithProperties(tableName1, Map.of(property, columnNames1))).thenReturn(table1);
        when(repository.findTableRowsWithProperties(tableName2, Map.of(property, columnNames2))).thenReturn(table2);
        when(repository.findTableRowsWithProperties(tableName3, Map.of(property, columnNames3))).thenReturn(table3);

        final List<Table> expected = List.of(table1, table2, table3);
        final List<Table> actual = service.searchThroughTables(List.of(tableName1, tableName2, tableName3), List.of(property));
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
    }

    @Test
    void test_searchThrough_multiple_properties() throws Exception {
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
        final Row row1 = new Row(List.of(column1, column2, column3));
        final Row row2 = new Row(List.of(column1, column2, column3, column4));
        final Row row3 = new Row(List.of(column1, column2, column3, column4, column5));
        final Table table1 = new Table(tableName1, List.of(row1, row1, row1));
        final Table table2 = new Table(tableName2, List.of(row2, row2, row2, row2));
        final Table table3 = new Table(tableName3, List.of(row3, row3, row3, row3, row3));
        final List<String> columnNames1 = List.of(columnName1, columnName2, columnName3);
        final List<String> columnNames2 = List.of(columnName1, columnName2, columnName3, columnName4);
        final List<String> columnNames3 = List.of(columnName1, columnName2, columnName3, columnName4, columnName5);
        final Property property1 = PropertyFactory.getProperty(REGEX, Pattern.compile(""));
        final Property property2 = PropertyFactory.getProperty(GREATERDATE, LocalDate.now());
        final Property property3 = PropertyFactory.getProperty(GREATERNUMERIC, BigDecimal.valueOf(0));
        final List<Property> properties1 = List.of(property1, property2);
        final List<Property> properties2 = List.of(property1, property3);

        when(repository.findTableColumnNamesAll(tableName1)).thenReturn(columnNames1);
        when(repository.findTableColumnNamesAll(tableName2)).thenReturn(columnNames2);
        when(repository.findTableColumnNamesAll(tableName3)).thenReturn(columnNames3);
        when(repository.findTableColumnNamesDate(tableName1)).thenReturn(columnNames1);
        when(repository.findTableColumnNamesDate(tableName2)).thenReturn(columnNames2);
        when(repository.findTableColumnNamesDate(tableName3)).thenReturn(columnNames3);
        when(repository.findTableRowsWithProperties(tableName1, Map.of(property1, columnNames1, property2, columnNames1))).thenReturn(table1);
        when(repository.findTableRowsWithProperties(tableName2, Map.of(property1, columnNames2, property2, columnNames2))).thenReturn(table2);
        when(repository.findTableRowsWithProperties(tableName3, Map.of(property1, columnNames3, property2, columnNames3))).thenReturn(table3);
        when(repository.findTableColumnNamesNumeric(tableName1)).thenReturn(columnNames1);
        when(repository.findTableColumnNamesNumeric(tableName2)).thenReturn(columnNames2);
        when(repository.findTableColumnNamesNumeric(tableName3)).thenReturn(columnNames3);
        when(repository.findTableRowsWithProperties(tableName1, Map.of(property1, columnNames1, property3, columnNames1))).thenReturn(table1);
        when(repository.findTableRowsWithProperties(tableName2, Map.of(property1, columnNames2, property3, columnNames2))).thenReturn(table2);
        when(repository.findTableRowsWithProperties(tableName3, Map.of(property1, columnNames3, property3, columnNames3))).thenReturn(table3);

        final List<Table> expected1 = List.of(table1, table2, table3);
        final List<Table> actual1 = service.searchThroughTables(List.of(tableName1, tableName2, tableName3), properties1);
        assertEquals(expected1.size(), actual1.size());
        for (Table tableExpected : expected1) {
            for (Row rowExpected : tableExpected.rows()) {
                boolean found = false;
                for (Table tableActual : actual1) {
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

        final List<Table> expected2 = List.of(table1, table2, table3);
        final List<Table> actual2 = service.searchThroughTables(List.of(tableName1, tableName2, tableName3), properties2);
        assertEquals(expected2.size(), actual2.size());
        for (Table tableExpected : expected2) {
            for (Row rowExpected : tableExpected.rows()) {
                boolean found = false;
                for (Table tableActual : actual2) {
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

    }

    @Test
    void test_searchThroughTables_throws() throws SQLException {
        final String tableName1 = "test1";
        final String tableName2 = "test2";
        final String tableName3 = "test3";
        final String tableName4 = "test4";
        final String columnName1 = "column1";
        final String columnName2 = "column2";
        final String columnName3 = "column3";
        final List<String> columnNames1 = List.of(columnName1, columnName2, columnName3);
        final Property property1 = PropertyFactory.getProperty(REGEX, Pattern.compile(""));
        final Property property2 = PropertyFactory.getProperty(GREATERDATE, LocalDate.now());
        final Property property3 = PropertyFactory.getProperty(GREATERNUMERIC, BigDecimal.valueOf(0));
        final Property property4 = PropertyFactory.getProperty(LIKE, Pattern.compile(""));
        final List<Property> properties2 = List.of(property1, property3);
        final List<Property> properties3 = List.of(property1, property2);

        when(repository.findTableColumnNamesAll(tableName1)).thenThrow(SQLException.class);
        when(repository.findTableColumnNamesAll(tableName2)).thenReturn(columnNames1);
        when(repository.findTableColumnNamesAll(tableName3)).thenReturn(columnNames1);
        when(repository.findTableColumnNamesNumeric(tableName2)).thenThrow(SQLException.class);
        when(repository.findTableColumnNamesDate(tableName3)).thenThrow(SQLException.class);
        when(repository.findTableColumnNamesAll(tableName4)).thenReturn(columnNames1);
        when(repository.findTableRowsWithProperties(tableName4, Map.of(property4, columnNames1))).thenThrow(SQLException.class);
        assertThrows(ServiceException.class, () -> service.searchThroughTables(List.of(tableName1), List.of(property1)));
        assertThrows(ServiceException.class, () -> service.searchThroughTables(List.of(tableName2), properties2));
        assertThrows(ServiceException.class, () -> service.searchThroughTables(List.of(tableName3), properties3));
        assertThrows(ServiceException.class, () -> service.searchThroughTables(List.of(tableName4), List.of(property4)));
    }

    @Test
    void test_searchThroughWholeDatabase_one_property() throws Exception {
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
        final Row row1 = new Row(List.of(column1, column2, column3));
        final Row row2 = new Row(List.of(column1, column2, column3, column4));
        final Row row3 = new Row(List.of(column1, column2, column3, column4, column5));
        final Table table1 = new Table(tableName1, List.of(row1, row1, row1));
        final Table table2 = new Table(tableName2, List.of(row2, row2, row2, row2));
        final Table table3 = new Table(tableName3, List.of(row3, row3, row3, row3, row3));
        final List<String> columnNames1 = List.of(columnName1, columnName2, columnName3);
        final List<String> columnNames2 = List.of(columnName1, columnName2, columnName3, columnName4);
        final List<String> columnNames3 = List.of(columnName1, columnName2, columnName3, columnName4, columnName5);
        final Property property = PropertyFactory.getProperty(REGEX, Pattern.compile(""));

        when(repository.findTableNames()).thenReturn(List.of(tableName1, tableName2, tableName3));
        when(repository.findTableColumnNamesAll(tableName1)).thenReturn(columnNames1);
        when(repository.findTableColumnNamesAll(tableName2)).thenReturn(columnNames2);
        when(repository.findTableColumnNamesAll(tableName3)).thenReturn(columnNames3);
        when(repository.findTableRowsWithProperties(tableName1, Map.of(property, columnNames1))).thenReturn(table1);
        when(repository.findTableRowsWithProperties(tableName2, Map.of(property, columnNames2))).thenReturn(table2);
        when(repository.findTableRowsWithProperties(tableName3, Map.of(property, columnNames3))).thenReturn(table3);


        final List<Table> expected = List.of(table1, table2, table3);
        final List<Table> actual = service.searchThroughWholeDatabase(List.of(property));
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
    }

    @Test
    void test_searchThroughWholeDatabase_multiple_properties() throws Exception {
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
        final Row row1 = new Row(List.of(column1, column2, column3));
        final Row row2 = new Row(List.of(column1, column2, column3, column4));
        final Row row3 = new Row(List.of(column1, column2, column3, column4, column5));
        final Table table1 = new Table(tableName1, List.of(row1, row1, row1));
        final Table table2 = new Table(tableName2, List.of(row2, row2, row2, row2));
        final Table table3 = new Table(tableName3, List.of(row3, row3, row3, row3, row3));
        final List<String> columnNames1 = List.of(columnName1, columnName2, columnName3);
        final List<String> columnNames2 = List.of(columnName1, columnName2, columnName3, columnName4);
        final List<String> columnNames3 = List.of(columnName1, columnName2, columnName3, columnName4, columnName5);
        final Property property1 = PropertyFactory.getProperty(REGEX, Pattern.compile(""));
        final Property property2 = PropertyFactory.getProperty(GREATERDATE, LocalDate.now());
        final Property property3 = PropertyFactory.getProperty(GREATERNUMERIC, BigDecimal.valueOf(0));
        final List<Property> properties1 = List.of(property1, property2);
        final List<Property> properties2 = List.of(property1, property3);

        when(repository.findTableNames()).thenReturn(List.of(tableName1, tableName2, tableName3));
        when(repository.findTableColumnNamesAll(tableName1)).thenReturn(columnNames1);
        when(repository.findTableColumnNamesAll(tableName2)).thenReturn(columnNames2);
        when(repository.findTableColumnNamesAll(tableName3)).thenReturn(columnNames3);
        when(repository.findTableColumnNamesDate(tableName1)).thenReturn(columnNames1);
        when(repository.findTableColumnNamesDate(tableName2)).thenReturn(columnNames2);
        when(repository.findTableColumnNamesDate(tableName3)).thenReturn(columnNames3);
        when(repository.findTableRowsWithProperties(tableName1, Map.of(property1, columnNames1, property2, columnNames1))).thenReturn(table1);
        when(repository.findTableRowsWithProperties(tableName2, Map.of(property1, columnNames2, property2, columnNames2))).thenReturn(table2);
        when(repository.findTableRowsWithProperties(tableName3, Map.of(property1, columnNames3, property2, columnNames3))).thenReturn(table3);
        when(repository.findTableColumnNamesNumeric(tableName1)).thenReturn(columnNames1);
        when(repository.findTableColumnNamesNumeric(tableName2)).thenReturn(columnNames2);
        when(repository.findTableColumnNamesNumeric(tableName3)).thenReturn(columnNames3);
        when(repository.findTableRowsWithProperties(tableName1, Map.of(property1, columnNames1, property3, columnNames1))).thenReturn(table1);
        when(repository.findTableRowsWithProperties(tableName2, Map.of(property1, columnNames2, property3, columnNames2))).thenReturn(table2);
        when(repository.findTableRowsWithProperties(tableName3, Map.of(property1, columnNames3, property3, columnNames3))).thenReturn(table3);

        final List<Table> expected1 = List.of(table1, table2, table3);
        final List<Table> actual1 = service.searchThroughWholeDatabase(properties1);
        assertEquals(expected1.size(), actual1.size());
        for (Table tableExpected : expected1) {
            for (Row rowExpected : tableExpected.rows()) {
                boolean found = false;
                for (Table tableActual : actual1) {
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

        final List<Table> expected2 = List.of(table1, table2, table3);
        final List<Table> actual2 = service.searchThroughWholeDatabase(properties2);
        assertEquals(expected2.size(), actual2.size());
        for (Table tableExpected : expected2) {
            for (Row rowExpected : tableExpected.rows()) {
                boolean found = false;
                for (Table tableActual : actual2) {
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
    }

    @Test
    void test_searchThroughWholeDatabase_throws() throws SQLException {
        final String tableName1 = "test1";
        final String tableName2 = "test2";
        final String columnName1 = "column1";
        final String columnName2 = "column2";
        final String columnName3 = "column3";
        final List<String> columnNames1 = List.of(columnName1, columnName2, columnName3);
        final Property property1 = PropertyFactory.getProperty(REGEX, Pattern.compile(""));
        final Property property2 = PropertyFactory.getProperty(GREATERDATE, LocalDate.now());
        final Property property3 = PropertyFactory.getProperty(GREATERNUMERIC, BigDecimal.valueOf(0));
        final List<Property> properties1 = List.of(property1);
        final List<Property> properties2 = List.of(property1, property3);
        final List<Property> properties3 = List.of(property1, property2);

        when(repository.findTableNames()).thenThrow(SQLException.class);
        assertThrows(ServiceException.class, () -> service.searchThroughWholeDatabase(null));

        reset(repository);
        when(repository.findTableNames()).thenReturn(List.of(tableName1, tableName2));

        when(repository.findTableColumnNamesAll(tableName1)).thenThrow(SQLException.class);
        assertThrows(ServiceException.class, () -> service.searchThroughWholeDatabase(properties1));

        when(repository.findTableColumnNamesNumeric(tableName1)).thenThrow(SQLException.class);
        assertThrows(ServiceException.class, () -> service.searchThroughWholeDatabase(properties2));

        when(repository.findTableColumnNamesDate(tableName1)).thenThrow(SQLException.class);
        assertThrows(ServiceException.class, () -> service.searchThroughWholeDatabase(properties3));

        reset(repository);
        when(repository.findTableNames()).thenReturn(List.of(tableName1));
        when(repository.findTableColumnNamesAll(tableName1)).thenReturn(columnNames1);
        when(repository.findTableRowsWithProperties(tableName1, Map.of(property1, columnNames1))).thenThrow(SQLException.class);
        assertThrows(ServiceException.class, () -> service.searchThroughWholeDatabase(properties1));
    }

    @Test
    void test_validateTableNames() throws SQLException {
        final String tableName1 = "test1";
        final String tableName2 = "test2";
        final String tableName3 = "test3";

        when(repository.findTableNames()).thenReturn(List.of(tableName1, tableName2, tableName3));
        assertDoesNotThrow(() -> service.validateTableNames(List.of(tableName1, tableName2, tableName3)));

        reset(repository);
        when(repository.findTableNames()).thenReturn(List.of(tableName1, tableName2));
        assertThrows(IllegalTableNameException.class, () -> service.validateTableNames(List.of(tableName1, tableName2, tableName3)));

        reset(repository);
        when(repository.findTableNames()).thenThrow(SQLException.class);
        assertThrows(ServiceException.class, () -> service.validateTableNames(null));
    }

    @Test
    void test_validateColumnNames() throws SQLException {
        final String tableName1 = "test1";
        final String tableName2 = "test2";
        final String tableName3 = "test3";
        final String columnName1 = "column1";
        final String columnName2 = "column2";
        final String columnName3 = "column3";

        when(repository.findTableColumnNamesAll(tableName1)).thenReturn(List.of(columnName1, columnName2));
        assertThrows(IllegalColumnNameException.class, () -> service.validateColumnNames(tableName1, List.of(columnName1, columnName2, columnName3)));
        assertThrows(IllegalColumnNameException.class, () -> service.validateColumnNames(tableName1, List.of(columnName1, columnName2, columnName3)));
        assertThrows(IllegalColumnNameException.class, () -> service.validateColumnNames(tableName1, List.of(columnName1, columnName2, columnName3)));

        when(repository.findTableColumnNamesAll(tableName2)).thenThrow(SQLException.class);
        assertThrows(ServiceException.class, () -> service.validateColumnNames(tableName2, List.of(columnName1, columnName2, columnName3)));

        when(repository.findTableColumnNamesAll(tableName3)).thenReturn(List.of(columnName1, columnName2));
        assertDoesNotThrow(() -> service.validateColumnNames(tableName3, List.of(columnName1, columnName2)));
    }
}
