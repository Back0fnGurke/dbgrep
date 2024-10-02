package de.hdm_stuttgart.mi.dad.incoming.input.connectionprofile.exception;

/**
 * Used for cases such as when a property is missing or misspelled in the connection profile.
 */
public class InvalidConnectionProfileException extends Exception {

    public InvalidConnectionProfileException(String message) {
        super(message);
    }
}
