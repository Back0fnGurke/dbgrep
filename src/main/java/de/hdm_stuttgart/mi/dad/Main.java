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

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    /**
     * This function is started when the application is executed.
     * Set up everything necessary for SearchLevelHandler and pass the service and user input to it.
     * Any exceptions are caught and printed for the user.
     *
     * @param args user input
     */
    public static void main(String[] args) {
        log.debug("args: {}", args);

        //args = new String[]{"--profile", "postgres.cfg", "--greater", "2007-12-24", "--range", "4.0,7.0", "--regex", "test"};

        try {
            if (ArgumentValidator.isValidArguments(args)) return;
            if (HelpInputHandler.handleHelp(args)) return;

            final SearchInputHandler inputHandler = new SearchInputHandler();
            final ConnectionProfileHandler profileHandler = new ConnectionProfileHandler();
            final ConnectionProfile profile = profileHandler.getConnectionProfile(args);
            //final ConnectionProfile profile = new ConnectionProfile("postgresql", "org.postgresql.Driver", "I:/Uni/Semester 6/Database and application developement/dbgrep/postgresql-42.7.4.jar", "localhost", "5432", "test", "test", "test");
            DriverLoader.loadDriver(profile);

            final String url = String.format("jdbc:%s://%s:%s/%s", profile.driver(), profile.host(), profile.port(), profile.database());
            try (Connection connection = DriverManager.getConnection(url, profile.user(), profile.password())) {
                final RepositoryPort repository = RepositoryFactory.createRepository(connection, profile.driver());
                final ServicePort service = new Service(repository);
                final ApplicationHandler applicationHandler = new ApplicationHandler(service);

                final SearchInput searchInput = inputHandler.handleInput(args);
                final List<Table> resultTables = applicationHandler.handle(searchInput);

                for (Table table : resultTables) {
                    if (!table.rows().isEmpty()) {
                        OutputHandler.printTable(table, searchInput.propertyList());
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        log.debug("end program");
    }
}
