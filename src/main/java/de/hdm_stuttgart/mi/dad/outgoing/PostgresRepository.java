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

    private static final String ERRTABLENAMENULL = "Parameter tableName must not be null or empty.";

    final Connection connection;
    final Logger log = LoggerFactory.getLogger(PostgresRepository.class);

    public PostgresRepository(final Connection connection) {
        this.connection = connection;
    }

    @Override
    public Table findTableRowsWithProperties(final String tableName, final List<String> columnNames, final List<Property> properties) throws SQLException {
        log.debug("table name: {}, column names: {}, properties: {}", tableName, columnNames, properties);

        if (tableName == null) {
            throw new IllegalArgumentException(ERRTABLENAMENULL);
        }
        if (columnNames == null || columnNames.isEmpty()) {
            throw new IllegalArgumentException("Parameter columnNames must not be null or empty.");
        }
        if (properties == null || properties.isEmpty()) {
            throw new IllegalArgumentException("Parameter properties must not be null or empty.");
        }

        final String query = "SELECT * FROM " + tableName + " WHERE " + getWhereClause(columnNames, properties);
        log.debug("sql query string with placeholders: {}", query);

        try (final PreparedStatement statement = connection.prepareStatement(query)) {
            int index = 1;
            for (int i = 0; i < columnNames.size(); i++) {
                for (final Property property : properties) {
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
        final String query = "SELECT table_name FROM information_schema.tables WHERE table_schema NOT IN ('pg_catalog', 'information_schema')";

        log.debug("query: {}", query);

        try (final PreparedStatement statement = connection.prepareStatement(query)) {
            return getNames(statement);
        }
    }

    @Override
    public List<String> findTableColumnNamesAll(final String tableName) throws SQLException {
        log.debug("table name: {}", tableName);

        if (tableName == null || tableName.isEmpty()) {
            throw new IllegalArgumentException(ERRTABLENAMENULL);
        }

        final String query = "SELECT column_name FROM information_schema.columns WHERE table_name = ?";
        return findTableColumnNames(query, tableName);
    }

    @Override
    public List<String> findTableColumnNamesNumeric(final String tableName) throws SQLException {
        log.debug("table name: {}", tableName);

        if (tableName == null || tableName.isEmpty()) {
            throw new IllegalArgumentException(ERRTABLENAMENULL);
        }

        final String query = "SELECT column_name FROM information_schema.columns WHERE table_name = ? AND data_type IN ('smallint', 'integer', 'bigint', 'decimal', 'numeric', 'real', 'double precision', 'smallserial', 'serial', 'bigserial', 'money')";
        return findTableColumnNames(query, tableName);
    }

    @Override
    public List<String> findTableColumnNamesDate(final String tableName) throws SQLException {
        log.debug("table name: {}", tableName);

        if (tableName == null || tableName.isEmpty()) {
            throw new IllegalArgumentException(ERRTABLENAMENULL);
        }

        final String query = "SELECT column_name FROM information_schema.columns WHERE table_name = ? AND data_type IN ('timestamp without time zone', 'timestamp with time zone', 'date')";
        return findTableColumnNames(query, tableName);
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

    private List<String> findTableColumnNames(final String query, final String tableName) throws SQLException {
        try (final PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, tableName);
            log.debug("query string: {}", statement);
            return getNames(statement);
        }
    }

    private List<String> getNames(final PreparedStatement statement) throws SQLException {
        final List<String> names = new ArrayList<>();
        try (final ResultSet namesSet = statement.executeQuery()) {
            while (namesSet.next()) {
                names.add(namesSet.getString(1));
            }
        }
        return names;
    }

    String getWhereClause(final List<String> columnNames, final List<Property> properties) {
        final StringBuilder clause = new StringBuilder();
        final int columnNamesCount = columnNames.size();

        for (int i = 0; i < columnNamesCount; i++) {
            clause.append(getStatementForColumn(columnNames.get(i), properties));
            if (i + 1 < columnNamesCount) {
                clause.append(" OR ");
            } else {
                clause.append(";");
            }
        }

        return clause.toString();
    }

    String getStatementForColumn(final String columnName, final List<Property> properties) {
        final StringBuilder statement = new StringBuilder();
        statement.append("((").append(columnName);

        for (int j = 0; j < properties.size(); j++) {
            switch (properties.get(j).getType()) {
                case REGEX -> statement.append("::text ~ ?");
                case LIKE -> statement.append("::text LIKE ?");
                case EQUAL -> statement.append("::numeric = ?::numeric");
                case GREATERNUMERIC -> statement.append("::numeric > ?::numeric");
                case GREATERDATE -> statement.append("::date > ?::date");
                case RANGENUMERIC -> statement.append("::numeric BETWEEN ?::numeric AND ?::numeric");
                default -> throw new IllegalArgumentException("Unexpected value: " + properties.get(j).getType());
            }

            if (j + 1 < properties.size()) {
                statement.append(" AND (").append(columnName);
            } else {
                statement.append(")");
            }
        }

        statement.append(")");
        return statement.toString();
    }
}
