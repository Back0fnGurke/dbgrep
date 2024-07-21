package de.hdm_stuttgart.mi.dad;

import de.hdm_stuttgart.mi.dad.core.ports.RepositoryPort;
import de.hdm_stuttgart.mi.dad.outgoing.RepositoryFactory;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;

class TestRepositoryFactory {

    @Test
    void testCreatePostgresRepository() {
        Connection connection = mock(Connection.class);
        RepositoryPort repository = RepositoryFactory.createRepository(connection, "postgresql");
        assertNotNull(repository);
    }

    @Test
    void testCreateMySQLRepository() {
        Connection connection = mock(Connection.class);
        RepositoryPort repository = RepositoryFactory.createRepository(connection, "mysql");
        assertNotNull(repository);
    }

    @Test
    void testCreateUnsupportedRepository() {
        Connection connection = mock(Connection.class);
        RepositoryPort repository = RepositoryFactory.createRepository(connection, "unsupported");
        assertNull(repository);
    }
}
