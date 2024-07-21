package de.hdm_stuttgart.mi.dad.outgoing;

import de.hdm_stuttgart.mi.dad.core.entity.ColumnValue;
import de.hdm_stuttgart.mi.dad.core.entity.Row;
import de.hdm_stuttgart.mi.dad.core.entity.Table;
import de.hdm_stuttgart.mi.dad.core.ports.RepositoryPort;
import de.hdm_stuttgart.mi.dad.core.property.Property;
import de.hdm_stuttgart.mi.dad.core.property.PropertyType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static de.hdm_stuttgart.mi.dad.core.property.PropertyType.*;

//TODO: doku für private methoden schreiben

/**
 * Implementation of the RepositoryPort Interface for mysql databases.
 */
class MySQLRepository implements RepositoryPort {

    private static final Logger log = LoggerFactory.getLogger(MySQLRepository.class);
    private static final EnumMap<PropertyType, String> propertyExpressions = new EnumMap<>(Map.of(
            REGEX, "CAST(%s AS CHAR) REGEXP ?",
            LIKE, "CAST(%s AS CHAR) LIKE ?",
            EQUAL, "CAST(%s AS CHAR) = ?", //cast to Decimal causes false positives
            GREATERNUMERIC, "CAST(%s AS DECIMAL) > ?",
            GREATERDATE, "CAST(%s AS DATE) > CAST(? AS DATE)",
            RANGENUMERIC, "CAST(%s AS DECIMAL) BETWEEN ? AND ?"
    ));

    private final Connection connection;
    private final QueryBuilder queryBuilder;

    public MySQLRepository(final Connection connection) {
        this.connection = connection;
        this.queryBuilder = new QueryBuilder(propertyExpressions);
    }

    private static List<String> getNames(final PreparedStatement statement) throws SQLException {
        final List<String> names = new ArrayList<>();
        try (final ResultSet namesSet = statement.executeQuery()) {
            while (namesSet.next()) {
                names.add(namesSet.getString(1));
            }
        }
        return names;
    }

    @Override
    public Table findTableRowsWithProperties(final String tableName, final Map<Property, List<String>> propertyColumns) throws SQLException {
        log.debug("tableName: {}, propertyColumns: {}", tableName, propertyColumns);

        final String query = queryBuilder.buildQueryString(tableName, propertyColumns);
        log.debug("sql query string with placeholders: {}", query);

        try (final PreparedStatement statement = connection.prepareStatement(query)) {
            int index = 1;
            for (Map.Entry<Property, List<String>> entry : propertyColumns.entrySet()) {
                Property property = entry.getKey();
                for (int i = 0; i < entry.getValue().size(); i++) {
                    if (property.getType().equals(RANGENUMERIC)) {
                        final BigDecimal[] range = (BigDecimal[]) property.getValue();
                        statement.setObject(index++, range[0]);
                        statement.setObject(index++, range[1]);
                    } else {
                        statement.setObject(index++, property.getValue());
                    }
                }
            }

            log.debug("sql query string: {}", statement);
            return getResultTable(statement, tableName);
        }
    }

    @Override
    public List<String> findTableNames() throws SQLException {
        final String query = "SELECT table_name FROM information_schema.tables WHERE table_schema NOT IN ('information_schema', 'mysql', 'performance_schema', 'sys')";

        log.debug("query: {}", query);

        try (final PreparedStatement statement = connection.prepareStatement(query)) {
            return getNames(statement);
        }
    }

    @Override
    public List<String> findTableColumnNamesAll(final String tableName) throws SQLException {
        log.debug("table name: {}", tableName);

        final String query = "SELECT column_name FROM information_schema.columns WHERE table_name = ?";
        return findTableColumnNames(query, tableName);
    }

    @Override
    public List<String> findTableColumnNamesNumeric(final String tableName) throws SQLException {
        log.debug("table name: {}", tableName);

        final String query = "SELECT column_name FROM information_schema.columns WHERE table_name = ? AND data_type IN ('tinyint', 'smallint', 'mediumint', 'int', 'bigint', 'decimal', 'bit', 'float', 'double')";
        return findTableColumnNames(query, tableName);
    }

    @Override
    public List<String> findTableColumnNamesDate(final String tableName) throws SQLException {
        log.debug("table name: {}", tableName);

        final String query = "SELECT column_name FROM information_schema.columns WHERE table_name = ? AND data_type IN ('date', 'datetime', 'timestamp')";
        return findTableColumnNames(query, tableName);
    }

    private List<String> findTableColumnNames(final String query, final String tableName) throws SQLException {
        try (final PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, tableName);
            log.debug("query string: {}", statement);
            return getNames(statement);
        }
    }

    private Table getResultTable(final PreparedStatement statement, final String tableName) throws SQLException {
        final List<Row> result = new ArrayList<>();
        try (final ResultSet tableSet = statement.executeQuery()) {
            while (tableSet.next()) {
                final List<ColumnValue> columnValues = new ArrayList<>();
                for (int i = 1; i <= tableSet.getMetaData().getColumnCount(); i++) {
                    columnValues.add(new ColumnValue(tableSet.getMetaData().getColumnLabel(i), tableSet.getString(i)));
                }
                log.debug("column count in found row: {}, columns: {}", columnValues.size(), columnValues);
                result.add(new Row(columnValues));
            }
        }
        return new Table(tableName, result);
    }
}
