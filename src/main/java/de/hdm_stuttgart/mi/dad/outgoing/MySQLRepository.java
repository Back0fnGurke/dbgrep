package de.hdm_stuttgart.mi.dad.outgoing;

import de.hdm_stuttgart.mi.dad.ports.RepositoryPort;

import java.sql.Connection;

public class MySQLRepository implements RepositoryPort {

    final Connection connection;

    public MySQLRepository(final Connection connection) {
        this.connection = connection;
    }

    @Override
    public void findInWholeDatabase() {

    }

    @Override
    public void findInColumn() {

    }

    @Override
    public void findInTable() {

    }
}
