package de.hdm_stuttgart.mi.dad;

import de.hdm_stuttgart.mi.dad.connectionprofile.ConnectionProfile;
import de.hdm_stuttgart.mi.dad.connectionprofile.ConnectionProfileHandler;
import de.hdm_stuttgart.mi.dad.core.Service;
import de.hdm_stuttgart.mi.dad.core.ports.RepositoryPort;
import de.hdm_stuttgart.mi.dad.core.ports.ServicePort;
import de.hdm_stuttgart.mi.dad.incoming.SearchLevelHandler;
import de.hdm_stuttgart.mi.dad.outgoing.RepositoryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;


public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(final String[] args) {
        start(args);
    }

    public static void start(String[] args) {
        ConnectionProfileHandler profileHandler = new ConnectionProfileHandler("src/main/resources/connection_profile");

        try {
            final ConnectionProfile profile = profileHandler.getConnectionProfile(args);

            final String url = String.format("jdbc:%s://%s:%s/%s", profile.getDriver(), profile.getHost(), profile.getPort(), profile.getDatabase());

            try (final Connection connection = DriverManager.getConnection(url, profile.getUser(), profile.getPassword())) {
                log.debug("build connection");
                final RepositoryPort repository = RepositoryFactory.createRepository(connection, profile.getDriver());
                final ServicePort service = new Service(repository);
                final SearchLevelHandler searchLevelHandler = new SearchLevelHandler(service);
                searchLevelHandler.handleInput(args);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        log.debug("end programm");
    }
}
