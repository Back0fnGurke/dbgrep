package de.hdm_stuttgart.mi.dad;

import de.hdm_stuttgart.mi.dad.core.Service;
import de.hdm_stuttgart.mi.dad.core.entity.ColumnValue;
import de.hdm_stuttgart.mi.dad.core.entity.Row;
import de.hdm_stuttgart.mi.dad.core.entity.Table;
import de.hdm_stuttgart.mi.dad.ports.RepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TestService {

    @Mock
    RepositoryPort repository;
    Service service;

    @BeforeEach
    void setUp() {
        service = new Service(repository);
    }

    @Test
    void test_searchTable() throws Exception {
        assertNotNull(repository);

        final String tableName1 = "account";
        final String tableName2 = "game";
        final String tableName3 = "review";
        final Pattern pattern = Pattern.compile("");
        final List<String> columnNames1 = Arrays.asList("id", "first_name", "last_name", "account_created", "username", "email", "password");
        final List<String> columnNames2 = Arrays.asList("id", "name", "description", "genre", "price");
        final List<String> columnNames3 = Arrays.asList("id", "game", "account", "review");
        final Table table1 = new Table(tableName1, Arrays.asList(
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
                )),
                new Row(Arrays.asList(
                        new ColumnValue("id", "9"),
                        new ColumnValue("first_name", "Laurel"),
                        new ColumnValue("last_name", "Norrie"),
                        new ColumnValue("account_created", "2020-11-15 17:37:15"),
                        new ColumnValue("username", "lnorrie8"),
                        new ColumnValue("email", "lnorrie8@google.ru"),
                        new ColumnValue("password", "mQ6}y=B8+eK")
                ))
        ));
        final Table table2 = new Table(tableName2, Arrays.asList(
                new Row(Arrays.asList(
                        new ColumnValue("id", "1"),
                        new ColumnValue("name", "The Last of Us"),
                        new ColumnValue("description", "survival shooter horror game"),
                        new ColumnValue("genre", "horror"),
                        new ColumnValue("price", "30")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("id", "2"),
                        new ColumnValue("name", "Hollow knight"),
                        new ColumnValue("description", "play as a bug"),
                        new ColumnValue("genre", "Metroidvania"),
                        new ColumnValue("price", "15")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("id", "3"),
                        new ColumnValue("name", "Helldivers 2"),
                        new ColumnValue("description", "bring democracy to the galaxy"),
                        new ColumnValue("genre", "coop shooter"),
                        new ColumnValue("price", "40")
                ))
        ));
        final Table table3 = new Table(tableName3, Arrays.asList(
                new Row(Arrays.asList(
                        new ColumnValue("id", "1"),
                        new ColumnValue("game", "1"),
                        new ColumnValue("account", "4"),
                        new ColumnValue("review", "10/10 would cry again")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("id", "2"),
                        new ColumnValue("game", "2"),
                        new ColumnValue("account", "7"),
                        new ColumnValue("review", "10/10 haven't seen the light of day since weeks")
                )),
                new Row(Arrays.asList(
                        new ColumnValue("id", "3"),
                        new ColumnValue("game", "3"),
                        new ColumnValue("account", "9"),
                        new ColumnValue("review", "10/10 scared of my roomba now")
                ))
        ));

        when(repository.findAllTableColumnNames(tableName1)).thenReturn(columnNames1);
        when(repository.findAllTableColumnNames(tableName2)).thenReturn(columnNames2);
        when(repository.findAllTableColumnNames(tableName3)).thenReturn(columnNames3);
        when(repository.findPattern(tableName1, columnNames1, pattern)).thenReturn(table1);
        when(repository.findPattern(tableName2, columnNames2, pattern)).thenReturn(table2);
        when(repository.findPattern(tableName3, columnNames3, pattern)).thenReturn(table3);


        final List<Table> expected1 = Arrays.asList(table1, table2, table3);
        final List<Table> actual1 = service.searchTables(List.of(tableName1, tableName2, tableName3), pattern);
        assertEquals(expected1.size(), actual1.size());
        for (int i = 0; i < expected1.size(); i++) {

            Table expectedTable = expected1.get(i);
            Table actualTable = actual1.get(i);
            assertEquals(expectedTable.name(), actualTable.name());

            for (int j = 0; j < expectedTable.rows().size(); j++) {

                Row expectedRow = expectedTable.rows().get(j);
                Row actualRow = actualTable.rows().get(j);
                assertTrue(
                        expectedRow.columns().containsAll(actualRow.columns()) && actualRow.columns().containsAll(expectedRow.columns()),
                        String.format("Expected row %d in table %s: %s\n Actual row %d in table %s: %s", j, expectedTable.name(), expectedRow.columns(), j, actualTable.name(), actualRow.columns())
                );
            }
        }
    }
}
