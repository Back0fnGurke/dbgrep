package de.hdm_stuttgart.mi.dad.core.property;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.regex.Pattern;

//TODO: doku

public class PropertyFactory {

    private PropertyFactory() {
    }

    public static Property getProperty(PropertyType type, Object value) {
        if (!((value instanceof Pattern) || (value instanceof BigDecimal) || (value instanceof BigDecimal[]) || (value instanceof LocalDate))) {
            throw new IllegalArgumentException("Provided value parameter is not of allowed type. Type was: " + value.getClass().getName());
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
}
