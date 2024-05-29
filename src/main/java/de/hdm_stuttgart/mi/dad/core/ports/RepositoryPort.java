package de.hdm_stuttgart.mi.dad.core.ports;

import de.hdm_stuttgart.mi.dad.core.entity.Table;
import de.hdm_stuttgart.mi.dad.core.property.Property;

import java.sql.SQLException;
import java.util.List;

/**
 * Defines database query methods needed for functionality of the Service classes.
 */
public interface RepositoryPort {

    /**
     * Find rows in the provided table whose columns match the given properties.
     *
     * @param tableName   the table name to search in
     * @param columnNames column names of the table to match against the provided pattern
     * @param properties  the start of the range (included)
     * @return a Table containing all rows with matching column values. List of Rows is Empty if none where found.
     * @throws SQLException if a database access error occurs
     */
    Table findTableRowsWithProperties(final String tableName, final List<String> columnNames, final List<Property> properties) throws SQLException;

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