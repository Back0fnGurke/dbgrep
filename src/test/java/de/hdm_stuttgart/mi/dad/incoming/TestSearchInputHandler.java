package de.hdm_stuttgart.mi.dad.incoming;

import de.hdm_stuttgart.mi.dad.core.property.Property;
import de.hdm_stuttgart.mi.dad.core.property.PropertyType;
import de.hdm_stuttgart.mi.dad.core.property.properties.PropertyFactory;
import de.hdm_stuttgart.mi.dad.incoming.input.SearchInput;
import de.hdm_stuttgart.mi.dad.incoming.input.SearchInputHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class TestSearchInputHandler {
    private SearchInputHandler inputHandler;

    @BeforeEach
    void setUp() {
        inputHandler = new SearchInputHandler();
    }

    @Test
    void testNoPropertiesSpecified() {
        final String[] args = {};
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> inputHandler.handleInput(args));
        assertEquals("No search properties specified.", exception.getMessage());
    }

    @Test
    void testOnePropertySpecified() {
        final String[] args = {"--like", "test"};
        final SearchInput input = inputHandler.handleInput(args);
        final List<Property<?>> expectedProperties = List.of(PropertyFactory.createProperty(PropertyType.LIKE, Pattern.compile("test")));
        assertEquals(expectedProperties, input.propertyList());
    }

    @Test
    void testMultiplePropertiesSpecified() {
        final String[] args = {"--like", "test", "--regex", "regex", "--equal", "10", "--greater", "14", "--range", "14.6,18"};
        final SearchInput input = inputHandler.handleInput(args);
        final List<Property<?>> expectedProperties = List.of(
                PropertyFactory.createProperty(PropertyType.LIKE, Pattern.compile("test")),
                PropertyFactory.createProperty(PropertyType.REGEX, Pattern.compile("regex")),
                PropertyFactory.createProperty(PropertyType.EQUAL, new BigDecimal("10")),
                PropertyFactory.createProperty(PropertyType.GREATER_NUMERIC, new BigDecimal("14")),
                PropertyFactory.createProperty(PropertyType.RANGE_NUMERIC, new BigDecimal[]{new BigDecimal("14.6"), new BigDecimal("18")})
        );
        assertEquals(expectedProperties, input.propertyList());
    }

    @Test
    void testMultipleSamePropertiesSpecified() {
        final String[] args = {"--like", "test1", "--like", "test2", "--regex", "regex"};
        final SearchInput input = inputHandler.handleInput(args);
        final List<Property<?>> expectedProperties = List.of(
                PropertyFactory.createProperty(PropertyType.LIKE, Pattern.compile("test1")),
                PropertyFactory.createProperty(PropertyType.LIKE, Pattern.compile("test2")),
                PropertyFactory.createProperty(PropertyType.REGEX, Pattern.compile("regex"))
        );
        assertEquals(expectedProperties, input.propertyList());
    }

    @Test
    void testSearchWholeDatabase() {
        final String[] args = {"--like", "test"};
        final SearchInput input = inputHandler.handleInput(args);
        assertFalse(input.searchTables());
        assertFalse(input.searchColumns());
    }

    @Test
    void testSearchSpecificTables() {
        final String[] args = {"--table", "table1", "--table", "table2", "--like", "test"};
        final SearchInput input = inputHandler.handleInput(args);
        final List<String> expectedTables = List.of("table1", "table2");
        assertEquals(expectedTables, input.tables());
        assertTrue(input.searchTables());
        assertFalse(input.searchColumns());
    }

    @Test
    void testSearchSpecificColumns() {
        final String[] args = {"--column", "table1.col1", "--column", "table1.col2", "--column", "table2.col3", "--like", "test"};
        final SearchInput input = inputHandler.handleInput(args);
        final Map<String, List<String>> expectedColumns = Map.of(
                "table1", List.of("col1", "col2"),
                "table2", List.of("col3")
        );
        assertEquals(expectedColumns, input.columns());
        assertTrue(input.searchColumns());
        assertFalse(input.searchTables());
    }

    @Test
    void testSearchSpecificColumnsAndTables() {
        final String[] args = {"--column", "table1.col1", "--table", "table2", "--like", "test"};
        final SearchInput input = inputHandler.handleInput(args);
        final Map<String, List<String>> expectedColumns = Map.of("table1", List.of("col1"));
        final List<String> expectedTables = List.of("table2");
        assertEquals(expectedColumns, input.columns());
        assertEquals(expectedTables, input.tables());
        assertTrue(input.searchTables());
        assertTrue(input.searchColumns());
    }
}
