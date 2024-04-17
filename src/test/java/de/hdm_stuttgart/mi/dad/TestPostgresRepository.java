package de.hdm_stuttgart.mi.dad;

import de.hdm_stuttgart.mi.dad.core.entity.ColumnValue;
import de.hdm_stuttgart.mi.dad.core.entity.Row;
import de.hdm_stuttgart.mi.dad.core.entity.Table;
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
import java.util.regex.Pattern;

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
    void test_findTableColumnNames() throws SQLException, FileNotFoundException {
        scriptRunner.runScript(new FileReader("src/test/resources/TestPostgresRepository/test_findTableColumnNames_testdata.sql"));

        final List<String> actual = repository.findTableColumnNames("account");

        final List<String> expected = Arrays.asList("id", "first_name", "last_name", "account_created", "username", "email", "password");
        assertEquals(expected.size(), actual.size(), "Wrong number of columns");
        assertTrue(expected.containsAll(actual));
        assertTrue(actual.containsAll(expected));

        assertTrue(repository.findTableColumnNames("test").isEmpty(), "should be empty");
    }

    @Test
    void test_findInTable() throws SQLException, FileNotFoundException {
        scriptRunner.runScript(new FileReader("src/test/resources/TestPostgresRepository/test_findPattern_testdata.sql"));

        final List<String> columns = Arrays.asList("id", "first_name", "last_name", "account_created", "username", "email", "password");


        final String tableName1 = "account";
        final Pattern pattern1 = Pattern.compile("Dorian");
        final Table actual1 = repository.findPattern(tableName1, columns, pattern1);
        final Table expected1 = new Table(tableName1, List.of(
                new Row(Arrays.asList(
                        new ColumnValue("id", "1"),
                        new ColumnValue("first_name", "Dorian"),
                        new ColumnValue("last_name", "Sporner"),
                        new ColumnValue("account_created", "2018-03-30 00:59:12"),
                        new ColumnValue("username", "dsporner0"),
                        new ColumnValue("email", "dsporner0@51.la"),
                        new ColumnValue("password", "jY0\\EZ&/9<X0.t")
                ))
        ));
        assertEquals(expected1.rows().size(), actual1.rows().size(), "Wrong number of rows");
        assertEquals(expected1.rows().getFirst(), actual1.rows().getFirst(), "Maps should match");


        final String tableName2 = "account";
        final Pattern pattern2 = Pattern.compile("^c");
        final Table actual2 = repository.findPattern(tableName2, columns, pattern2);
        final Table expected2 = new Table(tableName2, Arrays.asList(
                new Row(Arrays.asList(
                        new ColumnValue("id", "4"),
                        new ColumnValue("first_name", "Caroline"),
                        new ColumnValue("last_name", "Aubri"),
                        new ColumnValue("account_created", "2020-04-09 09:47:58"),
                        new ColumnValue("username", "caubri3"),
                        new ColumnValue("email", "caubri3@auda.org.au"),
                        new ColumnValue("password", "gK7*2Eit")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("id", "7"),
                        new ColumnValue("first_name", "Candace"),
                        new ColumnValue("last_name", "Breslauer"),
                        new ColumnValue("account_created", "2019-02-05 15:18:33"),
                        new ColumnValue("username", "cbreslauer6"),
                        new ColumnValue("email", "cbreslauer6@hao123.com"),
                        new ColumnValue("password", "tQ8/vr&.")
                ))
        ));
        assertEquals(expected2.rows().size(), actual2.rows().size(), "Wrong number of rows");
        assertEquals(expected2.rows().getFirst(), actual2.rows().getFirst(), "Maps should match");
        assertEquals(expected2.rows().get(1), actual2.rows().get(1), "Maps should match");


        final String tableName3 = "account";
        final Pattern pattern3 = Pattern.compile("[0-9]+");
        final Table actual3 = repository.findPattern(tableName3, columns, pattern3);
        final Table expected3 = new Table(tableName3, Arrays.asList(
                new Row(Arrays.asList(
                        new ColumnValue("id", "1"),
                        new ColumnValue("first_name", "Dorian"),
                        new ColumnValue("last_name", "Sporner"),
                        new ColumnValue("account_created", "2018-03-30 00:59:12"),
                        new ColumnValue("username", "dsporner0"),
                        new ColumnValue("email", "dsporner0@51.la"),
                        new ColumnValue("password", "jY0\\EZ&/9<X0.t")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("id", "2"),
                        new ColumnValue("first_name", "Bale"),
                        new ColumnValue("last_name", "Sandal"),
                        new ColumnValue("account_created", "2018-06-14 01:06:41"),
                        new ColumnValue("username", "bsandal1"),
                        new ColumnValue("email", "bsandal1@virginia.edu"),
                        new ColumnValue("password", "iO9\"J?s==0b6cP}9")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("id", "3"),
                        new ColumnValue("first_name", "Burke"),
                        new ColumnValue("last_name", "Klaves"),
                        new ColumnValue("account_created", "2022-12-31 14:43:42"),
                        new ColumnValue("username", "bklaves2"),
                        new ColumnValue("email", "bklaves2@opera.com"),
                        new ColumnValue("password", "eA3\\i$tyTe*M(/z")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("id", "4"),
                        new ColumnValue("first_name", "Caroline"),
                        new ColumnValue("last_name", "Aubri"),
                        new ColumnValue("account_created", "2020-04-09 09:47:58"),
                        new ColumnValue("username", "caubri3"),
                        new ColumnValue("email", "caubri3@auda.org.au"),
                        new ColumnValue("password", "gK7*2Eit")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("id", "5"),
                        new ColumnValue("first_name", "Andrea"),
                        new ColumnValue("last_name", "Rummings"),
                        new ColumnValue("account_created", "2020-03-29 01:43:10"),
                        new ColumnValue("username", "arummings4"),
                        new ColumnValue("email", "arummings4@cnbc.com"),
                        new ColumnValue("password", "iL7_~0m#~\\*")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("id", "6"),
                        new ColumnValue("first_name", "Keenan"),
                        new ColumnValue("last_name", "Ramme"),
                        new ColumnValue("account_created", "2019-01-07 09:11:25"),
                        new ColumnValue("username", "kramme5"),
                        new ColumnValue("email", "kramme5@vistaprint.com"),
                        new ColumnValue("password", "aE9)*,3ye1?)Snh")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("id", "7"),
                        new ColumnValue("first_name", "Candace"),
                        new ColumnValue("last_name", "Breslauer"),
                        new ColumnValue("account_created", "2019-02-05 15:18:33"),
                        new ColumnValue("username", "cbreslauer6"),
                        new ColumnValue("email", "cbreslauer6@hao123.com"),
                        new ColumnValue("password", "tQ8/vr&.")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("id", "8"),
                        new ColumnValue("first_name", "Nelson"),
                        new ColumnValue("last_name", "Santacrole"),
                        new ColumnValue("account_created", "2018-09-01 19:57:19"),
                        new ColumnValue("username", "nsantacrole7"),
                        new ColumnValue("email", "nsantacrole7@usatoday.com"),
                        new ColumnValue("password", "qP2#Z0.I1C@2kV")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("id", "9"),
                        new ColumnValue("first_name", "Laurel"),
                        new ColumnValue("last_name", "Norrie"),
                        new ColumnValue("account_created", "2020-11-15 17:37:15"),
                        new ColumnValue("username", "lnorrie8"),
                        new ColumnValue("email", "lnorrie8@google.ru"),
                        new ColumnValue("password", "mQ6}y=B8+eK")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("id", "10"),
                        new ColumnValue("first_name", "Vernen"),
                        new ColumnValue("last_name", "Pordal"),
                        new ColumnValue("account_created", "2020-07-20 01:21:51"),
                        new ColumnValue("username", "vpordal9"),
                        new ColumnValue("email", "vpordal9@paginegialle.it"),
                        new ColumnValue("password", "nZ4_k\\+!N*xhT")
                ))
        ));
        assertEquals(expected3.rows().size(), actual3.rows().size(), "Wrong number of rows");
        assertEquals(expected3.rows().getFirst(), actual3.rows().getFirst(), "Maps should match");
        assertEquals(expected3.rows().get(1), actual3.rows().get(1), "Maps should match");
        assertEquals(expected3.rows().get(2), actual3.rows().get(2), "Maps should match");
        assertEquals(expected3.rows().get(3), actual3.rows().get(3), "Maps should match");
        assertEquals(expected3.rows().get(4), actual3.rows().get(4), "Maps should match");
        assertEquals(expected3.rows().get(5), actual3.rows().get(5), "Maps should match");
        assertEquals(expected3.rows().get(6), actual3.rows().get(6), "Maps should match");
        assertEquals(expected3.rows().get(7), actual3.rows().get(7), "Maps should match");
        assertEquals(expected3.rows().get(8), actual3.rows().get(8), "Maps should match");
        assertEquals(expected3.rows().get(9), actual3.rows().get(9), "Maps should match");


        final String tableName4 = "account";
        final Pattern pattern4 = Pattern.compile("test");
        final Table actual4 = repository.findPattern(tableName4, columns, pattern4);
        assertTrue(actual4.rows().isEmpty(), "should be empty");
    }

}
