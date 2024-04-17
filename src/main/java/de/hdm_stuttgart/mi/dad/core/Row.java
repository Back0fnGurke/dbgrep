package de.hdm_stuttgart.mi.dad.core;

import java.util.List;

public class Row {

    private final List<ColumnValue> columnsOfRow;

    public Row(List<ColumnValue> columnsOfRow) {
        this.columnsOfRow = columnsOfRow;
    }

    public List<ColumnValue> getColumnsOfRow() {
        return columnsOfRow;
    }
}
