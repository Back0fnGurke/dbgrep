package de.hdm_stuttgart.mi.dad.core.property;

import java.util.Arrays;
import java.util.List;

/**
 * The PropertyType enum represents the type of a Property.
 * It provides several constants representing different types of properties, such as GREATER_NUMERIC, GREATER_DATE, EQUAL, LIKE, REGEX, and RANGE_NUMERIC.
 * <p>
 * The enum provides two static methods: getNumericTypes and getDateTypes.
 * The getNumericTypes method returns a list of PropertyType constants that represent numeric types.
 * The getDateTypes method returns a list of PropertyType constants that represent date types.
 * <p>
 * Example usage:
 * <p>
 * PropertyType type = PropertyType.GREATER_NUMERIC;  // Represents a numeric property with a "greater than" condition
 */
public enum PropertyType {
    GREATER_NUMERIC,
    GREATER_DATE,
    EQUAL,
    LIKE,
    REGEX,
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
