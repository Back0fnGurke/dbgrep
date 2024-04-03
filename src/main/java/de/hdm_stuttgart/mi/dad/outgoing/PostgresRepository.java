package de.hdm_stuttgart.mi.dad.outgoing;

import de.hdm_stuttgart.mi.dad.ports.RepositoryPort;

import java.sql.Connection;

public class PostgresRepository implements RepositoryPort {

    final Connection connection;

    public PostgresRepository(final Connection connection) {
        this.connection = connection;
    }

    @Override
    public void findData() {
        // do stuff
    }
}
