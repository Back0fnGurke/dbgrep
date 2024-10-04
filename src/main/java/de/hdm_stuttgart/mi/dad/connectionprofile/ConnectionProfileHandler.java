package de.hdm_stuttgart.mi.dad.connectionprofile;

import de.hdm_stuttgart.mi.dad.Main;
import de.hdm_stuttgart.mi.dad.connectionprofile.exception.InvalidConnectionProfileException;
import de.hdm_stuttgart.mi.dad.connectionprofile.exception.MultipleProfileException;
import de.hdm_stuttgart.mi.dad.connectionprofile.exception.NoProfileException;
import de.hdm_stuttgart.mi.dad.incoming.input.ArgumentType;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

/**
 * Find the connection profile file in the determined directory and create a connection profile.
 */
public class ConnectionProfileHandler {

    private final Path directoryOfProfiles;

    public ConnectionProfileHandler() throws FileNotFoundException, URISyntaxException {
        this.directoryOfProfiles = getConnectionProfileDirectory();

        if (!Files.exists(directoryOfProfiles)) {
            throw new FileNotFoundException("Please create a directory with the name \"connection_profiles\" at the root directory" +
                    " of the jar file and add at least one connection profile to it.");
        }
    }

    public ConnectionProfileHandler(Path directoryOfProfiles) throws FileNotFoundException {
        this.directoryOfProfiles = directoryOfProfiles;

        if (!Files.exists(directoryOfProfiles)) {
            throw new FileNotFoundException("Please create a directory with the name \"connection_profiles\" at the root directory" +
                    " of the jar file and add at least one connection profile to it.");
        }
    }

    /**
     * Locate the root directory of the jar and check if the connection profile folder exists.
     * If not, an exception is thrown.
     *
     * @return path of connection profile directory
     * @throws URISyntaxException if creating the uri from the path of the jar fails
     */
    private static Path getConnectionProfileDirectory() throws URISyntaxException {
        Path path = Paths.get(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        return path.getParent().resolve("connection_profiles");
    }

    /**
     * List all files of the directory of profiles and return a connection profile if only one exists
     * else an exception is thrown.
     *
     * @return a connection profile of the profile file
     * @throws IOException                       if an I/O error occurs while reading the connection profile file.
     * @throws NoProfileException                if no file exist in directoryOfProfiles
     * @throws MultipleProfileException          if more than one file exist in directoryOfProfiles
     * @throws InvalidConnectionProfileException if the connection profile miss a property or has wrong syntax
     */
    public ConnectionProfile getDefaultProfile() throws NoProfileException, MultipleProfileException, IOException, InvalidConnectionProfileException {
        final List<Path> profiles = getListOfProfilesPath();

        final long fileCount = profiles.size();

        if (fileCount == 0) {
            throw new NoProfileException("There are no profile files located in '" + directoryOfProfiles + "'.");
        }
        if (fileCount > 1) {
            throw new MultipleProfileException("There are multiply profile files in '" + directoryOfProfiles + "'." +
                    "Please specify the profile that should be used with the option: " + ArgumentType.PROFILE);
        }

        return readProfileFile(profiles.getFirst());
    }

    /**
     * List all files of the directory of profiles
     *
     * @return String with file names of all profiles in directory
     * @throws IOException if an I/O error occurs while create a file list from directory.
     */
    public String getStringOfProfileList() throws IOException {
        final List<Path> profiles = getListOfProfilesPath();
        final StringBuilder profileList = new StringBuilder();
        for (final Path profile : profiles) {
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
     * @throws IOException if an I/O error occurs while create a file list from directory.
     */
    private List<Path> getListOfProfilesPath() throws IOException {
        try (Stream<Path> stream = Files.list(directoryOfProfiles)) {
            return stream
                    .filter(file -> !Files.isDirectory(file))
                    .toList();
        }
    }

    /**
     * Test if a file of the file name exist and create a connection profile
     *
     * @param fileName the file name of the connection profile
     * @return a connection profile object of the file
     * @throws IOException                       if an I/O error occurs while reading the connection profile file.
     * @throws InvalidConnectionProfileException if the connection profile miss a property or has wrong syntax
     */
    private ConnectionProfile getSelectedProfile(final String fileName) throws IOException, InvalidConnectionProfileException {
        final Path pathOfProfile = directoryOfProfiles.resolve(fileName);
        if (Files.exists(pathOfProfile) && !Files.isDirectory(pathOfProfile)) {
            return readProfileFile(pathOfProfile);
        }
        throw new FileNotFoundException("File '" + fileName + "' does not exist in '" +
                directoryOfProfiles + "'.");
    }

    /**
     * Read the connection profile file and filter for certain properties and pass this value to the ConnectionProfile
     *
     * @param pathOfProfile of read connection profile file
     * @return ConnectionProfile from the read connection profile file
     * @throws IOException                       if an I/O error occurs while reading the connection profile file.
     * @throws InvalidConnectionProfileException if the connection profile miss a property or has wrong syntax
     */
    private ConnectionProfile readProfileFile(final Path pathOfProfile) throws IOException, InvalidConnectionProfileException {
        final Properties configProperties = new Properties();
        try (InputStream stream = Files.newInputStream(pathOfProfile)) {
            configProperties.load(stream);
        }

        final Map<String, String> profileProperties = new HashMap<>();
        profileProperties.put("driver", configProperties.getProperty("driver"));
        profileProperties.put("driverClassName", configProperties.getProperty("driverClassName"));
        profileProperties.put("jarName", configProperties.getProperty("jarName"));
        profileProperties.put("host", configProperties.getProperty("host"));
        profileProperties.put("port", configProperties.getProperty("port"));
        profileProperties.put("user", configProperties.getProperty("user"));
        profileProperties.put("password", configProperties.getProperty("password"));
        profileProperties.put("database", configProperties.getProperty("database"));

        for (final Map.Entry<String, String> property : profileProperties.entrySet()) {
            if (property.getValue() == null) {
                throw new InvalidConnectionProfileException("\"" + property.getKey() + "\" is missing in the connection profile file or was not specified correctly.");
            }
        }

        return new ConnectionProfile(profileProperties.get("driver"), profileProperties.get("driverClassName"), profileProperties.get("jarName"), profileProperties.get("host"), profileProperties.get("port"),
                profileProperties.get("user"), profileProperties.get("password"), profileProperties.get("database"));
    }

    /**
     * Checks whether the --profile command is present
     * and creates a ConnectionProfile from the specified connection profile file.
     * If no --profile command is available, the default profile is searched for.
     *
     * @return ConnectionProfile from given file name or default profile
     * @throws IOException                       if an I/O error occurs while reading the connection profile file.
     * @throws NoProfileException                if no file exist in directoryOfProfiles
     * @throws MultipleProfileException          if more than one file exist in directoryOfProfiles
     * @throws InvalidConnectionProfileException if the connection profile miss a property or has wrong syntax
     */
    public ConnectionProfile getConnectionProfile(final String[] args) throws IOException, NoProfileException, MultipleProfileException, InvalidConnectionProfileException {
        if (Arrays.asList(args).contains(ArgumentType.PROFILE.toString())) {
            final int indexProfileArgument = Arrays.asList(args).indexOf(ArgumentType.PROFILE.toString()) + 1;
            final String profileArgument = args[indexProfileArgument];
            return getSelectedProfile(profileArgument);
        } else {
            return getDefaultProfile();
        }
    }
}
