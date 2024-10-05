package de.hdm_stuttgart.mi.dad.connectionprofile.exception;

/**
 * Exception thrown when an invalid connection profile is encountered.
 */
public class InvalidConnectionProfileException extends Exception {

    /**
     * Constructs a new InvalidConnectionProfileException with the specified detail message.
     *
     * @param message the detail message
     */
    public InvalidConnectionProfileException(String message) {
        super(message);
    }
}
