package de.hdm_stuttgart.mi.dad.core.ports;

import de.hdm_stuttgart.mi.dad.core.entity.Table;
import de.hdm_stuttgart.mi.dad.core.exception.ServiceException;
import de.hdm_stuttgart.mi.dad.core.property.Property;

import java.util.List;
import java.util.Map;

/**
 * Defines methods needed for functionality of the Handler classes.
 */
public interface ServicePort {
    /**
     * Find rows in provided table columns which have values that match the provided search pattern. Use validateTableNames and validateColumnNames to prevent sql injection and cast errors in database.
     *
     * @param columnNamesOfTables a Map consisting of table names as keys and a List of the column names of the respective table as value
     * @param properties          the properties to search for in the column values
     * @return a List of Tables with non-empty rows.
     * @throws ServiceException if Repository throws SQLException
     */
    List<Table> searchThroughColumns(final Map<String, List<String>> columnNamesOfTables, final List<Property> properties) throws ServiceException;

    /**
     * Find rows in provided tables that have column values which match the provided search pattern. Use validateTableNames to prevent sql injection and cast errors in database.
     *
     * @param tableNames the table names to search in
     * @param properties the properties to search for in the column values
     * @return a List of Tables with non-empty rows.
     * @throws ServiceException if Repository throws SQLException
     */
    List<Table> searchThroughTables(final List<String> tableNames, final List<Property> properties) throws ServiceException;

    /**
     * Find rows in non system tables of database whose columns have values that match the provided search pattern.
     *
     * @param properties the properties to search for in the column value
     * @return a List of Tables with non-empty rows.
     * @throws ServiceException if Repository throws SQLException
     */
    List<Table> searchThroughWholeDatabase(final List<Property> properties) throws ServiceException;

    /**
     * Checks whether the provided table names exist in the database or not.
     *
     * @param tableNames the table names to validate
     * @throws ServiceException either gets thrown if one of the provided tableNames is not present in database or if a general database error occurs. Underlying Exception: IllegalTableNameException or SQLException.
     */
    void validateTableNames(final List<String> tableNames) throws ServiceException;

    /**
     * Checks whether the provided column names are part of the provided table.
     *
     * @param tableName   the table the columns belong to. Is not being checked. Use validateTableNames beforehand to prevent sql injections.
     * @param columnNames the column names to validate
     * @throws ServiceException either gets thrown if one of the provided columnNames is not present in database table, if the type of one of the columns does not match one of the supported property types or if a general database error occurs. Underlying Exception: IllegalColumnNameException or SQLException.
     */
    void validateColumnNames(final String tableName, final List<String> columnNames) throws ServiceException;
}
