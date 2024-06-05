package de.hdm_stuttgart.mi.dad.core.exception;

//TODO: doku

public class IllegalTableNameException extends ValidationException {

    public IllegalTableNameException(final String invalidValue, final String message) {
        super(invalidValue, message);
    }
}
