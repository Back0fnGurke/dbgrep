package de.hdm_stuttgart.mi.dad.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import java.util.stream.Stream;

public class ConfigHandler {
    private final String DIRECTORY_OF_PROFILES = ".dbgrep/profiles";

    public DatabaseConfig getDefaultDBConfig() throws IOException {
        try (Stream<Path> stream = Files.list(getDirectory())) {
            List<Path> profiles = stream
                    //TODO Filterung mit verschiedenen Dateitypen wie .cnf
                    .filter(file -> !Files.isDirectory(file))
                    .toList();

            long fileCount = profiles.size();

            if (fileCount == 0) {
                //TODO Errornachrichten System erstellen? Enums?
                throw new IllegalArgumentException("Im Ordner " + DIRECTORY_OF_PROFILES +
                        " existiert keine File. Bitte erstellen Sie ein Profil in diesem Ordner.");
            }
            if (fileCount > 1) {
                //TODO Auf jeden Fall Formatierung xD
                throw new IllegalArgumentException("Es sind mehrere Profile im Ordner " + DIRECTORY_OF_PROFILES +
                        " vorhanden. Bitte wählen Sie mit dem Befehl '--profile' ein Profil aus den hier" +
                        " aufgelisteten Profilen heraus: \n" + getStringOfProfileList(profiles));
            }

            return readProfileFile(profiles.get(0));
        }
    }

    public DatabaseConfig getSelectedDBConfig(String fileName) throws IOException {
        Path pathOfProfile = getDirectory().resolve(fileName);
        if (Files.exists(pathOfProfile) && !Files.isDirectory(pathOfProfile)) {
            return readProfileFile(pathOfProfile);
        }
        //TODO eigene Exception schreiben?
        throw new IllegalArgumentException("Die angegebene File" + fileName + "existiert nicht im Ordner " +
                DIRECTORY_OF_PROFILES + ".");
    }

    private Path getDirectory() {
        return Paths.get(DIRECTORY_OF_PROFILES);
    }

    private String getStringOfProfileList(List<Path> profiles) {
        StringBuilder profileList = new StringBuilder();
        for (Path profile : profiles) {
            profileList.append(profile.getFileName().toString());
            profileList.append("\n");
        }
        return profileList.toString();
    }

    private DatabaseConfig readProfileFile(Path pathOfProfile) throws IOException {
        Properties configProperties = new Properties();
        try (InputStream stream = Files.newInputStream(pathOfProfile)) {
            configProperties.load(stream);
        }
        //TODO null Abfrage; Testen ob die angegebenen Werte passen? Wo?
        String driver = configProperties.getProperty("driver");
        String host = configProperties.getProperty("host");
        String port = configProperties.getProperty("port");
        String user = configProperties.getProperty("user");
        String password = configProperties.getProperty("password");
        String database = configProperties.getProperty("database");

        return new DatabaseConfig(driver, host, port, user, password, database);
    }

}
