package de.hdm_stuttgart.mi.dad;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(final String[] args) {
        System.out.println(args[0]);

//        final ConnectionProfile connectionProfile = new ConnectionProfile("postgresql", "127.0.0.1", "5432", "test", "test", "test");
//
//        final String url = String.format("jdbc:%s://%s:%s/%s", connectionProfile.getDriver(), connectionProfile.getHost(), connectionProfile.getPort(), connectionProfile.getDatabase());
//
//        try (final Connection connection = DriverManager.getConnection(url, connectionProfile.getUser(), connectionProfile.getPassword())) {
//
//            final RepositoryPort repository = RepositoryFactory.newRepository(connection, connectionProfile.getDriver());
//            final ServicePort service = new Service(repository);
//            final OptionHandler optionHandler = new OptionHandler(service);
//
//            optionHandler.handleInput(args);
//        } catch (final Exception exception) {
//            log.error(exception.getMessage());
//        }
    }
}
