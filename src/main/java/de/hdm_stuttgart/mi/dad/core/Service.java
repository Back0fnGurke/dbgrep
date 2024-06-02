package de.hdm_stuttgart.mi.dad.core;

import de.hdm_stuttgart.mi.dad.core.entity.Table;
import de.hdm_stuttgart.mi.dad.core.exception.*;
import de.hdm_stuttgart.mi.dad.core.ports.RepositoryPort;
import de.hdm_stuttgart.mi.dad.core.ports.ServicePort;
import de.hdm_stuttgart.mi.dad.core.property.Property;
import de.hdm_stuttgart.mi.dad.core.property.PropertyType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.*;

import static de.hdm_stuttgart.mi.dad.core.property.PropertyType.*;

public class Service implements ServicePort {

    private static final String ERRPROPERTIESNULL = "Parameter properties must not be null or empty.";
    private static final String ERRSQL = "Failed to find matching pattern in column values of table rows.";

    final RepositoryPort repository;

    final Logger log = LoggerFactory.getLogger(Service.class);

    public Service(final RepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public List<Table> searchThroughColumns(final Map<String, List<String>> columnNamesOfTables, final List<Property> properties) throws ServiceException {
        try {
            log.debug("columnNamesOfTables: {}, properties: {}", columnNamesOfTables, properties);

            if (columnNamesOfTables == null || columnNamesOfTables.isEmpty()) {
                throw new IllegalArgumentException("Parameter columnNamesOfTables must not be null or empty.");
            }
            if (properties == null || properties.isEmpty()) {
                throw new IllegalArgumentException(ERRPROPERTIESNULL);
            }

            if (properties.size() != 1) {
                validatePropertyCombination(properties);
            }
            validateTableNames(columnNamesOfTables.keySet().stream().toList());
            for (final Map.Entry<String, List<String>> entry : columnNamesOfTables.entrySet()) {
                validateColumnNames(entry.getKey(), properties, entry.getValue());
            }

            return searchColumns(columnNamesOfTables, properties);
        } catch (final SQLException e) {
            throw new ServiceException(ERRSQL, e);
        }
    }

    @Override
    public List<Table> searchThroughTables(final List<String> tableNames, final List<Property> properties) throws ServiceException {
        try {
            log.debug("tableNames: {}, properties: {}", tableNames, properties);

            if (tableNames == null || tableNames.isEmpty()) {
                throw new IllegalArgumentException("Parameter tableNames must not be null or empty.");
            }
            if (properties == null || properties.isEmpty()) {
                throw new IllegalArgumentException(ERRPROPERTIESNULL);
            }

            if (properties.size() != 1) {
                validatePropertyCombination(properties);
            }
            validateTableNames(tableNames);

            return searchTables(tableNames, properties);
        } catch (final SQLException e) {
            throw new ServiceException(ERRSQL, e);
        }
    }

    @Override
    public List<Table> searchThroughWholeDatabase(final List<Property> properties) throws ServiceException {
        try {
            log.debug("properties: {}", properties);

            if (properties == null || properties.isEmpty()) {
                throw new IllegalArgumentException(ERRPROPERTIESNULL);
            }

            if (properties.size() != 1) {
                validatePropertyCombination(properties);
            }

            final List<String> tableNames = getTableNames();

            log.debug("table names of database: {}", tableNames);

            return searchTables(tableNames, properties);
        } catch (final SQLException e) {
            throw new ServiceException(ERRSQL, e);
        }
    }

    private List<Table> searchColumns(final Map<String, List<String>> columnNamesOfTables, final List<Property> properties) throws SQLException {
        final List<Table> results = new ArrayList<>(columnNamesOfTables.size());
        for (final Map.Entry<String, List<String>> entry : columnNamesOfTables.entrySet()) {
            final String tableName = entry.getKey();
            final List<String> columnNames = entry.getValue();

            log.debug("tableName: {}, columnNames: {}", tableName, columnNames);

            final Table table = repository.findTableRowsWithProperties(tableName, columnNames, properties);

            log.debug("found rows in table: {}", table);

            results.add(table);
        }

        log.debug("found table results: {}", results);

        return results;
    }

    private List<Table> searchTables(final List<String> tableNames, final List<Property> properties) throws SQLException {
        final Map<String, List<String>> columnNamesOfTables = new HashMap<>();
        for (final String tableName : tableNames) {
            log.debug("tableName: {}", tableName);

            final List<String> columnNames = getColumnNames(tableName, properties);
            log.debug("column names of table: {}", columnNames);

            columnNamesOfTables.put(tableName, columnNames);
        }

        return searchColumns(columnNamesOfTables, properties);
    }

    private void validatePropertyCombination(final List<Property> properties) throws SQLException, ValidationException {
        log.debug("properties: {}, validPropertyCombinations: {}", properties, validPropertyCombinations);

        boolean valid = false;
        for (final List<PropertyType> combination : validPropertyCombinations) {
            if (new HashSet<>(combination).containsAll(properties.stream().map(Property::getType).toList())) {
                valid = true;
                break;
            }
        }

        log.debug("valid: {}", valid);

        if (!valid) {
            throw new IllegalPropertyCombinationException(properties.toString(), "Invalid property combination");
        }
    }

    private void validateTableNames(final List<String> tableNames) throws SQLException, ValidationException {
        try {
            log.debug("table names: {}", tableNames);

            final List<String> validTableNames = repository.findTableNames();

            log.debug("valid table names: {}", validTableNames);

            for (final String tableName : tableNames) {
                if (!validTableNames.contains(tableName)) {
                    throw new IllegalTableNameException(tableName, "Provided invalid table name.");
                }
            }
        } catch (final SQLException e) {
            throw new SQLException("Failed to validate table names", e);
        }
    }

    private void validateColumnNames(final String tableName, final List<Property> properties, final List<String> columnNames) throws SQLException, ValidationException {
        try {
            log.debug("tableName: {}, columnNames: {}", tableName, columnNames);

            final List<String> validColumnNames = getColumnNames(tableName, properties);

            log.debug("validColumnNames: {}", validColumnNames);

            for (final String columnName : columnNames) {
                if (!validColumnNames.contains(columnName)) {
                    throw new IllegalColumnNameException(columnName, "Provided invalid column name. Either it doesn't exist or doesn't match data type of provided properties");
                }
            }
        } catch (final SQLException e) {
            throw new SQLException("Failed to validate column names", e);
        }
    }

    private List<String> getColumnNames(final String tableName, final List<Property> properties) throws SQLException {
        try {
            log.debug("tableName: {}, properties: {}", tableName, properties);

            if (properties.stream().anyMatch((property) -> property.getType() == EQUAL || property.getType() == RANGENUMERIC || property.getType() == GREATERNUMERIC)) {
                return repository.findTableColumnNamesNumeric(tableName);
            } else if (properties.stream().anyMatch((property) -> property.getType() == GREATERDATE)) {
                return repository.findTableColumnNamesDate(tableName);
            } else {
                return repository.findTableColumnNamesAll(tableName);
            }
        } catch (final SQLException e) {
            throw new SQLException("Failed to find column names of table.", e);
        }
    }

    private List<String> getTableNames() throws SQLException {
        try {
            return repository.findTableNames();
        } catch (final SQLException e) {
            throw new SQLException("Failed to find table names of database.", e);
        }
    }
}
