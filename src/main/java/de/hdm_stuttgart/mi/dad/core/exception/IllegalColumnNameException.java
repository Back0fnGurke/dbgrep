package de.hdm_stuttgart.mi.dad.core.exception;

public class IllegalColumnNameException extends ValidationException {

    public IllegalColumnNameException(final String invalidValue, final String message) {
        super(invalidValue, message);
    }
}
