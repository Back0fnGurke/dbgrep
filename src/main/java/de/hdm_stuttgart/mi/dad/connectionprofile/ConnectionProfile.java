package de.hdm_stuttgart.mi.dad.connectionprofile;

/**
 * Store connection profile data.
 */
public record ConnectionProfile(String driver, String driverClassName, String jarName, String host, String port,
                                String user,
                                String password,
                                String database) {
}
