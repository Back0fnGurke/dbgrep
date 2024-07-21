package de.hdm_stuttgart.mi.dad;

import de.hdm_stuttgart.mi.dad.core.entity.ColumnValue;
import de.hdm_stuttgart.mi.dad.core.entity.ColumnValueOutput;
import de.hdm_stuttgart.mi.dad.core.property.PropertyFactory;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Pattern;

import static de.hdm_stuttgart.mi.dad.core.property.PropertyType.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestEntityColumnValueOutput {

    @Test
    public void testRegex() {
        ColumnValueOutput column = new ColumnValueOutput(new ColumnValue("Name", "Mia Winter"),
                List.of(
                        PropertyFactory.createProperty(REGEX, Pattern.compile("Mia.*?"))
                ));
        ColumnValueOutput column2 = new ColumnValueOutput(new ColumnValue("Name", "Mia Winter"),
                List.of(
                        PropertyFactory.createProperty(REGEX, Pattern.compile("Alexa"))
                ));

        assertTrue(column.isMatch());
        assertFalse(column2.isMatch());
    }

    @Test
    public void testLike() {
        ColumnValueOutput column = new ColumnValueOutput(new ColumnValue("Name", "Mia Winter"),
                List.of(
                        PropertyFactory.createProperty(LIKE, Pattern.compile("Mia%"))
                ));
        ColumnValueOutput column2 = new ColumnValueOutput(new ColumnValue("Name", "Mia Winter"),
                List.of(
                        PropertyFactory.createProperty(LIKE, Pattern.compile("M_a%"))
                ));
        ColumnValueOutput column3 = new ColumnValueOutput(new ColumnValue("Name", "Mia Winter"),
                List.of(
                        PropertyFactory.createProperty(LIKE, Pattern.compile("Mia"))
                ));

        assertTrue(column.isMatch());
        assertTrue(column2.isMatch());
        assertFalse(column3.isMatch());
    }

    @Test
    public void testEqual() {
        ColumnValueOutput column = new ColumnValueOutput(new ColumnValue("Age", "25"),
                List.of(
                        PropertyFactory.createProperty(EQUAL, new BigDecimal(25))
                ));
        ColumnValueOutput column2 = new ColumnValueOutput(new ColumnValue("Age", "25"),
                List.of(
                        PropertyFactory.createProperty(EQUAL, new BigDecimal(55))
                ));

        assertTrue(column.isMatch());
        assertFalse(column2.isMatch());
    }

    @Test
    public void testGreaterNumeric() {
        ColumnValueOutput column = new ColumnValueOutput(new ColumnValue("Age", "25"),
                List.of(
                        PropertyFactory.createProperty(GREATERNUMERIC, new BigDecimal(25))
                ));
        ColumnValueOutput column2 = new ColumnValueOutput(new ColumnValue("Age", "25"),
                List.of(
                        PropertyFactory.createProperty(GREATERNUMERIC, new BigDecimal(55))
                ));
        ColumnValueOutput column3 = new ColumnValueOutput(new ColumnValue("Age", "25"),
                List.of(
                        PropertyFactory.createProperty(GREATERNUMERIC, new BigDecimal(15))
                ));

        assertFalse(column.isMatch());
        assertFalse(column2.isMatch());
        assertTrue(column3.isMatch());
    }

    @Test
    public void testGreaterDate() {
        ColumnValueOutput column = new ColumnValueOutput(new ColumnValue("Birthday", "1996-05-25"),
                List.of(
                        PropertyFactory.createProperty(GREATERDATE, LocalDate.of(1996, 5, 20))
                ));
        ColumnValueOutput column2 = new ColumnValueOutput(new ColumnValue("Birthday", "1996-05-25"),
                List.of(
                        PropertyFactory.createProperty(GREATERDATE, LocalDate.of(1996, 2, 25))
                ));
        ColumnValueOutput column3 = new ColumnValueOutput(new ColumnValue("Birthday", "1996-05-25"),
                List.of(
                        PropertyFactory.createProperty(GREATERDATE, LocalDate.of(1896, 5, 25))
                ));
        ColumnValueOutput column4 = new ColumnValueOutput(new ColumnValue("Birthday", "1996-05-25"),
                List.of(
                        PropertyFactory.createProperty(GREATERDATE, LocalDate.of(1996, 5, 25))
                ));
        ColumnValueOutput column5 = new ColumnValueOutput(new ColumnValue("Birthday", "1996-05-25"),
                List.of(
                        PropertyFactory.createProperty(GREATERDATE, LocalDate.of(1996, 12, 25))
                ));


        assertTrue(column.isMatch());
        assertTrue(column2.isMatch());
        assertTrue(column3.isMatch());
        assertFalse(column4.isMatch());
        assertFalse(column5.isMatch());
    }

    @Test
    public void testRangeNumeric() {
        BigDecimal[] range = new BigDecimal[2];
        range[0] = new BigDecimal(20);
        range[1] = new BigDecimal(30);
        ColumnValueOutput column = new ColumnValueOutput(new ColumnValue("Age", "25"),
                List.of(
                        PropertyFactory.createProperty(RANGENUMERIC, range)
                ));
        range[0] = new BigDecimal(30);
        range[1] = new BigDecimal(40);
        ColumnValueOutput column2 = new ColumnValueOutput(new ColumnValue("Age", "25"),
                List.of(
                        PropertyFactory.createProperty(RANGENUMERIC, range)
                ));
        range[0] = new BigDecimal(25);
        range[1] = new BigDecimal(25);
        ColumnValueOutput column3 = new ColumnValueOutput(new ColumnValue("Age", "25"),
                List.of(
                        PropertyFactory.createProperty(RANGENUMERIC, range)
                ));


        assertTrue(column.isMatch());
        assertFalse(column2.isMatch());
        assertTrue(column3.isMatch());
    }
}
