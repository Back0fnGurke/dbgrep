package de.hdm_stuttgart.mi.dad.incoming;

import de.hdm_stuttgart.mi.dad.core.entity.Table;
import de.hdm_stuttgart.mi.dad.core.exception.ServiceException;
import de.hdm_stuttgart.mi.dad.core.ports.ServicePort;
import de.hdm_stuttgart.mi.dad.core.property.Property;
import de.hdm_stuttgart.mi.dad.incoming.input.SearchInput;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TestApplicationHandler {
    @Mock
    private ServicePort serviceMock;

    @InjectMocks
    private ApplicationHandler applicationHandler;

    @Test
    void testHandle_searchThroughWholeDatabase() throws ServiceException {
        // given
        final List<Property<?>> properties = new ArrayList<>();
        final SearchInput searchInput = new SearchInput(properties, new ArrayList<>(), Map.of(), false, false);
        when(serviceMock.searchThroughWholeDatabase(properties)).thenReturn(new ArrayList<>());

        // when
        final List<Table> result = applicationHandler.handle(searchInput);

        // then
        assertTrue(result.isEmpty());
        verify(serviceMock).searchThroughWholeDatabase(properties);
        verifyNoMoreInteractions(serviceMock);
    }

    @Test
    void testHandle_searchThroughTables() throws ServiceException {
        // given
        final List<Property<?>> properties = new ArrayList<>();
        final List<String> tableNames = List.of("table1", "table2");
        final SearchInput searchInput = new SearchInput(properties, tableNames, Map.of(), true, false);
        when(serviceMock.searchThroughTables(tableNames, properties)).thenReturn(new ArrayList<>());

        // when
        final List<Table> result = applicationHandler.handle(searchInput);

        // then
        assertTrue(result.isEmpty());
        verify(serviceMock).validateTableNames(tableNames);
        verify(serviceMock).searchThroughTables(tableNames, properties);
        verifyNoMoreInteractions(serviceMock);
    }

    @Test
    void testHandle_searchThroughColumns() throws ServiceException {
        // given
        final List<Property<?>> properties = new ArrayList<>();
        final Map<String, List<String>> columns = new HashMap<>();
        columns.put("table1", List.of("column1", "column2"));
        final SearchInput searchInput = new SearchInput(properties, new ArrayList<>(), columns, false, true);
        when(serviceMock.searchThroughColumns(columns, properties)).thenReturn(new ArrayList<>());

        // when
        final List<Table> result = applicationHandler.handle(searchInput);

        // then
        for (Map.Entry<String, List<String>> entry : columns.entrySet()) {
            verify(serviceMock).validateColumnNames(entry.getKey(), entry.getValue());
        }
        verify(serviceMock).searchThroughColumns(columns, properties);
        assertTrue(result.isEmpty());
        verifyNoMoreInteractions(serviceMock);
    }

    @Test
    void testHandle_searchThroughTablesAndColumns() throws ServiceException {
        // given
        final List<Property<?>> properties = new ArrayList<>();
        final List<String> tableNames = List.of("table1", "table2");
        final Map<String, List<String>> columns = new HashMap<>();
        columns.put("table1", List.of("column1", "column2"));
        final SearchInput searchInput = new SearchInput(properties, tableNames, columns, true, true);
        when(serviceMock.searchThroughColumns(columns, properties)).thenReturn(new ArrayList<>());
        when(serviceMock.searchThroughTables(tableNames, properties)).thenReturn(new ArrayList<>());

        // when
        final List<Table> result = applicationHandler.handle(searchInput);

        // Assert
        verify(serviceMock).validateTableNames(tableNames);
        for (Map.Entry<String, List<String>> entry : columns.entrySet()) {
            verify(serviceMock).validateColumnNames(entry.getKey(), entry.getValue());
        }
        verify(serviceMock).searchThroughTables(tableNames, properties);
        verify(serviceMock).searchThroughColumns(columns, properties);

        assertTrue(result.isEmpty());
        verifyNoMoreInteractions(serviceMock);
    }

    @Test
    void testHandle_ServiceException() throws ServiceException {
        // given
        final List<Property<?>> properties = new ArrayList<>();
        final SearchInput searchInput = new SearchInput(properties, new ArrayList<>(), Map.of(), false, false);
        when(serviceMock.searchThroughWholeDatabase(properties)).thenThrow(new ServiceException("Error"));

        // then
        assertThrows(ServiceException.class, () -> applicationHandler.handle(searchInput));
        verify(serviceMock).searchThroughWholeDatabase(properties);
        verifyNoMoreInteractions(serviceMock);
    }
}
