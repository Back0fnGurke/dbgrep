package de.hdm_stuttgart.mi.dad.core.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import de.hdm_stuttgart.mi.dad.core.property.Property;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Record storing data of a single row column
 *
 * @param name  the name of the column
 * @param value value of the column
 */
public record ColumnValueOutput(String name, String value, List<Property> properties) {

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
}