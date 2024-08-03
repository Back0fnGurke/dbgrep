package de.hdm_stuttgart.mi.dad.connectionprofile;

import de.hdm_stuttgart.mi.dad.connectionprofile.exception.IllegalConnectionProfileException;
import de.hdm_stuttgart.mi.dad.connectionprofile.exception.MultipleProfileException;
import de.hdm_stuttgart.mi.dad.connectionprofile.exception.NoProfileException;
import de.hdm_stuttgart.mi.dad.incoming.ArgumentType;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

/**
 * Find the connection profile file in the determined directory and create a connection profile
 */
public class ConnectionProfileHandler {
    public final Path directoryOfProfiles;

    public ConnectionProfileHandler(Path directory) throws FileNotFoundException {
        if (!Files.exists(directory)) {
            throw new FileNotFoundException("Please create a directory with the name “connection_profiles” at the directory where" +
                    " the jar file is located. Add at least one connection profile there.");
        }
        directoryOfProfiles = directory;
    }

    /**
     * List all files of the directory of profiles and return a connection profile if only one exists
     * else an exception is thrown.
     *
     * @return a connection profile of the profile file
     */
    public ConnectionProfile getDefaultProfile() throws NoProfileException, MultipleProfileException, IOException, IllegalConnectionProfileException {
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
    public ConnectionProfile getSelectedProfile(String fileName) throws IOException, IllegalConnectionProfileException {
        Path pathOfProfile = directoryOfProfiles.resolve(fileName);
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

    /**
     * Returns a list of paths from the existing profiles.
     * These are located in the specified directory.
     *
     * @return a list of paths
     */
    private List<Path> getListOfProfilesPath() throws IOException {
        try (Stream<Path> stream = Files.list(directoryOfProfiles)) {
            return stream
                    .filter(file -> !Files.isDirectory(file))
                    .toList();
        }
    }

    /**
     * Read the connection profile file and filter for certain properties and pass this value to the ConnectionProfile
     *
     * @param pathOfProfile of read connection profile file
     * @return ConnectionProfile from the read connection profile file
     */
    private ConnectionProfile readProfileFile(Path pathOfProfile) throws IOException, IllegalConnectionProfileException {
        Properties configProperties = new Properties();
        try (InputStream stream = Files.newInputStream(pathOfProfile)) {
            configProperties.load(stream);
        }

        Map<String, String> profileProperties = new HashMap<>();
        profileProperties.put("driver", configProperties.getProperty("driver"));
        profileProperties.put("host", configProperties.getProperty("host"));
        profileProperties.put("port", configProperties.getProperty("port"));
        profileProperties.put("user", configProperties.getProperty("user"));
        profileProperties.put("password", configProperties.getProperty("password"));
        profileProperties.put("database", configProperties.getProperty("database"));

        for (Map.Entry<String, String> property : profileProperties.entrySet()) {
            if (property.getValue() == null) {
                throw new IllegalConnectionProfileException("\"" + property.getKey() + "\" is missing in the connection profile file or was not specified correctly.");
            }
        }

        return new ConnectionProfile(profileProperties.get("driver"), profileProperties.get("host"), profileProperties.get("port"),
                profileProperties.get("user"), profileProperties.get("password"), profileProperties.get("database"));
    }

    /**
     * Checks whether the --profile command is present
     * and creates a ConnectionProfile from the specified connection profile file.
     * If no --profile command is available, the default profile is searched for.
     *
     * @return ConnectionProfile from given file name or default profile
     */
    public ConnectionProfile getConnectionProfile(String[] args) throws IOException, NoProfileException, MultipleProfileException, IllegalConnectionProfileException {
        if (Arrays.asList(args).contains(ArgumentType.PROFILE.toString())) {
            int indexProfileArgument = Arrays.asList(args).indexOf(ArgumentType.PROFILE.toString()) + 1;
            String profileArgument = args[indexProfileArgument];
            return getSelectedProfile(profileArgument);
        } else {
            return getDefaultProfile();
        }
    }
}
