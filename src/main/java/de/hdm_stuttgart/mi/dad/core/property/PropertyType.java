package de.hdm_stuttgart.mi.dad.core.property;

import java.util.Arrays;
import java.util.List;

/**
 * Enum representing different types of properties.
 */
public enum PropertyType {
    /**
     * Property type for greater operation with numeric values.
     */
    GREATER_NUMERIC,
    /**
     * Property type for greater operation with date values.
     */
    GREATER_DATE,
    /**
     * Property type for equal operation.
     */
    EQUAL,
    /**
     * Property type for like operation.
     */
    LIKE,
    /**
     * Property type for regex operation.
     */
    REGEX,
    /**
     * Property type for range operation with numeric values.
     */
    RANGE_NUMERIC;

    static final List<PropertyType> numericTypes = Arrays.asList(GREATER_NUMERIC, RANGE_NUMERIC, EQUAL);
    static final List<PropertyType> dateTypes = List.of(GREATER_DATE);

    /**
     * Returns a list of PropertyType constants that represent numeric types.
     *
     * @return a list of PropertyType constants that represent numeric types.
     */
    public static List<PropertyType> getNumericTypes() {
        return List.copyOf(numericTypes);
    }

    /**
     * Returns a list of PropertyType constants that represent date types.
     *
     * @return a list of PropertyType constants that represent date types.
     */
    public static List<PropertyType> getDateTypes() {
        return List.copyOf(dateTypes);
    }
}
