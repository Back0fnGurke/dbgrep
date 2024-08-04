package de.hdm_stuttgart.mi.dad.connectionprofile.exception;

/**
 * Is thrown if a default profile is searched for and there are several files in the folder.
 */
public class MultipleProfileException extends Exception {
    public MultipleProfileException(String message) {
        super(message);
    }
}
