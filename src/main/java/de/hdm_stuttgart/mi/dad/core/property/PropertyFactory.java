package de.hdm_stuttgart.mi.dad.core.property;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.regex.Pattern;

/**
 * This class is responsible for creating different types of {@link Property} objects based on the {@link PropertyType} and value provided.
 * It supports the creation of the following types of {@link Property} objects:
 * <ul>
 *     <li>{@link Regex}: Requires a {@link Pattern} value.</li>
 *     <li>{@link Like}: Requires a {@link Pattern} value.</li>
 *     <li>{@link Equal}: Requires a {@link BigDecimal} value.</li>
 *     <li>{@link GreaterNumeric}: Requires a {@link BigDecimal} value.</li>
 *     <li>{@link GreaterDate}: Requires a {@link LocalDate} value.</li>
 *     <li>{@link RangeNumeric}: Requires a {@link BigDecimal[]} value.</li>
 * </ul>
 *
 * <p>Each {@link PropertyType} has a corresponding private method in this class that is responsible for creating the {@link Property} object and validating the value type.</p>
 *
 * <p>This class is not meant to be instantiated.</p>
 */
public class PropertyFactory {


    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private PropertyFactory() {
    }

    /**
     * Creates a {@link Property} object based on the provided {@link PropertyType} and value.
     *
     * @param type  the type of the property to create
     * @param value the value to use when creating the property
     * @return the created property
     * @throws IllegalArgumentException if the value type does not match the expected type for the provided property type
     */
    public static Property createProperty(PropertyType type, Object value) {
        return switch (type) {
            case REGEX -> createRegexProperty(value);
            case LIKE -> createLikeProperty(value);
            case EQUAL -> createEqualProperty(value);
            case GREATER_NUMERIC -> createGreaterNumericProperty(value);
            case GREATER_DATE -> createGreaterDateProperty(value);
            case RANGE_NUMERIC -> createRangeNumericProperty(value);
            default -> throw new IllegalArgumentException("Can not use param value with type: " + type);
        };
    }

    /**
     * Creates a {@link Regex} property object based on the provided value.
     *
     * @param value the value to use when creating the property
     * @return the created property
     * @throws IllegalArgumentException if the value type is not a {@link Pattern}
     */
    private static Property<String> createRegexProperty(Object value) {
        if (value instanceof Pattern pattern) {
            return new Regex(pattern);
        }
        throw new IllegalArgumentException("REGEX needs value type Pattern. Type was: " + value.getClass().getName());
    }

    /**
     * Creates a {@link Like} property object based on the provided value.
     *
     * @param value the value to use when creating the property
     * @return the created property
     * @throws IllegalArgumentException if the value type is not a {@link Pattern}
     */
    private static Property<String> createLikeProperty(Object value) {
        if (value instanceof Pattern pattern) {
            return new Like(pattern);
        }
        throw new IllegalArgumentException("LIKE needs value type Pattern. Type was: " + value.getClass().getName());
    }

    /**
     * Creates an {@link Equal} property object based on the provided value.
     *
     * @param value the value to use when creating the property
     * @return the created property
     * @throws IllegalArgumentException if the value type is not a {@link BigDecimal}
     */
    private static Property<BigDecimal> createEqualProperty(Object value) {
        if (value instanceof BigDecimal num) {
            return new Equal(num);
        }
        throw new IllegalArgumentException("EQUAL needs value type BigDecimal. Type was: " + value.getClass().getName());
    }

    /**
     * Creates a {@link GreaterNumeric} property object based on the provided value.
     *
     * @param value the value to use when creating the property
     * @return the created property
     * @throws IllegalArgumentException if the value type is not a {@link BigDecimal}
     */
    private static Property<BigDecimal> createGreaterNumericProperty(Object value) {
        if (value instanceof BigDecimal num) {
            return new GreaterNumeric(num);
        }
        throw new IllegalArgumentException("GREATER_NUMERIC needs value type BigDecimal. Type was: " + value.getClass().getName());
    }

    /**
     * Creates a {@link GreaterDate} property object based on the provided value.
     *
     * @param value the value to use when creating the property
     * @return the created property
     * @throws IllegalArgumentException if the value type is not a {@link LocalDate}
     */
    private static Property<LocalDate> createGreaterDateProperty(Object value) {
        if (value instanceof LocalDate date) {
            return new GreaterDate(date);
        }
        throw new IllegalArgumentException("GREATER_DATE needs value type LocalDate. Type was: " + value.getClass().getName());
    }

    /**
     * Creates a {@link RangeNumeric} property object based on the provided value.
     *
     * @param value the value to use when creating the property
     * @return the created property
     * @throws IllegalArgumentException if the value type is not a {@link BigDecimal[]}
     */
    private static Property<BigDecimal[]> createRangeNumericProperty(Object value) {
        if (value instanceof BigDecimal[] nums) {
            return new RangeNumeric(nums);
        }
        throw new IllegalArgumentException("RANGE_NUMERIC needs value type BigDecimal[]. Type was: " + value.getClass().getName());
    }
}
