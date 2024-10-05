package de.hdm_stuttgart.mi.dad.core.exception;

/**
 * Exception thrown when validation fails.
 */
public class ValidationException extends ServiceException {

    /**
     * The invalid value that caused the exception.
     */
    private final String invalidValue;

    /**
     * Constructs a new ValidationException with the specified detail message and invalid value.
     *
     * @param message      the detail message
     * @param invalidValue the invalid value
     */
    public ValidationException(final String invalidValue, final String message) {
        super(message);
        this.invalidValue = invalidValue;
    }

    /**
     * Returns the invalid value that caused the exception.
     *
     * @return the invalid value
     */
    public String getInvalidValue() {
        return invalidValue;
    }
}
