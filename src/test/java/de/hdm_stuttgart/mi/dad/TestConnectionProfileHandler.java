package de.hdm_stuttgart.mi.dad;

import de.hdm_stuttgart.mi.dad.connectionprofile.ConnectionProfile;
import de.hdm_stuttgart.mi.dad.connectionprofile.ConnectionProfileHandler;
import de.hdm_stuttgart.mi.dad.core.exception.IllegalFileExtensionException;
import de.hdm_stuttgart.mi.dad.core.exception.MultipleProfileException;
import de.hdm_stuttgart.mi.dad.core.exception.NoProfileException;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

public class TestConnectionProfileHandler {

    @Test
    void testGetDefaultProfileWithNoProfile() {
        ConnectionProfileHandler handler = new ConnectionProfileHandler("src/test/resources/TestConnectionProfileHandler/no_profile");
        assertThrows(NoProfileException.class, handler::getDefaultProfile);
    }

    @Test
    void testGetDefaultProfileWithOneProfile() {
        ConnectionProfileHandler handler = new ConnectionProfileHandler("src/test/resources/TestConnectionProfileHandler/one_profile");
        ConnectionProfile profile = assertDoesNotThrow(handler::getDefaultProfile);
        assertEquals(profile.getUser(), "user5");
    }

    @Test
    void testGetDefaultProfileWithMultipleProfile() {
        ConnectionProfileHandler handler = new ConnectionProfileHandler("src/test/resources/TestConnectionProfileHandler/multiple_profile");
        assertThrows(MultipleProfileException.class, handler::getDefaultProfile);
    }

    @Test
    void testGetSelectedProfileWithNotExistingFile() {
        ConnectionProfileHandler handler = new ConnectionProfileHandler("src/test/resources/TestConnectionProfileHandler/multiple_profile");
        assertThrows(FileNotFoundException.class, () -> handler.getSelectedProfile("tet1.cnf"));
    }

    @Test
    void testGetSelectedProfileWithExistingFile() {
        ConnectionProfileHandler handler = new ConnectionProfileHandler("src/test/resources/TestConnectionProfileHandler/multiple_profile");
        assertThrows(IllegalFileExtensionException.class, () -> handler.getSelectedProfile("test2.txt"));

        ConnectionProfile profile1 = assertDoesNotThrow(() -> handler.getSelectedProfile("test1.cnf"));
        assertEquals(profile1.getUser(), "user1");

        ConnectionProfile profile2 = assertDoesNotThrow(() -> handler.getSelectedProfile("test3.cnf"));
        assertEquals(profile2.getUser(), "user3");
        assertEquals(profile2.getHost(), "myserver.companynet.com");
        assertEquals(profile2.getPort(), "5432");
        assertEquals(profile2.getDatabase(), "main_test_data");
        assertEquals(profile2.getPassword(), "secret");
        assertEquals(profile2.getDriver(), "Driver/postgresql-42.6.0.jar");
    }

    @Test
    void testGetStringOfProfileList() {
        ConnectionProfileHandler handler = new ConnectionProfileHandler("src/test/resources/TestConnectionProfileHandler/multiple_profile");
        String string = assertDoesNotThrow(handler::getStringOfProfileList);
        assertEquals(string, "test1.cnf\ntest3.cnf\n");

        ConnectionProfileHandler handlerNoProfile = new ConnectionProfileHandler("src/test/resources/TestConnectionProfileHandler/no_profile");
        String stringNoProfile = assertDoesNotThrow(handlerNoProfile::getStringOfProfileList);
        assertEquals(stringNoProfile, "");
    }
}
