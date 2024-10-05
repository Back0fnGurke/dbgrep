package de.hdm_stuttgart.mi.dad.connectionprofile.exception;

/**
 * Exception thrown when multiple profiles are encountered.
 */
public class MultipleProfileException extends Exception {

    /**
     * Constructs a new MultipleProfileException with the specified detail message.
     *
     * @param message the detail message
     */
    public MultipleProfileException(String message) {
        super(message);
    }
}
