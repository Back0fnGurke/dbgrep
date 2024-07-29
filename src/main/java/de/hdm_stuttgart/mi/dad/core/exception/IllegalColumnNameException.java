package de.hdm_stuttgart.mi.dad.core.exception;

/**
 * The IllegalColumnNameException class extends the ValidationException class and represents an exception that is thrown when an invalid column name is encountered.
 * <p>
 * The class has a constructor that takes a String representing the invalid column name that caused the exception and a message describing the exception.
 * <p>
 * Example usage:
 * <p>
 * try {
 * // some validation logic for column name
 * } catch (IllegalColumnNameException e) {
 * System.out.println("Invalid column name: " + e.getInvalidValue());
 * System.out.println("Error message: " + e.getMessage());
 * }
 */
public class IllegalColumnNameException extends ValidationException {

    /**
     * Constructor for the IllegalColumnNameException class.
     *
     * @param invalidValue the invalid column name that caused the exception.
     * @param message      the message describing the exception.
     */
    public IllegalColumnNameException(final String invalidValue, final String message) {
        super(invalidValue, message);
    }
}
