package de.hdm_stuttgart.mi.dad.ports;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
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
     * @return a map with table names as keys and a List containing found table rows as values. Rows are represented by maps with column names as keys and their values as the value. List is Empty if none where found.
     * @throws SQLException if a database access error occurs
     */
    Map<String, List<Map<String, String>>> searchTable(final List<String> tableNames, final Pattern pattern) throws SQLException;
}
