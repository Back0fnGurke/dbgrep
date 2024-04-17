package de.hdm_stuttgart.mi.dad.core;

public class ColumnValue {

    private final String name;
    private final String value;

    public ColumnValue(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
