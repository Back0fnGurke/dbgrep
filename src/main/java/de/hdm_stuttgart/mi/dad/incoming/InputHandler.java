package de.hdm_stuttgart.mi.dad.incoming;

import de.hdm_stuttgart.mi.dad.core.entity.Table;
import de.hdm_stuttgart.mi.dad.core.exception.ServiceException;
import de.hdm_stuttgart.mi.dad.core.ports.ServicePort;
import de.hdm_stuttgart.mi.dad.core.property.Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Handles user input from command line and executes corresponding methods of service
 */
public class InputHandler {

    private static final Logger log = LoggerFactory.getLogger(InputHandler.class);
    final ServicePort service;

    public InputHandler(final ServicePort service) {
        this.service = service;
    }

    /**
     * First, a list is created using the properties specified.
     * It then checks whether the search is tables-specifically and/or column-specifically.
     * If neither variant is available, the entire database is searched.
     * The output is then print using the OutputHandler.
     *
     * @param args whole user input
     */
    public void handleInput(final String[] args) throws ServiceException {
        log.debug("start handle input");

        validateArguments(args);

        List<Table> resultTables = new ArrayList<>();
        List<Property<?>> propertyList = createPropertyList(args);
        log.debug("property liste erstellt:{}", propertyList);


        if (hasNotArgument(args, ArgumentType.COLUMN) && hasNotArgument(args, ArgumentType.TABLE)) {
            log.debug("all databases are searched through");
            resultTables.addAll(service.searchThroughWholeDatabase(propertyList));
        } else {
            List<String> tableValues = findAllValuesOfArgument(args, ArgumentType.TABLE);
            resultTables.addAll(service.searchThroughTables(tableValues, propertyList));

            List<String> columnValues = findAllValuesOfArgument(args, ArgumentType.COLUMN);
            Map<String, List<String>> columnsByTable = createColumnsByTable(columnValues);
            resultTables.addAll(service.searchThroughColumns(columnsByTable, propertyList));
        }

        OutputHandler outputHandler = new OutputHandler();
        log.debug("result list{} size: {}", resultTables, resultTables.size());
        for (Table table : resultTables) {
            if (!table.rows().isEmpty()) {
                outputHandler.printTable(table, propertyList);
            }
        }
    }

    /**
     * Creates a list of all properties specified by the user.
     *
     * @param args whole user input
     * @return list of properties
     */
    private List<Property<?>> createPropertyList(final String[] args) {
        List<Property<?>> propertyList = new ArrayList<>();

        for (int i = 0; i < args.length; i++) {
            createPropertyIfArgumentIsProperty(args, i, propertyList);
        }
        return propertyList;
    }

    /**
     * Creates a property and add it to the property list.
     *
     * @param args whole user input
     * @param index for args
     * @param propertyList the properties are added here
     */
    private void createPropertyIfArgumentIsProperty(final String[] args, int index, List<Property<?>> propertyList) {
        String argument = args[index];

        for (ArgumentType argumentType : ArgumentType.values()) {
            if (argumentType.argumentString.equals(argument) && argumentType.isProperty) {
                propertyList.add(ArgumentType.createPropertyFromArgumentType(argumentType, args[index + 1]));
            }
        }
    }

    /**
     * See if all words beginning with ‘-- ’ are valid arguments. And prints warnings if this is not the case.
     *
     * @param args whole user input
     */
    private void validateArguments(final String[] args) {
        StringBuilder validationErrorMessages = new StringBuilder();
        for (String argument : args) {
            if (!argument.startsWith("--")) {
                continue;
            }

            boolean isNotValid = true;

            for (ArgumentType argumentType : ArgumentType.values()) {
                if (argumentType.argumentString.equals(argument)) {
                    isNotValid = false;
                    break;
                }
            }

            if (isNotValid) {
                validationErrorMessages.append("Warning! ").append(argument).append(" is not a valid argument. Use --help to see all arguments. \n");
            }
        }
        System.out.println(validationErrorMessages);
    }

    /**
     * Creates a list of strings that are all arguments of a certain argument type.
     *
     * @param args     whole user input
     * @param argument argument type which arguments are searched for
     * @return list of strings that are all arguments of a certain argument type
     */
    private List<String> findAllValuesOfArgument(final String[] args, ArgumentType argument) {
        List<String> values = new ArrayList<>();
        for (int i = 0; i < args.length - 1; i++) {
            if (args[i].equals(argument.toString())) {
                i++;
                values.add(args[i]);
            }
        }
        return values;
    }

    /**
     * Separates the table name from the column values that the user enters.
     *
     * @param columnValues The names of the columns that the user has entered
     * @return map that assigns the columns to each table
     */
    private Map<String, List<String>> createColumnsByTable(List<String> columnValues) {
        Map<String, List<String>> columnsByTable = new HashMap<>();
        for (String columnValue : columnValues) {
            String[] tableAndColumn = columnValue.split("\\.");
            if (tableAndColumn.length != 2) {
                throw new IllegalArgumentException(columnValue + "is not a valid " + ArgumentType.COLUMN.argumentString + " argument.");
            }
            if (columnsByTable.containsKey(tableAndColumn[0])) {
                columnsByTable.get(tableAndColumn[0]).add(tableAndColumn[1]);
            } else {
                List<String> columns = new ArrayList<>();
                columns.add(tableAndColumn[1]);
                columnsByTable.put(tableAndColumn[0], columns);
            }
        }
        return columnsByTable;
    }

    /**
     * Check if in the user input is no argument of a specific argument type.
     *
     * @param args     whole user input
     * @param argument argument type which arguments are searched for
     * @return true if there is no argument od argument type
     */
    private boolean hasNotArgument(final String[] args, ArgumentType argument) {
        return !Arrays.asList(args).contains(argument.toString());
    }
}