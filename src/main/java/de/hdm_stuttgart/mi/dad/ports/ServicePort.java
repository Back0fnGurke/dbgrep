package de.hdm_stuttgart.mi.dad.ports;

import de.hdm_stuttgart.mi.dad.core.entity.Table;

import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Defines methods needed for functionality of the Handler classes.
 */
public interface ServicePort {

    /**
     * Find rows in provided tables that have column values which match the provided search pattern.
     *
     * @param tableNames the table names to search in
     * @param pattern    the pattern to search for in the column values
     * @return a List of Tables. List of Rows in Table is Empty if none where found.
     * @throws SQLException if a database access error occurs
     */
    List<Table> searchTables(final List<String> tableNames, final Pattern pattern) throws SQLException;
}
