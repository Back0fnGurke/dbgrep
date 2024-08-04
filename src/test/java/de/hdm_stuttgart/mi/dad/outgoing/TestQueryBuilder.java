package de.hdm_stuttgart.mi.dad.outgoing;

import de.hdm_stuttgart.mi.dad.core.property.Property;
import de.hdm_stuttgart.mi.dad.core.property.PropertyType;
import de.hdm_stuttgart.mi.dad.core.property.properties.PropertyFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Pattern;

import static de.hdm_stuttgart.mi.dad.core.property.PropertyType.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TestQueryBuilder {

    private QueryBuilder queryBuilderPostgres;
    private QueryBuilder queryBuilderMySQL;

    @BeforeEach
    void setUp() {
        final EnumMap<PropertyType, String> propertyExpressionsPostgres = new EnumMap<>(Map.of(
                REGEX, "%s::text ~ ?",
                LIKE, "%s::text LIKE ?",
                EQUAL, "%s::numeric = ?::numeric",
                GREATER_NUMERIC, "%s::numeric > ?::numeric",
                GREATER_DATE, "%s::date > ?::date",
                RANGE_NUMERIC, "%s::numeric BETWEEN ?::numeric AND ?::numeric"
        ));
        final EnumMap<PropertyType, String> propertyExpressionsMySQL = new EnumMap<>(Map.of(
                REGEX, "CAST(%s AS CHAR) REGEXP ?",
                LIKE, "CAST(%s AS CHAR) LIKE ?",
                EQUAL, "CAST(%s AS CHAR) = ?", //cast to Decimal causes false positives
                GREATER_NUMERIC, "CAST(%s AS DECIMAL) > ?",
                GREATER_DATE, "CAST(%s AS DATE) > CAST(? AS DATE)",
                RANGE_NUMERIC, "CAST(%s AS DECIMAL) BETWEEN ? AND ?"
        ));
        queryBuilderPostgres = new QueryBuilder(propertyExpressionsPostgres);
        queryBuilderMySQL = new QueryBuilder(propertyExpressionsMySQL);
    }

    @Test
    void test_buildQueryString_Postgres_REGEX() {
        final Map<Property<?>, List<String>> propertyColumns = new LinkedHashMap<>();
        final Property<?> property = PropertyFactory.createProperty(PropertyType.REGEX, Pattern.compile("test"));
        propertyColumns.put(property, Arrays.asList("column1", "column2"));

        final String expectedQuery = "SELECT * FROM tableName WHERE (column1::text ~ ? OR column2::text ~ ?);";
        final String actualQuery = queryBuilderPostgres.buildQueryString("tableName", propertyColumns);
        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    void test_buildQueryString_Postgres_LIKE() {
        final Map<Property<?>, List<String>> propertyColumns = new LinkedHashMap<>();
        final Property<?> property = PropertyFactory.createProperty(LIKE, Pattern.compile("test"));
        propertyColumns.put(property, Arrays.asList("column1", "column2"));

        final String expectedQuery = "SELECT * FROM tableName WHERE (column1::text LIKE ? OR column2::text LIKE ?);";
        final String actualQuery = queryBuilderPostgres.buildQueryString("tableName", propertyColumns);
        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    void test_buildQueryString_Postgres_EQUAL() {
        final Map<Property<?>, List<String>> propertyColumns = new LinkedHashMap<>();
        final Property<?> property = PropertyFactory.createProperty(EQUAL, BigDecimal.valueOf(1));
        propertyColumns.put(property, Arrays.asList("column1", "column2"));

        final String expectedQuery = "SELECT * FROM tableName WHERE (column1::numeric = ?::numeric OR column2::numeric = ?::numeric);";
        final String actualQuery = queryBuilderPostgres.buildQueryString("tableName", propertyColumns);
        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    void test_buildQueryString_Postgres_GREATER_NUMERIC() {
        final Map<Property<?>, List<String>> propertyColumns = new LinkedHashMap<>();
        final Property<?> property = PropertyFactory.createProperty(GREATER_NUMERIC, BigDecimal.valueOf(1));
        propertyColumns.put(property, Arrays.asList("column1", "column2"));

        final String expectedQuery = "SELECT * FROM tableName WHERE (column1::numeric > ?::numeric OR column2::numeric > ?::numeric);";
        final String actualQuery = queryBuilderPostgres.buildQueryString("tableName", propertyColumns);
        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    void test_buildQueryString_Postgres_GREATER_DATE() {
        final Map<Property<?>, List<String>> propertyColumns = new LinkedHashMap<>();
        final Property<?> property = PropertyFactory.createProperty(GREATER_DATE, LocalDate.now());
        propertyColumns.put(property, Arrays.asList("column1", "column2"));

        final String expectedQuery = "SELECT * FROM tableName WHERE (column1::date > ?::date OR column2::date > ?::date);";
        final String actualQuery = queryBuilderPostgres.buildQueryString("tableName", propertyColumns);
        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    void test_buildQueryString_Postgres_RANGE_NUMERIC() {
        final Map<Property<?>, List<String>> propertyColumns = new LinkedHashMap<>();
        final Property<?> property = PropertyFactory.createProperty(RANGE_NUMERIC, new BigDecimal[]{BigDecimal.valueOf(1), BigDecimal.valueOf(1)});
        propertyColumns.put(property, Arrays.asList("column1", "column2"));

        final String expectedQuery = "SELECT * FROM tableName WHERE (column1::numeric BETWEEN ?::numeric AND ?::numeric OR column2::numeric BETWEEN ?::numeric AND ?::numeric);";
        final String actualQuery = queryBuilderPostgres.buildQueryString("tableName", propertyColumns);
        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    void test_buildQueryString_Postgres_combination() {
        final Map<Property<?>, List<String>> propertyColumns = new LinkedHashMap<>();
        final Property<?> property1 = PropertyFactory.createProperty(RANGE_NUMERIC, new BigDecimal[]{BigDecimal.valueOf(1), BigDecimal.valueOf(1)});
        final Property<?> property2 = PropertyFactory.createProperty(EQUAL, BigDecimal.valueOf(1));
        propertyColumns.put(property1, Arrays.asList("column1", "column2"));
        propertyColumns.put(property2, Arrays.asList("column1", "column2"));

        final String expectedQuery = "SELECT * FROM tableName WHERE (column1::numeric BETWEEN ?::numeric AND ?::numeric OR column2::numeric BETWEEN ?::numeric AND ?::numeric) AND (column1::numeric = ?::numeric OR column2::numeric = ?::numeric);";
        final String actualQuery = queryBuilderPostgres.buildQueryString("tableName", propertyColumns);
        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    void test_buildQueryString_MySQL_REGEX() {
        final Map<Property<?>, List<String>> propertyColumns = new LinkedHashMap<>();
        final Property<?> property = PropertyFactory.createProperty(PropertyType.REGEX, Pattern.compile("test"));
        propertyColumns.put(property, Arrays.asList("column1", "column2"));

        final String expectedQuery = "SELECT * FROM tableName WHERE (CAST(column1 AS CHAR) REGEXP ? OR CAST(column2 AS CHAR) REGEXP ?);";
        final String actualQuery = queryBuilderMySQL.buildQueryString("tableName", propertyColumns);

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    void test_buildQueryString_MySQL_LIKE() {
        final Map<Property<?>, List<String>> propertyColumns = new LinkedHashMap<>();
        final Property<?> property = PropertyFactory.createProperty(LIKE, Pattern.compile("test"));
        propertyColumns.put(property, Arrays.asList("column1", "column2"));

        final String expectedQuery = "SELECT * FROM tableName WHERE (CAST(column1 AS CHAR) LIKE ? OR CAST(column2 AS CHAR) LIKE ?);";
        final String actualQuery = queryBuilderMySQL.buildQueryString("tableName", propertyColumns);
        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    void test_buildQueryString_MySQL_EQUAL() {
        final Map<Property<?>, List<String>> propertyColumns = new LinkedHashMap<>();
        final Property<?> property = PropertyFactory.createProperty(EQUAL, BigDecimal.valueOf(1));
        propertyColumns.put(property, Arrays.asList("column1", "column2"));

        final String expectedQuery = "SELECT * FROM tableName WHERE (CAST(column1 AS CHAR) = ? OR CAST(column2 AS CHAR) = ?);";
        final String actualQuery = queryBuilderMySQL.buildQueryString("tableName", propertyColumns);
        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    void test_buildQueryString_MySQL_GREATER_NUMERIC() {
        final Map<Property<?>, List<String>> propertyColumns = new LinkedHashMap<>();
        final Property<?> property = PropertyFactory.createProperty(GREATER_NUMERIC, BigDecimal.valueOf(1));
        propertyColumns.put(property, Arrays.asList("column1", "column2"));

        final String expectedQuery = "SELECT * FROM tableName WHERE (CAST(column1 AS DECIMAL) > ? OR CAST(column2 AS DECIMAL) > ?);";
        final String actualQuery = queryBuilderMySQL.buildQueryString("tableName", propertyColumns);
        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    void test_buildQueryString_MySQL_GREATER_DATE() {
        final Map<Property<?>, List<String>> propertyColumns = new LinkedHashMap<>();
        final Property<?> property = PropertyFactory.createProperty(GREATER_DATE, LocalDate.now());
        propertyColumns.put(property, Arrays.asList("column1", "column2"));

        final String expectedQuery = "SELECT * FROM tableName WHERE (CAST(column1 AS DATE) > CAST(? AS DATE) OR CAST(column2 AS DATE) > CAST(? AS DATE));";
        final String actualQuery = queryBuilderMySQL.buildQueryString("tableName", propertyColumns);
        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    void test_buildQueryString_MySQL_RANGE_NUMERIC() {
        final Map<Property<?>, List<String>> propertyColumns = new LinkedHashMap<>();
        final Property<?> property = PropertyFactory.createProperty(RANGE_NUMERIC, new BigDecimal[]{BigDecimal.valueOf(1), BigDecimal.valueOf(1)});
        propertyColumns.put(property, Arrays.asList("column1", "column2"));

        final String expectedQuery = "SELECT * FROM tableName WHERE (CAST(column1 AS DECIMAL) BETWEEN ? AND ? OR CAST(column2 AS DECIMAL) BETWEEN ? AND ?);";
        final String actualQuery = queryBuilderMySQL.buildQueryString("tableName", propertyColumns);
        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    void test_buildQueryString_MySQL_combination() {
        final Map<Property<?>, List<String>> propertyColumns = new LinkedHashMap<>();
        final Property<?> property1 = PropertyFactory.createProperty(RANGE_NUMERIC, new BigDecimal[]{BigDecimal.valueOf(1), BigDecimal.valueOf(1)});
        final Property<?> property2 = PropertyFactory.createProperty(EQUAL, BigDecimal.valueOf(1));
        propertyColumns.put(property1, Arrays.asList("column1", "column2"));
        propertyColumns.put(property2, Arrays.asList("column1", "column2"));

        final String expectedQuery = "SELECT * FROM tableName WHERE (CAST(column1 AS DECIMAL) BETWEEN ? AND ? OR CAST(column2 AS DECIMAL) BETWEEN ? AND ?) AND (CAST(column1 AS CHAR) = ? OR CAST(column2 AS CHAR) = ?);";
        final String actualQuery = queryBuilderMySQL.buildQueryString("tableName", propertyColumns);
        assertEquals(expectedQuery, actualQuery);
    }
}
