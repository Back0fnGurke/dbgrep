package de.hdm_stuttgart.mi.dad.connectionprofile;

/**
 * Store connection profile data.
 *
 * @param driver          the database driver
 * @param driverClassName the class name of the database driver
 * @param pathToDriverJar the path to the driver JAR file
 * @param host            the database host
 * @param port            the database port
 * @param user            the database user
 * @param password        the database password
 * @param database        the database name
 */
public record ConnectionProfile(String driver, String driverClassName, String pathToDriverJar, String host, String port,
                                String user,
                                String password,
                                String database) {
}
