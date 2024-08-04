package de.hdm_stuttgart.mi.dad.incoming.output;

import de.hdm_stuttgart.mi.dad.core.property.Property;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Utility class for matching values against properties.
 */
public class ValueMatcher {

    /**
     * Evaluates if the value is a match for the properties.
     *
     * @param value      a String value
     * @param properties a list with values of type Property
     * @return true if the value matches one of the properties
     */
    public static boolean isMatch(final String value, final List<Property<?>> properties) {
        final String checkedValue = (value == null) ? "null" : value;

        for (Property<?> property : properties) {
            switch (property.getType()) {
                case REGEX -> {
                    return isRegexMatch(checkedValue, property);
                }
                case LIKE -> {
                    return isLikeMatch(checkedValue, property);
                }
                case EQUAL -> {
                    return isEqualMatch(checkedValue, property);
                }
                case GREATER_NUMERIC -> {
                    return isGreaterNumericMatch(checkedValue, property);
                }
                case GREATER_DATE -> {
                    return isGreaterDateMatch(checkedValue, property);
                }
                case RANGE_NUMERIC -> {
                    return isRangeNumericMatch(checkedValue, property);
                }
                default -> throw new IllegalArgumentException("Unexpected value: " + property.getType());
            }
        }
        return false;
    }

    /**
     * Checks if the given value matches the regex pattern specified in the property.
     *
     * @param value    the value to check
     * @param property the property containing the regex pattern
     * @return true if the value matches the regex pattern, false otherwise
     */
    private static boolean isRegexMatch(final String value, final Property<?> property) {
        return Pattern.compile(property.getValue().toString()).matcher(value).matches();
    }

    /**
     * Checks if the given value matches the LIKE pattern specified in the property.
     * The LIKE pattern supports SQL-like wildcards:
     * - \_ matches any single character
     * - \% matches any sequence of characters
     *
     * @param value    the value to check
     * @param property the property containing the LIKE pattern
     * @return true if the value matches the LIKE pattern, false otherwise
     */
    private static boolean isLikeMatch(final String value, final Property<?> property) {
        final String regex = property.getValue().toString()
                .replace("_", ".")
                .replace("%", ".*?");
        final Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        return p.matcher(value).matches();
    }

    /**
     * Checks if the given value is equal to the value specified in the property.
     *
     * @param value    the value to check
     * @param property the property containing the value to compare
     * @return true if the values are equal, false otherwise
     */
    private static boolean isEqualMatch(final String value, final Property<?> property) {
        return property.getValue().equals(new BigDecimal(value));
    }

    /**
     * Checks if the given value is greater than the numeric value specified in the property.
     *
     * @param value    the value to check
     * @param property the property containing the numeric value to compare
     * @return true if the value is greater, false otherwise
     */
    private static boolean isGreaterNumericMatch(final String value, final Property<?> property) {
        return ((BigDecimal) property.getValue()).compareTo(new BigDecimal(value)) < 0;
    }

    /**
     * Checks if the given value is greater than the date value specified in the property.
     *
     * @param value    the value to check
     * @param property the property containing the date value to compare
     * @return true if the value is greater, false otherwise
     */
    private static boolean isGreaterDateMatch(final String value, final Property<?> property) {
        return ((LocalDate) property.getValue()).isBefore(LocalDate.parse(value));
    }

    /**
     * Checks if the given value is within the numeric range specified in the property.
     *
     * @param value    the value to check
     * @param property the property containing the numeric range
     * @return true if the value is within the range, false otherwise
     */
    private static boolean isRangeNumericMatch(final String value, final Property<?> property) {
        final BigDecimal[] range = (BigDecimal[]) property.getValue();
        return range[0].compareTo(new BigDecimal(value)) <= 0 && range[1].compareTo(new BigDecimal(value)) >= 0;
    }
}
