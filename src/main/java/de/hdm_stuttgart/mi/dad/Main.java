package de.hdm_stuttgart.mi.dad;

import de.hdm_stuttgart.mi.dad.config.DatabaseConfig;
import de.hdm_stuttgart.mi.dad.core.Service;
import de.hdm_stuttgart.mi.dad.incoming.InputHandler;
import de.hdm_stuttgart.mi.dad.outgoing.RepositoryFactory;
import de.hdm_stuttgart.mi.dad.ports.RepositoryPort;
import de.hdm_stuttgart.mi.dad.ports.ServicePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(final String[] args) {
        final DatabaseConfig dbConf = new DatabaseConfig("postgresql", "127.0.0.1", "5432", "test", "test", "test");
        try {
            final RepositoryPort repository = RepositoryFactory.newRepository(dbConf);
            final ServicePort service = new Service(repository);
            final InputHandler inputHandler = new InputHandler(service);

            inputHandler.handleInput(args);
        } catch (final Exception exception) {
            log.error(exception.getMessage());
        }
    }
}
