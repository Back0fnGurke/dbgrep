package de.hdm_stuttgart.mi.dad.incoming.input;

/**
 * The `ArgumentValidator` class provides methods to validate command-line arguments.
 */
public class ArgumentValidator {

    private ArgumentValidator() {
    }

    /**
     * Validates the given command-line arguments and prints warnings for invalid arguments.
     *
     * @param args the command-line arguments
     * @return true if there are invalid arguments, false otherwise
     */
    public static boolean isValidArguments(final String[] args) {
        final StringBuilder validationErrorMessages = new StringBuilder();
        for (String argument : args) {
            if (!argument.startsWith("--")) {
                continue;
            }

            boolean isNotValid = true;

            for (ArgumentType argumentType : ArgumentType.values()) {
                if (argumentType.argumentString.equals(argument)) {
                    isNotValid = false;
                    break;
                }
            }

            if (isNotValid) {
                validationErrorMessages.append("Warning! ").append(argument).append(" is not a valid argument. Use --help to see all arguments. \n");
                return true;
            }
        }
        System.out.println(validationErrorMessages);
        return false;
    }
}
