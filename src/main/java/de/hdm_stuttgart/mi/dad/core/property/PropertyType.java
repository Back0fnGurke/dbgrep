package de.hdm_stuttgart.mi.dad.core.property;

import java.util.Arrays;
import java.util.List;

/**
 * The PropertyType enum represents the type of a Property.
 * It provides several constants representing different types of properties, such as GREATERNUMERIC, GREATERDATE, EQUAL, LIKE, REGEX, and RANGENUMERIC.
 * <p>
 * The enum provides two static methods: getNumericTypes and getDateTypes.
 * The getNumericTypes method returns a list of PropertyType constants that represent numeric types.
 * The getDateTypes method returns a list of PropertyType constants that represent date types.
 * <p>
 * Example usage:
 * <p>
 * PropertyType type = PropertyType.GREATERNUMERIC;  // Represents a numeric property with a "greater than" condition
 */
public enum PropertyType {
    GREATERNUMERIC,
    GREATERDATE,
    EQUAL,
    LIKE,
    REGEX,
    RANGENUMERIC;

    static final List<PropertyType> numericTypes = Arrays.asList(GREATERNUMERIC, RANGENUMERIC, EQUAL);
    static final List<PropertyType> dateTypes = List.of(GREATERDATE);

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
