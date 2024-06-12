package de.hdm_stuttgart.mi.dad.outgoing;

import de.hdm_stuttgart.mi.dad.core.property.Property;
import de.hdm_stuttgart.mi.dad.core.property.PropertyType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

//TODO: doku

public final class QueryBuilder {

    private static final Logger log = LoggerFactory.getLogger(QueryBuilder.class);

    private final Map<PropertyType, String> propertyExpressions;


    public QueryBuilder(final Map<PropertyType, String> propertyExpressions) {
        this.propertyExpressions = propertyExpressions;
    }

    public String buildQuerryString(final String tableName, final Map<Property<?>, List<String>> propertyColumns) {
        log.debug("tableName: {}, propertyColumns: {}", tableName, propertyColumns);

        final StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM ").append(tableName).append(" WHERE ").append(getWhereClause(propertyColumns));
        log.debug("sql query string with placeholders: {}", query);

        return query.toString();
    }

    private String getWhereClause(final Map<Property<?>, List<String>> propertyColumns) {
        final StringBuilder clause = new StringBuilder();
        final int propertyColumnsCount = propertyColumns.size();

        int index = 0;
        for (Map.Entry<Property<?>, List<String>> entry : propertyColumns.entrySet()) {
            clause.append(getStatementForColumn(entry.getValue(), entry.getKey()));
            if (index + 1 < propertyColumnsCount) {
                clause.append(" AND ");
            } else {
                clause.append(";");
            }
            index++;
        }

        return clause.toString();
    }

    private String getStatementForColumn(final List<String> columnNames, final Property<?> property) {
        final StringBuilder statement = new StringBuilder();
        statement.append("(");

        for (int i = 0; i < columnNames.size(); i++) {
            statement.append(String.format(propertyExpressions.get(property.getType()), columnNames.get(i)));

            if (i + 1 < columnNames.size()) {
                statement.append(" OR ");
            }
        }

        statement.append(")");
        return statement.toString();
    }
}
