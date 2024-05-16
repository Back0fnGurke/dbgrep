package de.hdm_stuttgart.mi.dad.outgoing;

import de.hdm_stuttgart.mi.dad.core.entity.ColumnValue;
import de.hdm_stuttgart.mi.dad.core.entity.Row;
import de.hdm_stuttgart.mi.dad.core.entity.Table;
import de.hdm_stuttgart.mi.dad.core.ports.RepositoryPort;
import de.hdm_stuttgart.mi.dad.core.property.Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static de.hdm_stuttgart.mi.dad.core.property.PropertyType.RANGENUMERIC;

/**
 * Implementation of the RepositoryPort Interface for postgresql databases.
 */
class PostgresRepository implements RepositoryPort {

    final Connection connection;

    final Logger log = LoggerFactory.getLogger(PostgresRepository.class);

    public PostgresRepository(final Connection connection) {
        this.connection = connection;
    }

    @Override
    public Table findTableRowsWithProperties(String tableName, List<String> columnNames, List<Property> properties) throws SQLException {
        log.debug("table name: {}, column names: {}, properties: {}", tableName, columnNames, properties);

        String query = "SELECT * FROM " + tableName + " WHERE " + getWhereClause(columnNames, properties);

        log.debug("sql query string with placeholders: {}", query);

        final int columnNamesCount = columnNames.size();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            int index = 1;
            for (int i = 0; i < columnNamesCount; i++) {
                for (Property property : properties) {
                    if (property.getType().equals(RANGENUMERIC)) {
                        BigDecimal[] range = ((BigDecimal[]) property.getValue());
                        statement.setObject(index, range[0]);
                        statement.setObject(index + 1, range[1]);
                        index += 2;
                    } else {
                        statement.setObject(index, property.getValue());
                        index++;
                    }
                }
                index++;
            }

            log.debug("sql query string: {}", statement);

            return getResultTable(statement, tableName);
        }
    }

    @Override
    public List<String> findTableColumnNamesAll(final String tableName) throws SQLException {
        log.debug("table name: {}", tableName);

        final String query = "SELECT column_name FROM information_schema.columns WHERE table_name = ?";
        try (final PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, tableName);

            log.debug("query string: {}", statement);

            return findTableColumnNames(statement);
        }
    }

    @Override
    public List<String> findTableColumnNamesNumeric(final String tableName) throws SQLException {
        log.debug("table name: {}", tableName);

        final String query = "SELECT column_name FROM information_schema.columns " +
                "WHERE table_name = ? AND data_type in " +
                "('smallint', 'integer', 'bigint', 'decimal', 'numeric', 'real'," +
                " 'double precision', 'smallserial', 'serial', 'bigserial', 'money')";
        try (final PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, tableName);

            log.debug("query string: {}", statement);

            return findTableColumnNames(statement);
        }
    }

    @Override
    public List<String> findTableColumnNamesDate(String tableName) throws SQLException {
        log.debug("table name: {}", tableName);

        final String query = "SELECT column_name FROM information_schema.columns " +
                "WHERE table_name = ? AND data_type in " +
                "('timestamp without time zone', 'timestamp with time zone', 'date')";
        try (final PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, tableName);

            log.debug("query string: {}", statement);

            return findTableColumnNames(statement);
        }
    }

    public Table getResultTable(final PreparedStatement statement, final String tableName) throws SQLException {
        final List<Row> result = new ArrayList<>();
        try (final ResultSet tableSet = statement.executeQuery()) {

            while (tableSet.next()) {

                final int columnCount = tableSet.getMetaData().getColumnCount();
                final List<ColumnValue> columnValues = new ArrayList<>(columnCount);

                for (int i = 1; i <= columnCount; i++) {
                    columnValues.add(new ColumnValue(tableSet.getMetaData().getColumnLabel(i), tableSet.getString(i)));
                }

                log.debug("column count in found row: {}, columns: {}", columnCount, columnValues);

                result.add(new Row(columnValues));
            }
        } catch (SQLException e) {
            throw new SQLException(e);
        }

        return new Table(tableName, result);
    }

    List<String> findTableColumnNames(final PreparedStatement statement) throws SQLException {
        final List<String> columnNames = new ArrayList<>();

        try (final ResultSet columnNamesSet = statement.executeQuery()) {

            while (columnNamesSet.next()) {
                columnNames.add(columnNamesSet.getString(1));
            }
        }

        return columnNames;
    }

    String getWhereClause(List<String> columnNames, List<Property> properties) {
        final int columnNamesCount = columnNames.size();
        String clause = "";
        for (int i = 0; i < columnNamesCount; i++) {
            if (i + 1 == columnNamesCount) {
                clause += getStatementForColumn(columnNames.get(i), properties) + ";";
            } else {
                clause += getStatementForColumn(columnNames.get(i), properties) + " OR ";
            }
        }

        return clause;
    }

    String getStatementForColumn(final String columnName, final List<Property> properties) {
        String statement = "((" + columnName;
        for (int j = 0; j < properties.size(); j++) {
            switch (properties.get(j).getType()) {
                case REGEX -> {
                    if (j + 1 == properties.size()) {
                        statement += "::text ~ ?))";
                    } else {
                        statement += "::text ~ ?) AND (" + columnName;
                    }
                }
                case LIKE -> {
                    if (j + 1 == properties.size()) {
                        statement += "::text LIKE ?))";
                    } else {
                        statement += "::text LIKE ?) AND (" + columnName;
                    }
                }
                case EQUAL -> {
                    if (j + 1 == properties.size()) {
                        statement += "::numeric = ?::numeric))";
                    } else {
                        statement += "::numeric = ?::numeric) AND (" + columnName;
                    }
                }
                case GREATERNUMERIC -> {
                    if (j + 1 == properties.size()) {
                        statement += "::numeric > ?::numeric))";
                    } else {
                        statement += "::numeric > ?::numeric) AND (" + columnName;
                    }
                }
                case GREATERDATE -> {
                    if (j + 1 == properties.size()) {
                        statement += "::date > ?::date))";
                    } else {
                        statement += "::date > ?::date) AND (" + columnName;
                    }
                }
                case RANGENUMERIC -> {
                    if (j + 1 == properties.size()) {
                        statement += "::numeric BETWEEN ?::numeric AND ?::numeric))";
                    } else {
                        statement += "::numeric BETWEEN ?::numeric AND ?::numeric) AND (" + columnName;
                    }
                }
                default -> throw new IllegalStateException("Unexpected value: " + properties.get(j).getType());
            }
        }
        return statement;
    }
}
