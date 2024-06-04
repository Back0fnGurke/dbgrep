package de.hdm_stuttgart.mi.dad.core.exception;

//TODO: doku

public class ValidationException extends ServiceException {

    private final String invalidValue;

    public ValidationException(final String invalidValue, final String message) {
        super(message);
        this.invalidValue = invalidValue;
    }

    public String getInvalidValue() {
        return invalidValue;
    }
}
