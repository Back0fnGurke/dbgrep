package de.hdm_stuttgart.mi.dad.incoming;

import de.hdm_stuttgart.mi.dad.core.entity.Table;
import de.hdm_stuttgart.mi.dad.core.exception.ServiceException;
import de.hdm_stuttgart.mi.dad.core.ports.ServicePort;
import de.hdm_stuttgart.mi.dad.core.property.Property;
import de.hdm_stuttgart.mi.dad.incoming.input.SearchInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Handles the processing of search inputs and delegates the search operations to the service layer.
 * This class is responsible for determining the search mode based on the input and invoking the appropriate
 * service methods to perform the search.
 */
public class ApplicationHandler {

    private static final Logger log = LoggerFactory.getLogger(ApplicationHandler.class);

    private final ServicePort service;

    /**
     * Constructor for the ApplicationHandler class.
     *
     * @param service the {@link ServicePort} to be used for search operations
     */
    public ApplicationHandler(final ServicePort service) {
        this.service = service;
    }

    /**
     * Processes the given {@link SearchInput} and performs the search operations based on the input parameters.
     * The search can be performed on the whole database, specific tables, and or specific columns.
     *
     * @param searchInput the parsed user {@link SearchInput}
     * @return a {@link List} of {@link Table} that match the search criteria
     * @throws ServiceException if an error occurs during the search operations
     */
    public List<Table> handle(final SearchInput searchInput) throws ServiceException {
        log.debug("Called with searchInput: {}", searchInput);

        final List<Property<?>> propertyList = searchInput.propertyList();
        final List<Table> resultTables = new ArrayList<>();

        if (!searchInput.searchTables() && !searchInput.searchColumns()) {
            log.debug("Searching through whole database.");
            resultTables.addAll(service.searchThroughWholeDatabase(propertyList));
        }
        if (searchInput.searchTables()) {
            log.debug("Searching through tables.");
            final List<String> tableNames = searchInput.tables();
            service.validateTableNames(tableNames);
            resultTables.addAll(service.searchThroughTables(tableNames, propertyList));
        }
        if (searchInput.searchColumns()) {
            log.debug("Searching through columns.");
            final Map<String, List<String>> columns = searchInput.columns();
            for (final Map.Entry<String, List<String>> entry : columns.entrySet()) {
                service.validateColumnNames(entry.getKey(), entry.getValue());
            }
            resultTables.addAll(service.searchThroughColumns(columns, propertyList));
        }

        log.debug("Result list size: {}", resultTables.size());
        return resultTables;
    }
}