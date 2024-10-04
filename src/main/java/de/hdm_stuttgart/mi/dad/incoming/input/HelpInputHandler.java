package de.hdm_stuttgart.mi.dad.incoming.input;

import java.io.*;
import java.util.Arrays;

/**
 * The `HelpInputHandler` class provides methods to handle the `--help` command-line argument.
 */
public class HelpInputHandler {

    private HelpInputHandler() {
    }

    /**
     * Handles the `--help` command-line argument by printing the manual.
     *
     * @param args the command-line arguments
     * @return true if the `--help` argument is present, false otherwise
     * @throws IOException if an I/O error occurs while reading the manual
     */
    public static boolean handleHelp(final String[] args) throws IOException {
        if (hasArgument(args, ArgumentType.HELP)) {
            printManual();
            return true;
        }
        return false;
    }

    /**
     * Prints the manual by reading it from the resource file `manual.txt`.
     *
     * @throws IOException if an I/O error occurs while reading the manual
     */
    private static void printManual() throws IOException {
        final ClassLoader classLoader = HelpInputHandler.class.getClassLoader();
        final InputStream inputStream = classLoader.getResourceAsStream("manual.txt");
        if (inputStream == null) {
            throw new FileNotFoundException();
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            System.out.println();

            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
            System.out.println();
        }
    }

    /**
     * Checks if the specified {@link ArgumentType} is present in the command-line arguments.
     *
     * @param args     the command-line arguments
     * @param argument the {@link ArgumentType} to check for
     * @return true if the {@link ArgumentType} is present, false otherwise
     */
    private static boolean hasArgument(final String[] args, ArgumentType argument) {
        return Arrays.asList(args).contains(argument.toString());
    }
}
