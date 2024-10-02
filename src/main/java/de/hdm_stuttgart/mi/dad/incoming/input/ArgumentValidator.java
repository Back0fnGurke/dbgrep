package de.hdm_stuttgart.mi.dad.incoming.input;

public class ArgumentValidator {

    private ArgumentValidator() {
    }

    /**
     * Validates the given command-line arguments and prints warnings for invalid arguments.
     *
     * @param args the command-line arguments
     */
    public static void validateArguments(final String[] args) {
        StringBuilder validationErrorMessages = new StringBuilder();
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
            }
        }
        System.out.println(validationErrorMessages);
    }
}
