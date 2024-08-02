package de.hdm_stuttgart.mi.dad;

import de.hdm_stuttgart.mi.dad.connectionprofile.ConnectionProfile;
import de.hdm_stuttgart.mi.dad.connectionprofile.ConnectionProfileHandler;
import de.hdm_stuttgart.mi.dad.connectionprofile.exception.MultipleProfileException;
import de.hdm_stuttgart.mi.dad.connectionprofile.exception.NoProfileException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class TestConnectionProfileHandler {

    @BeforeAll
    public static void setUpEmptyDirectory() throws IOException {
        Files.createDirectory(Paths.get("src/test/resources/TestConnectionProfileHandler/no_profile"));
    }

    @AfterAll
    public static void deleteEmptyDirectory() throws IOException {
        Files.deleteIfExists(Paths.get("src/test/resources/TestConnectionProfileHandler/no_profile"));
    }


    @Test
    void testConstructor() {
        assertDoesNotThrow(() -> new ConnectionProfileHandler(Paths.get("src/test/resources/TestConnectionProfileHandler/no_profile")));
        assertThrows(FileNotFoundException.class, () -> new ConnectionProfileHandler(Paths.get("src/test/resources/TestConnectionProfileHandler/no_directory")));
    }

    @Test
    void testGetDefaultProfileWithNoProfile() {
        ConnectionProfileHandler handler = assertDoesNotThrow(() -> new ConnectionProfileHandler(Paths.get("src/test/resources/TestConnectionProfileHandler/no_profile")));
        assertThrows(NoProfileException.class, handler::getDefaultProfile);
    }

    @Test
    void testGetDefaultProfileWithOneProfile() {
        ConnectionProfileHandler handler = assertDoesNotThrow(() -> new ConnectionProfileHandler(Paths.get("src/test/resources/TestConnectionProfileHandler/one_profile")));
        ConnectionProfile profile = assertDoesNotThrow(handler::getDefaultProfile);
        assertEquals(profile.getUser(), "user5");
    }

    @Test
    void testGetDefaultProfileWithMultipleProfile() {
        ConnectionProfileHandler handler = assertDoesNotThrow(() -> new ConnectionProfileHandler(Paths.get("src/test/resources/TestConnectionProfileHandler/multiple_profile")));
        assertThrows(MultipleProfileException.class, handler::getDefaultProfile);
    }

    @Test
    void testGetSelectedProfileWithNotExistingFile() {
        ConnectionProfileHandler handler = assertDoesNotThrow(() -> new ConnectionProfileHandler(Paths.get("src/test/resources/TestConnectionProfileHandler/multiple_profile")));
        assertThrows(FileNotFoundException.class, () -> handler.getSelectedProfile("tet1.cnf"));
    }

    @Test
    void testGetSelectedProfileWithExistingFile() {
        ConnectionProfileHandler handler = assertDoesNotThrow(() -> new ConnectionProfileHandler(Paths.get("src/test/resources/TestConnectionProfileHandler/multiple_profile")));
        assertDoesNotThrow(() -> handler.getSelectedProfile("test2.txt"));

        ConnectionProfile profile1 = assertDoesNotThrow(() -> handler.getSelectedProfile("test1.cfg"));
        assertEquals(profile1.getUser(), "user1");

        ConnectionProfile profile2 = assertDoesNotThrow(() -> handler.getSelectedProfile("test3.cfg"));
        assertEquals(profile2.getUser(), "user3");
        assertEquals(profile2.getHost(), "myserver.companynet.com");
        assertEquals(profile2.getPort(), "5432");
        assertEquals(profile2.getDatabase(), "main_test_data");
        assertEquals(profile2.getPassword(), "secret");
        assertEquals(profile2.getDriver(), "Driver/postgresql-42.6.0.jar");
    }

    @Test
    void testGetStringOfProfileList() {
        ConnectionProfileHandler handler = assertDoesNotThrow(() -> new ConnectionProfileHandler(Paths.get("src/test/resources/TestConnectionProfileHandler/multiple_profile")));
        String string = assertDoesNotThrow(handler::getStringOfProfileList);
        assertTrue(string.contains("test1.cfg"));
        assertTrue(string.contains("test3.cfg"));
        assertTrue(string.contains("test2.txt"));

        ConnectionProfileHandler handlerNoProfile = assertDoesNotThrow(() -> new ConnectionProfileHandler(Paths.get("src/test/resources/TestConnectionProfileHandler/no_profile")));
        String stringNoProfile = assertDoesNotThrow(handlerNoProfile::getStringOfProfileList);
        assertEquals("", stringNoProfile);
    }
}
