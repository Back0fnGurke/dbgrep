package de.hdm_stuttgart.mi.dad.incoming;

import de.hdm_stuttgart.mi.dad.incoming.input.HelpInputHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TestHelpInputHandler {

    private final PrintStream standardOut = System.out;
    private ByteArrayOutputStream outContent;

    @BeforeEach
    void setUp() {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }

    @Test
    void testPrintManual() throws IOException {
        final String[] args = {"--help"};

        final boolean isHelp = HelpInputHandler.handleHelp(args);

        assertTrue(isHelp);
        assertTrue(outContent.toString().contains("DESCRIPTION"));
        assertTrue(outContent.toString().contains("PARAMETERS FOR MATCHING"));
    }

    @Test
    void testDontPrintManual() throws IOException {
        final String[] args = {"--profile", "postgres.cfg", "--greater", "2007-12-24", "--range", "4.0,7.0", "--regex", "test"};

        final boolean isHelp = HelpInputHandler.handleHelp(args);

        assertFalse(isHelp);
        assertTrue(outContent.toString().isEmpty());
    }
}
