package de.hdm_stuttgart.mi.dad.core.ports;

import de.hdm_stuttgart.mi.dad.core.entity.Table;
import de.hdm_stuttgart.mi.dad.core.property.Property;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Defines database query methods needed for functionality of the Service classes.
 */
public interface RepositoryPort {

    /**
     * Finds table rows with properties.
     *
     * @param tableName       the name of the table
     * @param propertyColumns the map of properties to their respective columns
     * @return the table with the found rows
     * @throws SQLException if a database access error occurs
     */
    Table findTableRowsWithProperties(final String tableName, final LinkedHashMap<Property<?>, List<String>> propertyColumns) throws SQLException;

    /**
     * Find non system table names of tables existing in database.
     *
     * @return {@link List} containing all found table names of the database, empty if none where found
     * @throws SQLException if a database access error occurs
     */
    List<String> findTableNames() throws SQLException;

    /**
     * Find all column names of a table regardless of column type.
     *
     * @param tableName the table name to search the columns for
     * @return {@link List} containing all found column names of the table, empty if none where found
     * @throws SQLException if a database access error occurs
     */
    List<String> findTableColumnNamesAll(final String tableName) throws SQLException;

    /**
     * Find column names of a table with column type numeric.
     *
     * @param tableName the table name to search the columns for
     * @return {@link List} containing all found column names of the table, empty if none where found
     * @throws SQLException if a database access error occurs
     */
    List<String> findTableColumnNamesNumeric(final String tableName) throws SQLException;

    /**
     * Find column names of a table with a date related column type.
     *
     * @param tableName the table name to search the columns for
     * @return {@link List} containing all found column names of the table, empty if none where found
     * @throws SQLException if a database access error occurs
     */
    List<String> findTableColumnNamesDate(final String tableName) throws SQLException;
}