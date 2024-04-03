package de.hdm_stuttgart.mi.dad;

import de.hdm_stuttgart.mi.dad.connectionprofile.ConnectionProfile;
import de.hdm_stuttgart.mi.dad.core.Service;
import de.hdm_stuttgart.mi.dad.incoming.InputHandler;
import de.hdm_stuttgart.mi.dad.outgoing.RepositoryFactory;
import de.hdm_stuttgart.mi.dad.ports.RepositoryPort;
import de.hdm_stuttgart.mi.dad.ports.ServicePort;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TestInputHandlerIT {

    @Test
    void testInputHandler() throws SQLException {
        final ConnectionProfile connectionProfile = new ConnectionProfile("postgresql", "127.0.0.1", "5432", "test", "test", "test");

        final String url = String.format("jdbc:%s://%s:%s/%s", connectionProfile.getDriver(), connectionProfile.getHost(), connectionProfile.getPort(), connectionProfile.getDatabase());

        try (final Connection connection = DriverManager.getConnection(url, connectionProfile.getUser(), connectionProfile.getPassword())) {
            final RepositoryPort repository = RepositoryFactory.newRepository(connection, connectionProfile.getDriver());
            final ServicePort service = new Service(repository);
            final InputHandler inputHandler = new InputHandler(service);

            inputHandler.handleInput(new String[]{});
        }
        assertTrue(true);
    }
}
