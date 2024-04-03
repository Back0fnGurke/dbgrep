package de.hdm_stuttgart.mi.dad.core;

import de.hdm_stuttgart.mi.dad.ports.RepositoryPort;
import de.hdm_stuttgart.mi.dad.ports.ServicePort;

public class Service implements ServicePort {

    final RepositoryPort repository;

    public Service(final RepositoryPort repository) {
        this.repository = repository;
    }

    public void handle() {
        repository.findInColumn();
    }
}
