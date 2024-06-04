package de.hdm_stuttgart.mi.dad.core.property;

import java.util.Arrays;
import java.util.List;

public enum PropertyType {
    GREATERNUMERIC,
    GREATERDATE,
    EQUAL,
    LIKE,
    REGEX,
    RANGENUMERIC;

    static final List<PropertyType> numericTypes = Arrays.asList(GREATERNUMERIC, RANGENUMERIC, EQUAL);
    static final List<PropertyType> dateTypes = List.of(GREATERDATE);

    public static List<PropertyType> getNumericTypes() {
        return List.copyOf(numericTypes);
    }

    public static List<PropertyType> getDateTypes() {
        return List.copyOf(dateTypes);
    }
}
