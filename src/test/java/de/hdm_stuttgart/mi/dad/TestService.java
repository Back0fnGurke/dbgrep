package de.hdm_stuttgart.mi.dad;

import de.hdm_stuttgart.mi.dad.core.Service;
import de.hdm_stuttgart.mi.dad.ports.RepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static java.util.Map.entry;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

        final String table1 = "account";
        final String table2 = "game";
        final String table3 = "review";
        final Pattern pattern = Pattern.compile("");
        final List<String> columnNames1 = Arrays.asList("id", "first_name", "last_name", "account_created", "username", "email", "password");
        final List<String> columnNames2 = Arrays.asList("id", "name", "description", "genre", "price");
        final List<String> columnNames3 = Arrays.asList("id", "game", "account", "review");
        final List<Map<String, String>> foundRows1 = Arrays.asList(
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
                ),
                Map.ofEntries(
                        entry("id", "9"),
                        entry("first_name", "Laurel"),
                        entry("last_name", "Norrie"),
                        entry("account_created", "2020-11-15 17:37:15"),
                        entry("username", "lnorrie8"),
                        entry("email", "lnorrie8@google.ru"),
                        entry("password", "mQ6}y=B8+eK")
                )
        );
        final List<Map<String, String>> foundRows2 = Arrays.asList(
                Map.ofEntries(
                        entry("id", "1"),
                        entry("name", "The Last of Us"),
                        entry("description", "survival shooter horror game"),
                        entry("genre", "horror"),
                        entry("price", "30")
                ),
                Map.ofEntries(
                        entry("id", "2"),
                        entry("name", "Hollow knight"),
                        entry("description", "play as a bug"),
                        entry("genre", "Metroidvania"),
                        entry("price", "15")
                ),
                Map.ofEntries(
                        entry("id", "3"),
                        entry("name", "Helldivers 2"),
                        entry("description", "bring democracy to the galaxy"),
                        entry("genre", "coop shooter"),
                        entry("price", "40")
                )
        );
        final List<Map<String, String>> foundRows3 = Arrays.asList(
                Map.ofEntries(
                        entry("id", "1"),
                        entry("game", "1"),
                        entry("account", "4"),
                        entry("review", "10/10 would cry again")
                ),
                Map.ofEntries(
                        entry("id", "2"),
                        entry("game", "2"),
                        entry("account", "7"),
                        entry("review", "10/10 haven't seen the light of day since weeks")
                ),
                Map.ofEntries(
                        entry("id", "3"),
                        entry("game", "3"),
                        entry("account", "9"),
                        entry("review", "10/10 scared of my roomba now")
                )
        );

        when(repository.findTableColumns(table1)).thenReturn(columnNames1);
        when(repository.findTableColumns(table2)).thenReturn(columnNames2);
        when(repository.findTableColumns(table3)).thenReturn(columnNames3);
        when(repository.findInTable(table1, columnNames1, pattern)).thenReturn(foundRows1);
        when(repository.findInTable(table2, columnNames2, pattern)).thenReturn(foundRows2);
        when(repository.findInTable(table3, columnNames3, pattern)).thenReturn(foundRows3);


        final Map<String, List<Map<String, String>>> expected1 = Map.ofEntries(
                entry(table1, foundRows1),
                entry(table2, foundRows2),
                entry(table3, foundRows3)
        );
        final Map<String, List<Map<String, String>>> actual1 = service.searchTable(List.of(table1, table2, table3), pattern);
        assertEquals(expected1.size(), actual1.size());
        assertEquals(expected1.get(table1).size(), actual1.get(table1).size());
        assertEquals(expected1.get(table2).size(), actual1.get(table2).size());
        assertEquals(expected1.get(table3).size(), actual1.get(table3).size());

        assertEquals(expected1.get(table1).getFirst(), actual1.get(table1).getFirst());
        assertEquals(expected1.get(table1).get(1), actual1.get(table1).get(1));
        assertEquals(expected1.get(table1).get(2), actual1.get(table1).get(2));
        assertEquals(expected1.get(table2).getFirst(), actual1.get(table2).getFirst());
        assertEquals(expected1.get(table2).get(1), actual1.get(table2).get(1));
        assertEquals(expected1.get(table2).get(2), actual1.get(table2).get(2));
        assertEquals(expected1.get(table3).getFirst(), actual1.get(table3).getFirst());
        assertEquals(expected1.get(table3).get(1), actual1.get(table3).get(1));
        assertEquals(expected1.get(table3).get(2), actual1.get(table3).get(2));
    }
}
