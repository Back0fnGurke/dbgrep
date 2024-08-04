package de.hdm_stuttgart.mi.dad.incoming.output;

import de.hdm_stuttgart.mi.dad.core.entity.ColumnValue;
import de.hdm_stuttgart.mi.dad.core.property.Property;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Storing data of a single row column with the information if it is a match.
 */
public record ColumnValueOutput(String name, String value, boolean isMatch) {

    public ColumnValueOutput(ColumnValue column, List<Property<?>> properties) {
        this(column.name(), column.value(), isMatch(column.value(), properties));
    }

    /**
     * Evaluates if the value is a match for the properties.
     *
     * @param value      a String value
     * @param properties a list with values of type Property
     * @return true if the value matches one of the properties
     */
    private static boolean isMatch(String value, List<Property<?>> properties) {
        for (Property<?> property : properties) {
            switch (property.getType()) {
                case REGEX -> {
                    return isRegexMatch(value, property);
                }
                case LIKE -> {
                    return isLikeMatch(value, property);
                }
                case EQUAL -> {
                    return isEqualMatch(value, property);
                }
                case GREATER_NUMERIC -> {
                    return isGreaterNumericMatch(value, property);
                }
                case GREATER_DATE -> {
                    return isGreaterDateMatch(value, property);
                }
                case RANGE_NUMERIC -> {
                    return isRangeNumericMatch(value, property);
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
    private static boolean isRegexMatch(String value, Property<?> property) {
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
    private static boolean isLikeMatch(String value, Property<?> property) {
        String regex = property.getValue().toString();
        regex = regex.replace("_", ".").replace("%", ".*?");
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
    private static boolean isEqualMatch(String value, Property<?> property) {
        return property.getValue().equals(new BigDecimal(value));
    }

    /**
     * Checks if the given value is greater than the numeric value specified in the property.
     *
     * @param value    the value to check
     * @param property the property containing the numeric value to compare
     * @return true if the value is greater, false otherwise
     */
    private static boolean isGreaterNumericMatch(String value, Property<?> property) {
        return ((BigDecimal) property.getValue()).compareTo(new BigDecimal(value)) < 0;
    }

    /**
     * Checks if the given value is greater than the date value specified in the property.
     *
     * @param value    the value to check
     * @param property the property containing the date value to compare
     * @return true if the value is greater, false otherwise
     */
    private static boolean isGreaterDateMatch(String value, Property<?> property) {
        return ((LocalDate) property.getValue()).isBefore(LocalDate.parse(value));
    }

    /**
     * Checks if the given value is within the numeric range specified in the property.
     *
     * @param value    the value to check
     * @param property the property containing the numeric range
     * @return true if the value is within the range, false otherwise
     */
    private static boolean isRangeNumericMatch(String value, Property<?> property) {
        final BigDecimal[] range = (BigDecimal[]) property.getValue();
        return range[0].compareTo(new BigDecimal(value)) <= 0 && range[1].compareTo(new BigDecimal(value)) >= 0;
    }
}