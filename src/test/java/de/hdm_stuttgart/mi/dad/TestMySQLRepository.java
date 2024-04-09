package de.hdm_stuttgart.mi.dad;

import de.hdm_stuttgart.mi.dad.outgoing.MySQLRepository;
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
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

public class TestMySQLRepository {

    static Connection connection;
    static MySQLRepository repository;
    static ScriptRunner scriptRunner;

    @BeforeAll
    public static void setUp() throws SQLException {
        final String url = "jdbc:mysql://localhost:3306/test?allowMultiQueries=true";
        connection = DriverManager.getConnection(url, "test", "test");
        scriptRunner = new ScriptRunner(connection);
        scriptRunner.setSendFullScript(true);
        scriptRunner.setStopOnError(true);
        repository = new MySQLRepository(connection);
    }

    @AfterAll
    public static void tearDown() throws SQLException {
        connection.close();
    }

    @BeforeEach
    public void setUpEach() throws FileNotFoundException {
        scriptRunner.runScript(new FileReader("src/test/resources/TestMySQLRepository/drop_tables.sql"));
    }

    @Test
    void test_findTableColumns() throws SQLException, FileNotFoundException {
        scriptRunner.runScript(new FileReader("src/test/resources/TestMySQLRepository/test_findTableColumns_testdata.sql"));

        final List<String> expected = Arrays.asList("id", "first_name", "last_name", "account_created", "username", "email", "password");
        final List<String> actual = repository.findTableColumns("account");

        System.out.println(actual);
        assertEquals(expected.size(), actual.size(), "Wrong number of columns");
        assertTrue(expected.containsAll(actual));
        assertTrue(actual.containsAll(expected));

        assertThrows(SQLException.class, () -> repository.findTableColumns("test"));
    }

    @Test
    void test_findInTable() throws SQLException, FileNotFoundException {

        scriptRunner.runScript(new FileReader("src/test/resources/TestMySQLRepository/test_findInTable_testdata.sql"));

        final List<String> columns = Arrays.asList("id", "first_name", "last_name", "account_created", "username", "email", "password");

        final Pattern pattern1 = Pattern.compile("Dorian");
        final List<List<String>> actual1 = repository.findInTable("account", columns, pattern1);
        final List<List<String>> expected1 = Arrays.asList(
                Arrays.asList("1", "Dorian", "Sporner", "2018-03-30 00:59:12", "dsporner0", "dsporner0@51.la", "jY0\\EZ&/9<X0.t")
        );
        assertEquals(1, actual1.size(), "Wrong number of rows");
        assertTrue(expected1.getFirst().containsAll(actual1.getFirst()));
        assertTrue(actual1.getFirst().containsAll(expected1.getFirst()));


        final Pattern pattern2 = Pattern.compile("^c");
        final List<List<String>> actual2 = repository.findInTable("account", columns, pattern2);
        final List<List<String>> expected2 = Arrays.asList(
                Arrays.asList("4", "Caroline", "Aubri", "2020-04-09 09:47:58", "caubri3", "caubri3@auda.org.au", "gK7*2Eit"),
                Arrays.asList("7", "Candace", "Breslauer", "2019-02-05 15:18:33", "cbreslauer6", "cbreslauer6@hao123.com", "tQ8/vr&.")
        );
        assertEquals(2, actual2.size(), "Wrong number of rows");
        assertTrue(expected2.getFirst().containsAll(actual2.getFirst()));
        assertTrue(actual2.getFirst().containsAll(expected2.getFirst()));
        assertTrue(expected2.get(1).containsAll(actual2.get(1)));
        assertTrue(actual2.get(1).containsAll(expected2.get(1)));

        final Pattern pattern3 = Pattern.compile("[0-9]+");
        final List<List<String>> actual3 = repository.findInTable("account", columns, pattern3);
        final List<List<String>> expected3 = Arrays.asList(
                Arrays.asList("1", "Dorian", "Sporner", "2018-03-30 00:59:12", "dsporner0", "dsporner0@51.la", "jY0\\EZ&/9<X0.t"),
                Arrays.asList("2", "Bale", "Sandal", "2018-06-14 01:06:41", "bsandal1", "bsandal1@virginia.edu", "iO9\"J?s==0b6cP}9"),
                Arrays.asList("3", "Burke", "Klaves", "2022-12-31 14:43:42", "bklaves2", "bklaves2@opera.com", "eA3\\i$tyTe*M(/z"),
                Arrays.asList("4", "Caroline", "Aubri", "2020-04-09 09:47:58", "caubri3", "caubri3@auda.org.au", "gK7*2Eit"),
                Arrays.asList("5", "Andrea", "Rummings", "2020-03-29 01:43:10", "arummings4", "arummings4@cnbc.com", "iL7_~0m#~\\*"),
                Arrays.asList("6", "Keenan", "Ramme", "2019-01-07 09:11:25", "kramme5", "kramme5@vistaprint.com", "aE9)*,3ye1?)Snh"),
                Arrays.asList("7", "Candace", "Breslauer", "2019-02-05 15:18:33", "cbreslauer6", "cbreslauer6@hao123.com", "tQ8/vr&."),
                Arrays.asList("8", "Nelson", "Santacrole", "2018-09-01 19:57:19", "nsantacrole7", "nsantacrole7@usatoday.com", "qP2#Z0.I1C@2kV"),
                Arrays.asList("9", "Laurel", "Norrie", "2020-11-15 17:37:15", "lnorrie8", "lnorrie8@google.ru", "mQ6}y=B8+eK"),
                Arrays.asList("10", "Vernen", "Pordal", "2020-07-20 01:21:51", "vpordal9", "vpordal9@paginegialle.it", "nZ4_k\\+!N*xhT")
        );
        assertEquals(10, actual3.size(), "Wrong number of rows");
        assertTrue(expected3.getFirst().containsAll(actual3.getFirst()));
        assertTrue(actual3.getFirst().containsAll(expected3.getFirst()));
        assertTrue(expected3.get(1).containsAll(actual3.get(1)));
        assertTrue(actual3.get(1).containsAll(expected3.get(1)));
        assertTrue(expected3.get(2).containsAll(actual3.get(2)));
        assertTrue(actual3.get(2).containsAll(expected3.get(2)));
        assertTrue(expected3.get(3).containsAll(actual3.get(3)));
        assertTrue(actual3.get(3).containsAll(expected3.get(3)));
        assertTrue(expected3.get(4).containsAll(actual3.get(4)));
        assertTrue(actual3.get(4).containsAll(expected3.get(4)));
        assertTrue(expected3.get(5).containsAll(actual3.get(5)));
        assertTrue(actual3.get(5).containsAll(expected3.get(5)));
        assertTrue(expected3.get(6).containsAll(actual3.get(6)));
        assertTrue(actual3.get(6).containsAll(expected3.get(6)));
        assertTrue(expected3.get(7).containsAll(actual3.get(7)));
        assertTrue(actual3.get(7).containsAll(expected3.get(7)));
        assertTrue(expected3.get(8).containsAll(actual3.get(8)));
        assertTrue(actual3.get(8).containsAll(expected3.get(8)));
        assertTrue(expected3.get(9).containsAll(actual3.get(9)));
        assertTrue(actual3.get(9).containsAll(expected3.get(9)));
    }

}
