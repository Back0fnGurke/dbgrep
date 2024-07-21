package de.hdm_stuttgart.mi.dad.core;

import de.hdm_stuttgart.mi.dad.core.entity.Table;
import de.hdm_stuttgart.mi.dad.core.exception.IllegalColumnNameException;
import de.hdm_stuttgart.mi.dad.core.exception.IllegalTableNameException;
import de.hdm_stuttgart.mi.dad.core.exception.RepositoryException;
import de.hdm_stuttgart.mi.dad.core.exception.ServiceException;
import de.hdm_stuttgart.mi.dad.core.ports.RepositoryPort;
import de.hdm_stuttgart.mi.dad.core.ports.ServicePort;
import de.hdm_stuttgart.mi.dad.core.property.Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static de.hdm_stuttgart.mi.dad.core.property.PropertyType.getDateTypes;
import static de.hdm_stuttgart.mi.dad.core.property.PropertyType.getNumericTypes;

//TODO: mutable parameter vor bearbeitung kopieren

/**
 * The Service class implements the ServicePort interface and provides the main business logic for the application.
 * <p>
 * This class is responsible for searching through tables, columns, and the whole database based on provided properties.
 * It also validates table names and column names.
 * <p>
 * The class has a constructor that takes a RepositoryPort object, which is used to interact with the database.
 * <p>
 * Example usage:
 * <p>
 * RepositoryPort repository = new RepositoryImplementation();
 * Service service = new Service(repository);
 * List<Property> properties = List.of(new Property(...));
 * List<String> tableNames = List.of("table1", "table2");
 * service.searchThroughTables(tableNames, properties);
 * <p>
 * Note: This class throws ServiceException when there is an issue with the service logic.
 */
public class Service implements ServicePort {

    private static final String ERRSQL = "Failed to find matching pattern in column values of table rows.";

    final RepositoryPort repository;

    /**
     * Logger for this class.
     */
    final Logger log = LoggerFactory.getLogger(Service.class);

    /**
     * Constructor for the Service class.
     *
     * @param repository the RepositoryPort object used to interact with the database.
     */
    public Service(final RepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public List<Table> searchThroughColumns(final Map<String, List<String>> columnNamesOfTables, final List<Property> properties) throws ServiceException {
        try {
            log.debug("columnNamesOfTables: {}, properties: {}", columnNamesOfTables, properties);

            final List<Table> results = columnNamesOfTables.entrySet().stream().map(entry -> {
                final String tableName = entry.getKey();
                final List<String> columnNames = entry.getValue();
                log.debug("tableName: {}, columnNames: {}", tableName, columnNames);

                final Map<Property, List<String>> propertyColumns = properties.stream().collect(Collectors.toMap(
                        property -> property,
                        property -> {
                            try {
                                List<String> validColumnNames = getColumnNames(tableName, property);
                                return columnNames.stream().filter(validColumnNames::contains).toList();
                            } catch (SQLException e) {
                                throw new RepositoryException(e);
                            }
                        }
                ));

                try {
                    final Table table = repository.findTableRowsWithProperties(tableName, propertyColumns);
                    log.debug("found rows in table: {}", table);
                    return table;
                } catch (SQLException e) {
                    throw new RepositoryException(e);
                }
            }).toList();

            log.debug("found table results: {}", results);

            return results;
        } catch (final RepositoryException e) {
            throw new ServiceException(ERRSQL, e);
        }
    }

    @Override
    public List<Table> searchThroughTables(final List<String> tableNames, final List<Property> properties) throws ServiceException {
        try {
            log.debug("tableNames: {}, properties: {}", tableNames, properties);

            final List<Table> results = new ArrayList<>();
            for (final String tableName : tableNames) {
                log.debug("tableName: {}", tableName);

                final Map<Property, List<String>> propertyColumns = properties.stream().collect(Collectors.toMap(
                        property -> property,
                        property -> {
                            try {
                                List<String> columnNames = getColumnNames(tableName, property);
                                log.debug("column names of table: {}", columnNames);
                                return columnNames;
                            } catch (SQLException e) {
                                throw new RepositoryException(e);
                            }
                        }
                ));

                final Table table = repository.findTableRowsWithProperties(tableName, propertyColumns);
                log.debug("found rows in table: {}", table);

                results.add(table);
            }

            return results;
        } catch (final SQLException | RepositoryException e) {
            throw new ServiceException(ERRSQL, e);
        }
    }

    @Override
    public List<Table> searchThroughWholeDatabase(final List<Property> properties) throws ServiceException {
        try {
            log.debug("properties: {}", properties);

            final List<String> tableNames = getTableNames();
            log.debug("table names of database: {}", tableNames);

            return searchThroughTables(tableNames, properties);
        } catch (final SQLException e) {
            throw new ServiceException(ERRSQL, e);
        }
    }

    @Override
    public void validateTableNames(final List<String> tableNames) throws ServiceException {
        try {
            log.debug("table names: {}", tableNames);

            final List<String> validTableNames = repository.findTableNames();

            log.debug("valid table names: {}", validTableNames);

            for (final String tableName : tableNames) {
                log.debug("valid tableName: {}", tableName);

                if (!validTableNames.contains(tableName)) {
                    throw new IllegalTableNameException(tableName, "Provided invalid table name.");
                }
            }
        } catch (final SQLException e) {
            throw new ServiceException("Failed to validate table names", e);
        }
    }

    @Override
    public void validateColumnNames(final String tableName, final List<String> columnNames) throws ServiceException {
        try {
            log.debug("tableName: {}, columnNames: {}", tableName, columnNames);

            final List<String> validColumnNames = repository.findTableColumnNamesAll(tableName);

            log.debug("validColumnNames: {}", validColumnNames);

            for (final String columnName : columnNames) {
                log.debug("valid columnName: {}", columnName);

                if (!validColumnNames.contains(columnName)) {
                    throw new IllegalColumnNameException(columnName, "Provided invalid column name. Either it doesn't exist or doesn't match data type of provided properties");
                }
            }
        } catch (final SQLException e) {
            throw new ServiceException("Failed to validate column names", e);
        }
    }

    /**
     * Returns column names with valid column types according to provided property
     *
     * @param tableName the table the columns belong to
     * @param property  property for column type check.
     * @return a list of Strings containing valid column names
     * @throws SQLException if a database error occurs
     */
    private List<String> getColumnNames(final String tableName, final Property property) throws SQLException {
        try {
            log.debug("tableName: {}, properties: {}", tableName, property);

            if (getNumericTypes().contains(property.getType())) {
                return repository.findTableColumnNamesNumeric(tableName);
            } else if (getDateTypes().contains(property.getType())) {
                return repository.findTableColumnNamesDate(tableName);
            } else {
                return repository.findTableColumnNamesAll(tableName);
            }
        } catch (final SQLException e) {
            throw new SQLException("Failed to find column names of table.", e);
        }
    }

    /**
     * Returns table names existing in the database
     *
     * @return a list of Strings containing valid non system table names
     * @throws SQLException if a database error occurs
     */
    private List<String> getTableNames() throws SQLException {
        try {
            return repository.findTableNames();
        } catch (final SQLException e) {
            throw new SQLException("Failed to find table names of database.", e);
        }
    }
}
