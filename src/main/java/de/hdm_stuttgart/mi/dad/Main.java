package de.hdm_stuttgart.mi.dad;

import de.hdm_stuttgart.mi.dad.connectionprofile.ConnectionProfile;
import de.hdm_stuttgart.mi.dad.connectionprofile.ConnectionProfileHandler;
import de.hdm_stuttgart.mi.dad.core.Service;
import de.hdm_stuttgart.mi.dad.core.ports.RepositoryPort;
import de.hdm_stuttgart.mi.dad.core.ports.ServicePort;
import de.hdm_stuttgart.mi.dad.incoming.input.InputHandler;
import de.hdm_stuttgart.mi.dad.outgoing.repository.RepositoryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    /**
     * This function is started when the application is executed.
     * Set up everything necessary for SearchLevelHandler and pass the service and user input to it.
     * Any exceptions are caught and printed for the user.
     *
     * @param args user input
     */
    public static void main(final String[] args) {
        try {
            ConnectionProfileHandler profileHandler = new ConnectionProfileHandler(getConnectionProfileDirectory());
            final ConnectionProfile profile = profileHandler.getConnectionProfile(args);

            final String url = String.format("jdbc:%s://%s:%s/%s", profile.getDriver(), profile.getHost(), profile.getPort(), profile.getDatabase());

            try (final Connection connection = DriverManager.getConnection(url, profile.getUser(), profile.getPassword())) {
                log.debug("build connection");
                final RepositoryPort repository = RepositoryFactory.createRepository(connection, profile.getDriver());
                final ServicePort service = new Service(repository);
                final InputHandler inputHandler = new InputHandler(service);
                inputHandler.handleInput(args);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        log.debug("end programm");
    }

    /**
     * Finds out the directory where the jar is located and checks whether the connection profile folder exists.
     * If not, an exception is thrown.
     *
     * @return path of connection profile directory
     */
    public static Path getConnectionProfileDirectory() throws URISyntaxException {
        Path path = Paths.get(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        return path.getParent().resolve("connection_profiles");
    }
}
