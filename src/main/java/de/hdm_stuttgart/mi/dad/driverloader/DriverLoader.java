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

public class DriverLoader {

    public static void loadDriver(final ConnectionProfile connectionProfile) {
        try {
            final Path path = Paths.get(connectionProfile.pathToDriverJar());
            final URLClassLoader classLoader = new URLClassLoader(new URL[]{path.toUri().toURL()});
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
