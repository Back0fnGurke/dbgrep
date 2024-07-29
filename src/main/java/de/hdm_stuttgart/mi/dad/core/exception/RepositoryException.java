package de.hdm_stuttgart.mi.dad.core.exception;

import java.sql.SQLException;

/**
 * The RepositoryException class extends the RuntimeException class and represents an exception that can occur in the repository layer.
 * <p>
 * This exception is thrown when there is an issue with the database operation, such as a SQL exception.
 * <p>
 * The class has a constructor that takes a SQLException object, which represents the original exception that occurred during the database operation.
 * <p>
 * Example usage:
 * <p>
 * try {
 * // some database operation
 * } catch (SQLException e) {
 * throw new RepositoryException(e);
 * }
 */
public class RepositoryException extends RuntimeException {

    /**
     * Constructor for the RepositoryException class.
     *
     * @param e the SQLException that caused this exception.
     */
    public RepositoryException(SQLException e) {
        super(e);
    }
}
