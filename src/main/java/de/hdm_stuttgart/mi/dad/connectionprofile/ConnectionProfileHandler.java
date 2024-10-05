package de.hdm_stuttgart.mi.dad.connectionprofile;

import de.hdm_stuttgart.mi.dad.Main;
import de.hdm_stuttgart.mi.dad.connectionprofile.exception.InvalidConnectionProfileException;
import de.hdm_stuttgart.mi.dad.connectionprofile.exception.MultipleProfileException;
import de.hdm_stuttgart.mi.dad.connectionprofile.exception.NoProfileException;
import de.hdm_stuttgart.mi.dad.incoming.input.ArgumentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
 * The `ConnectionProfileHandler` class is responsible for locating and reading connection profile files.
 * It provides methods to retrieve a default profile, a specific profile, or a list of available profiles.
 * The connection profiles are expected to be located in a directory named "connection_profiles" at the root of the jar file.
 */
public class ConnectionProfileHandler {

    private static final Logger log = LoggerFactory.getLogger(ConnectionProfileHandler.class);

    private final Path directoryOfProfiles;

    /**
     * Constructs a `ConnectionProfileHandler` and initializes the directory of profiles.
     *
     * @throws FileNotFoundException if the directory of profiles does not exist
     * @throws URISyntaxException    if creating the URI from the path of the jar fails
     */
    public ConnectionProfileHandler() throws FileNotFoundException, URISyntaxException {
        this.directoryOfProfiles = getConnectionProfileDirectory();

        if (!Files.exists(directoryOfProfiles)) {
            throw new FileNotFoundException("Please create a directory with the name \"connection_profiles\" at the root directory" +
                    " of the jar file and add at least one connection profile to it.");
        }
    }

    /**
     * Constructs a `ConnectionProfileHandler` with a specified directory of profiles.
     *
     * @param directoryOfProfiles the path to the directory of profiles
     * @throws FileNotFoundException if the directory of profiles does not exist
     */
    public ConnectionProfileHandler(final Path directoryOfProfiles) throws FileNotFoundException {
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
     * @throws URISyntaxException if creating the URI from the path of the jar fails
     */
    private static Path getConnectionProfileDirectory() throws URISyntaxException {
        log.debug("Called getConnectionProfileDirectory");
        final Path path = Paths.get(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        final Path result = path.getParent().resolve("connection_profiles");
        log.debug("Returning: {}", result);
        return result;
    }

    /**
     * List all files of the directory of profiles and return a connection profile if only one exists,
     * else an exception is thrown.
     *
     * @return a connection profile of the profile file
     * @throws IOException                       if an I/O error occurs while reading the connection profile file
     * @throws NoProfileException                if no file exists in directoryOfProfiles
     * @throws MultipleProfileException          if more than one file exists in directoryOfProfiles
     * @throws InvalidConnectionProfileException if the connection profile misses a property or has wrong syntax
     */
    public ConnectionProfile getDefaultProfile() throws NoProfileException, MultipleProfileException, IOException, InvalidConnectionProfileException {
        log.debug("Called getDefaultProfile");
        final List<Path> profiles = getListOfProfilesPath();

        final long fileCount = profiles.size();

        if (fileCount == 0) {
            throw new NoProfileException("There are no profile files located in '" + directoryOfProfiles + "'.");
        }
        if (fileCount > 1) {
            throw new MultipleProfileException("There are multiply profile files in '" + directoryOfProfiles + "'." +
                    "Please specify the profile that should be used with the option: " + ArgumentType.PROFILE);
        }

        final ConnectionProfile result = readProfileFile(profiles.getFirst());
        log.debug("Returning: {}", result);
        return result;
    }

    /**
     * List all files of the directory of profiles.
     *
     * @return String with file names of all profiles in directory
     * @throws IOException if an I/O error occurs while creating a file list from directory
     */
    public String getStringOfProfileList() throws IOException {
        log.debug("Called getStringOfProfileList");
        final List<Path> profiles = getListOfProfilesPath();
        final StringBuilder profileList = new StringBuilder();
        for (final Path profile : profiles) {
            profileList.append(profile.getFileName().toString());
            profileList.append("\n");
        }
        final String result = profileList.toString();
        log.debug("Returning: {}", result);
        return result;
    }

    /**
     * Returns a list of paths from the existing profiles.
     * These are located in the specified directory.
     *
     * @return a list of paths
     * @throws IOException if an I/O error occurs while creating a file list from directory
     */
    private List<Path> getListOfProfilesPath() throws IOException {
        log.debug("Called getListOfProfilesPath");
        try (Stream<Path> stream = Files.list(directoryOfProfiles)) {
            final List<Path> result = stream
                    .filter(file -> !Files.isDirectory(file))
                    .toList();
            log.debug("Returning: {}", result);
            return result;
        }
    }

    /**
     * Test if a file of the file name exists and create a connection profile.
     *
     * @param fileName the file name of the connection profile
     * @return a connection profile object of the file
     * @throws IOException                       if an I/O error occurs while reading the connection profile file
     * @throws InvalidConnectionProfileException if the connection profile misses a property or has wrong syntax
     */
    public ConnectionProfile getSelectedProfile(final String fileName) throws IOException, InvalidConnectionProfileException {
        log.debug("Called getSelectedProfile with fileName: {}", fileName);
        final Path pathOfProfile = directoryOfProfiles.resolve(fileName);
        if (Files.exists(pathOfProfile) && !Files.isDirectory(pathOfProfile)) {
            final ConnectionProfile result = readProfileFile(pathOfProfile);
            log.debug("Returning: {}", result);
            return result;
        }
        throw new FileNotFoundException("File '" + fileName + "' does not exist in '" +
                directoryOfProfiles + "'.");
    }

    /**
     * Read the connection profile file and filter for certain properties and pass this value to the ConnectionProfile.
     *
     * @param pathOfProfile the path of the connection profile file
     * @return ConnectionProfile from the read connection profile file
     * @throws IOException                       if an I/O error occurs while reading the connection profile file
     * @throws InvalidConnectionProfileException if the connection profile misses a property or has wrong syntax
     */
    private ConnectionProfile readProfileFile(final Path pathOfProfile) throws IOException, InvalidConnectionProfileException {
        log.debug("Called readProfileFile with pathOfProfile: {}", pathOfProfile);
        final Properties configProperties = new Properties();
        try (InputStream stream = Files.newInputStream(pathOfProfile)) {
            configProperties.load(stream);
        }

        final Map<String, String> profileProperties = new HashMap<>();
        profileProperties.put("driver", configProperties.getProperty("driver"));
        profileProperties.put("driverClassName", configProperties.getProperty("driverClassName"));
        profileProperties.put("pathToDriverJar", configProperties.getProperty("pathToDriverJar"));
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

        final ConnectionProfile result = new ConnectionProfile(profileProperties.get("driver"), profileProperties.get("driverClassName"), profileProperties.get("pathToDriverJar"), profileProperties.get("host"), profileProperties.get("port"),
                profileProperties.get("user"), profileProperties.get("password"), profileProperties.get("database"));
        log.debug("Returning: {}", result);
        return result;
    }

    /**
     * Checks whether the --profile command is present and creates a ConnectionProfile from the specified connection profile file.
     * If no --profile command is available, the default profile is searched for.
     *
     * @param args the command-line arguments
     * @return ConnectionProfile from given file name or default profile
     * @throws IOException                       if an I/O error occurs while reading the connection profile file
     * @throws NoProfileException                if no file exists in directoryOfProfiles
     * @throws MultipleProfileException          if more than one file exists in directoryOfProfiles
     * @throws InvalidConnectionProfileException if the connection profile misses a property or has wrong syntax
     */
    public ConnectionProfile getConnectionProfile(final String[] args) throws IOException, NoProfileException, MultipleProfileException, InvalidConnectionProfileException {
        log.debug("Called getConnectionProfile with args: {}", (Object) args);
        if (Arrays.asList(args).contains(ArgumentType.PROFILE.toString())) {
            final int indexProfileArgument = Arrays.asList(args).indexOf(ArgumentType.PROFILE.toString()) + 1;
            final String profileArgument = args[indexProfileArgument];
            final ConnectionProfile result = getSelectedProfile(profileArgument);
            log.debug("Returning: {}", result);
            return result;
        } else {
            final ConnectionProfile result = getDefaultProfile();
            log.debug("Returning: {}", result);
            return result;
        }
    }
}