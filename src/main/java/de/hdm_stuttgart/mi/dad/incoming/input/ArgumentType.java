package de.hdm_stuttgart.mi.dad.incoming.input;

import de.hdm_stuttgart.mi.dad.core.property.Property;
import de.hdm_stuttgart.mi.dad.core.property.PropertyType;
import de.hdm_stuttgart.mi.dad.core.property.properties.PropertyFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Argument types that the user can use. They always start with “--”.
 */
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

    /**
     * Validates the string value for the property, parses it to the correct type and creates the property.
     *
     * @param argumentType argument type of the argument
     * @param stringValue  value of the argument as string
     * @return created property
     */
    public static Property<?> createPropertyFromArgumentType(ArgumentType argumentType, String stringValue) {
        switch (argumentType){
            case LIKE -> {
                return createPropertyWithPattern(LIKE, PropertyType.LIKE, stringValue);
            }
            case EQUAL -> {
                return createPropertyWithBigDecimal(EQUAL, PropertyType.EQUAL, stringValue);
            }
            case GREATER -> {
                try {
                    LocalDate date = LocalDate.parse(stringValue);
                    return PropertyFactory.createProperty(PropertyType.GREATER_DATE, date);
                } catch (DateTimeParseException e1){
                    try {
                       return createPropertyWithBigDecimal(GREATER, PropertyType.GREATER_NUMERIC, stringValue);
                    } catch (IllegalArgumentException e2){
                        throw new IllegalArgumentException(GREATER.argumentString + " argument " + stringValue + " is neither a date nor a number.");
                    }
                }
            }
            case REGEX -> {
                return createPropertyWithPattern(REGEX, PropertyType.REGEX, stringValue);
            }
            case RANGE -> {
                String[] rangeNumberStrings = stringValue.split(",");
                if (rangeNumberStrings.length != 2){
                    throw new IllegalArgumentException(RANGE.argumentString + " can only have two numbers as arguments not " + rangeNumberStrings.length
                            + ". You can separate numbers through ','.");
                }
                try {
                    BigDecimal[] rangeNumbers = new BigDecimal[2];
                    rangeNumbers[0] = new BigDecimal(rangeNumberStrings[0]);
                    rangeNumbers[1] = new BigDecimal(rangeNumberStrings[1]);
                    return PropertyFactory.createProperty(PropertyType.RANGE_NUMERIC, rangeNumbers);
                } catch (NumberFormatException e){
                    throw new IllegalArgumentException(RANGE.argumentString + " numbers " + rangeNumberStrings[0] + " or " + rangeNumberStrings[1] +
                            " has wrong format. You have to separate the numbers through ','. Example: '--range 2.55,5.67'");
                }
            }
            default -> throw new IllegalArgumentException();
        }
    }

    /**
     * Validates the string value for the property that uses big decimal, parses it to BigDecimal and creates the property of property type.
     *
     * @param argumentType is used for exception message
     * @param propertyType  property type of the created property
     * @param stringValue  value of the argument as string
     * @return created property
     */
    private static Property<?> createPropertyWithBigDecimal(ArgumentType argumentType, PropertyType propertyType, String stringValue){
        try {
            BigDecimal value = new BigDecimal(stringValue);
            return PropertyFactory.createProperty(propertyType, value);
        } catch (NumberFormatException e){
            throw new IllegalArgumentException(argumentType.argumentString + " number " + stringValue + " is invalid. Example of valid number: 13.554");
        }
    }

    /**
     * Validates the string value for the property that uses pattern like regex and like, parses it to Pattern and creates the property of property type.
     *
     * @param argumentType is used for exception message
     * @param propertyType  property type of the created property
     * @param stringValue  value of the argument as string
     * @return created property
     */
    private static Property<?> createPropertyWithPattern(ArgumentType argumentType, PropertyType propertyType, String stringValue){
        try {
            Pattern value = Pattern.compile(stringValue);
            return PropertyFactory.createProperty(propertyType, value);
        } catch (PatternSyntaxException e) {
            throw new IllegalArgumentException(argumentType.argumentString + " pattern " + stringValue + " is invalid.");
        }
    }

    @Override
    public String toString() {
        return argumentString;
    }
}
