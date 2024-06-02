package de.hdm_stuttgart.mi.dad.core.exception;

public class IllegalPropertyCombinationException extends ValidationException {

    public IllegalPropertyCombinationException(final String invalidValue, final String message) {
        super(invalidValue, message);
    }
}
