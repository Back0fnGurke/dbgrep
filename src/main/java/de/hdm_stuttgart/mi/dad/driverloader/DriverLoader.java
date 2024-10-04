package de.hdm_stuttgart.mi.dad.driverloader;

import de.hdm_stuttgart.mi.dad.Main;
import de.hdm_stuttgart.mi.dad.connectionprofile.ConnectionProfile;

import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DriverLoader {

    private final Path driverDirectory;

    public DriverLoader() throws URISyntaxException, FileNotFoundException {
        this.driverDirectory = getDriverDirectory();

        if (!Files.exists(driverDirectory)) {
            throw new FileNotFoundException("Please create a directory with the name \"drivers\" at the root directory" +
                    " of the jar file and add at least one driver jar to it.");
        }
    }

    /**
     * Locate the root directory of the jar and check if the driver folder exists.
     * If not, an exception is thrown.
     *
     * @return path of driver directory
     * @throws URISyntaxException if creating the uri from the path of the jar fails
     */
    private static Path getDriverDirectory() throws URISyntaxException {
        Path path = Paths.get(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        return path.getParent().resolve("drivers");
    }

    public void loadDriver(final ConnectionProfile connectionProfile) {
        try {
            final URL url = driverDirectory.resolve(connectionProfile.jarName()).toUri().toURL();
            final URLClassLoader classLoader = new URLClassLoader(new URL[]{url});
            final Class<?> driverClass = Class.forName(connectionProfile.driverClassName(), true, classLoader);
            final Driver driverInstance = (Driver) driverClass.getDeclaredConstructor().newInstance();

            DriverManager.registerDriver(new DriverShim(driverInstance));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException | IllegalAccessException | NoSuchMethodException | InstantiationException |
                 InvocationTargetException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
