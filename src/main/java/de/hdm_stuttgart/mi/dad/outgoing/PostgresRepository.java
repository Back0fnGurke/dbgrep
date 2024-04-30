package de.hdm_stuttgart.mi.dad.outgoing;

import de.hdm_stuttgart.mi.dad.core.entity.ColumnValue;
import de.hdm_stuttgart.mi.dad.core.entity.Row;
import de.hdm_stuttgart.mi.dad.core.entity.Table;
import de.hdm_stuttgart.mi.dad.ports.RepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
            }

            return new Table(tableName, result);
        }
    }

    @Override
    public List<String> findTableColumnNames(final String tableName) throws SQLException {

        log.debug("table name: {}", tableName);

        final String query = "SELECT column_name FROM information_schema.columns WHERE table_name = ?";
        final List<String> columnNames = new ArrayList<>();
        try (final PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, tableName);

            log.debug("query string: {}", statement);

            try (final ResultSet columnNamesSet = statement.executeQuery()) {

                while (columnNamesSet.next()) {
                    columnNames.add(columnNamesSet.getString(1));
                }
            }
        }

        return columnNames;
    }
}
