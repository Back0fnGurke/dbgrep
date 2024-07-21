package de.hdm_stuttgart.mi.dad.core.exception;

/**
 * The ValidationException class extends the ServiceException class and represents an exception that is thrown when a validation fails.
 * <p>
 * The class has a constructor that takes a String representing the invalid value that caused the exception and a message describing the exception.
 * <p>
 * The class provides a method getInvalidValue to retrieve the invalid value that caused the exception.
 * <p>
 * Example usage:
 * <p>
 * try {
 * // some validation logic
 * } catch (ValidationException e) {
 * System.out.println("Invalid value: " + e.getInvalidValue());
 * System.out.println("Error message: " + e.getMessage());
 * }
 */
public class ValidationException extends ServiceException {

    private final String invalidValue;

    /**
     * Constructor for the ValidationException class.
     *
     * @param invalidValue the invalid value that caused the exception.
     * @param message      the message describing the exception.
     */
    public ValidationException(final String invalidValue, final String message) {
        super(message);
        this.invalidValue = invalidValue;
    }

    /**
     * Returns the invalid value that caused the exception.
     *
     * @return the invalid value that caused the exception.
     */
    public String getInvalidValue() {
        return invalidValue;
    }
}
