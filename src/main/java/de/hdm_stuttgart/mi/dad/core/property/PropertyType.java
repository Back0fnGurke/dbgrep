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

    public static final List<List<PropertyType>> validPropertyCombinations = Arrays.asList(
            Arrays.asList(GREATERNUMERIC, RANGENUMERIC, REGEX, LIKE, EQUAL),
            Arrays.asList(REGEX, LIKE, GREATERDATE)
    );
}
