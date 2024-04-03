package de.hdm_stuttgart.mi.dad;

import de.hdm_stuttgart.mi.dad.connectionprofile.ConnectionProfile;
import de.hdm_stuttgart.mi.dad.core.Service;
import de.hdm_stuttgart.mi.dad.incoming.InputHandler;
import de.hdm_stuttgart.mi.dad.outgoing.RepositoryFactory;
import de.hdm_stuttgart.mi.dad.ports.RepositoryPort;
import de.hdm_stuttgart.mi.dad.ports.ServicePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;


public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(final String[] args) {
        final ConnectionProfile connectionProfile = new ConnectionProfile("postgresql", "127.0.0.1", "5432", "test", "test", "test");

        final String url = String.format("jdbc:%s://%s:%s/%s", connectionProfile.getDriver(), connectionProfile.getHost(), connectionProfile.getPort(), connectionProfile.getDatabase());

        try (final Connection connection = DriverManager.getConnection(url, connectionProfile.getUser(), connectionProfile.getPassword())) {

            final RepositoryPort repository = RepositoryFactory.newRepository(connection, connectionProfile.getDriver());
            final ServicePort service = new Service(repository);
            final InputHandler inputHandler = new InputHandler(service);

            inputHandler.handleInput(args);
        } catch (final Exception exception) {
            log.error(exception.getMessage());
        }
    }
}
