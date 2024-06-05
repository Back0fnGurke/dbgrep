package de.hdm_stuttgart.mi.dad.incoming;

import de.hdm_stuttgart.mi.dad.core.entity.Table;
import de.hdm_stuttgart.mi.dad.core.exception.ServiceException;
import de.hdm_stuttgart.mi.dad.core.ports.ServicePort;
import de.hdm_stuttgart.mi.dad.core.property.Property;
import de.hdm_stuttgart.mi.dad.core.property.PropertyFactory;
import de.hdm_stuttgart.mi.dad.core.property.PropertyType;

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

        } else if (!hasArgument(args, ArgumentType.COLUMN)){
            List<String> tableValues = findAllValuesOfArgument(args, ArgumentType.TABLE);
            resultTables = service.searchThroughTables(tableValues, propertyList);
        }
    }

    private List<Property> createPropertyList(final String[] args) {
        List<Property> propertyList = new ArrayList<>();

        for (ArgumentType argumentType : ArgumentType.values()) {
            if (argumentType.isProperty && hasArgument(args, argumentType)) {
                propertyList.addAll(createAllPropertiesToArgumentType(args, argumentType));
            }
        }
        return propertyList;
    }

    private List<Property> createAllPropertiesToArgumentType(final String[] args, ArgumentType propertyArgument) {
        List<Property> propertyList = new ArrayList<>();
        int indexPropertyValue = Arrays.asList(args).indexOf(propertyArgument.toString()) + 1;
        String propertyValue = args[indexPropertyValue];
        PropertyFactory.getProperty(PropertyType.EQUAL, propertyValue); //TODO switch case
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
