package de.hdm_stuttgart.mi.dad.incoming;

import de.hdm_stuttgart.mi.dad.core.property.Property;
import de.hdm_stuttgart.mi.dad.core.property.PropertyFactory;
import de.hdm_stuttgart.mi.dad.core.property.PropertyType;

import java.math.BigDecimal;
import java.util.regex.Pattern;

public enum ArgumentType {
    PROFILE("--profile", false),

    COLUMN("--column", false),
    TABLE("--table", false),

    EQUAL("--equal", true),
    LIKE("--like", true),
    GREATER("--greater", true),
    RANGE("--range", true),
    REGEX("--regex", true),

    HELP("--help", false);

    public final String argumentString;
    public final boolean isProperty;

    ArgumentType(String argumentString, boolean isProperty){
        this.argumentString = argumentString;
        this.isProperty = isProperty;
    }

    public static Property createPropertyFromArgumentType(ArgumentType argumentType, String stringValue) {
        switch (argumentType){
            case LIKE -> {
                Pattern likeValue = Pattern.compile(stringValue);
                return PropertyFactory.getProperty(PropertyType.LIKE, likeValue);
            }
            case EQUAL -> {
                BigDecimal equalValue = new BigDecimal(stringValue);
                return PropertyFactory.getProperty(PropertyType.EQUAL, equalValue);
            }
            case GREATER -> {
                BigDecimal greaterValue = new BigDecimal(stringValue);
                return PropertyFactory.getProperty(PropertyType.GREATER_DATE, greaterValue);
            }
            case REGEX -> {
                Pattern regexValue = Pattern.compile(stringValue);
                return PropertyFactory.getProperty(PropertyType.REGEX, regexValue);
            }
            case RANGE -> {
                return PropertyFactory.getProperty(PropertyType.RANGE_NUMERIC, stringValue);
            }
            default -> throw new IllegalArgumentException();

        }
    }

    @Override
    public String toString() {
        return argumentString;
    }
}
