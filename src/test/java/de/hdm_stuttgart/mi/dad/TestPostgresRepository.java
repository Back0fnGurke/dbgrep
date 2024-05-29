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
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
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
    void test_findPattern_no_match() throws SQLException, FileNotFoundException {
        scriptRunner.runScript(new FileReader("src/test/resources/TestPostgresRepository/test_findPattern_test_data.sql"));

        final List<String> columns = Arrays.asList("id", "first_name", "last_name", "account_created", "username", "email", "password");

        final String tableName = "account";
        final Pattern pattern = Pattern.compile("test");
        final Table actual = repository.findPattern(tableName, columns, pattern);
        assertTrue(actual.rows().isEmpty(), "should be empty");
    }

    @Test
    void test_findPattern_one_match() throws SQLException, FileNotFoundException {
        scriptRunner.runScript(new FileReader("src/test/resources/TestPostgresRepository/test_findPattern_test_data.sql"));

        final List<String> columns = Arrays.asList("id", "first_name", "last_name", "account_created", "username", "email", "password");


        final String tableName = "account";
        final Pattern pattern = Pattern.compile("Dorian");
        final Table actual = repository.findPattern(tableName, columns, pattern);
        final Table expected = new Table(tableName, List.of(
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
        assertEquals(expected.rows().size(), actual.rows().size(), "Wrong number of rows");
        assertEquals(expected.rows().getFirst(), actual.rows().getFirst(), "Should match");
    }

    @Test
    void test_findPattern_multiple_match() throws SQLException, FileNotFoundException {
        scriptRunner.runScript(new FileReader("src/test/resources/TestPostgresRepository/test_findPattern_test_data.sql"));

        final List<String> columns = Arrays.asList("id", "first_name", "last_name", "account_created", "username", "email", "password");

        final String tableName = "account";
        final Pattern pattern = Pattern.compile("^c");
        final Table actual = repository.findPattern(tableName, columns, pattern);
        final Table expected = new Table(tableName, Arrays.asList(
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
        assertEquals(expected.rows().size(), actual.rows().size(), "Wrong number of rows");
        assertEquals(expected.rows().getFirst(), actual.rows().getFirst(), "Should match");
        assertEquals(expected.rows().get(1), actual.rows().get(1), "Should match");
    }

    @Test
    void test_findPattern_number_match() throws SQLException, FileNotFoundException {
        scriptRunner.runScript(new FileReader("src/test/resources/TestPostgresRepository/test_findPattern_test_data.sql"));

        final List<String> columns = Arrays.asList("id", "first_name", "last_name", "account_created", "username", "email", "password");

        final String tableName = "account";
        final Pattern pattern = Pattern.compile("10");
        final Table actual = repository.findPattern(tableName, columns, pattern);
        final Table expected = new Table(tableName, Arrays.asList(
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
                        new ColumnValue("id", "10"),
                        new ColumnValue("first_name", "Vernen"),
                        new ColumnValue("last_name", "Pordal"),
                        new ColumnValue("account_created", "2020-07-20 01:21:51"),
                        new ColumnValue("username", "vpordal9"),
                        new ColumnValue("email", "vpordal9@paginegialle.it"),
                        new ColumnValue("password", "nZ4_k\\+!N*xhT")
                ))
        ));
        assertEquals(expected.rows().size(), actual.rows().size(), "Wrong number of rows");
        assertEquals(expected.rows().getFirst(), actual.rows().getFirst(), "Should match");
        assertEquals(expected.rows().get(1), actual.rows().get(1), "Should match");
    }

    @Test
    void test_findLikePattern_no_match() throws SQLException, FileNotFoundException {
        scriptRunner.runScript(new FileReader("src/test/resources/TestPostgresRepository/test_findPattern_test_data.sql"));

        final List<String> columns = Arrays.asList("id", "first_name", "last_name", "account_created", "username", "email", "password");

        final String tableName = "account";
        final Pattern pattern = Pattern.compile("test");
        final Table actual = repository.findPattern(tableName, columns, pattern);
        assertTrue(actual.rows().isEmpty(), "should be empty");
    }

    @Test
    void test_findLikePattern_one_match() throws SQLException, FileNotFoundException {
        scriptRunner.runScript(new FileReader("src/test/resources/TestPostgresRepository/test_findPattern_test_data.sql"));

        final List<String> columns = Arrays.asList("id", "first_name", "last_name", "account_created", "username", "email", "password");

        final String tableName = "account";
        final Pattern pattern = Pattern.compile("Dorian");
        final Table actual = repository.findLikePattern(tableName, columns, pattern);
        final Table expected = new Table(tableName, List.of(
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
        assertEquals(expected.rows().size(), actual.rows().size(), "Wrong number of rows");
        assertEquals(expected.rows().getFirst(), actual.rows().getFirst(), "Should match");
    }

    @Test
    void test_findLikePattern_multiple_match() throws SQLException, FileNotFoundException {
        scriptRunner.runScript(new FileReader("src/test/resources/TestPostgresRepository/test_findPattern_test_data.sql"));

        final List<String> columns = Arrays.asList("id", "first_name", "last_name", "account_created", "username", "email", "password");

        final String tableName = "account";
        final Pattern pattern = Pattern.compile("c%");
        final Table actual = repository.findLikePattern(tableName, columns, pattern);
        final Table expected = new Table(tableName, Arrays.asList(
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
        assertEquals(expected.rows().size(), actual.rows().size(), "Wrong number of rows");
        assertEquals(expected.rows().getFirst(), actual.rows().getFirst(), "Should match");
        assertEquals(expected.rows().get(1), actual.rows().get(1), "Should match");
    }

    @Test
    void test_findLikePattern_date_match() throws SQLException, FileNotFoundException {
        scriptRunner.runScript(new FileReader("src/test/resources/TestPostgresRepository/test_findPattern_test_data.sql"));

        final List<String> columns = Arrays.asList("id", "first_name", "last_name", "account_created", "username", "email", "password");

        final String tableName = "account";
        final Pattern pattern = Pattern.compile("2018-__-__ __:__:__");
        final Table actual = repository.findLikePattern(tableName, columns, pattern);
        final Table expected = new Table(tableName, Arrays.asList(
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
                        new ColumnValue("id", "8"),
                        new ColumnValue("first_name", "Nelson"),
                        new ColumnValue("last_name", "Santacrole"),
                        new ColumnValue("account_created", "2018-09-01 19:57:19"),
                        new ColumnValue("username", "nsantacrole7"),
                        new ColumnValue("email", "nsantacrole7@usatoday.com"),
                        new ColumnValue("password", "qP2#Z0.I1C@2kV")
                ))
        ));
        assertEquals(expected.rows().size(), actual.rows().size(), "Wrong number of rows");
        assertEquals(expected.rows().getFirst(), actual.rows().getFirst(), "Should match");
        assertEquals(expected.rows().get(1), actual.rows().get(1), "Should match");
        assertEquals(expected.rows().get(2), actual.rows().get(2), "Should match");
    }

    @Test
    void test_findEqual_no_match() throws SQLException, FileNotFoundException {
        scriptRunner.runScript(new FileReader("src/test/resources/TestPostgresRepository/test_findEqual_testdata.sql"));

        final List<String> columns = Arrays.asList("id", "age", "money");

        final String tableName = "account";
        final double number = 11;
        final Table actual = repository.findEqual(tableName, columns, number);
        assertTrue(actual.rows().isEmpty(), "should be empty");
    }

    @Test
    void test_findEqual_integer() throws SQLException, FileNotFoundException {
        scriptRunner.runScript(new FileReader("src/test/resources/TestPostgresRepository/test_findEqual_testdata.sql"));

        final List<String> columns = Arrays.asList("id", "age", "money");

        final String tableName = "account";
        final double number = 34;
        final Table actual = repository.findEqual(tableName, columns, number);
        final Table expected = new Table(tableName, List.of(
                new Row(Arrays.asList(
                        new ColumnValue("id", "5"),
                        new ColumnValue("first_name", "Onfroi"),
                        new ColumnValue("last_name", "Martignoni"),
                        new ColumnValue("age", "34"),
                        new ColumnValue("money", "2830.37")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("id", "8"),
                        new ColumnValue("first_name", "Binnie"),
                        new ColumnValue("last_name", "Feld"),
                        new ColumnValue("age", "34"),
                        new ColumnValue("money", "5306.60")
                ))
        ));
        assertEquals(expected.rows().size(), actual.rows().size(), "Wrong number of rows");
        assertEquals(expected.rows().getFirst(), actual.rows().getFirst(), "Should match");
        assertEquals(expected.rows().getLast(), actual.rows().getLast(), "Should match");
    }

    @Test
    void test_findEqual_decimal() throws SQLException, FileNotFoundException {
        scriptRunner.runScript(new FileReader("src/test/resources/TestPostgresRepository/test_findEqual_testdata.sql"));

        final List<String> columns = Arrays.asList("id", "age", "money");

        final String tableName = "account";
        final double number = 0.50;
        final Table actual = repository.findEqual(tableName, columns, number);
        final Table expected = new Table(tableName, List.of(
                new Row(Arrays.asList(
                        new ColumnValue("id", "4"),
                        new ColumnValue("first_name", "Malinde"),
                        new ColumnValue("last_name", "Ketchen"),
                        new ColumnValue("age", "94"),
                        new ColumnValue("money", "0.50")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("id", "6"),
                        new ColumnValue("first_name", "Ardelis"),
                        new ColumnValue("last_name", "Coundley"),
                        new ColumnValue("age", "61"),
                        new ColumnValue("money", "0.50")
                ))
        ));
        assertEquals(expected.rows().size(), actual.rows().size(), "Wrong number of rows");
        assertEquals(expected.rows().getFirst(), actual.rows().getFirst(), "Should match");
        assertEquals(expected.rows().getLast(), actual.rows().getLast(), "Should match");
    }

    @Test
    void test_findGreaterNumeric_no_match() throws FileNotFoundException, SQLException {
        scriptRunner.runScript(new FileReader("src/test/resources/TestPostgresRepository/test_findGreaterNumeric_test_data.sql"));

        final List<String> columns = Arrays.asList("id", "zipcode", "balance", "votes", "bought_books");

        final String tableName = "account";
        final BigDecimal number = BigDecimal.valueOf(98278675);
        final Table actual = repository.findGreaterNumeric(tableName, columns, number);
        assertTrue(actual.rows().isEmpty(), "should be empty");
    }

    @Test
    void test_findGreaterNumeric_one_match() throws SQLException, FileNotFoundException {
        scriptRunner.runScript(new FileReader("src/test/resources/TestPostgresRepository/test_findGreaterNumeric_test_data.sql"));

        final List<String> columns = Arrays.asList("id", "zipcode", "balance", "votes", "bought_books");

        final String tableName = "account";
        final BigDecimal number = BigDecimal.valueOf(97278674);
        final Table actual = repository.findGreaterNumeric(tableName, columns, number);
        final Table expected = new Table(tableName, List.of(
                new Row(Arrays.asList(
                        new ColumnValue("id", "10"),
                        new ColumnValue("first_name", "Claudianus"),
                        new ColumnValue("last_name", "Hirsch"),
                        new ColumnValue("account_created", "2023-10-22 13:53:38"),
                        new ColumnValue("username", "chirsch9"),
                        new ColumnValue("email", "chirsch9@fc2.com"),
                        new ColumnValue("password", "fR1`C=>yQCP"),
                        new ColumnValue("zipcode", "618"),
                        new ColumnValue("balance", "$97,278,675.00"),
                        new ColumnValue("votes", "316616"),
                        new ColumnValue("bought_books", "358568")
                ))
        ));
        assertEquals(expected.rows().size(), actual.rows().size(), "Wrong number of rows");
        assertEquals(expected.rows().getFirst(), actual.rows().getFirst(), "Should match");
    }

    @Test
    void test_findGreaterNumeric_multiple_match() throws FileNotFoundException, SQLException {
        scriptRunner.runScript(new FileReader("src/test/resources/TestPostgresRepository/test_findGreaterNumeric_test_data.sql"));

        final List<String> columns = Arrays.asList("id", "zipcode", "balance", "votes", "bought_books");

        final String tableName = "account";
        final BigDecimal number = BigDecimal.valueOf(94935073);
        final Table actual = repository.findGreaterNumeric(tableName, columns, number);
        final Table expected = new Table(tableName, List.of(
                new Row(Arrays.asList(
                        new ColumnValue("id", "7"),
                        new ColumnValue("first_name", "Yvonne"),
                        new ColumnValue("last_name", "Biaggetti"),
                        new ColumnValue("account_created", "2024-01-04 01:24:35"),
                        new ColumnValue("username", "ybiaggetti6"),
                        new ColumnValue("email", "ybiaggetti6@ning.com"),
                        new ColumnValue("password", "bB5$4''AkxnC>uK("),
                        new ColumnValue("zipcode", "11247"),
                        new ColumnValue("balance", "$94,935,074.00"),
                        new ColumnValue("votes", "479981"),
                        new ColumnValue("bought_books", "107847")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("id", "9"),
                        new ColumnValue("first_name", "Tye"),
                        new ColumnValue("last_name", "Heintzsch"),
                        new ColumnValue("account_created", "2024-03-01 18:11:51"),
                        new ColumnValue("username", "theintzsch8"),
                        new ColumnValue("email", "theintzsch8@newyorker.com"),
                        new ColumnValue("password", "hE1#zsv=P0"),
                        new ColumnValue("zipcode", "24442"),
                        new ColumnValue("balance", "$96,443,590.00"),
                        new ColumnValue("votes", "827467"),
                        new ColumnValue("bought_books", "928780")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("id", "10"),
                        new ColumnValue("first_name", "Claudianus"),
                        new ColumnValue("last_name", "Hirsch"),
                        new ColumnValue("account_created", "2023-10-22 13:53:38"),
                        new ColumnValue("username", "chirsch9"),
                        new ColumnValue("email", "chirsch9@fc2.com"),
                        new ColumnValue("password", "fR1`C=>yQCP"),
                        new ColumnValue("zipcode", "618"),
                        new ColumnValue("balance", "$97,278,675.00"),
                        new ColumnValue("votes", "316616"),
                        new ColumnValue("bought_books", "358568")
                ))
        ));
        assertEquals(expected.rows().size(), actual.rows().size(), "Wrong number of rows");
        assertEquals(expected.rows().getFirst(), actual.rows().getFirst(), "Should match");
        assertEquals(expected.rows().get(1), actual.rows().get(1), "Should match");
        assertEquals(expected.rows().getLast(), actual.rows().getLast(), "Should match");
    }

    @Test
    void test_findGreaterDate_no_match() throws FileNotFoundException, SQLException {
        scriptRunner.runScript(new FileReader("src/test/resources/TestPostgresRepository/test_findGreaterDate_test_data.sql"));

        final List<String> columns = Arrays.asList("account_created", "last_updated", "last_login");

        final String tableName = "account";
        final LocalDate date = LocalDate.parse("2024-12-12");
        final Table actual = repository.findGreaterDate(tableName, columns, date);
        assertTrue(actual.rows().isEmpty(), "should be empty");
    }

    @Test
    void test_findGreaterDate_one_match() throws SQLException, FileNotFoundException {
        scriptRunner.runScript(new FileReader("src/test/resources/TestPostgresRepository/test_findGreaterDate_test_data.sql"));

        final List<String> columns = Arrays.asList("account_created", "last_updated", "last_login");

        final String tableName = "account";
        final LocalDate date = LocalDate.parse("2024-05-03");
        final Table actual = repository.findGreaterDate(tableName, columns, date);
        final Table expected = new Table(tableName, List.of(
                new Row(Arrays.asList(
                        new ColumnValue("id", "4"),
                        new ColumnValue("first_name", "Conni"),
                        new ColumnValue("last_name", "Jennemann"),
                        new ColumnValue("account_created", "2023-06-13"),
                        new ColumnValue("last_updated", "2024-05-04 11:06:29"),
                        new ColumnValue("last_login", "2023-06-13 03:20:04+02")
                ))
        ));
        assertEquals(expected.rows().size(), actual.rows().size(), "Wrong number of rows");
        assertEquals(expected.rows().getFirst(), actual.rows().getFirst(), "Should match");
    }

    @Test
    void test_findGreaterDate_multiple_match() throws FileNotFoundException, SQLException {
        scriptRunner.runScript(new FileReader("src/test/resources/TestPostgresRepository/test_findGreaterDate_test_data.sql"));

        final List<String> columns = Arrays.asList("account_created", "last_updated", "last_login");

        final String tableName = "account";
        final LocalDate date = LocalDate.parse("2024-04-01");
        final Table actual = repository.findGreaterDate(tableName, columns, date);
        final Table expected = new Table(tableName, List.of(
                new Row(Arrays.asList(
                        new ColumnValue("id", "1"),
                        new ColumnValue("first_name", "Patrica"),
                        new ColumnValue("last_name", "Bront"),
                        new ColumnValue("account_created", "2024-05-03"),
                        new ColumnValue("last_updated", "2023-08-24 10:12:24"),
                        new ColumnValue("last_login", "2023-06-13 03:20:04+02")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("id", "4"),
                        new ColumnValue("first_name", "Conni"),
                        new ColumnValue("last_name", "Jennemann"),
                        new ColumnValue("account_created", "2023-06-13"),
                        new ColumnValue("last_updated", "2024-05-04 11:06:29"),
                        new ColumnValue("last_login", "2023-06-13 03:20:04+02")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("id", "5"),
                        new ColumnValue("first_name", "Estella"),
                        new ColumnValue("last_name", "Markie"),
                        new ColumnValue("account_created", "2023-09-06"),
                        new ColumnValue("last_updated", "2024-03-12 17:39:27"),
                        new ColumnValue("last_login", "2024-04-12 17:39:27+02")
                ))
        ));
        assertEquals(expected.rows().size(), actual.rows().size(), "Wrong number of rows");
        assertEquals(expected.rows().getFirst(), actual.rows().getFirst(), "Should match");
        assertEquals(expected.rows().get(1), actual.rows().get(1), "Should match");
        assertEquals(expected.rows().getLast(), actual.rows().getLast(), "Should match");
    }

    @Test
    void test_findInRangeNumeric_no_match() throws SQLException, FileNotFoundException {
        scriptRunner.runScript(new FileReader("src/test/resources/TestPostgresRepository/test_findInRangeNumeric_test_data.sql"));

        final List<String> columns = Arrays.asList("id", "zipcode", "balance", "votes", "bought_books");

        final String tableName = "account";
        final BigDecimal from = BigDecimal.valueOf(98278675);
        final BigDecimal to = BigDecimal.valueOf(100000000);
        final Table actual = repository.findInRangeNumeric(tableName, columns, from, to);
        assertTrue(actual.rows().isEmpty(), "should be empty");
    }

    @Test
    void test_findInRangeNumeric_one_match() throws SQLException, FileNotFoundException {
        scriptRunner.runScript(new FileReader("src/test/resources/TestPostgresRepository/test_findInRangeNumeric_test_data.sql"));

        final List<String> columns = Arrays.asList("id", "zipcode", "balance", "votes", "bought_books");

        final String tableName = "account";
        final BigDecimal from = BigDecimal.valueOf(80);
        final BigDecimal to = BigDecimal.valueOf(100);
        final Table actual = repository.findInRangeNumeric(tableName, columns, from, to);
        final Table expected = new Table(tableName, List.of(
                new Row(Arrays.asList(
                        new ColumnValue("id", "4"),
                        new ColumnValue("first_name", "Conni"),
                        new ColumnValue("last_name", "Jennemann"),
                        new ColumnValue("account_created", "2024-05-03 11:06:29"),
                        new ColumnValue("username", "cjennemann3"),
                        new ColumnValue("email", "cjennemann3@whitehouse.gov"),
                        new ColumnValue("password", "sJ3@y)c|`w{ku"),
                        new ColumnValue("zipcode", "88"),
                        new ColumnValue("balance", "$1,958,874.00"),
                        new ColumnValue("votes", "474797"),
                        new ColumnValue("bought_books", "546353")
                ))
        ));
        assertEquals(expected.rows().size(), actual.rows().size(), "Wrong number of rows");
        assertEquals(expected.rows().getFirst(), actual.rows().getFirst(), "Should match");
    }

    @Test
    void test_findInRangeNumeric_multiple_match() throws SQLException, FileNotFoundException {
        scriptRunner.runScript(new FileReader("src/test/resources/TestPostgresRepository/test_findInRangeNumeric_test_data.sql"));

        final List<String> columns = Arrays.asList("id", "zipcode", "balance", "votes", "bought_books");

        final String tableName = "account";
        final BigDecimal from = BigDecimal.valueOf(90000000);
        final BigDecimal to = BigDecimal.valueOf(100000000);
        final Table actual = repository.findInRangeNumeric(tableName, columns, from, to);
        final Table expected = new Table(tableName, List.of(
                new Row(Arrays.asList(
                        new ColumnValue("id", "7"),
                        new ColumnValue("first_name", "Yvonne"),
                        new ColumnValue("last_name", "Biaggetti"),
                        new ColumnValue("account_created", "2024-01-04 01:24:35"),
                        new ColumnValue("username", "ybiaggetti6"),
                        new ColumnValue("email", "ybiaggetti6@ning.com"),
                        new ColumnValue("password", "bB5$4''AkxnC>uK("),
                        new ColumnValue("zipcode", "11247"),
                        new ColumnValue("balance", "$94,935,074.00"),
                        new ColumnValue("votes", "479981"),
                        new ColumnValue("bought_books", "107847")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("id", "9"),
                        new ColumnValue("first_name", "Tye"),
                        new ColumnValue("last_name", "Heintzsch"),
                        new ColumnValue("account_created", "2024-03-01 18:11:51"),
                        new ColumnValue("username", "theintzsch8"),
                        new ColumnValue("email", "theintzsch8@newyorker.com"),
                        new ColumnValue("password", "hE1#zsv=P0"),
                        new ColumnValue("zipcode", "24442"),
                        new ColumnValue("balance", "$96,443,590.00"),
                        new ColumnValue("votes", "827467"),
                        new ColumnValue("bought_books", "928780")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("id", "10"),
                        new ColumnValue("first_name", "Claudianus"),
                        new ColumnValue("last_name", "Hirsch"),
                        new ColumnValue("account_created", "2023-10-22 13:53:38"),
                        new ColumnValue("username", "chirsch9"),
                        new ColumnValue("email", "chirsch9@fc2.com"),
                        new ColumnValue("password", "fR1`C=>yQCP"),
                        new ColumnValue("zipcode", "618"),
                        new ColumnValue("balance", "$97,278,675.00"),
                        new ColumnValue("votes", "316616"),
                        new ColumnValue("bought_books", "358568")
                ))
        ));
        assertEquals(expected.rows().size(), actual.rows().size(), "Wrong number of rows");
        assertEquals(expected.rows().getFirst(), actual.rows().getFirst(), "Should match");
        assertEquals(expected.rows().get(1), actual.rows().get(1), "Should match");
        assertEquals(expected.rows().getLast(), actual.rows().getLast(), "Should match");
    }

    @Test
    void test_findTableColumnNamesAll_no_match() throws SQLException, FileNotFoundException {
        scriptRunner.runScript(new FileReader("src/test/resources/TestPostgresRepository/test_findTableColumnNamesAll_test_data.sql"));

        final List<String> actual = repository.findTableColumnNamesAll("test");

        assertTrue(actual.isEmpty(), "should be empty");
    }

    @Test
    void test_findTableColumnNamesAll() throws SQLException, FileNotFoundException {
        scriptRunner.runScript(new FileReader("src/test/resources/TestPostgresRepository/test_findTableColumnNamesAll_test_data.sql"));

        final List<String> expected = Arrays.asList("id", "first_name", "last_name", "account_created", "username", "email", "password");
        final List<String> actual = repository.findTableColumnNamesAll("account");

        assertEquals(expected.size(), actual.size(), "Wrong number of columns");
        assertTrue(expected.containsAll(actual));
        assertTrue(actual.containsAll(expected));
    }

    @Test
    void test_findTableColumnNamesNumeric_no_match() throws SQLException, FileNotFoundException {
        scriptRunner.runScript(new FileReader("src/test/resources/TestPostgresRepository/test_findTableColumnNamesNumeric_test_data.sql"));

        final List<String> actual = repository.findTableColumnNamesNumeric("test");

        assertTrue(actual.isEmpty(), "should be empty");
    }

    @Test
    void test_findTableColumnNamesNumeric() throws SQLException, FileNotFoundException {
        scriptRunner.runScript(new FileReader("src/test/resources/TestPostgresRepository/test_findTableColumnNamesNumeric_test_data.sql"));

        final List<String> expected = Arrays.asList("id", "zipcode", "balance", "votes", "bought_books");
        final List<String> actual = repository.findTableColumnNamesNumeric("account");

        assertEquals(expected.size(), actual.size(), "Wrong number of columns");
        assertTrue(expected.containsAll(actual));
        assertTrue(actual.containsAll(expected));
    }

    @Test
    void test_findTableColumnNamesDate_no_match() throws SQLException, FileNotFoundException {
        scriptRunner.runScript(new FileReader("src/test/resources/TestPostgresRepository/test_findTableColumnNamesDate_test_data.sql"));

        final List<String> actual = repository.findTableColumnNamesDate("test");

        assertTrue(actual.isEmpty(), "should be empty");
    }

    @Test
    void test_findTableColumnNamesDate() throws SQLException, FileNotFoundException {
        scriptRunner.runScript(new FileReader("src/test/resources/TestPostgresRepository/test_findTableColumnNamesDate_test_data.sql"));

        final List<String> expected = Arrays.asList("account_created", "last_updated", "last_login");
        final List<String> actual = repository.findTableColumnNamesDate("account");

        assertEquals(expected.size(), actual.size(), "Wrong number of columns");
        assertTrue(expected.containsAll(actual));
        assertTrue(actual.containsAll(expected));
    }
}
