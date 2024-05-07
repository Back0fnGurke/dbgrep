package de.hdm_stuttgart.mi.dad.ports;

import de.hdm_stuttgart.mi.dad.core.entity.Table;

import java.sql.PreparedStatement;
import java.sql.SQLException;
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

    Table getResultTable(PreparedStatement statement, String tableName);

    /**
     * Find column names of a table.
     *
     * @param tableName the table name to search the columns for
     * @return List<String> containing all found column names of the table, empty if none where found
     * @throws SQLException if a database access error occurs
     */
    List<String> findTableColumnNames(final String tableName) throws SQLException;
}