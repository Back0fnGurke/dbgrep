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
import java.util.List;
import java.util.Map;

import static de.hdm_stuttgart.mi.dad.core.property.PropertyType.RANGE_NUMERIC;

/**
 * The PostgresRepository class is an implementation of the RepositoryPort interface for PostgreSQL databases.
 * It provides methods to interact with a PostgreSQL database, such as finding table names, column names, and rows with specific properties.
 * <p>
 * The class uses a Connection object to connect to the database and a QueryBuilder object to build SQL queries.
 * <p>
 * The class has a constructor that takes a Connection object and a Map of PropertyType and String pairs, which are used to initialize the connection and queryBuilder fields, respectively.
 * <p>
 * The findTableRowsWithProperties method is used to find rows in a specified table whose columns match the given properties. It builds an SQL query using the QueryBuilder, executes the query, and returns a Table object containing the result.
 * <p>
 * The findTableNames method is used to find the names of all non-system tables in the database. It executes an SQL query and returns a List of table names.
 * <p>
 * The findTableColumnNamesAll, findTableColumnNamesNumeric, and findTableColumnNamesDate methods are used to find the names of all columns, numeric columns, and date columns, respectively, in a specified table. They execute an SQL query and return a List of column names.
 * <p>
 * The getNames method is a private helper method used to get the names from a ResultSet object. It is used by the findTableNames and findTableColumnNames methods.
 * <p>
 * The getResultTable method is a private helper method used to get a Table object from a ResultSet object. It is used by the findTableRowsWithProperties method.
 */
class PostgresRepository implements RepositoryPort {

    /**
     * Logger for this class.
     */
    private static final Logger log = LoggerFactory.getLogger(PostgresRepository.class);

    /**
     * Connection to the PostgreSQL database.
     */
    private final Connection connection;

    /**
     * QueryBuilder to build SQL queries.
     */
    private final QueryBuilder queryBuilder;

    /**
     * Constructor for the PostgresRepository class.
     *
     * @param connection          the Connection object to connect to the PostgreSQL database.
     * @param propertyExpressions the Map of PropertyType and String pairs to initialize the QueryBuilder.
     */
    public PostgresRepository(final Connection connection, final Map<PropertyType, String> propertyExpressions) {
        this.connection = connection;
        this.queryBuilder = new QueryBuilder(propertyExpressions);
    }

    @Override
    public Table findTableRowsWithProperties(final String tableName, final Map<Property<?>, List<String>> propertyColumns) throws SQLException {
        log.debug("tableName: {}, propertyColumns: {}", tableName, propertyColumns);

        final String query = queryBuilder.buildQueryString(tableName, propertyColumns);
        log.debug("sql query string with placeholders: {}", query);

        try (final PreparedStatement statement = connection.prepareStatement(query)) {
            int index = 1;
            for (Map.Entry<Property<?>, List<String>> entry : propertyColumns.entrySet()) {
                Property<?> property = entry.getKey();
                for (int i = 0; i < entry.getValue().size(); i++) {
                    if (property.getType().equals(RANGE_NUMERIC)) {
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

        final String query = "SELECT column_name FROM information_schema.columns WHERE table_name = ?";
        return findTableColumnNames(query, tableName);
    }

    @Override
    public List<String> findTableColumnNamesNumeric(final String tableName) throws SQLException {
        log.debug("table name: {}", tableName);

        final String query = "SELECT column_name FROM information_schema.columns WHERE table_name = ? AND data_type IN ('smallint', 'integer', 'bigint', 'decimal', 'numeric', 'real', 'double precision', 'smallserial', 'serial', 'bigserial', 'money')";
        return findTableColumnNames(query, tableName);
    }

    @Override
    public List<String> findTableColumnNamesDate(final String tableName) throws SQLException {
        log.debug("table name: {}", tableName);

        final String query = "SELECT column_name FROM information_schema.columns WHERE table_name = ? AND data_type IN ('timestamp without time zone', 'timestamp with time zone', 'date')";
        return findTableColumnNames(query, tableName);
    }

    /**
     * Private helper method to get the names from a ResultSet object.
     *
     * @param statement the PreparedStatement object to execute the SQL query.
     * @return a List of names from the ResultSet object.
     * @throws SQLException if a database access error occurs.
     */
    private static List<String> getNames(final PreparedStatement statement) throws SQLException {
        final List<String> names = new ArrayList<>();
        try (final ResultSet namesSet = statement.executeQuery()) {
            while (namesSet.next()) {
                names.add(namesSet.getString(1));
            }
        }
        return names;
    }

    /**
     * Private helper method to find the column names of a specified table based on the provided SQL query.
     *
     * @param query     the SQL query to execute.
     * @param tableName the name of the table to search the columns for.
     * @return a List of column names in the specified table based on the provided SQL query.
     * @throws SQLException if a database access error occurs.
     */
    private List<String> findTableColumnNames(final String query, final String tableName) throws SQLException {
        try (final PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, tableName);
            log.debug("query string: {}", statement);
            return getNames(statement);
        }
    }

    /**
     * Private helper method to get a Table object from a ResultSet object.
     *
     * @param statement the PreparedStatement object to execute the SQL query.
     * @param tableName the name of the table to get the Table object from.
     * @return a Table object from the ResultSet object.
     * @throws SQLException if a database access error occurs.
     */
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
