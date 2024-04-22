package de.hdm_stuttgart.mi.dad.connectionprofile;

import de.hdm_stuttgart.mi.dad.core.exception.IllegalFileExtensionException;
import de.hdm_stuttgart.mi.dad.core.exception.MultipleProfileException;
import de.hdm_stuttgart.mi.dad.core.exception.NoProfileException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import java.util.stream.Stream;

public class ConnectionProfileHandler {
    private String directoryOfProfiles = "~/.dbgrep/profiles";

    public ConnectionProfileHandler(String directory) {
        directoryOfProfiles = directory;
    }

    public ConnectionProfile getDefaultDBConfig() throws NoProfileException, MultipleProfileException, IOException {
        try (Stream<Path> stream = Files.list(getDirectory())) {
            List<Path> profiles = stream
                    .filter(file -> !Files.isDirectory(file) && hasConfigExtension(file.getFileName().toString()))
                    .toList();

            long fileCount = profiles.size();

            if (fileCount == 0) {
                throw new NoProfileException("Im Ordner " + directoryOfProfiles +
                        " existiert keine File. Bitte erstellen Sie ein Profil in diesem Ordner.");
            }
            if (fileCount > 1) {
                throw new MultipleProfileException("Es sind mehrere Profile im Ordner " + directoryOfProfiles +
                        " vorhanden. Bitte wählen Sie mit dem Befehl '--profile' ein Profil aus den hier" +
                        " aufgelisteten Profilen heraus: \n" + getStringOfProfileList(profiles));
            }

            return readProfileFile(profiles.get(0));
        }
    }

    public ConnectionProfile getSelectedDBConfig(String fileName) throws IOException, IllegalFileExtensionException {
        if(hasConfigExtension(fileName)){
            throw new IllegalFileExtensionException("Die Profile File muss mit .cnf enden.");
        }
        Path pathOfProfile = getDirectory().resolve(fileName);
        if (Files.exists(pathOfProfile) && !Files.isDirectory(pathOfProfile)) {
            return readProfileFile(pathOfProfile);
        }
        throw new FileNotFoundException("Die angegebene File" + fileName + "existiert nicht im Ordner " +
                directoryOfProfiles + ".");
    }

    private Path getDirectory() {
        return Paths.get(directoryOfProfiles);
    }

    private String getStringOfProfileList(List<Path> profiles) {
        StringBuilder profileList = new StringBuilder();
        for (Path profile : profiles) {
            profileList.append(profile.getFileName().toString());
            profileList.append("\n");
        }
        return profileList.toString();
    }

    private ConnectionProfile readProfileFile(Path pathOfProfile) throws IOException {
        Properties configProperties = new Properties();
        try (InputStream stream = Files.newInputStream(pathOfProfile)) {
            configProperties.load(stream);
        }

        String driver = configProperties.getProperty("driver");
        String host = configProperties.getProperty("host");
        String port = configProperties.getProperty("port");
        String user = configProperties.getProperty("user");
        String password = configProperties.getProperty("password");
        String database = configProperties.getProperty("database");

        return new ConnectionProfile(driver, host, port, user, password, database);
    }

    private boolean hasConfigExtension(String file){
        return file.endsWith(".cnf");
    }
}
