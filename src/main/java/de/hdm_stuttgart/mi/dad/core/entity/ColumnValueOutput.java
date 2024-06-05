package de.hdm_stuttgart.mi.dad.core.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import de.hdm_stuttgart.mi.dad.core.property.Property;
import java.util.regex.Pattern;

/**
 * storing data of a single row column with information if it is a match
 *
 */
public class ColumnValueOutput {

    String name;
    String value;
    boolean isMatch;

    public ColumnValueOutput(String name, String value, List<Property> properties){
        this.name = name;
        this.value = value;
        this.isMatch = isMatch(value, properties);
    }

    private boolean isMatch(String value, List<Property> properties){
        for (Property property : properties) {
            switch (property.getType()) {
                case REGEX:
                    return ((Pattern) property.getValue()).matcher(value).find();
                case LIKE:
                    String regex = property.toString();
                    regex = regex.replace("_", ".").replace("%", ".*?");
                    Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
                    return p.matcher(value).matches();
                case EQUAL:
                    return property.getValue().equals(value);
                case GREATERNUMERIC:
                    return ((BigDecimal) property.getValue()).compareTo(new BigDecimal(value)) > 0;
                case GREATERDATE:
                    return ((LocalDate) property.getValue()).isAfter(LocalDate.parse(value));
                case RANGENUMERIC:
                    final BigDecimal[] range = (BigDecimal[]) property.getValue();
                    return
                            range[0].compareTo(new BigDecimal(value)) > 0
                                    && range[1].compareTo(new BigDecimal(value)) < 0;
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