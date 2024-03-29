package de.hdm_stuttgart.mi.dad;

import de.hdm_stuttgart.mi.dad.config.DatabaseConfig;
import de.hdm_stuttgart.mi.dad.core.Service;
import de.hdm_stuttgart.mi.dad.incoming.InputHandler;
import de.hdm_stuttgart.mi.dad.outgoing.RepositoryFactory;
import de.hdm_stuttgart.mi.dad.ports.RepositoryPort;
import de.hdm_stuttgart.mi.dad.ports.ServicePort;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TestInputHandlerIT {

    @Test
    void testInputHandler() throws SQLException {
        final DatabaseConfig dbConf = new DatabaseConfig("postgresql", "127.0.0.1", "5432", "test", "test", "test");
        final RepositoryPort repository = RepositoryFactory.newRepository(dbConf);
        final ServicePort service = new Service(repository);
        final InputHandler inputHandler = new InputHandler(service);

        inputHandler.handleInput(new String[]{});

        assertTrue(true);
    }
}
