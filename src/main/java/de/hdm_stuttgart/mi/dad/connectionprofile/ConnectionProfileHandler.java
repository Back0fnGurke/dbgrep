package de.hdm_stuttgart.mi.dad.connectionprofile;

import de.hdm_stuttgart.mi.dad.connectionprofile.exception.IllegalFileExtensionException;
import de.hdm_stuttgart.mi.dad.connectionprofile.exception.MultipleProfileException;
import de.hdm_stuttgart.mi.dad.connectionprofile.exception.NoProfileException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import java.util.stream.Stream;

/**
 * Find the connection profile file in the determined directory and create a connection profile
 */
public class ConnectionProfileHandler {
    private final String directoryOfProfiles;

    public ConnectionProfileHandler(String directory) {
        directoryOfProfiles = directory;
    }

    /**
     * List all files of the directory of profiles and return a connection profile if only one exists
     * else an exception is thrown.
     *
     * @return a connection profile of the profile file
     */
    public ConnectionProfile getDefaultProfile() throws NoProfileException, MultipleProfileException, IOException {
        List<Path> profiles = getListOfProfilesPath();

        long fileCount = profiles.size();

        if (fileCount == 0) {
            throw new NoProfileException("There is no profile file in '" + directoryOfProfiles + "'.");
        }
        if (fileCount > 1) {
            throw new MultipleProfileException("There are multiply profile files in '" + directoryOfProfiles + "'.");
        }

        return readProfileFile(profiles.getFirst());
    }

    /**
     * Test if a file of the file name exist and create a connection profile
     *
     * @param fileName the file name of the connection profile
     * @return a connection profile object of the file
     */
    public ConnectionProfile getSelectedProfile(String fileName) throws IOException, IllegalFileExtensionException {
        if (!hasConfigExtension(fileName)) {
            throw new IllegalFileExtensionException("A profile file has to end with '.cnf'.");
        }
        Path pathOfProfile = getDirectory().resolve(fileName);
        if (Files.exists(pathOfProfile) && !Files.isDirectory(pathOfProfile)) {
            return readProfileFile(pathOfProfile);
        }
        throw new FileNotFoundException("File '" + fileName + "' does not exist in '" +
                directoryOfProfiles + "'.");
    }

    /**
     * List all files of the directory of profiles
     *
     * @return String with file names of all profiles in directory
     */
    public String getStringOfProfileList() throws IOException {
        List<Path> profiles = getListOfProfilesPath();
        StringBuilder profileList = new StringBuilder();
        for (Path profile : profiles) {
            profileList.append(profile.getFileName().toString());
            profileList.append("\n");
        }
        return profileList.toString();
    }

    private Path getDirectory() {
        return Paths.get(directoryOfProfiles);
    }

    private List<Path> getListOfProfilesPath() throws IOException {
        try (Stream<Path> stream = Files.list(getDirectory())) {
            return stream
                    .filter(file -> !Files.isDirectory(file) && hasConfigExtension(file.getFileName().toString()))
                    .toList();
        }
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

    private boolean hasConfigExtension(String file) {
        return file.endsWith(".cnf");
    }
}
