package de.hdm_stuttgart.mi.dad.core.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import de.hdm_stuttgart.mi.dad.core.property.Property;
import java.util.regex.Pattern;

/**
 * storing data of a single row column with the information if it is a match
 *
 */
public class ColumnValueOutput {

    String name;
    String value;
    boolean isMatch;

    public ColumnValueOutput(ColumnValue column, List<Property> properties){
        this.name = column.name();
        this.value = column.value();
        this.isMatch = isMatch(value, properties);
    }

    /**
     * evaluates if the value is a match for the properties
     * @param value a String value
     * @param properties a list with values of type Property
     * @return
     */
    private boolean isMatch(String value, List<Property> properties){
        for (Property property : properties) {
            switch (property.getType()) {
                case REGEX:
                    return Pattern.compile(property.getValue().toString()).matcher(value).matches();
                case LIKE:
                    String regex = property.getValue().toString();
                    regex = regex.replace("_", ".").replace("%", ".*?");
                    Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
                    return p.matcher(value).matches();
                case EQUAL:
                    return property.getValue().equals(new BigDecimal(value));
                case GREATERNUMERIC:
                    return ((BigDecimal) property.getValue()).compareTo(new BigDecimal(value)) < 0;
                case GREATERDATE:
                    return ((LocalDate) property.getValue()).isBefore(LocalDate.parse(value));
                case RANGENUMERIC:
                    final BigDecimal[] range = (BigDecimal[]) property.getValue();
                    return
                            range[0].compareTo(new BigDecimal(value)) <= 0
                                    && range[1].compareTo(new BigDecimal(value)) >= 0;
                default:
                    throw new IllegalArgumentException("Unexpected value: " + property.getType());
            }
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public boolean isMatch() {
        return isMatch;
    }

    public String getValue() {
        return value;
    }
}