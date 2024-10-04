package de.hdm_stuttgart.mi.dad.incoming.input;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Arrays;

/**
 * The `HelpInputHandler` class provides methods to handle the `--help` command-line argument.
 */
public class HelpInputHandler {

    private static final Logger log = LoggerFactory.getLogger(HelpInputHandler.class);

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
        log.debug("Called with args: {}", (Object) args);
        if (hasArgument(args)) {
            printManual();
            log.debug("Returning: true");
            return true;
        }
        log.debug("Returning: false");
        return false;
    }

    /**
     * Prints the manual by reading it from the resource file `manual.txt`.
     *
     * @throws IOException if an I/O error occurs while reading the manual
     */
    private static void printManual() throws IOException {
        log.debug("Called printManual");
        final ClassLoader classLoader = HelpInputHandler.class.getClassLoader();
        final InputStream inputStream = classLoader.getResourceAsStream("manual.txt");
        if (inputStream == null) {
            log.error("manual.txt not found");
            throw new FileNotFoundException("manual.txt not found");
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            System.out.println();
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
            System.out.println();
        }
        log.debug("Completed printManual");
    }

    /**
     * Checks if the specified {@link ArgumentType} is present in the command-line arguments.
     *
     * @param args the command-line arguments
     * @return true if the {@link ArgumentType} is present, false otherwise
     */
    private static boolean hasArgument(final String[] args) {
        log.debug("Called with args: {}", (Object) args);
        final boolean result = Arrays.asList(args).contains(ArgumentType.HELP.toString());
        log.debug("Returning: {}", result);
        return result;
    }
}