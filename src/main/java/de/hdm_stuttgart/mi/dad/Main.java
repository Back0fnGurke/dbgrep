package de.hdm_stuttgart.mi.dad;

import de.hdm_stuttgart.mi.dad.connectionprofile.ConnectionProfile;
import de.hdm_stuttgart.mi.dad.connectionprofile.ConnectionProfileHandler;
import de.hdm_stuttgart.mi.dad.core.Service;
import de.hdm_stuttgart.mi.dad.core.entity.Table;
import de.hdm_stuttgart.mi.dad.core.ports.RepositoryPort;
import de.hdm_stuttgart.mi.dad.core.ports.ServicePort;
import de.hdm_stuttgart.mi.dad.driverloader.DriverLoader;
import de.hdm_stuttgart.mi.dad.incoming.ApplicationHandler;
import de.hdm_stuttgart.mi.dad.incoming.input.ArgumentValidator;
import de.hdm_stuttgart.mi.dad.incoming.input.HelpInputHandler;
import de.hdm_stuttgart.mi.dad.incoming.input.SearchInput;
import de.hdm_stuttgart.mi.dad.incoming.input.SearchInputHandler;
import de.hdm_stuttgart.mi.dad.incoming.output.OutputHandler;
import de.hdm_stuttgart.mi.dad.outgoing.repository.RepositoryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

/**
 * The main class of the application.
 */
public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    private Main() {
    }

    /**
     * The main method of the application.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        log.debug("args: {}", (Object) args);

        try {
            if (ArgumentValidator.isValidArguments(args)) return;
            if (HelpInputHandler.handleHelp(args)) return;
            
            final ConnectionProfileHandler profileHandler = new ConnectionProfileHandler();
            final ConnectionProfile profile = profileHandler.getConnectionProfile(args);
            DriverLoader.loadDriver(profile);

            final String url = String.format("jdbc:%s://%s:%s/%s", profile.driver(), profile.host(), profile.port(), profile.database());
            try (Connection connection = DriverManager.getConnection(url, profile.user(), profile.password())) {
                final RepositoryPort repository = RepositoryFactory.createRepository(connection, profile.driver());
                final ServicePort service = new Service(repository);
                final ApplicationHandler applicationHandler = new ApplicationHandler(service);

                final SearchInput searchInput = SearchInputHandler.handleInput(args);
                final List<Table> resultTables = applicationHandler.handle(searchInput);

                OutputHandler.handleOutput(resultTables, searchInput.propertyList());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        log.debug("end program");
    }
}
