package de.hdm_stuttgart.mi.dad.outgoing.repository;

import de.hdm_stuttgart.mi.dad.core.ports.RepositoryPort;
import de.hdm_stuttgart.mi.dad.outgoing.PropertyExpressionReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Connection;

/**
 * The RepositoryFactory class is a factory class that is responsible for creating instances of repositories.
 * It uses the Factory design pattern to create objects, which allows for more flexibility in creating different types of repositories.
 * <p>
 * The class is not meant to be instantiated. Instead, use its static methods to create repositories.
 * <p>
 * The class has a private constructor to prevent instantiation, and a static method `createRepository` that creates a repository based on the provided driver name.
 * The driver name is passed as a string, and the method uses a switch statement to create the appropriate type of repository.
 * <p>
 * Currently, the `createRepository` method supports two types of repositories: "postgresql" and "mysql". If an unsupported driver is provided, the method returns null.
 * <p>
 * This class can be used to create a repository without knowing the exact class of the repository that will be created. This can be useful in situations where the specific type of repository is not known until runtime.
 * <p>
 * Example usage:
 * <p>
 * RepositoryPort repository = RepositoryFactory.createRepository(connection, "postgresql");
 */
public class RepositoryFactory {

    /**
     * Logger for this class.
     */
    private static final Logger log = LoggerFactory.getLogger(RepositoryFactory.class);

    /**
     * Private constructor to prevent instantiation.
     */
    private RepositoryFactory() {
    }

    /**
     * Creates a repository based on the provided driver name.
     *
     * @param connection the database connection.
     * @param driver     the driver of the repository to create.
     * @return the created repository.
     * @throws IOException if an I/O error occurs.
     */
    public static RepositoryPort createRepository(final Connection connection, final String driver) throws IOException {
        log.debug("Creating repository for driver: {}", driver);

        return switch (driver) {
            case "postgresql" ->
                    new PostgresRepository(connection, PropertyExpressionReader.readPropertyExpressions("postgres_property_expressions.json"));
            case "mysql" ->
                    new MySQLRepository(connection, PropertyExpressionReader.readPropertyExpressions("mysql_property_expressions.json"));
            default -> null;
        };
    }
}
