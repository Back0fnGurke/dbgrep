package de.hdm_stuttgart.mi.dad.core.ports;

import de.hdm_stuttgart.mi.dad.core.entity.Table;
import de.hdm_stuttgart.mi.dad.core.exception.ServiceException;
import de.hdm_stuttgart.mi.dad.core.property.Property;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Defines methods needed for functionality of the Handler classes.
 */
public interface ServicePort {
    /**
     * Find rows in provided table columns which have values that match the provided search pattern.
     *
     * @param columnNamesOfTables a Map consisting of table names as keys and a List of the column names of the respective table as value
     * @param properties          the properties to search for in the column values
     * @return a List of Tables. List of Rows in Table is Empty if none where found.
     * @throws SQLException if a database access error occurs
     */
    List<Table> searchThroughColumns(final Map<String, List<String>> columnNamesOfTables, final List<Property> properties) throws ServiceException;

    /**
     * Find rows in provided tables that have column values which match the provided search pattern.
     *
     * @param tableNames the table names to search in
     * @param properties the properties to search for in the column values
     * @return a List of Tables. List of Rows in Table is Empty if none where found.
     * @throws SQLException if a database access error occurs
     */
    List<Table> searchThroughTables(final List<String> tableNames, final List<Property> properties) throws ServiceException;

    /**
     * Find rows in tables of database whose columns have values that match the provided search pattern.
     *
     * @param properties the properties to search for in the column value
     * @return a List of Tables. List of Rows in Table is Empty if none where found.
     * @throws SQLException if a database access error occurs
     */
    List<Table> searchThroughWholeDatabase(final List<Property> properties) throws ServiceException;
}
