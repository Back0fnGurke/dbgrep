package de.hdm_stuttgart.mi.dad.ports;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
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
     * @return a List containing all rows with matching column values. Rows are represented by maps with column names as keys and their values as the value. List is Empty if none where found.
     * @throws SQLException if a database access error occurs
     */
    List<Map<String, String>> findInTable(final String tableName, final List<String> columnNames, final Pattern pattern) throws SQLException;

    /**
     * Find column names of a table.
     *
     * @param tableName the table name to search the columns for
     * @return List<String> containing all found column names of the table, empty if none where found
     * @throws SQLException if a database access error occurs
     */
    List<String> findTableColumns(final String tableName) throws SQLException;
}