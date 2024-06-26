package de.hdm_stuttgart.mi.dad.incoming;

import de.hdm_stuttgart.mi.dad.core.entity.Table;
import de.hdm_stuttgart.mi.dad.core.exception.ServiceException;
import de.hdm_stuttgart.mi.dad.core.ports.ServicePort;
import de.hdm_stuttgart.mi.dad.core.property.Property;

import java.util.*;

/**
 * Handles user input from command line and executes corresponding methods of service
 */
public class SearchLevelHandler {

    final ServicePort service;

    public SearchLevelHandler(final ServicePort service) {
        this.service = service;
    }

    /**
     * First, a list is created using the properties specified.
     * It then checks whether the search is tables-specifically and/or column-specifically.
     * If neither variant is available, the entire database is searched.
     * The output is then print using the OutputFormatter.
     *
     * @param args whole user input
     */
    public void handleInput(final String[] args) throws ServiceException {
        List<Table> resultTables = new ArrayList<>();
        try {
            List<Property<?>> propertyList = createPropertyList(args);

            List<String> tableValues = findAllValuesOfArgument(args, ArgumentType.TABLE);
            resultTables.addAll(service.searchThroughTables(tableValues, propertyList));

            List<String> columnValues = findAllValuesOfArgument(args, ArgumentType.COLUMN);
            Map<String, List<String>> columnsByTable = createColumnsByTable(columnValues);
            resultTables.addAll(service.searchThroughColumns(columnsByTable, propertyList));

            if (hasNotArgument(args, ArgumentType.COLUMN) && hasNotArgument(args, ArgumentType.TABLE)) {
                resultTables.addAll(service.searchThroughWholeDatabase(propertyList));
            }
            //TODO OutputFormatter
            System.out.println(resultTables);
        } catch (IllegalArgumentException e) {
            System.out.println(e);
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

        for (int i = 0; i < args.length - 1; i++) {
            for (ArgumentType argumentType : ArgumentType.values()) {
                if (argumentType.isProperty && argumentType.argumentString.equals(args[i])) {
                    propertyList.add(ArgumentType.createPropertyFromArgumentType(argumentType, args[i + 1]));
                    i++;
                }
            }
        }
        return propertyList;
    }

    /**
     * Creates a list of strings that are all arguments of a certain argument type.
     *
     * @param args whole user input
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

    private Map<String, List<String>> createColumnsByTable(List<String> columnValues) {
        Map<String, List<String>> columnsByTable = new HashMap<>();
        for (String columnValue : columnValues) {
            String[] tableAndColumn = columnValue.split("\\.");
            if (tableAndColumn.length != 2) {
                throw new IllegalArgumentException(columnValue + "is not a valid " + ArgumentType.COLUMN.argumentString + " argument.");
            }
            if (columnsByTable.containsKey(tableAndColumn[0])){
                columnsByTable.get(tableAndColumn[0]).add(tableAndColumn[1]);
            } else {
                List<String> columns = new ArrayList<>();
                columns.add(tableAndColumn[1]);
                columnsByTable.put(tableAndColumn[0], columns);
            }
        }
        return columnsByTable;
    }

    private boolean hasNotArgument(final String[] args, ArgumentType argument) {
        return !Arrays.asList(args).contains(argument.toString());
    }
}
