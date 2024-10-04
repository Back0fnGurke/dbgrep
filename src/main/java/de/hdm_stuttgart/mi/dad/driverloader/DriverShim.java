package de.hdm_stuttgart.mi.dad.driverloader;

import java.sql.*;
import java.util.Properties;

/**
 * The `DriverShim` class is a wrapper for a dynamically loaded JDBC driver.
 * It implements the {@link Driver} interface and delegates all method calls to the wrapped driver instance.
 */
class DriverShim implements Driver {
    private final Driver driver;


    /**
     * Constructs a new `DriverShim` with the specified driver.
     *
     * @param driver the {@link Driver} to be wrapped.
     */
    DriverShim(Driver driver) {
        this.driver = driver;
    }

    @Override
    public Connection connect(String url, Properties info) throws SQLException {
        return driver.connect(url, info);
    }

    @Override
    public boolean acceptsURL(String url) throws SQLException {
        return driver.acceptsURL(url);
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
        return driver.getPropertyInfo(url, info);
    }

    @Override
    public int getMajorVersion() {
        return driver.getMajorVersion();
    }

    @Override
    public int getMinorVersion() {
        return driver.getMinorVersion();
    }

    @Override
    public boolean jdbcCompliant() {
        return driver.jdbcCompliant();
    }

    @Override
    public java.util.logging.Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return driver.getParentLogger();
    }
}
