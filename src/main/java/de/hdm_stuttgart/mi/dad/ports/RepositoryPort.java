package de.hdm_stuttgart.mi.dad.ports;

import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

public interface RepositoryPort {
    void findInWholeDatabase();

    void findInColumn();

    List<List<String>> findInTable(final String tableName, final List<String> columnNames, final Pattern pattern) throws SQLException;

    List<String> findTableColumns(final String tableName) throws SQLException;
}