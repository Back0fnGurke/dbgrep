package de.hdm_stuttgart.mi.dad.outgoing;

import de.hdm_stuttgart.mi.dad.config.DatabaseConfig;
import de.hdm_stuttgart.mi.dad.ports.RepositoryPort;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class RepositoryFactory {

    private RepositoryFactory() {
    }

    public static RepositoryPort newRepository(final DatabaseConfig dbConf) throws SQLException {
        final String url = String.format("jdbc:%s://%s:%s/%s", dbConf.getDriver(), dbConf.getHost(), dbConf.getPort(), dbConf.getDatabase());

        final Connection connection = DriverManager.getConnection(url, dbConf.getUser(), dbConf.getPassword());

        return switch (dbConf.getDriver()) {
            case "postgresql" -> new PostgresRepository(connection);
            case "mysql" -> new MySQLRepository(connection);
            default -> null;
        };
    }
}
