package de.hdm_stuttgart.mi.dad.core.exception;

import java.sql.SQLException;

//TODO: doku

public class RepositoryException extends RuntimeException {

    public RepositoryException(SQLException e) {
        super(e);
    }
}
