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
        for (int j = 0; j < properties.size(); j++) {
            Property property = properties.get(j);
            switch (property.getType()) {
                case REGEX: return ((Pattern) property.getValue()).matcher(value).find();break;
                case LIKE: ;
                case EQUAL: return ((String) property.getValue()).equals(value);break;
                case GREATERNUMERIC: return ((BigDecimal) property.getValue()).compareTo(new BigDecimal(value)) > 0 ;break;
                case GREATERDATE: ;return ((LocalDate) property.getValue()).isAfter(LocalDate.parse(value));break;
                case RANGENUMERIC:
                    final BigDecimal[] range = (BigDecimal[]) property.getValue();
                    return
                        range[0].compareTo(new BigDecimal(value)) > 0
                                && range[1].compareTo(new BigDecimal(value)) < 0;break; ;
                default: throw new IllegalArgumentException("Unexpected value: " + properties.get(j).getType());
            }
        }
    }
}