package de.hdm_stuttgart.mi.dad.connectionprofile.exception;

/**
 * Exception thrown when no profile is found.
 */
public class NoProfileException extends Exception {

    /**
     * Constructs a new NoProfileException with the specified detail message.
     *
     * @param message the detail message
     */
    public NoProfileException(String message) {
        super(message);
    }
}
