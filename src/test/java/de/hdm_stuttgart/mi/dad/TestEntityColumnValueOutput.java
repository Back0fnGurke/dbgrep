package de.hdm_stuttgart.mi.dad;

import de.hdm_stuttgart.mi.dad.core.entity.ColumnValueOutput;
import de.hdm_stuttgart.mi.dad.core.property.Property;
import de.hdm_stuttgart.mi.dad.core.property.PropertyFactory;
import org.checkerframework.checker.regex.qual.Regex;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import static de.hdm_stuttgart.mi.dad.core.property.PropertyType.*;
import static org.junit.jupiter.api.Assertions.*;

public class TestEntityColumnValueOutput {

    @Test
    public void testRegex(){
        ColumnValueOutput column = new ColumnValueOutput("Name", "Mia Winter",
                List.of(
                        PropertyFactory.getProperty(REGEX, Pattern.compile("Mia.*?"))
                ));
        ColumnValueOutput column2 = new ColumnValueOutput("Name", "Mia Winter",
                List.of(
                        PropertyFactory.getProperty(REGEX, Pattern.compile("Alexa"))
                ));

        assertTrue(column.isMatch());
        assertFalse(column2.isMatch());
    }

    @Test
    public void testLike(){
        ColumnValueOutput column = new ColumnValueOutput("Name", "Mia Winter",
                List.of(
                        PropertyFactory.getProperty(LIKE, Pattern.compile("Mia%"))
                ));
        ColumnValueOutput column2 = new ColumnValueOutput("Name", "Mia Winter",
                List.of(
                        PropertyFactory.getProperty(LIKE, Pattern.compile("M_a%"))
                ));
        ColumnValueOutput column3 = new ColumnValueOutput("Name", "Mia Winter",
                List.of(
                        PropertyFactory.getProperty(LIKE, Pattern.compile("Mia"))
                ));

        assertTrue(column.isMatch());
        assertTrue(column2.isMatch());
        assertFalse(column3.isMatch());
    }

    @Test
    public void testEqual(){
        ColumnValueOutput column = new ColumnValueOutput("Age", "25",
                List.of(
                        PropertyFactory.getProperty(EQUAL, new BigDecimal(25))
                ));
        ColumnValueOutput column2 = new ColumnValueOutput("Age", "25",
                List.of(
                        PropertyFactory.getProperty(EQUAL, new BigDecimal(55))
                ));

        assertTrue(column.isMatch());
        assertFalse(column2.isMatch());
    }
}
