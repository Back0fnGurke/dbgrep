package de.hdm_stuttgart.mi.dad.incoming.input;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The `ArgumentValidator` class provides methods to validate command-line arguments.
 */
public class ArgumentValidator {

    private static final Logger log = LoggerFactory.getLogger(ArgumentValidator.class);

    private ArgumentValidator() {
    }

    /**
     * Validates the given command-line arguments and prints warnings for invalid arguments.
     *
     * @param args the command-line arguments
     * @return true if there are invalid arguments, false otherwise
     */
    public static boolean isValidArguments(final String[] args) {
        log.debug("Called with args: {}", (Object) args);
        final StringBuilder validationErrorMessages = new StringBuilder();
        for (final String argument : args) {
            if (!argument.startsWith("--")) {
                continue;
            }

            boolean isNotValid = true;

            for (final ArgumentType argumentType : ArgumentType.values()) {
                if (argumentType.argumentString.equals(argument)) {
                    isNotValid = false;
                    break;
                }
            }

            if (isNotValid) {
                validationErrorMessages.append("Warning! ").append(argument).append(" is not a valid argument. Use --help to see all arguments. \n");
                log.debug("Returning: true with message: {}", validationErrorMessages);
                System.out.println(validationErrorMessages);
                return true;
            }
        }
        log.debug("Returning: false");
        System.out.println(validationErrorMessages);
        return false;
    }
}