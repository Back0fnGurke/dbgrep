package de.hdm_stuttgart.mi.dad.outgoing;

import de.hdm_stuttgart.mi.dad.ports.RepositoryPort;

import java.sql.Connection;

public class RepositoryFactory {

    private RepositoryFactory() {
    }

    public static RepositoryPort newRepository(final Connection connection, final String driver) {
        return switch (driver) {
            case "postgresql" -> new PostgresRepository(connection);
            case "mysql" -> new MySQLRepository(connection);
            default -> null;
        };
    }
}
