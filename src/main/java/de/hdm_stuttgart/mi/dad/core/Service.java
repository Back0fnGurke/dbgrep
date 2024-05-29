package de.hdm_stuttgart.mi.dad.core;

import de.hdm_stuttgart.mi.dad.core.entity.Table;
import de.hdm_stuttgart.mi.dad.core.property.Property;
import de.hdm_stuttgart.mi.dad.ports.RepositoryPort;
import de.hdm_stuttgart.mi.dad.ports.ServicePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

public class Service implements ServicePort {

    final RepositoryPort repository;

    final Logger log = LoggerFactory.getLogger(Service.class);

    public Service(final RepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public List<Table> searchTables(final List<String> tableNames, final Property property) throws SQLException {

//        log.debug("table names: {}, search pattern: {}", tableNames, pattern.pattern());
//
//        final List<Table> results = new ArrayList<>(tableNames.size());
//
//        for (final String tableName : tableNames) {
//
//            log.debug("table name: {}", tableName);
//
//            final List<String> columnNames;
//            try {
//                columnNames = repository.findTableColumnNamesAll(tableName);
//            } catch (final SQLException e) {
//                throw new SQLException("failed to find column names of table: " + e.getMessage(), e);
//            }
//
//            log.debug("column names of table: {}", columnNames);
//
//            final Table table;
//            try {
//                table = repository.findPattern(tableName, columnNames, pattern);
//            } catch (final SQLException e) {
//                throw new SQLException("failed to find matching pattern in table rows: " + e.getMessage(), e);
//            }
//
//            log.debug("found rows in table: {}", table);
//
//            results.add(table);
//        }
//
//        log.debug("found table results: {}", results);
//
//        return results;
        return List.of();
    }
}
