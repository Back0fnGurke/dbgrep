package de.hdm_stuttgart.mi.dad.incoming.output;

import de.hdm_stuttgart.mi.dad.core.property.Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Utility class for matching values against properties.
 */
public class ValueMatcher {

    private static final Logger log = LoggerFactory.getLogger(ValueMatcher.class);

    private ValueMatcher() {
    }

    /**
     * Evaluates if the value is a match for the properties.
     *
     * @param value      a String value
     * @param properties a list with values of type Property
     * @return true if the value matches one of the properties
     */
    public static boolean isMatch(final String value, final List<Property<?>> properties) {
        log.debug("Called with value: {}, properties: {}", value, properties);
        final String checkedValue = (value == null) ? "null" : value;

        boolean isMatch = false;
        for (final Property<?> property : properties) {
            switch (property.getType()) {
                case REGEX -> isMatch = isRegexMatch(checkedValue, property);
                case LIKE -> isMatch = isLikeMatch(checkedValue, property);
                case EQUAL -> isMatch = isEqualMatch(checkedValue, property);
                case GREATER_NUMERIC -> isMatch = isGreaterNumericMatch(checkedValue, property);
                case GREATER_DATE -> isMatch = isGreaterDateMatch(checkedValue, property);
                case RANGE_NUMERIC -> isMatch = isRangeNumericMatch(checkedValue, property);
                default -> throw new IllegalArgumentException("Unexpected value: " + property.getType());
            }
            if (isMatch) break;
        }
        log.debug("Returning: {}", isMatch);
        return isMatch;
    }

    private static boolean isRegexMatch(final String value, final Property<?> property) {
        log.debug("Called with value: {}, property: {}", value, property);
        final boolean result = Pattern.compile(property.getValue().toString()).matcher(value).matches();
        log.debug("Returning: {}", result);
        return result;
    }

    private static boolean isLikeMatch(final String value, final Property<?> property) {
        log.debug("Called with value: {}, property: {}", value, property);
        final String regex = property.getValue().toString()
                .replace("_", ".")
                .replace("%", ".*?");
        final Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        final boolean result = p.matcher(value).matches();
        log.debug("Returning: {}", result);
        return result;
    }

    private static boolean isEqualMatch(final String value, final Property<?> property) {
        log.debug("Called with value: {}, property: {}", value, property);
        try {
            final BigDecimal numericValue = new BigDecimal(value);
            final boolean result = ((BigDecimal) property.getValue()).compareTo(numericValue) == 0;
            log.debug("Returning: {}", result);
            return result;
        } catch (NumberFormatException e) {
            log.debug("Returning: false due to NumberFormatException");
            return false;
        }
    }

    private static boolean isGreaterNumericMatch(final String value, final Property<?> property) {
        log.debug("Called with value: {}, property: {}", value, property);
        try {
            final BigDecimal numericValue = new BigDecimal(value);
            final boolean result = ((BigDecimal) property.getValue()).compareTo(numericValue) < 0;
            log.debug("Returning: {}", result);
            return result;
        } catch (NumberFormatException e) {
            log.debug("Returning: false due to NumberFormatException");
            return false;
        }
    }

    private static boolean isGreaterDateMatch(final String value, final Property<?> property) {
        log.debug("Called with value: {}, property: {}", value, property);
        try {
            final LocalDate dateValue;
            if (value.contains(" ")) {
                dateValue = LocalDateTime.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toLocalDate();
            } else {
                dateValue = LocalDate.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            }
            final boolean result = ((LocalDate) property.getValue()).isBefore(dateValue);
            log.debug("Returning: {}", result);
            return result;
        } catch (DateTimeParseException e) {
            log.debug("Returning: false due to DateTimeParseException");
            return false;
        }
    }

    private static boolean isRangeNumericMatch(final String value, final Property<?> property) {
        log.debug("Called with value: {}, property: {}", value, property);
        try {
            final BigDecimal numericValue = new BigDecimal(value);
            final BigDecimal[] range = (BigDecimal[]) property.getValue();
            final boolean result = range[0].compareTo(numericValue) <= 0 && range[1].compareTo(numericValue) >= 0;
            log.debug("Returning: {}", result);
            return result;
        } catch (NumberFormatException e) {
            log.debug("Returning: false due to NumberFormatException");
            return false;
        }
    }
}