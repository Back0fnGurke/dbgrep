package de.hdm_stuttgart.mi.dad.incoming.input;

import de.hdm_stuttgart.mi.dad.core.property.Property;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Represents the search input data.
 *
 * @param propertyList  the list of properties used in the search
 * @param tables        the list of tables to search
 * @param columns       the map of table names to their respective columns
 * @param searchTables  flag indicating whether to search tables
 * @param searchColumns flag indicating whether to search columns
 */
public record SearchInput(List<Property<?>> propertyList, List<String> tables, Map<String, List<String>> columns,
                          boolean searchTables, boolean searchColumns) {

    /**
     * Constructs a new SearchInput instance with the specified properties, tables, columns, and search flags.
     * The lists and maps provided are wrapped in unmodifiable collections to ensure immutability.
     *
     * @param propertyList  the {@link List} of {@link Property} to be used in the search
     * @param tables        the {@link List} of table names to be searched
     * @param columns       the {@link Map} of column names to be searched, where the key is the table name and the value is the list of column names
     * @param searchTables  flag indicating whether to search tables
     * @param searchColumns flag indicating whether to search columns
     */
    public SearchInput(final List<Property<?>> propertyList, final List<String> tables, final Map<String, List<String>> columns, final boolean searchTables, final boolean searchColumns) {
        this.propertyList = Collections.unmodifiableList(propertyList);
        this.tables = Collections.unmodifiableList(tables);
        this.columns = Collections.unmodifiableMap(columns);
        this.searchTables = searchTables;
        this.searchColumns = searchColumns;
    }
}
