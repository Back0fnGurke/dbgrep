package de.hdm_stuttgart.mi.dad.core.property;

import java.util.Arrays;
import java.util.List;

//TODO: doku

public enum PropertyType {
    GREATER_NUMERIC,
    GREATER_DATE,
    EQUAL,
    LIKE,
    REGEX,
    RANGE_NUMERIC;

    static final List<PropertyType> numericTypes = Arrays.asList(GREATER_NUMERIC, RANGE_NUMERIC, EQUAL);
    static final List<PropertyType> dateTypes = List.of(GREATER_DATE);

    public static List<PropertyType> getNumericTypes() {
        return List.copyOf(numericTypes);
    }

    public static List<PropertyType> getDateTypes() {
        return List.copyOf(dateTypes);
    }
}
