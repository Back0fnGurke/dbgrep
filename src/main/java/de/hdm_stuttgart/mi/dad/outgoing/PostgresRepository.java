package de.hdm_stuttgart.mi.dad.outgoing;

import de.hdm_stuttgart.mi.dad.core.entity.ColumnValue;
import de.hdm_stuttgart.mi.dad.core.entity.Row;
import de.hdm_stuttgart.mi.dad.core.entity.Table;
import de.hdm_stuttgart.mi.dad.ports.RepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

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
    public void findInWholeDatabase() {
        // do stuff
    }

    @Override
    public void findInColumn() {
        // do stuff
    }

    @Override
    public Table findPattern(final String tableName, final List<String> columnNames, final Pattern pattern) throws SQLException {
        log.debug("table name: {}, column names: {}, search pattern: {}", tableName, columnNames, pattern);

        final int columnNamesCount = columnNames.size();
        String query = "SELECT * FROM " + tableName + " WHERE ";
        for (int i = 0; i < columnNamesCount; i++) {
            if (i + 1 == columnNamesCount) {
                query += columnNames.get(i) + "::text ~ ?;";
            } else {
                query += columnNames.get(i) + "::text ~ ? OR ";
            }
        }

        log.debug("sql query string with placeholders: {}", query);

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            for (int i = 0; i < columnNamesCount; i++) {
                statement.setString(i + 1, pattern.pattern());
            }

            log.debug("sql query string: {}", statement);

            return getResultTable(statement, tableName);
        }
    }

    @Override
    public Table findLikePattern(final String tableName, final List<String> columnNames, final Pattern pattern) throws SQLException {
        log.debug("table name: {}, column names: {}, search pattern: {}", tableName, columnNames, pattern);

        final int columnNamesCount = columnNames.size();
        String query = "SELECT * FROM " + tableName + " WHERE ";
        for (int i = 0; i < columnNamesCount; i++) {
            if (i + 1 == columnNamesCount) {
                query += columnNames.get(i) + "::text LIKE ?;";
            } else {
                query += columnNames.get(i) + "::text LIKE ? OR ";
            }
        }

        log.debug("sql query string with placeholders: {}", query);

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            for (int i = 0; i < columnNamesCount; i++) {
                statement.setString(i + 1, pattern.pattern());
            }

            log.debug("sql query string: {}", statement);

            return getResultTable(statement, tableName);
        }
    }

    @Override
    public Table findGreaterNumeric(final String tableName, final List<String> columnNames, final BigDecimal number) throws SQLException {
        log.debug("table name: {}, column names: {}, number: {}", tableName, columnNames, number);

        final int columnNamesCount = columnNames.size();
        String query = "SELECT * FROM " + tableName + " WHERE ";
        for (int i = 0; i < columnNamesCount; i++) {
            if (i + 1 == columnNamesCount) {
                query += columnNames.get(i) + "::numeric > ?::numeric;";
            } else {
                query += columnNames.get(i) + "::numeric > ?::numeric OR ";
            }
        }

        log.debug("sql query string with placeholders: {}", query);

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            for (int i = 0; i < columnNamesCount; i++) {
                statement.setBigDecimal(i + 1, number);
            }

            log.debug("sql query string: {}", statement);

            return getResultTable(statement, tableName);
        }
    }

    @Override
    public Table findGreaterDate(final String tableName, final List<String> columnNames, final LocalDate date) throws SQLException {
        log.debug("table name: {}, column names: {}, number: {}", tableName, columnNames, date);

        final int columnNamesCount = columnNames.size();
        String query = "SELECT * FROM " + tableName + " WHERE ";
        for (int i = 0; i < columnNamesCount; i++) {
            if (i + 1 == columnNamesCount) {
                query += columnNames.get(i) + "::date > ?::date;";
            } else {
                query += columnNames.get(i) + "::date > ?::date OR ";
            }
        }

        log.debug("sql query string with placeholders: {}", query);

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            for (int i = 0; i < columnNamesCount; i++) {
                statement.setObject(i + 1, date);
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
}
