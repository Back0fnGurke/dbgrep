package de.hdm_stuttgart.mi.dad.driverloader;

import de.hdm_stuttgart.mi.dad.connectionprofile.ConnectionProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * The `DriverLoader` class is responsible for loading and registering JDBC drivers dynamically at runtime.
 * It uses the details provided in a {@link ConnectionProfile} to locate and load the driver JAR file,
 * instantiate the driver class, and register it with the {@link DriverManager}.
 */
public class DriverLoader {

    private static final Logger log = LoggerFactory.getLogger(DriverLoader.class);

    private DriverLoader() {
    }

    /**
     * Loads and registers a JDBC driver specified in the given {@link ConnectionProfile}.
     * <p>This method performs the following steps:</p>
     * <ol>
     *     <li>Validates the existence of the driver JAR file specified in the connection profile.</li>
     *     <li>Creates a new {@link URLClassLoader} to load the driver class from the JAR file.</li>
     *     <li>Instantiates the driver class using reflection.</li>
     *     <li>Registers the driver instance with the {@link DriverManager}.</li>
     * </ol>
     *
     * @param connectionProfile the {@link ConnectionProfile} containing the driver details.
     * @throws NoSuchMethodException     if the driver class does not have a default constructor.
     * @throws ClassNotFoundException    if the driver class cannot be found in the JAR file.
     * @throws InvocationTargetException if the constructor of the driver class throws an exception.
     * @throws InstantiationException    if the driver class cannot be instantiated.
     * @throws IllegalAccessException    if the default constructor of the driver class is not accessible.
     * @throws SQLException              if a database access error occurs.
     * @throws MalformedURLException     if the path to the driver JAR file is not a valid URL.
     * @throws FileNotFoundException     if the driver JAR file does not exist.
     */
    public static void loadDriver(final ConnectionProfile connectionProfile) throws NoSuchMethodException, ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException, SQLException, MalformedURLException, FileNotFoundException {
        log.debug("Called with connectionProfile: {}", connectionProfile);
        final Path path = Paths.get(connectionProfile.pathToDriverJar());
        if (!path.toFile().exists()) {
            log.error("Driver JAR file not found: {}", path);
            throw new FileNotFoundException("Driver JAR file not found: " + path);
        }
        final URLClassLoader classLoader = new URLClassLoader(new URL[]{path.toUri().toURL()});
        final Class<?> driverClass = Class.forName(connectionProfile.driverClassName(), true, classLoader);
        final Driver driverInstance = (Driver) driverClass.getDeclaredConstructor().newInstance();

        DriverManager.registerDriver(new DriverShim(driverInstance));
        log.debug("Driver loaded and registered successfully: {}", driverClass.getName());
    }
}