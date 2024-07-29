package de.hdm_stuttgart.mi.dad;

import de.hdm_stuttgart.mi.dad.core.ports.RepositoryPort;
import de.hdm_stuttgart.mi.dad.outgoing.RepositoryFactory;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class TestRepositoryFactory {

    @Test
    void testCreatePostgresRepository() throws IOException {
        final RepositoryPort repository = RepositoryFactory.createRepository(null, "postgresql");
        assertNotNull(repository);
    }

    @Test
    void testCreateMySQLRepository() throws IOException {
        final RepositoryPort repository = RepositoryFactory.createRepository(null, "mysql");
        assertNotNull(repository);
    }

    @Test
    void testCreateUnsupportedRepository() throws IOException {
        final RepositoryPort repository = RepositoryFactory.createRepository(null, "unsupported");
        assertNull(repository);
    }
}
