package de.hdm_stuttgart.mi.dad.ports;

import de.hdm_stuttgart.mi.dad.core.entity.Table;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Defines database query methods needed for functionality of the Service classes.
 */
public interface RepositoryPort {
    void findInWholeDatabase();

    void findInColumn();

    /**
     * Find rows in provided table that have column values which match the provided search pattern.
     *
     * @param tableName   the table name to search in
     * @param columnNames column names of the table to match against the provided pattern
     * @param pattern     the pattern to match the columns against
     * @return a Table containing all rows with matching column values. List of Rows is Empty if none where found.
     * @throws SQLException if a database access error occurs
     */
    Table findPattern(final String tableName, final List<String> columnNames, final Pattern pattern) throws SQLException;

    /**
     * Find rows in provided table that have column values which match the provided LIKE pattern.
     *
     * @param tableName   the table name to search in
     * @param columnNames column names of the table to match against the provided pattern
     * @param pattern     the pattern to match the columns against
     * @return a Table containing all rows with matching column values. List of Rows is Empty if none where found.
     * @throws SQLException if a database access error occurs
     */
    Table findLikePattern(final String tableName, final List<String> columnNames, final Pattern pattern) throws SQLException;

    /**
     * Find rows in provided table that have numeric column values which are greater than the provided numeric value.
     *
     * @param tableName   the table name to search in
     * @param columnNames column names of the table to match against the provided pattern
     * @param number      the number to match the columns against
     * @return a Table containing all rows with matching column values. List of Rows is Empty if none where found.
     * @throws SQLException if a database access error occurs
     */
    Table findGreaterNumeric(final String tableName, final List<String> columnNames, final BigDecimal number) throws SQLException;

    /**
     * Find rows in provided table that have date column values which are greater than the provided date value.
     *
     * @param tableName   the table name to search in
     * @param columnNames column names of the table to match against the provided pattern
     * @param date        the date to match the columns against
     * @return a Table containing all rows with matching column values. List of Rows is Empty if none where found.
     * @throws SQLException if a database access error occurs
     */
    Table findGreaterDate(final String tableName, final List<String> columnNames, final LocalDate date) throws SQLException;

    /**
     * Find all column names of a table regardless of column type.
     *
     * @param tableName the table name to search the columns for
     * @return List<String> containing all found column names of the table, empty if none where found
     * @throws SQLException if a database access error occurs
     */
    List<String> findTableColumnNamesAll(final String tableName) throws SQLException;

    /**
     * Find column names of a table with column type numeric.
     *
     * @param tableName the table name to search the columns for
     * @return List<String> containing all found column names of the table, empty if none where found
     * @throws SQLException if a database access error occurs
     */
    List<String> findTableColumnNamesNumeric(final String tableName) throws SQLException;

    /**
     * Find column names of a table with a date related column type.
     *
     * @param tableName the table name to search the columns for
     * @return List<String> containing all found column names of the table, empty if none where found
     * @throws SQLException if a database access error occurs
     */
    List<String> findTableColumnNamesDate(final String tableName) throws SQLException;
}