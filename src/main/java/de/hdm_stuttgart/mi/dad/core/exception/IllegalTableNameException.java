package de.hdm_stuttgart.mi.dad.core.exception;

public class IllegalTableNameException extends ValidationException {

    public IllegalTableNameException(final String invalidValue, final String message) {
        super(invalidValue, message);
    }
}
