package de.hdm_stuttgart.mi.dad.incoming;

import de.hdm_stuttgart.mi.dad.core.entity.Table;
import de.hdm_stuttgart.mi.dad.core.exception.ServiceException;
import de.hdm_stuttgart.mi.dad.core.ports.ServicePort;
import de.hdm_stuttgart.mi.dad.core.property.Property;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Handles user input from command line and executes corresponding methods of service
 */
public class SearchLevelHandler {

    final ServicePort service;

    public SearchLevelHandler(final ServicePort service) {
        this.service = service;
    }

    public void handleInput(final String[] args) throws ServiceException {
        List<Table> resultTables;
        List<Property> propertyList = createPropertyList(args);

        if (!hasArgument(args, ArgumentType.COLUMN) && !hasArgument(args, ArgumentType.TABLE)) {
            resultTables = service.searchThroughWholeDatabase(propertyList);
            return;
        }
        if (hasArgument(args, ArgumentType.TABLE)){
            List<String> tableValues = findAllValuesOfArgument(args, ArgumentType.TABLE);
            resultTables = service.searchThroughTables(tableValues, propertyList);
        }
        if (hasArgument(args,  ArgumentType.COLUMN)){
            List<String> tableValues = findAllValuesOfArgument(args, ArgumentType.TABLE);
            resultTables = service.searchThroughTables(tableValues, propertyList);
        }
    }

    private List<Property> createPropertyList(final String[] args) {
        List<Property> propertyList = new ArrayList<>();

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

    private boolean hasArgument(final String[] args, ArgumentType argument) {
        return Arrays.asList(args).contains(argument.toString());
    }
}
