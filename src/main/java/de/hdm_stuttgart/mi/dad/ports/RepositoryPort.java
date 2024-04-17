package de.hdm_stuttgart.mi.dad.ports;

public interface RepositoryPort {
    void findInWholeDatabase();

    void findInColumn();

    void findInTable();
}