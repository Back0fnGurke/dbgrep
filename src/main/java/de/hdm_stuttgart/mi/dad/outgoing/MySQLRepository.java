package de.hdm_stuttgart.mi.dad.outgoing;

import de.hdm_stuttgart.mi.dad.core.entity.ColumnValue;
import de.hdm_stuttgart.mi.dad.core.entity.Row;
import de.hdm_stuttgart.mi.dad.core.entity.Table;
import de.hdm_stuttgart.mi.dad.core.ports.RepositoryPort;
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
 * Implementation of the RepositoryPort Interface for mysql databases.
 */
class MySQLRepository implements RepositoryPort {

    final Connection connection;

    final Logger log = LoggerFactory.getLogger(MySQLRepository.class);

    public MySQLRepository(final Connection connection) {
        this.connection = connection;
    }

    @Override
    public Table findPattern(final String tableName, final List<String> columnNames, final Pattern pattern) throws SQLException {
        log.debug("table name: {}, column names: {}, search pattern: {}", tableName, columnNames, pattern);

        final int columnNamesCount = columnNames.size();
        String query = "SELECT * FROM " + tableName + " WHERE ";
        for (int i = 0; i < columnNamesCount; i++) {
            if (i + 1 == columnNamesCount) {
                query += "CAST(" + columnNames.get(i) + " AS CHAR) REGEXP ?;";
            } else {
                query += "CAST(" + columnNames.get(i) + " AS CHAR) REGEXP ? OR ";
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
    public Table findLikePattern(String tableName, List<String> columnNames, Pattern pattern) throws SQLException {
        log.debug("table name: {}, column names: {}, search pattern: {}", tableName, columnNames, pattern);

        final int columnNamesCount = columnNames.size();
        String query = "SELECT * FROM " + tableName + " WHERE ";
        for (int i = 0; i < columnNamesCount; i++) {
            if (i + 1 == columnNamesCount) {
                query += "CAST(" + columnNames.get(i) + " AS CHAR) LIKE ?;";
            } else {
                query += "CAST(" + columnNames.get(i) + " AS CHAR) LIKE ? OR ";
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
    public Table findEqual(String tableName, List<String> columnNames, final double number) throws SQLException {
        log.debug("table name: {}, column names: {}, search pattern: {}", tableName, columnNames, number);

        final int columnNamesCount = columnNames.size();
        String query = "SELECT * FROM " + tableName + " WHERE ";
        for (int i = 0; i < columnNamesCount; i++) {
            if (i + 1 == columnNamesCount) {
                query += "CAST(" + columnNames.get(i) + " AS CHAR) = ?;";
            } else {
                query += "CAST(" + columnNames.get(i) + " AS CHAR) = ? OR ";
            }
        }

        log.debug("sql query string with placeholders: {}", query);

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            for (int i = 0; i < columnNamesCount; i++) {
                statement.setDouble(i + 1, number);
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
                query += columnNames.get(i) + " > ?;";
            } else {
                query += columnNames.get(i) + " > ? OR ";
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
                query += "CAST(" + columnNames.get(i) + " AS DATE) > CAST( ? AS DATE);";
            } else {
                query += "CAST(" + columnNames.get(i) + " AS DATE) > CAST( ? AS DATE) OR ";
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
    public Table findInRangeNumeric(String tableName, List<String> columnNames, BigDecimal from, BigDecimal to) throws SQLException {
        log.debug("table name: {}, column names: {}, from: {}, to: {}", tableName, columnNames, from, to);

        final int columnNamesCount = columnNames.size();
        String query = "SELECT * FROM " + tableName + " WHERE ";
        for (int i = 0; i < columnNamesCount; i++) {
            if (i + 1 == columnNamesCount) {
                query += columnNames.get(i) + " BETWEEN ? AND ?;";
            } else {
                query += columnNames.get(i) + " BETWEEN ? AND ? OR ";
            }
        }

        log.debug("sql query string with placeholders: {}", query);

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            for (int i = 0; i < columnNamesCount * 2; i += 2) {
                statement.setBigDecimal(i + 1, from);
                statement.setBigDecimal(i + 2, to);
            }

            log.debug("sql query string: {}", statement);

            return getResultTable(statement, tableName);
        }
    }

    @Override
    public List<String> findTableColumnNamesAll(final String tableName) throws SQLException {
        log.debug("table name: {}", tableName);

        final String query = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = ?";
        try (final PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, tableName);

            log.debug("query string: {}", statement);

            return findTableColumnNames(statement);
        }
    }

    @Override
    public List<String> findTableColumnNamesNumeric(String tableName) throws SQLException {
        log.debug("table name: {}", tableName);

        final String query = "SELECT column_name FROM information_schema.columns " +
                "WHERE table_name = ? AND data_type in " +
                "('tinyint', 'smallint', 'mediumint', 'int', 'bigint', " +
                "'decimal', 'bit', 'float', 'double')";
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
                "('date', 'datetime', 'timestamp')";
        try (final PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, tableName);

            log.debug("query string: {}", statement);

            return findTableColumnNames(statement);
        }
    }

    public Table getResultTable(PreparedStatement statement, final String tableName) throws SQLException {
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
