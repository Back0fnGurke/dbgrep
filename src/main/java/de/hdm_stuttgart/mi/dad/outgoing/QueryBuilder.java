package de.hdm_stuttgart.mi.dad.outgoing;

import de.hdm_stuttgart.mi.dad.core.property.Property;
import de.hdm_stuttgart.mi.dad.core.property.PropertyType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * This class is responsible for building SQL query strings based on provided properties and their corresponding columns.
 * It uses a map of property expressions to generate the WHERE clause of the SQL query.
 * <p>
 * The class is not meant to be instantiated multiple times for a single query. Instead, create a single instance and use it to build your query.
 * <p>
 * Example usage:
 * <p>
 * QueryBuilder builder = new QueryBuilder(propertyExpressions);
 * String query = builder.buildQueryString("tableName", propertyColumns);
 */
public final class QueryBuilder {

    /**
     * Logger for this class.
     */
    private static final Logger log = LoggerFactory.getLogger(QueryBuilder.class);

    /**
     * Map of property expressions used to generate the WHERE clause of the SQL query.
     */
    private final Map<PropertyType, String> propertyExpressions;


    /**
     * Constructor for QueryBuilder.
     *
     * @param propertyExpressions a map of property expressions used to generate the WHERE clause of the SQL query.
     */
    public QueryBuilder(final Map<PropertyType, String> propertyExpressions) {
        this.propertyExpressions = Map.copyOf(propertyExpressions);
    }

    /**
     * Builds the SQL query string based on the provided table name and property columns.
     *
     * @param tableName       the name of the table to query.
     * @param propertyColumns a map of properties and their corresponding columns.
     * @return the SQL query string.
     */
    public String buildQueryString(final String tableName, final Map<Property, List<String>> propertyColumns) {
        final Map<Property, List<String>> copyPropertyColumns = Map.copyOf(propertyColumns);
        log.debug("tableName: {}, propertyColumns: {}", tableName, copyPropertyColumns);

        final StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM ").append(tableName).append(" WHERE ").append(getWhereClause(copyPropertyColumns));
        log.debug("sql query string with placeholders: {}", query);

        return query.toString();
    }

    /**
     * Generates the WHERE clause of the SQL query based on the provided property columns.
     *
     * @param propertyColumns a map of properties and their corresponding columns.
     * @return the WHERE clause of the SQL query.
     */
    private String getWhereClause(final Map<Property, List<String>> propertyColumns) {
        final StringBuilder clause = new StringBuilder();
        final int propertyColumnsCount = propertyColumns.size();

        int index = 0;
        for (Map.Entry<Property, List<String>> entry : propertyColumns.entrySet()) {
            clause.append(getStatementForColumn(entry.getValue(), entry.getKey()));
            if (index + 1 < propertyColumnsCount) {
                clause.append(" AND ");
            } else {
                clause.append(";");
            }
            index++;
        }

        return clause.toString();
    }

    /**
     * Generates the SQL statement for a specific column based on the provided property.
     *
     * @param columnNames the names of the columns.
     * @param property    the property to use when generating the SQL statement.
     * @return the SQL statement for the column.
     */
    private String getStatementForColumn(final List<String> columnNames, final Property property) {
        final StringBuilder statement = new StringBuilder();
        statement.append("(");

        for (int i = 0; i < columnNames.size(); i++) {
            statement.append(String.format(propertyExpressions.get(property.getType()), columnNames.get(i)));

            if (i + 1 < columnNames.size()) {
                statement.append(" OR ");
            }
        }

        statement.append(")");
        return statement.toString();
    }
}
