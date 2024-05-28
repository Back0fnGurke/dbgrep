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
 * Implementation of the RepositoryPort Interface for mysql databases.
 */
class MySQLRepository implements RepositoryPort {

    final Connection connection;

    final Logger log = LoggerFactory.getLogger(MySQLRepository.class);

    public MySQLRepository(final Connection connection) {
        this.connection = connection;
    }

    @Override
    public Table findTableRowsWithProperties(String tableName, List<String> columnNames, List<Property> properties) throws SQLException {
        log.debug("table name: {}, column names: {}, properties: {}", tableName, columnNames, properties);

        String query = "SELECT * FROM " + tableName + " WHERE " + getWhereClause(columnNames, properties);
        log.debug("sql query string with placeholders: {}", query);

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            int index = 1;
            for (String ignored : columnNames) {
                for (Property property : properties) {
                    if (property.getType().equals(RANGENUMERIC)) {
                        BigDecimal[] range = (BigDecimal[]) property.getValue();
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
    public List<String> findTableColumnNamesAll(final String tableName) throws SQLException {
        log.debug("table name: {}", tableName);
        String query = "SELECT column_name FROM information_schema.columns WHERE table_name = ?";
        return findTableColumnNames(query, tableName);
    }

    @Override
    public List<String> findTableColumnNamesNumeric(final String tableName) throws SQLException {
        log.debug("table name: {}", tableName);
        String query = "SELECT column_name FROM information_schema.columns WHERE table_name = ? AND data_type IN ('tinyint', 'smallint', 'mediumint', 'int', 'bigint', 'decimal', 'bit', 'float', 'double')";
        return findTableColumnNames(query, tableName);
    }

    @Override
    public List<String> findTableColumnNamesDate(final String tableName) throws SQLException {
        log.debug("table name: {}", tableName);
        String query = "SELECT column_name FROM information_schema.columns WHERE table_name = ? AND data_type IN ('date', 'datetime', 'timestamp')";
        return findTableColumnNames(query, tableName);
    }

    private Table getResultTable(final PreparedStatement statement, final String tableName) throws SQLException {
        List<Row> result = new ArrayList<>();
        try (ResultSet tableSet = statement.executeQuery()) {
            while (tableSet.next()) {
                List<ColumnValue> columnValues = new ArrayList<>();
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
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, tableName);
            log.debug("query string: {}", statement);
            return getColumnNames(statement);
        }
    }

    private List<String> getColumnNames(final PreparedStatement statement) throws SQLException {
        List<String> columnNames = new ArrayList<>();
        try (ResultSet columnNamesSet = statement.executeQuery()) {
            while (columnNamesSet.next()) {
                columnNames.add(columnNamesSet.getString(1));
            }
        }
        return columnNames;
    }

    String getWhereClause(final List<String> columnNames, final List<Property> properties) {
        StringBuilder clause = new StringBuilder();
        int columnNamesCount = columnNames.size();

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
        StringBuilder statement = new StringBuilder();
        statement.append("((CAST(").append(columnName);

        for (int j = 0; j < properties.size(); j++) {
            switch (properties.get(j).getType()) {
                case REGEX -> statement.append(" AS CHAR) REGEXP ?");
                case LIKE -> statement.append(" AS CHAR) LIKE ?");
                case EQUAL -> statement.append(" AS CHAR) = ?");
                case GREATERNUMERIC -> statement.append(" AS DECIMAL) > ?");
                case GREATERDATE -> statement.append(" AS DATE) > CAST(? AS DATE)");
                case RANGENUMERIC -> statement.append(" AS DECIMAL) BETWEEN ? AND ?");
                default -> throw new IllegalStateException("Unexpected value: " + properties.get(j).getType());
            }

            if (j + 1 < properties.size()) {
                statement.append(" AND (CAST(").append(columnName);
            } else {
                statement.append(")");
            }
        }

        statement.append(")");
        return statement.toString();
    }
}
