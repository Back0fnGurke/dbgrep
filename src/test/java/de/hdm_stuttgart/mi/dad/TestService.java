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
        final List<List<String>> foundRows1 = Arrays.asList(
                Arrays.asList("4", "Caroline", "Aubri", "2020-04-09 09:47:58", "caubri3", "caubri3@auda.org.au", "gK7*2Eit"),
                Arrays.asList("7", "Candace", "Breslauer", "2019-02-05 15:18:33", "cbreslauer6", "cbreslauer6@hao123.com", "tQ8/vr&."),
                Arrays.asList("9", "Laurel", "Norrie", "2020-11-15 17:37:15", "lnorrie8", "lnorrie8@google.ru", "mQ6}y=B8+eK")
        );
        final List<List<String>> foundRows2 = Arrays.asList(
                Arrays.asList("1", "The Last of Us", "survival shooter horror game", "horror", "30"),
                Arrays.asList("2", "Hollow knight", "play as a bug", "Metroidvania", "15"),
                Arrays.asList("3", "Helldivers 2", "bring democracy to the galaxy", "coop shooter", "40")
        );
        final List<List<String>> foundRows3 = Arrays.asList(
                Arrays.asList("1", "1", "4", "10/10 would cry again"),
                Arrays.asList("2", "2", "7", "10/10 haven't seen the light of day since weeks"),
                Arrays.asList("3", "3", "9", "10/10 scared of my roomba now")
        );

        when(repository.findTableColumns(table1)).thenReturn(columnNames1);
        when(repository.findTableColumns(table2)).thenReturn(columnNames2);
        when(repository.findTableColumns(table3)).thenReturn(columnNames3);
        when(repository.findInTable(table1, columnNames1, pattern)).thenReturn(foundRows1);
        when(repository.findInTable(table2, columnNames2, pattern)).thenReturn(foundRows2);
        when(repository.findInTable(table3, columnNames3, pattern)).thenReturn(foundRows3);


        final Map<String, List<List<String>>> expected1 = Map.ofEntries(
                entry(table1, foundRows1),
                entry(table2, foundRows2),
                entry(table3, foundRows3)
        );

        final Map<String, List<List<String>>> actual1 = service.searchTable(List.of(table1, table2, table3), pattern);

        assertEquals(expected1.size(), actual1.size());
        assertEquals(expected1.get(table1).size(), actual1.get(table1).size());
        assertEquals(expected1.get(table2).size(), actual1.get(table2).size());
        assertEquals(expected1.get(table3).size(), actual1.get(table3).size());
        assertEquals(expected1.get(table1), actual1.get(table1));
        assertEquals(expected1.get(table2), actual1.get(table2));
        assertEquals(expected1.get(table3), actual1.get(table3));
    }
}
