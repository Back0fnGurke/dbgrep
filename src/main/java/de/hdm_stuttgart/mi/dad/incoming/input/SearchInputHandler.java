package de.hdm_stuttgart.mi.dad.incoming.input;

import de.hdm_stuttgart.mi.dad.core.property.Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Handles the processing of command-line arguments to create a {@link SearchInput} object.
 * This class is responsible for parsing the arguments, validating them, and constructing
 * the necessary properties, tables, and columns for the search operation.
 */
public class SearchInputHandler {

    private static final Logger log = LoggerFactory.getLogger(SearchInputHandler.class);

    /**
     * Processes the given command-line arguments and constructs a {@link SearchInput} object.
     * If the help argument is present, prints the manual and returns null.
     *
     * @param args the command-line arguments
     * @return a {@link SearchInput} object containing the parsed search parameters
     */
    public SearchInput handleInput(final String[] args) {
        log.debug("Called with args: {}", (Object) args);

        final List<Property<?>> propertyList = createPropertyList(args);
        if (propertyList.isEmpty()) {
            throw new IllegalArgumentException("No search properties specified.");
        }

        final List<String> tableNames = findAllValuesOfArgument(args, ArgumentType.TABLE);
        final Map<String, List<String>> columnsByTable = createColumnsByTable(findAllValuesOfArgument(args, ArgumentType.COLUMN));

        final SearchInput searchInput = new SearchInput(propertyList, tableNames, columnsByTable, !tableNames.isEmpty(), !columnsByTable.isEmpty());
        log.debug("Returning: {}", searchInput);
        return searchInput;
    }

    /**
     * Creates a {@link List} of {@link Property} from the given command-line arguments.
     *
     * @param args the command-line arguments
     * @return a {@link List} of {@link Property}
     */
    private List<Property<?>> createPropertyList(final String[] args) {
        log.debug("Called with args: {}", (Object) args);
        final List<Property<?>> propertyList = new ArrayList<>();

        for (int i = 0; i < args.length; i++) {
            createPropertyIfArgumentIsProperty(args, i, propertyList);
        }
        log.debug("Returning: {}", propertyList);
        return propertyList;
    }

    /**
     * Adds a {@link Property} to the list if the argument at the specified index is a property argument.
     *
     * @param args         the command-line arguments
     * @param index        the index of the current argument
     * @param propertyList the {@link List} of {@link Property} to add to
     */
    private void createPropertyIfArgumentIsProperty(final String[] args, final int index, final List<Property<?>> propertyList) {
        log.debug("Called with args: {}, index: {}, propertyList: {}", args, index, propertyList);
        final String argument = args[index];

        for (final ArgumentType argumentType : ArgumentType.values()) {
            if (argumentType.argumentString.equals(argument) && argumentType.isProperty && (index + 1) < args.length) {
                propertyList.add(ArgumentType.createPropertyFromArgumentType(argumentType, args[index + 1]));
            }
        }
        log.debug("Updated propertyList: {}", propertyList);
    }

    /**
     * Finds all values associated with the specified {@link ArgumentType} in the command-line arguments.
     *
     * @param args     the command-line arguments
     * @param argument the {@link ArgumentType} to search for
     * @return a {@link List} of values associated with the specified {@link ArgumentType}
     */
    private List<String> findAllValuesOfArgument(final String[] args, final ArgumentType argument) {
        log.debug("Called with args: {}, argument: {}", args, argument);
        final List<String> values = new ArrayList<>();
        for (int i = 0; i < args.length - 1; i++) {
            if (args[i].equals(argument.toString())) {
                i++;
                values.add(args[i]);
            }
        }
        log.debug("Returning: {}", values);
        return values;
    }

    /**
     * Creates a {@link Map} of columns by table from the given list of column values.
     *
     * @param columnNames the {@link List} of column names
     * @return a {@link Map} where the key is the table name and the value is a {@link List} of column names
     * @throws IllegalArgumentException if the column values are not in the correct format
     */
    private Map<String, List<String>> createColumnsByTable(final List<String> columnNames) {
        log.debug("Called with columnNames: {}", columnNames);
        final Map<String, List<String>> columnsByTable = new HashMap<>();
        for (final String columnValue : columnNames) {
            final String[] tableAndColumn = columnValue.split("\\.");
            if (tableAndColumn.length != 2) {
                throw new IllegalArgumentException(columnValue + " is not a valid " + ArgumentType.COLUMN.argumentString + " argument.");
            }
            if (columnsByTable.containsKey(tableAndColumn[0])) {
                columnsByTable.get(tableAndColumn[0]).add(tableAndColumn[1]);
            } else {
                final List<String> columns = new ArrayList<>();
                columns.add(tableAndColumn[1]);
                columnsByTable.put(tableAndColumn[0], columns);
            }
        }
        log.debug("Returning: {}", columnsByTable);
        return columnsByTable;
    }
}