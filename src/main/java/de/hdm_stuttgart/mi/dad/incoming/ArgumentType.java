package de.hdm_stuttgart.mi.dad.incoming;

import de.hdm_stuttgart.mi.dad.core.property.Property;
import de.hdm_stuttgart.mi.dad.core.property.PropertyFactory;
import de.hdm_stuttgart.mi.dad.core.property.PropertyType;

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
                return PropertyFactory.getProperty(PropertyType.LIKE, stringValue);
            }
            case EQUAL -> {
                return PropertyFactory.getProperty(PropertyType.EQUAL, stringValue);
            }
            case GREATER -> {
                return PropertyFactory.getProperty(PropertyType.GREATER_DATE, stringValue);
            }
            case REGEX -> {
                return PropertyFactory.getProperty(PropertyType.REGEX, stringValue);
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
