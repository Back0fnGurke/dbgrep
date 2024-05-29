package de.hdm_stuttgart.mi.dad.ports;

import de.hdm_stuttgart.mi.dad.core.entity.Table;
import de.hdm_stuttgart.mi.dad.core.property.Property;

import java.sql.SQLException;
import java.util.List;

/**
 * Defines methods needed for functionality of the Handler classes.
 */
public interface ServicePort {
    /**
     * Find rows in provided table columns which have values that match the provided search pattern.
     *
     * @param tableNames  the table names to search in
     * @param columnNames the column names of the tables to search in
     * @param property    the property to search for in the column values
     * @return a List of Tables. List of Rows in Table is Empty if none where found.
     * @throws SQLException if a database access error occurs
     */
    List<Table> searchColumns(final List<String> tableNames, final List<String> columnNames, final Property property) throws SQLException;

    /**
     * Find rows in provided tables that have column values which match the provided search pattern.
     *
     * @param tableNames the table names to search in
     * @param property   the property to search for in the column values
     * @return a List of Tables. List of Rows in Table is Empty if none where found.
     * @throws SQLException if a database access error occurs
     */
    List<Table> searchTables(final List<String> tableNames, final Property property) throws SQLException;

    /**
     * Find rows in tables of database whose columns have values that match the provided search pattern.
     *
     * @param property the property to search for in the column values
     * @return a List of Tables. List of Rows in Table is Empty if none where found.
     * @throws SQLException if a database access error occurs
     */
    List<Table> searchWholeDatabase(final Property property) throws SQLException;
}
