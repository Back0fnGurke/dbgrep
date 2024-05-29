package de.hdm_stuttgart.mi.dad.incoming;

import de.hdm_stuttgart.mi.dad.connectionprofile.ConnectionProfile;
import de.hdm_stuttgart.mi.dad.connectionprofile.ConnectionProfileHandler;
import de.hdm_stuttgart.mi.dad.core.Service;
import de.hdm_stuttgart.mi.dad.core.exception.IllegalFileExtensionException;
import de.hdm_stuttgart.mi.dad.core.exception.MultipleProfileException;
import de.hdm_stuttgart.mi.dad.core.exception.NoProfileException;
import de.hdm_stuttgart.mi.dad.outgoing.OutputHandler;
import de.hdm_stuttgart.mi.dad.outgoing.RepositoryFactory;
import de.hdm_stuttgart.mi.dad.ports.RepositoryPort;
import de.hdm_stuttgart.mi.dad.ports.ServicePort;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;

public class InputHandlerGoik {
    OutputHandler outputHandler;
    public InputHandlerGoik() {
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



        } catch (IllegalFileExtensionException e) {
            outputHandler.printErrorMessage("Die Profile File muss mit .cnf enden.");

        } catch (IOException e) {
            outputHandler.printErrorMessage("Die angegebene File existiert nicht im Ordner" + profileHandler.directoryOfProfiles);

        } catch (NoProfileException e) {
            outputHandler.printErrorMessage("Im Ordner" + profileHandler.directoryOfProfiles + " existiert keine File. Bitte erstellen Sie ein Profil in diesem Ordner.");

        } catch (MultipleProfileException e) {
            try {
                outputHandler.printErrorMessage("Es sind mehrere Profile im Ordner " + profileHandler.directoryOfProfiles +
                        " vorhanden. Bitte wählen Sie mit dem Befehl '--profile' ein Profil aus den hier" +
                        " aufgelisteten Profilen heraus: \n" + profileHandler.getStringOfProfileList());
            } catch (IOException ex) {
                outputHandler.printErrorMessage("Irgendwas ist schief gelaufen.");
            }

        }

    }

        public ConnectionProfile handleConnectionProfile(ConnectionProfileHandler profileHandler, String[] args) throws IllegalFileExtensionException, IOException, NoProfileException, MultipleProfileException {
        if (Arrays.asList(args).contains("--profile")){
            int indexProfileArgument = Arrays.asList(args).indexOf("--profile") + 1;
            String profileArgument = args[indexProfileArgument];
            return profileHandler.getSelectedProfile(profileArgument);
        } else {
           return profileHandler.getDefaultProfile();
        }
    }

}
