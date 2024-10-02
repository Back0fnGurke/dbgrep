package de.hdm_stuttgart.mi.dad.incoming.input.connectionprofile;

/**
 * Store connection profile data.
 */
public record ConnectionProfile(String driver, String host, String port, String user, String password,
                                String database) {
}
