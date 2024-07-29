package de.hdm_stuttgart.mi.dad.core.exception;

/**
 * The ServiceException class extends the Exception class and represents a general exception that can occur in the service layer.
 * <p>
 * The class has two constructors:
 * - One that takes a String representing the message describing the exception.
 * - Another that takes a String representing the message and a Throwable representing the cause of the exception.
 * <p>
 * Example usage:
 * <p>
 * try {
 * // some service logic
 * } catch (ServiceException e) {
 * System.out.println("Error message: " + e.getMessage());
 * e.printStackTrace();
 * }
 */
public class ServiceException extends Exception {

    /**
     * Constructor for the ServiceException class.
     *
     * @param message the message describing the exception.
     */
    public ServiceException(String message) {
        super(message);
    }

    /**
     * Constructor for the ServiceException class.
     *
     * @param message the message describing the exception.
     * @param cause   the cause of the exception.
     */
    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
