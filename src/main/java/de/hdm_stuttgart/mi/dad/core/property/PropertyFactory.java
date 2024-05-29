package de.hdm_stuttgart.mi.dad.core.property;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.regex.Pattern;

public class PropertyFactory {

    public <T extends BigDecimal, Pattern> Property getProperty(PropertyType type, T value) {



        switch (type) {
            case REGEX -> {
                return new Regex((Pattern) value);
            }
            case LIKE -> {
                return new Like((Pattern) value);
            }
            case EQUAL -> {
                return new Equal((BigDecimal) value);
            }
            case GREATERNUMERIC -> {
                return new GreaterNumeric((BigDecimal) value);
            }
            case GREATERDATE -> {
                return new GreaterDate((LocalDate) value);
            }
            case RANGENUMERIC -> {
                return new RangeNumeric((BigDecimal[]) value);
            }
            default -> throw new IllegalStateException("Unexpected value: " + type);
        }
    }
}
