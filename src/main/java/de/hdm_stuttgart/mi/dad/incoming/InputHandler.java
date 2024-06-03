package de.hdm_stuttgart.mi.dad.incoming;

import de.hdm_stuttgart.mi.dad.connectionprofile.ConnectionProfile;
import de.hdm_stuttgart.mi.dad.connectionprofile.ConnectionProfileHandler;
import de.hdm_stuttgart.mi.dad.core.Service;
import de.hdm_stuttgart.mi.dad.core.exception.MultipleProfileException;
import de.hdm_stuttgart.mi.dad.core.exception.NoProfileException;
import de.hdm_stuttgart.mi.dad.core.ports.RepositoryPort;
import de.hdm_stuttgart.mi.dad.core.ports.ServicePort;
import de.hdm_stuttgart.mi.dad.outgoing.OutputHandler;
import de.hdm_stuttgart.mi.dad.outgoing.RepositoryFactory;


import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;

public class InputHandler {
    OutputHandler outputHandler;
    public InputHandler() {
        outputHandler = new OutputHandler();
    }

    public void handleInput(String[] args) {
        ConnectionProfileHandler profileHandler = new ConnectionProfileHandler("src/test/resources/TestConnectionProfileHandler/one_profile");

        try {
            final ConnectionProfile profile = handleConnectionProfile(profileHandler, args);

            final String url = String.format("jdbc:%s://%s:%s/%s", profile.getDriver(), profile.getHost(), profile.getPort(), profile.getDatabase());

            try (final Connection connection = DriverManager.getConnection(url, profile.getUser(), profile.getPassword())) {

                final RepositoryPort repository = RepositoryFactory.newRepository(connection, profile.getDriver());
                final ServicePort service = new Service(repository);
            } catch (SQLException e) {
                outputHandler.printErrorMessage("Es gab Probleme beim Lesen der Datenbank.");
            }

        } catch (Exception e) {
            outputHandler.printErrorMessage(e.getMessage());
        }

    }

        public ConnectionProfile handleConnectionProfile(ConnectionProfileHandler profileHandler, String[] args) throws IOException, NoProfileException, MultipleProfileException {
        if (Arrays.asList(args).contains("--profile")){
            int indexProfileArgument = Arrays.asList(args).indexOf("--profile") + 1;
            String profileArgument = args[indexProfileArgument];
            return profileHandler.getSelectedProfile(profileArgument);
        } else {
           return profileHandler.getDefaultProfile();
        }
    }

}
