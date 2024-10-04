package de.hdm_stuttgart.mi.dad;

import de.hdm_stuttgart.mi.dad.connectionprofile.ConnectionProfile;
import de.hdm_stuttgart.mi.dad.driverloader.DriverLoader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

import static org.junit.jupiter.api.Assertions.*;

class TestDriverLoader {

    @BeforeEach
    void deregisterAllDrivers() throws SQLException {
        // Deregister all drivers to prevent interference from the classpath drivers
        final Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            DriverManager.deregisterDriver(drivers.nextElement());
        }
    }

    @AfterEach
    void resetDrivers() throws SQLException {
        // Ensure all drivers are deregistered after each test
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            DriverManager.deregisterDriver(drivers.nextElement());
        }
    }

    @Test
    void testLoadDriver_ShouldRegisterDriver() throws Exception {
        // given
        assertFalse(DriverManager.getDrivers().hasMoreElements());
        final ConnectionProfile connectionProfile = new ConnectionProfile(
                "postgresql",
                "org.postgresql.Driver",
                "src/test/resources/TestDriverLoader/postgresql-42.7.4.jar",
                "localhost",
                "5432",
                "testdb",
                "",
                ""
        );

        // when
        DriverLoader.loadDriver(connectionProfile);

        // then
        Driver driver = DriverManager.getDriver("jdbc:postgresql://localhost:5432/testdb");
        assertNotNull(driver, "The PostgreSQL driver should have been registered");
    }

    @Test
    void testLoadDriver_WithInvalidDriverPath_ShouldThrowMalformedURLException() {
        // given
        final ConnectionProfile connectionProfile = new ConnectionProfile(
                "postgresql",
                "org.postgresql.Driver",
                "invalid/path/to/driver.jar",
                "localhost",
                "5432",
                "testdb",
                "",
                ""
        );

        // then
        assertThrows(FileNotFoundException.class, () -> DriverLoader.loadDriver(connectionProfile));
    }

    @Test
    void testLoadDriver_WithInvalidDriverClassName_ShouldThrowClassNotFoundException() {
        // given
        final ConnectionProfile connectionProfile = new ConnectionProfile(
                "postgresql",
                "invalid.DriverClass",
                "src/test/resources/TestDriverLoader/postgresql-42.7.4.jar",
                "localhost",
                "5432",
                "testdb",
                "",
                ""
        );

        // then
        assertThrows(ClassNotFoundException.class, () -> DriverLoader.loadDriver(connectionProfile));
    }
}
