package de.hdm_stuttgart.mi.dad.driverloader;

import de.hdm_stuttgart.mi.dad.connectionprofile.ConnectionProfile;

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
 */
public class DriverLoader {

    private DriverLoader() {
    }

    /**
     * Loads and registers a JDBC driver specified in the given {@link ConnectionProfile}.
     *
     * @param connectionProfile the {@link ConnectionProfile} containing the driver details.
     * @throws RuntimeException if there is an error loading or registering the drivers
     */
    public static void loadDriver(final ConnectionProfile connectionProfile) throws NoSuchMethodException, ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException, SQLException, MalformedURLException {
        final Path path = Paths.get(connectionProfile.pathToDriverJar());
        final URLClassLoader classLoader = new URLClassLoader(new URL[]{path.toUri().toURL()});
        final Class<?> driverClass = Class.forName(connectionProfile.driverClassName(), true, classLoader);
        final Driver driverInstance = (Driver) driverClass.getDeclaredConstructor().newInstance();

        DriverManager.registerDriver(new DriverShim(driverInstance));
    }
}
