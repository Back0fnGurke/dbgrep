package de.hdm_stuttgart.mi.dad.incoming.input.connectionprofile.exception;

/**
 * Is thrown if a default profile is searched for and there is no file in the folder.
 */
public class NoProfileException extends Exception {
    public NoProfileException(String message) {
        super(message);
    }
}
