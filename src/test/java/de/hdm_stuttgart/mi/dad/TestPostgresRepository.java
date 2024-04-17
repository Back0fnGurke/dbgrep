package de.hdm_stuttgart.mi.dad;

import de.hdm_stuttgart.mi.dad.outgoing.RepositoryFactory;
import de.hdm_stuttgart.mi.dad.ports.RepositoryPort;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static java.util.Map.entry;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestPostgresRepository {

    static Connection connection;
    static RepositoryPort repository;
    static ScriptRunner scriptRunner;

    @BeforeAll
    public static void setUp() throws SQLException {
        final String url = "jdbc:postgresql://localhost:5432/test";
        connection = DriverManager.getConnection(url, "test", "test");
        scriptRunner = new ScriptRunner(connection);
        scriptRunner.setSendFullScript(true);
        scriptRunner.setStopOnError(true);
        repository = RepositoryFactory.newRepository(connection, "postgresql");
    }

    @AfterAll
    public static void tearDown() throws SQLException {
        connection.close();
    }

    @BeforeEach
    public void setUpEach() throws FileNotFoundException {
        scriptRunner.runScript(new FileReader("src/test/resources/TestPostgresRepository/drop_tables.sql"));
    }

    @Test
    void test_findTableColumns() throws SQLException, FileNotFoundException {
        scriptRunner.runScript(new FileReader("src/test/resources/TestPostgresRepository/test_findTableColumns_testdata.sql"));

        final List<String> actual = repository.findTableColumns("account");

        final List<String> expected = Arrays.asList("id", "first_name", "last_name", "account_created", "username", "email", "password");
        assertEquals(expected.size(), actual.size(), "Wrong number of columns");
        assertTrue(expected.containsAll(actual));
        assertTrue(actual.containsAll(expected));

        assertTrue(repository.findTableColumns("test").isEmpty(), "should be empty");
    }

    @Test
    void test_findInTable() throws SQLException, FileNotFoundException {
        scriptRunner.runScript(new FileReader("src/test/resources/TestPostgresRepository/test_findInTable_testdata.sql"));

        final List<String> columns = Arrays.asList("id", "first_name", "last_name", "account_created", "username", "email", "password");

        final Pattern pattern1 = Pattern.compile("Dorian");
        final List<Map<String, String>> actual1 = repository.findInTable("account", columns, pattern1);
        final List<Map<String, String>> expected1 = Arrays.asList(
                Map.ofEntries(
                        entry("id", "1"),
                        entry("first_name", "Dorian"),
                        entry("last_name", "Sporner"),
                        entry("account_created", "2018-03-30 00:59:12"),
                        entry("username", "dsporner0"),
                        entry("email", "dsporner0@51.la"),
                        entry("password", "jY0\\EZ&/9<X0.t")
                )
        );
        assertEquals(expected1.size(), actual1.size(), "Wrong number of rows");
        assertEquals(expected1.getFirst(), actual1.getFirst(), "Maps should match");


        final Pattern pattern2 = Pattern.compile("^c");
        final List<Map<String, String>> actual2 = repository.findInTable("account", columns, pattern2);
        final List<Map<String, String>> expected2 = Arrays.asList(
                Map.ofEntries(
                        entry("id", "4"),
                        entry("first_name", "Caroline"),
                        entry("last_name", "Aubri"),
                        entry("account_created", "2020-04-09 09:47:58"),
                        entry("username", "caubri3"),
                        entry("email", "caubri3@auda.org.au"),
                        entry("password", "gK7*2Eit")
                ),
                Map.ofEntries(
                        entry("id", "7"),
                        entry("first_name", "Candace"),
                        entry("last_name", "Breslauer"),
                        entry("account_created", "2019-02-05 15:18:33"),
                        entry("username", "cbreslauer6"),
                        entry("email", "cbreslauer6@hao123.com"),
                        entry("password", "tQ8/vr&.")
                )
        );
        assertEquals(expected2.size(), actual2.size(), "Wrong number of rows");
        assertEquals(expected2.getFirst(), actual2.getFirst(), "Maps should match");
        assertEquals(expected2.get(1), actual2.get(1), "Maps should match");


        final Pattern pattern3 = Pattern.compile("[0-9]+");
        final List<Map<String, String>> actual3 = repository.findInTable("account", columns, pattern3);
        final List<Map<String, String>> expected3 = Arrays.asList(
                Map.ofEntries(
                        entry("id", "1"),
                        entry("first_name", "Dorian"),
                        entry("last_name", "Sporner"),
                        entry("account_created", "2018-03-30 00:59:12"),
                        entry("username", "dsporner0"),
                        entry("email", "dsporner0@51.la"),
                        entry("password", "jY0\\EZ&/9<X0.t")
                ),
                Map.ofEntries(
                        entry("id", "2"),
                        entry("first_name", "Bale"),
                        entry("last_name", "Sandal"),
                        entry("account_created", "2018-06-14 01:06:41"),
                        entry("username", "bsandal1"),
                        entry("email", "bsandal1@virginia.edu"),
                        entry("password", "iO9\"J?s==0b6cP}9")
                ),
                Map.ofEntries(
                        entry("id", "3"),
                        entry("first_name", "Burke"),
                        entry("last_name", "Klaves"),
                        entry("account_created", "2022-12-31 14:43:42"),
                        entry("username", "bklaves2"),
                        entry("email", "bklaves2@opera.com"),
                        entry("password", "eA3\\i$tyTe*M(/z")
                ),
                Map.ofEntries(
                        entry("id", "4"),
                        entry("first_name", "Caroline"),
                        entry("last_name", "Aubri"),
                        entry("account_created", "2020-04-09 09:47:58"),
                        entry("username", "caubri3"),
                        entry("email", "caubri3@auda.org.au"),
                        entry("password", "gK7*2Eit")
                ),
                Map.ofEntries(
                        entry("id", "5"),
                        entry("first_name", "Andrea"),
                        entry("last_name", "Rummings"),
                        entry("account_created", "2020-03-29 01:43:10"),
                        entry("username", "arummings4"),
                        entry("email", "arummings4@cnbc.com"),
                        entry("password", "iL7_~0m#~\\*")
                ),
                Map.ofEntries(
                        entry("id", "6"),
                        entry("first_name", "Keenan"),
                        entry("last_name", "Ramme"),
                        entry("account_created", "2019-01-07 09:11:25"),
                        entry("username", "kramme5"),
                        entry("email", "kramme5@vistaprint.com"),
                        entry("password", "aE9)*,3ye1?)Snh")
                ),
                Map.ofEntries(
                        entry("id", "7"),
                        entry("first_name", "Candace"),
                        entry("last_name", "Breslauer"),
                        entry("account_created", "2019-02-05 15:18:33"),
                        entry("username", "cbreslauer6"),
                        entry("email", "cbreslauer6@hao123.com"),
                        entry("password", "tQ8/vr&.")
                ),
                Map.ofEntries(
                        entry("id", "8"),
                        entry("first_name", "Nelson"),
                        entry("last_name", "Santacrole"),
                        entry("account_created", "2018-09-01 19:57:19"),
                        entry("username", "nsantacrole7"),
                        entry("email", "nsantacrole7@usatoday.com"),
                        entry("password", "qP2#Z0.I1C@2kV")
                ),
                Map.ofEntries(
                        entry("id", "9"),
                        entry("first_name", "Laurel"),
                        entry("last_name", "Norrie"),
                        entry("account_created", "2020-11-15 17:37:15"),
                        entry("username", "lnorrie8"),
                        entry("email", "lnorrie8@google.ru"),
                        entry("password", "mQ6}y=B8+eK")
                ),
                Map.ofEntries(
                        entry("id", "10"),
                        entry("first_name", "Vernen"),
                        entry("last_name", "Pordal"),
                        entry("account_created", "2020-07-20 01:21:51"),
                        entry("username", "vpordal9"),
                        entry("email", "vpordal9@paginegialle.it"),
                        entry("password", "nZ4_k\\+!N*xhT")
                )
        );
        assertEquals(expected3.size(), actual3.size(), "Wrong number of rows");
        assertEquals(expected3.getFirst(), actual3.getFirst(), "Maps should match");
        assertEquals(expected3.get(1), actual3.get(1), "Maps should match");
        assertEquals(expected3.get(2), actual3.get(2), "Maps should match");
        assertEquals(expected3.get(3), actual3.get(3), "Maps should match");
        assertEquals(expected3.get(4), actual3.get(4), "Maps should match");
        assertEquals(expected3.get(5), actual3.get(5), "Maps should match");
        assertEquals(expected3.get(6), actual3.get(6), "Maps should match");
        assertEquals(expected3.get(7), actual3.get(7), "Maps should match");
        assertEquals(expected3.get(8), actual3.get(8), "Maps should match");
        assertEquals(expected3.get(9), actual3.get(9), "Maps should match");


        final Pattern pattern4 = Pattern.compile("test");
        final List<Map<String, String>> actual4 = repository.findInTable("account", columns, pattern4);
        assertTrue(actual4.isEmpty(), "should be empty");
    }

}
