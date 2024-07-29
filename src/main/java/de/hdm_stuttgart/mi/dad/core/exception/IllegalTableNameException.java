package de.hdm_stuttgart.mi.dad.core.exception;

/**
 * The IllegalTableNameException class extends the ValidationException class and represents an exception that is thrown when an invalid table name is encountered.
 * <p>
 * The class has a constructor that takes a String representing the invalid table name that caused the exception and a message describing the exception.
 * <p>
 * Example usage:
 * <p>
 * try {
 * // some validation logic for table name
 * } catch (IllegalTableNameException e) {
 * System.out.println("Invalid table name: " + e.getInvalidValue());
 * System.out.println("Error message: " + e.getMessage());
 * }
 */
public class IllegalTableNameException extends ValidationException {

    /**
     * Constructor for the IllegalTableNameException class.
     *
     * @param invalidValue the invalid table name that caused the exception.
     * @param message      the message describing the exception.
     */
    public IllegalTableNameException(final String invalidValue, final String message) {
        super(invalidValue, message);
    }
}
