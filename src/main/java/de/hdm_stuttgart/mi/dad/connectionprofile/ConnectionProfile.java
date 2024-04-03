package de.hdm_stuttgart.mi.dad.connectionprofile;

public class ConnectionProfile {
    private final String driver;
    private final String host;
    private final String port;
    private final String user;
    private final String password;
    private final String database;

    public ConnectionProfile(String driver, String host, String port, String user, String password, String dbName) {
        this.driver = driver;
        this.host = host;
        this.user = user;
        this.password = password;
        this.database = dbName;
        this.port = port;
    }

    public String getDriver() {
        return driver;
    }

    public String getHost() {
        return host;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getDatabase() {
        return database;
    }

    public String getPort() {
        return port;
    }

}
