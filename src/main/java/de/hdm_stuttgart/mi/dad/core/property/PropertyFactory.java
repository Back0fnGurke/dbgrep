package de.hdm_stuttgart.mi.dad.core.property;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.regex.Pattern;

public class PropertyFactory {

    private PropertyFactory() {
    }

    public static Property getProperty(PropertyType type, Object value) {
        if (!((value instanceof Pattern) || (value instanceof BigDecimal) || (value instanceof BigDecimal[]) || (value instanceof LocalDate))) {
            throw new IllegalArgumentException("Provided value argument is not of allowed type. Type was: " + value.getClass().getName());
        }

        switch (type) {
            case REGEX -> {
                if (value instanceof Pattern pattern) {
                    return new Regex(pattern);
                }
                throw new IllegalArgumentException("REGEX needs value type Pattern. Type was: " + value.getClass().getName());
            }
            case LIKE -> {
                if (value instanceof Pattern pattern) {
                    return new Like(pattern);
                }
                throw new IllegalArgumentException("LIKE needs value type Pattern. Type was: " + value.getClass().getName());
            }
            case EQUAL -> {
                if (value instanceof BigDecimal num) {
                    return new Equal(num);
                }
                throw new IllegalArgumentException("EQUAL needs value type BigDecimal. Type was: " + value.getClass().getName());
            }
            case GREATERNUMERIC -> {
                if (value instanceof BigDecimal num) {
                    return new GreaterNumeric(num);
                }
                throw new IllegalArgumentException("GREATERNUMERIC needs value type BigDecimal. Type was: " + value.getClass().getName());
            }
            case GREATERDATE -> {
                if (value instanceof LocalDate date) {
                    return new GreaterDate(date);
                }
                throw new IllegalArgumentException("GREATERDATE needs value type LocalDate. Type was: " + value.getClass().getName());

            }
            case RANGENUMERIC -> {
                if (value instanceof BigDecimal[] nums) {
                    return new RangeNumeric(nums);
                }
                throw new IllegalArgumentException("RANGENUMERIC needs value type BigDecimal[]. Type was: " + value.getClass().getName());

            }
            default -> throw new IllegalArgumentException("Can not use String value with type: " + type);
        }
    }

//    public static Property<String> getProperty(PropertyType type, Pattern value) {
//        switch (type) {
//            case REGEX -> {
//                return new Regex(value);
//            }
//            case LIKE -> {
//                return new Like(value);
//            }
//            default -> throw new IllegalArgumentException("Can not use String value with type: " + type);
//        }
//    }
//
//    public static Property<BigDecimal> getProperty(PropertyType type, BigDecimal value) {
//        switch (type) {
//            case EQUAL -> {
//                return new Equal(value);
//            }
//            case GREATERNUMERIC -> {
//                return new GreaterNumeric(value);
//            }
//            default -> throw new IllegalArgumentException("Can not use BigDecimal value with type: " + type);
//        }
//    }
//
//    public static Property<LocalDate> getProperty(PropertyType type, LocalDate value) {
//        if (type != PropertyType.GREATERDATE) {
//            throw new IllegalArgumentException("Can not use LocalDate value with type: " + type);
//        }
//
//        return new GreaterDate(value);
//    }
//
//    public static Property<BigDecimal[]> getProperty(PropertyType type, BigDecimal[] value) {
//        if (type != PropertyType.RANGENUMERIC) {
//            throw new IllegalArgumentException("Can not use BigDecimal[] value with type: " + type);
//        }
//
//        return new RangeNumeric(value);
//    }
}
