package de.hdm_stuttgart.mi.dad;
import de.hdm_stuttgart.mi.dad.core.entity.Table;
import de.hdm_stuttgart.mi.dad.core.exception.ServiceException;
import de.hdm_stuttgart.mi.dad.core.ports.ServicePort;
import de.hdm_stuttgart.mi.dad.incoming.InputHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

public class TestInputHandler {
    @Mock
    ServicePort mockServicePort;
    InputHandler inputHandler;

    @BeforeEach
    void setUp() {
        reset(mockServicePort);
        inputHandler = new InputHandler(mockServicePort);
    }

    @Test
    void testSearchWholeDatabase() throws ServiceException, IOException {
        String[] args = {"--like", "someProperty"};
        List<Table> tables = List.of(new Table("table1", Collections.emptyList()));
        when(mockServicePort.searchThroughWholeDatabase(anyList())).thenReturn(tables);

        inputHandler.handleInput(args);
        verify(mockServicePort).searchThroughWholeDatabase(anyList());
    }

    @Test
    void testPropertyList(){
    }

    @Test
    void testSearchSpecificTables() throws ServiceException, IOException {
        String[] args = {"--table", "table1", "--like", "someProperty"};
        List<Table> tables = List.of(new Table("table1", Collections.emptyList()));
        when(mockServicePort.searchThroughTables(anyList(), anyList())).thenReturn(tables);

        inputHandler.handleInput(args);
        verify(mockServicePort).searchThroughTables(anyList(), anyList());
    }

    @Test
    void testSearchSpecificColumns() throws ServiceException, IOException {
        String[] args = {"--column", "table1.col1", "--like", "someProperty"};
        List<Table> tables = List.of(new Table("table1", Collections.emptyList()));
        when(mockServicePort.searchThroughColumns(anyMap(), anyList())).thenReturn(tables);

        inputHandler.handleInput(args);
        verify(mockServicePort).searchThroughColumns(anyMap(), anyList());
    }

    @Test
    void testPrintManual() throws IOException, ServiceException {
        String[] args = {"--help"};
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        inputHandler.handleInput(args);

        assert(outContent.toString().contains("DESCRIPTION"));
    }
}
